package cz.papezzde.talkingplaces;

import java.util.Collection;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import cz.papezzde.talkingplaces.R;
import cz.papezzde.talkingplaces.listener.FilterOnClickListener;
import cz.papezzde.talkingplaces.model.AugmentedApplication;
import cz.papezzde.talkingplaces.model.Environment;
import cz.papezzde.talkingplaces.model.Layer;

/**
 * Displays a list to set up visibilities of categories or layers.
 * 
 * The list can only show layers in a category that is specified in extras under <code>category</code> key.
 * If extra with a <code>layer</code> key is specified, the list displays all siblings of the specified layer. 
 * 
 * @author Zdenek Papez
 *
 */
public class FilterActivity extends ListActivity {

    private Environment env;

    private String catName;

    private String layerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_list);
        getListView().setOnKeyListener(new FilterOnClickListener(null));
        env = ((AugmentedApplication) getApplication()).getEnvironment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Layer[] layersArr = new Layer[1];
        Bundle ex = this.getIntent().getExtras();
        if (ex != null) {
            catName = this.getIntent().getExtras().getString("category");
            layerName = this.getIntent().getExtras().getString("layer");
        }
        if (catName != null) {
            layersArr = env.getAllLayersFromCategory(catName).toArray(layersArr);
        } else if (layerName != null) {
            Layer selectedLayer = env.getLayerByNameID(layerName);
            Collection<Layer> layers = selectedLayer.getParentCategory().getAllLayers();
            for (Layer l : layers) {
                l.setVisible(false);
            }
            selectedLayer.setVisible(true);
            layersArr = layers.toArray(layersArr);
        } else {
            layersArr = env.getAllLayersFromVisibleCategories().toArray(layersArr);
        }
        setListAdapter(new LayerAdapter(this, R.layout.filter_categories_row, layersArr));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class LayerAdapter extends ArrayAdapter<Layer> {

        private Layer[] layers;

        public LayerAdapter(Context context, int resourceId, Layer[] layers) {
            super(context, resourceId, layers);
            this.layers = layers;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.filter_categories_row, null);
            }
            CheckBox cb = (CheckBox) v.findViewById(R.id.filterCategoryCheckbox);
            if (cb != null) {
                cb.setChecked(layers[position].isVisible());
                cb.setText(layers[position].getName());
            }
            FilterOnClickListener fl = new FilterOnClickListener(layers[position]);
            cb.setOnClickListener(fl);
            return v;
        }
    }
}
