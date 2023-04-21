package org.esprit.ocm.model.ec2;

import java.util.List;
import net.sf.gilead.pojo.java5.LightEntity;

/**
 * This is a container class for an Availability Zone
 */
public class AvailabilityZoneMapping extends LightEntity {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5415667591494798958L;

    private String name;

    private String state;

    private String regionName;

    private List<String> messages;

    public String toString() {
        return "AvailabilityZone[name=" + name + ", state=" + state + ", region=" + regionName + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
