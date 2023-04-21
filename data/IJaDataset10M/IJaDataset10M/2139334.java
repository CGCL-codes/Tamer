package com.nexirius.framework.datamodel;

import java.awt.*;

/**
 * This is the event multicaster class to support the DataModelListener interface.
 */
public class DataModelEventMulticaster extends AWTEventMulticaster implements DataModelListener {

    /**
     * Constructor to support multicast events.
     *
     * @param a DataModelListener
     * @param b DataModelListener
     */
    protected DataModelEventMulticaster(DataModelListener a, DataModelListener b) {
        super(a, b);
    }

    /**
     * Add new listener to support multicast events.
     *
     * @param a DataModelListener
     * @param b DataModelListener
     * @return DataModelListener
     */
    public static DataModelListener add(DataModelListener a, DataModelListener b) {
        if (a == null) return b;
        if (b == null) return a;
        return new DataModelEventMulticaster(a, b);
    }

    /**
     * Remove listener to support multicast events.
     *
     * @param a DataModelListener
     * @param b DataModelListener
     * @return DataModelListener
     */
    public static DataModelListener remove(DataModelListener a, DataModelListener b) {
        return (DataModelListener) removeInternal(a, b);
    }

    public void dataModelChangeValue(DataModelEvent event) {
        ((DataModelListener) a).dataModelChangeValue(event);
        ((DataModelListener) b).dataModelChangeValue(event);
    }

    public void dataModelChangeStructure(DataModelEvent event) {
        ((DataModelListener) a).dataModelChangeStructure(event);
        ((DataModelListener) b).dataModelChangeStructure(event);
    }

    public void dataModelGrabFocus(DataModelEvent event) {
        ((DataModelListener) a).dataModelGrabFocus(event);
        ((DataModelListener) b).dataModelGrabFocus(event);
    }
}
