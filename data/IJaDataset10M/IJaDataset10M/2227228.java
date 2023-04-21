package org.fixsuite.message.info;

/**
 * Represents a Pre-defined value as defined by the FIX specification.
 * 
 * @author jramoyo
 */
public class ValueInfo {

    private String value;

    private String description;

    private String group;

    private String deprecatingVersion;

    /**
     * Returns the value
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Modifies the value
     * 
     * @param value - the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifies the description
     * 
     * @param description - the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the group
     * 
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * Modifies the group
     * 
     * @param group - the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Returns the deprecatingVersion
     * 
     * @return the deprecatingVersion
     */
    public String getDeprecatingVersion() {
        return deprecatingVersion;
    }

    /**
     * Modifies the deprecatingVersion
     * 
     * @param deprecatingVersion - the deprecatingVersion to set
     */
    public void setDeprecatingVersion(String deprecatingVersion) {
        this.deprecatingVersion = deprecatingVersion;
    }
}
