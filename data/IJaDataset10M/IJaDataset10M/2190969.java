package org.das2.beans;

import org.das2.beans.AccessLevelBeanInfo.AccessLevel;
import org.das2.beans.AccessLevelBeanInfo.Property;
import org.das2.graph.DasAxis;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;

/**
 * BeanInfo class for DasColorBar
 *
 * @author Edward West
 */
public class AttachedLabelBeanInfo extends AccessLevelBeanInfo {

    protected static final Property[] properties = { new Property("label", AccessLevel.DASML, PersistenceLevel.PERSISTENT, "getLabel", "setLabel", null), new Property("orientation", AccessLevel.DASML, PersistenceLevel.PERSISTENT, "getOrientation", "setOrientation", null), new Property("emOffset", AccessLevel.DASML, PersistenceLevel.PERSISTENT, "getEmOffet", "setEmOffset", null) };

    public AttachedLabelBeanInfo() {
        super(properties, org.das2.graph.AttachedLabel.class);
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        BeanInfo[] additional = { new DasCanvasComponentBeanInfo() };
        return additional;
    }
}
