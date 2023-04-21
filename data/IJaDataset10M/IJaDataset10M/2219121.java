package net.sourceforge.squirrel_sql.fw.util.beanwrapper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * This is the <CODE>BeanInfo</CODE> class for <CODE>URLWrapper</CODE>.
 * 
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class URLWrapperBeanInfo extends SimpleBeanInfo {

    /**
	 * See http://tinyurl.com/63no6t for discussion of the proper thread-safe way to implement
	 * getPropertyDescriptors().
	 * 
	 * @see java.beans.SimpleBeanInfo#getPropertyDescriptors()
	 */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] s_descriptors = new PropertyDescriptor[1];
            s_descriptors[0] = new PropertyDescriptor(URLWrapper.IURLWrapperPropertyNames.URL, URLWrapper.class, "getExternalForm", "setExternalForm");
            return s_descriptors;
        } catch (IntrospectionException e) {
            throw new Error(e);
        }
    }
}
