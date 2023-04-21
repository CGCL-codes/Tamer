package org.opennms.netmgt.inventory;

/**
 * <P>The Pollable class...</P>
 * 
 * @author <A HREF="mailto:mike@opennms.org">Mike</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS</A>
 *
 */
public interface Pollable {

    /** 
	 * Status of the pollable object.
	 * 
	 * WARNING:  Don't change the integer value of these constants.
	 *           They are set up to correspond to the integer values
	 * 	     of the GroupMonitor.DOWNLOAD_SUCCESS, and
	 *  	     GroupMonitor.DOWNLOAD_FAILURE, 
	 */
    public static final int STATUS_UNKNOWN = 0;

    public static final int STATUS_UP = 1;

    public static final int STATUS_DOWN = 2;

    public static final String[] statusType = { "Unknown", "Up", "Down" };

    public boolean statusChanged();

    /**
	 * Returns current status of the object 
	 */
    public int getStatus();
}
