package org.tinymarbles.impl.proxy;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author duke
 * 
 */
public class WrappingInterceptor implements MethodInterceptor {

    protected static final Logger LOGGER = Logger.getLogger(WrappingInterceptor.class);

    private final ProxyWrapper wrapper;

    public WrappingInterceptor(final ProxyWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public ProxyWrapper getWrapper() {
        return this.wrapper;
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        boolean objectMethod = (args.length == 0 && (method.getName().equals("toString") || method.getName().equals("finalize")));
        if (!objectMethod && LOGGER.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder(">>> ").append(method);
            if (args.length > 0) {
                sb.append(" with args: [");
                for (int i = 0; i < args.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(args[i]);
                }
                sb.append("]");
            }
            LOGGER.debug(sb.toString());
        }
        Object[] wArgs = (args.length == 0 ? args : this.wrapper.unwrap(args));
        Object result = this.invoke(obj, wArgs, proxy);
        if (!objectMethod && Object.class.isAssignableFrom(method.getReturnType())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("unwrapped result of " + proxy.getSignature() + ":" + result);
            }
            result = wrapResult(obj, result);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("wrapped result of " + proxy.getSignature() + ":" + result);
            }
        } else {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(proxy.getSignature() + " finished");
            }
        }
        return result;
    }

    /**
	 * Wraps the source object if needed
	 * @param obj the proxy what's being intercepted
	 * @param source the result of the method call 
	 * @return the wrapped result
	 */
    protected Object wrapResult(Object obj, Object source) {
        return (source == obj ? obj : this.wrapper.wrapIfNeeded(source));
    }

    /**
	 * Invokes the correct method on the object.
	 * 
	 * @param obj the proxy object
	 * @param args the unwrapped arguments
	 * @param proxy the method proxy
	 * @return the result of the invocation
	 * @throws Throwable if the invocation throws a Throwable
	 */
    protected Object invoke(Object obj, Object[] args, MethodProxy proxy) throws Throwable {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("invoking " + proxy.getSignature() + " on the superclass...");
        }
        return proxy.invokeSuper(obj, args);
    }
}
