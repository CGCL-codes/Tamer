package com.aurorasoftworks.signal.runtime.ui.mvc;

import com.aurorasoftworks.signal.runtime.core.context.proxy.AbstractProxy;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IProxyClass;
import com.aurorasoftworks.signal.runtime.core.context.proxy.ProxyFactory;

public class MockControllerProxyFactory extends ProxyFactory {

    @Override
    protected AbstractProxy doCreateProxy(Class targetClass) {
        return new MockController__Proxy();
    }

    public IProxyClass getProxyClass(Class targetClass) {
        return new MockController__ProxyClass();
    }
}
