package jaxlib.beans.dynamic;

/**
 *
 * @author jw
 */
public interface DynamicBeanInvocationHandlerFactory {

    public DynamicBeanInvocationHandler createDynamicBeanInvocationHandler(final String objectName);
}
