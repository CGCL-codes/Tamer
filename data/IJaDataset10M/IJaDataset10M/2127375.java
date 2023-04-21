package com.rudolfheszele.smsselector.controller;

import com.rudolfheszele.smsselector.model.ActionFactory;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class SmsSelectorRuleCreatorActionSpinnerAdapter implements SpinnerAdapter {

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        if (convertView == null) {
            textView = new TextView(parent.getContext());
        } else textView = (TextView) convertView;
        textView.setText(ActionFactory.getActionId(position).getName());
        textView.setTextSize(15);
        return textView;
    }

    @Override
    public int getCount() {
        return ActionFactory.getNumberOfActions();
    }

    @Override
    public Object getItem(int position) {
        return ActionFactory.getActionId(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getDropDownView(position, convertView, parent);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
}
