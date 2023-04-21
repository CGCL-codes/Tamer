package org.kaiec.treemap;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 *
 * @author kai
 */
public class TreemapItem {

    private TreemapData data;

    private int x, y, width, height;

    private LayoutConfiguration layoutConfiguration;

    public LayoutConfiguration getLayoutConfiguration() {
        return layoutConfiguration;
    }

    public void setLayoutConfiguration(LayoutConfiguration layoutConfiguration) {
        this.layoutConfiguration = layoutConfiguration;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public TreemapData getData() {
        return data;
    }

    public TreemapItem(TreemapData data, int x, int y, int width, int height, LayoutConfiguration layoutConfig) {
        this.data = data;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.layoutConfiguration = layoutConfig;
        setWithLabel();
    }

    public Rectangle childArea() {
        int m = layoutConfiguration.getMargin();
        int lh = isWithLabel() ? layoutConfiguration.getLabelHeight() : 0;
        return new Rectangle(x + m, y + m + lh, width - 2 * m, height - 2 * m - lh);
    }

    private boolean withLabel;

    public void setWithLabel() {
        this.withLabel = width > labelWidth("XXX...") && height > layoutConfiguration.getLabelHeight();
    }

    private boolean isWithLabel() {
        return withLabel;
    }

    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setColor(layoutConfiguration.getColorAlgorithm().Color(layoutConfiguration.getColorMetricProvider().getColorMetric(getData())));
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.setFont(layoutConfiguration.getLabelFont());
        g.setColor(layoutConfiguration.getLabelColor());
        FontMetrics fontmetrics = g.getFontMetrics();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (isWithLabel()) {
            String text = data.getTMLabel();
            if (width < labelWidth(text)) {
                text = text.substring(0, text.length() - 2);
                while (width < labelWidth(text + "...")) text = text.substring(0, text.length() - 1);
                text = text + "...";
            }
            g.drawString(text, x + 2 + layoutConfiguration.getMargin(), y + fontmetrics.getAscent() + layoutConfiguration.getMargin());
        }
    }

    private double labelWidth(String label) {
        return layoutConfiguration.getLabelFont().getStringBounds(label, layoutConfiguration.getFontRenderContext()).getWidth() + 4;
    }
}
