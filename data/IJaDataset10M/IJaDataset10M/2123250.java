package com.seaglasslookandfeel.painter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.JComponent;
import com.seaglasslookandfeel.painter.AbstractRegionPainter.PaintContext.CacheMode;

public final class TreePainter extends AbstractRegionPainter {

    public enum Which {

        COLLAPSEDICON_ENABLED, COLLAPSEDICON_ENABLED_SELECTED, EXPANDEDICON_ENABLED, EXPANDEDICON_ENABLED_SELECTED
    }

    private Which state;

    private PaintContext ctx;

    private Color selectedColor = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);

    private Color enabledColor = decodeColor("nimbusBlueGrey", -0.6111111f, -0.110526316f, -0.34509805f, 0);

    public TreePainter(Which state) {
        super();
        this.state = state;
        this.ctx = new PaintContext(CacheMode.FIXED_SIZES);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        switch(state) {
            case COLLAPSEDICON_ENABLED:
                paintCollapsedIconEnabled(g, width, height);
                break;
            case COLLAPSEDICON_ENABLED_SELECTED:
                paintCollapsedIconEnabledAndSelected(g, width, height);
                break;
            case EXPANDEDICON_ENABLED:
                paintExpandedIconEnabled(g, width, height);
                break;
            case EXPANDEDICON_ENABLED_SELECTED:
                paintExpandedIconEnabledAndSelected(g, width, height);
                break;
        }
    }

    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintCollapsedIconEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeCollapsedPath(width, height);
        g.setPaint(enabledColor);
        g.fill(s);
    }

    private void paintCollapsedIconEnabledAndSelected(Graphics2D g, int width, int height) {
        Shape s = decodeCollapsedPath(width, height);
        g.setPaint(selectedColor);
        g.fill(s);
    }

    private void paintExpandedIconEnabled(Graphics2D g, int width, int height) {
        Shape s = decodeExpandedPath(width, height);
        g.setPaint(enabledColor);
        g.fill(s);
    }

    private void paintExpandedIconEnabledAndSelected(Graphics2D g, int width, int height) {
        Shape s = decodeExpandedPath(width, height);
        g.setPaint(selectedColor);
        g.fill(s);
    }

    private Shape decodeCollapsedPath(int width, int height) {
        return shapeGenerator.createArrowRight(0, 0, width, height);
    }

    private Shape decodeExpandedPath(int width, int height) {
        return shapeGenerator.createArrowDown(0, 0, width, height);
    }
}
