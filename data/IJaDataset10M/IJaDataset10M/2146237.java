package com.szxys.mhub.ui.virtualui;

import java.util.ArrayList;
import java.util.HashMap;
import com.szxys.mhub.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ButtonApapterExpansion extends BaseAdapter {

    private class buttonViewHolder {

        ImageView appIcon;

        TextView appdoctorName;

        TextView appTime;

        TextView appContent;

        Button buttonDelete;
    }

    private ArrayList<HashMap<String, Object>> mAppList;

    private LayoutInflater mInflater;

    private Context mContext;

    private String[] keyString;

    private int[] valueViewID;

    private buttonViewHolder holder;

    private ArrayList<Integer> deletevalueList;

    public ButtonApapterExpansion(Context c, ArrayList<HashMap<String, Object>> appList, int resource, String[] from, int[] to) {
        mAppList = appList;
        mContext = c;
        deletevalueList = new ArrayList<Integer>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }

    public ArrayList<Integer> getDeletList() {
        return deletevalueList;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(ArrayList<Integer> postionList) {
        ArrayList<HashMap<String, Object>> SubList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < postionList.size(); i++) {
            SubList.add(mAppList.get(postionList.get(i)));
        }
        mAppList.removeAll(SubList);
        deletevalueList.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            holder = (buttonViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.pfphone_del_errormessageitem, null);
            holder = new buttonViewHolder();
            holder.appIcon = (ImageView) convertView.findViewById(valueViewID[0]);
            holder.appdoctorName = (TextView) convertView.findViewById(valueViewID[1]);
            holder.appTime = (TextView) convertView.findViewById(valueViewID[2]);
            holder.appContent = (TextView) convertView.findViewById(valueViewID[3]);
            holder.buttonDelete = (Button) convertView.findViewById(valueViewID[4]);
            convertView.setTag(holder);
        }
        HashMap<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            int ico = (Integer) appInfo.get(keyString[0]);
            String aname = (String) appInfo.get(keyString[1]);
            String atime = (String) appInfo.get(keyString[2]);
            String acontent = (String) appInfo.get(keyString[3]);
            holder.appdoctorName.setText(aname);
            holder.appTime.setText(atime);
            holder.appContent.setText(acontent);
            ;
            holder.appIcon.setImageResource(ico);
            holder.buttonDelete.setOnClickListener(new lvButtonListener(position));
            if (!deletevalueList.contains(position)) {
                holder.buttonDelete.setBackgroundResource(R.drawable.pfphone_delete);
            } else {
                holder.buttonDelete.setBackgroundResource(R.drawable.pfphone_delete_pressed);
            }
        }
        return convertView;
    }

    class lvButtonListener implements OnClickListener {

        private int position;

        lvButtonListener(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == holder.buttonDelete.getId()) {
                if (deletevalueList.contains(position)) {
                    deletevalueList.remove((Object) position);
                    v.findViewById(R.id.imgBtn).setBackgroundResource(R.drawable.pfphone_delete);
                } else {
                    deletevalueList.add(position);
                    v.findViewById(R.id.imgBtn).setBackgroundResource(R.drawable.pfphone_delete_pressed);
                }
            }
        }
    }
}
