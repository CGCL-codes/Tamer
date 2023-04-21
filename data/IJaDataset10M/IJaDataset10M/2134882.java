package com.commandus.meetup;

import java.util.Hashtable;
import java.util.Vector;
import com.commandus.meetup.R;
import com.commandus.util.Date;
import com.commandus.meetup.ViewCurrentProfile;
import com.commandus.meetup.provider.Appointment;
import com.commandus.meetup.provider.AppointmentProvider;
import com.commandus.meetup.provider.MediaProvider;
import com.commandus.meetup.provider.Profile;
import com.commandus.meetup.provider.ProfileProvider;
import android.widget.ListAdapter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.content.ContentUris;
import android.os.Bundle;
import android.database.Cursor;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewStub;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Agenda - "main" default appointment activity show list of scheduled appointments (meetings, etc)
 * getting from {@link AppointmentProvider}.
 * User credential {@link MeetupSettings} class store data and other user settings and preferences in the
 * persistent storage.
 * {@link MediaProvider} store ring tones.   
 * @author andrei
 *
 */
public class ViewAgenda extends Activity {

    static final int CMD_EDIT_USER_PROFILE = 1;

    static final int CMD_ABOUTBOX = 2;

    static final int CMD_FIND_USER = 3;

    private static ListView listview;

    public static final int MENU_INSERT = Menu.FIRST;

    public static final int MENU_DELETE = Menu.FIRST + 1;

    public static final int MENU_DELETE_ALL_OLD = Menu.FIRST + 2;

    public static final int MENU_TOOGLE_AVAILABLE = Menu.FIRST + 3;

    public static final int MENU_COMMANDS_MAIN = Menu.FIRST + 4;

    public static final int MENU_DATE_MAIN = Menu.FIRST + 5;

    public static final int MENU_DATE_TODAY = Menu.FIRST + 6;

    public static final int MENU_DATE_EARLIER = Menu.FIRST + 7;

    public static final int MENU_DATE_LATER = Menu.FIRST + 8;

    public static final int MENU_OPTIONS = Menu.FIRST + 9;

    public static final int MENU_OPTIONS_PROFILE = Menu.FIRST + 10;

    public static final int MENU_OPTIONS_ABOUT = Menu.FIRST + 11;

    public static final int MENU_FIND = Menu.FIRST + 12;

    public static final int MENU_CALL = Menu.FIRST + 13;

    public static final int MENU_IM = Menu.FIRST + 14;

    public static final int CMD_FINISH_CHAIN_FIND = 100;

    private int notification_id = -1;

    private String tel = null;

    private String xmpp = null;

    private Long mDateFrom = MeetupHelper.StartDayMillis(System.currentTimeMillis());

    private Cursor CursorProfile;

    private Cursor CursorAppointment;

    private TextView tvNotice;

    private ImageButton bSearchOpen;

    private ImageButton bSearchClose;

    private ImageButton bNewAppointment;

    private ImageButton bFindUser;

    private ImageButton bLocate;

    private SubMenu mMenuCommands;

    private SubMenu mMenuOptions;

    private EditText mFastSearch;

    MeetupSettings settings;

    private SvrCallRun rpc;

    private Uri IntentUri;

    private ViewStub searchStub;

    private View searchLayout = null;

    /** Called when the activity is first created.
	 * Start {@link com.commandus.meetup.MeetupService} service in the background at the first start up. 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        settings = new MeetupSettings(this);
        MeetupService.start(this);
        setContentView(R.layout.agenda);
        searchStub = (ViewStub) findViewById(R.id.stubSearch);
        Intent intent = getIntent();
        IntentUri = intent.getData();
        if (IntentUri == null) {
            IntentUri = intent.setData(Appointment.URI_APPOINTMENT_PROVIDER).getData();
        }
        CursorProfile = managedQuery(Profile.User.CONTENT_URI, ProfileProvider.PROJECTION_PROFILE, null, null, null);
        boolean haveProfile = getCurrentProfile();
        if (!haveProfile) {
            selectProfile(CursorProfile.getCount() == 0);
        } else {
            boolean needStore = false;
            if ((settings.UserName == null) || (settings.UserName == "")) {
                settings.UserName = CursorProfile.getString(ProfileProvider.F_USERNAME);
                needStore = true;
            }
            if ((settings.UserPassword == null) || (settings.UserPassword == "")) {
                settings.UserPassword = CursorProfile.getString(ProfileProvider.F_USERPASSWORD);
                needStore = true;
            }
            if (needStore) settings.save();
        }
        rpc = new SvrCallRun(this, settings.Host, settings.UserName, settings.UserPassword);
        CursorAppointment = managedQuery(getIntent().getData(), AppointmentProvider.PROJECTION_APPOINTMENT, AppointmentProvider.SQL_WHERE_NEWONLY, new String[] { mDateFrom.toString() }, null);
        fixPrevoiusDateWhereDataExist();
        ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, CursorAppointment, Appointment.Meeting.getAgendaColumns(), Appointment.Meeting.getAgendaTrans());
        bSearchOpen = (ImageButton) findViewById(R.id.bSearchOpen);
        bSearchOpen.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                showSearchPanel(true);
            }
        });
        bNewAppointment = (ImageButton) findViewById(R.id.bAgendaAdd);
        bNewAppointment.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
            }
        });
        listview = (ListView) findViewById(R.id.LVAgenda);
        listview.setAdapter(adapter);
        listview.setOnItemSelectedListener(new OnItemSelectedListener() {

            @SuppressWarnings("unchecked")
            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                String s;
                tel = null;
                xmpp = null;
                if ((CursorAppointment != null) & (tvNotice != null)) {
                    if (CursorAppointment.getCount() != 0) {
                        boolean b = CursorAppointment.moveToFirst();
                        while (b) {
                            if (id == CursorAppointment.getLong(AppointmentProvider.F_ID)) {
                                break;
                            }
                            b = CursorAppointment.moveToNext();
                        }
                        if ((b) && (!CursorAppointment.isNull(AppointmentProvider.F_STARTS))) {
                            Long st = CursorAppointment.getLong(AppointmentProvider.F_STARTS);
                            String p = CursorAppointment.getString(AppointmentProvider.F_PERSONNAME);
                            if (p.length() > 0) p = " - " + p;
                            String l = CursorAppointment.getString(AppointmentProvider.F_LOCATIONNAME);
                            if (l.length() > 0) l = " @ " + l;
                            String subj = CursorAppointment.getString(AppointmentProvider.F_SUBJECT);
                            s = MeetupHelper.Milliseconds2Str(st, getString(R.string.tct_timedelimiter_starting), 1) + p + l + " " + subj;
                            setTitle(s);
                            tel = CursorAppointment.getString(AppointmentProvider.F_TEL);
                            xmpp = CursorAppointment.getString(AppointmentProvider.F_XMPP);
                        }
                    }
                }
            }

            @SuppressWarnings("unchecked")
            public void onNothingSelected(AdapterView parent) {
            }
        });
        listview.setOnItemClickListener(new ListView.OnItemClickListener() {

            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView a, View v, int position, long id) {
                Uri url = ContentUris.withAppendedId(getIntent().getData(), id);
                String action = getIntent().getAction();
                if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
                    setResult(RESULT_OK);
                } else {
                    startActivity(new Intent(Intent.ACTION_EDIT, url));
                }
            }
        });
        registerForContextMenu(listview);
        tvNotice = (TextView) findViewById(R.id.tvNotice);
    }

    /** Create context menu to place a call to the person in the agenda
     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(R.string.menu_ap_connect);
        menu.add(0, MENU_CALL, 0, R.string.menu_ap_call);
        menu.add(0, MENU_IM, 0, R.string.menu_ap_xmpp);
    }

    /** Implement menu actions
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_CALL:
                placeCall();
                return true;
            case MENU_IM:
                placeIM();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /** Create menu items 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int m = 1;
        mMenuCommands = menu.addSubMenu(m, MENU_COMMANDS_MAIN, Menu.NONE, R.string.menu_ap_commands).setIcon(android.R.drawable.ic_menu_agenda);
        mMenuCommands.add(m, MENU_INSERT, Menu.NONE, R.string.menu_ap_insert).setIcon(android.R.drawable.ic_menu_mylocation).setShortcut('1', 'a');
        mMenuCommands.add(m, MENU_FIND, Menu.NONE, R.string.menu_ap_find).setShortcut('0', 'f').setIcon(android.R.drawable.ic_menu_search);
        mMenuCommands.add(m, MENU_TOOGLE_AVAILABLE, Menu.NONE, R.string.menu_ap_toggle_available).setShortcut('3', 's').setCheckable(true);
        m = 2;
        final SubMenu mMenuDate = menu.addSubMenu(m, MENU_DATE_MAIN, Menu.NONE, R.string.menu_ap_date);
        mMenuDate.setIcon(android.R.drawable.ic_menu_day);
        mMenuDate.add(m, MENU_DATE_TODAY, Menu.NONE, R.string.menu_ap_date_today).setIcon(android.R.drawable.ic_menu_today).setShortcut('4', 't');
        mMenuDate.add(m, MENU_DATE_EARLIER, Menu.NONE, R.string.menu_ap_date_earlier).setIcon(android.R.drawable.ic_media_next).setShortcut('5', 'e');
        mMenuDate.add(m, MENU_DATE_LATER, Menu.NONE, R.string.menu_ap_date_later).setIcon(android.R.drawable.ic_media_next).setShortcut('6', 'l');
        m = 3;
        mMenuOptions = menu.addSubMenu(m, MENU_OPTIONS, Menu.NONE, R.string.menu_ap_options).setIcon(android.R.drawable.ic_menu_preferences);
        mMenuOptions.add(m, MENU_DELETE_ALL_OLD, Menu.NONE, R.string.menu_ap_delete_all_old).setIcon(android.R.drawable.ic_delete).setShortcut('7', 'd');
        mMenuOptions.add(m, MENU_OPTIONS_PROFILE, Menu.NONE, R.string.menu_ap_options_profile).setShortcut('8', 'p');
        mMenuOptions.add(m, MENU_OPTIONS_ABOUT, Menu.NONE, R.string.menu_ap_about).setIcon(R.drawable.mabout).setShortcut('9', 'u');
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, Menu.NONE, new ComponentName(this, ViewAgenda.class), null, intent, 0, null);
        return true;
    }

    /** Prepare menu items
     * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean haveItems = CursorAppointment != null;
        MenuItem mi;
        SubMenu sm = mMenuCommands;
        if (haveItems) haveItems = CursorAppointment.getCount() > 0;
        if (haveItems) {
            Uri uri = ContentUris.appendId(getIntent().getData().buildUpon(), listview.getSelectedItemId()).build();
            Intent[] specifics = new Intent[1];
            specifics[0] = new Intent(Intent.ACTION_EDIT, uri);
            MenuItem[] items = new MenuItem[1];
            Intent intent = new Intent(null, uri);
            intent.addCategory(Intent.CATEGORY_SELECTED_ALTERNATIVE);
            sm.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, Menu.NONE, null, specifics, intent, 0, items);
            mi = sm.findItem(MENU_DELETE);
            if (mi == null) {
                mi = sm.add(1, MENU_DELETE, Menu.NONE, R.string.menu_ap_delete).setIcon(android.R.drawable.ic_delete).setShortcut('2', 'd');
            }
            if (items[0] != null) {
                items[0].setIcon(android.R.drawable.ic_menu_agenda);
                items[0].setShortcut('1', 'e');
            }
        } else {
            sm.removeGroup(Menu.CATEGORY_ALTERNATIVE);
            mi = sm.findItem(MENU_DELETE);
            if (mi != null) sm.removeItem(MENU_DELETE);
        }
        sm.setGroupVisible(MENU_DELETE, haveItems);
        int isOn = 0;
        if (getCurrentProfile()) {
            if (!CursorProfile.isNull(ProfileProvider.F_SVCON)) {
                isOn = CursorProfile.getInt(ProfileProvider.F_SVCON);
            }
        }
        mi = menu.findItem(MENU_TOOGLE_AVAILABLE);
        if (mi != null) {
            if (isOn > 0) mi.setIcon(android.R.drawable.presence_online); else mi.setIcon(android.R.drawable.presence_busy);
        }
        return true;
    }

    /** Implement options menu
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_DELETE:
                deleteItem();
                updateNotice();
                return true;
            case MENU_DELETE_ALL_OLD:
                deleteItemsOlder();
                updateNotice();
                return true;
            case MENU_INSERT:
                insertItem();
                updateNotice();
                return true;
            case MENU_TOOGLE_AVAILABLE:
                toogleAvailability();
                return true;
            case MENU_DATE_TODAY:
                mDateFrom = MeetupHelper.StartDayMillis(System.currentTimeMillis());
                fixPrevoiusDateWhereDataExist();
                return true;
            case MENU_DATE_EARLIER:
                mDateFrom = mDateFrom - Date.DAY_IN_MILLIS;
                fixPrevoiusDateWhereDataExist();
                return true;
            case MENU_DATE_LATER:
                mDateFrom = mDateFrom + Date.DAY_IN_MILLIS;
                fixPrevoiusDateWhereDataExist();
                return true;
            case MENU_OPTIONS_PROFILE:
                selectProfile(false);
                return true;
            case MENU_OPTIONS_ABOUT:
                Intent i = new Intent(ViewAgenda.this, ViewAbout.class);
                i.setAction(Intent.ACTION_DEFAULT);
                startActivityForResult(i, CMD_ABOUTBOX);
                return true;
            case MENU_FIND:
                startFindAdvanced();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Resume Agenda activity re-load settings
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (CursorAppointment.requery()) {
            fixPrevoiusDateWhereDataExist();
        }
        settings.load();
        updateNotice();
        int position = MeetupHelper.getClosestLongRow(CursorAppointment, AppointmentProvider.F_STARTS, System.currentTimeMillis());
        if ((position < listview.getCount()) & (position >= 0)) listview.setSelection(position);
        Intent intent = getIntent();
        notification_id = intent.getIntExtra(MeetupService.PAR_FROM_NOTIFICATION, -1);
        if (notification_id != -1) {
            NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNM.cancel(notification_id);
        }
    }

    /** Stop activity
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        settings.save();
    }

    /**
     * Insert a new appointment
     */
    private final void insertItem() {
        startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
    }

    /** get current user profile.
     * @param none
     * @return true if active profile is found, CursorProfile is set to the profile record
     */
    private final boolean getCurrentProfile() {
        int isSel = 0;
        if (CursorProfile == null) {
            return false;
        }
        if (CursorProfile.getCount() == 0) {
            return false;
        }
        if (CursorProfile.moveToFirst()) {
            while (true) {
                if (!CursorProfile.isNull(ProfileProvider.F_ISACTIVE)) {
                    isSel = CursorProfile.getInt(ProfileProvider.F_ISACTIVE);
                    if (isSel > 0) {
                        return true;
                    }
                }
                if (!CursorProfile.moveToNext()) break;
            }
        }
        return (isSel > 0);
    }

    /**
     * This method is called when the sending activity has finished, with the
     * result it supplied.
     * 
     * @param requestCode The original request code as given to
     *                    startActivity().
     * @param resultCode From sending activity as per setResult().
     * @param data From sending activity as per setResult().
     * @param extras From sending activity as per setResult().
     * @return none
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        Bundle extras = data.getExtras();
        switch(requestCode) {
            case CMD_EDIT_USER_PROFILE:
                if (resultCode == RESULT_CANCELED) {
                }
                rpc.setUserName(settings.UserName);
                rpc.setUserPassword(settings.UserPassword);
                break;
            case CMD_ABOUTBOX:
                break;
            case SvrCallRun.RPC_FINDREC:
            case SvrCallRun.RPC_FINDREC_SLICE:
                String userto = extras.getString(ViewUserInfo.KEY_VUI_NAME);
                Long datestarts;
                Long dateends;
                if (!CursorProfile.requery()) {
                }
                if (!CursorProfile.moveToFirst()) {
                    return;
                }
                datestarts = MeetupHelper.StartDayMillis(System.currentTimeMillis());
                dateends = datestarts + (settings.AppointmentDays * Date.DAY_IN_MILLIS);
                BusyTime[] busytimes = null;
                boolean confirm = false;
                int opcode = 0;
                int status = 0;
                String personname = CursorProfile.getString(ProfileProvider.F_NICK);
                String locationname = CursorProfile.getString(ProfileProvider.F_SVCLOCATIONNAME);
                Double latitude = CursorProfile.getDouble(ProfileProvider.F_LATITUDE);
                Double longitude = CursorProfile.getDouble(ProfileProvider.F_LONGITUDE);
                Double altitude = CursorProfile.getDouble(ProfileProvider.F_ALTITUDE);
                boolean needcoords = CursorProfile.getInt(ProfileProvider.F_SVCNEEDCOORDS) > 0;
                boolean locationisfixed = CursorProfile.getInt(ProfileProvider.F_SVCLOCATIONISFIXED) > 0;
                Long starts = CursorProfile.getLong(ProfileProvider.F_SVCSTARTS);
                Long ends = CursorProfile.getLong(ProfileProvider.F_SVCENDS);
                Long breakstarts = CursorProfile.getLong(ProfileProvider.F_SVCBREAKSTARTS);
                Long breakends = CursorProfile.getLong(ProfileProvider.F_SVCBREAKENDS);
                String action = "";
                String items = "";
                int qty = 0;
                Double cost = 0.0;
                String subject = "";
                String xmpp = CursorProfile.getString(ProfileProvider.F_XMPPNAME);
                String sip = CursorProfile.getString(ProfileProvider.F_SIP);
                String tel = CursorProfile.getString(ProfileProvider.F_PHONE);
                String address = CursorProfile.getString(ProfileProvider.F_SVCLOCATIONNAME);
                rpc.getappointments(0, 0, userto, datestarts, dateends, busytimes, confirm, opcode, status, personname, locationname, latitude, longitude, altitude, needcoords, locationisfixed, starts, ends, breakstarts, breakends, action, items, qty, cost, subject, xmpp, tel, sip, address, 0);
                break;
            case CMD_FIND_USER:
                break;
        }
    }

    /**
     * This method is called when the activity has freeze
     * @param outState
     */
    protected void onFreeze(Bundle outState) {
    }

    /** This method is called when the activity has pause
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (CursorProfile != null) {
            if (isFinishing()) {
            } else {
            }
        }
    }

    /**
     * Delete selected appointment from the list helper function
     * */
    private final void deleteItem() {
        long id = 1;
        getContentResolver().delete(ContentUris.withAppendedId(IntentUri, id), null, null);
        CursorAppointment.requery();
    }

    /**
     * Delete old appointments helper function
     * */
    private final void deleteItemsOlder() {
        getContentResolver().delete(IntentUri, AppointmentProvider.SQL_WHERE_OLDER, new String[] { mDateFrom.toString() });
    }

    /**
     * User mark menu item "I'm available" if he/she want to receive appointments to his/her working hours helper function
     * */
    private final void toogleAvailability() {
        int isOn = 0;
        if (CursorProfile.getCount() != 0) {
            if (!CursorProfile.isNull(ProfileProvider.F_SVCON)) {
                isOn = CursorProfile.getInt(ProfileProvider.F_SVCON);
            }
        }
        if (isOn > 0) isOn = 0; else isOn = 1;
        ContentValues values = new ContentValues();
        values.put(Profile.User.SVCON, isOn);
        ContentResolver c = getContentResolver();
        c.update(ContentUris.withAppendedId(Profile.User.CONTENT_URI, CursorProfile.getLong(0)), values, null, null);
        settings.ServiceOn = (isOn == 1);
    }

    /** create new profile or select from the list helper function
     * @param	ANew	true- force create a new profile, false- select from inactive profiles
     * @return			true	if profile created or selected. CursorProfile() is set to a new created profile record
     */
    private final boolean selectProfile(boolean ANew) {
        if (!ANew) {
            if (CursorProfile.getCount() > 0) {
                CursorProfile.moveToFirst();
                CursorProfile.getInt(ProfileProvider.F_ID);
            }
        }
        Uri data = Profile.User.CONTENT_URI;
        Intent i = new Intent(this, ViewSettings.class);
        startActivityForResult(i, CMD_EDIT_USER_PROFILE);
        return true;
    }

    /**
     * set button visibility helper function. 
     * @param v
     */
    private final void setNewButtonVisibility(boolean v) {
    }

    /** update screen elements. Update notice bar.
     * @param none
     * @return none
     */
    private final void updateNotice() {
        if (CursorAppointment == null) {
            setNewButtonVisibility(true);
            tvNotice.setText(getString(R.string.msg_cp_noticenoappointments));
            setTitle(getString(R.string.msg_cp_msgnoappointments));
        } else {
            Integer cnt = CursorAppointment.getCount();
            if (cnt <= 0) {
                setNewButtonVisibility(true);
                tvNotice.setText(getString(R.string.msg_cp_noticenoappointments));
                setTitle(getString(R.string.msg_cp_msgnoappointments));
            } else {
                setNewButtonVisibility(false);
                tvNotice.setText(getString(R.string.msg_cp_msgappointementcnt) + " " + cnt.toString());
            }
        }
    }

    /**
     * if no appointment at the {@link mDateFrom}, find out a day previous to mDate with any appointment
     * @param		none
     * @return		none
     */
    private void fixPrevoiusDateWhereDataExist() {
        if (CursorAppointment != null) {
            long vl;
            if (CursorAppointment.getCount() <= 0) {
                CursorAppointment = managedQuery(getIntent().getData(), AppointmentProvider.PROJECTION_APPOINTMENT, null, null, "starts DESC");
                if (CursorAppointment.getCount() <= 0) return;
                CursorAppointment.moveToFirst();
                while (true) {
                    vl = CursorAppointment.getLong(AppointmentProvider.F_STARTS);
                    if (vl <= mDateFrom) {
                        mDateFrom = MeetupHelper.StartDayMillis(vl);
                        CursorAppointment = managedQuery(getIntent().getData(), AppointmentProvider.PROJECTION_APPOINTMENT, AppointmentProvider.SQL_WHERE_NEWONLY, new String[] { mDateFrom.toString() }, null);
                        return;
                    }
                    if (!CursorAppointment.moveToNext()) break;
                }
            }
        }
    }

    /**
     * Start find a person via server
     * @param		none
     * @return		none
     * */
    private void startFindAdvanced() {
        Intent ifu = new Intent(ViewAgenda.this, ViewFind.class);
        ifu.setAction(Intent.ACTION_DEFAULT);
        startActivityForResult(ifu, CMD_FIND_USER);
    }

    /**
     * Simple search by keys (and other parameters) in fast search line.
     * RPC created in the Agenda callbacks to OnFindUser() and OnFindUserNoData() 
     * @param searchKeys
     */
    private void startSearchSimple(String searchKeys) {
        int capacity = 0;
        String findphone = null;
        String findxmpp = null;
        String findsip = null;
        String findkeys = searchKeys;
        if ((findkeys == null) || (findkeys.length() == 0)) return;
        long datestarts = System.currentTimeMillis();
        long dateends = datestarts + settings.timewindow;
        float radius = settings.radius;
        rpc.findrec(settings.ttl, null, findphone, findxmpp, findsip, settings.latitude, settings.longitude, settings.altitude, settings.findslice, findkeys, radius, datestarts, dateends, capacity, settings.findperson, settings.findlocation);
    }

    /**
     * When thread failed search person, or not found,
     * thread call OnFindUserNoData()
     * @param		none
     * @return		none
     * */
    public void OnFindUserNoData() {
        AlertDialog.Builder b = new AlertDialog.Builder(ViewAgenda.this);
        b.setIcon(R.drawable.service0);
        b.setTitle(R.string.msg_cp_dialog_findrec_no);
        b.setPositiveButton(R.string.ab_b_ok, null);
        b.create();
    }

    /**
     * When thread found some persons, it call OnFindUser()
     * @param		vui				vector if UserInfo
     * @param		parameters		keeping other info about searching, actually Agenda object as 
     * initiator of search procedure must to know about aspects of findrec(), but if more than one 
     * procedure was started, it would be helpful. 
     * @return		none
     * */
    public void OnFindUser(Vector<UserInfo> vui, Hashtable<String, String> parameters) {
        Intent mI = new Intent(this, ViewUserList.class);
        Uri data = Appointment.URI_APPOINTMENT_PROVIDER;
        mI.setData(data);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + ViewUserList.KEY_UL_HASHMAP, UserInfo.getVectorHashMap(vui));
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + ViewUserList.KEY_UL_CAPTION, String.format(getString(R.string.view_findrec), vui.size()));
        if (parameters != null) {
            if (parameters.get("s") != null) {
                mI.putExtra(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + ViewUserList.KEY_UL_DATESTARTS, parameters.get("s"));
            }
            if (parameters.get("e") != null) {
                mI.putExtra(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + ViewUserList.KEY_UL_DATEENDS, parameters.get("e"));
            }
        }
        mI.setAction(Intent.ACTION_PICK);
        this.startActivityForResult(mI, SvrCallRun.RPC_FINDREC);
    }

    /**
     * When thread found some persons, it call OnFindUserMap() to show them on map
     * @param		vui				vector if UserInfo
     * @param		parameters		keeping other info about searching, actually Agenda object as 
     * initiator of search procedure must to know about aspects of findrec(), but if more than one 
     * procedure was started, it would be helpful. 
     * @return		none
     * */
    public void OnFindUserMap(Vector<UserInfo> vui, Hashtable<String, String> parameters) {
        Intent mI = new Intent(this, ViewUserMap.class);
        Uri data = Appointment.URI_APPOINTMENT_PROVIDER;
        mI.setData(data);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + ViewUserList.KEY_UL_HASHMAP, UserInfo.getVectorHashMap(vui));
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + ViewUserList.KEY_UL_CAPTION, String.format(getString(R.string.view_findrec), vui.size()));
        if (parameters != null) {
            if (parameters.get("s") != null) {
                mI.putExtra(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + ViewUserList.KEY_UL_DATESTARTS, parameters.get("s"));
            }
            if (parameters.get("e") != null) {
                mI.putExtra(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + ViewUserList.KEY_UL_DATEENDS, parameters.get("e"));
            }
        }
        mI.setAction(Intent.ACTION_PICK);
        this.startActivityForResult(mI, SvrCallRun.RPC_FINDREC_SLICE);
    }

    private LocationProvider mLocProvider = null;

    private LocationManager mLocMgr;

    private Location location;

    /**
	 * connect to the location service
	 * @param none
	 * @return none 
	 * Set up mLocMgr, mLocProvider variables
	 */
    private void checkLocationSvc() {
        if (mLocProvider != null) return;
        mLocMgr = (LocationManager) getSystemService(android.content.Context.LOCATION_SERVICE);
        if (mLocMgr != null) {
            Criteria criteria = new Criteria();
            if (criteria != null) {
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_HIGH);
                criteria.setSpeedRequired(false);
                try {
                    String n = mLocMgr.getBestProvider(criteria, false);
                    if (n == null) n = LocationManager.NETWORK_PROVIDER;
                    mLocProvider = mLocMgr.getProvider(n);
                } catch (Exception e) {
                    Log.e("MS", getString(R.string.msg_current_profile_location_error));
                    mLocProvider = null;
                }
            }
        }
    }

    /**
     * Locate a service
     * @param fastLocate true- do not show a list but immediately select closest service  
     */
    private void fastLocate(String fastLocate) {
        checkLocationSvc();
        if (mLocProvider != null) {
            String n = mLocProvider.getName();
            if (mLocMgr.isProviderEnabled(n)) {
                location = mLocMgr.getLastKnownLocation(n);
                if (location != null) {
                    settings.latitude = location.getLatitude();
                    settings.longitude = location.getLongitude();
                    settings.altitude = location.getAltitude();
                }
            }
        }
        startSearchSimple(fastLocate);
    }

    /**
     * Place a call to the selected partner
     */
    private void placeCall() {
        if ((tel != null) && (tel.length() > 0)) {
            Uri t;
            if (!tel.startsWith("tel:")) tel = "tel:" + tel;
            t = Uri.parse(tel);
            Intent call = new Intent(Intent.ACTION_DIAL, t);
            startActivity(call);
        } else {
        }
    }

    /**
     * Place instant message to the selected partner
     */
    private void placeIM() {
        if ((xmpp != null) && (xmpp.length() > 0)) {
        } else {
        }
    }

    private void showSearchPanel(boolean on) {
        if (on) {
            if (searchLayout == null) {
                searchLayout = searchStub.inflate();
                bSearchClose = (ImageButton) findViewById(R.id.bSearchClose);
                bSearchClose.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        showSearchPanel(false);
                    }
                });
                mFastSearch = (EditText) findViewById(R.id.fastsearch);
                bFindUser = (ImageButton) findViewById(R.id.bAgendaFind);
                bFindUser.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View v) {
                        String s = mFastSearch.getText().toString();
                        if ((s != null) && (s.length() > 0)) startSearchSimple(s); else startFindAdvanced();
                    }
                });
                bLocate = (ImageButton) findViewById(R.id.bAgendaLocate);
                bLocate.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View v) {
                        String s = mFastSearch.getText().toString();
                        fastLocate(s);
                    }
                });
            } else {
                searchLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (searchLayout != null) searchLayout.setVisibility(View.GONE);
        }
    }
}
