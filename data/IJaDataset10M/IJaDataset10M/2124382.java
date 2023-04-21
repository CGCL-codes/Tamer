package org.sgmiller.formicidae.gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;

/**
 * <P>Manages drag and drop features and mouse wheel listening for the
 *  LargeWorldImage / SimPanel JScrollPane viewport.</P><BR> 
 *  
 * <P><BLOCKQUOTE> 
 *  Copyright (C) 2004, Scott G. Miller
 *  all rights reserved.
 *  See the LICENCE file for details.
 * </BLOCKQUOTE></P><BR>
 * 
 * @author scgmille
 * @since 1.4, Sep-6-2004
 * 
 * @see org.sgmiller.formicidae.gui.LargeWorldImage
 * @see org.sgmiller.formicidae.gui.SimPanel
 */
public class GrabAndDragAdapter extends MouseInputAdapter implements MouseWheelListener {

    /**
   * Remembers the x coordinate of the original mouse press event for a drag
   * action, effectively giving us a baseline reference to derive a delta
   * from as regards the current mouse coordinate location at any given time.
   */
    private int m_XDifference;

    /**
   * Remembers the y coordinate of the original mouse press event for a drag
   * action, effectively giving us a baseline reference to derive a delta
   * from as regards the current mouse coordinate location at any given time.
   */
    private int m_YDifference;

    /**
   * We use mouse dragging to allow the user to pan and shift the gameboard map
   * (when cropped or clipped due to zooming) in the viewport's visible window.
   */
    private boolean dragEnabled = true;

    /**
   * Indicates whether the mouse is actively in the middle of a drag operation.
   */
    private boolean m_dragging = false;

    /**
   * Reference to the scrollpane housing the LargeWorldImage 'image canvas'
   * that performs the actual low-level drawing and graphical representation 
   * of the gameboard and it's state.  This adapter encapsulates and performs
   * various mouse listening services on their behalf.
   */
    private JScrollPane scrollPane;

    /**
   * Reference to the low-level component that does the drawing and graphical 
   * representation of the gameboard and it's state.
   */
    private LargeWorldImage lwi;

    /**
   * Reference to the model controlling zooming info and affine (z-axis) 
   * transformations, relevant as various dragging features are available
   * only when the degree of zooming causes the map's visual representation 
   * area to exceed the scrollpane viewport area.
   */
    private ZoomModel zoomModel;

    /**
   * Cursor memory, to allow toggling the cursor between the classic pointer 
   * cursor image and the four-way arrow move cursor image.
   */
    private Cursor last;

    /**
   * Recommended constructor
   * 
   * @param lwi
   * @param scrollPane
   * @param zoomModel
   */
    public GrabAndDragAdapter(LargeWorldImage lwi, JScrollPane scrollPane, ZoomModel zoomModel) {
        this.lwi = lwi;
        this.scrollPane = scrollPane;
        this.zoomModel = zoomModel;
    }

    /**
   * Simple mutator
   * 
   * @param flag
   */
    public void setDragEnabled(boolean flag) {
        this.dragEnabled = flag;
        return;
    }

    /**
   * MouseInputListner (MouseMotionListner) callback for dragging.
   * 
   * @param e Standard MouseEvent parameter
   */
    public void mouseDragged(MouseEvent e) {
        if (this.dragEnabled) {
            JViewport viewport = this.scrollPane.getViewport();
            Point pt = viewport.getViewPosition();
            int newX = pt.x - (e.getX() - m_XDifference);
            int newY = pt.y - (e.getY() - m_YDifference);
            int maxX = lwi.getWidth() - viewport.getWidth();
            int maxY = lwi.getHeight() - viewport.getHeight();
            if (newX < 0) {
                newX = 0;
            }
            if (newX > maxX) {
                newX = maxX;
            }
            if (newY < 0) {
                newY = 0;
            }
            if (newY > maxY) {
                newY = maxY;
            }
            viewport.setViewPosition(new Point(newX, newY));
        }
        return;
    }

    /**
   * MouseInputListner (MouseListner) callback for downpress.
   * 
   * @param e Standard MouseEvent parameter
   */
    public void mousePressed(MouseEvent e) {
        this.lwi.requestFocus();
        if (this.dragEnabled) {
            last = this.scrollPane.getCursor();
            this.scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
        this.m_XDifference = e.getX();
        this.m_YDifference = e.getY();
        return;
    }

    /**
   * MouseInputListner (MouseListner) callback
   * 
   * @param e Standard MouseEvent parameter
   */
    public void mouseReleased(MouseEvent e) {
        if (this.dragEnabled) {
            this.scrollPane.setCursor(last);
        }
        if (this.lwi != null) {
            this.lwi.grabFocus();
        }
        return;
    }

    /**
   * MouseWheelListener callback, for which we suitably adjust the zoom amount
   * while translating the viewport so the mouse remains over the same cell.
   * 
   * @param e Standard MouseWheelEvent parameter
   * 
   * @see java.awt.event.MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
   */
    public void mouseWheelMoved(MouseWheelEvent e) {
        int cellpos = this.lwi.coordToCellPos(e.getX(), e.getY());
        JViewport viewport = this.scrollPane.getViewport();
        Point oldView = viewport.getViewPosition();
        int notches = e.getWheelRotation();
        double oldZoomLevel = zoomModel.getZoomLevel();
        zoomModel.setZoomLevel(((zoomModel.getZoomLevel() * 10) + notches) / 10.0);
        double newZoomLevel = zoomModel.getZoomLevel();
        double cx = this.lwi.translateX(cellpos) / oldZoomLevel;
        double cy = this.lwi.translateY(cellpos) / oldZoomLevel;
        double dcx = cx - oldView.getX();
        double dcy = cy - oldView.getY();
        double ncx = this.lwi.translateX(cellpos) / newZoomLevel;
        double ncy = this.lwi.translateY(cellpos) / newZoomLevel;
        int dx = (int) (ncx - cx);
        int dy = (int) (ncy - cy);
        Point newView = viewport.getViewPosition();
        viewport.setViewPosition(new Point(newView.x + dx, newView.y + dy));
        if (this.lwi != null) {
            this.lwi.grabFocus();
        }
        return;
    }
}
