package org.terukusu.ahoomsgr.view;

import java.io.IOException;
import java.util.List;
import org.terukusu.ahoomsgr.Buddy;
import org.terukusu.ahoomsgr.BuddyList;
import org.terukusu.ahoomsgr.Constants;
import org.terukusu.ahoomsgr.Conversation;
import org.terukusu.ahoomsgr.Message;
import org.terukusu.ahoomsgr.R;
import org.terukusu.ahoomsgr.Session;
import org.terukusu.ahoomsgr.SessionAdapter;
import org.terukusu.ahoomsgr.SessionEvent;
import org.terukusu.ahoomsgr.User;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Teruhiko Kusunoki&lt;<a
 *         href="teru.kusu@gmail.com">teru.kusu@gmail.com</a>&gt;
 *
 */
public class YmsgService extends Service implements Constants {

    private Session session;

    private final Handler handler = new Handler();

    private final IBinder binder = new LocalBinder();

    private String currentRecipientId;

    public class LocalBinder extends Binder {

        YmsgService getService() {
            return YmsgService.this;
        }
    }

    /**
     * 自分のステータスメッセージを変更します。
     * @param status ステータスコード
     * @param statusMessage ステータスメッセージ
     */
    public void changeStatus(int status, String statusMessage) {
        Session session = getSession();
        if (session != null) {
            session.changeStatus(status, statusMessage);
        }
    }

    /**
     * ログアウトします
     */
    public void logout() {
        Session session = getSession();
        if (session != null) {
            try {
                session.close();
            } catch (Exception ignored) {
            }
            session = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        Session session = getSession();
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                Log.v(LOG_TAG, "ymsg session close failed.", e);
            }
        }
        super.onDestroy();
    }

    /**
     * session を設定します。
     *
     * @param session セットする session
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     *
     * @return
     */
    protected Session getSession() {
        return this.session;
    }

    /**
     * ログインします。
     *
     * @param id ID
     * @param password パスワード
     * @param hidden ログイン状態を隠す場合は true
     */
    public void login(String id, String password, boolean hidden) {
        Session session = getSession();
        if (session != null) {
            try {
                session.close();
            } catch (Exception ignored) {
            }
        }
        session = new Session();
        session.addSessionListener(new MySessionListener());
        setSession(session);
        session.login(id, password, hidden);
    }

    public void sendMessage(Message message) {
        getSession().sendMessage(message);
    }

    public BuddyList getBuddyList() {
        return getSession().getBuddyList();
    }

    /**
     * 会話を取得します。
     *
     * @param senderId 送信に使用するID
     * @param recipientId 会話相手のID
     * @param create 会話が存在しなければ新しく作る場合は true
     * @return 会話です
     */
    public synchronized Conversation getConversation(String senderId, String recipientId, boolean create) {
        Conversation c = getSession().getConversation(senderId, recipientId, create);
        return c;
    }

    protected class MySessionListener extends SessionAdapter {

        /**
         * 新しいオブジェクトを生成します。
         *
         */
        public MySessionListener() {
            super();
        }

        @Override
        public void onLogin(SessionEvent event) {
            Intent i = new Intent(ACTION_LOGIN);
            sendBroadcast(i);
        }

        @Override
        public void onMessageSent(SessionEvent event) {
            Intent i = new Intent(ACTION_MESSAGE_SENT);
            sendBroadcast(i);
        }

        @Override
        public void onLoginFailure(SessionEvent event) {
            Intent i = new Intent(ACTION_LOGIN_FAILED);
            sendBroadcast(i);
        }

        @Override
        public void onBuddyLogin(SessionEvent event) {
            Buddy b = (Buddy) event.getParam("buddy");
            Intent intent = new Intent(ACTION_BUDDY_ONLINE);
            intent.putExtra("buddy", b);
            sendBroadcast(intent);
            final String msg = b.getId() + getText(R.string.sysmsg_buddy_login);
            getHandler().post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(YmsgService.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onBuddyLogout(SessionEvent event) {
            Buddy b = (Buddy) event.getParam("buddy");
            Intent intent = new Intent(ACTION_BUDDY_OFFLINE);
            intent.putExtra("buddy", b);
            sendBroadcast(intent);
            final String msg = b.getId() + getText(R.string.sysmsg_buddy_logoff);
            getHandler().post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(YmsgService.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onBuddyStatusChange(SessionEvent event) {
            Buddy b = (Buddy) event.getParam("buddy");
            Intent intent = new Intent(ACTION_BUDDY_STATUS_CHANGE);
            intent.putExtra("buddy", b);
            sendBroadcast(intent);
        }

        @Override
        public void onLogout(SessionEvent event) {
            super.onLogout(event);
        }

        @Override
        public void onChangeStatusMessage(SessionEvent event) {
            Intent intent = new Intent(ACTION_MY_STATUS_CHANGE);
            sendBroadcast(intent);
        }

        @Override
        public void onChangeStatusMessageFailure(SessionEvent event) {
            Log.e(LOG_TAG, "onChangeStatusMessageFailure() called: cause=" + event.getParam("exception"));
            super.onChangeStatusMessageFailure(event);
        }

        @Override
        public void onDisconnect(SessionEvent event) {
            final String msg = getText(R.string.app_name).toString() + getText(R.string.sysmsg_disconnect).toString();
            getHandler().post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(YmsgService.this, msg, Toast.LENGTH_LONG).show();
                }
            });
            Intent intent = new Intent(ACTION_DISCONNECT);
            sendBroadcast(intent);
        }

        @Override
        public void onSendMessageFailure(SessionEvent event) {
            super.onSendMessageFailure(event);
        }

        @Override
        public void onMessageReceived(SessionEvent event) {
            Message m = (Message) event.getParam("message");
            String recipientId = getCurrentRecipientId();
            if (recipientId == null || !recipientId.equals(m.getSenderId())) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent notifyIntent = new Intent(YmsgService.this, BuddyListActivity.class);
                notifyIntent.putExtra("notifyId", R.string.app_name);
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(YmsgService.this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new Notification(android.R.drawable.stat_notify_chat, m.getSenderId() + getText(R.string.notify_message_received), System.currentTimeMillis());
                notification.defaults |= Notification.DEFAULT_ALL;
                notification.defaults |= Notification.DEFAULT_LIGHTS;
                notification.setLatestEventInfo(getApplicationContext(), getText(R.string.app_name), m.getSenderId() + getText(R.string.notify_message_received), pendingIntent);
                notificationManager.notify(notifyIntent.getIntExtra("notifyId", -1), notification);
            }
            Intent intent = new Intent(ACTION_MESSAGE_RECEIVED);
            intent.putExtra("message", m);
            sendBroadcast(intent);
        }
    }

    public boolean isLogin() {
        return (getSession() != null && getSession().isLogin());
    }

    /**
     * handler を取得します。
     *
     * @return handler
     */
    protected Handler getHandler() {
        return handler;
    }

    protected User getUser() {
        return getSession().getUser();
    }

    /**
     * currentRecipientId を取得します。
     * @return currentRecipientId
     */
    protected String getCurrentRecipientId() {
        return currentRecipientId;
    }

    /**
     * 会話リストを取得します。
     * @return 会話リスト
     */
    public List<Conversation> getConversationList() {
        if (isLogin()) {
            return getSession().getConversationList();
        }
        return null;
    }

    /**
     * currentRecipientId を設定します。
     * @param currentRecipientId セットする currentRecipientId
     */
    protected void setCurrentRecipientId(String currentRecipientId) {
        this.currentRecipientId = currentRecipientId;
    }
}
