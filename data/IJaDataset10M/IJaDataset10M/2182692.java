package com.mycila.aop;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ProxyConstruction {

    ProxyConstructor getProxyConstructor(Class<?>... parameterTypes);
}
