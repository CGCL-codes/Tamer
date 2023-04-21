package cz.papezzde.talkingplaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cz.papezzde.talkingplaces.R;
import cz.papezzde.talkingplaces.collection.DistanceComparator;
import cz.papezzde.talkingplaces.collection.WalkingTurnsComparator;
import cz.papezzde.talkingplaces.location.CompassSensorListenerForList;
import cz.papezzde.talkingplaces.model.AugmentedApplication;
import cz.papezzde.talkingplaces.model.Environment;
import cz.papezzde.talkingplaces.model.ExtendedOverlayItem;
import cz.papezzde.talkingplaces.thread.ListLoaderTask;
import cz.papezzde.talkingplaces.thread.PreFilterCalculation;
import cz.papezzde.talkingplaces.view.DetailDialogBuilder;
import cz.papezzde.talkingplaces.widget.PlacemarkArrayAdapter;

/**
 * Lists places from visible categories and layers.
 * The category or layer can be specified in extras under <code>category</code> or <code>layer</code> keys.
 * 
 * In options menu the user can open the <code>FilterActivity</code> to change the visibilities and filter the places.
 * 
 * The list is sorted by the air distance of the place by default.
 * Additionally it can be sorted by walking distance that is being read on demand or it can be sorted by the number of turns on the walking route. 
 * 
 * @author Zdenek Papez
 *
 */
public class ListPlacesActivity extends ListActivity {

    private static final int PROGRESS_DIALOG = -1;

    /**
	 * Number of places for which the walking distance is read on demand.
	 */
    public static int LOAD_BUFFER_SIZE;

    private SensorManager sensorManager;

    private ArrayList<CompassSensorListenerForList> sensorListeners = new ArrayList<CompassSensorListenerForList>();

    private List<Integer> usedDialogs = new ArrayList<Integer>();

    private Environment environment;

    private ArrayList<ExtendedOverlayItem> items;

    private String categoryName;

    private String layerName;

    private int mode;

    private boolean init;

    private static ProgressDialog progressDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_places);
        environment = ((AugmentedApplication) getApplication()).getEnvironment();
        LOAD_BUFFER_SIZE = getResources().getInteger(R.integer.list_walking_buffer);
        handleIntent(getIntent());
    }

    /**
     * Reads the places visibilities and filters, sorts and displays them. 
     */
    @Override
    protected void onResume() {
        super.onResume();
        boolean voiceCommand = false;
        Bundle ex = this.getIntent().getExtras();
        if (ex != null) {
            categoryName = ex.getString("category");
            layerName = ex.getString("layer");
            voiceCommand = ex.getBoolean("voiceCommand", false);
            mode = ex.getInt("mode", 0);
        }
        if (categoryName != null) {
            items = environment.getAllVisibleOverlayItemsFromCategory(categoryName);
        } else if (layerName != null) {
            if (voiceCommand) {
                items = environment.getOverlayItemsFromLayer(layerName);
                this.getIntent().putExtra("voiceCommand", false);
            } else {
                categoryName = environment.getLayerByNameID(layerName).getParentCategory().getNameID();
                items = environment.getAllVisibleOverlayItemsFromCategory(categoryName);
            }
        } else if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
        } else {
            items = environment.getAllVisibleOverlayItems();
        }
        Collections.sort(items);
        init = false;
        ExtendedOverlayItem[] arr = new ExtendedOverlayItem[items.size()];
        showDialog(PROGRESS_DIALOG);
        new PreFilterCalculation(this, new ListLoadingHandler(), mode).execute(items.toArray(arr));
        ListView lv = getListView();
        PlacemarkArrayAdapter adapter = new PlacemarkArrayAdapter(this, R.layout.list_places_row, items);
        setListAdapter(adapter);
        lv.setOnScrollListener(adapter);
    }

    /**
     * Finishes the filtering and initializes the list of items
     * - adds on click listeners for items
     */
    public void initItems() {
        if (!init) {
            init = true;
            ListView lv = getListView();
            PlacemarkArrayAdapter adapter = (PlacemarkArrayAdapter) getListAdapter();
            items = environment.filterItems(items);
            adapter.setItemsAvailable(items.size());
            lv.clearFocus();
            TextView tv = (TextView) findViewById(R.id.textListHeading);
            tv.setText(getText(R.string.list_of_places_heading_part1) + " " + items.size() + " " + getText(R.string.list_of_places_heading_part2));
            tv.requestFocus();
            new ListLoaderTask(ListPlacesActivity.this).execute(adapter);
            adapter.notifyDataSetChanged();
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= ((PlacemarkArrayAdapter) parent.getAdapter()).getCurrentSize()) {
                        new ListLoaderTask(ListPlacesActivity.this).execute((PlacemarkArrayAdapter) getListAdapter());
                    } else {
                        showDialog(position);
                        usedDialogs.add(position);
                    }
                }
            });
            getListView().invalidateViews();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (Integer i : usedDialogs) {
            removeDialog(i);
        }
        usedDialogs.clear();
        for (CompassSensorListenerForList l : sensorListeners) {
            sensorManager.unregisterListener(l);
        }
        sensorListeners.clear();
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        Dialog dialog = null;
        if (id == PROGRESS_DIALOG) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.progress_dialog_header));
            progressDialog.setMessage(getString(R.string.progress_dialog_description));
            progressDialog.setCancelable(true);
            return progressDialog;
        }
        if (items != null) {
            ExtendedOverlayItem i = items.get(id);
            dialog = DetailDialogBuilder.getDetailDialog(ListPlacesActivity.this, environment.getMyLocation(), i).create();
        }
        return dialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<ExtendedOverlayItem> list;
        switch(item.getItemId()) {
            case R.id.filterMenuItem:
                Intent intent = new Intent(this, FilterActivity.class);
                if (categoryName != null) {
                    intent.putExtra("category", categoryName);
                } else {
                    intent.putExtra("layer", layerName);
                }
                startActivity(intent);
                break;
            case R.id.showMapMenuItem:
                Intent mapIntent = new Intent(this, MapViewActivity.class);
                if (categoryName != null) {
                    mapIntent.putExtra("category", categoryName);
                }
                startActivity(mapIntent);
                break;
            case R.id.sortByWalkingDistanceMenuItem:
                list = ((PlacemarkArrayAdapter) getListAdapter()).getCurrentItems();
                Collections.sort(list, new DistanceComparator());
                ((PlacemarkArrayAdapter) getListAdapter()).notifyDataSetChanged();
                break;
            case R.id.sortByWalkingTurnsMenuItem:
                list = ((PlacemarkArrayAdapter) getListAdapter()).getCurrentItems();
                Collections.sort(list, new WalkingTurnsComparator());
                ((PlacemarkArrayAdapter) getListAdapter()).notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            items = environment.searchItems(query);
        }
    }

    private class ListLoadingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            findViewById(R.id.textListHeading).requestFocus();
        }
    }

    /**
	 * Adds and registers the listener for compass updates. 
	 * @param compassSensorListener listener to be added
	 */
    public void addSensorListener(CompassSensorListenerForList compassSensorListener) {
        if (!sensorListeners.contains(compassSensorListener)) {
            sensorListeners.add(compassSensorListener);
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            sensorManager.registerListener(compassSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * Unregisters and removes the listener 
     * @param oldListener listener to be removed
     */
    public void removeSensorListener(CompassSensorListenerForList oldListener) {
        sensorManager.unregisterListener(oldListener);
        sensorListeners.remove(oldListener);
    }

    /**
     * Getter for the current environment
     * @return environment instance
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
	 *  Getter for all displayed items
	 * @return items displayed
	 */
    public ArrayList<ExtendedOverlayItem> getItems() {
        return items;
    }
}
