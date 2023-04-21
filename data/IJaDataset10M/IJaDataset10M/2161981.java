package com.comarretechnologies.midp.widgets.border;

import javax.microedition.lcdui.Graphics;
import com.comarretechnologies.midp.widgets.Color;
import com.comarretechnologies.midp.widgets.style.Style;

/**
 * @author Comarre Technologies
 */
public class BevelBorder implements Border {

    public static final int RAISED = 0;

    public static final int LOWERED = 1;

    public static final int DEFAULT_THICKNESS = 3;

    private int thickness;

    private int[] insets;

    private int bevelType = RAISED;

    private int outlineColor;

    private int shadowColor;

    private int highlightColor;

    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public BevelBorder(Style style, int bevelType) {
        this(style, bevelType, DEFAULT_THICKNESS);
    }

    public void setStyle(Style style) {
        if (style != null) {
            shadowColor = Color.darker(style.getBackgroundColor());
            highlightColor = Color.lighter(style.getBackgroundColor());
            outlineColor = Color.darker(shadowColor);
        }
    }

    public BevelBorder(Style style, int bevelType, int thickness) {
        insets = new int[4];
        setStyle(style);
        setBevelType(bevelType);
        setThickness(thickness);
    }

    /**
	 * {@inheritDoc}
	 */
    public int[] getInsets() {
        return insets;
    }

    /**
	 * {@inheritDoc}
	 */
    public void paint(Graphics graphics, int width, int height, boolean focused) {
        graphics.setStrokeStyle(Graphics.SOLID);
        graphics.setColor(bevelType == LOWERED ? shadowColor : highlightColor);
        for (int i = 0; i < thickness; i++) {
            graphics.drawLine(0, i, width - i - 1, i);
            graphics.drawLine(i, i, i, height - i);
        }
        graphics.setColor(bevelType == LOWERED ? highlightColor : shadowColor);
        for (int i = 0; i < thickness; i++) {
            graphics.drawLine(width - i, i, width - i, height);
            graphics.drawLine(i + 1, height - i, width, height - i);
        }
    }

    public int getBevelType() {
        return bevelType;
    }

    public void setBevelType(int bevelType) {
        this.bevelType = bevelType;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
        insets[0] = insets[1] = insets[2] = insets[3] = thickness;
    }

    public int getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(int outlineColor) {
        this.outlineColor = outlineColor;
    }
}
