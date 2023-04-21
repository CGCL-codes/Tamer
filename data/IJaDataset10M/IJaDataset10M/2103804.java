package com.turnengine.client.local.group.command;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Add Child Group.
 */
public class AddChildGroup implements IAddChildGroup {

    /** The command id. */
    public static final short COMMAND_ID = 1402;

    /** The login id. */
    private long loginId;

    /** The instance id. */
    private int instanceId;

    /** The name. */
    private String name;

    /** The parent name. */
    private String parentName;

    /** The faction name. */
    private String factionName;

    /**
	 * Creates a new Add Child Group.
	 */
    public AddChildGroup() {
    }

    /**
	 * Creates a new Add Child Group.
	 * @param loginId the login id
	 * @param instanceId the instance id
	 * @param name the name
	 * @param parentName the parent name
	 * @param factionName the faction name
	 */
    public AddChildGroup(long loginId, int instanceId, String name, String parentName, String factionName) {
        setLoginId(loginId);
        setInstanceId(instanceId);
        setName(name);
        setParentName(parentName);
        setFactionName(factionName);
    }

    /**
	 * Creates a new Add Child Group.
	 * @param iAddChildGroup the i add child group
	 */
    public AddChildGroup(IAddChildGroup iAddChildGroup) {
        setLoginId(iAddChildGroup.getLoginId());
        setInstanceId(iAddChildGroup.getInstanceId());
        setName(iAddChildGroup.getName());
        setParentName(iAddChildGroup.getParentName());
        setFactionName(iAddChildGroup.getFactionName());
    }

    /**
	 * Returns the login id.
	 * @return the login id.
	 */
    public long getLoginId() {
        return loginId;
    }

    /**
	 * Returns the instance id.
	 * @return the instance id.
	 */
    public int getInstanceId() {
        return instanceId;
    }

    /**
	 * Returns the name.
	 * @return the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns the parent name.
	 * @return the parent name.
	 */
    public String getParentName() {
        return parentName;
    }

    /**
	 * Returns the faction name.
	 * @return the faction name.
	 */
    public String getFactionName() {
        return factionName;
    }

    /**
	 * Sets the login id.
	 * @param loginId the login id to set.
	 */
    public void setLoginId(long loginId) {
        this.loginId = loginId;
    }

    /**
	 * Sets the instance id.
	 * @param instanceId the instance id to set.
	 */
    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    /**
	 * Sets the name.
	 * @param name the name to set.
	 */
    public void setName(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
    }

    /**
	 * Sets the parent name.
	 * @param parentName the parent name to set.
	 */
    public void setParentName(String parentName) {
        if (parentName == null) {
            throw new NullPointerException("parentName");
        }
        this.parentName = parentName;
    }

    /**
	 * Sets the faction name.
	 * @param factionName the faction name to set.
	 */
    public void setFactionName(String factionName) {
        this.factionName = factionName;
    }

    /**
	 * Returns the hash code.
	 * @return the hash code.
	 */
    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder(643, 37);
        hash.append(loginId);
        hash.append(instanceId);
        hash.append(name);
        hash.append(parentName);
        hash.append(factionName);
        return hash.toHashCode();
    }

    /**
	 * Returns true if this is equal to the given object.
	 * @param object the object to compare.
	 * @return true if this is equal to the given object.
	 */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof AddChildGroup) {
            AddChildGroup compare = (AddChildGroup) object;
            EqualsBuilder equals = new EqualsBuilder();
            equals.append(this.loginId, compare.loginId);
            equals.append(this.instanceId, compare.instanceId);
            equals.append(this.name, compare.name);
            equals.append(this.parentName, compare.parentName);
            equals.append(this.factionName, compare.factionName);
            return equals.isEquals();
        }
        return false;
    }

    /**
	 * Compare this to the given object.
	 * @param compare the object to compare to.
	 * @return the result of the comparison.
	 */
    @Override
    public int compareTo(IAddChildGroup compare) {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(this.loginId, compare.getLoginId());
        builder.append(this.instanceId, compare.getInstanceId());
        builder.append(this.name, compare.getName());
        builder.append(this.parentName, compare.getParentName());
        builder.append(this.factionName, compare.getFactionName());
        return builder.toComparison();
    }

    /**
	 * Returns this as a string.
	 * @return this as a string.
	 */
    @Override
    public String toString() {
        ToStringBuilder string = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        string.append("loginId", loginId);
        string.append("instanceId", instanceId);
        if (name != null) {
            string.append("name", name);
        }
        if (parentName != null) {
            string.append("parentName", parentName);
        }
        if (factionName != null) {
            string.append("factionName", factionName);
        }
        return string.toString();
    }

    public short getCommandId() {
        return COMMAND_ID;
    }
}
