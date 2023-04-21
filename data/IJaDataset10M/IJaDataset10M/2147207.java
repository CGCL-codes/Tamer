package sao.DML;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class DMLTableCluster extends ArrayList<DMLTable> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5316107171145228998L;

    private String globalClusterName;

    private Properties globalSysProps;

    private Properties globalUserProps;

    /**
	 * 
	 */
    public DMLTableCluster() {
        initialize("");
    }

    public DMLTableCluster(String clusterName) {
        initialize(clusterName);
    }

    /**
	 * @param initialCapacity
	 */
    public DMLTableCluster(int initialCapacity) {
        super(initialCapacity);
        initialize("");
    }

    public DMLTableCluster(String clusterName, int initialCapacity) {
        super(initialCapacity);
        initialize(clusterName);
    }

    /**
	 * @param c
	 */
    public DMLTableCluster(Collection<DMLTable> c) {
        super(c);
        initialize("");
    }

    public DMLTableCluster(String clusterName, Collection<DMLTable> c) {
        super(c);
        initialize(clusterName);
    }

    /**
	 * Define and initialize the collection and the properties
	 * @param groupName
	 */
    private void initialize(String clusterName) {
        setGroupName(clusterName);
        globalSysProps = new Properties();
        globalUserProps = new Properties();
    }

    /**
	 * clear, then remove all pointers to all objects
	 */
    public void dispose() {
        clear();
        globalSysProps = null;
        globalUserProps = null;
    }

    /**
	 * clear all properties, and all values from our list
	 */
    public void clear() {
        super.clear();
        globalSysProps.clear();
        globalUserProps.clear();
    }

    /**
	 * set the group name, rename it if necessary
	 * @param groupName
	 */
    public void setGroupName(String groupName) {
        globalClusterName = groupName;
    }

    /**
	 * Return the current group name
	 * @return
	 */
    public String getGroupName() {
        return (globalClusterName);
    }

    /**
	 * Provide the group name for the display tree when possible
	 */
    public String toString() {
        return (getGroupName());
    }

    /**
	 * Add a system defined property that the key cannot be edited
	 * 
	 * @param propKey
	 * @param propVal
	 */
    public void addSystemProperty(String propKey, Object propVal) {
        globalSysProps.put(propKey, propVal);
    }

    /**
	 * Add a user defined property, where the key and the value can be edited
	 * 
	 * @param propKey
	 * @param propVal
	 */
    public void addUserProperty(String propKey, Object propVal) {
        globalUserProps.put(propKey, propVal);
    }

    /**
	 * Grab a system defined property value
	 * @param propKey
	 * @return
	 */
    public Object getSystemProperty(String propKey) {
        return (globalSysProps.get(propKey));
    }

    /**
	 * Grab a user defined property value
	 * 
	 * @param propKey
	 * @return
	 */
    public Object getUserProperty(String propKey) {
        return (globalUserProps.get(propKey));
    }
}
