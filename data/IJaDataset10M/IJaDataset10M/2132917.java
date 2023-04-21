package com.whale.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * 
 * 基础性的ArrayList Adapter封装
 * 
 * @author lzk
 * 
 */
public abstract class BaseArrayListAdapter<T> extends BaseAdapter {

    protected ArrayList<T> list;

    protected Activity context;

    protected ListView listView;

    public BaseArrayListAdapter(Activity context) {
        this.context = context;
        list = new ArrayList<T>();
    }

    @Override
    public int getCount() {
        if (list != null) return list.size(); else return 0;
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
	 * 此抽象方法用于对视图的内容填充
	 */
    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public void setList(ArrayList<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setCopyList(ArrayList<T> list) {
        this.list.clear();
        if (list != null) this.list.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(T[] list) {
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
