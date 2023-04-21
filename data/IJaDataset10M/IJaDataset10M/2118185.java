package com.example.android.supportv4.app;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.example.android.supportv4.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.IntentCompat;
import android.support.v4.content.Loader;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Demonstration of the implementation of a custom Loader.
 */
public class LoaderCustomSupport extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            AppListFragment list = new AppListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }
    }

    /**
	 * This class holds the per-item data in our Loader.
	 */
    public static class AppEntry {

        public AppEntry(AppListLoader loader, ApplicationInfo info) {
            mLoader = loader;
            mInfo = info;
            mApkFile = new File(info.sourceDir);
        }

        public ApplicationInfo getApplicationInfo() {
            return mInfo;
        }

        public String getLabel() {
            return mLabel;
        }

        public Drawable getIcon() {
            if (mIcon == null) {
                if (mApkFile.exists()) {
                    mIcon = mInfo.loadIcon(mLoader.mPm);
                    return mIcon;
                } else {
                    mMounted = false;
                }
            } else if (!mMounted) {
                if (mApkFile.exists()) {
                    mMounted = true;
                    mIcon = mInfo.loadIcon(mLoader.mPm);
                    return mIcon;
                }
            } else {
                return mIcon;
            }
            return mLoader.getContext().getResources().getDrawable(android.R.drawable.sym_def_app_icon);
        }

        @Override
        public String toString() {
            return mLabel;
        }

        void loadLabel(Context context) {
            if (mLabel == null || !mMounted) {
                if (!mApkFile.exists()) {
                    mMounted = false;
                    mLabel = mInfo.packageName;
                } else {
                    mMounted = true;
                    CharSequence label = mInfo.loadLabel(context.getPackageManager());
                    mLabel = label != null ? label.toString() : mInfo.packageName;
                }
            }
        }

        private final AppListLoader mLoader;

        private final ApplicationInfo mInfo;

        private final File mApkFile;

        private String mLabel;

        private Drawable mIcon;

        private boolean mMounted;
    }

    /**
	 * Perform alphabetical comparison of application entry objects.
	 */
    public static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {

        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(AppEntry object1, AppEntry object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };

    /**
	 * Helper for determining if the configuration has changed in an interesting
	 * way so we need to rebuild the app list.
	 */
    public static class InterestingConfigChanges {

        final Configuration mLastConfiguration = new Configuration();

        int mLastDensity;

        boolean applyNewConfig(Resources res) {
            int configChanges = mLastConfiguration.updateFrom(res.getConfiguration());
            boolean densityChanged = mLastDensity != res.getDisplayMetrics().densityDpi;
            if (densityChanged || (configChanges & (ActivityInfo.CONFIG_LOCALE | ActivityInfoCompat.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
                mLastDensity = res.getDisplayMetrics().densityDpi;
                return true;
            }
            return false;
        }
    }

    /**
	 * Helper class to look for interesting changes to the installed apps so
	 * that the loader can be updated.
	 */
    public static class PackageIntentReceiver extends BroadcastReceiver {

        final AppListLoader mLoader;

        public PackageIntentReceiver(AppListLoader loader) {
            mLoader = loader;
            IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            filter.addDataScheme("package");
            mLoader.getContext().registerReceiver(this, filter);
            IntentFilter sdFilter = new IntentFilter();
            sdFilter.addAction(IntentCompat.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            sdFilter.addAction(IntentCompat.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            mLoader.getContext().registerReceiver(this, sdFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mLoader.onContentChanged();
        }
    }

    /**
	 * A custom Loader that loads all of the installed applications.
	 */
    public static class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {

        final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();

        final PackageManager mPm;

        List<AppEntry> mApps;

        PackageIntentReceiver mPackageObserver;

        public AppListLoader(Context context) {
            super(context);
            mPm = getContext().getPackageManager();
        }

        /**
		 * This is where the bulk of our work is done. This function is called
		 * in a background thread and should generate a new set of data to be
		 * published by the loader.
		 */
        @Override
        public List<AppEntry> loadInBackground() {
            List<ApplicationInfo> apps = mPm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);
            if (apps == null) {
                apps = new ArrayList<ApplicationInfo>();
            }
            final Context context = getContext();
            List<AppEntry> entries = new ArrayList<AppEntry>(apps.size());
            for (int i = 0; i < apps.size(); i++) {
                AppEntry entry = new AppEntry(this, apps.get(i));
                entry.loadLabel(context);
                entries.add(entry);
            }
            Collections.sort(entries, ALPHA_COMPARATOR);
            return entries;
        }

        /**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
        @Override
        public void deliverResult(List<AppEntry> apps) {
            if (isReset()) {
                if (apps != null) {
                    onReleaseResources(apps);
                }
            }
            List<AppEntry> oldApps = apps;
            mApps = apps;
            if (isStarted()) {
                super.deliverResult(apps);
            }
            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }

        /**
		 * Handles a request to start the Loader.
		 */
        @Override
        protected void onStartLoading() {
            if (mApps != null) {
                deliverResult(mApps);
            }
            if (mPackageObserver == null) {
                mPackageObserver = new PackageIntentReceiver(this);
            }
            boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());
            if (takeContentChanged() || mApps == null || configChange) {
                forceLoad();
            }
        }

        /**
		 * Handles a request to stop the Loader.
		 */
        @Override
        protected void onStopLoading() {
            cancelLoad();
        }

        /**
		 * Handles a request to cancel a load.
		 */
        @Override
        public void onCanceled(List<AppEntry> apps) {
            super.onCanceled(apps);
            onReleaseResources(apps);
        }

        /**
		 * Handles a request to completely reset the Loader.
		 */
        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
            if (mApps != null) {
                onReleaseResources(mApps);
                mApps = null;
            }
            if (mPackageObserver != null) {
                getContext().unregisterReceiver(mPackageObserver);
                mPackageObserver = null;
            }
        }

        /**
		 * Helper function to take care of releasing resources associated with
		 * an actively loaded data set.
		 */
        protected void onReleaseResources(List<AppEntry> apps) {
        }
    }

    public static class AppListAdapter extends ArrayAdapter<AppEntry> {

        private final LayoutInflater mInflater;

        public AppListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<AppEntry> data) {
            clear();
            if (data != null) {
                for (AppEntry appEntry : data) {
                    add(appEntry);
                }
            }
        }

        /**
		 * Populate new items in the list.
		 */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item_icon_text, parent, false);
            } else {
                view = convertView;
            }
            AppEntry item = getItem(position);
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(item.getIcon());
            ((TextView) view.findViewById(R.id.text)).setText(item.getLabel());
            return view;
        }
    }

    public static class AppListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<AppEntry>> {

        AppListAdapter mAdapter;

        String mCurFilter;

        OnQueryTextListenerCompat mOnQueryTextListenerCompat;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setEmptyText("No applications");
            setHasOptionsMenu(true);
            mAdapter = new AppListAdapter(getActivity());
            setListAdapter(mAdapter);
            setListShown(false);
            getLoaderManager().initLoader(0, null, this);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            View searchView = SearchViewCompat.newSearchView(getActivity());
            if (searchView != null) {
                SearchViewCompat.setOnQueryTextListener(searchView, new OnQueryTextListenerCompat() {

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
                        mAdapter.getFilter().filter(mCurFilter);
                        return true;
                    }
                });
                MenuItemCompat.setActionView(item, searchView);
            }
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("LoaderCustom", "Item clicked: " + id);
        }

        @Override
        public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
            return new AppListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
            mAdapter.setData(data);
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<AppEntry>> loader) {
            mAdapter.setData(null);
        }
    }
}
