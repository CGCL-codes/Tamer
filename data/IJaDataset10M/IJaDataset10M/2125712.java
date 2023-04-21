package com.angis.fx.activity.jcdj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.angis.fx.activity.R;

public class CheckTypeActivity extends ListActivity {

    private Intent lIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleAdapter lSimpleAdapter = new SimpleAdapter(this, getData(), com.angis.fx.activity.R.layout.checktype, new String[] { "title", "img" }, new int[] { com.angis.fx.activity.R.id.checktype_title, com.angis.fx.activity.R.id.checktype_img });
        setListAdapter(lSimpleAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("debug", id + "");
        if (0 == position) {
            lIntent = new Intent();
            lIntent.setClass(CheckTypeActivity.this, DailyCheckActivity.class);
            CheckTypeActivity.this.startActivity(lIntent);
        } else {
            lIntent = new Intent();
            lIntent.setClass(CheckTypeActivity.this, NoCedulaCheckActivity.class);
            CheckTypeActivity.this.startActivity(lIntent);
        }
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> lData = new ArrayList<Map<String, Object>>();
        Map<String, Object> lMap = new HashMap<String, Object>();
        lMap.put("title", "有证检查");
        lMap.put("img", R.drawable.cedula1);
        lData.add(lMap);
        lMap = new HashMap<String, Object>();
        lMap.put("title", "无证检查");
        lMap.put("img", R.drawable.cedula2);
        lData.add(lMap);
        return lData;
    }
}
