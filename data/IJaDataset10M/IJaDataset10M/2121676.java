package com.hifi.plugin.ui.components.smooth.list.reorder;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ListItemMotionListener extends MouseAdapter implements MouseMotionListener {

    private ReorderAnimator animator;

    private boolean dragActive;

    private Point origin;

    public ListItemMotionListener(ReorderAnimator animator) {
        this.animator = animator;
    }

    private boolean sufficientMove(Point where) {
        int dx = Math.abs(origin.x - where.x);
        int dy = Math.abs(origin.y - where.y);
        return Math.sqrt(dx * dx + dy * dy) > 5;
    }

    public void mousePressed(MouseEvent e) {
        origin = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
        if (dragActive) {
            animator.endDrag(e.getPoint());
            dragActive = false;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (!dragActive) {
            if (sufficientMove(e.getPoint())) {
                dragActive = animator.startDrag(origin);
            }
        }
        if (dragActive) animator.setInsertionLocation(e.getPoint());
    }

    public void mouseExited(MouseEvent e) {
        if (dragActive) animator.setInsertionIndex(-1);
    }

    public void mouseEntered(MouseEvent e) {
        if (dragActive) animator.setInsertionLocation(e.getPoint());
    }

    public void mouseMoved(MouseEvent e) {
    }
}
