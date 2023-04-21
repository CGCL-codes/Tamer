package com.hcs.activity;

import com.hcs.service.HeartService;
import com.hcs.service.PollingService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 手机刚唤醒执行的Activity
 * 功能：手机刚唤醒时，马上去从服务器上轮询数据
 * 实现步骤:
 * 		1、获取之前登入的用户明和密码
 * 		2、开启心跳service
 * 		3、开启轮询service
 * @author zhongming.meng
 * @since 2011-06-21
 */
public class ScreenOnWakeUpAct extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        intent.putExtra("userName", intent.getExtras().getString("userName"));
        intent.putExtra("passWord", intent.getExtras().getString("passWord"));
        intent.setClass(this, HeartService.class);
        stopService(intent);
        startService(intent);
        intent.setClass(this, PollingService.class);
        stopService(intent);
        startService(intent);
        finish();
    }
}
