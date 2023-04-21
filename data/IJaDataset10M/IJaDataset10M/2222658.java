package org.icenigrid.gridsam.core.jmx;

import java.lang.reflect.Method;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

/**
 * Created by IntelliJ IDEA. User: wwhl Date: May 1, 2005 Time: 1:17:03 PM To
 * change this template use File | Settings | File Templates.
 */
public class JMXSupport {

    /**
     * get the platform MBean server
     * 
     * @return MBeanServer instance
     */
    public static MBeanServer getPlatformMBeanServer() {
        try {
            Class xManagementFactoryCls = Class.forName("java.lang.management.ManagementFactory");
            Method xMethod = xManagementFactoryCls.getMethod("getPlatformMBeanServer", new Class[] {});
            return (MBeanServer) xMethod.invoke(null, new Object[] {});
        } catch (Exception xEx) {
            return MBeanServerFactory.createMBeanServer("GridSAM");
        }
    }
}
