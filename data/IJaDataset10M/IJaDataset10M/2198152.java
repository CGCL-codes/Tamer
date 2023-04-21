package org.jcvi.vics.web.gwt.common.client.ui;

/**
 * @author Tareq Nabeel
 */
public class Index {

    private int currentValue;

    public Index(int startingIndex) {
        currentValue = startingIndex;
    }

    public Index increment() {
        currentValue++;
        return this;
    }

    public int getCurrentValue() {
        return currentValue;
    }
}
