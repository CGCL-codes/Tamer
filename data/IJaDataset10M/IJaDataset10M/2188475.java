package com.bluebrim.layout.impl.shared.view;

import java.awt.*;
import java.awt.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.page.shared.*;

/**
 * Proxy for CoPageLayoutArea.
 * 
 * @author: Dennis Malmstr�m
 */
public class CoPageLayoutAreaView extends CoCompositePageItemView {

    protected CoImmutableCustomGridIF m_customGrid;

    protected transient CoGridRenderer m_customGridRenderer;

    protected boolean m_isLeftSideOfSpread;

    protected boolean m_isRightSideOfSpread;

    protected boolean m_isSpread;

    protected CoPageSizeIF m_pageSize;

    protected String m_pageSizeName;

    private static final long serialVersionUID = -8680222622744236194L;

    public CoPageLayoutAreaView(CoPageItemIF pageItem, CoCompositePageItemView parent, CoPageLayoutAreaIF.State d, int detailMode) {
        super(pageItem, parent, d, detailMode);
        sync(d);
    }

    public CoPageItemViewRenderer createRenderer(CoPageItemViewRendererFactory f) {
        return f.create(this);
    }

    public boolean isMoveable() {
        return false;
    }

    public boolean isReshapeable() {
        return false;
    }

    private void sync(CoPageLayoutAreaIF.State d) {
        m_pageSize = d.m_pageSize;
        m_isSpread = d.m_isSpread;
        m_pageSizeName = d.m_pageSizeName;
        m_isLeftSideOfSpread = d.m_isLeftSideOfSpread;
        m_isRightSideOfSpread = d.m_isRightSideOfSpread;
        m_customGrid = d.m_customGrid;
        m_customGridRenderer = null;
    }

    protected void paintIcon(Graphics2D g) {
        Object tmp = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(0.7f));
        int h = CoPageItemViewClientUtilities.m_iconHeight - 2 * CoPageItemViewClientUtilities.m_iconY + 1;
        int w = (int) (h / 1.4f);
        int x = (CoPageItemViewClientUtilities.m_iconWidth - w + 1) / 2;
        int y = CoPageItemViewClientUtilities.m_iconY;
        g.setColor(Color.white);
        g.fillRect(x, y, w, h);
        g.setColor(CoPageItemViewClientUtilities.m_pageLayerLayoutAreaIconGridColor);
        g.draw(CoPageItemViewClientUtilities.m_pageLayerLayoutAreaIconShape);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, tmp);
    }

    boolean validateAddTo(CoCompositePageItemView parent, boolean isDirectParent) {
        return false;
    }

    public CoImmutableCustomGridIF getCustomGrid() {
        return m_customGrid;
    }

    public CoCustomGridIF getMutableCustomGrid() {
        return getPageLayerLayoutArea().getMutableCustomGrid();
    }

    public CoPageLayoutAreaIF getPageLayerLayoutArea() {
        return (CoPageLayoutAreaIF) getPageItem();
    }

    public CoPageSizeIF getPageSize() {
        return m_pageSize;
    }

    public String getPageSizeName() {
        return m_pageSizeName;
    }

    public double getWidth() {
        double w = super.getWidth();
        if (m_isLeftSideOfSpread || m_isRightSideOfSpread) {
            w /= 2;
        }
        return w;
    }

    public boolean isSpread() {
        return m_isSpread;
    }

    public void modelChanged(CoPageItemIF.State d, CoPageItemView.Event ev) {
        super.modelChanged(d, ev);
        sync((CoPageLayoutAreaIF.State) d);
    }

    public Point2D snap(double x, double y, double width, double height, double range, Point2D delta) {
        Point2D p = super.snap(x, y, width, height, range, delta);
        return m_customGrid.snap(p.getX(), p.getY(), width, height, range, delta);
    }

    public Point2D snap(double x, double y, double range, int edgeMask, int dirMask, boolean useEdges, Point2D delta) {
        Point2D p = super.snap(x, y, range, edgeMask, dirMask, useEdges, delta);
        return m_customGrid.snap(p.getX(), p.getY(), range, edgeMask, dirMask, useEdges, delta);
    }
}
