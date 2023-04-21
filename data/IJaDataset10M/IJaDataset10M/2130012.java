package cn.chengdu.in.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.chengdu.in.android.app.LoadableListAct;
import cn.chengdu.in.android.basic.DataFetcherListener;
import cn.chengdu.in.android.basic.HttpDataFetcher;
import cn.chengdu.in.android.config.Config;
import cn.chengdu.in.android.util.RemoteResourceManager;
import cn.chengdu.in.android.widget.BannerView;
import cn.chengdu.in.android.widget.TabButtonBarView;
import cn.chengdu.in.android.widget.TimeLineAdapter;
import cn.chengdu.in.android.widget.RefreshLoadMoreListView.OnLoadMoreListener;
import cn.chengdu.in.android.widget.TabButtonBarView.OnTabClickListener;
import cn.chengdu.in.api.ApiManager;
import cn.chengdu.in.api.ApiV1;
import cn.chengdu.in.type.Banner;
import cn.chengdu.in.type.Feed;
import cn.chengdu.in.type.IcdList;
import cn.chengdu.in.type.Place;
import cn.chengdu.in.type.Post;
import cn.chengdu.in.type.User;

/**
 * 
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-3-10
 */
public class TimeLineAct extends LoadableListAct implements OnLoadMoreListener, OnTabClickListener, DataFetcherListener<Banner> {

    private static final String TAG = "TimeLineAct";

    private static final boolean DEBUG = Config.DEBUG;

    private App mApp;

    private RemoteResourceManager mRrm;

    private TimeLineAdapter mListAdapter;

    private StateHolder mStateHolder;

    private HttpDataFetcher<Banner> mFetcher;

    private BannerView mBanner;

    private boolean mRecevierSet = false;

    /**
     * 关注的人
     */
    public static final int TYPE_USER_FOLLOW = 1;

    /**
     * 附近
     */
    public static final int TYPE_USER_NEARBY = 2;

    /**
     * 煎鱼风云
     */
    public static final int TYPE_PRISON = 3;

    public static final int REQUEST_VIEW_POST = 1001;

    private BroadcastReceiver mLocationRecevier = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mRecevierSet) {
                mRecevierSet = false;
                mStateHolder.reload(TimeLineAct.this);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (App) this.getApplicationContext();
        mRrm = mApp.getmRrm();
        mStateHolder = new StateHolder();
        mStateHolder.mType = getIntent().getIntExtra("type", TYPE_USER_FOLLOW);
        mListAdapter = new TimeLineAdapter(this, mRrm, TYPE_PRISON);
        mListView.setAdapter(mListAdapter);
        mListView.setOnLoadMoreListener(this);
        mStateHolder.startTask(this);
        if (mStateHolder.mType != TYPE_PRISON) {
            initListTitle();
            mFetcher = mApp.getApiManager().getNewestBanner();
            mFetcher.setOnDataFetcherListener(this);
            mFetcher.fetch();
        } else {
            setTitleText(R.string.title_blackroom);
            findViewById(R.id.titleText).setOnClickListener(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStateHolder.cancelTask();
        mListAdapter.removeObserver();
        unregisterReceiver(mLocationRecevier);
        if (mFetcher != null) {
            mFetcher.stop();
        }
        if (mBanner != null) {
            mBanner.deleteObserver();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListAdapter.setObserver();
        registerReceiver(mLocationRecevier, new IntentFilter(App.INTENT_ACTION_LOCATION_CHANGE));
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        MenuItem menuLocation = menu.findItem(R.id.location);
        if (menuLocation != null) {
            menuLocation.setVisible(false);
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        if (mStateHolder.mType == TYPE_PRISON) {
            mi.inflate(R.menu.prison, menu);
            return true;
        } else {
            mi.inflate(R.menu.timeline, menu);
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public void onDataError(Exception e) {
    }

    @Override
    public void onDataFetch(Banner result) {
        addBanner(result);
    }

    public void addBanner(Banner banner) {
        mBanner = new BannerView(this, banner, mApp);
        LinearLayout main = (LinearLayout) findViewById(R.id.main);
        main.addView(mBanner, 1);
        mBanner.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()) {
            case R.id.location:
                checkLocation();
                break;
            case R.id.prisonBroken:
                intent = new Intent(this, StartAct.class);
                intent.putExtra("isLogout", true);
                startActivity(intent);
                finish();
                break;
            case R.id.why:
                Toast.makeText(this, R.string.toast_prison, Toast.LENGTH_LONG).show();
                break;
            case R.id.labor:
                intent = new Intent(this, CreatePostAct.class);
                intent.putExtra("type", CreatePostAct.TYPE_LABOR);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Feed feed = (Feed) mListAdapter.getItem(position - mListView.getHeaderViewsCount());
        Intent intent = null;
        int type = feed.getType();
        if (type == Feed.TYPE_ENTER_PRISON || type == Feed.TYPE_IN_PRISON || type == Feed.TYPE_LEAVE_PRISON) {
            return;
        }
        if (type == Feed.TYPE_TIP || type == Feed.TYPE_CHECK_IN) {
            Post post = feed.getPost();
            post.setType(feed.getType());
            User user = feed.getUser();
            Place place = feed.getPlace();
            post.setUser(user);
            post.setPlace(place);
            intent = new Intent(this, PostInfoAct.class);
            intent.putExtra("post", post);
            startActivity(intent);
        } else if (type == Feed.TYPE_LIKE_CHECKIN || type == Feed.TYPE_LIKE_COMENT) {
            Post post = feed.getPost();
            post.setType(feed.getType() == Feed.TYPE_LIKE_CHECKIN ? ApiV1.TYPE_POST_CHECKIN : ApiV1.TYPE_POST_TIP);
            Place place = feed.getPlace();
            post.setPlace(place);
            intent = new Intent(this, PostInfoAct.class);
            intent.putExtra("post", post);
            startActivity(intent);
        } else if (type == Feed.TYPE_CHANGE_AVATAR || type == Feed.TYPE_CHANGE_MSG) {
            intent = new Intent(this, UserInfoAct.class);
            intent.putExtra("user", feed.getUser());
            startActivity(intent);
        } else {
            intent = new Intent(this, BadgeInfoAct.class);
            intent.putExtra("badge", feed.getBadge());
            intent.putExtra("user", feed.getUser());
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mStateHolder.mType != TYPE_PRISON) {
                    Intent intent = new Intent();
                    intent.putExtra("index", MainAct.TAB_INDEX_EXIT);
                    sendBroadcast(intent);
                    return false;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnTabClick(int index) {
        switch(index) {
            case 0:
                mStateHolder.mType = TYPE_USER_FOLLOW;
                mStateHolder.clear(this);
                mStateHolder.refresh(this);
                break;
            case 1:
                mStateHolder.mType = TYPE_USER_NEARBY;
                mStateHolder.clear(this);
                if (mApp.getLastLocation() == null) {
                    checkLocation();
                } else {
                    mStateHolder.refresh(this);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        mStateHolder.refresh(this);
        setLoadingView();
    }

    @Override
    public void onLoadMore() {
        if (mStateHolder.hasMore()) {
            mStateHolder.loadMore(this);
        } else {
            mListView.onNotAnyMore();
        }
    }

    @Override
    public void onRetry() {
        onRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mListAdapter.notifyDataSetChanged();
    }

    private void onTaskComplete(IcdList<Feed> result, Exception ex) {
        if (result != null) {
            mStateHolder.mHasMore = result.size() == Config.QUERY_PAGE_SIZE;
            if (mStateHolder.isFirstPage()) {
                if (result.size() == 0) {
                    if (mStateHolder.mType == TYPE_PRISON) {
                        setEmptyView(R.string.no_search_results_prison);
                    } else if (mStateHolder.mType == TYPE_USER_FOLLOW) {
                        setEmptyView(R.string.no_search_results_timeline);
                    } else {
                        setEmptyView();
                    }
                }
                mListAdapter.setList(result);
                mListView.onRefreshComplete(true);
                mListView.setSelection(1);
            } else {
                mListAdapter.addList(result);
                mListView.onLoadMoreComplete(true);
            }
            if (mStateHolder.mHasMore) {
                mListView.resetFooter();
            }
            mStateHolder.mPage++;
        } else {
            if (mStateHolder.isFirstPage()) {
                mListView.onRefreshError();
                setErrorView(ex);
            } else {
                mListView.onLoadMoreComplete(false);
            }
        }
        mStateHolder.cancelTask();
    }

    private void checkLocation() {
        mRecevierSet = true;
    }

    private void initListTitle() {
        LinearLayout main = (LinearLayout) findViewById(R.id.main);
        TabButtonBarView view = new TabButtonBarView(this, TabButtonBarView.TYPE_WITH_BUTTON, new int[] { R.string.tab_title_follow, R.string.tab_title_nearby });
        view.setOnTabClickListener(this);
        main.addView(view, 0);
    }

    private static class TimeLineTask extends AsyncTask<Void, Void, IcdList<Feed>> {

        private TimeLineAct mActivity;

        private StateHolder mStateHolder;

        private Exception exception;

        public TimeLineTask(TimeLineAct activity, StateHolder holder) {
            mActivity = activity;
            mStateHolder = holder;
        }

        @Override
        protected IcdList<Feed> doInBackground(Void... params) {
            try {
                ApiManager api = mActivity.mApp.getApiManager();
                String page = mStateHolder.mPage + "";
                switch(mStateHolder.mType) {
                    case TYPE_USER_FOLLOW:
                        return api.getFeedList(page, null);
                    case TYPE_USER_NEARBY:
                        return api.getFeedListNearby(page, null, mActivity.mApp.getLastLocation(), null);
                    case TYPE_PRISON:
                        return api.getPrisonFeed(page);
                }
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(IcdList<Feed> result) {
            mActivity.onTaskComplete(result, exception);
        }
    }

    private static class StateHolder {

        private int mMaxId = -1;

        private int mPage = 1;

        private int mType = TYPE_USER_FOLLOW;

        private boolean mHasMore = true;

        private TimeLineTask mTask;

        public StateHolder() {
        }

        public void startTask(TimeLineAct activity) {
            mTask = new TimeLineTask(activity, this);
            mTask.execute();
        }

        public boolean hasMore() {
            return mHasMore;
        }

        public boolean isFirstPage() {
            return mPage == 1;
        }

        public void cancelTask() {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
            }
        }

        public void clear(TimeLineAct activity) {
            activity.mListView.onNotAnyMore();
            activity.setLoadingView();
            activity.mListAdapter.clearAll();
        }

        public void refresh(TimeLineAct activity) {
            cancelTask();
            mPage = 1;
            startTask(activity);
        }

        public void loadMore(TimeLineAct activity) {
            if (mTask == null) {
                startTask(activity);
            }
        }

        public void reload(TimeLineAct activity) {
            activity.setLoadingView();
            activity.mListAdapter.clearAll();
            refresh(activity);
        }
    }
}
