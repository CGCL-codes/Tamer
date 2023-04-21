package org.jfree.beans;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;

/**
 * Bean info for the {@link JLineChart} bean.
 */
public class JLineChartBeanInfo extends SimpleBeanInfo {

    /** The resourceBundle for the localization. */
    static ResourceBundle localizationResources = ResourceBundle.getBundle("org.jfree.beans.LocalizationBundle");

    /**
     * Returns the bean info from the super classes.
     * 
     * @return Additional bean info.
     */
    public BeanInfo[] getAdditionalBeanInfo() {
        return new BeanInfo[] { new AbstractChartBeanInfo(), new AbstractXYChartBeanInfo(), new NumericalXYChartBeanInfo() };
    }

    /**
     * Returns some property descriptors.
     * 
     * @return The property descriptors.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        final String prefix = "JLineChart.";
        Object[][] props = new Object[][] { { "dataset", Boolean.FALSE, "Category.Dataset" }, { "shapesVisible", Boolean.FALSE, "Category.Renderer" } };
        List pds = new java.util.ArrayList();
        for (int i = 0; i < props.length; i++) {
            try {
                String name = (String) props[i][0];
                PropertyDescriptor pd = new PropertyDescriptor(name, JLineChart.class);
                Boolean expert = (Boolean) props[i][1];
                pd.setExpert(expert.booleanValue());
                String desc = localizationResources.getString(prefix + name);
                pd.setShortDescription(desc);
                pd.setValue("category", localizationResources.getString((String) props[i][2]));
                pds.add(pd);
            } catch (IntrospectionException e) {
            }
        }
        PropertyDescriptor[] result = new PropertyDescriptor[pds.size()];
        for (int i = 0; i < pds.size(); i++) {
            result[i] = (PropertyDescriptor) pds.get(i);
        }
        return result;
    }

    @Override
    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_COLOR_16x16) {
            URL imageURL = this.getClass().getClassLoader().getResource("org/jfree/beans/line16.png");
            if (imageURL != null) {
                ImageIcon temp = new ImageIcon(imageURL);
                return temp.getImage();
            } else {
                return super.getIcon(iconKind);
            }
        } else {
            return super.getIcon(iconKind);
        }
    }
}
