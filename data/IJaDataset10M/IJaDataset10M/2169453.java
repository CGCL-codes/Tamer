package org.engine.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collections;
import org.engine.Layer;
import org.engine.SortedSyncList;
import org.engine.Synchronizable;

/**
 * The Class PlainGUI. A simple class to handle the i/o for objects.
 */
public class PlainGUI implements GUI {

    /** The parent layer. */
    private Layer parentLayer;

    public void output(Graphics2D g, Rectangle r) {
        SortedSyncList depthList = new SortedSyncList(SortedSyncList.COMPARE_Z);
        depthList.addAll(getParentLayer().getModel().getAllObjects());
        Collections.reverse(depthList);
        for (Synchronizable s : depthList) {
            s.output(g, r);
        }
    }

    public boolean input(MouseEvent e, KeyEvent k, MouseWheelEvent w, boolean wasCatchedAbove) {
        SortedSyncList depthList = new SortedSyncList(SortedSyncList.COMPARE_Z);
        depthList.addAll(getParentLayer().getModel().getAllObjects());
        for (Synchronizable s : depthList) {
            if (s.input(e, k, w, wasCatchedAbove)) {
                wasCatchedAbove = true;
            }
        }
        return wasCatchedAbove;
    }

    public Layer getParentLayer() {
        return parentLayer;
    }

    public void setParentLayer(Layer parentLayer) {
        this.parentLayer = parentLayer;
    }
}
