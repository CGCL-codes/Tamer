package com.vividsolutions.jump.workbench.ui.renderer;

import java.awt.Color;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

public class PartSelectionRenderer extends AbstractSelectionRenderer {

    public static final String CONTENT_ID = "SELECTED_PARTS";

    public PartSelectionRenderer(LayerViewPanel panel) {
        super(CONTENT_ID, panel, Color.red, true, false);
    }

    protected CollectionMap featureToSelectedItemsMap(Layer layer) {
        return panel.getSelectionManager().getPartSelection().getFeatureToSelectedItemCollectionMap(layer);
    }
}
