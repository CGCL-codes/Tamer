package com.michael.gps.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

/**
 * @author zhanghongze
 * 
 */
public class MyFriendsView extends ListActivity {

    public List<Map<String, Object>> mData;

    public int tab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getData();
        MyAdapter adapter = new MyAdapter(this, null);
        setListAdapter(adapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("title", "G1");
        map.put("info", "google 1");
        list.add(map);
        return list;
    }
}
