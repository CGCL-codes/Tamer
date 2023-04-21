package com.diipo.weibo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import com.diipo.weibo.adapter.ContactsItemAdapter;
import com.diipo.weibo.entity.Contacts;
import com.diipo.weibo.entity.ContactsList;
import com.diipo.weibo.utils.HttpUtils;
import com.diipo.weibo.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class ContactsActivity extends Activity {

    private String TAG = ContactsActivity.class.getSimpleName();

    private Button mRefreshWeibo;

    private ListView mContactsListView;

    private ProgressDialog progressDialog;

    private SharedPreferences mPrefs;

    private ContactsList mContactsList = new ContactsList();

    private ArrayList<String> mContactsNameList;

    private ArrayList<String> mContactsIdList;

    private ArrayList<String> mContactsPicList;

    private Context mctx;

    private Handler handler = new Handler();

    private String result;

    private Intent mIntent;

    private final class RemoveWindow implements Runnable {

        public void run() {
            removeWindow();
        }
    }

    private RemoveWindow mRemoveWindow = new RemoveWindow();

    Handler mHandler = new Handler();

    private WindowManager mWindowManager;

    private TextView mDialogText;

    private boolean mShowing;

    private boolean mReady;

    private char mPrevLetter = Character.MIN_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contacts_view);
        mIntent = getIntent();
        mctx = this;
        mPrefs = getSharedPreferences(ConfigInfo.USER_PREFERENCES, MODE_PRIVATE);
        mContactsListView = (ListView) findViewById(R.id.contacts_listview);
        mRefreshWeibo = (Button) findViewById(R.id.titlebar_refresh);
        mRefreshWeibo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e(TAG, " refresh data from server,get the newest contacts data .........");
                new AsyncDataContacts().execute();
            }
        });
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialogText = (TextView) inflate.inflate(R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        mHandler.post(new Runnable() {

            public void run() {
                mReady = true;
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
                mWindowManager.addView(mDialogText, lp);
            }
        });
        mContactsListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mContactsNameList.get(position);
                String userId = mContactsIdList.get(position);
                String pic_url = mContactsPicList.get(position);
                Log(TAG + "  ContactsActivity name = " + name + " ,userId = " + userId);
                if (null == mIntent.getStringExtra(ConfigInfo.COMM.SEND_TYPE)) {
                    Log(TAG + "  ContactsActivity null null null ");
                    Intent intent = new Intent(ContactsActivity.this, UserInfoActivity.class);
                    intent.putExtra(ConfigInfo.COMM.USER_NAME, name);
                    startActivity(intent);
                    return;
                }
                if (mIntent.getStringExtra(ConfigInfo.COMM.SEND_TYPE).equals(ConfigInfo.COMM.WEIBO_LETTER)) {
                    Intent i = new Intent();
                    Bundle b = new Bundle();
                    b.putString(ConfigInfo.COMM.SEND_TYPE, ConfigInfo.COMM.WEIBO_LETTER);
                    b.putString(ConfigInfo.COMM.USER_ID, userId);
                    b.putString(ConfigInfo.COMM.PIC_URL, pic_url);
                    b.putString(ConfigInfo.COMM.USER_NAME, name);
                    i.putExtras(b);
                    setResult(ConfigInfo.CALLBACK_CREATE_LETTER, i);
                    finish();
                } else {
                }
            }
        });
        new AsyncDataContacts().execute();
    }

    private void removeWindow() {
        if (mShowing) {
            mShowing = false;
            mDialogText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReady = true;
        Log(TAG + " onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeWindow();
        mReady = false;
        Log(TAG + "  onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mDialogText);
        mReady = false;
        Log(TAG + " onDestroy() called...");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log(TAG + " send_type = " + mIntent.getStringExtra(ConfigInfo.COMM.SEND_TYPE));
            if (result == ConfigInfo.HTTPRETURN.COMMHTTPERRORS && mIntent.getStringExtra(ConfigInfo.COMM.SEND_TYPE) != null && mIntent.getStringExtra(ConfigInfo.COMM.SEND_TYPE).equals(ConfigInfo.COMM.WEIBO_LETTER)) {
                result = null;
                Log(TAG + " just call finish() here...");
                finish();
                return true;
            }
            ConfirmExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ConfirmExit() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ContactsActivity.this);
        dialog.setTitle(mctx.getString(R.string.exit));
        dialog.setMessage(mctx.getString(R.string.is_exit_weibo));
        dialog.setPositiveButton(mctx.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                ContactsActivity.this.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        dialog.setNegativeButton(mctx.getString(R.string.btn_cancle), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        dialog.show();
    }

    void Log(String msg) {
        Log.e(TAG, "ContactsActivity--" + msg);
    }

    /**
	 * 异步加载服务器上的微博数据的监听器,联系人
	 */
    private class AsyncDataContacts extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.e(TAG, " asyncRemoteCallback onStart() called..... + by pyb");
            mPrefs = getSharedPreferences(ConfigInfo.USER_PREFERENCES, MODE_PRIVATE);
            StringBuffer url = new StringBuffer();
            String url_contacts = "/api/users/contacts.json?s=1&api_key=";
            url.append(mPrefs.getString(ConfigInfo.PREF_SERVER_ADDRESS, mPrefs.getString(ConfigInfo.PREF_SERVER_ADDRESS, ConfigInfo.COMM.CONST_SERVER_ADDRESS)));
            url.append(url_contacts);
            url.append(ConfigInfo.COMM.APY_KEY);
            String username = mPrefs.getString(ConfigInfo.PREF_USERNAME, "");
            String passwd = mPrefs.getString(ConfigInfo.PREF_PASSWORD, "");
            result = HttpUtils.doPost(url.toString(), username, passwd, null);
            Log.e(TAG, "url address = " + url);
            Log.e(TAG, " username  = " + username);
            Log.e(TAG, " passwd  = " + passwd);
            Log.e(TAG, " activity result =  " + result);
            if (result != ConfigInfo.HTTPRETURN.COMMHTTPERRORS && result != ConfigInfo.HTTPRETURN.HTTP_ERROR_400 && result != ConfigInfo.HTTPRETURN.HTTP_ERROR_401 && result != ConfigInfo.HTTPRETURN.HTTPERROR) {
                try {
                    mContactsList = JsonUtils.parseUserFromJson(result, ContactsList.class);
                    AddContacts();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, " network error here.... ");
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ContactsActivity.this);
            progressDialog.setMessage("正在加载中...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result != null && !result.equals("") && result != ConfigInfo.HTTPRETURN.HTTPERROR && result != ConfigInfo.HTTPRETURN.HTTP_ERROR_401 && result != ConfigInfo.HTTPRETURN.COMMHTTPERRORS) {
                try {
                    ContactsItemAdapter adapter = new ContactsItemAdapter(mContactsNameList, ContactsActivity.this);
                    mContactsListView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ContactsActivity.this, result, Toast.LENGTH_LONG).show();
                return;
            }
            mContactsListView.setOnScrollListener(new OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (mReady) {
                        char firstLetter = mContactsNameList.get(firstVisibleItem).charAt(0);
                        if (!mShowing && firstLetter != mPrevLetter) {
                            mShowing = true;
                            mDialogText.setVisibility(View.VISIBLE);
                        }
                        mDialogText.setText(((Character) firstLetter).toString());
                        mHandler.removeCallbacks(mRemoveWindow);
                        mHandler.postDelayed(mRemoveWindow, 500);
                        mPrevLetter = firstLetter;
                    }
                }
            });
        }

        private void AddContacts() {
            mContactsNameList = new ArrayList<String>();
            mContactsIdList = new ArrayList<String>();
            mContactsPicList = new ArrayList<String>();
            Contacts[] A = mContactsList.getA();
            if (A != null) {
                for (int i = 0; i < A.length; i++) {
                    mContactsNameList.add(mContactsList.getA()[i].getUsername());
                    mContactsIdList.add(mContactsList.getA()[i].getId());
                    mContactsPicList.add(mContactsList.getA()[i].getProfile_picture());
                }
            }
            Contacts[] B = mContactsList.getB();
            if (B != null) {
                for (int i = 0; i < B.length; i++) {
                    mContactsNameList.add(mContactsList.getB()[i].getUsername());
                    mContactsIdList.add(mContactsList.getB()[i].getId());
                    mContactsPicList.add(mContactsList.getB()[i].getProfile_picture());
                }
            }
            Contacts[] C = mContactsList.getC();
            if (C != null) {
                for (int i = 0; i < C.length; i++) {
                    mContactsNameList.add(mContactsList.getC()[i].getUsername());
                    mContactsIdList.add(mContactsList.getC()[i].getId());
                    mContactsPicList.add(mContactsList.getC()[i].getProfile_picture());
                }
            }
            Contacts[] D = mContactsList.getD();
            if (D != null) {
                for (int i = 0; i < D.length; i++) {
                    mContactsNameList.add(mContactsList.getD()[i].getUsername());
                    mContactsIdList.add(mContactsList.getD()[i].getId());
                    mContactsPicList.add(mContactsList.getD()[i].getProfile_picture());
                }
            }
            Contacts[] E = mContactsList.getE();
            if (E != null) {
                for (int i = 0; i < E.length; i++) {
                    mContactsNameList.add(mContactsList.getE()[i].getUsername());
                    mContactsIdList.add(mContactsList.getE()[i].getId());
                    mContactsPicList.add(mContactsList.getE()[i].getProfile_picture());
                }
            }
            Contacts[] F = mContactsList.getF();
            if (F != null) {
                for (int i = 0; i < F.length; i++) {
                    mContactsNameList.add(mContactsList.getF()[i].getUsername());
                    mContactsIdList.add(mContactsList.getF()[i].getId());
                    mContactsPicList.add(mContactsList.getF()[i].getProfile_picture());
                }
            }
            Contacts[] G = mContactsList.getG();
            if (G != null) {
                for (int i = 0; i < G.length; i++) {
                    mContactsNameList.add(mContactsList.getG()[i].getUsername());
                    mContactsIdList.add(mContactsList.getG()[i].getId());
                    mContactsPicList.add(mContactsList.getG()[i].getProfile_picture());
                }
            }
            Contacts[] H = mContactsList.getH();
            if (H != null) {
                for (int i = 0; i < H.length; i++) {
                    mContactsNameList.add(mContactsList.getH()[i].getUsername());
                    mContactsIdList.add(mContactsList.getH()[i].getId());
                    mContactsPicList.add(mContactsList.getH()[i].getProfile_picture());
                }
            }
            Contacts[] I = mContactsList.getI();
            if (I != null) {
                for (int i = 0; i < I.length; i++) {
                    mContactsNameList.add(mContactsList.getI()[i].getUsername());
                    mContactsIdList.add(mContactsList.getI()[i].getId());
                    mContactsPicList.add(mContactsList.getI()[i].getProfile_picture());
                }
            }
            Contacts[] J = mContactsList.getJ();
            if (J != null) {
                for (int i = 0; i < J.length; i++) {
                    mContactsNameList.add(mContactsList.getJ()[i].getUsername());
                    mContactsIdList.add(mContactsList.getJ()[i].getId());
                    mContactsPicList.add(mContactsList.getJ()[i].getProfile_picture());
                }
            }
            Contacts[] K = mContactsList.getK();
            if (K != null) {
                for (int i = 0; i < K.length; i++) {
                    mContactsNameList.add(mContactsList.getK()[i].getUsername());
                    mContactsIdList.add(mContactsList.getK()[i].getId());
                    mContactsPicList.add(mContactsList.getK()[i].getProfile_picture());
                }
            }
            Contacts[] L = mContactsList.getL();
            if (L != null) {
                for (int i = 0; i < L.length; i++) {
                    mContactsNameList.add(mContactsList.getL()[i].getUsername());
                    mContactsIdList.add(mContactsList.getL()[i].getId());
                    mContactsPicList.add(mContactsList.getL()[i].getProfile_picture());
                }
            }
            Contacts[] M = mContactsList.getM();
            if (M != null) {
                for (int i = 0; i < M.length; i++) {
                    mContactsNameList.add(mContactsList.getM()[i].getUsername());
                    mContactsIdList.add(mContactsList.getM()[i].getId());
                    mContactsPicList.add(mContactsList.getM()[i].getProfile_picture());
                }
            }
            Contacts[] N = mContactsList.getN();
            if (N != null) {
                for (int i = 0; i < N.length; i++) {
                    mContactsNameList.add(mContactsList.getN()[i].getUsername());
                    mContactsIdList.add(mContactsList.getN()[i].getId());
                    mContactsPicList.add(mContactsList.getN()[i].getProfile_picture());
                }
            }
            Contacts[] O = mContactsList.getO();
            if (O != null) {
                for (int i = 0; i < O.length; i++) {
                    mContactsNameList.add(mContactsList.getO()[i].getUsername());
                    mContactsIdList.add(mContactsList.getO()[i].getId());
                    mContactsPicList.add(mContactsList.getO()[i].getProfile_picture());
                }
            }
            Contacts[] P = mContactsList.getP();
            if (P != null) {
                for (int i = 0; i < P.length; i++) {
                    mContactsNameList.add(mContactsList.getP()[i].getUsername());
                    mContactsIdList.add(mContactsList.getP()[i].getId());
                    mContactsPicList.add(mContactsList.getP()[i].getProfile_picture());
                }
            }
            Contacts[] Q = mContactsList.getQ();
            if (Q != null) {
                for (int i = 0; i < Q.length; i++) {
                    mContactsNameList.add(mContactsList.getQ()[i].getUsername());
                    mContactsIdList.add(mContactsList.getQ()[i].getId());
                    mContactsPicList.add(mContactsList.getQ()[i].getProfile_picture());
                }
            }
            Contacts[] R = mContactsList.getR();
            if (R != null) {
                for (int i = 0; i < R.length; i++) {
                    mContactsNameList.add(mContactsList.getR()[i].getUsername());
                    mContactsIdList.add(mContactsList.getR()[i].getId());
                    mContactsPicList.add(mContactsList.getR()[i].getProfile_picture());
                }
            }
            Contacts[] S = mContactsList.getS();
            if (S != null) {
                for (int i = 0; i < S.length; i++) {
                    mContactsNameList.add(mContactsList.getS()[i].getUsername());
                    mContactsIdList.add(mContactsList.getS()[i].getId());
                    mContactsPicList.add(mContactsList.getS()[i].getProfile_picture());
                }
            }
            Contacts[] T = mContactsList.getT();
            if (T != null) {
                for (int i = 0; i < T.length; i++) {
                    mContactsNameList.add(mContactsList.getT()[i].getUsername());
                    mContactsIdList.add(mContactsList.getT()[i].getId());
                    mContactsPicList.add(mContactsList.getT()[i].getProfile_picture());
                }
            }
            Contacts[] U = mContactsList.getU();
            if (U != null) {
                for (int i = 0; i < U.length; i++) {
                    mContactsNameList.add(mContactsList.getU()[i].getUsername());
                    mContactsIdList.add(mContactsList.getU()[i].getId());
                    mContactsPicList.add(mContactsList.getU()[i].getProfile_picture());
                }
            }
            Contacts[] V = mContactsList.getV();
            if (V != null) {
                for (int i = 0; i < V.length; i++) {
                    mContactsNameList.add(mContactsList.getV()[i].getUsername());
                    mContactsIdList.add(mContactsList.getV()[i].getId());
                    mContactsPicList.add(mContactsList.getV()[i].getProfile_picture());
                }
            }
            Contacts[] W = mContactsList.getW();
            if (W != null) {
                for (int i = 0; i < W.length; i++) {
                    mContactsNameList.add(mContactsList.getW()[i].getUsername());
                    mContactsIdList.add(mContactsList.getW()[i].getId());
                    mContactsPicList.add(mContactsList.getW()[i].getProfile_picture());
                }
            }
            Contacts[] X = mContactsList.getX();
            if (X != null) {
                for (int i = 0; i < X.length; i++) {
                    mContactsNameList.add(mContactsList.getX()[i].getUsername());
                    mContactsIdList.add(mContactsList.getX()[i].getId());
                    mContactsPicList.add(mContactsList.getX()[i].getProfile_picture());
                }
            }
            Contacts[] Y = mContactsList.getY();
            if (Y != null) {
                for (int i = 0; i < Y.length; i++) {
                    mContactsNameList.add(mContactsList.getY()[i].getUsername());
                    mContactsIdList.add(mContactsList.getY()[i].getId());
                    mContactsPicList.add(mContactsList.getY()[i].getProfile_picture());
                }
            }
            Contacts[] Z = mContactsList.getZ();
            if (Z != null) {
                for (int i = 0; i < Z.length; i++) {
                    mContactsNameList.add(mContactsList.getZ()[i].getUsername());
                    mContactsIdList.add(mContactsList.getZ()[i].getId());
                    mContactsPicList.add(mContactsList.getZ()[i].getProfile_picture());
                }
            }
            Log.i(TAG, " pyb  ---- contactsArrayList SIZE = " + mContactsNameList.size());
        }
    }

    /**
	 * 异步加载服务器上的微博数据的监听器,联系人
	 */
    private AsyncDataLoader.Callback asyncRemoteCallback = new AsyncDataLoader.Callback() {

        String url_contacts = "/api/users/contacts.json?s=1&api_key=";

        private SharedPreferences mPrefs;

        private String serveraddress = "http://q8.diipo.cn";

        private LinkedList<Contacts> list = new LinkedList<Contacts>();

        private LinkedList<Contacts> list2 = new LinkedList<Contacts>();

        private Type listType = new TypeToken<LinkedList<Contacts>>() {
        }.getType();

        @Override
        public void onStart(String address, String id) {
            Log.e(TAG, " asyncRemoteCallback onStart() called..... + by pyb");
            mPrefs = getSharedPreferences(ConfigInfo.USER_PREFERENCES, MODE_PRIVATE);
            StringBuffer url = new StringBuffer();
            url.append(mPrefs.getString(ConfigInfo.PREF_SERVER_ADDRESS, serveraddress));
            url.append(url_contacts);
            url.append(ConfigInfo.COMM.APY_KEY);
            String username = mPrefs.getString(ConfigInfo.PREF_USERNAME, "");
            String passwd = mPrefs.getString(ConfigInfo.PREF_PASSWORD, "");
            String result = HttpUtils.doPost(url.toString(), username, passwd, null);
            Log.e("", "url address = " + url);
            Log.e("", " username  = " + username);
            Log.e("", " passwd  = " + passwd);
            Log.e("", " activity result =  " + result);
            if (result != ConfigInfo.HTTPRETURN.COMMHTTPERRORS && result != ConfigInfo.HTTPRETURN.HTTP_ERROR_400 && result != ConfigInfo.HTTPRETURN.HTTP_ERROR_401 && result != ConfigInfo.HTTPRETURN.HTTPERROR) {
                if (list.size() >= 20) {
                    list2 = JsonUtils.parseUserFromJsons(result, listType);
                    Iterator<Contacts> iterator = list2.iterator();
                    while (iterator.hasNext()) {
                        list.add(iterator.next());
                    }
                } else {
                    list = JsonUtils.parseUserFromJsons(result, listType);
                }
            }
        }

        @Override
        public void onPrepare() {
            Log.e(TAG, " asyncRemoteCallback onPrepare() called..... + by pyb");
        }

        @Override
        public void onFinish() {
            Log.e(TAG, "  asyncRemoteCallback onFinish() called..... + by pyb");
        }

        @Override
        public void onStart(String address, String arg1, String arg2) {
        }
    };
}
