package cn.chengdu.in.android.widget;

import java.util.Observable;
import java.util.Observer;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import cn.chengdu.in.android.R;
import cn.chengdu.in.android.config.Config;
import cn.chengdu.in.android.util.RemoteResourceManager;
import cn.chengdu.in.type.Badge;
import cn.chengdu.in.type.IcdList;

/**
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-8-12
 */
public class BadgeExListAdapter extends BaseExpandableListAdapter implements OnClickListener {

    public static final boolean DEBUG = Config.DEBUG;

    public static final String TAG = "BadgeExListAdapter";

    private IcdList<IcdList<Badge>> list;

    private LayoutInflater mInflater;

    private RemoteResourceManager mRrm;

    private Handler mHandler;

    private RemoteResourceManagerObserver mResourcesObserver;

    private OnClickListener mOnClickListener;

    public BadgeExListAdapter(Context context, RemoteResourceManager rrm) {
        mInflater = LayoutInflater.from(context);
        mRrm = rrm;
        mHandler = new Handler();
        mResourcesObserver = new RemoteResourceManagerObserver();
        mRrm.addObserver(mResourcesObserver);
    }

    public void removeObserver() {
        mRrm.deleteObserver(mResourcesObserver);
    }

    @Override
    public Badge getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (int) Math.ceil(list.get(groupPosition).size() / 3.0);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.badge_child, null);
            holder = new ViewHolder();
            holder.badges[0] = (BadgeButton) convertView.findViewById(R.id.b1);
            holder.badges[1] = (BadgeButton) convertView.findViewById(R.id.b2);
            holder.badges[2] = (BadgeButton) convertView.findViewById(R.id.b3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IcdList<Badge> badges = getGroup(groupPosition);
        int count = 3;
        if (isLastChild) {
            count = list.get(groupPosition).size() - (childPosition * 3);
        }
        int i = 0;
        for (; i < count; i++) {
            Badge badge = badges.get(i + childPosition * 3);
            holder.badges[i].setData(badge, mRrm);
            holder.badges[i].setVisibility(View.VISIBLE);
            holder.badges[i].setOnClickListener(mOnClickListener);
        }
        for (; i < 3; i++) {
            holder.badges[i].setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public IcdList<Badge> getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.badge_parent, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.title.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IcdList<Badge> badges = getGroup(groupPosition);
        holder.title.setText(badges.getTitle());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setList(IcdList<IcdList<Badge>> list) {
        this.list = list;
    }

    public void setOnBadgeClickListener(OnClickListener l) {
        mOnClickListener = l;
    }

    private class RemoteResourceManagerObserver implements Observer {

        @Override
        public void update(Observable observable, final Object data) {
            if (DEBUG) Log.d(TAG, "Fetcher got: " + data);
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    private static class ViewHolder {

        BadgeButton[] badges = new BadgeButton[3];

        TextView title;
    }
}
