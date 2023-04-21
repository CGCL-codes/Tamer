package cc.carnero.ctwee;

import java.util.Date;
import java.util.Timer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.NotificationManager;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.content.res.Configuration;
import android.content.SharedPreferences;

public class ctwee extends ListActivity {

    private ctData tweets = null;

    private ctTweetListAdapter adapter;

    private Timer refresh;

    public Date lastOpened = null;

    private Handler timerHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };

    private SharedPreferences prefs = null;

    private SharedPreferences.Editor prefsW = null;

    private String tokenPublic = "";

    private String tokenSecret = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences(ctGlobal.preferences, 0);
        prefsW = prefs.edit();
        tokenPublic = prefs.getString("token-public", "");
        tokenSecret = prefs.getString("token-secret", "");
        this.getLastOpened();
        final Context context = getApplicationContext();
        setContentView(R.layout.main);
        if (tokenPublic.length() == 0 || tokenSecret.length() == 0) {
            Intent authIntent = new Intent(this, ctauth.class);
            this.startActivity(authIntent);
        }
        tweets = new ctData(context);
        prefsW.putString("lastOpened", "");
        prefsW.commit();
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("cc.carnero.ctwee.CTSERVICE");
        startService(serviceIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ctGlobal.notificationNewTweets);
        try {
            adapter = new ctTweetListAdapter(this, this.tweets.storage, this.lastOpened, false);
            setListAdapter(adapter);
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "Failed to create adapter: " + e.toString());
        }
        this.tweets.loadTweetsFromDb(this.lastOpened);
        this.adapter.notifyDataSetChanged();
        this.refresh = new Timer("refresh", true);
        this.refresh.schedule(new ctRefreshTimer(this.timerHandler, this.tweets), 5000, 15000);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getLastOpened();
        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ctGlobal.notificationNewTweets);
        prefsW.putString("lastOpened", "");
        prefsW.commit();
        if (this.tweets != null) {
            this.tweets.openDb();
        }
        if (this.adapter != null) {
            this.tweets.loadTweetsFromDb(this.lastOpened);
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        prefsW.putString("lastOpened", ctGlobal.sqlDate.format(new Date()));
        prefsW.commit();
        if (this.tweets != null) {
            this.tweets.closeDb();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        prefsW.putString("lastOpened", ctGlobal.sqlDate.format(new Date()));
        prefsW.commit();
        if (this.tweets != null) {
            this.tweets.closeDb();
        }
        if (this.refresh != null) {
            this.refresh.cancel();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (this.tweets != null) {
            this.tweets.closeDb();
        }
        if (this.refresh != null) {
            this.refresh.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, this.getResources().getString(R.string.menu_tweet)).setIcon(android.R.drawable.ic_menu_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 0:
                ctweet newTweet = new ctweet(this);
                newTweet.show();
                return true;
        }
        return false;
    }

    private void getLastOpened() {
        try {
            String date = prefs.getString("lastOpened", "");
            if (date.length() > 0) {
                this.lastOpened = ctGlobal.sqlDate.parse(date);
                return;
            }
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "ctwee.getLastOpened: " + e.toString());
        }
        this.lastOpened = null;
    }
}
