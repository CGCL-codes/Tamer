package uk.co.brunella.osgi.bdt.osgitestrunner;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class OSGiTestCaseUtils {

    private static final int WAIT_TIME = 250;

    /**
   * Registers a <code>IntegrationTestCase</code> and runs the tests.
   * 
   * @param context     a bundle context
   * @param testSuite   the test suite
   */
    public static void run(BundleContext context, OSGiTestCase testSuite) {
        context.registerService(OSGiTestCase.class.getName(), testSuite, null);
    }

    /**
   * Waits till a service reference for a given service class is available or
   * the request times out.
   * 
   * @param bundleContext   a bundle context
   * @param serviceClass    the service class name
   * @param timeout         timeout in milli seconds
   * @return                a service reference or null
   */
    public static ServiceReference waitForServiceReference(BundleContext bundleContext, String serviceClass, long timeout) {
        ServiceReference reference = bundleContext.getServiceReference(serviceClass);
        while ((null == reference) && (0 < timeout)) {
            try {
                timeout -= WAIT_TIME;
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
            }
            reference = bundleContext.getServiceReference(serviceClass);
        }
        return reference;
    }

    /**
   * Waits till service references for a given service class and filter are 
   * available or the request times out.
   * 
   * @param bundleContext   a bundle context
   * @param serviceClass    the service class name
   * @param filter          a filter or null
   * @param timeout         timeout in milli seconds
   * @return                an array of service references or null
   */
    public static ServiceReference[] waitForServiceReferences(BundleContext bundleContext, String serviceClass, String filter, long timeout) {
        try {
            ServiceReference[] references;
            references = bundleContext.getServiceReferences(serviceClass, filter);
            while ((references == null) && (timeout > 0)) {
                try {
                    timeout -= WAIT_TIME;
                    Thread.sleep(WAIT_TIME);
                } catch (InterruptedException e) {
                }
                references = bundleContext.getServiceReferences(serviceClass, filter);
            }
            return references;
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
   * Waits till a service from a service tracker is available or the request
   * times out.
   * 
   * @param serviceTracker  a service tracker
   * @param timeout         timeout in milli seconds
   * @return                a service object or null
   */
    public static Object waitForService(ServiceTracker serviceTracker, long timeout) {
        Object service = serviceTracker.getService();
        while (service == null && timeout > 0) {
            try {
                timeout -= WAIT_TIME;
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
            }
            service = serviceTracker.getService();
        }
        return service;
    }

    /**
   * Waits till a service for a given service class is available or the request
   * times out.
   * 
   * @param bundleContext   a bundle context
   * @param serviceClass    the service class name
   * @param timeout         timeout in milli seconds
   * @return                a service object or null
   */
    public static Object waitForService(BundleContext bundleContext, String serviceClass, long timeout) {
        ServiceReference reference = waitForServiceReference(bundleContext, serviceClass, timeout);
        if (reference != null) {
            return bundleContext.getService(reference);
        } else {
            return null;
        }
    }

    /**
   * Waits till a service for a given service class and filter is available or 
   * the request times out. If more than one service is available the first
   * service will be returned.
   * 
   * @param bundleContext   a bundle context
   * @param serviceClass    the service class name
   * @param filter          a filter or null
   * @param timeout         timeout in milli seconds
   * @return                a service object or null
   */
    public static Object waitForService(BundleContext bundleContext, String serviceClass, String filter, long timeout) {
        ServiceReference[] references = waitForServiceReferences(bundleContext, serviceClass, filter, timeout);
        if (references != null) {
            return bundleContext.getService(references[0]);
        } else {
            return null;
        }
    }
}
