package org.jrichclient.richdock.dockable;

import static org.jrichclient.richdock.utils.PropertyDescriptorFactory.*;
import java.beans.BeanDescriptor;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.EventSetDescriptor;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.List;

public class BasicDockableBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        List<PropertyDescriptor> descriptorList = new ArrayList<PropertyDescriptor>();
        addDockablePropertyDescriptors(descriptorList, BasicDockable.class);
        descriptorList.add(createPropertyDescriptor(BasicDockable.class, "content", "getContent", "setContent", BOUND, NOT_CONSTRAINED, TRANSIENT));
        return createPropertyDescriptorArray(descriptorList);
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return new EventSetDescriptor[] {};
    }

    private static PersistenceDelegate DELEGATE = new Delegate();

    private static class Delegate extends DefaultPersistenceDelegate {

        @Override
        protected Expression instantiate(Object oldInstance, Encoder out) {
            BasicDockable oldDockable = (BasicDockable) oldInstance;
            return new Expression(oldDockable, BasicDockable.class, "new", new Object[] { oldDockable.getContent() });
        }
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor descriptor = new BeanDescriptor(BasicDockable.class);
        descriptor.setValue("persistenceDelegate", DELEGATE);
        return descriptor;
    }
}
