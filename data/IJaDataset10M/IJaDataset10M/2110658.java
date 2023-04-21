package com.hifi.plugin.ui.components.smooth.list.reorder;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import com.hifi.plugin.ui.dnd.AbstractComponentDecorator;

/** Animates moving list cells out of the way for a potential drop.
 * This decorator completely over-paints the target JList, optionally
 * painting a dragged item and animating creation of a space for the
 * dragged item to be dropped. 
 * Thanks to Neil Cochran/keilly for a base visualization:
 * http://jroller.com/page/swinguistuff?entry=animated_jlist 
 */
public abstract class ReorderAnimator extends AbstractComponentDecorator {

    /** Animation repaint interval.  Make this larger to slow down the 
     * animation. 
     */
    private static final int INTERVAL = 1000 / 24;

    private static Timer timer;

    private Counter counter;

    /** Index of insertion point. */
    private int insertionIndex = -1;

    /** Index of object being dragged, if any. */
    private int draggedIndex = -1;

    protected JList list;

    private Rectangle[] bounds;

    private GhostedDragImage dragImage;

    private Point origin;

    private ReorderAnimator syncObj = this;

    public ReorderAnimator(final JList list) {
        super(list);
        this.setVisible(false);
        this.list = list;
        counter = new Counter();
    }

    protected Object getPlaceholder() {
        return "";
    }

    protected abstract void move(int fromIndex, int toIndex);

    protected void drop(Transferable t, int index) {
    }

    private synchronized void initialize(Point where) {
        insertionIndex = draggedIndex = -1;
        origin = where;
        int size = list.getModel().getSize();
        bounds = new Rectangle[size];
        for (int i = 0; i < size; i++) {
            Rectangle r = getCellBoundsAfterInsertion(i);
            bounds[i] = r;
        }
    }

    /** Track a drag which originated somewhere else. */
    public synchronized void startDragOver(Point where) {
        initialize(where);
        insertionIndex = getIndex(where, false);
    }

    /** Stop tracking an external drag. */
    public synchronized void endDragOver(Point where, Transferable t) {
        int idx = getIndex(where, false);
        if (idx != -1) {
            drop(t, idx);
        }
    }

    /** Start an internal drag. */
    public synchronized boolean startDrag(Point where) {
        boolean returnVal = true;
        synchronized (syncObj) {
            if (list != null && list.getModel().getSize() > 0) {
                this.setVisible(true);
                list.setValueIsAdjusting(true);
                initialize(where);
                draggedIndex = insertionIndex = getIndex(where, true);
                if (draggedIndex < 0) returnVal = false; else dragImage = new GhostedDragImage(draggedIndex, origin);
            }
        }
        if (returnVal) {
            timer = new Timer(true);
            timer.schedule(counter, INTERVAL, INTERVAL);
        }
        return returnVal;
    }

    int count = 0;

    /** End an internal drag. */
    public synchronized void endDrag(Point where) {
        int toIndex = 0;
        synchronized (syncObj) {
            if (list != null && list.getModel().getSize() > 0) {
                toIndex = getIndex(where, true);
                int fromIndex = draggedIndex;
                dragImage.dispose();
                dragImage = null;
                draggedIndex = insertionIndex = -1;
                if (toIndex != -1 && toIndex != fromIndex) {
                    move(fromIndex, toIndex);
                }
                list.setValueIsAdjusting(false);
                list.setSelectedIndex(toIndex);
            }
        }
        list.repaint();
        this.setVisible(false);
        timer.cancel();
    }

    private synchronized boolean reposition() {
        boolean changed = false;
        if (bounds != null) {
            for (int i = 0; i < bounds.length; i++) {
                Rectangle current = getCurrentCellBounds(i);
                Rectangle end = getCellBoundsAfterInsertion(i);
                if (current != null && end != null) {
                    if (current.x != end.x || current.y != end.y) {
                        int xdelta = (end.x - current.x) / 2;
                        int ydelta = (end.y - current.y) / 2;
                        if (xdelta == 0) current.x = end.x; else current.x += xdelta;
                        if (ydelta == 0) current.y = end.y; else current.y += ydelta;
                        bounds[i] = current;
                        changed = true;
                    }
                }
            }
        }
        return changed;
    }

    private synchronized int getIndex(Point where, boolean restrict) {
        int idx = list.locationToIndex(where);
        if (!restrict) {
            int size = list.getModel().getSize();
            Rectangle last = list.getCellBounds(size - 1, size - 1);
            if (idx == size - 1 && where.y > last.y + last.height) {
                idx = size;
            }
        }
        return idx;
    }

    public synchronized void setInsertionLocation(Point where) {
        synchronized (syncObj) {
            if (list != null && list.getModel().getSize() > 0) {
                getPainter().requestFocus();
                int index = 0;
                list.clearSelection();
                index = getIndex(where, draggedIndex != -1);
                setInsertionIndex(index);
                dragImage.setLocation(where);
            }
        }
    }

    public synchronized void setInsertionIndex(int idx) {
        if (idx != insertionIndex) {
            insertionIndex = idx;
        }
    }

    private synchronized Rectangle getCellBoundsAfterInsertion(int index) {
        Rectangle r = list.getCellBounds(index, index);
        if (draggedIndex != -1) {
            if (index > draggedIndex) {
                if (index <= insertionIndex) {
                    Rectangle r2 = list.getCellBounds(draggedIndex, draggedIndex);
                    r.y -= r2.height;
                }
            } else if (index < draggedIndex) {
                if (index >= insertionIndex) {
                    Rectangle r2 = list.getCellBounds(draggedIndex, draggedIndex);
                    r.y += r2.height;
                }
            } else {
                if (insertionIndex != -1) {
                    Rectangle r2 = list.getCellBounds(insertionIndex, insertionIndex);
                    r.y = r2.y;
                }
            }
        } else if (insertionIndex != -1 && index > insertionIndex) {
            ListCellRenderer rnd = list.getCellRenderer();
            Component c = rnd.getListCellRendererComponent(list, getPlaceholder(), insertionIndex, false, false);
            r.y += c.getHeight();
        }
        return r;
    }

    private synchronized Rectangle getCurrentCellBounds(int cellIndex) {
        Rectangle r = getCellBoundsAfterInsertion(cellIndex);
        Rectangle r2 = bounds[cellIndex];
        if (r2 != null) {
            r.x = r2.x;
            r.y = r2.y;
        }
        return r;
    }

    public synchronized void paint(Graphics g) {
        if (!this.isVisible()) return;
        synchronized (syncObj) {
            boolean db = list.isDoubleBuffered();
            list.setDoubleBuffered(false);
            Insets in = list.getInsets();
            try {
                if (draggedIndex != -1) {
                    g.setColor(list.getBackground());
                    Rectangle rb1 = list.getCellBounds(0, 0);
                    Point p1 = list.indexToLocation(0);
                    g.fillRect(p1.x, p1.y, rb1.width, list.getHeight() - in.top - in.bottom - p1.y);
                    for (int i = 0; i < list.getModel().getSize(); i++) {
                        if (i == draggedIndex) continue;
                        Rectangle r = getCurrentCellBounds(i);
                        if (i != draggedIndex) {
                            Graphics g2 = g.create(r.x, r.y, r.width, r.height);
                            Rectangle r2 = list.getCellBounds(i, i);
                            ((Graphics2D) g2).translate(-r2.x, -r2.y);
                            Graphics g3 = g2.create(0, 0, rb1.width, list.getHeight() - in.top - in.bottom);
                            list.paint(g3);
                        }
                    }
                }
            } finally {
                list.setDoubleBuffered(db);
                list.repaint();
            }
        }
    }

    /** Simple decorator to provide the ghosted drag image. */
    private final class GhostedDragImage extends AbstractComponentDecorator {

        private int index;

        private Point location;

        private Point offset;

        public GhostedDragImage(int cellIndex, Point origin) {
            super(list);
            this.index = cellIndex;
            Rectangle b = list.getCellBounds(index, index);
            location = origin;
            this.offset = new Point(0, origin.y - b.y);
        }

        public synchronized void setLocation(Point where) {
            this.location = where;
            getPainter().repaint();
        }

        public synchronized void paint(Graphics g) {
            synchronized (syncObj) {
                if (list != null && list.getModel().getSize() > 0) {
                    Rectangle b = list.getCellBounds(index, index);
                    if (b != null) {
                        Point origin = new Point(0, location.y - offset.y);
                        origin.y = Math.max(0, origin.y);
                        origin.y = Math.min(origin.y, list.getHeight() - b.height);
                        g = g.create(origin.x, origin.y, b.width, b.height);
                        ((Graphics2D) g).translate(-b.x, -b.y);
                        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                        list.paint(g);
                    }
                }
            }
        }
    }

    private final class Counter extends TimerTask {

        public void run() {
            synchronized (syncObj) {
                boolean repos = false;
                if (draggedIndex != -1) {
                    repos = reposition();
                    if (repos) {
                        repaint();
                    }
                } else {
                    try {
                        Thread.sleep(25);
                    } catch (Exception e) {
                    }
                    ;
                }
            }
        }
    }
}
