package com.iver.cit.gvsig.project.documents.view.legend.gui;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

public class Statistics extends AbstractParentPanel {

    public String getDescription() {
        throw new Error("Not yet implemented!");
    }

    public String getParentTitle() {
        return null;
    }

    public String getTitle() {
        return PluginServices.getText(Statistics.class, "statistics");
    }

    public boolean isSuitableFor(FLayer layer) {
        return layer instanceof FLyrVect;
    }
}
