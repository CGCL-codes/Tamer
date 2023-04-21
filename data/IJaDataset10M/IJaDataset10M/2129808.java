package org.das2.beans;

import java.beans.BeanInfo;

/**
 * BeanInfo class for org.das2.graph.DasAxis.
 *
 * @author Edward West
 * @see org.das2.graph.DasAxis
 */
public class DasLabelAxisBeanInfo extends AccessLevelBeanInfo {

    private static final Property[] properties = { new Property("label", AccessLevel.DASML, "getLabel", "setLabel", null), new Property("outsidePadding", AccessLevel.DASML, "getOutsidePadding", "setOutsidePadding", null), new Property("floppyItemSpacing", AccessLevel.DASML, "isFloppyItemSpacing", "setFloppyItemSpacing", null), new Property("tickLabelsVisible", AccessLevel.DASML, "areTickLabelsVisible", "setTickLabelsVisible", null), new Property("oppositeAxisVisible", AccessLevel.DASML, "isOppositeAxisVisible", "setOppositeAxisVisible", null) };

    public DasLabelAxisBeanInfo() {
        super(properties, org.das2.graph.DasLabelAxis.class);
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        java.beans.BeanInfo[] additional = { new DasCanvasComponentBeanInfo() };
        return additional;
    }
}
