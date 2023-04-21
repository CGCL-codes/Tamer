package com.tenline.pinecone.platform.model;

import java.util.Collection;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Bill
 *
 */
@XmlRootElement
@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Variable extends Entity {

    /**
	 * Variable's Type
	 */
    public static final String READ = "read";

    public static final String WRITE = "write";

    public static final String DISCRETE = "discrete";

    public static final String CONTINUOUS = "continuous";

    public static final String IMAGE_JPEG = "image/jpeg";

    @Persistent
    private String name;

    @Persistent
    private String type;

    @Persistent(defaultFetchGroup = "true")
    private Device device;

    @Persistent(mappedBy = "variable", defaultFetchGroup = "true")
    @Element(dependent = "true")
    private Collection<Item> items;

    /**
	 * 
	 */
    public Variable() {
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param device the device to set
	 */
    public void setDevice(Device device) {
        this.device = device;
    }

    /**
	 * @return the device
	 */
    public Device getDevice() {
        return device;
    }

    /**
	 * @param items the items to set
	 */
    @XmlTransient
    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    /**
	 * @return the items
	 */
    public Collection<Item> getItems() {
        return items;
    }
}
