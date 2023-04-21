package vavi.swing.layout;

import java.awt.FlowLayout;
import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.logging.Level;
import vavi.util.Debug;

/**
 * FlowLayoutInfo.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020527 nsano initial version <br>
 */
public class FlowLayoutInfo extends SimpleBeanInfo {

    private final Class<?> clazz = FlowLayout.class;

    private final Class<?> customizerClass = FlowLayoutCustomizer.class;

    /** */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(clazz, customizerClass);
    }

    /** */
    public Image getIcon(int iconKind) {
        return loadImage("resources/flowLayout.gif");
    }

    /** */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] pds = new PropertyDescriptor[3];
            pds[0] = new PropertyDescriptor("hgap", clazz);
            pds[1] = new PropertyDescriptor("vgap", clazz);
            pds[2] = new PropertyDescriptor("alignment", clazz);
            Object[] value = new Object[] { "LEADING", new Integer(FlowLayout.LEADING), null, "CENTER", new Integer(FlowLayout.CENTER), null, "RIGHT", new Integer(FlowLayout.RIGHT), null, "LEFT", new Integer(FlowLayout.LEFT), null, "TRAILING", new Integer(FlowLayout.TRAILING), null };
            pds[2].setValue("enumerationValues", value);
            return pds;
        } catch (IntrospectionException e) {
            Debug.println(Level.SEVERE, e);
            return null;
        }
    }
}
