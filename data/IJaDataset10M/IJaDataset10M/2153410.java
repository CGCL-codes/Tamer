package net.assimilator.qos.capability.system;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import net.assimilator.core.ServiceLevelAgreements.SystemRequirement;
import net.assimilator.qos.capability.PlatformCapability;

/**
 * The <code>StorageCapability</code> object provides a definition of storage
 * support
 */
public class StorageCapability extends PlatformCapability implements Observer {

    static final long serialVersionUID = 1L;

    static final String DEFAULT_DESCRIPTION = "Storage Capability";

    /** Storage media type */
    public static final String TYPE = "StorageType";

    /** Storage availability  */
    public static final String AVAILABLE = "Available";

    /** Storage capacity  */
    public static final String CAPACITY = "Capacity";

    /** The availabile disk space */
    private int available = 0;

    /** 
     * Create a StorageCapability 
     */
    public StorageCapability() {
        this(DEFAULT_DESCRIPTION);
    }

    /** 
     * Create a StorageCapability with a description
     */
    public StorageCapability(String description) {
        this.description = description;
    }

    /**
     * Override parents define method to create value for the capacity
     */
    public void define(Object key, Object value) {
        if (key.equals(CAPACITY)) {
            try {
                Double dCap = new Double((String) value);
                capabilities.put(CAPACITY, dCap);
                return;
            } catch (NumberFormatException e) {
                System.out.println("Bad value for Capacity");
                e.printStackTrace();
                return;
            }
        }
        if (key.equals(AVAILABLE)) {
            try {
                Integer iAvail = new Integer((String) value);
                available = iAvail.intValue();
                capabilities.put(AVAILABLE, iAvail);
                return;
            } catch (NumberFormatException e) {
                System.out.println("Bad value for Available");
                e.printStackTrace();
                return;
            }
        }
        super.define(key, value);
    }

    /**
     * Notification from the DiskSpace MeasurableCapability
     * 
     * @param o The Observable object
     * @param arg The argument
     */
    public void update(Observable o, Object arg) {
        try {
            java.lang.reflect.Method getAvailable = arg.getClass().getMethod("getAvailable", (Class[]) null);
            Double dAvail = (Double) getAvailable.invoke(arg, (Object[]) null);
            available = dAvail.intValue();
            java.lang.reflect.Method getCapacity = arg.getClass().getMethod("getCapacity", (Class[]) null);
            Double dCap = (Double) getCapacity.invoke(arg, (Object[]) null);
            capabilities.put(CAPACITY, dCap);
            capabilities.put(AVAILABLE, dAvail);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Override supports to ensure that storage requirements are supported
     */
    public boolean supports(SystemRequirement requirement) {
        boolean supports = hasBasicSupport(requirement.getName(), requirement.getClassName());
        if (supports) {
            Map attributes = requirement.getAttributes();
            java.util.Set requirementKeys = attributes.keySet();
            for (java.util.Iterator it = requirementKeys.iterator(); it.hasNext(); ) {
                Object key = it.next();
                if (key.equals(CAPACITY)) {
                    try {
                        int requestedSize = new Integer((String) attributes.get(key)).intValue();
                        supports = (requestedSize < available ? true : false);
                    } catch (NumberFormatException e) {
                        supports = false;
                    }
                    break;
                }
            }
        }
        if (!supports) return (false);
        return (super.supports(requirement));
    }

    /**
     * Determine if the requested available diskspace can be met by the
     * StorageCapability
     * 
     * @param requestedSize The request amount of disk space
     * @return Return true is the available amount of disk space is less then
     * the request amount
     */
    public boolean supports(int requestedSize) {
        return (requestedSize < available);
    }
}
