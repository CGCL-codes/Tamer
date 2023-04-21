package org.openintents.historify.data.adapters;

import java.util.ArrayList;
import java.util.List;
import org.openintents.historify.R;
import org.openintents.historify.data.loaders.SourceIconHelper;
import org.openintents.historify.data.loaders.SourceLoader;
import org.openintents.historify.data.model.source.EventSource;
import org.openintents.historify.data.model.source.EventSource.SourceState;
import org.openintents.historify.ui.SourcesActivity;
import org.openintents.historify.ui.fragments.SourcesConfigurationFragment;
import org.openintents.historify.uri.ContentUris;
import org.openintents.historify.utils.WebsiteHelper;
import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.text.Spannable;
import android.text.style.RelativeSizeSpan;
import android.text.style.URLSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * 
 * Adapter for the list of sources on {@link SourcesActivity}. Provides sources
 * and their filtered state for all contact.
 * 
 * @author berke.andras
 */
public class SourcesAdapter extends BaseAdapter {

    private static final int HEADER_OFFSET = 1;

    protected static final int VIEW_TYPE_HEADER = 0;

    protected static final int VIEW_TYPE_ITEM = 1;

    protected static final int VIEW_TYPE_NEED_MORE_MESSAGE = 2;

    protected Context mContext;

    protected int mListItemResId;

    protected SourceLoader mSourceLoader;

    protected SourceIconHelper mSourceIconHelper;

    protected List<EventSource> mSources;

    protected List<EventSource> mInternalSources;

    protected List<EventSource> mExternalSources;

    protected SparseBooleanArray mCheckedItems;

    private SourcesChangedObserver mObserver;

    /**
	 * Observer for the list. If the list of sources changes, the data set will
	 * be refreshed.
	 */
    private class SourcesChangedObserver extends ContentObserver {

        public SourcesChangedObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            load();
        }
    }

    /** Public constructor. */
    public SourcesAdapter(Activity context, ListView listView) {
        init(context, listView, new SourceLoader(ContentUris.Sources), R.layout.listitem_source);
    }

    /** Constructor for derived classes to override default behaviour. */
    protected SourcesAdapter(Activity context, ListView listView, SourceLoader sourceLoader, int listItemResId) {
        init(context, listView, sourceLoader, listItemResId);
    }

    protected SourcesAdapter() {
    }

    /**
	 * Init method called by the different constructor of this class and derived
	 * classes.
	 */
    protected void init(Context context, ListView listView, SourceLoader sourceLoader, int listItemResId) {
        mContext = context;
        mSourceLoader = sourceLoader;
        mSourceIconHelper = new SourceIconHelper();
        mSources = new ArrayList<EventSource>();
        mInternalSources = new ArrayList<EventSource>();
        mExternalSources = new ArrayList<EventSource>();
        mCheckedItems = listView.getCheckedItemPositions();
        mListItemResId = listItemResId;
        load();
    }

    /**
	 * Loading external and internal sources with their state.
	 * 
	 * Registers content observer.
	 */
    public void load() {
        if (mObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mObserver);
            mObserver = null;
        }
        mInternalSources.clear();
        mExternalSources.clear();
        mSources.clear();
        Cursor c = mSourceLoader.openCursor(mContext);
        for (int i = 0; i < c.getCount(); i++) {
            EventSource source = mSourceLoader.loadFromCursor(c, i);
            if (source != null) {
                mSources.add(source);
                if (source.isInternal()) mInternalSources.add(source); else mExternalSources.add(source);
            }
        }
        c.close();
        mObserver = new SourcesChangedObserver(new Handler());
        mContext.getContentResolver().registerContentObserver(mSourceLoader.getUri(), true, mObserver);
        notifyDataSetChanged();
    }

    /** Update the enabled / disabled state of a source */
    public void update(EventSource source) {
        mSourceLoader.updateItemState(mContext, source);
    }

    /** Update the enabled / disabled state of all sources */
    public void updateAll(SourceState newState) {
        mSourceLoader.updateAllItemState(mContext, newState);
    }

    /**
	 * Gets the set of loaded items.
	 * 
	 * @return List of all sources loaded.
	 */
    public List<EventSource> getItems() {
        return mSources;
    }

    /**
	 * Called by onDestroy() to release the cursor an unregister the content
	 * observer.
	 */
    public void release() {
        if (mObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mObserver);
            mObserver = null;
        }
    }

    public int getCount() {
        return mInternalSources.size() + mExternalSources.size() + HEADER_OFFSET + 1;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mInternalSources.size()) return VIEW_TYPE_HEADER; else if (position == mInternalSources.size() + HEADER_OFFSET + mExternalSources.size()) {
            return VIEW_TYPE_NEED_MORE_MESSAGE;
        } else return VIEW_TYPE_ITEM;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != VIEW_TYPE_HEADER;
    }

    public long getItemId(int position) {
        EventSource item = getItem(position);
        return item == null ? -1 : item.getId();
    }

    public EventSource getItem(int position) {
        if (getItemViewType(position) != VIEW_TYPE_ITEM) {
            return null;
        } else {
            return (position > mInternalSources.size()) ? mExternalSources.get(position - mInternalSources.size() - HEADER_OFFSET) : mInternalSources.get(position);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Integer viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_HEADER) {
            if (convertView == null || !viewType.equals(convertView.getTag())) {
                convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.preference_category, null);
            }
            ((TextView) convertView).setText(R.string.sources_external_sources);
        } else if (viewType == VIEW_TYPE_NEED_MORE_MESSAGE) {
            if (convertView == null || !viewType.equals(convertView.getTag())) {
                convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_empty_view, null);
                convertView.setVisibility(View.VISIBLE);
            }
            TextView tv = ((TextView) convertView);
            String text = mContext.getString(R.string.sources_need_mode);
            tv.setText(text, BufferType.SPANNABLE);
            ((Spannable) tv.getText()).setSpan(new RelativeSizeSpan(2.0f), 0, text.indexOf('\n'), 0);
            ((Spannable) tv.getText()).setSpan(new URLSpan(new WebsiteHelper().getMoreInfoURL()), text.lastIndexOf('\n') + 1, text.length(), 0);
            convertView.setBackgroundResource((position - 1) % 2 == 0 ? R.drawable.listitem_background1 : R.drawable.listitem_background2);
        } else {
            if (convertView == null || !viewType.equals(convertView.getTag())) {
                convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(mListItemResId, null);
                initListItem(convertView);
            }
            EventSource item = getItem(position);
            loadItemToView(convertView, item, position);
        }
        convertView.setTag(viewType);
        return convertView;
    }

    /**
	 * Initializes the state of the convertView after inflating. Derived classes
	 * could override this method.
	 * 
	 * @param convertView
	 *            the loaded view.
	 */
    protected void initListItem(View convertView) {
        View btnMore = convertView.findViewById(R.id.sources_listitem_btnMore);
        btnMore.setOnClickListener(new SourcesConfigurationFragment.OnMoreButtonClickedListener());
    }

    /**
	 * Initializes the content of the listview item. Derived classes could
	 * override this method.
	 * 
	 * @param convertView
	 *            the loaded view.
	 * @param item
	 *            the item to load.
	 * @param pos
	 *            position in the adapter.
	 */
    protected void loadItemToView(View convertView, EventSource item, int position) {
        int pos = item.isInternal() ? position : position - 1;
        convertView.setBackgroundResource(pos % 2 == 0 ? R.drawable.listitem_background1 : R.drawable.listitem_background2);
        TextView tv = (TextView) convertView.findViewById(R.id.sources_listitem_txtName);
        tv.setText(item.getName());
        ((CheckedTextView) tv).setChecked(item.isEnabled());
        mCheckedItems.put(position, item.isEnabled());
        tv = (TextView) convertView.findViewById(R.id.sources_listitem_txtDescription);
        tv.setText(item.getDescription() == null ? "" : item.getDescription());
        ImageView iv = (ImageView) convertView.findViewById(R.id.sources_listitem_imgIcon);
        mSourceIconHelper.toImageView(mContext, item, null, iv);
        View btnMore = convertView.findViewById(R.id.sources_listitem_btnMore);
        if (item.getConfigIntent() == null) btnMore.setVisibility(View.INVISIBLE); else {
            btnMore.setVisibility(View.VISIBLE);
            btnMore.setTag(item.getConfigIntent());
        }
    }
}
