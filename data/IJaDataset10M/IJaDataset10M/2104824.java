package de.mse.mogwai.impl.swing.beans;

import java.awt.*;
import java.util.Vector;

/**
 * Layout manager to create an extended null layout.
 *
 * @author  Mirko Sertic.
 */
public class ExtendedNullLayout implements LayoutManager {

    public ExtendedNullLayout() {
    }

    public void addLayoutComponent(String str, java.awt.Component component) {
    }

    public void layoutContainer(java.awt.Container container) {
        int insetTop = container.getInsets().top;
        int insetLeft = container.getInsets().left;
        int insetBottom = container.getInsets().bottom;
        int insetRight = container.getInsets().right;
        insetTop = 0;
        insetLeft = 0;
        insetBottom = 0;
        insetRight = 0;
        Component components[] = container.getComponents();
        Vector right = new Vector();
        Vector left = new Vector();
        Vector top = new Vector();
        Vector bottom = new Vector();
        Vector client = new Vector();
        for (int iCount = 0; iCount < components.length; iCount++) {
            Component c = components[iCount];
            if (c instanceof ExtendedLayoutable) {
                ExtendedLayoutable l = (ExtendedLayoutable) c;
                switch(l.getAlign()) {
                    case ExtendedLayoutable.LEFT:
                        left.add(c);
                        break;
                    case ExtendedLayoutable.RIGHT:
                        right.add(c);
                        break;
                    case ExtendedLayoutable.TOP:
                        top.add(c);
                        break;
                    case ExtendedLayoutable.BOTTOM:
                        bottom.add(c);
                        break;
                    case ExtendedLayoutable.CLIENT:
                        client.add(c);
                        break;
                }
            }
        }
        int xp = insetLeft;
        int yp = insetTop;
        int width = container.getWidth() - insetLeft - insetRight;
        for (int iCount = 0; iCount < top.size(); iCount++) {
            Component c = (Component) top.get(iCount);
            int height = c.getHeight();
            c.setLocation(xp, yp);
            c.setSize(width, height);
            yp += height;
        }
        int startY = yp;
        yp = container.getHeight() - insetTop - insetBottom;
        for (int iCount = 0; iCount < bottom.size(); iCount++) {
            Component c = (Component) bottom.get(iCount);
            int height = c.getHeight();
            c.setLocation(xp, yp - height);
            c.setSize(width, height);
            yp -= height;
        }
        int endY = yp;
        yp = startY;
        int height = (endY - startY);
        for (int iCount = 0; iCount < left.size(); iCount++) {
            Component c = (Component) left.get(iCount);
            int cwidth = c.getWidth();
            c.setLocation(xp, yp);
            c.setSize(cwidth, height);
            xp += cwidth;
        }
        int startX = xp;
        xp = container.getWidth() - insetLeft - insetRight;
        for (int iCount = 0; iCount < right.size(); iCount++) {
            Component c = (Component) right.get(iCount);
            int cwidth = c.getWidth();
            c.setLocation(xp - cwidth, yp);
            c.setSize(cwidth, height);
            xp -= cwidth;
        }
        int endX = xp;
        for (int iCount = 0; iCount < client.size(); iCount++) {
            Component c = (Component) client.get(iCount);
            c.setLocation(startX, startY);
            c.setSize(endX - startX, endY - startY);
        }
    }

    public java.awt.Dimension minimumLayoutSize(java.awt.Container container) {
        return container.getSize();
    }

    public java.awt.Dimension preferredLayoutSize(java.awt.Container container) {
        return container.getSize();
    }

    public void removeLayoutComponent(java.awt.Component component) {
    }
}
