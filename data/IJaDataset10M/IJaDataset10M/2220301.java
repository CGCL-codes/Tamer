package drcl.comp;

import java.beans.*;

/** Defines JavaBeans properties for {@link Port}.  */
public class PortBeanInfo extends SimpleBeanInfo {

    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor pds[] = null;
        try {
            pds = new PropertyDescriptor[] { new PropertyDescriptor("groupID", Port.class), new PropertyDescriptor("ID", Port.class), new PropertyDescriptor("type", Port.class), new PropertyDescriptor("eventExportEnabled", Port.class), new PropertyDescriptor("removable", Port.class), new PropertyDescriptor("executionBoundary", Port.class), new PropertyDescriptor("sendTraceEnabled", Port.class), new PropertyDescriptor("dataTraceEnabled", Port.class), new PropertyDescriptor("shadow", Port.class) };
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return pds;
    }
}
