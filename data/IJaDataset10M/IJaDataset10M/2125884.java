package org.wi.ctrl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wi.R;
import org.wi.Wi;
import org.wi.adapter.RRFriendAdapter;
import org.wi.ctrl.service.AutoSyncService;
import org.wi.db.DBFriend;
import org.wi.entity.CtrlData;
import org.wi.entity.Friend;
import org.wi.entity.Preference;
import org.wi.entity.RRFriend;
import org.wi.entity.UserInfo;
import org.wi.etc.Constants;
import org.wi.etc.Global;
import org.wi.etc.Util;
import org.wi.etc.Constants.RENREN_USER_FIELDS;
import org.wi.ui.MainActivity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.RequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.view.RenrenDialogListener;

/**
 * Only support getInstance() to create one in factory mode.
 * @author GorebillChaos
 *
 */
public class MainController {

    private static final String TAG = "MainController";

    private static MainController mainController = null;

    private Renren renren;

    private AsyncRenren asyncRenren;

    private Wi mainUi;

    private StorageCenter storageCenter = null;

    private ImageLoader imageLoader = null;

    private Synchronizer synchronizer = null;

    public static MainController getInstance(Wi context) {
        mainController = new MainController(context);
        Global.initialize();
        return mainController;
    }

    public static MainController getInstance() {
        if (null == mainController) return null; else return mainController;
    }

    public Wi getMainUi() {
        return mainUi;
    }

    /**
	 * Constructor, should only be called by system.
	 * @param context
	 */
    public MainController(Wi context) {
        mainUi = context;
        this.renren = new Renren(context, Constants.API_KEY, Constants.API_SECRECT);
        this.asyncRenren = new AsyncRenren(this.renren);
        this.storageCenter = StorageCenter.getInstance(context);
        this.imageLoader = ImageLoader.getInstance(context);
        this.synchronizer = new Synchronizer(context);
    }

    public Renren getRenren() {
        return this.renren;
    }

    /**
	 * Null params to ignore.
	 * @param isAutoSync
	 * @return List<Dictionary<String, String>>
	 */
    public List<Dictionary<String, String>> getDBFriend(Boolean isAutoSync) {
        List<Dictionary<String, String>> list = new ArrayList<Dictionary<String, String>>();
        ContentResolver resolver = mainUi.getContentResolver();
        Cursor cursor = resolver.query(DBFriend.CONTENT_URI, null, null, null, null);
        if (cursor.isBeforeFirst() && cursor.moveToFirst()) {
            do {
                Dictionary<String, String> accDict = new Hashtable<String, String>();
                String allAccount = cursor.getString(cursor.getColumnIndex(DBFriend.ACCOUNT_ID));
                String preferences = cursor.getString(cursor.getColumnIndex(DBFriend.PREFERENCE));
                String[] accounts = allAccount.split(DBFriend.DEFAULT_DIVIDER);
                Preference pref = new Preference(preferences);
                for (String item : accounts) {
                    String key = item.substring(0, item.indexOf('='));
                    String value = item.substring(item.indexOf('=') + 1);
                    accDict.put(key, value);
                }
                if (null != isAutoSync) {
                    if (isAutoSync && pref.autoSync.equals("true")) {
                        list.add(accDict);
                    } else if (!isAutoSync && pref.autoSync.equals("false")) {
                        list.add(accDict);
                    }
                } else {
                    list.add(accDict);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Dictionary<String, String> getDBFriend(String id) {
        ContentResolver resolver = mainUi.getContentResolver();
        String where = DBFriend._ID + "=" + id;
        Cursor cursor = resolver.query(DBFriend.CONTENT_URI, null, where, null, null);
        if (cursor.isBeforeFirst() && cursor.moveToFirst()) {
            Dictionary<String, String> accDict = new Hashtable<String, String>();
            String allAccount = cursor.getString(cursor.getColumnIndex(DBFriend.ACCOUNT_ID));
            String[] accounts = allAccount.split(DBFriend.DEFAULT_DIVIDER);
            for (String item : accounts) {
                String key = item.substring(0, item.indexOf('='));
                String value = item.substring(item.indexOf('=') + 1);
                accDict.put(key, value);
            }
            cursor.close();
            return accDict;
        } else {
            return null;
        }
    }

    /**
	 * It only takes RRFriend's id, others are invalid.
	 * @param rrf
	 */
    public void syncToContacts(RRFriend rrf) {
        synchronizer.syncToContacts(rrf);
    }

    public void syncToContacts(final List<?> friends, final Handler handler) {
        int count = 0, total = friends.size();
        Message msg = null;
        if (null != handler) {
            msg = handler.obtainMessage();
            msg.arg1 = 0;
            msg.arg2 = total;
            handler.sendMessage(msg);
        }
        for (Object obj : friends) {
            if (obj instanceof RRFriend) {
                Log.w("MainController::syncTocontacts", "synchroning a RRFriend");
                RRFriend rrf = (RRFriend) obj;
                syncToContacts(rrf);
                count++;
                if (null != handler) {
                    msg = handler.obtainMessage();
                    msg.arg1 = count;
                    msg.arg2 = total;
                    handler.sendMessage(msg);
                }
            } else {
                Log.w("MainController::syncTocontacts", "Type not supported.");
            }
        }
    }

    /**
	 * Get specified user info by UID.
	 * @param callback
	 */
    public void getUserInfo(CallbackListener callback) {
        if (null == Global.Renren.UID) return;
        Bundle params = new Bundle();
        params.putString(Constants.RENREN_PARAMS_METHOD, Constants.RENREN_METHOD.USERS_GETINFO);
        params.putString(Constants.RENREN_PARAMS_UIDS, Global.Renren.UID);
        params.putString(Constants.RENREN_PARAMS_SESSION_KEY, renren.getSessionKey());
        params.putString(Constants.RENREN_PARAMS_FIELDS, Constants.RENREN_USER_FIELDS_FULL);
        asyncRenren.request(params, new RequestAdapter(Constants.CALLBACK_ACTION_GETUSERINFO, callback), Constants.RENREN_DATAFORMAT_JSON);
    }

    public void getFriendInfo(String friendUid, CallbackListener callback) {
        if (null == Global.Renren.UID) return;
        Bundle params = new Bundle();
        params.putString(Constants.RENREN_PARAMS_METHOD, Constants.RENREN_METHOD.USERS_GETINFO);
        params.putString(Constants.RENREN_PARAMS_UIDS, friendUid);
        params.putString(Constants.RENREN_PARAMS_SESSION_KEY, renren.getSessionKey());
        params.putString(Constants.RENREN_PARAMS_FIELDS, Constants.RENREN_USER_FIELDS_FULL);
        asyncRenren.request(params, new RequestAdapter(Constants.CALLBACK_ACTION_GETFRIENDINFO, callback), Constants.RENREN_DATAFORMAT_JSON);
        params = new Bundle();
        params.putString(Constants.RENREN_PARAMS_METHOD, "status.get");
        params.putString(Constants.RENREN_PARAMS_SESSION_KEY, renren.getSessionKey());
        asyncRenren.request(params, new RequestListener() {

            public void onComplete(String response) {
                Log.w("-------------", "response=" + response);
            }

            public void onRenrenError(RenrenError renrenError) {
                Log.w("-------------", "error=" + renrenError);
            }

            public void onFault(Throwable fault) {
                Log.w("-------------", "error=" + fault);
            }
        }, Constants.RENREN_DATAFORMAT_JSON);
    }

    public void getLoggedInUserId(CallbackListener callback) {
        if (null == renren || !renren.isSessionKeyValid()) return;
        Bundle params = new Bundle();
        params.putString(Constants.RENREN_PARAMS_METHOD, Constants.RENREN_METHOD.USERS_GETLOGGEDINUSER);
        params.putString(Constants.RENREN_PARAMS_UIDS, Global.Renren.UID);
        asyncRenren.request(params, new RequestAdapter(Constants.CALLBACK_ACTION_GETLOGGEDINUSER, callback), Constants.RENREN_DATAFORMAT_JSON);
    }

    public void getFriends(CallbackListener callback) {
        Bundle params = new Bundle();
        params.putString(Constants.RENREN_PARAMS_METHOD, Constants.RENREN_METHOD.FRIENDS_GETFRIENDS);
        params.putString(Constants.RENREN_PARAMS_COUNT, Constants.RENREN_FRIENDS_MAX);
        asyncRenren.request(params, new RequestAdapter(Constants.CALLBACK_ACTION_GETFRIENDS, callback), Constants.RENREN_DATAFORMAT_JSON);
    }

    public void storeLoginInfo(Bundle values) {
        Global.Renren.UID = values.getString("uid");
        Global.Renren.AUTH_TOKEN = values.getString("auth_token");
        Global.Renren.AUTO_LOGIN = values.getString("auto_login");
        Global.Renren.SIG = values.getString("sig");
    }

    public void storeUserInfo(String uid, String name, String sex) {
        Global.Renren.NAME = name;
        Global.Renren.SEX = sex;
    }

    public void storeFriendList(JSONArray friends) throws JSONException {
        Global.Renren.friends.clear();
        for (int i = 0; i < friends.length(); i++) {
            RRFriend friend = new RRFriend(friends.getJSONObject(i));
            Global.Renren.friends.add(friend);
        }
    }

    public void resetLoginInfo() {
        Global.Renren.UID = null;
        Global.Renren.AUTH_TOKEN = null;
        Global.Renren.AUTO_LOGIN = null;
        Global.Renren.SIG = null;
        Global.Renren.NAME = null;
        Global.Renren.SEX = null;
    }

    public ImageLoader getImageLoader() {
        return this.imageLoader;
    }

    public StorageCenter getStorageCenter() {
        return this.storageCenter;
    }

    /**
	 * Defined for the Renren asynchronous response.
	 * @author Administrator
	 *
	 */
    class RequestAdapter implements RequestListener {

        private CallbackListener callback = null;

        private String action = null;

        private boolean delayCallback = false;

        /**
		 * Constructor.
		 * @param action
		 * @param uiCallback
		 */
        public RequestAdapter(String action, CallbackListener uiCallback) {
            this.callback = uiCallback;
            this.action = action;
        }

        @Override
        public void onComplete(String response) {
            CtrlData data = new CtrlData(action, response);
            data.put(Constants.CALLBACK_KEY_STATUS, Constants.CALLBACK_STATUS_SUCCESS);
            data.put(Constants.CALLBACK_KEY_DATA, response);
            if (null == data || Constants.CALLBACK_STATUS_FAILURE.equals(data.get(Constants.CALLBACK_KEY_STATUS))) {
                Log.i("MainController::processCallback", "CtrlData null or failure.");
            } else if (Constants.CALLBACK_ACTION_GETLOGGEDINUSER.equals(data.getAction())) {
                try {
                    String jsonStr = (String) data.get(Constants.CALLBACK_KEY_DATA);
                    JSONObject json = new JSONObject(jsonStr);
                    String uid = "" + json.get(RENREN_USER_FIELDS.UID);
                    Global.Renren.UID = uid;
                } catch (JSONException e) {
                    Log.e("MainController::processCallback::CALLBACK_ACTION_GETLOGGEDINUSER", e.getMessage());
                }
            } else if (Constants.CALLBACK_ACTION_GETUSERINFO.equals(data.getAction())) {
                try {
                    String jsonStr = (String) data.get(Constants.CALLBACK_KEY_DATA);
                    JSONObject json = new JSONObject(Util.trimToJSON(jsonStr));
                    String uid = Util.jsonGet(json, RENREN_USER_FIELDS.UID);
                    String name = Util.jsonGet(json, RENREN_USER_FIELDS.NAME);
                    String sex = Util.jsonGet(json, RENREN_USER_FIELDS.SEX);
                    MainController.this.storeUserInfo(uid, name, sex);
                } catch (JSONException e) {
                    Log.e("MainController::processCallback::CALLBACK_ACTION_GETUSERINFO", e.getMessage());
                }
            } else if (Constants.CALLBACK_ACTION_GETFRIENDS.equals(data.getAction())) {
                try {
                    String jsonStr = (String) data.get(Constants.CALLBACK_KEY_DATA);
                    Log.i("MainController::processCallback::CALLBACK_ACTION_GETFRIENDS", jsonStr);
                    JSONArray friends = new JSONArray(jsonStr);
                    MainController.this.storeFriendList(friends);
                } catch (JSONException e) {
                    Log.e("MainController::processCallback::CALLBACK_ACTION_GETFRIENDS", e.getMessage());
                }
            } else if (Constants.CALLBACK_ACTION_GETFRIENDINFO.equals(data.getAction())) {
                try {
                    data.put(Constants.CALLBACK_KEY_OBJECT, new Friend(new RRFriendAdapter(response)));
                } catch (JSONException e) {
                    data.remove(Constants.CALLBACK_KEY_OBJECT);
                    e.printStackTrace();
                }
            } else {
                Log.i("MainController::processCalback", "Undefined Action: " + response);
            }
            if (null != callback && !delayCallback) {
                callback.onComplete(data);
            }
        }

        @Override
        public void onRenrenError(RenrenError renrenError) {
            CtrlData data = new CtrlData(action, renrenError.getMessage());
            data.put(Constants.CALLBACK_KEY_STATUS, Constants.CALLBACK_STATUS_FAILURE);
            if (null != callback) {
                callback.onRenrenError(data);
            }
        }

        @Override
        public void onFault(Throwable fault) {
            CtrlData data = new CtrlData(action, fault.getMessage());
            data.put(Constants.CALLBACK_KEY_STATUS, Constants.CALLBACK_STATUS_FAILURE);
            if (null != callback) {
                callback.onFault(data);
            }
        }
    }
}
