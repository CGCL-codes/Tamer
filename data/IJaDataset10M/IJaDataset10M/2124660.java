package com.baozou.app.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baozou.R;

public class BookmarkListAdapter extends BaseAdapter {

    Context context;

    List<ContentValues> data;

    LayoutInflater inflater;

    public BookmarkListAdapter(Context c, List<ContentValues> data) {
        this.context = c;
        this.data = data;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, Object> viewHolder = null;
        if (convertView == null) {
            viewHolder = new HashMap<String, Object>();
            convertView = inflater.inflate(R.layout.bookmark_list_item, null);
            viewHolder.put("text1", convertView.findViewById(R.id.bookmark_list_item_text1));
            viewHolder.put("text2", convertView.findViewById(R.id.bookmark_list_item_text2));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Map<String, Object>) convertView.getTag();
        }
        ((TextView) viewHolder.get("text1")).setText((position + 1) + "��");
        ((TextView) viewHolder.get("text2")).setText(data.get(position).getAsString("bk_name"));
        return convertView;
    }
}
