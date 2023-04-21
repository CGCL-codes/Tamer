package org.das2.beans;

import org.das2.components.propertyeditor.EnumerationEditor;
import java.beans.BeanInfo;

/**
 * BeanInfo class for DasColorBar
 *
 * @author Edward West
 */
public class RowRowConnectorBeanInfo extends AccessLevelBeanInfo {

    protected static final Property[] properties = {};

    public RowRowConnectorBeanInfo() {
        super(properties, org.das2.graph.RowRowConnector.class);
    }
}
