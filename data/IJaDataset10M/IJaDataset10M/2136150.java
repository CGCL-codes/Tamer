package com.teletalk.jserver.rmi.remote;

import java.util.List;

/**
 * Transfer structure class containing information about a subcomponent. This class is used to transfer 
 * subcomponent data to the administration tool 
 * 
 * @see com.teletalk.jserver.SubComponent
 * 
 * @author Tobias L�fstrand
 * 
 * @since Beta 2
 */
public class RemoteSubComponentData implements java.io.Serializable {

    static final long serialVersionUID = 7838207775072985222L;

    final String name;

    final int status;

    final RemoteSubComponentData[] subComponents;

    final RemotePropertyData[] properties;

    /**
	 * Creates a new RemoteSubComponentData object.
	 * 
	 * @param name the name of the subcomponent.
	 * @param status the current status of the subcomponent.
	 * @param subComponents a list of child subcomponents of the subcomponent.
	 * @param properties a list of properties owned by the subcomponent.
	 */
    public RemoteSubComponentData(String name, int status, List subComponents, List properties) {
        this.name = name;
        this.status = status;
        this.subComponents = (subComponents != null) ? (RemoteSubComponentData[]) subComponents.toArray(new RemoteSubComponentData[] {}) : null;
        this.properties = (properties != null) ? (RemotePropertyData[]) properties.toArray(new RemotePropertyData[] {}) : null;
    }

    /**
	 * Returns the name of the subcomponent.
	 * 
	 * @return the name as a String.
	 */
    public final String getName() {
        return name;
    }

    /**
	 * Returns the status of the subcomponent.
	 * 
	 * @return the status as an integer value.
	 */
    public final int getStatus() {
        return status;
    }

    /**
	 * Gets an array of all child subcomponents of the subcomponent this object represents.
	 * 
	 * @return an array of RemoteSubComponentData objects.
	 */
    public final RemoteSubComponentData[] getSubComponents() {
        return subComponents;
    }

    /**
	 * Gets an array of all properties owned by the subcomponent this object represents.
	 * 
	 * @return an array of RemotePropertyData objects.
	 */
    public final RemotePropertyData[] getProperties() {
        return properties;
    }

    /**
    * Gets a string representation of this object.
    */
    public String toString() {
        int childComponents = (this.subComponents != null) ? this.subComponents.length : 0;
        int properties = (this.properties != null) ? this.properties.length : 0;
        return "RemoteSubComponentData[name: " + name + ", status: " + status + ", no of subcomponents: " + childComponents + ", no of properties: " + properties + "]";
    }
}
