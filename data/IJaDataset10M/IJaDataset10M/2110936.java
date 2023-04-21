package org.jhotdraw.draw;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jhotdraw.geom.BezierPath;

/**
 * LineFigure.
 *
 * @author Werner Randelshofer
 * @version 2.0 2006-02-27 Support point editing at handle level 0. 
 * <br>2.0 2006-01-14 Changed to support double precison coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public class LineFigure extends BezierFigure {

    /** Creates a new instance. */
    public LineFigure() {
        addNode(new BezierPath.Node(new Point2D.Double(0, 0)));
        addNode(new BezierPath.Node(new Point2D.Double(0, 0)));
    }

    public Collection<Handle> createHandles(int detailLevel) {
        LinkedList<Handle> handles = new LinkedList<Handle>();
        switch(detailLevel) {
            case 0:
                handles.add(new BezierOutlineHandle(this));
                for (int i = 0, n = path.size(); i < n; i++) {
                    handles.add(new BezierNodeHandle(this, i));
                }
                break;
        }
        return handles;
    }

    public boolean canConnect() {
        return false;
    }

    /**
     * Handles a mouse click.
     */
    public boolean handleMouseClick(Point2D.Double p, MouseEvent evt, DrawingView view) {
        if (evt.getClickCount() == 2 && view.getHandleDetailLevel() == 0) {
            willChange();
            final int index = splitSegment(p, (float) (5f / view.getScaleFactor()));
            if (index != -1) {
                final BezierPath.Node newNode = getNode(index);
                fireUndoableEditHappened(new AbstractUndoableEdit() {

                    public void redo() throws CannotRedoException {
                        super.redo();
                        willChange();
                        addNode(index, newNode);
                        changed();
                    }

                    public void undo() throws CannotUndoException {
                        super.undo();
                        willChange();
                        removeNode(index);
                        changed();
                    }
                });
                changed();
                return true;
            }
        }
        return false;
    }
}
