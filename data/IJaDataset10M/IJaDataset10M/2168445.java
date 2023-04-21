package org.das2.beans;

public class DataPointRecorderBeanInfo extends AccessLevelBeanInfo {

    private static final Property[] properties = { new Property("xTagWidth", AccessLevel.DASML, "getXTagWidth", "setXTagWidth", null), new Property("snapToGrid", AccessLevel.DASML, "isSnapToGrid", "setSnapToGrid", null), new Property("sorted", AccessLevel.DASML, "isSorted", "setSorted", null) };

    public DataPointRecorderBeanInfo() {
        super(properties, org.das2.components.DataPointRecorder.class);
    }
}
