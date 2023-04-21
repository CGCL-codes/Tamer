package net.cw.talos.persistence;

import java.io.Serializable;
import net.cw.talos.Securable;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Securable composed id.
 * 
 * @author Philipp Leusmann
 * 
 */
public class SecurableId implements Serializable {

    String targetid;

    String targetType;

    public SecurableId(Securable securable) {
        super();
        setTargetId(securable.getId().toString());
        setTargetType(securable.getTargetType());
    }

    public SecurableId(String targetid, String type) {
        super();
        this.targetid = targetid;
        this.targetType = type;
    }

    public SecurableId() {
        super();
    }

    public static final int MAX_TARGET_TYPE_LENGTH = 240;

    public static final int MAX_TARGET_ID_LENGTH = 100;

    /**
	 * 
	 * @return
	 * @hibernate.property column="target_id" not-null="true" type="string" length="100"
	 */
    public String getTargetId() {
        return targetid;
    }

    public void setTargetId(String id) {
        this.targetid = id;
    }

    /**
	 * 
	 * @return
	 * @hibernate.property column="target_type" type="string" not-null="true" length="240"
	 */
    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public boolean equals(final Object other) {
        if (!(other instanceof SecurableId)) return false;
        SecurableId castOther = (SecurableId) other;
        return new EqualsBuilder().append(getTargetId(), castOther.getTargetId()).append(getTargetType(), castOther.getTargetType()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getTargetId()).append(targetType).toHashCode();
    }

    public int compareTo(final Object other) {
        SecurableId castOther = (SecurableId) other;
        return new CompareToBuilder().append(getTargetId(), castOther.getTargetId()).append(getTargetType(), castOther.getTargetType()).toComparison();
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getTargetId()).append("targetType", getTargetType()).toString();
    }
}
