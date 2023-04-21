package com.turnengine.client.local.location.command;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Get Grid Details List.
 */
public class GetGridDetailsList implements IGetGridDetailsList {

    /** The command id. */
    public static final short COMMAND_ID = 2011;

    /** The login id. */
    private long loginId;

    /** The instance id. */
    private int instanceId;

    /** The coordinates. */
    private int[] coordinates;

    /**
	 * Creates a new Get Grid Details List.
	 */
    public GetGridDetailsList() {
    }

    /**
	 * Creates a new Get Grid Details List.
	 * @param loginId the login id
	 * @param instanceId the instance id
	 * @param coordinates the coordinates
	 */
    public GetGridDetailsList(long loginId, int instanceId, int[] coordinates) {
        setLoginId(loginId);
        setInstanceId(instanceId);
        setCoordinates(coordinates);
    }

    /**
	 * Creates a new Get Grid Details List.
	 * @param iGetGridDetailsList the i get grid details list
	 */
    public GetGridDetailsList(IGetGridDetailsList iGetGridDetailsList) {
        setLoginId(iGetGridDetailsList.getLoginId());
        setInstanceId(iGetGridDetailsList.getInstanceId());
        setCoordinates(iGetGridDetailsList.getCoordinates());
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
	 * Returns the coordinates.
	 * @return the coordinates.
	 */
    public int[] getCoordinates() {
        return coordinates;
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
	 * Sets the coordinates.
	 * @param coordinates the coordinates to set.
	 */
    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    /**
	 * Returns the hash code.
	 * @return the hash code.
	 */
    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder(787, 37);
        hash.append(loginId);
        hash.append(instanceId);
        hash.append(coordinates);
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
        if (object instanceof GetGridDetailsList) {
            GetGridDetailsList compare = (GetGridDetailsList) object;
            EqualsBuilder equals = new EqualsBuilder();
            equals.append(this.loginId, compare.loginId);
            equals.append(this.instanceId, compare.instanceId);
            equals.append(this.coordinates, compare.coordinates);
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
    public int compareTo(IGetGridDetailsList compare) {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(this.loginId, compare.getLoginId());
        builder.append(this.instanceId, compare.getInstanceId());
        builder.append(this.coordinates, compare.getCoordinates());
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
        if (coordinates != null) {
            string.append("coordinates", coordinates);
        }
        return string.toString();
    }

    public short getCommandId() {
        return COMMAND_ID;
    }
}
