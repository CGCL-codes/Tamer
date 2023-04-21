package com.rapidminer.gui;

import com.rapidminer.ObjectVisualizer;
import com.rapidminer.gui.tools.SwingTools;

/**
 * A dummy visualizer, capable of visualizing anything, but actually doing
 * nothing.
 * 
 * @author Michael Wurst, Ingo Mierswa
 */
public class DummyObjectVisualizer implements ObjectVisualizer {

    public void startVisualization(Object objId) {
        SwingTools.showVerySimpleErrorMessage("no_visual_for_obj", objId);
    }

    public void stopVisualization(Object objId) {
    }

    public String getTitle(Object objId) {
        return objId.toString();
    }

    public boolean isCapableToVisualize(Object id) {
        return true;
    }

    public String getDetailData(Object id, String fieldName) {
        return null;
    }

    public String[] getFieldNames(Object id) {
        return new String[0];
    }
}
