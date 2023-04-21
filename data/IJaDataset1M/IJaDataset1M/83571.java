package org.ncgr.cmtv;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class RectangleDragMouseListener extends MouseInputAdapter {

    Component coordinateSystem;

    RectangleDragCallback callback;

    int minSize;

    Rectangle currentRect = null;

    Rectangle previousRectDrawn = new Rectangle();

    Rectangle rectToDraw;

    boolean passedMinSize;

    boolean firstDraw;

    private static Color RECT_DRAG_COLOR = new Color(255, 255, 0, 20);

    public RectangleDragMouseListener(Component coordinateSystem, RectangleDragCallback callback, int minSize) {
        this.coordinateSystem = coordinateSystem;
        this.callback = callback;
        this.minSize = minSize;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) return;
        passedMinSize = false;
        firstDraw = true;
        e = SwingUtilities.convertMouseEvent((Component) e.getSource(), e, coordinateSystem);
        int x = e.getX();
        int y = e.getY();
        currentRect = new Rectangle(x, y, 0, 0);
        updateDrawableRect();
    }

    public void mouseDragged(MouseEvent e) {
        if (currentRect == null) return;
        e = SwingUtilities.convertMouseEvent((Component) e.getSource(), e, coordinateSystem);
        updateSize(e);
        Rectangle[] differenceRects = SwingUtilities.computeDifference(rectToDraw, previousRectDrawn);
        for (int i = 0; i < differenceRects.length; i++) {
            ((JComponent) coordinateSystem).scrollRectToVisible(differenceRects[i]);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (currentRect == null) return;
        if (passedMinSize) {
            if (callback != null) callback.rectangleDragCompleted(currentRect);
            Graphics g = coordinateSystem.getGraphics();
            g.setXORMode(RECT_DRAG_COLOR);
            g.drawRect(rectToDraw.x, rectToDraw.y, rectToDraw.width - 1, rectToDraw.height - 1);
            g.setPaintMode();
            g.dispose();
        }
        currentRect = null;
    }

    void updateSize(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        currentRect.setSize(x - currentRect.x, y - currentRect.y);
        if (!passedMinSize && (Math.abs(currentRect.width) >= minSize || Math.abs(currentRect.height) >= minSize)) passedMinSize = true;
        updateDrawableRect();
        if (passedMinSize) {
            Graphics g = coordinateSystem.getGraphics();
            g.setXORMode(coordinateSystem.getBackground());
            if (!firstDraw) g.drawRect(previousRectDrawn.x, previousRectDrawn.y, previousRectDrawn.width - 1, previousRectDrawn.height - 1); else firstDraw = false;
            g.drawRect(rectToDraw.x, rectToDraw.y, rectToDraw.width - 1, rectToDraw.height - 1);
            g.setPaintMode();
            g.dispose();
        }
    }

    void updateDrawableRect() {
        int x = currentRect.x;
        int y = currentRect.y;
        int width = currentRect.width;
        int height = currentRect.height;
        if (width < 0) {
            width = 0 - width;
            x = x - width + 1;
            if (x < 0) {
                width += x;
                x = 0;
            }
        }
        if (height < 0) {
            height = 0 - height;
            y = y - height + 1;
            if (y < 0) {
                height += y;
                y = 0;
            }
        }
        if ((x + width) > coordinateSystem.getWidth()) {
            width = coordinateSystem.getWidth() - x;
        }
        if ((y + height) > coordinateSystem.getHeight()) {
            height = coordinateSystem.getHeight() - y;
        }
        if (rectToDraw != null) {
            previousRectDrawn.setBounds(rectToDraw.x, rectToDraw.y, rectToDraw.width, rectToDraw.height);
            rectToDraw.setBounds(x, y, width, height);
        } else {
            rectToDraw = new Rectangle(x, y, width, height);
        }
    }
}
