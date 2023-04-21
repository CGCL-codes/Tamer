package org.apache.webbeans.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.webbeans.Dependent;
import javax.webbeans.manager.Bean;
import javax.webbeans.manager.Manager;
import org.apache.webbeans.container.ManagerImpl;
import org.apache.webbeans.context.DependentContext;

public class WebBeansELResolver extends ELResolver {

    @Override
    public Class<?> getCommonPropertyType(ELContext arg0, Object arg1) {
        return null;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext arg0, Object arg1) {
        return null;
    }

    @Override
    public Class<?> getType(ELContext arg0, Object arg1, Object arg2) throws NullPointerException, PropertyNotFoundException, ELException {
        return null;
    }

    @Override
    public Object getValue(ELContext context, Object obj, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
        Manager manager = ManagerImpl.getManager();
        Object object = null;
        DependentContext dependentContext = null;
        Bean<?> bean = null;
        boolean isActiveSet = false;
        boolean isResolution = false;
        try {
            if (obj == null) {
                isResolution = true;
                dependentContext = (DependentContext) ManagerImpl.getManager().getContext(Dependent.class);
                if (!dependentContext.isActive()) {
                    dependentContext.setActive(true);
                    isActiveSet = true;
                }
                String name = (String) property;
                object = manager.getInstanceByName(name);
                context.setPropertyResolved(true);
                bean = manager.resolveByName(name).iterator().next();
            }
        } finally {
            if (isResolution) {
                if (bean != null) {
                    destroyBean(bean, object);
                }
                if (isActiveSet) {
                    dependentContext.setActive(false);
                }
            }
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    private <T> void destroyBean(Bean<?> bean, Object instance) {
        Bean<T> destroy = (Bean<T>) bean;
        if (destroy.getScopeType().equals(Dependent.class)) {
            T inst = (T) instance;
            destroy.destroy(inst);
        }
    }

    @Override
    public boolean isReadOnly(ELContext arg0, Object arg1, Object arg2) throws NullPointerException, PropertyNotFoundException, ELException {
        return false;
    }

    @Override
    public void setValue(ELContext arg0, Object arg1, Object arg2, Object arg3) throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException {
    }
}
