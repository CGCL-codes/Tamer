package com.codename1.ui.spinner;

import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.util.EventDispatcher;

/**
 * Represents a numeric model for the spinner
 *
 * @author Shai Almog
 */
class SpinnerNumberModel implements ListModel {

    private EventDispatcher dataListener = new EventDispatcher();

    private EventDispatcher selectionListener = new EventDispatcher();

    private double min;

    private double max;

    private double currentValue;

    private double step;

    boolean realValues;

    void setValue(Object value) {
        if (value instanceof Integer) {
            currentValue = ((Integer) value).doubleValue();
        } else {
            currentValue = ((Double) value).doubleValue();
        }
        selectionListener.fireSelectionEvent(-1, -1);
    }

    Object getValue() {
        if (realValues) {
            return new Double(currentValue);
        }
        return new Integer((int) currentValue);
    }

    /**
     * Indicates the range of the spinner
     * 
     * @param min lowest value allowed
     * @param max maximum value allowed
     * @param currentValue the starting value for the mode
     * @param step the value by which we increment the entries in the model
     */
    public SpinnerNumberModel(int min, int max, int currentValue, int step) {
        this.max = max;
        this.min = min;
        this.currentValue = currentValue;
        this.step = step;
    }

    /**
     * Indicates the range of the spinner
     *
     * @param min lowest value allowed
     * @param max maximum value allowed
     * @param currentValue the starting value for the mode
     * @param step the value by which we increment the entries in the model
     */
    public SpinnerNumberModel(double min, double max, double currentValue, double step) {
        this.max = max;
        this.min = min;
        this.currentValue = currentValue;
        this.step = step;
        realValues = true;
    }

    /**
     * @inheritDoc
     */
    public Object getItemAt(int index) {
        if (realValues) {
            return new Double(min + step * index);
        }
        return new Integer((int) (min + step * index));
    }

    /**
     * @inheritDoc
     */
    public int getSize() {
        return (int) ((max - min) / step);
    }

    /**
     * @inheritDoc
     */
    public int getSelectedIndex() {
        double d = Math.floor((max - currentValue) / step + 0.5);
        int v = getSize() - (int) d;
        return v;
    }

    /**
     * @inheritDoc
     */
    public void setSelectedIndex(int index) {
        int oldIndex = getSelectedIndex();
        currentValue = min + index * step;
        int newIndex = getSelectedIndex();
        selectionListener.fireSelectionEvent(oldIndex, newIndex);
    }

    /**
     * @inheritDoc
     */
    public void addDataChangedListener(DataChangedListener l) {
        dataListener.addListener(l);
    }

    /**
     * @inheritDoc
     */
    public void removeDataChangedListener(DataChangedListener l) {
        dataListener.removeListener(l);
    }

    /**
     * @inheritDoc
     */
    public void addSelectionListener(SelectionListener l) {
        selectionListener.addListener(l);
    }

    /**
     * @inheritDoc
     */
    public void removeSelectionListener(SelectionListener l) {
        selectionListener.removeListener(l);
    }

    /**
     * @inheritDoc
     */
    public void addItem(Object item) {
    }

    /**
     * @inheritDoc
     */
    public void removeItem(int index) {
    }

    /**
     * @return the min
     */
    public double getMin() {
        return min;
    }

    /**
     * @return the max
     */
    public double getMax() {
        return max;
    }
}
