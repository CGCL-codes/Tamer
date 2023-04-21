package domainhealth.backend.wldfcapture.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * For a specific MBean object name, holds a set of attribute name-value pairs
 * for the retrieved statistics for the object.
 */
public class InstanceDataRecord {

    /**
	 * Creates new instance of MBean object record
	 * 
	 * @param objectName The MBean object name
	 * @param timestamp The date-time of the retrieved data record
	 */
    public InstanceDataRecord(String objectName, long timestamp) {
        this.objectName = objectName;
        this.timestamp = timestamp;
    }

    /**
	 * Add a new attribute key-value pair retrieved statistic.
	 * 
	 * @param attrName The attribute name
	 * @param attrValue The attribute value
	 */
    public void addElement(String attrName, String attrValue) {
        attributes.put(attrName, attrValue);
    }

    /**
	 * Gets the name of the object.
	 * 
	 * @return The name of the object
	 */
    public String getInstanceObjectName() {
        return objectName;
    }

    /**
	 * Gets the date-time of the retrieved statistic.
	 * 
	 * @return The date-time of the retrieved statistic
	 */
    public long getTimestamp() {
        return timestamp;
    }

    /**
	 * The list of names of statistic attribute stored.
	 *  
	 * @return The list of statistic attribute names
	 */
    public Iterator<String> getAttrNames() {
        return attributes.keySet().iterator();
    }

    /**
	 * Get the value of a stored attribute by name.
	 * 
	 * @param attrName The name of the attribute to lookup
	 * @return THe value of the lookup attribute
	 */
    public String getAttrValue(String attrName) {
        return attributes.get(attrName);
    }

    private final Map<String, String> attributes = new HashMap<String, String>();

    private final String objectName;

    private final long timestamp;
}
