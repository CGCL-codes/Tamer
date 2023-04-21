package net.butov.wrappers;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Maxim Butov
 * @version $Id$
 */
public class WrapperMethodInterceptor implements MethodInterceptor, Serializable {

    private Object source;

    private Class wrapperClass;

    private WrapperMode mode;

    public WrapperMethodInterceptor(Object source, Class wrapperClass, WrapperMode mode) {
        this.source = source;
        this.wrapperClass = wrapperClass;
        this.mode = mode;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (Wrapper.class.equals(method.getDeclaringClass()) && Wrapper.GET_SOURCE.equals(method.getName())) {
            return source;
        } else if (mode.shouldWrap(wrapperClass, method) || source == null) {
            return methodProxy.invokeSuper(proxy, args);
        } else {
            return methodProxy.invoke(source, args);
        }
    }
}
