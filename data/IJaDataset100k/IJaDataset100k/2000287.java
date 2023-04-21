package com.andrewid.timetable.gui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import com.andrewid.timetable.R;
import com.andrewid.timetable.TimeTableContext;
import com.andrewid.timetable.db.TimeTableDB;

public class MainActivity extends Activity {

    static final String tag = "TimeTable";

    MainLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContext();
        layout = (MainLayout) LayoutInflater.from(this).inflate(R.layout.main_layout, null);
        setContentView(layout);
    }

    private void initContext() {
        TimeTableContext.db = new TimeTableDB(this);
        TimeTableContext.mainActivity = this;
        TimeTableContext.preferences = TimeTablePreferences.getPreferences(this);
        TimeTableContext.tf = Typeface.createFromAsset(getAssets(), "fonts/EraserRegular.ttf");
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout.refresh();
        layout.updateTheme();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TimeTableContext.db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_new:
                SubjectNameManager.showSubjectNameAlert(this, -1);
                break;
            case R.id.menu_prefs:
                startActivity(new Intent(this, TimeTablePreferences.class));
                break;
            case R.id.menu_times:
                startActivity(new Intent(this, TimesActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
