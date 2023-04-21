package net.sf.jtreemap.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JToolTip;

/**
 * Default ToolTip for the jTreeMap.
 * 
 * @author Laurent DUTHEIL
 */
public class DefaultToolTip extends JToolTip {

    private static final int TOOLTIP_OFFSET = 5;

    private static final int DEFAULT_VALUE_SIZE = 10;

    private static final int DEFAULT_LABEL_SIZE = 12;

    private static final long serialVersionUID = -2492627777999093973L;

    private final JTreeMap jTreeMap;

    private final Font labelFont;

    private final Font valueFont;

    private final String weightPrefix;

    private final String valuePrefix;

    private final boolean showWeight;

    /**
     * Constructor.
     * 
     * @param jTreeMap
     *            the jTreeMap who display the tooltip
     */
    public DefaultToolTip(final JTreeMap jTreeMap, final String weightPrefix, final String valuePrefix, final boolean showWeight) {
        this.jTreeMap = jTreeMap;
        this.weightPrefix = weightPrefix;
        this.valuePrefix = valuePrefix;
        this.showWeight = showWeight;
        this.labelFont = new Font("Default", Font.BOLD, DEFAULT_LABEL_SIZE);
        this.valueFont = new Font("Default", Font.PLAIN, DEFAULT_VALUE_SIZE);
        final int width = 160;
        final int height = getFontMetrics(this.labelFont).getHeight() + getFontMetrics(this.valueFont).getHeight();
        final Dimension size = new Dimension(width, height);
        this.setSize(size);
        this.setPreferredSize(size);
    }

    @Override
    public void paint(final Graphics g) {
        if (this.jTreeMap.getActiveLeaf() != null) {
            final Graphics g2D = g;
            g2D.setColor(Color.YELLOW);
            g2D.fill3DRect(0, 0, this.getWidth(), this.getHeight(), true);
            g2D.setColor(Color.black);
            g2D.setFont(this.labelFont);
            g2D.drawString(this.jTreeMap.getActiveLeaf().getLabel(), TOOLTIP_OFFSET, g2D.getFontMetrics(this.labelFont).getAscent());
            g2D.setFont(this.valueFont);
            String toDraw = this.jTreeMap.getActiveLeaf().getLabelValue();
            if (valuePrefix != null) {
                toDraw = valuePrefix + " " + toDraw;
            }
            if (showWeight) {
                toDraw = this.jTreeMap.getActiveLeaf().getWeight() + ", " + toDraw;
            }
            if (weightPrefix != null && showWeight) {
                toDraw = weightPrefix + " " + toDraw;
            }
            g2D.drawString(toDraw, TOOLTIP_OFFSET, this.getHeight() - TOOLTIP_OFFSET);
        }
    }
}
