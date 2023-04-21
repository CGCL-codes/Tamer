package com.android.zweibo.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.android.zweibo.bean.Common;
import com.android.zweibo.bean.Task;
import com.android.zweibo.bean.UserInfo;
import com.android.zweibo.ui.IWeiboActivity;
import com.android.zweibo.util.AuthUtil;
import com.android.zweibo.util.JavaScriptInterface;
import com.android.zweibo.util.NetworkUtil;

public class MainService extends Service implements Runnable {

    private static Queue<Task> taskList = new LinkedList<Task>();

    private static List<Activity> activityList = new ArrayList<Activity>();

    private boolean isRun;

    public static UserInfo nowUser;

    private static Weibo weibo;

    static {
        System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        weibo = new Weibo();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            IWeiboActivity activity = null;
            switch(msg.what) {
                case Common.USER_LOGIN:
                    activity = (IWeiboActivity) getActivityByName("LoginActivity");
                    activity.refresh(msg.obj);
                    break;
                case Common.AUTHORIZE:
                    activity = (IWeiboActivity) getActivityByName("WebViewActivity");
                    activity.refresh();
                    break;
                case Common.ACCESS_TOKEN:
                    activity = (IWeiboActivity) getActivityByName("AccessTokenActivity");
                    activity.refresh(Common.ACCESS_TOKEN, msg.obj);
                    break;
                case Common.GET_ICON_NAME:
                    activity = (IWeiboActivity) getActivityByName("AccessTokenActivity");
                    activity.refresh(Common.GET_ICON_NAME, msg.obj);
                    break;
                case Common.GET_FRIEND_TIMELINE:
                    activity = (IWeiboActivity) getActivityByName("HomeActivity");
                    activity.refresh(msg.obj);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onCreate() {
        isRun = true;
        System.out.println("-----thread");
        Thread thread = new Thread(this);
        thread.start();
    }

    ;

    @Override
    public void run() {
        while (isRun) {
            if (!taskList.isEmpty()) {
                doTask();
            }
        }
    }

    /**
	 * 执行任务
	 */
    private void doTask() {
        Task t = taskList.poll();
        Message msg = handler.obtainMessage();
        msg.what = t.getTaskID();
        switch(t.getTaskID()) {
            case Common.USER_LOGIN:
                {
                    nowUser = (UserInfo) t.getTaskParam().get("user");
                    weibo.setToken(nowUser.getToken(), nowUser.getTokenSecret());
                }
                break;
            case Common.AUTHORIZE:
                msg.obj = AuthUtil.getAuthorizationURL();
                break;
            case Common.ACCESS_TOKEN:
                {
                    String pin = null;
                    AccessToken accessToken = null;
                    while (null == pin) {
                        pin = JavaScriptInterface.PIN;
                    }
                    while (null == accessToken) {
                        accessToken = AuthUtil.getAccessToken(pin);
                    }
                    msg.obj = accessToken;
                }
                break;
            case Common.GET_ICON_NAME:
                {
                    try {
                        UserInfo userInfo = (UserInfo) t.getTaskParam().get("user");
                        weibo.setToken(userInfo.getToken(), userInfo.getTokenSecret());
                        User user = weibo.showUser(userInfo.getUserId());
                        Drawable userIcon = NetworkUtil.getUserIcon(user.getProfileImageURL());
                        userInfo.setUserName(user.getName());
                        userInfo.setUserIcon(userIcon);
                        msg.obj = userInfo;
                    } catch (WeiboException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Common.GET_FRIEND_TIMELINE:
                {
                    Paging page = new Paging(1, 20);
                    try {
                        List<Status> status = weibo.getFriendsTimeline(page);
                        msg.obj = status;
                    } catch (WeiboException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        handler.sendMessage(msg);
    }

    /**
	 * 添加任务到任务队列
	 * @param task
	 */
    public static void newTask(Task task) {
        taskList.add(task);
    }

    /**
	 * 添加activity
	 * @param activity
	 */
    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 根据activity名称获取acitivity对象
     * @param activityName
     * @return
     */
    public Activity getActivityByName(String activityName) {
        if (!activityList.isEmpty()) {
            for (Activity activity : activityList) {
                if (null != activity) {
                    if (activity.getClass().getName().indexOf(activityName) > 0) return activity;
                }
            }
        }
        return null;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
