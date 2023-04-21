package org.proteomecommons.MSExpedite.app;

import java.awt.Container;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author takis
 */
public class SessionComponentToggleButton extends JToggleButton implements ItemListener, IDnDButton, ISessionButton, ISessionListener, ISessionUIListener, MouseListener, MouseMotionListener {

    protected ISessionHandler sessionHandler = null;

    String setMethodName = "";

    String getMethodName = "";

    Class componentClass = null;

    Point startPoint = new Point(0, 0);

    Point currentPoint = new Point(0, 0);

    boolean removeComponent = false;

    public SessionComponentToggleButton(ISessionHandler h, String text, boolean isSelected, String icon, String getMethodName, String setMethodName, Class componentClass) {
        addItemListener(this);
        if (sessionHandler != null) set(sessionHandler);
        this.getMethodName = getMethodName;
        this.setMethodName = setMethodName;
        this.componentClass = componentClass;
        setIcon(new ImageIcon(this.getClass().getClassLoader().getResource(icon)));
        setToolTipText(text);
    }

    public void finilize() {
        if (this.sessionHandler != null) {
            sessionHandler.remove((ISessionListener) this);
        }
        removeItemListener(this);
    }

    public void itemStateChanged(ItemEvent e) {
        SessionGraph gr = (SessionGraph) (sessionHandler.getContext().getGraph());
        Class c = SessionGraph.class;
        if (isComponentShowing() && e.getStateChange() == ItemEvent.SELECTED) return;
        if (!isComponentShowing() && e.getStateChange() == ItemEvent.DESELECTED) return;
        Method m = null;
        try {
            m = c.getMethod(setMethodName, Boolean.TYPE);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        try {
            m.invoke(gr, isSelected());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    public void set(ISessionHandler sessionHandler) {
        if (this.sessionHandler != null) {
            sessionHandler.remove((ISessionListener) this);
        }
        this.sessionHandler = sessionHandler;
        this.sessionHandler.add((ISessionListener) this);
    }

    public ISessionHandler getSessionHandler() {
        return sessionHandler;
    }

    public void sessionChanged(IContext oldContext, IContext newContext) {
        SessionGraph gr = (SessionGraph) oldContext;
        Class c = gr.getClass();
        gr.removeUIListener(this);
        gr = (SessionGraph) newContext;
        gr.addUIListener(this);
        Method method = null;
        try {
            method = c.getMethod(getMethodName);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        Boolean isOn = new Boolean(false);
        try {
            isOn = (Boolean) method.invoke(gr);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        this.setSelected(isOn.booleanValue());
    }

    public void sessionUpdated(IContext context) {
    }

    public void sessionAdded(IContext newContext) {
    }

    public void sessionRemoved(IContext newContext) {
    }

    public void uiStateChanged(SessionUIState state) {
        Class c = state.getComponent().getClass();
        if (c != componentClass) return;
        if (state.getOwner() != sessionHandler.getContext()) return;
        int flag = state.getChangedState();
        setSelected(flag == SessionUIState.COMPONENT_CREATED);
    }

    public String getShortDescription() {
        return this.getToolTipText();
    }

    public void enableDnD(boolean b) {
        removeMouseListener(this);
        removeMouseMotionListener(this);
        if (b) {
            addMouseListener(this);
            addMouseListener(this);
        }
    }

    public String toString() {
        return getShortDescription();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        startPoint = MouseInfo.getPointerInfo().getLocation();
        currentPoint = new Point(0, 0);
        removeComponent = false;
    }

    public void mouseReleased(MouseEvent e) {
        if (removeComponent) {
            Container container = getParent();
            if (!(container instanceof JToolBar)) return;
            JToolBar toolbar = (JToolBar) container;
            toolbar.remove(this);
            container = SwingUtilities.getWindowAncestor(toolbar);
            container.setVisible(true);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        removeComponent = false;
        currentPoint = MouseInfo.getPointerInfo().getLocation();
        Container container = getParent();
        if (!(container instanceof JToolBar)) return;
        JToolBar toolbar = (JToolBar) container;
        Point locOnScreen = toolbar.getLocationOnScreen();
        if (locOnScreen.y < e.getPoint().y) {
            removeComponent = true;
            return;
        }
        Point pDiff = diff(startPoint, currentPoint);
        Rectangle bounds = getBounds();
        int x = (bounds.x + bounds.width) / 2;
        if (pDiff.x > 0 && ((pDiff.x + x) > (bounds.x + bounds.width))) {
            incrementComponentPos();
        } else if (pDiff.x < 0 && ((pDiff.x + x) < (bounds.x))) {
            decrementComponentPos();
        }
        int y = 0;
        Point p = new Point(x, y);
    }

    public void mouseMoved(MouseEvent e) {
    }

    private Point diff(Point p1, Point p2) {
        int x = p2.x - p1.x;
        int y = p2.y - p1.y;
        return new Point(x, y);
    }

    private void incrementComponentPos() {
        Container container = getParent();
        if (!(container instanceof JToolBar)) return;
        JToolBar toolbar = (JToolBar) container;
        int count = toolbar.getComponentCount();
        int currentIndex = toolbar.getComponentIndex(this);
        if (currentIndex == count - 1) return;
        toolbar.add(this, currentIndex + 1);
    }

    private boolean isComponentShowing() {
        SessionGraph gr = (SessionGraph) (sessionHandler.getContext().getGraph());
        Class c = SessionGraph.class;
        Method m = null;
        try {
            m = c.getMethod(getMethodName);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        Boolean b = false;
        try {
            b = (Boolean) m.invoke(gr);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return b.booleanValue();
    }

    private void decrementComponentPos() {
        Container container = getParent();
        if (!(container instanceof JToolBar)) return;
        JToolBar toolbar = (JToolBar) container;
        int count = toolbar.getComponentCount();
        int currentIndex = toolbar.getComponentIndex(this);
        if (currentIndex == 0) return;
        toolbar.add(this, currentIndex - 1);
    }
}
