package com.hcs.activity.tabmenu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.hcs.R;
import com.hcs.application.ActivityContainerApp;
import com.hcs.protocol.ISetUpService;
import com.hcs.protocol.impl.SetUpService;
import com.hcs.protocol.model.Version;
import com.hcs.protocol.utils.ProtocolContanst;
import com.hcs.utils.HttpDownloader;
import com.hcs.utils.InfoConstants;
import com.hcs.utils.UseDialog;

/**
 * 帮助中心界面功能：本界面显示帮助内容
 * 该Activity目前提供一个功能：检查新版本
 * 实现步骤：
 * 		1、监听到用户点击“检测新版本”按钮事件
 * 		2、系统获取当前工程的版本号，并把版本号通过协议发往服务器，
 * 		        如果与服务器上的版本号不一致，服务器会返回一个新版本下载
 * 		        的相对路径，并继续执行下述步骤，如果一致服务器返回空，提
 * 		        示用户已是最新版本并停止执行以下步骤
 * 		3、 系统弹出对话框提示用户是否需要更新如果用户点是，继续执行
 * 		4、 根据服务器返回的新版本下载路径，下载新版本APK
 * 		5、执行新版本APK安装
 * @author zhaoxu
 * @version 0.0.1
 * @createTime 2011/5/15 16:20
 */
public class GoHelpAct extends Activity {

    /** loading提示框 */
    private ProgressDialog proDialog;

    /** 错误标识 */
    private int errorState;

    /** SD卡路径 */
    private String SDHCSPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hcs/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ActivityContainerApp) getApplication()).setLayer1(this);
        setContentView(R.layout.main_list);
        ListView list = (ListView) findViewById(R.id.ListView01);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("ItemImage", R.drawable.h1);
        map1.put("ItemText", "检查新版本");
        listItem.add(map1);
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.main_item, new String[] { "ItemImage", "ItemText" }, new int[] { R.id.ItemImage, R.id.ItemText });
        list.setAdapter(listItemAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch(arg2) {
                    case 0:
                        ISetUpService setUpService = new SetUpService();
                        String versionName = getVersionName();
                        if (null != versionName && !"".equals(versionName)) {
                            Version version = setUpService.updateVersion(versionName);
                            if (null == version) {
                                Toast.makeText(GoHelpAct.this, InfoConstants.CONNECT_FAIL, Toast.LENGTH_LONG).show();
                            } else if (version.getIsNewVersion() == 1) {
                                showAPKDownLoadDialog(version.getVersionURL());
                            } else {
                                Toast.makeText(GoHelpAct.this, InfoConstants.LAST_VERSION, Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(this).setMessage("确定退出系统吗？").setIcon(R.drawable.icon).setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                }
            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                }
            }).show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ActivityContainerApp) getApplication()).setLayer1(null);
    }

    /**下载APK的线程*/
    class updateVersionHandler implements Runnable {

        private String path;

        public updateVersionHandler(String path) {
            this.path = path;
        }

        @Override
        public void run() {
            int result = 0;
            HttpDownloader httpDownloader = new HttpDownloader();
            result = httpDownloader.downFile(ProtocolContanst.DOWNLOAD_APK_URL + path, "hcs/", "Hcs.apk");
            if (result == 1) {
                installApk(SDHCSPATH + "Hcs.apk");
                proDialog.dismiss();
            } else {
                proDialog.dismiss();
                doHandle(1);
            }
        }
    }

    /**通过调用handler来通知UI主线程更新UI*/
    private void doHandle(int errorState) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("errorState", errorState);
        message.setData(bundle);
        messageHandler.sendMessage(message);
    }

    /** 登录后台通知更新UI线程,主要用于显示修改密状况,通知UI线程更新界面 */
    Handler messageHandler = new Handler() {

        public void handleMessage(Message msg) {
            errorState = msg.getData().getInt("errorState");
            if (proDialog != null) {
                proDialog.dismiss();
            }
            if (0 == errorState) {
                Toast.makeText(GoHelpAct.this, InfoConstants.UPDATEVER_SUCCESS, Toast.LENGTH_LONG).show();
            } else if (1 == errorState) {
                Toast.makeText(GoHelpAct.this, InfoConstants.CONNECT_FAIL, Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
	 * 安装APK 
	 * @param filePath APK路径
	 */
    private void installApk(String filePath) {
        File f = new File(filePath);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
	 * 获取当前APK版本号
	 * @return  当前APK版本号
	 */
    private String getVersionName() {
        String pName = "com.hcs";
        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(pName, PackageManager.GET_CONFIGURATIONS);
            String versionName = pinfo.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 显示是否要下载的对话框
	 * 	@param path APK相对路径 
	 */
    private void showAPKDownLoadDialog(final String path) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final UseDialog ud = new UseDialog();
        builder.setIcon(R.drawable.icon);
        builder.setTitle("提示");
        builder.setMessage("检查到新版本，是否进行版本更新？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                proDialog = ProgressDialog.show(GoHelpAct.this, InfoConstants.DOWNLOAD_TITLE, InfoConstants.DOWNLOAD_INFO, true, true);
                proDialog.setIcon(R.drawable.icon);
                Thread thread = new Thread(new updateVersionHandler(path));
                thread.start();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }
}
