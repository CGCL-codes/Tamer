package org.opennms.netmgt.threshd;

import java.util.Map;
import org.opennms.netmgt.model.events.EventProxy;

/**
 * <P>
 * The Thresholder class...
 * </P>
 * 
 * @author <A HREF="mailto:mike@opennms.org">Mike </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * 
 */
public interface ServiceThresholder {

    /**
     * Status of the thresholder object.
     */
    public static final int THRESHOLDING_UNKNOWN = 0;

    public static final int THRESHOLDING_SUCCEEDED = 1;

    public static final int THRESHOLDING_FAILED = 2;

    public static final String[] statusType = { "Unknown", "THRESHOLDING_SUCCEEDED", "THRESHOLDING_FAILED" };

    public void initialize(Map parameters);

    /**
     * Called when configurations have changed and need to be refreshed at the ServiceThresolder level.  
     * Should not do a "full" initialization, but just reload any config objects that might have 
     * incorrect cached data.  It is up to the caller to call "release/initialize" for any interfaces
     * that need reinitialization, and it is recommended to do so *after* calling reinitialize(), so that
     * any objects that might be used in initializing the interfaces have been reloaded.   
     */
    public void reinitialize();

    public void release();

    public void initialize(ThresholdNetworkInterface iface, Map parameters);

    public void release(ThresholdNetworkInterface iface);

    /**
     * <P>
     * Invokes threshold checking on the object.
     * </P>
     */
    public int check(ThresholdNetworkInterface iface, EventProxy eproxy, Map parameters);
}
