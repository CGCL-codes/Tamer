package com.amerilib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JComponent;

/**
 * This class is the primary class for interacting with the AmeriLib package.
 * StateShapes can be added to it, and user interaction is sent to listeners
 * who register with a StateCanvas object.  
 * 
 * @author  Jason Nichols (jason@amerilib.com)
 */
public class StateCanvas extends JComponent {

    private List<StateShape> stateList;

    private StateShape selected = null;

    private List<StateMouseListener> stateMouseListeners;

    private Area area;

    private float scale = 1.0f;

    private boolean autoSize = true;

    private boolean autoCenter = true;

    private float margins = 0.05f;

    Paint bgPaint = new Color(25, 25, 25);

    /** Creates new form StateCanvas */
    public StateCanvas() {
        initComponents();
        addComponentListener(new ResizeAdapter());
        area = new Area();
        stateList = new ArrayList<StateShape>();
        stateMouseListeners = new ArrayList<StateMouseListener>();
        CanvasMouseListener l = new CanvasMouseListener();
        addMouseMotionListener(l);
        addMouseListener(l);
    }

    /**
     * Method to set the background paint to the specified paint.  This may
     * be any class that implements the Paint interface (currently Color or
     * GradientPaint).
     */
    public void setBackgroundPaint(Paint paint) {
        if (paint != null) {
            bgPaint = paint;
        }
        repaint();
    }

    /**
    * Same as calling setBackgroundPaint(Paint paint), overridden for 
    * compatibility.
    */
    @Override
    public void setBackground(Color bg) {
        setBackgroundPaint(bg);
    }

    /**
    * Returns the Paint used to fill the background of the canvas. This may be
    * any class that implements the Paint interface (Color, GradientPaint, etc).
    */
    public Paint getBackgroundPaint() {
        return bgPaint;
    }

    /**
    * This method is not used by the library, and has been overridden to return
    * null.  Use getBackgroundPaint() instead.
    * @return Null.  This method should not be used.
    */
    @Override
    public Color getBackground() {
        return null;
    }

    /**
     * Add a StateMouseListener to the internal listener list.  After
     * adding a listener it will start receiving mouse events regarding 
     * StateShapes.
     * 
     * @param l The StateMouseListener to add.
     * @return True if the listener was added to the list.
     */
    public boolean addStateMouseListener(StateMouseListener l) {
        if (!stateMouseListeners.contains(l)) {
            return stateMouseListeners.add(l);
        } else return false;
    }

    /**
     * Removes a StateMouseListener from the internal listener list.  After
     * removing a listener it will no longer receive mouse events regarding
     * StateShapes.
     * 
     * @param l The StateMouseListener to remove.
     * @return True if the listener was removed from the list.
     */
    public boolean removeStateMouseListener(StateMouseListener l) {
        return stateMouseListeners.remove(l);
    }

    /**
     * Remove listed states from the canvas, if present.  After executing this
     * method layoutStates() should be called, followed by a call to repaint().
     * 
     * @param c Collection of StateShapes to be removed.
     */
    public void removeStates(Collection<StateShape> c) {
        for (StateShape s : c) {
            stateList.remove(s);
        }
        area.reset();
        for (StateShape s : stateList) {
            area.add(s.getTranslatedArea());
        }
    }

    /**
     * Remove Stateshape from the canvas, if present.  After executing this
     * method layoutStates() should be called, followed by a call to repaint().
     * 
     * @param state The StateShape to be removed from the canvas.
     */
    public void removeState(StateShape state) {
        boolean rem = stateList.remove(state);
        if (rem) {
            area.reset();
            for (StateShape s : stateList) {
                area.add(s.getTranslatedArea());
            }
        }
    }

    /**
     * Add all 50 states to the canvas. After executing this method 
     * layoutStates() should be called, followed by repaint().  
     */
    public void addAllStates() {
        for (States state : States.values()) {
            addState(state.getInstance());
        }
    }

    /**
     * Add the continental 48 states to the canvas. After executing this method
     * layoutStates() should be called, followed by repaint().  
     */
    public void addLower48States() {
        addAllStates();
        removeState(States.AK.getInstance());
        removeState(States.HI.getInstance());
    }

    /**
     * Go through the list of states and lay them out accordingly.  This will
     * find the largest zoom size that will fit in the canvas, lay the states
     * out relative to each other, and then center them in the canvas. This 
     * should be run after any state is added or removed from the canvas.
     * 
     * <b>This should only run in the EDT</b>
     */
    public void layoutStates() {
        if (autoSize) {
            sizeStates();
        }
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        for (StateShape state : stateList) {
            if (minX > state.getXOffset()) {
                minX = state.getXOffset();
            }
            if (minY > state.getYOffset()) {
                minY = state.getYOffset();
            }
        }
        if (autoCenter) {
            float xDelta = (getWidth() / scale - area.getBounds().width) / 2;
            float yDelta = (getHeight() / scale - area.getBounds().height) / 2;
            minX -= xDelta;
            minY -= yDelta;
        }
        for (StateShape state : stateList) {
            state.setMods(-minX, -minY);
        }
    }

    /**
     * Automatically determine the biggest size that will fit within our
     * canvas margins.
     */
    private void sizeStates() {
        if (area.getBounds().width == 0 || area.getBounds().height == 0 || getWidth() == 0 || getHeight() == 0) {
            scale = 1.0f;
            return;
        }
        float xScale = (getWidth() * (1 - 2 * margins)) / area.getBounds().width;
        float yScale = (getHeight() * (1 - 2 * margins)) / area.getBounds().height;
        scale = Math.min(xScale, yScale);
    }

    /**
     * Add a StateShape to the canvas. After executing this method,
     * layoutStates() should be called, followed by repaint().
     * 
     * @param shape The Stateshape to be added to the canvas.
     * @return True if the StateShape was added to the canvas.
     */
    public boolean addState(StateShape shape) {
        boolean add = stateList.add(shape);
        if (add) {
            area.add(shape.getTranslatedArea());
        }
        return add;
    }

    /**
     * Add StateShapes to the canvas. After executing this method,
     * layoutStates() should be called, followed by repaint().
     * 
     * @param states The collection of StateShapes to be added to the canvas.
     */
    public void addStates(Collection<StateShape> states) {
        for (StateShape s : states) {
            stateList.add(s);
            area.add(s.getTranslatedArea());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(bgPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.scale(scale, scale);
        for (StateShape state : stateList) {
            Path2D p = state.getModifiedPath();
            if (g2d.getClip().intersects(p.getBounds())) {
                g2d.setPaint(state.getBackgroundPaint());
                g2d.fill(p);
                g2d.setStroke(state.getBorderStroke());
                g2d.setPaint(state.getBorderPaint());
                g2d.draw(p);
            }
        }
        g2d.dispose();
    }

    private class CanvasMouseListener extends MouseAdapter implements MouseMotionListener {

        /**
         * Invoked when the mouse button has been clicked (pressed and released)
         * on a component.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            StateShape s = findStateAt(e.getX() / scale, e.getY() / scale);
            if (s != null) {
                for (StateMouseListener l : stateMouseListeners) {
                    l.mouseClicked(s, e);
                }
            }
        }

        /**
         * Invoked when a mouse button has been pressed on a component.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            StateShape s = findStateAt(e.getX() / scale, e.getY() / scale);
            if (s != null) {
                for (StateMouseListener l : stateMouseListeners) {
                    l.mousePressed(s, e);
                }
            }
        }

        /**
         * Invoked when a mouse button has been released the canvas.      
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            StateShape s = findStateAt(e.getX() / scale, e.getY() / scale);
            if (s != null) {
                for (StateMouseListener l : stateMouseListeners) {
                    l.mouseReleased(s, e);
                }
            }
        }

        /**
         * Invoked when the mouse has moved over the canvas.
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            StateShape s = findStateAt(e.getX() / scale, e.getY() / scale);
            if (selected != s) {
                for (StateMouseListener l : stateMouseListeners) {
                    if (selected != null) {
                        l.mouseExited(selected, e);
                    }
                    if (s != null) {
                        l.mouseEntered(s, e);
                    }
                }
                selected = s;
            }
        }
    }

    /**
     * Queue the region for repainting.  This method is a convience method for
     * painting the modified (actual) bounds of a particular state, before 
     * scaling occurs internally.  Expected usage is <i>
     * repaintScaled( shape.getModifiedArea().getBounds() );</i>
     */
    public void repaintScaled(Rectangle bounds) {
        Rectangle r = bounds;
        int x = (int) (r.x * scale);
        int y = (int) (r.y * scale);
        repaint(x, y, (int) (x + r.width * scale), (int) (y + r.height * scale));
    }

    /**
     * Return a reference to the StateShape found at the given canvas
     * coordinates.
     *
     * @return The StateShape at x,y, or null if none is found.
     */
    public StateShape findStateAt(float x, float y) {
        StateShape state = null;
        boolean found = false;
        for (StateShape s : stateList) {
            Area a = s.getModifiedArea();
            if (a.getBounds().contains(x, y)) {
                found = a.contains(x, y);
            }
            if (found) {
                state = s;
                break;
            }
        }
        return state;
    }

    /**
     * Get the current scaling factor used by the canvas when rendering the
     * states.  This value is dependent on the states shown as well as the
     * canvas size.
     */
    public float getScale() {
        return scale;
    }

    /**
     * This class updates the layout when the canvas is resized.
     */
    private class ResizeAdapter extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            if (autoSize) {
                layoutStates();
                repaint();
            }
        }
    }

    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    }
}
