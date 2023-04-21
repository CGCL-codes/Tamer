package com.banyingli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import com.banyingli.common.Cons;
import com.banyingli.more.AboutActivity;
import com.banyingli.more.HelpActivity;
import com.banyingli.more.MoreActivity;
import com.banyingli.more.SettingActivity;
import com.banyingli.music.AlbulmActivity;
import com.banyingli.news.NewsActivity;
import com.banyingli.photo.PhotoTypeActivity;
import com.banyingli.video.VideoTypeActivity;

public class Home extends Activity implements OnClickListener {

    private WebView webView;

    private Button btn_news;

    private Button btn_photo;

    private Button btn_music;

    private Button btn_video;

    private Button btn_more;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        findAndSetViews();
    }

    public void findAndSetViews() {
        btn_news = (Button) findViewById(R.id.btn_home_news);
        btn_photo = (Button) findViewById(R.id.btn_home_photo);
        btn_music = (Button) findViewById(R.id.btn_home_music);
        btn_video = (Button) findViewById(R.id.btn_home_video);
        btn_more = (Button) findViewById(R.id.btn_home_more);
        webView = (WebView) findViewById(R.id.webview_home);
        btn_news.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
        btn_music.setOnClickListener(this);
        btn_video.setOnClickListener(this);
        btn_more.setOnClickListener(this);
        webView.loadUrl(Cons.HOME_WEB_URL);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_home_news:
                startActivity(new Intent(Home.this, NewsActivity.class));
                finish();
                break;
            case R.id.btn_home_music:
                startActivity(new Intent(Home.this, AlbulmActivity.class));
                finish();
                break;
            case R.id.btn_home_photo:
                startActivity(new Intent(Home.this, PhotoTypeActivity.class));
                finish();
                break;
            case R.id.btn_home_video:
                startActivity(new Intent(Home.this, VideoTypeActivity.class));
                finish();
                break;
            case R.id.btn_home_more:
                startActivity(new Intent(Home.this, MoreActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menu_setting = menu.add(1, 1, 1, getResources().getString(R.string.setting_topic));
        MenuItem menu_help = menu.add(1, 2, 2, getResources().getString(R.string.help_topic));
        MenuItem menu_about = menu.add(1, 3, 3, getResources().getString(R.string.about_topic));
        MenuItem menu_exit = menu.add(1, 4, 4, getResources().getString(R.string.exit));
        menu_setting.setIcon(R.drawable.menu_setting);
        menu_help.setIcon(R.drawable.menu_help);
        menu_about.setIcon(R.drawable.menu_about);
        menu_exit.setIcon(R.drawable.menu_exit);
        menu_setting.setIntent(new Intent(Home.this, SettingActivity.class));
        menu_help.setIntent(new Intent(Home.this, HelpActivity.class));
        menu_about.setIntent(new Intent(Home.this, AboutActivity.class));
        menu_exit.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });
        return true;
    }
}
