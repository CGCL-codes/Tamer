package beantable;

import java.awt.Image;
import java.beans.*;

public class DelegatingBeanInfo implements BeanInfo {

    private BeanInfo delegate;

    public DelegatingBeanInfo() {
        super();
    }

    public DelegatingBeanInfo(final BeanInfo delegate) {
        super();
        this.setDelegate(delegate);
    }

    public BeanInfo getDelegate() {
        return this.delegate;
    }

    public void setDelegate(final BeanInfo delegate) {
        this.delegate = delegate;
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        return this.delegate.getAdditionalBeanInfo();
    }

    public BeanDescriptor getBeanDescriptor() {
        return this.delegate.getBeanDescriptor();
    }

    public int getDefaultEventIndex() {
        return this.delegate.getDefaultEventIndex();
    }

    public int getDefaultPropertyIndex() {
        return this.delegate.getDefaultPropertyIndex();
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        return this.delegate.getEventSetDescriptors();
    }

    public Image getIcon(final int iconKind) {
        return this.delegate.getIcon(iconKind);
    }

    public MethodDescriptor[] getMethodDescriptors() {
        return this.delegate.getMethodDescriptors();
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        return this.delegate.getPropertyDescriptors();
    }
}
