package com.commandus.meetup;

import com.commandus.meetup.R;
import com.commandus.provider.Appointment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.net.URL;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;

public class ViewUserList extends Activity {

    public static final String KEY_UL_HASHMAP = "hmul";

    public static final String KEY_UL_CAPTION = "n";

    public static final String KEY_UL_SLICE = "s";

    public static final String KEY_UL_FILTER = "f";

    public static final String KEY_UL_DATESTARTS = "s";

    public static final String KEY_UL_DATEENDS = "e";

    private ListView mUsers;

    private TextView mKeys;

    private TextView mSlice;

    private TextView mLocation;

    private TextView mStartsEnds;

    private TextView mCompany;

    private TextView mDistance;

    private ImageView mPhoto;

    private MenuItem mMenuOptionsAnytime;

    private HashMap<String, Object> hm;

    private Integer mIndex = -1;

    private Long datestarts;

    private Long dateends;

    private Integer foundUsersCount;

    private String tel;

    private String xmpp;

    boolean isFindOp;

    static final int SHOW_ABOUTBOX = 1;

    private static final int MENU_CANCEL = Menu.FIRST + 0;

    public static final int MENU_OPTIONS_ABOUT = Menu.FIRST + 1;

    public static final int MENU_OPTIONS_ANYTIME = Menu.FIRST + 2;

    public static final int MENU_CALL = Menu.FIRST + 3;

    public static final int MENU_XMPP = Menu.FIRST + 4;

    public static final int MENU_APPOINTMENT = Menu.FIRST + 5;

    public static final int MENU_LINK = Menu.FIRST + 6;

    public static final int MENU_ENLARGEPHOTO = Menu.FIRST + 7;

    private SvrCallRun rpc;

    private MeetupSettings settings;

    boolean mHasPhoto;

    String mPhotoURL;

    UtilHttpContent mPhotoData = new UtilHttpContent();

    int loadPhotoCnt = 0;

    boolean addString(Object o, ArrayList<String> r) {
        if (o != null) {
            r.add(o.toString());
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        final Intent intent = getIntent();
        settings = new MeetupSettings(this);
        rpc = new SvrCallRun(this, settings.Host, settings.UserName, settings.UserPassword);
        Object n;
        Object nn;
        String title = null;
        Bundle b = intent.getExtras();
        if (b != null) {
            hm = (HashMap<String, Object>) b.get(Appointment.CONTENT_STRING_MEETUP_LS + "." + KEY_UL_HASHMAP);
        }
        isFindOp = (hm == null);
        final ArrayList<String> users = new ArrayList<String>();
        if (isFindOp) {
            hm = (HashMap<String, Object>) intent.getExtras().get(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + KEY_UL_HASHMAP);
            String caption;
            for (foundUsersCount = 0; ; foundUsersCount++) {
                n = hm.get(foundUsersCount.toString() + ViewUserInfo.KEY_VUI_NAME);
                if (n == null) {
                    break;
                }
                nn = hm.get(foundUsersCount.toString() + ViewUserInfo.KEY_VUI_NICK);
                if (nn != null) caption = (String) nn; else caption = "";
                nn = hm.get(foundUsersCount.toString() + ViewUserInfo.KEY_VUI_SERVICENAME);
                if (nn != null) caption = caption + " " + (String) nn;
                nn = hm.get(foundUsersCount.toString() + ViewUserInfo.KEY_VUI_LOCATIONNAME);
                if (nn != null) caption = caption + " " + (String) nn;
                if (caption.length() < 3) {
                    caption = caption + " " + (String) n;
                }
                addString(caption, users);
            }
        } else {
            foundUsersCount = 0;
            addString(getString(R.string.msg_cp_dialog_findrec_no).toString(), users);
        }
        title = intent.getStringExtra(Appointment.CONTENT_STRING_MEETUP_FINDREC + "." + KEY_UL_CAPTION);
        if (title != null) {
            this.setTitle(title);
        }
        datestarts = intent.getLongExtra(KEY_UL_DATESTARTS, 0);
        if (datestarts == null) datestarts = System.currentTimeMillis();
        dateends = intent.getLongExtra(KEY_UL_DATEENDS, 0);
        if (dateends == null) dateends = datestarts + settings.timewindow;
        if (dateends <= datestarts) dateends = datestarts + settings.timewindow;
        setContentView(R.layout.viewusers);
        mCompany = (TextView) findViewById(R.id.UsersCompanyName);
        mDistance = (TextView) findViewById(R.id.UsersDetails);
        mKeys = (TextView) findViewById(R.id.UsersKeys);
        mSlice = (TextView) findViewById(R.id.UsersSlice);
        mLocation = (TextView) findViewById(R.id.UsersLocation);
        mStartsEnds = (TextView) findViewById(R.id.UsersStartsEnds);
        mPhoto = (ImageView) findViewById(R.id.UsersPhoto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        mUsers = (ListView) findViewById(R.id.LVUsers);
        mUsers.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                mIndex = position;
                tel = (String) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_PHONE);
                xmpp = (String) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_XMPP);
                String n;
                n = (String) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_SERVICENAME);
                if (n == null) n = "";
                mCompany.setText(String.format(getText(R.string.view_users_company).toString(), n));
                n = (String) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_KEYS);
                if (n == null) n = "";
                mKeys.setText(MeetupHelper.validateKeys(n));
                Float f = (Float) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_RADIUS);
                if (f == null) mDistance.setText(getText(R.string.view_users_no_distance).toString()); else mDistance.setText(String.format(getText(R.string.view_users_distance).toString(), f / 1000));
                String u = (String) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_PHOTO);
                mHasPhoto = false;
                if (u == null) {
                    mPhoto.setImageResource(R.drawable.badge_pic);
                } else {
                    mPhotoURL = SvrCall.expandUrl(u, null);
                    startGettingPhoto();
                    mHasPhoto = true;
                }
                u = (String) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_SLICE);
                if (f == null) mSlice.setText(getText(R.string.view_users_no_slice).toString()); else mSlice.setText(u);
                u = (String) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_LOCATIONNAME);
                if (f == null) mLocation.setText(getText(R.string.view_users_no_location).toString()); else mLocation.setText(u);
                Long st = (Long) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_STARTS);
                Long en = (Long) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_ENDS);
                if (st == null) st = 0L;
                if (en == null) en = 0L;
                if (st == en) mStartsEnds.setText(getText(R.string.view_users_no_startsends).toString()); else mStartsEnds.setText(MeetupHelper.Milliseconds2Str(st, ":", 0) + " - " + MeetupHelper.Milliseconds2Str(en, ":", 0));
            }

            public void onNothingSelected(AdapterView parent) {
                mIndex = -1;
            }
        });
        mUsers.setOnItemClickListener(new ListView.OnItemClickListener() {

            public void onItemClick(AdapterView a, View v, int position, long id) {
                setAppointment(position);
            }
        });
        registerForContextMenu(mUsers);
        mUsers.setAdapter(adapter);
    }

    private void setAppointment(int position) {
        mIndex = position;
        if ((position >= 0) && (foundUsersCount > 0)) {
            String userto = (String) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_NAME);
            BusyTime[] busytimes = null;
            int opcode = 0;
            int status = 0;
            String action = "";
            String items = "";
            int qty = 0;
            Double cost = 0.0;
            String subject = "";
            long st;
            long en;
            long bst;
            long ben;
            if (settings.anytime) {
                st = 0;
                en = com.commandus.util.Date.DAY_IN_MILLIS;
                bst = 0;
                ben = 0;
            } else {
                st = settings.starts;
                en = settings.ends;
                bst = settings.breakstarts;
                ben = settings.breakends;
            }
            rpc.setPhoto((String) hm.get(mIndex.toString() + ViewUserInfo.KEY_VUI_PHOTO));
            rpc.getappointments(0, 0, userto, datestarts, dateends, busytimes, settings.confirm, opcode, status, settings.Nick, settings.LocationName, settings.latitude, settings.longitude, settings.altitude, settings.needcoords, settings.locationisfixed, st, en, bst, ben, action, items, qty, cost, subject, settings.xmpp, settings.tel, settings.sip, settings.address, 0);
        }
    }

    /** helper function creates bundle for intent
	 *  @param	none
	 *  @return	Bundle
	 */
    @SuppressWarnings("unused")
    private Bundle mkBundle() {
        Bundle b = new Bundle();
        if (mIndex >= 0) {
            String x = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_NAME);
            b.putString(ViewUserInfo.KEY_VUI_NAME, x);
            x = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_NICK);
            b.putString(ViewUserInfo.KEY_VUI_NICK, x);
            x = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_KEYS);
            b.putString(ViewUserInfo.KEY_VUI_KEYS, x);
            x = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_PHONE);
            b.putString(ViewUserInfo.KEY_VUI_PHONE, x);
            x = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_PHOTO);
            b.putString(ViewUserInfo.KEY_VUI_PHOTO, x);
            x = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_LINK);
            b.putString(ViewUserInfo.KEY_VUI_LINK, x);
            Double d = (Double) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_LATITUDE);
            b.putDouble(ViewUserInfo.KEY_VUI_LATITUDE, d);
            d = (Double) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_LONGITUDE);
            b.putDouble(ViewUserInfo.KEY_VUI_LONGITUDE, d);
            d = (Double) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_ALTITUDE);
            b.putDouble(ViewUserInfo.KEY_VUI_ALTITUDE, d);
            x = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_SERVICENAME);
            b.putString(ViewUserInfo.KEY_VUI_SERVICENAME, x);
            Long t = (Long) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_STARTS);
            b.putLong(ViewUserInfo.KEY_VUI_STARTS, t);
            t = (Long) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_ENDS);
            b.putLong(ViewUserInfo.KEY_VUI_ENDS, t);
            t = (Long) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_BREAKSTARTS);
            b.putLong(ViewUserInfo.KEY_VUI_BREAKSTARTS, t);
            t = (Long) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_BREAKENDS);
            b.putLong(ViewUserInfo.KEY_VUI_BREAKENDS, t);
        }
        return b;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(R.string.menu_ap_connect);
        menu.add(0, MENU_CALL, 0, R.string.menu_ap_call);
        menu.add(0, MENU_XMPP, 0, R.string.menu_ap_xmpp);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_CALL:
                placeCall();
                return true;
            case MENU_XMPP:
                placeCall();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem mi = menu.add(0, MENU_CANCEL, Menu.NONE, R.string.tc_menu_cancel);
        mi.setShortcut('2', 'c');
        mi.setIcon(R.drawable.mcancel);
        mi = menu.add(0, MENU_APPOINTMENT, Menu.NONE, R.string.menu_ap_appointment);
        mi.setShortcut('1', 'a');
        mi.setIcon(R.drawable.mapply);
        mMenuOptionsAnytime = menu.add(0, MENU_OPTIONS_ANYTIME, Menu.NONE, R.string.menu_users_anytime);
        mMenuOptionsAnytime.setShortcut('5', 'y');
        mMenuOptionsAnytime.setCheckable(true);
        mMenuOptionsAnytime.setChecked(settings.anytime);
        if (settings.anytime) mMenuOptionsAnytime.setIcon(R.drawable.mavailable); else mMenuOptionsAnytime.setIcon(R.drawable.mnotavailable);
        mi = menu.add(0, MENU_OPTIONS_ABOUT, Menu.NONE, R.string.menu_ap_about);
        mi.setShortcut('9', 'u');
        mi.setIcon(R.drawable.mabout);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        settings.load();
    }

    @Override
    protected void onStop() {
        super.onStop();
        settings.save();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean havePhone = (mIndex >= 0);
        boolean haveXmpp;
        boolean haveLink;
        String p, x;
        menu.removeGroup(1);
        menu.removeGroup(2);
        menu.removeGroup(3);
        if (havePhone) {
            p = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_PHONE);
            havePhone = (p != null);
            x = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_XMPP);
            haveXmpp = (x != null);
            hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_LINK);
            haveLink = (x != null);
        } else {
            haveXmpp = false;
            haveLink = false;
        }
        if (havePhone) {
            menu.add(1, MENU_CALL, Menu.NONE, R.string.menu_ap_call).setShortcut('3', 'p');
        }
        if (haveXmpp) {
            menu.add(2, MENU_XMPP, Menu.NONE, R.string.menu_ap_xmpp).setShortcut('4', 'x');
        }
        if (haveLink) {
            menu.add(3, MENU_LINK, Menu.NONE, R.string.menu_ap_link).setShortcut('5', 'w');
        }
        if (mHasPhoto) {
            menu.add(3, MENU_ENLARGEPHOTO, Menu.NONE, R.string.menu_ap_enlargephoto).setShortcut('6', 'p');
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_CANCEL:
                setResult(RESULT_OK);
                finish();
                break;
            case MENU_CALL:
                String p = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_PHONE);
                try {
                    Intent c = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + p));
                    startActivity(c);
                } catch (Exception e) {
                }
                break;
            case MENU_XMPP:
                String x = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_PHONE);
                hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_XMPP);
                try {
                } catch (Exception e) {
                }
                break;
            case MENU_LINK:
                String l = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_LINK);
                try {
                    Intent c = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(SvrCall.expandUrl(l, null)));
                    startActivity(c);
                } catch (Exception e) {
                }
                break;
            case MENU_APPOINTMENT:
                setAppointment(mUsers.getSelectedItemPosition());
                break;
            case MENU_OPTIONS_ANYTIME:
                settings.anytime = !settings.anytime;
                mMenuOptionsAnytime.setChecked(settings.anytime);
                if (settings.anytime) mMenuOptionsAnytime.setIcon(R.drawable.mavailable); else mMenuOptionsAnytime.setIcon(R.drawable.mnotavailable);
                break;
            case MENU_ENLARGEPHOTO:
                if (mHasPhoto) {
                    try {
                        Intent c = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mPhotoURL));
                        startActivity(c);
                    } catch (Exception e) {
                    }
                }
                break;
            case MENU_OPTIONS_ABOUT:
                MeetupHelper.SendObjectXMPP();
                Intent i = new Intent(ViewUserList.this, AboutBox.class);
                i.setAction(Intent.ACTION_DEFAULT);
                startActivityForResult(i, SHOW_ABOUTBOX);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        switch(requestCode) {
            case SHOW_ABOUTBOX:
                break;
            case Agenda.CMD_FINISH_CHAIN_FIND:
                setResult(Agenda.CMD_FINISH_CHAIN_FIND);
                finish();
                break;
        }
    }

    private boolean startGettingPhoto() {
        URL uri;
        try {
            uri = new URL(mPhotoURL);
        } catch (Exception e) {
            return false;
        }
        if (loadPhotoCnt > 0) return false;
        loadPhotoCnt++;
        rpc.gethttp(uri, mRunGetPhoto, mPhotoData);
        return true;
    }

    final Runnable mRunGetPhoto = new Runnable() {

        public void run() {
            OnCallGetPhotoReturn();
        }
    };

    private void OnCallGetPhotoReturn() {
        loadPhotoCnt--;
        Bitmap bm = BitmapFactory.decodeStream(mPhotoData.body);
        mPhoto.setImageBitmap(bm);
    }

    public void OnGetAppointmentsNoData() {
        AlertDialog.Builder dlg;
        dlg = new AlertDialog.Builder(this);
        dlg.setIcon(R.drawable.service0);
        dlg.setTitle(getString(R.string.msg_cp_dialog_getappointments));
        dlg.setPositiveButton(R.string.ab_b_ok, null);
        dlg.create();
    }

    /**
	 *	start ViewTimeSlots activity
	 * */
    public void OnGetAppointments(AppointmentVenueItems avitems, Hashtable<String, String> parameters, String photoUrl) {
        Intent mI = new Intent(this, ViewTimeSlots.class);
        Uri data = Appointment.URI_APPOINTMENT_PROVIDER;
        mI.setData(data);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlots.KEY_TS_HASHMAP, AppointmentItem.getVectorHashMap(avitems.Appointments));
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlots.KEY_TS_VENUES, Venue.getVectorHashMap(avitems.Venues));
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlots.KEY_TS_CAPTION, String.format(getString(R.string.view_timeslots), avitems.Appointments.size()));
        if (parameters == null) {
            mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlots.KEY_TS_CAPTION, getString(R.string.view_timeslots));
        } else {
            SvrCallRun.AppParameters2Intent(parameters, mI);
        }
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlots.KEY_TS_TIMDEDURATION, avitems.timeDuration);
        if (photoUrl != null) {
            mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlots.KEY_TS_PHOTO, photoUrl);
        }
        mI.setAction(Intent.ACTION_PICK);
        startActivityForResult(mI, SvrCallRun.RPC_GETAPPOINTMENTS);
        finish();
    }

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

    private void placeIM() {
        if ((xmpp != null) && (xmpp.length() > 0)) {
            Uri t = Uri.parse(xmpp);
        } else {
        }
    }
}
