package com.yubuild.coreman.data;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.yubuild.coreman.Constants;

public class Sender extends BaseObject implements Serializable {

    private static final long serialVersionUID = 0x3336373636373638L;

    private Long id;

    private String description;

    private String value;

    public Sender() {
    }

    public String getDescription() {
        if (description != null) return description.trim().toUpperCase(); else return description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Sender)) {
            return false;
        } else {
            Sender rhs = (Sender) object;
            return (new EqualsBuilder()).append(value, rhs.value).append(description, rhs.description).append(id, rhs.id).isEquals();
        }
    }

    public int hashCode() {
        return (new HashCodeBuilder(0x195812e3, 0x5ddd6bc5)).append(value).append(description).append(id).toHashCode();
    }

    public String toString() {
        return (new ToStringBuilder(this)).append("id", id).append("description", description).append("value", value).toString();
    }

    public String getActivityString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDescription()).append(Constants.ACTIVITY_SEPARATOR);
        sb.append(getValue()).append(Constants.ACTIVITY_SEPARATOR);
        return sb.toString();
    }
}
