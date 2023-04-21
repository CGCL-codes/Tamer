package net.jtank.input.managers;

import java.util.ArrayList;
import java.util.List;
import net.jtank.input.MouseDevice;
import net.jtank.input.MouseListener;
import net.jtank.input.MouseMovedDeltaEvent;
import net.jtank.input.MouseMovedEvent;
import net.jtank.input.MousePressedEvent;
import net.jtank.input.MouseReleasedEvent;
import net.jtank.input.MouseWheelEvent;

/**
 * Looks for the mouse beeing stopped for a specified amount of milliseconds.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class MouseStopManager extends Thread implements MouseListener {

    private long t0;

    private long dt = 500L;

    private MouseDevice lastMouse = null;

    private int lastX = -1, lastY = -1;

    private int lastButtonsState = 0;

    private long lastMovedTime = -1L;

    private boolean isSearching = false;

    private List<MouseStopListener> listeners = new ArrayList<MouseStopListener>();

    /**
     * Registers a listener to recieve mouse stop events from this device.
     * 
     * @param listener the litener to register
     * @return true if this set did not already contain the specified element.
     */
    public boolean registerListener(MouseStopListener listener) {
        return listeners.add(listener);
    }

    /**
     * Deregisters a listener so it no longer recieves mouse stop events from this manager.
     * 
     * @param listener the listener to deregister
     * @return true if the set contained the specified element.
     */
    public boolean deregisterListener(MouseStopListener listener) {
        return listeners.remove(listener);
    }

    protected void notifyListeners(MouseDevice mouse, int x, int y, int buttonsState, long when) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onMouseStopped(mouse, x, y, buttonsState, when);
        }
    }

    /**
     * Sets the amount of milliseconds after the last mouse moving
     * for the onMouseStopped method to be called.
     */
    public void setDelay(long delay) {
        dt = delay;
    }

    /**
     * Returns the amount of milliseconds after the last mouse moving
     * for the onMouseStopped method to be called.
     */
    public long getDelay() {
        return dt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        long t;
        isSearching = true;
        while (isSearching) {
            t = System.currentTimeMillis();
            if (t0 + dt <= t) {
                t0 = Long.MAX_VALUE;
                notifyListeners(lastMouse, lastX, lastY, lastButtonsState, lastMovedTime);
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        lastMouse = null;
        lastX = -1;
        lastY = -1;
        lastButtonsState = 0;
        lastMovedTime = -1L;
        t0 = Long.MAX_VALUE;
        super.start();
    }

    public void stopMe() {
        isSearching = false;
    }

    /**
     * {@inheritDoc}
     */
    public void onMouseMoved(MouseMovedEvent e) {
        lastMouse = e.getMouse();
        lastX = e.getX();
        lastY = e.getY();
        lastButtonsState = e.getButtonsState();
        lastMovedTime = e.getWhen();
        t0 = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    public void onMouseMovedDelta(MouseMovedDeltaEvent e) {
        lastMouse = e.getMouse();
        lastX = e.getDX();
        lastY = e.getDY();
        lastButtonsState = e.getButtonsState();
        lastMovedTime = e.getWhen();
        t0 = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    public void onMouseButtonPressed(MousePressedEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void onMouseButtonReleased(MouseReleasedEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void onMouseWheelMoved(MouseWheelEvent e) {
    }
}
