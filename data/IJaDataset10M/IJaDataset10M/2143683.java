package com.continuent.tungsten.manager.resource;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.commons.utils.ReflectUtils;
import com.continuent.tungsten.manager.client.CLUtils;
import com.continuent.tungsten.manager.exception.ResourceException;

/**
 * 
 * @author edward
 *
 */
public abstract class Resource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected String name = null;

    protected ResourceType type = ResourceType.UNDEFINED;

    protected boolean isContainer = false;

    protected boolean isExecutable = false;

    protected ResourceType childType = ResourceType.UNDEFINED;

    public Resource() {
        this.name = "UNKNOWN";
    }

    public Resource(String name, ResourceType type) {
        this.name = name;
        this.type = type;
    }

    public TungstenProperties toProperties() {
        TungstenProperties props = new TungstenProperties();
        props.setString("type", this.getType().toString());
        props.extractProperties(this, true);
        return props;
    }

    /**
     * Describe this instance, in detail if necessary.
     * 
     * @param detailed
     * @return
     */
    public String describe(boolean detailed) {
        TungstenProperties props = this.toProperties();
        return CLUtils.formatProperties(this.name, props, "");
    }

    /**
     * 
     */
    public String getKey() {
        return getName();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ResourceType type) {
        this.type = type;
    }

    /**
     * @return the isContainerResource
     */
    public boolean isContainer() {
        return isContainer;
    }

    /**
     * @param isContainer the isContainer to set
     */
    public void setContainer(boolean isContainer) {
        this.isContainer = isContainer;
    }

    /**
     * @return the isExecutable
     */
    public boolean isExecutable() {
        return isExecutable;
    }

    /**
     * @param isExecutable the isExecutable to set
     */
    public void setExecutable(boolean isExecutable) {
        this.isExecutable = isExecutable;
    }

    /**
     * @return the childType
     */
    public ResourceType getChildType() {
        return childType;
    }

    /**
     * @param childType the childType to set
     */
    public void setChildType(ResourceType childType) {
        this.childType = childType;
    }

    /**
     * Copies values from fields of this instance to another instance
     * 
     * @param destination
     * @return
     * @throws ResourceException
     */
    public Resource copyTo(Resource destination) {
        ReflectUtils.copy(this, destination);
        destination.setName(this.getName());
        return destination;
    }
}
