package cn.chengdu.in.android.widget;

import java.util.Observable;
import java.util.Observer;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.chengdu.in.android.BadgeListAct;
import cn.chengdu.in.android.R;
import cn.chengdu.in.android.config.Config;
import cn.chengdu.in.android.util.ImageUtil;
import cn.chengdu.in.android.util.RemoteResourceManager;
import cn.chengdu.in.android.util.StringUtil;
import cn.chengdu.in.type.Badge;

/**
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-8-15
 */
public class BadgeListDetailAdapter extends BaseBadgeAdapter implements ObservableAdapter {

    private static final String TAG = "BadgeListDetailAdapter";

    private static final boolean DEBUG = Config.DEBUG;

    private LayoutInflater mInflater;

    private RemoteResourceManager mRrm;

    private Handler mHandler;

    private Context mContext;

    private RemoteResourceManagerObserver mResourcesObserver;

    private int mType;

    public BadgeListDetailAdapter(Context context, RemoteResourceManager rrm, int type) {
        super(context);
        mInflater = LayoutInflater.from(context);
        mHandler = new Handler();
        mRrm = rrm;
        mContext = context;
        mResourcesObserver = new RemoteResourceManagerObserver();
        mType = type;
    }

    public void removeObserver() {
        mRrm.deleteObserver(mResourcesObserver);
    }

    public void setObserver() {
        mRrm.addObserver(mResourcesObserver);
    }

    /**
     * Make a view to hold each row.
     * 
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.feed_badge_detail, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.smallIcon = (ImageView) convertView.findViewById(R.id.smallIcon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            if (mType == BadgeListAct.TYPE_PREFERENCE) {
                holder.title.setVisibility(View.GONE);
                holder.smallIcon.setImageResource(R.drawable.icon_badge_list_point);
            }
            if (mType == BadgeListAct.TYPE_USABLE) {
                holder.smallIcon.setImageResource(R.drawable.icon_badge_list_point);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Badge badge = getItem(position);
        Badge preBadge = getItem(position - 1);
        holder.name.setText(badge.getName());
        if (mType == BadgeListAct.TYPE_USABLE) {
            badge.setState(Badge.STATE_USABLE);
        }
        holder.state.setText(badge.getState(mContext));
        holder.desc.setText(badge.getDesc());
        ImageUtil.setImage(mRrm, holder.icon, badge.getThumbUri(), R.drawable.default_badge);
        if (mType != BadgeListAct.TYPE_PREFERENCE && mType != BadgeListAct.TYPE_USABLE && (preBadge == null || !preBadge.getGroupName().equals(badge.getGroupName()))) {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(badge.getGroupName());
        } else {
            holder.title.setVisibility(View.GONE);
        }
        if (mType == BadgeListAct.TYPE_PREFERENCE) {
            showIt(holder.time, holder.smallIcon);
            holder.time.setText(StringUtil.format(mContext, R.string.badge_place_count, badge.getPlaceCount()));
        } else if (mType == BadgeListAct.TYPE_USABLE) {
            showIt(holder.time, holder.smallIcon);
            if (badge.getGot() != null) {
                holder.time.setText(badge.getGot().getWhere().getPlacename());
                holder.smallIcon.setImageResource(R.drawable.icon_badge_list_point);
            } else if (badge.getEndDate() != null) {
                holder.smallIcon.setImageResource(R.drawable.icon_badge_list_time);
                holder.time.setText(badge.getBeginDate() + " - " + badge.getEndDate());
            } else {
                hideIt(holder.time, holder.smallIcon);
            }
        } else if (mType == BadgeListAct.TYPE_EVENT || mType == BadgeListAct.TYPE_HISTORY) {
            showIt(holder.time, holder.smallIcon);
            holder.smallIcon.setImageResource(R.drawable.icon_badge_list_time);
            holder.time.setText(badge.getBeginDate() + " - " + badge.getEndDate());
        } else if (mType == BadgeListAct.TYPE_PLACE) {
            if (badge.getEndDate() != null) {
                showIt(holder.smallIcon);
                holder.time.setText(badge.getBeginDate() + " - " + badge.getEndDate());
            } else {
                hideIt(holder.smallIcon);
                holder.time.setText("");
            }
        } else if (mType == BadgeListAct.TYPE_BASIC) {
            hideIt(holder.smallIcon);
        }
        if (mType == BadgeListAct.TYPE_EVENT) {
            showIt(holder.count);
            holder.count.setText(StringUtil.format(mContext, R.string.badge_got_count, badge.getAchievedCount()));
        }
        return convertView;
    }

    private class RemoteResourceManagerObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
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

        ImageView icon;

        ImageView smallIcon;

        TextView title;

        TextView name;

        TextView desc;

        TextView time;

        TextView count;

        TextView state;
    }
}
