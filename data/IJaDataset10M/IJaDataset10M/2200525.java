package mx4j.server.interceptor;

/**
 * Management interface for the InvokerMBeanServerInterceptor MBean
 *
 * @version $Revision: 1.6 $
 */
public interface InvokerMBeanServerInterceptorMBean {

    /**
    * Returns the type of this interceptor
    */
    public String getType();

    /**
    * This interceptor is always enabled
    */
    public boolean isEnabled();
}
