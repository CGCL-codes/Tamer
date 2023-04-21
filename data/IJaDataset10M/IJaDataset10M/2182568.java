package com.haliyoo.adhere.sqlite;

import java.util.ArrayList;
import java.util.List;
import com.haliyoo.adhere.bean.UserBean;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataHelper4User {

    private static final String TAG = "DataHelper4User";

    private static String DB_NAME = "aduser.db";

    private static int DB_VERSION = 2;

    private SQLiteDatabase db;

    private SqliteHelper4User dbHelper;

    public DataHelper4User(Context context) {
        dbHelper = new SqliteHelper4User(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    public List<UserBean> selectByUser(String user) {
        List<UserBean> userList = new ArrayList<UserBean>();
        Cursor cursor = db.query(SqliteHelper4User.TB_NAME, null, "USER = '" + user + "'", null, null, null, UserBean.ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            UserBean userBean = new UserBean();
            userBean.setId(cursor.getString(0));
            userBean.setUser(cursor.getString(1));
            userBean.setUserId(cursor.getLong(2));
            userBean.setToken(cursor.getString(3));
            userBean.setTokenSecret(cursor.getString(4));
            userBean.setIsRemember(cursor.getShort(5) == 0 ? false : true);
            userBean.setIsAutoLogin(cursor.getShort(6) == 0 ? false : true);
            userList.add(userBean);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    public List<UserBean> selectByIsRemember(boolean isRemember) {
        List<UserBean> userList = new ArrayList<UserBean>();
        short shIsRemember = (short) (isRemember ? 1 : 0);
        Cursor cursor = db.query(SqliteHelper4User.TB_NAME, null, "ISREMEMBER = " + shIsRemember, null, null, null, UserBean.ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            UserBean userBean = new UserBean();
            userBean.setId(cursor.getString(0));
            userBean.setUser(cursor.getString(1));
            userBean.setUserId(cursor.getLong(2));
            userBean.setToken(cursor.getString(3));
            userBean.setTokenSecret(cursor.getString(4));
            userBean.setIsRemember(cursor.getShort(5) == 0 ? false : true);
            userBean.setIsAutoLogin(cursor.getShort(6) == 0 ? false : true);
            userList.add(userBean);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    public Boolean exist(String user) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper4User.TB_NAME, null, UserBean.USER + "=" + user, null, null, null, null);
        b = cursor.moveToFirst();
        Log.i(TAG, "exist " + b.toString());
        cursor.close();
        return b;
    }

    public int update(UserBean user) {
        ContentValues values = new ContentValues();
        values.put(UserBean.USER, user.getUser());
        values.put(UserBean.USERID, user.getUserId());
        values.put(UserBean.TOKEN, user.getToken());
        values.put(UserBean.TOKENSECRET, user.getTokenSecret());
        values.put(UserBean.ISREMEMBER, user.getIsRemember() ? 1 : 0);
        values.put(UserBean.ISAUTOLOGIN, user.getIsAutoLogin() ? 1 : 0);
        int id = db.update(SqliteHelper4User.TB_NAME, values, UserBean.ID + "=" + user.getId(), null);
        Log.i(TAG, "update " + id + "");
        return id;
    }

    public long insert(UserBean user) {
        ContentValues values = new ContentValues();
        values.put(UserBean.USER, user.getUser());
        values.put(UserBean.USERID, user.getUserId());
        values.put(UserBean.TOKEN, user.getToken());
        values.put(UserBean.TOKENSECRET, user.getTokenSecret());
        values.put(UserBean.ISREMEMBER, user.getIsRemember() ? 1 : 0);
        values.put(UserBean.ISAUTOLOGIN, user.getIsAutoLogin() ? 1 : 0);
        long uid = db.insert(SqliteHelper4User.TB_NAME, UserBean.ID, values);
        Log.i(TAG, "insert " + uid + "");
        return uid;
    }

    public int delete(UserBean user) {
        int id = db.delete(SqliteHelper4User.TB_NAME, UserBean.ID + "=" + user.getId(), null);
        Log.i(TAG, "delete " + id + "");
        return id;
    }
}
