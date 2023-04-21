package com.bluebrim.layout.impl.client.view;

import java.awt.*;
import java.util.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Renderer for CoRootView.
 * 
 * @author: Dennis Malmstr�m
 */
public class CoRootViewRenderer extends CoCompositePageItemViewRenderer {

    protected void fill(CoPaintable g, CoShapePageItemView v, Rectangle bounds) {
    }

    protected void outline(CoPaintable g, CoShapePageItemView v, Rectangle bounds) {
    }

    public void paint(CoPaintable p, CoPageItemView v, Rectangle bounds) {
        super.paint(p, v, bounds);
        CoRootView view = (CoRootView) v;
        Iterator e = view.getSelectionManager().getSelected().iterator();
        while (e.hasNext()) {
            ((CoSelectedView) e.next()).paint(p);
        }
        paintCustomGrid(p, view, bounds);
    }

    protected void paintChildren(CoPaintable g, CoCompositePageItemView v, Rectangle bounds) {
        super.paintChildren(g, v, bounds);
        CoRootView rootView = (CoRootView) v;
        Iterator e = rootView.m_modelViews.iterator();
        while (e.hasNext()) {
            CoShapePageItemView w = (CoShapePageItemView) e.next();
            boolean isActive = rootView.isActive(w);
            Map tmp = null;
            if (!isActive) {
                tmp = g.getRenderingHints();
                g.addRenderingHints(NO_DECORATIONS);
            }
            w.paint(g, bounds);
            if (!isActive) {
                g.setRenderingHints(tmp);
            }
        }
    }

    protected void paintCustomGrid(CoPaintable g, CoRootView v, Rectangle bounds) {
        if (g.getRenderingHint(PAINT_CUSTOM_GRID) == PAINT_CUSTOM_GRID_ON) {
            if (v.m_customGridRenderer == null) {
                v.m_customGridRenderer = new CoGridRenderer();
                v.m_customGridRenderer.setGrid(v.m_customGrid);
            }
            g.setColor(CUSTOM_GRID_COLOR);
            v.m_customGridRenderer.paint(g);
        }
    }

    protected void stroke(CoPaintable g, CoShapePageItemView v, Rectangle bounds) {
    }
}
