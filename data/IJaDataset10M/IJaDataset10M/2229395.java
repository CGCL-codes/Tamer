package com.commandus.meetup;

import com.commandus.meetup.R;
import com.commandus.provider.Appointment;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.ImageView;

public class ViewTimeSlots extends Activity {

    public static final String KEY_TS_HASHMAP = "hmul";

    public static final String KEY_TS_CAPTION = "n";

    public static final String KEY_TS_TOUSER = "to";

    public static final String KEY_TS_CONFIRM = "c";

    public static final String KEY_TS_DATESTARTS = "dS";

    public static final String KEY_TS_DATEENDS = "dE";

    public static final String KEY_TS_STATUS = "t";

    public static final String KEY_TS_PERSONNAME = "p";

    public static final String KEY_TS_LOCATIONNAME = "T";

    public static final String KEY_TS_LATITUDE = "l";

    public static final String KEY_TS_LONGITUDE = "o";

    public static final String KEY_TS_ALTITUDE = "i";

    public static final String KEY_TS_NEEDCOORDS = "W";

    public static final String KEY_TS_LOCATIONISFIXED = "F";

    public static final String KEY_TS_STARTS = "S";

    public static final String KEY_TS_ENDS = "E";

    public static final String KEY_TS_BREAKSTARTS = "B";

    public static final String KEY_TS_BREAKENDS = "R";

    public static final String KEY_TS_TIMDEDURATION = "td";

    public static final String KEY_TS_ACTION = "a";

    public static final String KEY_TS_ITEMS = "m";

    public static final String KEY_TS_QTY = "q";

    public static final String KEY_TS_COST = "c";

    public static final String KEY_TS_SUBJECT = "j";

    public static final String KEY_TS_XMPP = "x";

    public static final String KEY_TS_TEL = "n";

    public static final String KEY_TS_ADDRESS = "A";

    public static final String KEY_TS_FLAGS = "f";

    public static final String KEY_TS_PERSON = "u";

    public static final String KEY_TS_LINK = "l";

    public static final String KEY_TS_SIP = "s";

    public static final String KEY_TS_PHOTO = "I";

    public static final String KEY_TS_VENUES = "vs";

    public static final String KEY_TS_OPERATION = "op";

    public static final String KEY_TS_GROUP = "g";

    public static final String KEY_TS_SLICE = "sl";

    public static final String KEY_TS_ORDER = "or";

    public static final String KEY_TS_STOP = "sp";

    private ImageView mPhoto;

    private ListView mTimeslots;

    private Spinner mVenues;

    private TextView mDate;

    private HashMap<String, Object> hm;

    private Integer mIndex = -1;

    private boolean notavailable;

    private MgrListStartEnd mMgrListSE;

    private ArrayList<String> timeslots;

    static final int SHOW_ABOUTBOX = 1;

    static final int CMD_SEL_TIMESLOT = 2;

    private static final int MENU_APPLY = Menu.FIRST + 0;

    private static final int MENU_CANCEL = Menu.FIRST + 1;

    public static final int MENU_CALL = Menu.FIRST + 3;

    public static final int MENU_XMPP = Menu.FIRST + 4;

    public static final int MENU_APPOINTMENT = Menu.FIRST + 5;

    public static final int MENU_LINK = Menu.FIRST + 6;

    public static final int MENU_ENLARGEPHOTO = Menu.FIRST + 7;

    private static final int MENU_OPTIONS_ABOUT = Menu.FIRST + 10;

    private ArrayAdapter<String> adapter;

    private long timeduration = 0L;

    MeetupSettings settings;

    private SvrCallRun rpc;

    UtilHttpContent mPhotoData = new UtilHttpContent();

    private String photoURL;

    String mToUser;

    String myLocationName;

    String mItems;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        final Intent intent = getIntent();
        hm = (HashMap<String, Object>) intent.getExtras().get(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_HASHMAP);
        boolean isSlotsProvided = (hm != null);
        if (isSlotsProvided) {
            mMgrListSE = new MgrListStartEnd(hm, KEY_TS_STARTS, KEY_TS_ENDS, true);
            notavailable = mMgrListSE.getTimeSlotsCount() == 0;
        } else {
            notavailable = true;
        }
        setContentView(R.layout.viewtimeslots);
        mPhoto = (ImageView) findViewById(R.id.TimeSlotsPhoto);
        photoURL = SvrCall.expandUrl(intent.getStringExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_PHOTO), null);
        startGettingPhoto(photoURL);
        timeduration = intent.getLongExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_TIMDEDURATION, 0L);
        if (notavailable) {
            timeslots = new ArrayList<String>();
            timeslots.add(getString(R.string.view_notimeslot));
        } else {
            timeslots = mMgrListSE.getArrayList(-1);
        }
        String caption = intent.getStringExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_CAPTION);
        if ((caption != null) && (caption.length() > 0)) {
            this.setTitle(caption);
        } else {
            setTitle(String.format(getText(R.string.view_timeslots).toString(), 0));
        }
        mDate = (TextView) findViewById(R.id.TimeslotsDate);
        mVenues = (Spinner) findViewById(R.id.TimeslotsLocationName);
        long ds = intent.getLongExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_DATESTARTS, 0L);
        long de = intent.getLongExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_DATEENDS, 0L);
        mDate.setText(String.format(getString(R.string.view_timeslot_starts), MeetupHelper.Milliseconds2Str(ds, null, 3)) + " " + String.format(getString(R.string.view_timeslot_ends), MeetupHelper.Milliseconds2Str(de, null, 3)));
        myLocationName = intent.getStringExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_LOCATIONNAME);
        mItems = intent.getStringExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_ITEMS);
        ArrayList<String> venues = new ArrayList<String>();
        if ((myLocationName != null) && (myLocationName != "")) venues.add(myLocationName);
        mToUser = intent.getStringExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_TOUSER);
        Bundle b = intent.getExtras();
        if (b != null) {
            HashMap<String, Object> hashVenues = (HashMap<String, Object>) b.get(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + KEY_TS_VENUES);
            if (hashVenues != null) {
                String v;
                for (int i = 0; ; i++) {
                    v = (String) hashVenues.get(String.valueOf(i) + Venue.KEY_VE_LOCATIONNAME);
                    if (v == null) break;
                    if (v != "") venues.add(v);
                }
            }
        }
        ArrayAdapter<String> venuesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, venues);
        mVenues.setAdapter(venuesAdapter);
        if (mVenues.getCount() > 1) mVenues.setSelection(1);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, timeslots);
        mTimeslots = (ListView) findViewById(R.id.LVTimeSlots);
        mTimeslots.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                mIndex = position;
            }

            public void onNothingSelected(AdapterView parent) {
                mIndex = -1;
            }
        });
        mTimeslots.setOnItemClickListener(new ListView.OnItemClickListener() {

            public void onItemClick(AdapterView a, View v, int position, long id) {
                Long s;
                Long e;
                String action = getIntent().getAction();
                if (Intent.ACTION_PICK.equals(action)) {
                    if (notavailable) {
                        setResult(SvrCallRun.RPC_NODATA);
                        finish();
                    } else {
                        s = mMgrListSE.getStarts(position);
                        if (s == null) {
                        } else {
                            e = mMgrListSE.getEnds(position);
                            selectSlot(s, e);
                        }
                    }
                } else {
                }
            }
        });
        mTimeslots.setAdapter(adapter);
    }

    private void selectSlot(long st, long en) {
        Intent mI = new Intent(this, ViewTimeSlot.class);
        Uri data = Appointment.URI_APPOINTMENT_PROVIDER;
        mI.setData(data);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlot.KEY_TS_TOUSER, mToUser);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlot.KEY_TS_CAPTION, String.format(this.getString(R.string.view_timeslot), MeetupHelper.Milliseconds2Str(st, ":", 3), MeetupHelper.Milliseconds2Str(en, ":", 3)));
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlot.KEY_TS_STARTS, st);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlot.KEY_TS_ENDS, en);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlot.KEY_TS_LOCATIONNAME, myLocationName);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlot.KEY_TS_ITEMS, mItems);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlot.KEY_TS_VENUENAME, (String) mVenues.getSelectedItem());
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlot.KEY_TS_TIMEDURATION, timeduration);
        mI.putExtra(Appointment.CONTENT_STRING_MEETUP_GETAPPOINTMENTS + "." + ViewTimeSlot.KEY_TS_PHOTO, photoURL);
        mI.setAction(Intent.ACTION_PICK);
        this.startActivityForResult(mI, CMD_SEL_TIMESLOT);
    }

    @SuppressWarnings("unused")
    private Bundle mkBundle() {
        Bundle b = new Bundle();
        if (mIndex >= 0) {
            Long x = (Long) hm.get(String.valueOf(mIndex) + KEY_TS_STARTS);
            b.putLong(KEY_TS_STARTS, x);
            x = (Long) hm.get(String.valueOf(mIndex) + KEY_TS_ENDS);
            b.putLong(KEY_TS_ENDS, x);
        }
        return b;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_CANCEL, Menu.NONE, R.string.tc_menu_cancel).setShortcut('2', 'c');
        menu.add(3, MENU_OPTIONS_ABOUT, Menu.NONE, R.string.menu_ap_about).setShortcut('9', 'u');
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_APPLY:
                setResult(RESULT_OK);
                finish();
                break;
            case MENU_CANCEL:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case MENU_CALL:
                String p = (String) hm.get(String.valueOf(mIndex) + ViewUserInfo.KEY_VUI_PHONE);
                try {
                    Intent c = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:" + p));
                    startActivity(c);
                } catch (Exception e) {
                }
                break;
            case MENU_XMPP:
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
            case MENU_ENLARGEPHOTO:
                if (photoURL != null) {
                    try {
                        Intent c = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(photoURL));
                        startActivity(c);
                    } catch (Exception e) {
                    }
                }
                break;
            case MENU_OPTIONS_ABOUT:
                Intent i = new Intent(ViewTimeSlots.this, AboutBox.class);
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
            case CMD_SEL_TIMESLOT:
                setResult(Agenda.CMD_FINISH_CHAIN_FIND);
                finish();
                break;
            case Agenda.CMD_FINISH_CHAIN_FIND:
                finish();
                break;
        }
    }

    private boolean startGettingPhoto(String photoURL) {
        URL uri;
        if (photoURL == null) return false;
        settings = new MeetupSettings(this);
        rpc = new SvrCallRun(this, settings.Host, settings.UserName, settings.UserPassword);
        try {
            uri = new URL(SvrCall.expandUrl(photoURL, null));
        } catch (Exception e) {
            return false;
        }
        rpc.gethttp(uri, mRunGetPhoto, mPhotoData);
        return true;
    }

    final Runnable mRunGetPhoto = new Runnable() {

        public void run() {
            OnCallGetPhotoReturn();
        }
    };

    private void OnCallGetPhotoReturn() {
        Bitmap bm = BitmapFactory.decodeStream(mPhotoData.body);
        mPhoto.setImageBitmap(bm);
    }
}
