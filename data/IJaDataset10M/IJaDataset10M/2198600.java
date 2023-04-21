package com.emc.esu.api;

import java.util.List;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Encapsulates the replica information on an object.
 */
public class ObjectReplica {

    private String id;

    private String location;

    private String replicaType;

    private boolean current;

    private String storageType;

    public ObjectReplica() {
    }

    @SuppressWarnings("rawtypes")
    public ObjectReplica(Element replica) {
        Namespace esuNs = Namespace.getNamespace("http://www.emc.com/cos/");
        List children = replica.getChildren("id", esuNs);
        if (children == null || children.size() < 1) {
            throw new EsuException("id not found in replica");
        }
        id = ((Element) children.get(0)).getTextTrim();
        children = replica.getChildren("location", esuNs);
        if (children == null || children.size() < 1) {
            throw new EsuException("location not found in replica");
        }
        location = ((Element) children.get(0)).getTextTrim();
        children = replica.getChildren("type", esuNs);
        if (children == null || children.size() < 1) {
            throw new EsuException("type not found in replica");
        }
        replicaType = ((Element) children.get(0)).getTextTrim();
        children = replica.getChildren("current", esuNs);
        if (children == null || children.size() < 1) {
            throw new EsuException("current not found in replica");
        }
        current = "true".equals(((Element) children.get(0)).getTextTrim());
        children = replica.getChildren("storageType", esuNs);
        if (children == null || children.size() < 1) {
            throw new EsuException("storageType not found in replica");
        }
        storageType = ((Element) children.get(0)).getTextTrim();
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the location
	 */
    public String getLocation() {
        return location;
    }

    /**
	 * @param location the location to set
	 */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
	 * @return the replicaType
	 */
    public String getReplicaType() {
        return replicaType;
    }

    /**
	 * @param replicaType the replicaType to set
	 */
    public void setReplicaType(String replicaType) {
        this.replicaType = replicaType;
    }

    /**
	 * @return the current
	 */
    public boolean isCurrent() {
        return current;
    }

    /**
	 * @param current the current to set
	 */
    public void setCurrent(boolean current) {
        this.current = current;
    }

    /**
	 * @return the storageType
	 */
    public String getStorageType() {
        return storageType;
    }

    /**
	 * @param storageType the storageType to set
	 */
    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
}
