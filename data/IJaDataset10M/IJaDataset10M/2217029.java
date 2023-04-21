package com.clarionmedia.feedreader.application;

import java.util.Collections;
import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.clarionmedia.feedreader.domain.Feed;
import com.clarionmedia.feedreader.launcher.R;
import com.clarionmedia.feedreader.service.FeedReaderService;
import com.clarionmedia.feedreader.service.FeedReaderServiceImpl;
import com.clarionmedia.feedreader.util.Constants;
import com.clarionmedia.feedreader.util.DialogUtil;
import com.clarionmedia.feedreader.util.FeedAdapter;
import com.clarionmedia.feedreader.util.FeedComparator;

/**
 * Displays a list of Feeds of the selected Category.
 * 
 * @author Tyler Treat
 * 
 */
public class FeedList extends ListActivity {

    private FeedReaderService mService;

    private long mId;

    private List<Feed> mFeeds;

    private FeedAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = new FeedReaderServiceImpl(this);
        setContentView(R.layout.listview);
        TextView noData = (TextView) findViewById(android.R.id.empty);
        noData.setText("No Feeds");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mId = extras.getLong(Constants.CAT_ID_KEY);
        }
        mFeeds = mService.getFeedsByCategoryId(mId);
        Collections.sort(mFeeds, new FeedComparator());
        mAdapter = new FeedAdapter(this, R.layout.list_item, mFeeds);
        setListAdapter(mAdapter);
        registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Feed feed = (Feed) getListAdapter().getItem(position);
        if (feed != null) {
            long feedId = feed.getId();
            Intent i = new Intent(this, FeedView.class);
            i.putExtra(Constants.FEED_ID_KEY, feedId);
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ADD_FEED_CODE) {
            if (resultCode == RESULT_OK) {
                mFeeds = mService.getFeedsByCategoryId(mId);
                Collections.sort(mFeeds, new FeedComparator());
                mAdapter = new FeedAdapter(this, R.layout.list_item, mFeeds);
                setListAdapter(mAdapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addFeed:
                Intent intent = new Intent(this, AddFeed.class);
                intent.putExtra(Constants.ADD_FEED_INCAT, true);
                intent.putExtra(Constants.CAT_ID_KEY, mId);
                startActivityForResult(intent, Constants.ADD_FEED_CODE);
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == getListView().getId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Feed f = mFeeds.get(info.position);
            menu.setHeaderTitle(f.getName());
            List<Feed> favorites = mService.getFavorites();
            Boolean isFavorite = false;
            for (Feed feed : favorites) {
                if (feed.getId() == f.getId()) {
                    isFavorite = true;
                    break;
                }
            }
            String favoritesStr;
            if (isFavorite) favoritesStr = "Remove from Favorites"; else favoritesStr = "Add to Favorites";
            String[] menuItems = { "Edit Feed", "Delete Feed", favoritesStr, "Cancel" };
            for (int i = 0; i < menuItems.length; i++) menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        Feed feed = mFeeds.get(info.position);
        List<Feed> favorites = mService.getFavorites();
        Boolean isFavorite = false;
        for (Feed f : favorites) {
            if (f.getId() == feed.getId()) {
                isFavorite = true;
                break;
            }
        }
        switch(menuItemIndex) {
            case 0:
                Intent intent = new Intent(this, AddFeed.class);
                intent.putExtra(Constants.EDIT_FEED, true);
                intent.putExtra(Constants.FEED_ID_KEY, feed.getId());
                intent.putExtra(Constants.FEED_NAME_KEY, feed.getName());
                intent.putExtra(Constants.CAT_ID_KEY, feed.getCategoryId());
                intent.putExtra(Constants.FEED_DESC_KEY, feed.getDescription());
                intent.putExtra(Constants.FEED_IMG_KEY, feed.getImageUrl());
                intent.putExtra(Constants.FEED_SRC_KEY, feed.getSourceUrl());
                intent.putExtra(Constants.FEED_RSS_KEY, feed.getRss());
                startActivityForResult(intent, Constants.ADD_FEED_CODE);
                break;
            case 1:
                DialogUtil.createDeleteFeedDialog(this, mService, (FeedAdapter) getListAdapter(), getListView(), feed).show();
                break;
            case 2:
                if (isFavorite) mService.removeFromFavorites(feed); else mService.addToFavorites(feed);
                mFeeds = mService.getFeedsByCategoryId(mId);
                Collections.sort(mFeeds, new FeedComparator());
                mAdapter = new FeedAdapter(this, R.layout.list_item, mFeeds);
                getListView().setAdapter(mAdapter);
            case 3:
                break;
        }
        return true;
    }
}
