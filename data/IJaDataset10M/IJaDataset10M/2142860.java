package com.turnengine.client.local.alliance.command;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Get Alliance By Id.
 */
public class GetAllianceById implements IGetAllianceById {

    /** The command id. */
    public static final short COMMAND_ID = 1103;

    /** The login id. */
    private long loginId;

    /** The instance id. */
    private int instanceId;

    /** The id. */
    private int id;

    /**
	 * Creates a new Get Alliance By Id.
	 */
    public GetAllianceById() {
    }

    /**
	 * Creates a new Get Alliance By Id.
	 * @param loginId the login id
	 * @param instanceId the instance id
	 * @param id the id
	 */
    public GetAllianceById(long loginId, int instanceId, int id) {
        setLoginId(loginId);
        setInstanceId(instanceId);
        setId(id);
    }

    /**
	 * Creates a new Get Alliance By Id.
	 * @param iGetAllianceById the i get alliance by id
	 */
    public GetAllianceById(IGetAllianceById iGetAllianceById) {
        setLoginId(iGetAllianceById.getLoginId());
        setInstanceId(iGetAllianceById.getInstanceId());
        setId(iGetAllianceById.getId());
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
	 * Returns the id.
	 * @return the id.
	 */
    public int getId() {
        return id;
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
	 * Sets the id.
	 * @param id the id to set.
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * Returns the hash code.
	 * @return the hash code.
	 */
    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder(443, 37);
        hash.append(loginId);
        hash.append(instanceId);
        hash.append(id);
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
        if (object instanceof GetAllianceById) {
            GetAllianceById compare = (GetAllianceById) object;
            EqualsBuilder equals = new EqualsBuilder();
            equals.append(this.loginId, compare.loginId);
            equals.append(this.instanceId, compare.instanceId);
            equals.append(this.id, compare.id);
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
    public int compareTo(IGetAllianceById compare) {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(this.loginId, compare.getLoginId());
        builder.append(this.instanceId, compare.getInstanceId());
        builder.append(this.id, compare.getId());
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
        string.append("id", id);
        return string.toString();
    }

    public short getCommandId() {
        return COMMAND_ID;
    }
}
