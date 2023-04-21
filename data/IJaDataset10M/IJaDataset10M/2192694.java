package org.opengpx;

import java.util.ArrayList;
import org.opengpx.lib.CacheDatabase;
import org.opengpx.lib.CacheIndexItem;
import org.opengpx.lib.Coordinates;
import org.opengpx.lib.NavigationInfo;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class SearchListAdapter extends CacheListAdapter {

    SearchListAdapter(Activity context, ArrayList<String> items) {
        super(context, items);
    }

    /**
	 * 
	 */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CacheListViewHolder cacheListViewHolder;
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.cachelistitem, null);
            cacheListViewHolder = new CacheListViewHolder();
            cacheListViewHolder.twoLineListItem = (TwoLineListItem) convertView.findViewById(R.id.CacheItem);
            cacheListViewHolder.icon = (ImageView) convertView.findViewById(R.id.CacheIcon);
            convertView.setTag(cacheListViewHolder);
        } else {
            cacheListViewHolder = (CacheListViewHolder) convertView.getTag();
        }
        final String strFilterableString = this.getItem(position);
        final CacheIndexItem cacheIndexItem = this.mCacheDatabase.getSearchCacheIndexItemForFilter(strFilterableString);
        if (cacheIndexItem != null) {
            super.drawCacheName(cacheIndexItem, cacheListViewHolder.twoLineListItem.getText1());
            final TextView tvLine2 = cacheListViewHolder.twoLineListItem.getText2();
            if (this.mCacheDatabase.getSortOrder() == CacheDatabase.SORT_ORDER_NAME) {
                if (cacheIndexItem.vote > 0) {
                    tvLine2.setText(String.format("%s [D/T: %s/%s] [V:%.2f]", cacheIndexItem.container.toString().replace("_", " "), cacheIndexItem.difficulty, cacheIndexItem.terrain, cacheIndexItem.vote).replace(",", "."));
                } else {
                    tvLine2.setText(String.format("%s [D/T: %s/%s]", cacheIndexItem.container.toString().replace("_", " "), cacheIndexItem.difficulty, cacheIndexItem.terrain));
                }
            } else {
                final Coordinates coordinates = new Coordinates(cacheIndexItem.latitude, cacheIndexItem.longitude);
                final NavigationInfo navInfo = this.mReferenceCoordinates.getNavigationInfoTo(coordinates);
                if (cacheIndexItem.vote > 0) tvLine2.setText(String.format("Distance: %s [V:%.2f]", navInfo.toString(), cacheIndexItem.vote).replace(",", ".")); else tvLine2.setText(String.format("Distance: %s", navInfo.toString()));
            }
            cacheListViewHolder.icon.setImageDrawable(this.getIcon(parent, cacheIndexItem.type));
        }
        return convertView;
    }
}
