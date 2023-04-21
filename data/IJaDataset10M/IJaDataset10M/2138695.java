package de.fu.tracebook.gui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import de.fu.tracebook.R;
import de.fu.tracebook.core.data.IDataMedia;
import de.fu.tracebook.core.data.IDataMediaHolder;
import de.fu.tracebook.core.data.StorageFactory;
import de.fu.tracebook.core.logger.ServiceConnector;
import de.fu.tracebook.gui.adapter.GenericAdapter;
import de.fu.tracebook.gui.adapter.GenericAdapterData;
import de.fu.tracebook.gui.adapter.GenericItemDescription;
import de.fu.tracebook.util.Helper;
import de.fu.tracebook.util.LogIt;

/**
 * This activity shows a list of all media of a mediaholder.
 */
public class ListMediaActivity extends ListActivity {

    /**
     * GenericAdapter for our ListView which we use in this activity.
     */
    GenericAdapter adapter;

    /**
     * The object of which the media should be enlisted.
     */
    IDataMediaHolder holder;

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        GenericAdapterData itemData = adapter.getItem((int) info.id);
        IDataMedia media = (IDataMedia) itemData.getAdditional();
        switch(item.getItemId()) {
            case R.id.cm_listmedia_viewMedia:
                final String[] mimes = new String[] { "text/plain", "image/jpeg", "audio/*" };
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                File file = new File(media.getPath() + File.separator + media.getName());
                intent.setDataAndType(Uri.fromFile(file), mimes[media.getType()]);
                startActivity(intent);
                break;
            case R.id.cm_listmedia_delete:
                media.delete();
                initAdapter();
                break;
        }
        return true;
    }

    /**
     * Create ContextMenu for this activity.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu_listmediaactivity, menu);
        menu.setHeaderIcon(android.R.drawable.ic_menu_edit);
        menu.setHeaderTitle(getResources().getString(R.string.cm_listmediaActivity_title));
    }

    /**
     * The Method for the preference image Button from the status bar. The
     * Method starts the PreferenceActivity.
     * 
     * @param view
     *            not used
     */
    public void statusBarPrefBtn(View view) {
        final Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }

    /**
     * This Method for the two (title and description) button from the status
     * bar. This method starts the dialog with all activity informations.
     * 
     * @param v
     *            not used
     */
    public void statusBarTitleBtn(View v) {
        Helper.setActivityInfoDialog(this, getResources().getString(R.string.tv_statusbar_listmediaTitle), getResources().getString(R.string.tv_statusbar_listmediaDesc));
    }

    private void initAdapter() {
        final int[] imgs = { R.drawable.btn_notice, R.drawable.btn_photo, R.drawable.btn_memo };
        (new AsyncTask<Void, Void, List<GenericAdapterData>>() {

            @Override
            protected List<GenericAdapterData> doInBackground(Void... arg0) {
                GenericItemDescription desc = new GenericItemDescription();
                desc.addResourceId("name", R.id.tv_listmedia_listrow);
                desc.addResourceId("img", R.id.iv_listmedia_listrow);
                List<GenericAdapterData> data = new ArrayList<GenericAdapterData>();
                List<IDataMedia> allMedia = holder.getMedia();
                Iterator<IDataMedia> iterator = allMedia.iterator();
                while (iterator.hasNext()) {
                    IDataMedia media = iterator.next();
                    GenericAdapterData item = new GenericAdapterData(desc);
                    item.setText("name", media.getName());
                    item.setImage("img", imgs[media.getType()]);
                    item.setAdditional(media);
                    data.add(item);
                }
                return data;
            }

            @Override
            protected void onPostExecute(List<GenericAdapterData> result) {
                adapter = new GenericAdapter(ListMediaActivity.this, R.layout.listview_listmedia, R.id.list, result);
                setListAdapter(adapter);
            }
        }).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Helper.setTheme(this);
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long nodeId = extras.getLong("NodeId");
            long wayId = extras.getLong("WayId");
            String trackname = extras.getString("UseTrack");
            if (nodeId != 0) {
                holder = StorageFactory.getStorage().getTrack().getNodeById(nodeId);
                LogIt.d("node is: " + nodeId);
            } else if (wayId != 0) {
                holder = StorageFactory.getStorage().getTrack().getPointsListById(wayId);
                LogIt.d("way is: " + wayId);
            } else if (trackname != null) {
                holder = StorageFactory.getStorage().getTrack();
                LogIt.d("track is: " + trackname);
            } else {
                LogIt.d("Neither way nor node nor track ...");
                finish();
                return;
            }
        }
        setTitle(R.string.string_listmediaActivity_title);
        setContentView(R.layout.activity_listmediaactivity);
        registerForContextMenu(getListView());
        Helper.setStatusBar(this, getResources().getString(R.string.tv_statusbar_listmediaTitle), getResources().getString(R.string.tv_statusbar_listmediaDesc), R.id.ly_listmediaActivity_statusbar, false);
        initAdapter();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final String[] mimes = new String[] { "text/plain", "image/jpeg", "audio/*" };
        GenericAdapterData datum = adapter.getItem(position);
        IDataMedia media = (IDataMedia) datum.getAdditional();
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(media.getPath() + File.separator + media.getName());
        intent.setDataAndType(Uri.fromFile(file), mimes[media.getType()]);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (ServiceConnector.getLoggerService().isLogging()) {
                Helper.startUserNotification(this, R.drawable.ic_notification_active, ListMediaActivity.class, true);
            } else {
                Helper.startUserNotification(this, R.drawable.ic_notification_pause, ListMediaActivity.class, false);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
