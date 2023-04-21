package com.cnc.mediaconnect.filed;

import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;
import com.cnc.mediaconnect.adapters.InteractionsAdapter;
import com.cnc.mediaconnect.data.SearchItem;

public class InteractionFiled {

    private String id;

    private int type;

    private List<SearchItem> listdata = new ArrayList<SearchItem>();

    private InteractionsAdapter adapter;

    private TextView tVCount;

    private int maxItem;

    public int getMaxItem() {
        return maxItem;
    }

    public void setMaxItem(int maxItem) {
        this.maxItem = maxItem;
    }

    public TextView gettVCount() {
        return tVCount;
    }

    public void settVCount(TextView tVCount) {
        this.tVCount = tVCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SearchItem> getListdata() {
        return listdata;
    }

    public void setListdata(List<SearchItem> listdata) {
        this.listdata = listdata;
    }

    public InteractionsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(InteractionsAdapter adapter) {
        this.adapter = adapter;
    }
}
