package org.jdesktop.mtgame;

import java.util.ArrayList;
import java.awt.event.*;
import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Toolkit;

/**
 * This is the default input manager.  It listens to mouse and 
 * keyboard events via jME.  It currently routes events to all
 * Entities listening.
 * 
 * @author Doug Twilleager
 */
class AWTInputManager extends InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    /**
     * The list of all InputComponents
     */
    private ArrayList inputComponents = new ArrayList();

    /**
     * The list of entities interested in key events
     */
    private ArrayList keyListeners = new ArrayList();

    /**
     * The list of entities interested in mouse events
     */
    private ArrayList mouseListeners = new ArrayList();

    /**
     * A state variable tracking whether or not we are listening for mouse events
     */
    private boolean mouseListening = false;

    /**
     * A state variable tracking whether or not we are listening for key events
     */
    private boolean keyListening = false;

    /**
     * The WorldManager
     */
    private WorldManager worldManager = null;

    /**
     * The default constructor
     */
    AWTInputManager() {
    }

    /**
     * The initialize method
     */
    void initialize(WorldManager wm) {
        worldManager = wm;
    }

    /**
     * Create an AWTInputComponent.
     */
    public InputComponent createInputComponent(Canvas c, int events) {
        AWTInputComponent ic = new AWTInputComponent(c, events);
        if ((events & KEY_EVENTS) != 0) {
            addAWTKeyListener(c, ic);
        }
        if ((events & MOUSE_EVENTS) != 0) {
            addAWTMouseListener(c, ic);
        }
        return (ic);
    }

    /**
     * Create an AWTInputComponent.
     */
    public void removeInputComponent(InputComponent ic) {
        AWTInputComponent aic = (AWTInputComponent) ic;
        int events = aic.getEventMask();
        if ((events & KEY_EVENTS) != 0) {
            removeAWTKeyListener(aic.getCanvas(), aic);
        }
        if ((events & MOUSE_EVENTS) != 0) {
            removeAWTMouseListener(aic.getCanvas(), aic);
        }
    }

    /**
     * This method adds an entity to the list of those tracking key events
     * @param e The interested entity
     */
    void addAWTKeyListener(Canvas c, AWTEventListener listener) {
        synchronized (keyListeners) {
            keyListeners.add(listener);
            if (!keyListening) {
                worldManager.trackKeyInput(c, this);
                keyListening = true;
            }
        }
    }

    /**
     * This method removes an entity from the list of those tracking key events
     * @param e The uinterested entity
     */
    void removeAWTKeyListener(Canvas c, AWTEventListener listener) {
        synchronized (keyListeners) {
            keyListeners.remove(listener);
            if (keyListeners.size() == 0) {
                worldManager.untrackKeyInput(c, this);
                keyListening = false;
            }
        }
    }

    /**
     * This method adds an entity to the list of those tracking mouse events
     * @param e The interested entity
     */
    void addAWTMouseListener(Canvas c, AWTEventListener listener) {
        synchronized (mouseListeners) {
            mouseListeners.add(listener);
            if (!mouseListening) {
                worldManager.trackMouseInput(c, this);
                mouseListening = true;
            }
        }
    }

    /**
     * This method removes an entity from the list of those tracking mouse events
     * @param e The uinterested entity
     */
    void removeAWTMouseListener(Canvas c, AWTEventListener listener) {
        synchronized (mouseListeners) {
            mouseListeners.remove(listener);
            if (mouseListeners.size() == 0) {
                worldManager.untrackMouseInput(c, this);
                mouseListening = false;
            }
        }
    }

    /**
     * An internal method to make dispatching easier
     * @param e
     */
    private void dispatchKeyEvent(AWTEvent e) {
        AWTEventListener l = null;
        synchronized (keyListeners) {
            for (int i = 0; i < keyListeners.size(); i++) {
                l = (AWTEventListener) keyListeners.get(i);
                l.eventDispatched(e);
            }
        }
        worldManager.triggerAWTEvent();
    }

    /**
     * An internal method to make dispatching easier
     * @param e
     */
    private void dispatchMouseEvent(AWTEvent e) {
        AWTEventListener l = null;
        synchronized (mouseListeners) {
            for (int i = 0; i < mouseListeners.size(); i++) {
                l = (AWTEventListener) mouseListeners.get(i);
                l.eventDispatched(e);
            }
        }
        worldManager.triggerAWTEvent();
    }

    /**
     * The methods used by AWT to notify us of mouse events
     */
    public void keyPressed(KeyEvent e) {
        dispatchKeyEvent(e);
    }

    public void keyReleased(KeyEvent e) {
        dispatchKeyEvent(e);
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("USING THE ESCAPE HATCH!");
            try {
                Toolkit tool = Toolkit.getDefaultToolkit();
                tool.setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false);
            } catch (Exception ex) {
            }
            worldManager.shutdown();
            System.exit(0);
        }
    }

    public void keyTyped(KeyEvent e) {
        dispatchKeyEvent(e);
    }

    public void mouseClicked(MouseEvent e) {
        dispatchMouseEvent(e);
    }

    public void mouseEntered(MouseEvent e) {
        dispatchMouseEvent(e);
    }

    public void mouseExited(MouseEvent e) {
        dispatchMouseEvent(e);
    }

    public void mousePressed(MouseEvent e) {
        dispatchMouseEvent(e);
    }

    public void mouseReleased(MouseEvent e) {
        dispatchMouseEvent(e);
    }

    public void mouseDragged(MouseEvent e) {
        dispatchMouseEvent(e);
    }

    public void mouseMoved(MouseEvent e) {
        dispatchMouseEvent(e);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        dispatchMouseEvent(e);
    }
}
