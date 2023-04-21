package com.ecmdeveloper.plugin.search.figures;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * 
 * @author ricardo.belfor
 *
 */
public class ContainerBorder extends org.eclipse.draw2d.LineBorder {

    private static final int HEIGHT = 180;

    private static final int ALPHA_VALUE = 150;

    protected int grabBarWidth = 30;

    protected Dimension grabBarSize = new Dimension(grabBarWidth, HEIGHT);

    private final String label;

    private final Color grabBarColor;

    private boolean enabled = true;

    public ContainerBorder(String label, Color grabBarColor) {
        this.label = label;
        this.grabBarColor = grabBarColor;
    }

    public ContainerBorder(int width) {
        setGrabBarWidth(width);
        grabBarSize = new Dimension(width, HEIGHT);
        label = null;
        grabBarColor = QueryColorConstants.logicGreen;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Insets getInsets(IFigure figure) {
        return new Insets(getWidth() + 2, grabBarWidth + 2, getWidth() + 2, getWidth() + 2);
    }

    public Dimension getPreferredSize() {
        return grabBarSize;
    }

    public void paint(IFigure figure, Graphics graphics, Insets insets) {
        if (!enabled) {
            graphics.setAlpha(ALPHA_VALUE);
        }
        Rectangle bounds = figure.getBounds();
        paintLeftBar(graphics, bounds);
        if (label != null) {
            paintLabel(graphics, bounds);
        }
        super.paint(figure, graphics, insets);
    }

    private void paintLeftBar(Graphics graphics, Rectangle bounds) {
        tempRect.setBounds(new Rectangle(bounds.x, bounds.y, grabBarWidth, bounds.height));
        graphics.setBackgroundColor(grabBarColor);
        graphics.fillRectangle(tempRect);
    }

    private void paintLabel(Graphics graphics, Rectangle bounds) {
        Color foregroundColor = graphics.getForegroundColor();
        graphics.setForegroundColor(QueryColorConstants.containerLabelColor);
        Font labelFont = new Font(null, "Arial", 10, SWT.BOLD);
        graphics.setFont(labelFont);
        graphics.drawText(label, getTextPosition(bounds, labelFont));
        graphics.setForegroundColor(foregroundColor);
    }

    private Point getTextPosition(Rectangle bounds, Font font) {
        Dimension textExtents = FigureUtilities.getTextExtents(label, font);
        int yPos = bounds.y + bounds.height / 2 - textExtents.height / 2;
        int xPos = bounds.x + grabBarWidth / 2 - textExtents.width / 2;
        return new Point(xPos, yPos);
    }

    public void setGrabBarWidth(int width) {
        grabBarWidth = width;
    }
}
