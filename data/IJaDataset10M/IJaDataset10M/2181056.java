package org.amarena2d.examples.android;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import org.amarena2d.examples.Examples;
import java.util.List;
import java.util.Map;

public class ExamplesList extends ExpandableListActivity {

    MyExpandableListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MyExpandableListAdapter();
        setListAdapter(adapter);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Examples.Item example = adapter.getExample(groupPosition, childPosition);
        Intent intent = new Intent(this, SimpleScenePlayer.class);
        Bundle bundle = new Bundle();
        bundle.putString("scene", example.sceneClass.getName());
        intent.putExtras(bundle);
        startActivity(intent);
        return true;
    }

    public class MyExpandableListAdapter extends BaseExpandableListAdapter {

        Map<String, List<Examples.Item>> exampels;

        private String[] groups;

        public MyExpandableListAdapter() {
            exampels = Examples.createExamples();
            groups = new String[exampels.size()];
            int i = 0;
            for (String group : exampels.keySet()) {
                groups[i++] = group;
            }
        }

        public Examples.Item getExample(int groupPosition, int childPosition) {
            return exampels.get(groups[groupPosition]).get(childPosition);
        }

        public Object getChild(int groupPosition, int childPosition) {
            return exampels.get(groups[groupPosition]).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return exampels.get(groups[groupPosition]).size();
        }

        public TextView getGenericView() {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(ExamplesList.this);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            textView.setPadding(56, 5, 5, 5);
            textView.setTextSize(28);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(exampels.get(groups[groupPosition]).get(childPosition).name);
            textView.setTextSize(18);
            return textView;
        }

        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        public int getGroupCount() {
            return groups.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(groups[groupPosition]);
            return textView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }
    }
}
