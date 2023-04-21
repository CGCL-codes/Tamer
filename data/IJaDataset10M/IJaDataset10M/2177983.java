package orxatas.travelme.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import orxatas.travelme.R;
import orxatas.travelme.activity.GroupListActivity.Listener;
import orxatas.travelme.databases.exceptions.PhotoNoInLocal;
import orxatas.travelme.databases.exceptions.UserNotInLocal;
import orxatas.travelme.entity.Entry;
import orxatas.travelme.entity.Group;
import orxatas.travelme.entity.Photo;
import orxatas.travelme.entity.User;
import orxatas.travelme.manager.AccountManager;
import orxatas.travelme.manager.DataManager;
import orxatas.travelme.manager.EntryManager;
import orxatas.travelme.manager.PhotoManager;
import orxatas.travelme.manager.UserManager;
import orxatas.travelme.manager.exceptions.CantLoadPhotoFromFile;
import orxatas.travelme.sync.AsyncNoticeCode;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

public class DiaryActivity extends ListActivity implements AsyncActivity {

    private static final String TAG = "travelme_DiaryActivity";

    private User user;

    private int userID;

    private EntryAdapter adapter;

    private UserManager userManager;

    private EntryManager entryManager;

    private PhotoManager photoManager;

    private AccountManager accountManager;

    private DateFormat formatter;

    private ArrayList<Integer> entryIdList;

    private ArrayList<Entry> entryList;

    private ProgressDialog dialog;

    private Date tmpDate;

    private Listener listener;

    private boolean listenerRegistered = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.diary);
        formatter = DateFormat.getDateInstance();
        tmpDate = new Date();
        accountManager = new AccountManager(this);
        listener = new Listener();
        if (!accountManager.isLogged()) {
            startActivity(new Intent(DiaryActivity.this, LoginActivity.class));
            finish();
        }
        userManager = new UserManager(this);
        entryManager = new EntryManager(this);
        photoManager = new PhotoManager(this);
        user = accountManager.getUserLogged();
        entryIdList = new ArrayList<Integer>();
        entryList = new ArrayList<Entry>();
        if (user != null) {
            userID = user.getId();
            ArrayList<Integer> list = entryManager.getEntries();
            if (list != null) {
                entryIdList = list;
                entryList = new ArrayList<Entry>();
                Entry e = null;
                for (Integer i : entryIdList) {
                    e = entryManager.getEntry(i);
                    if (e != null) {
                        entryList.add(e);
                    }
                }
            }
        } else {
            Log.e(TAG, "onCreate - no user logged");
            userID = -1;
        }
        adapter = new EntryAdapter(this, R.layout.diary_row, entryList);
        setListAdapter(adapter);
    }

    public void showProgressDialog() {
        if (dialog == null) {
            dialog = ProgressDialog.show(DiaryActivity.this, "", getResources().getString(R.string.diary_progressdialog_text), true, false);
        }
    }

    public void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Entry item = (Entry) getListAdapter().getItem(position);
        Intent i = new Intent(DiaryActivity.this, EntryDetailsActivity.class);
        i.putExtra("entry_id", item.getIdEntryOffline());
        startActivity(i);
    }

    private void loadList() {
        entryList = new ArrayList<Entry>();
        Entry e = null;
        for (Integer i : entryIdList) {
            e = entryManager.getEntry(i);
            if (e != null) {
                entryList.add(e);
            }
        }
        refreshAdapterList();
    }

    private void refreshAdapterList() {
        adapter.clear();
        for (Entry en : entryList) {
            adapter.add(en);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_diary_disconnect:
                accountManager.disconnect();
                startActivity(new Intent(DiaryActivity.this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    static class ViewHolder {

        TextView tUser;

        TextView textView_date;

        TextView textView_text;

        ImageView photo;

        TextView textView_photos;
    }

    private class EntryAdapter extends ArrayAdapter<Entry> {

        private ArrayList<Entry> items;

        private LayoutInflater inflater;

        public EntryAdapter(Context context, int textViewResourceId, ArrayList<Entry> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.diary_row, null);
                holder = new ViewHolder();
                holder.tUser = (TextView) convertView.findViewById(R.id.row_diary_entry_user);
                holder.textView_date = (TextView) convertView.findViewById(R.id.row_diary_entry_date);
                holder.textView_text = (TextView) convertView.findViewById(R.id.row_diary_entry_text);
                holder.photo = (ImageView) convertView.findViewById(R.id.row_diary_entry_photo);
                holder.textView_photos = (TextView) convertView.findViewById(R.id.row_diary_entry_photos);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Entry entry = items.get(position);
            if (entry != null) {
                holder.tUser.setText(getNameString(entry.getIdAutor()));
                setDate(entry, holder);
                setText(entry, holder);
            }
            return convertView;
        }

        public String getNameString(int id) {
            String result = "";
            if (id == userID) {
                result = user.getUserName();
                if (result == null || result == "") {
                    result = user.getEmail();
                }
            } else {
                try {
                    User u = userManager.getUser(id);
                    result = u.getUserName();
                    if (result == null || result == "") {
                        result = u.getEmail();
                    }
                } catch (UserNotInLocal e) {
                    result = String.valueOf(id);
                } catch (Exception e) {
                    result = String.valueOf(id);
                }
            }
            return result;
        }

        private void setDate(Entry e, ViewHolder holder) {
            long date = ((long) e.getDate()) * 1000;
            tmpDate.setTime(date);
            holder.textView_date.setText(formatter.format(tmpDate));
            holder.textView_date.setVisibility(View.VISIBLE);
        }

        private void setText(Entry e, ViewHolder holder) {
            if (!(e.getText() == null) && !(e.getText() == "")) {
                holder.textView_text.setText(e.getText());
                holder.textView_text.setVisibility(View.VISIBLE);
            } else {
                holder.textView_text.setVisibility(View.GONE);
            }
        }
    }

    private void reloadEntryList() {
        ArrayList<Integer> list = entryManager.getEntries();
        if (list != null) {
            entryIdList = list;
        } else {
            entryIdList = new ArrayList<Integer>();
        }
        loadList();
    }

    @Override
    public void asyncNotice(AsyncNoticeCode code) {
        switch(code) {
            case GROUP_MANAGEMENT_ASYNC:
            case USERS_CHANGED:
            case GROUPS_CHANGED:
            case ENTRY_CHANGES:
                reloadEntryList();
        }
    }

    protected class Listener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("SYNC_ACTION")) {
                new DataManager(DiaryActivity.this).syncAll();
            } else if (intent.getAction().equals("SHOW_FRIENDS")) {
                startActivity(new Intent(DiaryActivity.this, FriendListActivity.class));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!listenerRegistered) {
            registerReceiver(listener, new IntentFilter("SYNC_ACTION"));
            registerReceiver(listener, new IntentFilter("SHOW_FRIENDS"));
            listenerRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (listenerRegistered) {
            unregisterReceiver(listener);
            listenerRegistered = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new DataManager(this).activityEnds(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void syncStarted() {
    }

    @Override
    public void syncEnded() {
    }

    @Override
    public void syncAndWaitCallStarted() {
        showProgressDialog();
    }

    @Override
    public void syncAndWaitCallEnded(Object o, int syncAction) {
        hideProgressDialog();
    }
}
