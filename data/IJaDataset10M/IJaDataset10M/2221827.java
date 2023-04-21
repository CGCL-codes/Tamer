package touchMenu.touchbutton;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultButtonBeanInfo extends SimpleBeanInfo {

    public DefaultButtonBeanInfo() {
        super();
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor myBeanDescriptor;
        myBeanDescriptor = new BeanDescriptor(DefaultButton.class);
        myBeanDescriptor.setDisplayName("DefaultButton");
        return myBeanDescriptor;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor pDesc1 = new PropertyDescriptor("text", DefaultButton.class);
            PropertyDescriptor pDesc2 = new PropertyDescriptor("icon", DefaultButton.class);
            PropertyDescriptor pDesc3 = new PropertyDescriptor("opacity", DefaultButton.class);
            PropertyDescriptor pDesc4 = new PropertyDescriptor("iconOpacity", DefaultButton.class);
            PropertyDescriptor[] myPropertyDescriptor = { pDesc1, pDesc2, pDesc3, pDesc4 };
            return myPropertyDescriptor;
        } catch (IntrospectionException ex) {
            Logger.getLogger(DefaultButtonBeanInfo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
