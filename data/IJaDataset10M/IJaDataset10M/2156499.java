package ide;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.util.WeakHashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A wrapper for the Introspector to generate and return instances
 * of BeanInfos. Creating a BeanInfo with the Introspector is a 
 * really expensive operation. Since a BeanInfo is immutable for
 * each class, it makes sense to share instances of BeanInfo's
 * throughout the application.
 *
 * @version 1.4 01/04/01
 * @author  Mark Davidson
 */
public class BeanInfoFactory {

    private static WeakHashMap infos = new WeakHashMap();

    /** 
     * Retrieves the BeanInfo for a Class
     */
    public static BeanInfo getBeanInfo(Class cls) {
        BeanInfo beanInfo = (BeanInfo) infos.get(cls);
        if (beanInfo == null) {
            try {
                beanInfo = BeanIntrospector.getBeanInfo(cls);
                infos.put(cls, beanInfo);
            } catch (IntrospectionException ex) {
                ex.printStackTrace();
            }
        }
        return beanInfo;
    }

    /** 
     * Retrieves the BeanInfo icon for the class.
     */
    public static Icon getIcon(Class cls) {
        Icon icon = null;
        if (cls == null) return null;
        BeanInfo info = BeanInfoFactory.getBeanInfo(cls);
        if (info != null) {
            Image image = info.getIcon(BeanInfo.ICON_COLOR_16x16);
            if (image != null) {
                icon = new ImageIcon(image);
            } else {
                return getIcon(cls.getSuperclass());
            }
        }
        return icon;
    }
}
