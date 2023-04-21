package com.turnengine.client.local.unit.command;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Get All Units At Player.
 */
public class GetAllUnitsAtPlayer implements IGetAllUnitsAtPlayer {

    /** The command id. */
    public static final short COMMAND_ID = 1915;

    /** The login id. */
    private long loginId;

    /** The instance id. */
    private int instanceId;

    /**
	 * Creates a new Get All Units At Player.
	 */
    public GetAllUnitsAtPlayer() {
    }

    /**
	 * Creates a new Get All Units At Player.
	 * @param loginId the login id
	 * @param instanceId the instance id
	 */
    public GetAllUnitsAtPlayer(long loginId, int instanceId) {
        setLoginId(loginId);
        setInstanceId(instanceId);
    }

    /**
	 * Creates a new Get All Units At Player.
	 * @param iGetAllUnitsAtPlayer the i get all units at player
	 */
    public GetAllUnitsAtPlayer(IGetAllUnitsAtPlayer iGetAllUnitsAtPlayer) {
        setLoginId(iGetAllUnitsAtPlayer.getLoginId());
        setInstanceId(iGetAllUnitsAtPlayer.getInstanceId());
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
	 * Returns the hash code.
	 * @return the hash code.
	 */
    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder(1249, 37);
        hash.append(loginId);
        hash.append(instanceId);
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
        if (object instanceof GetAllUnitsAtPlayer) {
            GetAllUnitsAtPlayer compare = (GetAllUnitsAtPlayer) object;
            EqualsBuilder equals = new EqualsBuilder();
            equals.append(this.loginId, compare.loginId);
            equals.append(this.instanceId, compare.instanceId);
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
    public int compareTo(IGetAllUnitsAtPlayer compare) {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(this.loginId, compare.getLoginId());
        builder.append(this.instanceId, compare.getInstanceId());
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
        return string.toString();
    }

    public short getCommandId() {
        return COMMAND_ID;
    }
}
