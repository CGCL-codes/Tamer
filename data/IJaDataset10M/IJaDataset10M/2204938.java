package com.ivis.xprocess.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class DragableEstimate implements IDragableNode {

    private static final Point size = new Point(7, 7);

    private double decimalHours;

    private final EstimateDiagram estimateDiagram;

    private String label;

    public DragableEstimate(EstimateDiagram estimateDiagram, String label) {
        this.estimateDiagram = estimateDiagram;
        this.label = label;
    }

    public String getLabel(int x, int y) {
        return estimateDiagram.getFormattedLabel(label, decimalHours);
    }

    public double getDecimalHours() {
        return decimalHours;
    }

    public void setDecimalHours(double timeValue) {
        this.decimalHours = timeValue;
    }

    public Point getSize() {
        return size;
    }

    public Rectangle getBounds() {
        return estimateDiagram.getDragableBounds(this);
    }

    public void paint(Display display, GC gc) {
        Rectangle bounds = getBounds();
        int middleX = bounds.x + (bounds.width / 2);
        int middleY = bounds.y + (bounds.height / 2);
        int[] diamondPoints = new int[] { middleX, bounds.y, bounds.x + bounds.width, middleY, middleX, bounds.y + bounds.height, bounds.x, middleY };
        Color color = estimateDiagram.getColourFor(this);
        gc.setBackground(color);
        gc.fillPolygon(diamondPoints);
        if (estimateDiagram.getControl().isEnabled()) {
            gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        } else {
            gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
        }
        gc.drawPolygon(diamondPoints);
    }
}
