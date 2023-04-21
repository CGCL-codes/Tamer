package it.openalmanac;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.Color;
import org.stardate.Stardate;
import it.openalmanac.R;
import com.luckycatlabs.sunrisesunset.dto.Location;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import astroLib.HijriCalendar;
import org.jscience.history.calendars.*;

public class AlmanacList extends Activity {

    private Calendar cal;

    private SQLiteDatabase db;

    private SaintDBEvent current;

    private Stardate m_stardate = null;

    private MayanCalendar instance;

    private double m_phaseValue;

    private int percent;

    private boolean ww;

    private String[] strDays = null;

    private String[] strMonths = null;

    private String dawn = null;

    private String dusk = null;

    private String hijriDate = null;

    private double lat;

    private double lng;

    private ArrayList<HashMap<String, Object>> data;

    private SimpleAdapter adapter;

    private static final String TAG = "OpenAlmanacList";

    private static final String ALMANAC_DATABASE_NAME = "almanac.db";

    private LocationManager locationManager;

    private static final long MIN_TIME = 10 * 60000;

    private static final float MIN_DIST = 1000;

    /**
	 * get if this is the first run
	 * 
	 * @return returns true, if this is the first run
	 */
    public boolean getFirstRun() {
        return mPrefs.getBoolean("firstRun", true);
    }

    /**
	 * get the Latitude Pref 
	 * 
	 * @return returns String Latitude Prefs
	 */
    public String getLatPrefs() {
        return mPrefs.getString("Latitude", "0.0");
    }

    /**
	 * get the Longitude Pref 
	 * 
	 * @return returns String Latitude Prefs
	 */
    public String getLongPrefs() {
        return mPrefs.getString("Longitude", "0.0");
    }

    /**
	 * store the first run
	 */
    public void setRunned() {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putBoolean("firstRun", false);
        edit.commit();
    }

    public void setLatLongPrefs(String Lat, String Long) {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString("Latitude", Lat);
        edit.putString("Longitude", Long);
        edit.commit();
    }

    SharedPreferences mPrefs;

    /**
	 * setting up preferences storage
	 */
    public void firstRunPreferences() {
        Context mContext = this.getApplicationContext();
        mPrefs = mContext.getSharedPreferences("AlmanacPrefs", 0);
    }

    public static final Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setSpeedRequired(false);
        return criteria;
    }

    private Calendar getDate() {
        return cal;
    }

    private void setDate(Calendar date) {
        cal = date;
        Date dateInput = cal.getTime();
        Log.d(TAG, "New date: " + dateInput);
        UpdateAllData();
    }

    private int CalculateEasterAndOtherMobileFestivity(Date dateInput) {
        int MyReturn = -1;
        try {
            if (Easter.isEaster(dateInput)) {
                MyReturn = 0;
            } else {
                MyReturn = -1;
            }
        } catch (EasterException e) {
            Log.e(TAG, e.getMessage());
        }
        return MyReturn;
    }

    private void UpdateAllData() {
        Date date = cal.getTime();
        SimpleDateFormat formatPattern = new SimpleDateFormat("yyyy");
        String nowYearFormatted = formatPattern.format(date);
        m_stardate = new Stardate();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(cal.getTime());
        m_stardate.setGregorian(gc);
        double phase = AlmanacPhaseOfMoon.MoonPhase(cal.getTimeInMillis());
        Log.i(TAG, "New computed moon phase: " + phase);
        m_phaseValue = phase;
        percent = (int) (50.0 * (1.0 - Math.cos(phase)) + 0.5);
        ww = (phase < Math.PI) ? true : false;
        int NumberPhase = (int) (Math.toDegrees(phase) / 12);
        Log.d(TAG, "New day: " + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        Log.d(TAG, "New month: " + Integer.toString(cal.get(Calendar.MONTH) + 1));
        if (db.isOpen()) {
            Log.d(TAG, "Language Default: " + java.util.Locale.getDefault().getLanguage());
            if (java.util.Locale.getDefault().getLanguage().equals("it")) {
                current = SaintDBEvent.getByDateAndLang(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), Integer.toString(cal.get(Calendar.MONTH) + 1), "it", db);
            } else {
                current = SaintDBEvent.getByDateAndLang(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), Integer.toString(cal.get(Calendar.MONTH) + 1), "en", db);
            }
            switch(CalculateEasterAndOtherMobileFestivity(date)) {
                case 50:
                    break;
                case 0:
                    current.setSaintName(getResources().getString(R.string.easter_name_label));
                    current.setSaintDescription(getResources().getString(R.string.easter_description_label));
                    break;
                case 1:
                    break;
                default:
            }
        }
        AlmanacUtility almanac = AlmanacUtility.getInstance();
        if (!almanac.isLocationServiceAvaiable(this.getApplicationContext())) {
            Toast.makeText(this, getResources().getString(R.string.warning_gps_notactive), Toast.LENGTH_LONG).show();
        }
        Location location = new Location(getLatPrefs(), getLongPrefs());
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, cal.getTimeZone().getID());
        Log.d(TAG, "TimeZone Correct: " + cal.getTimeZone().getID());
        dawn = calculator.getOfficialSunriseForDate(cal);
        dusk = calculator.getOfficialSunsetForDate(cal);
        Log.d(TAG, "New OfficialcivilSunrise: " + dawn);
        Log.d(TAG, "New OfficialcivilSunset: " + dusk);
        HijriCalendar hijriCalendar = new HijriCalendar(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.DATE));
        Log.d(TAG, "New Islamic data insert: " + Integer.toString(cal.get(Calendar.YEAR)) + "," + Integer.toString(cal.get(Calendar.MONTH) + 1) + "," + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        hijriDate = hijriCalendar.getHicriTakvim();
        org.jscience.history.calendars.GregorianCalendar calendar = new org.jscience.history.calendars.GregorianCalendar(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
        instance = new MayanCalendar(calendar);
        instance.toRD();
        data.get(0).put("description", strDays[cal.get(Calendar.DAY_OF_WEEK) - 1] + ", " + cal.get(Calendar.DATE) + " " + strMonths[cal.get(Calendar.MONTH)] + " " + nowYearFormatted);
        data.get(1).put("description", m_stardate.toStardateString());
        data.get(2).put("description", instance.toString());
        data.get(3).put("description", hijriDate);
        data.get(4).put("event", current.getSaintName());
        data.get(4).put("description", current.getSaintDescription());
        data.get(5).put("description", dawn + ", " + dusk);
        data.get(6).put("description", getResources().getString(getPhaseText(percent, ww)));
        data.get(6).put("image", IMAGE_LOOKUP[NumberPhase]);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almanaclist);
        firstRunPreferences();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(getCriteria(), true);
        Log.d(TAG, "Criteria: " + provider);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DIST, locationListener);
        } else {
        }
        GradientDrawable grad = new GradientDrawable(Orientation.TOP_BOTTOM, new int[] { Color.DKGRAY, Color.BLACK });
        this.getWindow().setBackgroundDrawable(grad);
        cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat formatPattern = new SimpleDateFormat("yyyy");
        String nowYearFormatted = formatPattern.format(date);
        m_stardate = new Stardate();
        m_stardate.setGregorian(new GregorianCalendar());
        double phase = AlmanacPhaseOfMoon.MoonPhase(cal.getTimeInMillis());
        Log.i(TAG, "New computed moon phase: " + phase);
        m_phaseValue = phase;
        percent = (int) (50.0 * (1.0 - Math.cos(phase)) + 0.5);
        ww = (phase < Math.PI) ? true : false;
        int NumberPhase = (int) (Math.toDegrees(phase) / 12);
        AlmanacSQLiteDatabaseAdapter aSQLiteDatabaseAdapter = AlmanacSQLiteDatabaseAdapter.getInstance(this, ALMANAC_DATABASE_NAME);
        if (getFirstRun()) {
            db = aSQLiteDatabaseAdapter.getDatabase();
            setRunned();
        } else {
            db = aSQLiteDatabaseAdapter.getWritableDatabase();
        }
        Log.d(TAG, "Day: " + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        Log.d(TAG, "Month: " + Integer.toString(cal.get(Calendar.MONTH) + 1));
        Log.d(TAG, "Language Default: " + java.util.Locale.getDefault().getLanguage());
        if (java.util.Locale.getDefault().getLanguage().equals("it")) {
            current = SaintDBEvent.getByDateAndLang(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), Integer.toString(cal.get(Calendar.MONTH) + 1), "it", db);
        } else {
            current = SaintDBEvent.getByDateAndLang(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), Integer.toString(cal.get(Calendar.MONTH) + 1), "en", db);
        }
        switch(CalculateEasterAndOtherMobileFestivity(date)) {
            case 50:
                break;
            case 0:
                current.setSaintName(getResources().getString(R.string.easter_name_label));
                current.setSaintDescription(getResources().getString(R.string.easter_description_label));
                break;
            case 1:
                break;
            default:
        }
        AlmanacUtility almanac = AlmanacUtility.getInstance();
        if ((almanac.isLocationServiceAvaiable(this.getApplicationContext())) == false) {
            Toast.makeText(this, getResources().getString(R.string.warning_gps_notactive), Toast.LENGTH_LONG).show();
        }
        Location location = new Location(getLatPrefs(), getLongPrefs());
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, cal.getTimeZone().getID());
        Log.d(TAG, "TimeZone Correct: " + cal.getTimeZone().getID());
        dawn = calculator.getOfficialSunriseForDate(cal);
        dusk = calculator.getOfficialSunsetForDate(cal);
        Log.d(TAG, "OfficialCivilSunrise: " + dawn);
        Log.d(TAG, "OfficialCivilSunset: " + dusk);
        HijriCalendar hijriCalendar = new HijriCalendar(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.DAY_OF_MONTH));
        Log.d(TAG, "Islamic data insert: " + Integer.toString(cal.get(Calendar.YEAR)) + "," + Integer.toString(cal.get(Calendar.MONTH) + 1) + "," + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        Log.d(TAG, "Islamic data converted: " + hijriCalendar.getHijriDay() + "," + hijriCalendar.getHijriMonthName() + "," + hijriCalendar.getHijriYear());
        hijriDate = hijriCalendar.getHicriTakvim();
        org.jscience.history.calendars.GregorianCalendar calendar = new org.jscience.history.calendars.GregorianCalendar(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
        instance = new MayanCalendar(calendar);
        instance.toRD();
        strDays = getResources().getStringArray(R.array.daysofweek_label);
        strMonths = getResources().getStringArray(R.array.monthsofyear_label);
        ArrayList<Event> eventList = new ArrayList<Event>();
        eventList.add(new Event(getResources().getString(R.string.todayis_label), strDays[cal.get(Calendar.DAY_OF_WEEK) - 1] + ", " + cal.get(Calendar.DATE) + " " + strMonths[cal.get(Calendar.MONTH)] + " " + nowYearFormatted, R.drawable.clock));
        eventList.add(new Event(getResources().getString(R.string.stardate_label), m_stardate.toStardateString(), R.drawable.treklogo));
        eventList.add(new Event(getResources().getString(R.string.mayan_calendar_label), instance.toString(), R.drawable.mayancalendar));
        eventList.add(new Event(getResources().getString(R.string.islamic_calendar_label), hijriDate, R.drawable.islamlogo));
        eventList.add(new Event(current.getSaintName(), current.getSaintDescription(), R.drawable.angel));
        eventList.add(new Event(getResources().getString(R.string.sunrisesunsite_label), dawn + ", " + dusk, R.drawable.sunrise));
        eventList.add(new Event(getResources().getString(R.string.moonphase_label), getResources().getString(getPhaseText(percent, ww)), IMAGE_LOOKUP[NumberPhase]));
        data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < eventList.size(); i++) {
            Event p = eventList.get(i);
            HashMap<String, Object> eventMap = new HashMap<String, Object>();
            eventMap.put("image", p.getPhotoRes());
            eventMap.put("event", p.getEventName());
            eventMap.put("description", p.getDescription());
            data.add(eventMap);
        }
        String[] from = { "image", "event", "description" };
        int[] to = { R.id.eventImage, R.id.eventName, R.id.eventDescription };
        adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.event_item, from, to);
        ((ListView) findViewById(R.id.eventListView)).setAdapter(adapter);
    }

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(android.location.Location location) {
            String latLongString = "";
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                latLongString = "Lat:" + Double.toString(lat) + "\nLong:" + Double.toString(lng);
                Log.d(TAG, latLongString);
                Location Almanaclocation = new Location(Double.toString(lat), Double.toString(lng));
                SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(Almanaclocation, cal.getTimeZone().getID());
                Log.d(TAG, "TimeZone Correct: " + cal.getTimeZone().getID());
                dawn = calculator.getCivilSunriseForDate(cal);
                dusk = calculator.getCivilSunsetForDate(cal);
                String dawn = calculator.getOfficialSunriseForDate(cal);
                String dusk = calculator.getOfficialSunsetForDate(cal);
                Log.d(TAG, "OfficialCivilSunrise: " + dawn);
                Log.d(TAG, "OfficialCivilSunset: " + dusk);
                data.get(5).put("description", dawn + ", " + dusk);
                adapter.notifyDataSetChanged();
                setLatLongPrefs(Double.toString(lat), Double.toString(lng));
                Log.d(TAG, "Lat saved: " + Double.toString(lat));
                Log.d(TAG, "Long saved: " + Double.toString(lng));
                locationManager.removeUpdates(locationListener);
            } else {
                latLongString = "No location found";
                Log.d(TAG, latLongString);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private String CopyToClipboard() {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(getResources().getString(R.string.todayis_label));
        strBuffer.append(" " + strDays[cal.get(Calendar.DAY_OF_WEEK) - 1] + ", " + cal.get(Calendar.DATE) + " " + strMonths[cal.get(Calendar.MONTH)] + "\n");
        strBuffer.append(getResources().getString(R.string.stardate_label));
        strBuffer.append(" " + m_stardate.toStardateString() + "\n");
        strBuffer.append(getResources().getString(R.string.mayan_calendar_label) + " " + instance.toString() + "\n");
        strBuffer.append(getResources().getString(R.string.islamic_calendar_label));
        strBuffer.append(" " + hijriDate + "\n");
        strBuffer.append(getResources().getString(R.string.saintofday_label));
        strBuffer.append(current.getSaintName() + " " + current.getSaintDescription() + "\n");
        strBuffer.append(getResources().getString(R.string.sunrisesunsite_label) + " " + dawn + "," + dusk + "\n");
        strBuffer.append(getResources().getString(R.string.moonphase_label) + " " + getResources().getString(getPhaseText(percent, ww)) + "\n");
        return strBuffer.toString();
    }

    public void share(String subject, String text) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, getString(R.string.sharedata_menu_label)));
    }

    private int getPhaseText(int perceValue, boolean ww) {
        if (perceValue == 0) {
            return R.string.new_moon;
        } else if (perceValue > 0 && perceValue < 50 && ww == true) {
            return R.string.waxing_crescent;
        } else if ((perceValue == 50) && (ww == true)) {
            return R.string.first_quarter;
        } else if ((perceValue > 50) && (perceValue < 100) && (ww == true)) {
            return R.string.waxing_gibbous;
        } else if (perceValue == 100) {
            return R.string.full_moon;
        } else if ((perceValue > 50) && (perceValue < 100) && (ww == false)) {
            return R.string.waning_gibbous;
        } else if ((perceValue == 50) && (ww == false)) {
            return R.string.third_quarter;
        } else {
            return R.string.waning_crescent;
        }
    }

    private static final int[] IMAGE_LOOKUP = { R.drawable.moon0, R.drawable.moon1, R.drawable.moon2, R.drawable.moon3, R.drawable.moon4, R.drawable.moon5, R.drawable.moon6, R.drawable.moon7, R.drawable.moon8, R.drawable.moon9, R.drawable.moon10, R.drawable.moon11, R.drawable.moon12, R.drawable.moon13, R.drawable.moon14, R.drawable.moon15, R.drawable.moon16, R.drawable.moon17, R.drawable.moon18, R.drawable.moon19, R.drawable.moon20, R.drawable.moon21, R.drawable.moon22, R.drawable.moon23, R.drawable.moon24, R.drawable.moon25, R.drawable.moon26, R.drawable.moon27, R.drawable.moon28, R.drawable.moon29 };

    private static final int MENUITEM_TODAY_ID = 0;

    private static final int MENUITEM_CHOOSE_DATE_ID = 1;

    private static final int MENUITEM_MAKE_SCREENSHOT_ID = 2;

    private static final int MENUITEM_TEXT_TO_SPEECH_ID = 3;

    private static final int MENUITEM_SHARE_DATA_ID = 4;

    private static final int MENUITEM_COPY_TO_CLIPBOARD_ID = 5;

    private static final int MENUITEM_OPTIONS_ID = 6;

    private static final int DATE_DIALOG_ID = 1;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_DIALOG_ID:
                Calendar date = getDate();
                return new DatePickerDialog(this, mDateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
            case DATE_DIALOG_ID:
                Calendar date = getDate();
                ((DatePickerDialog) dialog).updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar date = Calendar.getInstance();
            date.set(year, monthOfYear, dayOfMonth);
            setDate(date);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item;
        item = menu.add(0, MENUITEM_TODAY_ID, 0, getResources().getString(R.string.today_label));
        item.setIcon(android.R.drawable.ic_menu_today);
        item = menu.add(0, MENUITEM_CHOOSE_DATE_ID, 0, getResources().getString(R.string.choose_date_label));
        item.setIcon(android.R.drawable.ic_menu_month);
        item = menu.add(0, MENUITEM_MAKE_SCREENSHOT_ID, 0, getResources().getString(R.string.screenshot_label));
        item.setIcon(android.R.drawable.ic_menu_camera);
        item = menu.add(0, MENUITEM_SHARE_DATA_ID, 0, getResources().getString(R.string.sharedata_label));
        item.setIcon(android.R.drawable.ic_menu_share);
        item = menu.add(0, MENUITEM_COPY_TO_CLIPBOARD_ID, 0, getResources().getString(R.string.copytoclipboard_label));
        item.setIcon(R.drawable.copy);
        item = menu.add(0, MENUITEM_OPTIONS_ID, 0, getResources().getString(R.string.options_label));
        item.setIcon(android.R.drawable.ic_menu_preferences);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = this.getApplicationContext();
        View view = null;
        String appName = getString(R.string.app_name);
        switch(item.getItemId()) {
            case MENUITEM_TODAY_ID:
                setDate(Calendar.getInstance());
                return true;
            case MENUITEM_CHOOSE_DATE_ID:
                showDialog(DATE_DIALOG_ID);
                return true;
            case MENUITEM_MAKE_SCREENSHOT_ID:
                view = (ListView) findViewById(R.id.eventListView);
                AlmanacScreenShot.shot(context, view.getRootView(), appName);
                return true;
            case MENUITEM_TEXT_TO_SPEECH_ID:
                Toast.makeText(this, getResources().getString(R.string.notyetimplemented_label), Toast.LENGTH_SHORT).show();
                return true;
            case MENUITEM_COPY_TO_CLIPBOARD_ID:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(CopyToClipboard());
                Toast.makeText(this, getResources().getString(R.string.copiedtoclipboard_label), Toast.LENGTH_SHORT).show();
                return true;
            case MENUITEM_SHARE_DATA_ID:
                this.share(getResources().getString(R.string.app_name), CopyToClipboard());
                return true;
            case MENUITEM_OPTIONS_ID:
                startActivity(new Intent(this, AlmanacPreferences.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
