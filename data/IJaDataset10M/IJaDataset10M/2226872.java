package com.google.code.drift.api;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import com.google.code.drift.R;
import com.google.code.drift.utils.DLog;

public class AppSkyApp extends Application {

    private UserInfo myInfo = new UserInfo();

    private Handler progressAndToastHandler;

    private String ip = "";

    private boolean autoRefresh = true;

    private boolean chatRing = false;

    private boolean chatVibrator = true;

    private boolean myLocationIsChange = false;

    @Override
    public void onCreate() {
        super.onCreate();
        ip = getString(R.string.server_ip);
        DLog.i("Application启动！");
    }

    private boolean loginState = false;

    /**
	 * 是否已登录
	 */
    public boolean isLogin() {
        return loginState;
    }

    public void setLoginState(boolean loginState) {
        this.loginState = loginState;
    }

    public String getIp() {
        return this.ip;
    }

    /**
	 * 当有消息时，是否自动当前页面刷新
	 * 
	 * @return
	 */
    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    /**
	 * 设置是否自动刷新
	 * 
	 * @param isAuto
	 */
    public void setAutoRefresh(boolean isAuto) {
        this.autoRefresh = isAuto;
    }

    public boolean isChatRing() {
        return chatRing;
    }

    public void setChatRing(boolean flag) {
        chatRing = flag;
    }

    public boolean isChatVibrator() {
        return chatVibrator;
    }

    public void setChatVibrator(boolean chatVibrator) {
        this.chatVibrator = chatVibrator;
    }

    public void setLocationChange() {
        this.myLocationIsChange = true;
    }

    public boolean isLocationChange() {
        boolean flag = this.myLocationIsChange;
        this.myLocationIsChange = false;
        return flag;
    }

    public UserInfo getMyInfo() {
        return myInfo;
    }

    public void setUpdateUIHandler(Handler handler) {
        progressAndToastHandler = handler;
    }

    public void sendMessageToHandler(Message msg) {
        if (progressAndToastHandler != null) {
            progressAndToastHandler.sendMessage(msg);
        }
    }

    public void sendEmptyMessageToHandler(int what) {
        if (progressAndToastHandler != null) {
            progressAndToastHandler.sendEmptyMessage(what);
        }
    }
}
