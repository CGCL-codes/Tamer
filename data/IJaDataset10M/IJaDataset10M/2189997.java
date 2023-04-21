package com.vivilab.smth;

import java.util.List;
import com.vivilab.smth.R;
import com.vivilab.smth.helper.SmthHelper;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShowFavActivity extends ListActivity implements OnItemClickListener {

    private static final String TAG = "ShowFavActivity";

    private static List myFav;

    private static ProgressDialog dialog = null;

    private static ShowFavActivity currentActivity;

    private static boolean working = false;

    private static int state = 0;

    private static ArrayAdapter<String> favAdapter;

    public void onCreate(Bundle savedInstanceState) {
        currentActivity = this;
        super.onCreate(savedInstanceState);
        ListView lv = getListView();
        lv.setTextFilterEnabled(false);
        lv.setOnItemClickListener(this);
        if (!working) {
            if (state != 2) {
                dialog = ProgressDialog.show(ShowFavActivity.this, "", getString(R.string.info_getfav), true);
                ShowFav showFav = new ShowFav(handler);
                working = true;
                showFav.start();
            } else {
                setListAdapter(favAdapter);
            }
        } else {
            Log.i(TAG, "after rotate state = " + state);
            if (state != 2) dialog = ProgressDialog.show(ShowFavActivity.this, "", getString(R.string.info_getfav), true);
        }
    }

    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        Log.i(TAG, "we are going to show board:" + ((TextView) view).getText());
        Intent i = new Intent(this, BoardActivity.class);
        i.putExtra("board", ((TextView) view).getText());
        startActivity(i);
    }

    static final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            Log.i(TAG, "set state to 2 ");
            state = 2;
            dialog.dismiss();
            dialog = null;
            int size = msg.getData().getInt("size");
            if (size > 0) {
                favAdapter = new ArrayAdapter<String>(currentActivity, R.layout.listfav, myFav);
                currentActivity.setListAdapter(favAdapter);
            } else {
                Toast.makeText(currentActivity, currentActivity.getString(R.string.info_no_fav), Toast.LENGTH_SHORT).show();
            }
            working = false;
        }
    };

    private class ShowFav extends Thread {

        private Handler mHandler;

        public ShowFav(Handler handler) {
            mHandler = handler;
        }

        public void run() {
            myFav = SmthHelper.getFavorate();
            int size;
            if (myFav != null) size = myFav.size(); else size = 0;
            Message msg = mHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("size", size);
            msg.setData(b);
            mHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) dialog.dismiss();
    }
}
