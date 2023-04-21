package com.hcs.activity.infomanager;

import com.hcs.R;
import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import com.hcs.application.ActivityContainerApp;
import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RqSalesmanMonthAct extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ActivityContainerApp) getApplication()).setLayer3(this);
        setContentView(R.layout.reportquerysalesmanmonth);
        String TEMP = "report_temp";
        SharedPreferences pre = getSharedPreferences(TEMP, MODE_WORLD_WRITEABLE);
        String conse = pre.getString("MorW", "");
        String[] contentse = conse.split(";");
        ListView list = (ListView) findViewById(R.id.ListView);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        if (!conse.equals("")) {
            for (int i = 0; i < contentse.length; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                String[] con = contentse[i].split("\\|");
                map.put("tv1", con[0]);
                map.put("tv2", con[1]);
                map.put("tv3", con[2]);
                listItem.add(map);
            }
        }
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.reportquerysalesmanmonth_item, new String[] { "tv1", "tv2", "tv3" }, new int[] { R.id.tv1, R.id.tv2, R.id.tv3 });
        list.setAdapter(listItemAdapter);
    }
}
