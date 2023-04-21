package org.jzy3d.chart.controllers.mouse.interactives;

import java.awt.Graphics2D;
import org.jzy3d.chart.controllers.mouse.AbstractChartMouseSelector;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.interactive.InteractiveScatter;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.jzy3d.plot3d.rendering.view.View;

public class ScatterMouseSelector extends AbstractChartMouseSelector {

    public ScatterMouseSelector(InteractiveScatter scatter) {
        this.scatter = scatter;
    }

    /** Make projection and match points belonging to selection. */
    @Override
    protected void processSelection(Scene scene, View view, int width, int height) {
        view.project();
        Coord3d[] projection = scatter.getProjection();
        for (int i = 0; i < projection.length; i++) if (matchRectangleSelection(in, out, projection[i], width, height)) scatter.setHighlighted(i, true);
    }

    @Override
    protected void drawSelection(Graphics2D g2d, int width, int height) {
        this.width = width;
        this.height = height;
        if (dragging) {
            drawRectangle(g2d, in, out);
        }
    }

    protected InteractiveScatter scatter;

    protected int width;

    protected int height;
}
