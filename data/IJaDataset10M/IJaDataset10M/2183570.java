package com.rapidminer.gui.renderer.models;

import com.rapidminer.datatable.DataTable;
import com.rapidminer.gui.renderer.AbstractDataTableTableRenderer;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.learner.functions.kernel.hyperhyper.HyperModel;

/**
 * 
 * @author Sebastian Land
 */
public class HyperModelWeightsRenderer extends AbstractDataTableTableRenderer {

    @Override
    public DataTable getDataTable(Object renderable, IOContainer ioContainer, boolean isRendering) {
        HyperModel model = (HyperModel) renderable;
        return model.getWeightTable();
    }
}
