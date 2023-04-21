package mpr.openGPX;

import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.opengpx.Preferences;
import mpr.openGPX.lib.AdvancedSearchData;
import mpr.openGPX.lib.CacheDatabase;
import mpr.openGPX.lib.CoordinateFormat;
import mpr.openGPX.lib.Coordinates;
import mpr.openGPX.lib.AdvancedSearchData.CacheType;
import mpr.openGPX.lib.AdvancedSearchData.ContainerSize;
import mpr.openGPX.lib.AdvancedSearchData.EnumInterface;
import mpr.openGPX.lib.AdvancedSearchData.RadioOptions;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AdvancedSearchActivity extends Activity {

    private CacheDatabase cacheDatabase;

    private SearchBCaching searchBCaching;

    private ProgressDialog progressDialog;

    private Preferences mPreferences;

    private GpsLocationListener locationListener = null;

    private ScheduledExecutorService scheduler = null;

    private static final String titleBase = "OpenGPX - Advanced Search";

    private AdvancedSearchData data = new AdvancedSearchData();

    private Hashtable<EnumInterface, CheckBox> containerSizeBoxes = new Hashtable<EnumInterface, CheckBox>();

    private Hashtable<EnumInterface, CheckBox> cacheTypeBoxes = new Hashtable<EnumInterface, CheckBox>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheDatabase = CacheDatabase.getInstance();
        setTitle(titleBase);
        setContentView(R.layout.advancedsearch);
        locationListener = new GpsLocationListener((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        mPreferences = new Preferences(this);
        Spinner s = (Spinner) findViewById(R.id.AdvSchDiffToSpinner);
        setSpinnerValue(s, "5");
        s = (Spinner) findViewById(R.id.AdvSchTerrToSpinner);
        setSpinnerValue(s, "5");
        getSizeBoxes();
        getTypeBoxes();
        containerSizeBoxes.get(ContainerSize.ALL).setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAllChecks(containerSizeBoxes);
            }
        });
        cacheTypeBoxes.get(CacheType.ALL).setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAllChecks(cacheTypeBoxes);
            }
        });
        containerSizeBoxes.get(ContainerSize.NONE).setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                clearAllChecks(containerSizeBoxes);
            }
        });
        cacheTypeBoxes.get(CacheType.NONE).setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                clearAllChecks(cacheTypeBoxes);
            }
        });
        Button b = (Button) findViewById(R.id.AdvSchExecuteButton);
        b.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                getSelectedOptions();
                cacheDatabase.setPriorSearch(data);
                searchBCaching(null, data);
            }
        });
        b = (Button) findViewById(R.id.AdvSchClearButton);
        b.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                clearSearchData();
            }
        });
    }

    protected void setSpinnerValue(Spinner s, String value) {
        SpinnerAdapter a = s.getAdapter();
        for (int i = 0; i < a.getCount(); i++) {
            if (a.getItem(i).toString().equals(value)) {
                s.setSelection(i);
            }
        }
    }

    protected void startUpdater() {
        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(1);
            Runnable accuracyUpdater = new Runnable() {

                public void run() {
                    runOnUiThread(new Runnable() {

                        public void run() {
                            Location loc = locationListener.getLastKnownLocation();
                            if (loc != null) {
                                setTitle(String.format(titleBase + "     Accuracy: %.0fM", loc.getAccuracy()));
                            }
                        }
                    });
                }
            };
            scheduler.scheduleWithFixedDelay(accuracyUpdater, 0, 5, TimeUnit.SECONDS);
        }
    }

    protected void stopUpdater() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        locationListener.disableListener();
        stopUpdater();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationListener.enableListener();
        startUpdater();
        if (cacheDatabase.getPriorSearch() != null) displayPriorSearch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationListener.disableListener();
        stopUpdater();
    }

    private void getSizeBoxes() {
        containerSizeBoxes.put(ContainerSize.ALL, (CheckBox) findViewById(R.id.AdvSchSizeAll));
        containerSizeBoxes.put(ContainerSize.NONE, (CheckBox) findViewById(R.id.AdvSchSizeNone));
        containerSizeBoxes.put(ContainerSize.NOT_CHOSEN, (CheckBox) findViewById(R.id.AdvSchSizeNotChosen));
        containerSizeBoxes.put(ContainerSize.LARGE, (CheckBox) findViewById(R.id.AdvSchSizeLarge));
        containerSizeBoxes.put(ContainerSize.REGULAR, (CheckBox) findViewById(R.id.AdvSchSizeRegular));
        containerSizeBoxes.put(ContainerSize.SMALL, (CheckBox) findViewById(R.id.AdvSchSizeSmall));
        containerSizeBoxes.put(ContainerSize.MICRO, (CheckBox) findViewById(R.id.AdvSchSizeMicro));
        containerSizeBoxes.put(ContainerSize.OTHER, (CheckBox) findViewById(R.id.AdvSchSizeOther));
        containerSizeBoxes.put(ContainerSize.VIRTUAL, (CheckBox) findViewById(R.id.AdvSchSizeVirtual));
    }

    private void getTypeBoxes() {
        cacheTypeBoxes.put(CacheType.ALL, (CheckBox) findViewById(R.id.AdvSchTypeAll));
        cacheTypeBoxes.put(CacheType.NONE, (CheckBox) findViewById(R.id.AdvSchTypeNone));
        cacheTypeBoxes.put(CacheType.APE, (CheckBox) findViewById(R.id.AdvSchTypeApe));
        cacheTypeBoxes.put(CacheType.CITO, (CheckBox) findViewById(R.id.AdvSchTypeCito));
        cacheTypeBoxes.put(CacheType.EARTHCACHE, (CheckBox) findViewById(R.id.AdvSchTypeEarth));
        cacheTypeBoxes.put(CacheType.EVENT, (CheckBox) findViewById(R.id.AdvSchTypeEvent));
        cacheTypeBoxes.put(CacheType.GPS_ADVENTURES, (CheckBox) findViewById(R.id.AdvSchTypeGpsame));
        cacheTypeBoxes.put(CacheType.LETTERBOX, (CheckBox) findViewById(R.id.AdvSchTypeLetterbox));
        cacheTypeBoxes.put(CacheType.LOCATIONLESS, (CheckBox) findViewById(R.id.AdvSchTypeLocationless));
        cacheTypeBoxes.put(CacheType.MEGA_EVENT, (CheckBox) findViewById(R.id.AdvSchTypeMegaevent));
        cacheTypeBoxes.put(CacheType.MULTI, (CheckBox) findViewById(R.id.AdvSchTypeMulti));
        cacheTypeBoxes.put(CacheType.TRADITIONAL, (CheckBox) findViewById(R.id.AdvSchTypeTraditional));
        cacheTypeBoxes.put(CacheType.UNKNOWN, (CheckBox) findViewById(R.id.AdvSchTypeUnknown));
        cacheTypeBoxes.put(CacheType.VIRTUAL, (CheckBox) findViewById(R.id.AdvSchTypeVirtual));
        cacheTypeBoxes.put(CacheType.WEBCAM, (CheckBox) findViewById(R.id.AdvSchTypeWebcam));
        cacheTypeBoxes.put(CacheType.WHEREIGO, (CheckBox) findViewById(R.id.AdvSchTypeWhereigo));
    }

    private void setAllChecks(Hashtable<EnumInterface, CheckBox> checks) {
        for (Object key : checks.keySet()) {
            String keyStr = "";
            if (key instanceof ContainerSize) {
                ContainerSize c = (ContainerSize) key;
                keyStr = c.getTypeCode();
            } else if (key instanceof CacheType) {
                CacheType c = (CacheType) key;
                keyStr = c.getTypeCode();
            }
            CheckBox box = checks.get(key);
            if (!keyStr.equalsIgnoreCase("All") && !keyStr.equalsIgnoreCase("None")) {
                box.setChecked(true);
            } else {
                box.setChecked(false);
            }
        }
    }

    private void clearAllChecks(Hashtable<EnumInterface, CheckBox> checks) {
        for (CheckBox box : checks.values()) {
            box.setChecked(false);
        }
    }

    private RadioOptions getSelectedRadio(RadioGroup rg) {
        RadioButton b = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
        if (b.getText().toString().equalsIgnoreCase("YES")) return RadioOptions.YES; else if (b.getText().toString().equalsIgnoreCase("NO")) return RadioOptions.NO; else return RadioOptions.EITHER;
    }

    private void setSelectedRadio(RadioGroup rg, RadioOptions opts) {
        RadioButton yes = null;
        RadioButton no = null;
        RadioButton either = null;
        for (int i = 0; i < 3; i++) {
            RadioButton tmp = (RadioButton) rg.getChildAt(i);
            if (tmp.getText().toString().equalsIgnoreCase("YES")) yes = tmp; else if (tmp.getText().toString().equalsIgnoreCase("NO")) no = tmp; else either = tmp;
        }
        if (opts == RadioOptions.YES) yes.setChecked(true); else if (opts == RadioOptions.NO) no.setChecked(true); else either.setChecked(true);
    }

    private void getSelectedOptions() {
        Spinner s;
        RadioGroup rg;
        EditText et;
        s = (Spinner) findViewById(R.id.AdvSchDiffFromSpinner);
        data.difficultyFrom = (String) s.getSelectedItem();
        s = (Spinner) findViewById(R.id.AdvSchDiffToSpinner);
        data.difficultyTo = (String) s.getSelectedItem();
        s = (Spinner) findViewById(R.id.AdvSchTerrFromSpinner);
        data.terrainFrom = (String) s.getSelectedItem();
        s = (Spinner) findViewById(R.id.AdvSchTerrToSpinner);
        data.terrainTo = (String) s.getSelectedItem();
        et = (EditText) findViewById(R.id.AdvSchDistanceFromOrigin);
        data.maxDistance = Float.parseFloat(et.getText().toString());
        rg = (RadioGroup) findViewById(R.id.AdvSchAltCoordsRadioGroup);
        data.hasAltCoords = getSelectedRadio(rg);
        rg = (RadioGroup) findViewById(R.id.AdvSchWptsRadioGroup);
        data.hasWpts = getSelectedRadio(rg);
        rg = (RadioGroup) findViewById(R.id.AdvSchActiveRadioGroup);
        data.isActive = getSelectedRadio(rg);
        rg = (RadioGroup) findViewById(R.id.AdvSchFoundRadioGroup);
        data.isFound = getSelectedRadio(rg);
        rg = (RadioGroup) findViewById(R.id.AdvSchIgnoredRadioGroup);
        data.isIgnored = getSelectedRadio(rg);
        rg = (RadioGroup) findViewById(R.id.AdvSchOwnedRadioGroup);
        data.isOwned = getSelectedRadio(rg);
        rg = (RadioGroup) findViewById(R.id.AdvSchHasTBRadioGroup);
        data.hasTBs = getSelectedRadio(rg);
        for (EnumInterface key : cacheTypeBoxes.keySet()) {
            CheckBox box = cacheTypeBoxes.get(key);
            data.cacheTypeBoxes.put(key, box.isChecked());
        }
        for (EnumInterface key : containerSizeBoxes.keySet()) {
            CheckBox box = containerSizeBoxes.get(key);
            data.containerSizeBoxes.put(key, box.isChecked());
        }
    }

    private void displayPriorSearch() {
        AdvancedSearchData priorSearch = cacheDatabase.getPriorSearch();
        showSavedSearchData(priorSearch);
    }

    private int findStringInArray(String[] list, String str) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    private void showSavedSearchData(AdvancedSearchData searchData) {
        Spinner s;
        RadioGroup rg;
        EditText et;
        String[] diffTerrList = getResources().getStringArray(R.array.diff_terr_list);
        s = (Spinner) findViewById(R.id.AdvSchDiffFromSpinner);
        s.setSelection(findStringInArray(diffTerrList, searchData.difficultyFrom));
        s = (Spinner) findViewById(R.id.AdvSchDiffToSpinner);
        s.setSelection(findStringInArray(diffTerrList, searchData.difficultyTo));
        s = (Spinner) findViewById(R.id.AdvSchTerrFromSpinner);
        s.setSelection(findStringInArray(diffTerrList, searchData.terrainFrom));
        s = (Spinner) findViewById(R.id.AdvSchTerrToSpinner);
        s.setSelection(findStringInArray(diffTerrList, searchData.terrainTo));
        et = (EditText) findViewById(R.id.AdvSchDistanceFromOrigin);
        et.setText(Float.toString(searchData.maxDistance));
        rg = (RadioGroup) findViewById(R.id.AdvSchAltCoordsRadioGroup);
        setSelectedRadio(rg, searchData.hasAltCoords);
        rg = (RadioGroup) findViewById(R.id.AdvSchWptsRadioGroup);
        setSelectedRadio(rg, searchData.hasAltCoords);
        rg = (RadioGroup) findViewById(R.id.AdvSchActiveRadioGroup);
        setSelectedRadio(rg, searchData.isActive);
        rg = (RadioGroup) findViewById(R.id.AdvSchFoundRadioGroup);
        setSelectedRadio(rg, searchData.isFound);
        rg = (RadioGroup) findViewById(R.id.AdvSchIgnoredRadioGroup);
        setSelectedRadio(rg, searchData.isIgnored);
        rg = (RadioGroup) findViewById(R.id.AdvSchOwnedRadioGroup);
        setSelectedRadio(rg, searchData.isOwned);
        rg = (RadioGroup) findViewById(R.id.AdvSchHasTBRadioGroup);
        setSelectedRadio(rg, searchData.hasTBs);
        for (EnumInterface key : searchData.cacheTypeBoxes.keySet()) {
            Boolean checked = searchData.cacheTypeBoxes.get(key);
            CheckBox box = cacheTypeBoxes.get(key);
            box.setChecked(checked);
        }
        for (EnumInterface key : searchData.containerSizeBoxes.keySet()) {
            CheckBox box = containerSizeBoxes.get(key);
            Boolean checked = searchData.containerSizeBoxes.get(key);
            box.setChecked(checked);
        }
    }

    private void clearSearchData() {
        Spinner s;
        RadioGroup rg;
        EditText et;
        String[] diffTerrList = getResources().getStringArray(R.array.diff_terr_list);
        s = (Spinner) findViewById(R.id.AdvSchDiffFromSpinner);
        s.setSelection(findStringInArray(diffTerrList, "1"));
        s = (Spinner) findViewById(R.id.AdvSchDiffToSpinner);
        s.setSelection(findStringInArray(diffTerrList, "1"));
        s = (Spinner) findViewById(R.id.AdvSchTerrFromSpinner);
        s.setSelection(findStringInArray(diffTerrList, "1"));
        s = (Spinner) findViewById(R.id.AdvSchTerrToSpinner);
        s.setSelection(findStringInArray(diffTerrList, "1"));
        et = (EditText) findViewById(R.id.AdvSchDistanceFromOrigin);
        et.setText(Float.toString(30));
        rg = (RadioGroup) findViewById(R.id.AdvSchAltCoordsRadioGroup);
        setSelectedRadio(rg, RadioOptions.EITHER);
        rg = (RadioGroup) findViewById(R.id.AdvSchWptsRadioGroup);
        setSelectedRadio(rg, RadioOptions.EITHER);
        rg = (RadioGroup) findViewById(R.id.AdvSchActiveRadioGroup);
        setSelectedRadio(rg, RadioOptions.EITHER);
        rg = (RadioGroup) findViewById(R.id.AdvSchFoundRadioGroup);
        setSelectedRadio(rg, RadioOptions.EITHER);
        rg = (RadioGroup) findViewById(R.id.AdvSchIgnoredRadioGroup);
        setSelectedRadio(rg, RadioOptions.EITHER);
        rg = (RadioGroup) findViewById(R.id.AdvSchOwnedRadioGroup);
        setSelectedRadio(rg, RadioOptions.EITHER);
        rg = (RadioGroup) findViewById(R.id.AdvSchHasTBRadioGroup);
        setSelectedRadio(rg, RadioOptions.EITHER);
        clearAllChecks(cacheTypeBoxes);
        clearAllChecks(containerSizeBoxes);
    }

    private void showSearchingDialog() {
        progressDialog = ProgressDialog.show(this, "Running Online Search", "Please wait - this may take a while ...", true, false);
    }

    private void searchBCaching(final String name, final AdvancedSearchData data) {
        final String bcUsername = this.mPreferences.getBCachingUsername();
        final String bcPassword = this.mPreferences.getBCachingPassword();
        final int bcMaxCaches = this.mPreferences.getBCachingMaxCaches();
        final int bcMaxDistance = this.mPreferences.getBCachingMaxDistance();
        final boolean bcTestSite = this.mPreferences.getUseBCachingTestSite();
        final LocationInfo locationInfo = this.getLastKnownLocation();
        Coordinates coordinates = new Coordinates(locationInfo.latitude, locationInfo.longitude);
        CoordinateFormat coordinateFormat = this.mPreferences.getCoordinateFormat();
        String strMessage = String.format("%s [%s]", coordinates.toString(coordinateFormat), locationInfo.provider);
        Toast.makeText(this, strMessage, Toast.LENGTH_LONG).show();
        try {
            showSearchingDialog();
            Thread t = new Thread(new Runnable() {

                public void run() {
                    try {
                        searchBCaching = new SearchBCaching(bcTestSite);
                        searchBCaching.setLoginInfo(bcUsername, bcPassword);
                        searchBCaching.doFindQuery(locationInfo, bcMaxDistance, bcMaxCaches, name, data);
                        runOnUiThread(new Runnable() {

                            public void run() {
                                dismissProgressDialog();
                                cacheDatabase.isSearchUpdated.set(true);
                                finish();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        dismissProgressDialog();
                        showBCachingErrorInUIThread();
                    }
                }
            });
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocationInfo getLastKnownLocation() {
        LocationInfo locationInfo = new LocationInfo();
        Location locLastKnown = locationListener.getLastKnownLocation();
        if (locLastKnown != null) {
            if (locLastKnown.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                locationInfo.provider = "GPS";
            } else if (locLastKnown.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                locationInfo.provider = "Network";
            }
            locationInfo.latitude = locLastKnown.getLatitude();
            locationInfo.longitude = locLastKnown.getLongitude();
        } else {
            Coordinates homeCoordinates = this.mPreferences.getHomeCoordinates();
            locationInfo.latitude = homeCoordinates.getLatitude().getD();
            locationInfo.longitude = homeCoordinates.getLongitude().getD();
            locationInfo.provider = "Home";
        }
        return locationInfo;
    }

    private void dismissProgressDialog() {
        try {
            runOnUiThread(new Runnable() {

                public void run() {
                    if (progressDialog != null) progressDialog.dismiss();
                }
            });
        } catch (IllegalArgumentException iae) {
        }
    }

    private void showBCachingErrorInUIThread() {
        runOnUiThread(new Runnable() {

            public void run() {
                showBCachingError();
            }
        });
    }

    private void showBCachingError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("BCaching.com query failed.\n\nPlease check your login information.\n\nIf the error persists please email a log to the developers.");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alertDialog.show();
    }
}
