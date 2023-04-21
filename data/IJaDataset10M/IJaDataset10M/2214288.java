package kersoft.chart;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;

public class KSBarChartBeanInfo extends java.beans.SimpleBeanInfo {

    public KSBarChartBeanInfo() {
    }

    /**
	 * Gets a BeanInfo for the superclass of this bean.
	 * 
	 * @return BeanInfo[] containing this bean's superclass BeanInfo
	 */
    public BeanInfo[] getAdditionalBeanInfo() {
        try {
            BeanInfo[] bi = new BeanInfo[1];
            bi[0] = Introspector.getBeanInfo(beanClass.getSuperclass());
            return bi;
        } catch (IntrospectionException e) {
            throw new Error(e.toString());
        }
    }

    /**
	 * Gets the SymantecBeanDescriptor for this bean.
	 * 
	 * @return an object of type SymantecBeanDescriptor
	 * @see symantec.itools.beans.SymantecBeanDescriptor
	 */
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(beanClass);
        return bd;
    }

    /**
	 * Gets an image that may be used to visually represent this bean (in the toolbar, on a form, etc).
	 * 
	 * @param iconKind
	 *            the type of icon desired, one of: BeanInfo.ICON_MONO_16x16, BeanInfo.ICON_COLOR_16x16,
	 *            BeanInfo.ICON_MONO_32x32, or BeanInfo.ICON_COLOR_32x32.
	 * @return an image for this bean
	 * @see BeanInfo#ICON_MONO_16x16
	 * @see BeanInfo#ICON_COLOR_16x16
	 * @see BeanInfo#ICON_MONO_32x32
	 * @see BeanInfo#ICON_COLOR_32x32
	 */
    public java.awt.Image getIcon(int nIconKind) {
        java.awt.Image img = null;
        if (nIconKind == BeanInfo.ICON_COLOR_32x32) img = loadImage("chart.jpg");
        return img;
    }

    private static final Class<KSBarChart> beanClass = KSBarChart.class;
}
