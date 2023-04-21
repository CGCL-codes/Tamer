package net.sf.unruly.proxy.mapping;

import java.beans.PropertyDescriptor;

public interface ProxyPropertyDescriptor {

    public ProxyClassDescriptor getProxyClassDescriptor();

    public PropertyDescriptor getProxyPropertyDescriptor();

    public PropertyDescriptor getTargetPropertyDescriptor();
}
