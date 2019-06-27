package com.example.administrator.testz;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;

/**
 * Created by wrs on 2019/6/27,18:58
 * projectName: AppUpdateUtil-master
 * packageName: com.maiml.updatedemo
 */

public class MainActivity extends AppCompatActivity {
    // 下载的路径：path:/storage/emulated/0/TestApk/test.apk
    //注意动态权限申请，不然会报错，我这里没做 我是直接开启的存储权限
    final String url = "https://raw.githubusercontent.com/wrs13634194612/Image/master/raw/test.apk";
    String apkName = "test.apk";
    final String FILE_DIR = "TestApk";
    String apkPath = Environment.getExternalStoragePublicDirectory(FILE_DIR) + "/" + apkName;
    ProgressDialog progressDialog;
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initProcess();  //初始化进度条
        new Thread(new MyRunnable()).start();   //开启子线程下载
    }

    private void initProcess() {
        //ProgressDialog.show(this, "标题", "内容");
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("标题");
        progressDialog.setMessage("提示信息");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false); // 能够返回
        progressDialog.setCanceledOnTouchOutside(false); // 点击外部返回
        progressDialog.setMax(100);
        //progressDialog.setIndeterminate(true);
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        progressDialog.show();
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            final File saveFile = new File(apkPath);
            HttpRequest.download(url, saveFile, new FileDownloadCallback() {
                //开始下载
                @Override
                public void onStart() {
                    super.onStart();
                    System.out.println("start");

                }

                //下载进度
                @Override
                public void onProgress(int progress, long networkSpeed) {
                    super.onProgress(progress, networkSpeed);
                    System.out.println("process:" + progress);
                    progressDialog.setProgress(progress);
                    //String speed = FileUtils.generateFileSize(networkSpeed);
                }

                //下载失败
                @Override
                public void onFailure() {
                    super.onFailure();
                    System.out.println("fail");
                    Toast.makeText(getBaseContext(), "下载失败", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                //下载完成（下载成功）
                @Override
                public void onDone() {
                    super.onDone();
                    System.out.println("success");
                    Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }


}