package net.sourceforge.atides;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import net.sourceforge.atides.DataSets.SelectedDataset;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MainActivity extends Activity {

    public static final String SELECTEDDATE = "selectedDate";

    public static final String STATIONLIST = "stationList";

    public static final String STATIONNAME = "stationName";

    public static final String CONSTITUENTS = "constituents";

    public static final String STATIONINDEX = "stationIndex";

    public static final String SELECTEDDATASET = "selectedDataset";

    public static final String SELECTEDDATASET_POS = "selectedDatasetPos";

    public static final String PREFS = "preferences";

    public static final String IMPERIAL = "useImperial";

    public static final String SELECTEDFAVOURITE = "selectedFavourite";

    protected static final int HELP = 0;

    protected static final int DATASET = 1;

    protected static final int FAVOURITES = 2;

    protected static final int ABOUT = 3;

    protected static final int NONFREE = 5;

    protected static final int SEARCH = 6;

    protected static final int DISABLE = 0;

    protected static final int ENABLE = 1;

    protected static final int SETSTATIONS = 2;

    protected static final int SETSELECTED = 3;

    protected static final int NODATA = 4;

    protected static final int CHOOSEREGION = 0;

    private Intent graphIntent;

    private ProgressDialog progressDialog;

    private Constituents constituents;

    public static StationList stationList;

    private SelectedDataset selectedDataset, currentDataset;

    private Intent searchIntent;

    private Handler loadHandler, uiHandler;

    private Intent chooseRegionIntent;

    private int lastStation = -1;

    private int selectedDatasetPos = -1;

    private boolean useImperial = false;

    private Spinner spnrStations;

    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFS, MODE_PRIVATE);
        DataBaseHelper dbHelper = DataBaseHelper.getInstance(this);
        try {
            dbHelper.createDataBase();
            dbHelper.close();
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to create database");
        }
        setContentView(R.layout.main);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_BOTTOM);
        AdView adView = new AdView(this, AdSize.BANNER, "xxxxxxxxxxxxxxx");
        ((RelativeLayout) findViewById(R.id.rootLayout)).addView(adView, lp);
        adView.loadAd(new AdRequest());
        searchIntent = new Intent(this, SearchActivity.class);
        chooseRegionIntent = new Intent(this, ChooseRegionActivity.class);
        final Button btnDrawChart = (Button) this.findViewById(R.id.btnDrawChart);
        spnrStations = (Spinner) this.findViewById(R.id.spnrStation);
        final DatePicker datePicker = (DatePicker) this.findViewById(R.id.datePicker);
        final RadioButton rdoImperial = (RadioButton) this.findViewById(R.id.radioBtnImperial);
        final RadioButton rdoMetric = (RadioButton) this.findViewById(R.id.radioBtnMetric);
        graphIntent = new Intent(this, GraphActivity.class);
        uiHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == DISABLE) {
                    enableUI(false);
                } else if (msg.what == ENABLE) {
                    enableUI(true);
                } else if (msg.what == SETSTATIONS) {
                    if (stationList == null) {
                        this.sendEmptyMessage(NODATA);
                    } else {
                        ArrayList<StationDescription> stations = stationList.getStations();
                        ArrayAdapter<StationDescription> adapter = new ArrayAdapter<StationDescription>(MainActivity.this, android.R.layout.simple_spinner_item, stations);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        adapter.setNotifyOnChange(true);
                        spnrStations.setAdapter(adapter);
                        if (lastStation > -1) {
                            spnrStations.setSelection(lastStation);
                        }
                        if (selectedDataset != null) {
                            setTitle(MainActivity.this.getString(R.string.app_name) + " - " + selectedDataset.toString());
                        }
                        rdoImperial.setChecked(useImperial);
                        rdoMetric.setChecked(!useImperial);
                        enableUI(true);
                    }
                } else if (msg.what == NODATA) {
                    enableUI(false);
                    Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.nodataoncard_text), Toast.LENGTH_LONG).show();
                    startActivityForResult(chooseRegionIntent, CHOOSEREGION);
                }
            }

            private void enableUI(boolean enabled) {
                spnrStations.setEnabled(enabled);
                datePicker.setEnabled(enabled);
                btnDrawChart.setEnabled(enabled);
                rdoImperial.setEnabled(enabled);
                rdoMetric.setEnabled(enabled);
            }
        };
        loadHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (selectedDataset == null || selectedDataset == currentDataset) {
                    return;
                }
                currentDataset = selectedDataset;
                progressDialog = ProgressDialog.show(MainActivity.this, MainActivity.this.getString(R.string.wait_text), MainActivity.this.getString(R.string.loading_text), true);
                Thread loadThread = new Thread() {

                    @Override
                    public void run() {
                        DataInputStream dataStream = null;
                        DataInputStream stationsStream = null;
                        if (selectedDataset == SelectedDataset.SDCARD) {
                            try {
                                dataStream = new DataInputStream(new FileInputStream("/sdcard/data.dat"));
                                stationsStream = new DataInputStream(new FileInputStream("/sdcard/stations.dat"));
                            } catch (Exception nofiles) {
                                progressDialog.dismiss();
                                uiHandler.sendEmptyMessage(NODATA);
                                return;
                            }
                        } else {
                            dataStream = new DataInputStream(MainActivity.this.getResources().openRawResource(DataSets.getResource(selectedDataset, DataSets.DataType.DATA)));
                            stationsStream = new DataInputStream(MainActivity.this.getResources().openRawResource(DataSets.getResource(selectedDataset, DataSets.DataType.STATION)));
                        }
                        try {
                            constituents = new Constituents(dataStream);
                            stationList = new StationList(constituents, stationsStream);
                        } catch (IOException fatal) {
                            Log.e("aTide Error:", fatal.getLocalizedMessage());
                            System.exit(1);
                        }
                        uiHandler.sendEmptyMessage(SETSTATIONS);
                        progressDialog.dismiss();
                    }
                };
                loadThread.start();
            }
        };
        btnDrawChart.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if (stationList == null) {
                    Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.nodataoncard_text), Toast.LENGTH_LONG).show();
                } else {
                    progressDialog = ProgressDialog.show(MainActivity.this, MainActivity.this.getString(R.string.wait_text), MainActivity.this.getString(R.string.chart_text), true);
                    Thread passDataThread = new Thread() {

                        @Override
                        public void run() {
                            lastStation = spnrStations.getSelectedItemPosition();
                            useImperial = rdoImperial.isChecked();
                            Calendar cal = Calendar.getInstance();
                            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                            graphIntent.putExtra(SELECTEDDATE, cal.getTimeInMillis());
                            graphIntent.putExtra(STATIONINDEX, lastStation);
                            graphIntent.putExtra(IMPERIAL, useImperial);
                            startActivity(graphIntent);
                            progressDialog.dismiss();
                        }
                    };
                    passDataThread.start();
                }
            }
        });
        if (savedInstanceState == null) {
            selectedDatasetPos = settings.getInt(SELECTEDDATASET_POS, -1);
            if (selectedDatasetPos > -1) {
                selectedDataset = DataSets.getDataSets(this).get(selectedDatasetPos);
                lastStation = settings.getInt(STATIONINDEX, -1);
                useImperial = settings.getBoolean(IMPERIAL, false);
                loadHandler.sendEmptyMessage(0);
                Toast.makeText(this, this.getString(R.string.nag_message_part1), Toast.LENGTH_LONG).show();
                Toast.makeText(this, this.getString(R.string.nag_message_part2), Toast.LENGTH_LONG).show();
            } else {
                new AboutDialog(this).show();
                startActivityForResult(chooseRegionIntent, CHOOSEREGION);
            }
        } else {
            if (savedInstanceState.containsKey(SELECTEDDATASET)) {
                selectedDataset = (SelectedDataset) savedInstanceState.getSerializable(SELECTEDDATASET);
                stationList = (StationList) savedInstanceState.getSerializable(STATIONLIST);
                lastStation = savedInstanceState.getInt(STATIONINDEX);
                graphIntent.putExtra(STATIONLIST, stationList);
                graphIntent.putExtra(IMPERIAL, useImperial);
                uiHandler.sendEmptyMessage(SETSTATIONS);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, SEARCH, 0, this.getString(R.string.search_menu)).setIcon(android.R.drawable.ic_menu_search);
        menu.add(Menu.NONE, DATASET, 1, this.getString(R.string.region_title)).setIcon(android.R.drawable.ic_menu_mapmode);
        menu.add(Menu.NONE, HELP, 3, this.getString(R.string.help_menu)).setIcon(android.R.drawable.ic_menu_help);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DATASET:
                startActivityForResult(chooseRegionIntent, CHOOSEREGION);
                return true;
            case SEARCH:
                startActivityForResult(searchIntent, SEARCH);
                return true;
            case HELP:
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getExtras() != null) {
            switch(requestCode) {
                case CHOOSEREGION:
                    selectedDataset = (SelectedDataset) data.getExtras().get(SELECTEDDATASET);
                    selectedDatasetPos = data.getExtras().getInt(SELECTEDDATASET_POS);
                    lastStation = -1;
                    loadHandler.sendEmptyMessage(0);
                    break;
                case SEARCH:
                    String stationName = (String) data.getExtras().get(STATIONNAME);
                    for (StationDescription stationDesc : stationList.getStations()) {
                        if (stationDesc.toString().equals(stationName)) {
                            lastStation = stationList.getStations().indexOf(stationDesc);
                            break;
                        }
                    }
                    uiHandler.sendEmptyMessage(SETSTATIONS);
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            lastStation = spnrStations.getSelectedItemPosition();
        } catch (Exception ignore) {
        }
        outState.putSerializable(SELECTEDDATASET, selectedDataset);
        outState.putSerializable(STATIONLIST, stationList);
        outState.putInt(STATIONINDEX, lastStation);
        outState.putBoolean(IMPERIAL, useImperial);
        super.onSaveInstanceState(outState);
    }

    /**
	 * saves last settings on exit
	 * */
    @Override
    protected void onPause() {
        try {
            lastStation = spnrStations.getSelectedItemPosition();
        } catch (Exception ignore) {
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(STATIONINDEX, lastStation);
        editor.putInt(SELECTEDDATASET_POS, selectedDatasetPos);
        editor.putBoolean(IMPERIAL, useImperial);
        editor.commit();
        super.onPause();
    }
}
