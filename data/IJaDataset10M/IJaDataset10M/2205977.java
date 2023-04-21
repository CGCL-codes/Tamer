package net.sf.jasperreports.engine.design;

import com.l2fprod.common.beans.BaseBeanInfo;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;

/**
 *
 * @author manningj
 */
public abstract class JRDesignElementBeanInfo extends JRBaseElementBeanInfo {

    /** Creates a new instance of JRDesignBandBeanInfo */
    protected JRDesignElementBeanInfo(Class cls) {
        super(cls);
        ExtendedPropertyDescriptor desc;
        desc = addProperty("height").setCategory("Location");
        desc = addProperty("y").setCategory("Location");
    }
}
