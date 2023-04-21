package de.schlund.marwein.tasks;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author <a href="mailto:up@schlund.de">ute</a>
 */
public abstract class AbstractTaskBeanInfo extends SimpleBeanInfo {

    private final BeanDescriptor bd;

    protected AbstractTaskBeanInfo() {
        bd = new BeanDescriptor(this.getClass());
        bd.setExpert(false);
        bd.setHidden(false);
        bd.setName(this.getClass().getName());
        bd.setDisplayName(this.getClass().getName());
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return bd;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        List<PropertyDescriptor> l = new LinkedList<PropertyDescriptor>();
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor("name", AbstractTask.class);
            pd.setDisplayName("Name");
            l.add(pd);
            pd = new PropertyDescriptor("enabled", AbstractTask.class);
            pd.setDisplayName("Enabled");
            l.add(pd);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        getPDList(l);
        PropertyDescriptor[] p = new PropertyDescriptor[l.size()];
        return l.toArray(p);
    }

    protected abstract void getPDList(List<PropertyDescriptor> l);
}
