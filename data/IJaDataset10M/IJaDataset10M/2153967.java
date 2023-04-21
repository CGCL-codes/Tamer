package org.opennms.netmgt.provision.support.ssh;

import java.util.Map;
import org.opennms.netmgt.model.PollStatus;

/**
 * <p>
 * This is the interface that must be implemented by each poller plugin in the
 * framework. This well defined interface allows the framework to treat each
 * plugin identically.
 * </p>
 * 
 * <p>
 * When a service monitor plug-in is loaded and initialized, the framework will
 * initialize the monitor by calling the <EM>initialize()</EM> method.
 * Likewise, when the monitor is unloaded the framework calls the <EM>release()
 * </EM> method is called. If the plug-in needs to save or read any
 * configuration information after the initialize() call, a reference to the
 * proxy object should be saved at initialization.
 * </p>
 * 
 * <P>
 * <STRONG>NOTE: </STRONG> The plug-in <EM>poll()</EM> must be thread safe in
 * order to operate. Any synchronized methods or data accessed in the <EM>
 * poll()</EM> can negatively affect the framework if multiple poller threads
 * are blocked on a critical resource. Synchronization issues should be
 * seriously evaluated to ensure that the plug-in scales well to large
 * deployments.
 * </P>
 * 
 * @author <A HREF="mailto:weave@oculan.com">Brian Weaver </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * 
 */
public interface ServiceMonitor {

    /**
     * <P>
     * This method is called after the framework creates an instance of the
     * plug-in. The framework passes the object a proxy object that can be used
     * to retrieve configuration information specific to the plug-in.
     * Additionally, any parameters for the plug-in from the package definition
     * are passed using the parameters element.
     * </P>
     * 
     * <P>
     * If there is a critical error, like missing service libraries, the the
     * Monitor may throw a ServiceMonitorException. If the plug-in throws an
     * exception then the plug-in will be disabled in the framework.
     * </P>
	 * @param parameters
     *            Not currently used
     * 
     * @exception java.lang.RuntimeException
     *                Thrown if an unrecoverable error occurs that prevents the
     *                plug-in from functioning.
     * 
     */
    @SuppressWarnings("unchecked")
    public void initialize(Map parameters);

    /**
     * <P>
     * This method is called whenever the plug-in is being unloaded, normally
     * during framework exit. During this time the framework may release any
     * resource and save any state information using the proxy object from the
     * initialization routine.
     * </P>
     * 
     * <P>
     * Even if the plug-in throws a monitor exception, it will not prevent the
     * plug-in from being unloaded. The plug-in should not return until all of
     * its state information is saved. Once the plug-in returns from this call
     * its configuration proxy object is considered invalid.
     * </P>
     * 
     * @exception java.lang.RuntimeException
     *                Thrown if an error occurs during deallocation.
     * 
     */
    public void release();

    /**
     * <P>
     * This method is called whenever a new interface that supports the plug-in
     * service is added to the scheduling system. The plug-in has the option to
     * load and/or associate configuration information with the interface before
     * the framework begins scheduling the new device.
     * </P>
     * 
     * <P>
     * Should a monitor exception be thrown during an initialization call then
     * the framework will log an error and discard the interface from
     * scheduling.
     * </P>
     * @param svc TODO
     * 
     * @exception java.lang.RuntimeException
     *                Thrown if an unrecoverable error occurs that prevents the
     *                interface from being monitored.
     * @exception
     *                org.opennms.netmgt.poller.NetworkInterfaceNotSupportedException
     *                Thrown if the passed interface is invalid for this
     *                monitor.
     * 
     */
    public void initialize(MonitoredService svc);

    /**
     * <P>
     * This method is the called whenever an interface is being removed from the
     * scheduler. For example, if a service is determined as being no longer
     * supported then this method will be invoked to cleanup any information
     * associated with this device. This gives the implementor of the interface
     * the ability to serialize any data prior to the interface being discarded.
     * </P>
     * 
     * <P>
     * If an exception is thrown during the release the exception will be
     * logged, but the interface will still be discarded for garbage collection.
     * </P>
     * @param svc TODO
     * 
     * @exception java.lang.RuntimeException
     *                Thrown if an unrecoverable error occurs that prevents the
     *                interface from being monitored.
     */
    public void release(MonitoredService svc);

    /**
     * <P>
     * This method is the heart of the plug-in monitor. Each time an interface
     * requires a check to be performed as defined by the scheduler the poll
     * method is invoked. The poll is passed the interface to check
     * </P>
     * 
     * <P>
     * By default when the status transition from up to down or vice versa the
     * framework will generate an event. Additionally, if the polling interval
     * changes due to an extended unavailability, the framework will generate an
     * additional down event. The plug-in can suppress the generation of the
     * default events by setting the suppress event bit in the returned integer.
     * </P>
     * @param svc TODO
     * @param parameters
     *            The package parameters (timeout, retry, etc...) to be used for
     *            this poll.
     * @return The availability of the interface and if a transition event
     *         should be suppressed.
     * 
     * @exception java.lang.RuntimeException
     *                Thrown if an unrecoverable error occurs that prevents the
     *                interface from being monitored.
     * 
     * @see PollStatus#SERVICE_AVAILABLE
     * @see PollStatus#SERVICE_UNAVAILABLE
     */
    @SuppressWarnings("unchecked")
    public PollStatus poll(MonitoredService svc, Map parameters);
}
