package de.drak.Profiles;

import java.util.HashMap;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

/**Activity to create/modify a Profile*/
public class ProfileEditor extends Activity implements OnClickListener {

    public static final int START_APP = 1;

    public static final int START_PICKER = 0;

    public static final int START_ICON = 2;

    public static final int MENU_DELETE = Menu.FIRST;

    public static final int MEN_DELETE = Menu.FIRST + 1;

    public static final int MEN_TEST = Menu.FIRST + 2;

    private static final int MENU_NEW_ON = Menu.FIRST + 3;

    private static final int MENU_NEW_OFF = Menu.FIRST + 4;

    public static final String OpenParam_ProfileID = "de.drak.Profiles.ProfileNr";

    private static Database mDbHelper;

    private Long mRowId, openPlugin;

    private void pickPlugin(final long type, Context c) {
        openPlugin = type;
        Intent Opener = new Intent(c, ActivityPicker.class);
        Opener.putExtra("searchFor", TimedProfiles.ACTION_SET_EDIT);
        Opener.putExtra("getClass", TimedProfiles.ACTION_SET_FIRE);
        startActivityForResult(Opener, START_PICKER);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.ImageButton01) {
            Intent Opener = new Intent(v.getContext(), IconPicker.class);
            startActivityForResult(Opener, START_ICON);
            return;
        }
    }

    /** Handles click on ListItem */
    public void ItemClick(boolean activate, long id, Context con) {
        openPlugin = id;
        if (openPlugin == -1 || openPlugin == -2) {
            pickPlugin(openPlugin, con);
        } else {
            newPlugOpen = false;
            hasUnsetBundle = false;
            Cursor setting = mDbHelper.fetchProfileSettingsPluginState(openPlugin);
            if (setting == null) {
                return;
            }
            final Bundle bundle = mDbHelper.fetchPluginStateBundle(setting.getLong(setting.getColumnIndex(Database.KEY_ROWID)));
            final ComponentName name = mDbHelper.fetchPluginStateComponent(setting.getLong(setting.getColumnIndex(Database.KEY_ROWID)));
            setting.close();
            Long unset = activate ? mDbHelper.fetchUnsetProfileSettings(openPlugin) : 0;
            if (unset != 0) {
                final Bundle unbundle = mDbHelper.fetchPluginStateBundle(unset);
                if (unbundle != null) {
                    bundle.putBundle(TimedProfiles.INTENT_BUNDLE_UNSET, unbundle);
                    hasUnsetBundle = true;
                }
            }
            TimedProfiles.editPlugInSetting(name, bundle, START_APP, (Activity) con);
        }
    }

    private boolean newPlugOpen;

    private boolean hasUnsetBundle;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_ICON && resultCode == RESULT_OK) {
            int ico = data.getIntExtra("IconRes", R.drawable.icon);
            ((ImageButton) findViewById(R.id.ImageButton01)).setImageResource(ico);
            mDbHelper.updateProfile(mRowId, null, -1, ico);
        }
        if (requestCode == START_PICKER) {
            if (resultCode == RESULT_OK) {
                final String newPackage = data.getStringExtra("ActivityPackage");
                final String newEditor = data.getStringExtra("ActivityName");
                final String rec = data.getStringExtra("ActivityReceiver");
                if (newPackage == null || newEditor == null || rec == null) {
                    Toast.makeText(getApplicationContext(), "Oups! An error occured.", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ComponentName test = new ComponentName(newPackage, newEditor);
                hasUnsetBundle = openPlugin == -1;
                openPlugin = mDbHelper.createProfileSetting(mRowId, openPlugin == -1 ? 1L : 0L, newPackage, newEditor, rec)[0];
                newPlugOpen = true;
                TimedProfiles.editPlugInSetting(test, null, START_APP, (Activity) this);
                ((ExpandableListView) findViewById(R.id.expandableListView1)).expandGroup(openPlugin == -1 ? 0 : 1);
            }
        }
        if (requestCode == START_APP) {
            if (resultCode == RESULT_OK) {
                String info = null;
                if (hasUnsetBundle && data.hasExtra(TimedProfiles.INTENT_BUNDLE_UNSET)) {
                    Bundle stop = data.getBundleExtra(TimedProfiles.INTENT_BUNDLE_UNSET);
                    if (stop.containsKey(TimedProfiles.INTENT_INFO_STR)) {
                        info = stop.getString(TimedProfiles.INTENT_INFO_STR);
                        stop.remove(TimedProfiles.INTENT_INFO_STR);
                    } else {
                        info = "?";
                    }
                    if (newPlugOpen) {
                        Cursor setting = mDbHelper.fetchProfileSettingsPluginState(openPlugin);
                        if (setting == null) {
                            Toast.makeText(getApplicationContext(), "Oups! An error occured.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String pack = setting.getString(setting.getColumnIndex(Database.KEY_COMP)), edit = setting.getString(setting.getColumnIndex(Database.KEY_EDIT_CLASS)), rec = setting.getString(setting.getColumnIndex(Database.KEY_DO_CLASS));
                        setting.close();
                        long i = mDbHelper.createProfileSetting(mRowId, 0L, pack, edit, rec)[1];
                        if (!mDbHelper.updatePluginState(i, info, stop)) {
                            Toast.makeText(getApplicationContext(), "Oups! An error occured.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        final Long unset = mDbHelper.fetchUnsetProfileSettings(openPlugin);
                        if (unset != 0) {
                            if (!mDbHelper.updatePluginState(unset, info, stop)) {
                                Toast.makeText(getApplicationContext(), "Can't save Unset Bundle!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    data.removeExtra(TimedProfiles.INTENT_BUNDLE_UNSET);
                }
                if (data.hasExtra(TimedProfiles.INTENT_INFO_STR)) {
                    info = data.getStringExtra(TimedProfiles.INTENT_INFO_STR);
                    data.removeExtra(TimedProfiles.INTENT_INFO_STR);
                } else {
                    info = "?";
                }
                final Bundle store_forward = data.getExtras();
                if (store_forward == null) {
                    Toast.makeText(getApplicationContext(), "Oups! The Plugin triggered an error.", Toast.LENGTH_SHORT).show();
                    openPlugin = 0L;
                    return;
                }
                Cursor setting = mDbHelper.fetchProfileSettingsPluginState(openPlugin);
                if (!(setting != null && mDbHelper.updatePluginState(setting.getLong(setting.getColumnIndex(Database.KEY_ROWID)), info, store_forward))) {
                    Toast.makeText(getApplicationContext(), "Oups! An error occured.", Toast.LENGTH_SHORT).show();
                    return;
                }
                setting.close();
            }
            if ((resultCode == TimedProfiles.RESULT_REMOVE) || (newPlugOpen && resultCode == RESULT_CANCELED)) {
                mDbHelper.deleteProfileSetting(openPlugin);
                populateFields();
            }
            openPlugin = 0L;
            newPlugOpen = false;
            hasUnsetBundle = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final Intent helpIntent = new Intent(this, OnlineHelp.class);
        helpIntent.putExtra(OnlineHelp.HELP_URL, OnlineHelp.HELP_URL_PRE + "edit_profile");
        helpIntent.putExtra(TimedProfiles.INTENT_TITLE, getTitle().toString());
        menu.add(R.string.onlinehelp).setIcon(android.R.drawable.ic_menu_help).setIntent(helpIntent);
        menu.add(0, MENU_DELETE, 0, R.string.menu_delete).setIcon(android.R.drawable.ic_menu_delete).getItemId();
        return true;
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        switch(item.getItemId()) {
            case MENU_DELETE:
                {
                    mDbHelper.deleteProfile(mRowId);
                    finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
        if (ExpandableListView.getPackedPositionType(info.packedPosition) != ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            return;
        }
        final long id = info.id;
        if (id == -1) {
            return;
        }
        openPlugin = 0L;
        Cursor notesCursor = mDbHelper.fetchProfileSettingsPluginState(id);
        if (notesCursor != null) {
            CharSequence Name;
            final ComponentName test = mDbHelper.fetchPluginStateComponent(notesCursor.getLong(notesCursor.getColumnIndex(Database.KEY_ROWID)));
            notesCursor.close();
            try {
                Name = v.getContext().getPackageManager().getActivityInfo(test, 0).loadLabel(getPackageManager());
            } catch (NameNotFoundException e) {
                Name = test.getPackageName();
                e.printStackTrace();
            }
            menu.setHeaderTitle(Name);
        }
        openPlugin = id;
        menu.add(0, MEN_DELETE, 0, R.string.task_del).setOnMenuItemClickListener(Contextmen);
        menu.add(0, MEN_TEST, 0, R.string.task_test).setOnMenuItemClickListener(Contextmen);
    }

    private MenuItem.OnMenuItemClickListener Contextmen = new MenuItem.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            onContextItemSelected(item);
            return false;
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (openPlugin == 0L) {
            return false;
        }
        final Long id = openPlugin;
        openPlugin = 0L;
        switch(item.getItemId()) {
            case MEN_DELETE:
                mDbHelper.deleteProfileSetting(id);
                populateFields();
                return true;
            case MEN_TEST:
                Cursor setting = mDbHelper.fetchProfileSettingsPluginState(id);
                if (setting == null) {
                    return true;
                }
                final Bundle bundle = mDbHelper.fetchPluginStateBundle(setting.getLong(setting.getColumnIndex(Database.KEY_ROWID)));
                final ComponentName name = mDbHelper.fetchPluginStateFireComponent(setting.getLong(setting.getColumnIndex(Database.KEY_ROWID)));
                setting.close();
                TimedProfiles.setPlugInSettings(name, bundle, this);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new Database(this);
        setContentView(R.layout.profileeditor);
        TextView nameBox = (TextView) findViewById(R.id.EditText01);
        nameBox.setMinEms(10);
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(Database.P_KEY_ROWID) : null;
        if (mRowId == null) {
            mRowId = Long.parseLong(getIntent().getStringExtra(OpenParam_ProfileID));
        }
        if (mRowId != -1) {
            nameBox.setInputType(InputType.TYPE_NULL);
            nameBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((TextView) findViewById(R.id.EditText01)).setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                    ((TextView) findViewById(R.id.EditText01)).setOnFocusChangeListener(null);
                }
            });
        }
        openPlugin = 0L;
        mDbHelper.open();
    }

    @Override
    public void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Database.P_KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        final String title = ((TextView) findViewById(R.id.EditText01)).getText().toString();
        if (mRowId == -1) {
            long id = mDbHelper.createProfile(title, 0);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateProfile(mRowId, title, -1, -1);
        }
    }

    private ExpandableListView.OnChildClickListener listb = new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            ItemClick(groupPosition == 0, id, v.getContext());
            return true;
        }
    };

    private void populateFields() {
        String ProfileName = null;
        Cursor SettingsCursor = null;
        if (mRowId == -1) {
            ProfileName = getString(R.string.profile_add);
        } else {
            Cursor notesCursor = mDbHelper.fetchProfile(mRowId);
            if (notesCursor != null) {
                ProfileName = notesCursor.getString(notesCursor.getColumnIndex(Database.P_KEY_NAME));
                ((ImageButton) findViewById(R.id.ImageButton01)).setImageResource(notesCursor.getInt(notesCursor.getColumnIndex(Database.P_KEY_ICON)));
                notesCursor.close();
            }
            SettingsCursor = mDbHelper.fetchProfileSettings(mRowId);
        }
        setTitle(String.format("%s%s%s", getString(R.string.profiles), TimedProfiles.INTENT_TITLE_SEPERATOR, ProfileName));
        ((TextView) findViewById(R.id.EditText01)).setText(ProfileName);
        int pos_a = 0, pos_b = 0;
        long[] db_idA = null;
        long[] db_idB = null;
        HashMap<Long, CharSequence> db_name = new HashMap<Long, CharSequence>();
        HashMap<Long, CharSequence> db_text = new HashMap<Long, CharSequence>();
        HashMap<Long, Drawable> db_icon = new HashMap<Long, Drawable>();
        if (SettingsCursor != null) {
            SettingsCursor.moveToFirst();
            final int ir = SettingsCursor.getColumnIndex(Database.PS_KEY_ROWID), io = SettingsCursor.getColumnIndex(Database.PS_KEY_ON), ii = SettingsCursor.getColumnIndex(Database.KEY_INFO), ip = SettingsCursor.getColumnIndex(Database.PS_KEY_PLUGIN);
            db_idA = new long[SettingsCursor.getCount() + 1];
            db_idB = new long[SettingsCursor.getCount() + 1];
            CharSequence name_buff = null;
            Drawable icon_buff = null;
            do {
                final ComponentName test = mDbHelper.fetchPluginStateComponent(SettingsCursor.getLong(ip));
                try {
                    icon_buff = getPackageManager().getActivityIcon(test);
                } catch (NameNotFoundException e) {
                    icon_buff = getResources().getDrawable(R.drawable.plugin);
                    e.printStackTrace();
                }
                try {
                    name_buff = getPackageManager().getActivityInfo(test, 0).loadLabel(getPackageManager());
                } catch (NameNotFoundException e) {
                    name_buff = test.getPackageName();
                    e.printStackTrace();
                }
                long id = SettingsCursor.getLong(ir);
                if (SettingsCursor.getInt(io) == 1) {
                    db_idA[pos_a] = id;
                    pos_a++;
                } else {
                    db_idB[pos_b] = id;
                    pos_b++;
                }
                db_text.put(id, SettingsCursor.getString(ii));
                db_name.put(id, name_buff);
                db_icon.put(id, icon_buff);
            } while (SettingsCursor.moveToNext() != false);
            SettingsCursor.close();
        }
        long[] id_a = new long[pos_a + 1];
        if (pos_a > 0) {
            System.arraycopy(db_idA, 0, id_a, 1, pos_a);
        }
        id_a[0] = -1L;
        db_text.put(-1L, null);
        db_name.put(-1L, getString(R.string.task_add));
        db_icon.put(-1L, getResources().getDrawable(R.drawable.create));
        db_text.put(-2L, null);
        db_name.put(-2L, getString(R.string.task_add));
        db_icon.put(-2L, getResources().getDrawable(R.drawable.create));
        long[] id_b = new long[pos_b + 1];
        if (pos_b > 0) {
            System.arraycopy(db_idB, 0, id_b, 1, pos_b);
        }
        id_b[0] = -2L;
        ExpandableListView l2 = (ExpandableListView) findViewById(R.id.expandableListView1);
        l2.setAdapter(new EditorListAdapter(this, new String[] { getString(R.string.profile_start), getString(R.string.profile_end) }, new long[][] { id_a, id_b }, db_name, db_text, db_icon));
        l2.setOnCreateContextMenuListener((OnCreateContextMenuListener) this);
        l2.setOnChildClickListener(listb);
        l2.expandGroup(0);
        if (db_name.size() < 6) {
            l2.expandGroup(1);
        }
        ((ImageButton) findViewById(R.id.ImageButton01)).setOnClickListener(this);
    }

    private void scrollTo(int groupPos, int index) {
        ExpandableListView l2 = (ExpandableListView) findViewById(R.id.expandableListView1);
        l2.collapseGroup(groupPos);
        View child = l2.getChildAt(index);
        l2.requestChildRectangleOnScreen(child, new Rect(0, 0, 1, child.getHeight()), false);
    }
}
