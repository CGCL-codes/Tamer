package edu.uci.ics.jung.visualization.control;

import java.awt.geom.Point2D;
import edu.uci.ics.jung.visualization.VisualizationServer;

public interface ScalingControl {

    /**
     * zoom the display in or out
     * @param vv the VisualizationViewer
     * @param amount how much to adjust scale by
     * @param at where to adjust scale from
     */
    void scale(VisualizationServer<?, ?> vv, float amount, Point2D at);
}
