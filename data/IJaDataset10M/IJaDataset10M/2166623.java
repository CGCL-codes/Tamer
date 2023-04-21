package org.gbif.portal.dto.resources;

import java.io.Serializable;

/**
 * AgentDTO
 * 
 * @author Donald Hobern
 */
public class DataResourceAgentDTO implements Serializable {

    /**
	 * Generated 
	 */
    private static final long serialVersionUID = -7922106864289155372L;

    protected String key;

    protected String dataResourceKey;

    protected String agentKey;

    protected String agentName;

    protected String agentAddress;

    protected String agentEmail;

    protected String agentTelephone;

    protected Integer agentType;

    protected String agentTypeName;

    /**
	 * @return the agentAddress
	 */
    public String getAgentAddress() {
        return agentAddress;
    }

    /**
	 * @param agentAddress the agentAddress to set
	 */
    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    /**
	 * @return the agentEmail
	 */
    public String getAgentEmail() {
        return agentEmail;
    }

    /**
	 * @param agentEmail the agentEmail to set
	 */
    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }

    /**
	 * @return the agentKey
	 */
    public String getAgentKey() {
        return agentKey;
    }

    /**
	 * @param agentKey the agentKey to set
	 */
    public void setAgentKey(String agentKey) {
        this.agentKey = agentKey;
    }

    /**
	 * @return the agentName
	 */
    public String getAgentName() {
        return agentName;
    }

    /**
	 * @param agentName the agentName to set
	 */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    /**
	 * @return the agentTelephone
	 */
    public String getAgentTelephone() {
        return agentTelephone;
    }

    /**
	 * @param agentTelephone the agentTelephone to set
	 */
    public void setAgentTelephone(String agentTelephone) {
        this.agentTelephone = agentTelephone;
    }

    /**
	 * @return the agentType
	 */
    public Integer getAgentType() {
        return agentType;
    }

    /**
	 * @param agentType the agentType to set
	 */
    public void setAgentType(Integer agentType) {
        this.agentType = agentType;
    }

    /**
	 * @return the dataResourceKey
	 */
    public String getDataResourceKey() {
        return dataResourceKey;
    }

    /**
	 * @param dataResourceKey the dataResourceKey to set
	 */
    public void setDataResourceKey(String dataResourceKey) {
        this.dataResourceKey = dataResourceKey;
    }

    /**
	 * @return the key
	 */
    public String getKey() {
        return key;
    }

    /**
	 * @param key the key to set
	 */
    public void setKey(String key) {
        this.key = key;
    }

    /**
   * @return the agentTypeName
   */
    public String getAgentTypeName() {
        return agentTypeName;
    }

    /**
   * @param agentTypeName the agentTypeName to set
   */
    public void setAgentTypeName(String agentTypeName) {
        this.agentTypeName = agentTypeName;
    }
}
