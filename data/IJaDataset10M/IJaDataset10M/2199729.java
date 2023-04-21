package org.jzy3d.demos.histogram.barchart;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.AbstractChartMouseSelector;
import org.jzy3d.maths.BoundingBox2d;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.jzy3d.plot3d.rendering.view.View;

/**
 *
 * @author ao
 */
public class LabeledMouseSelector extends AbstractChartMouseSelector implements KeyListener {

    private final Chart target;

    public LabeledMouseSelector(Chart target) {
        this.target = target;
    }

    @Override
    protected void processSelection(Scene scene, View view, int width, int height) {
        view.project();
        BarChartBar bestMatch = null;
        for (AbstractDrawable ad : scene.getGraph().getAll()) {
            if (!(ad instanceof BarChartBar)) {
                continue;
            }
            BarChartBar bb = (BarChartBar) ad;
            bb.setSelected(false);
            List<Coord2d> l = bb.getBoundsToScreenProj();
            BoundingBox2d bb2 = new BoundingBox2d(l);
            boolean match = bb2.contains(new Coord2d(out.x, out.y));
            if (match) {
                if (bestMatch == null || (view.getCamera().getEye().distance(bestMatch.getShape().getBounds().getCenter()) > view.getCamera().getEye().distance(bb.getShape().getBounds().getCenter())) && bb.getShape().isDisplayed()) {
                    bestMatch = bb;
                    System.out.println(bb.getInfo());
                }
            }
        }
        if (bestMatch != null) {
            bestMatch.setSelected(true);
        }
    }

    @Override
    protected void drawSelection(Graphics2D g, int width, int height) {
        return;
    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                this.releaseChart();
                break;
            default:
                break;
        }
        holding = false;
        target.render();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (!holding) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_SHIFT:
                    this.attachChart(target);
                    break;
                default:
                    break;
            }
            holding = true;
            target.render();
        }
    }

    protected boolean holding = false;
}
