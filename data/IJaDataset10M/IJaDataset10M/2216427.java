package se.sics.tasim.visualizer.gui;

import javax.swing.*;
import javax.swing.event.*;

/**
* The <code>PositiveBoundedRangeModel</code> class is similar to a
* BoundedRangeModel, but it is implicitly limited by 0 at the lower end.
* It holds a last and a current value, where 0 <= current <= last.
*/
public class PositiveBoundedRangeModel {

    protected ChangeEvent changeEvent = null;

    protected EventListenerList listenerList = new EventListenerList();

    protected int last = 0;

    protected int current = 0;

    public PositiveBoundedRangeModel() {
    }

    /**
     * @return Last (maximum) day.
     */
    public int getLast() {
        return last;
    }

    /**
     * @param newLast Sets the last (maximum) day.
     */
    public void setLast(int newLast) {
        setDayProperties(current, newLast);
    }

    /**
     * @return Current day - an <code>int</code>.
     */
    public int getCurrent() {
        return current;
    }

    /**
     * Sets the current day in the model, and fires a change event.
     *
     * @param newCurrent The new current day.
     */
    public void setCurrent(int newCurrent) {
        setDayProperties(newCurrent, last);
    }

    /**
     * Changes the current day, and fires a change event.
     *
     * @param change Modifies the current day by adding change
     *               days to it.
     */
    public void changeCurrent(int change) {
        setDayProperties(current + change, last);
    }

    /**
     * Sets the current and last day used by the model. If the condition
     * 0 <= newCurrent <= newLast is not met, nothing is done.
     *
     * @param newCurrent an <code>int</code> value
     * @param newLast an <code>int</code> value
     */
    public void setDayProperties(int newCurrent, int newLast) {
        if (newCurrent > newLast || newCurrent < 0 || newLast < 0 || (newCurrent == current && newLast == last)) return;
        current = newCurrent;
        last = newLast;
        fireStateChanged();
    }

    /**
     * Adds a <code>ChangeListener</code> to the model. The listener is
     * notified each time the model changes.
     *
     * @param l The <code>ChangeListener</code>
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a <code>ChangeListener</code> from the model. The listener
     * will no longer be notified when the model changes.
     *
     * @param l The <code>ChangeListener</code>
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * Notifies registered <code>ChangeListeners</code>
     *
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
