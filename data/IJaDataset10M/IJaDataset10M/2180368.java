package com.yubuild.coreman.data;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.yubuild.coreman.Constants;

public class Keyword extends BaseObject implements Serializable {

    private static final long serialVersionUID = 0x3336373636373638L;

    private Long id;

    private String description;

    public Keyword() {
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

    public boolean equals(Object object) {
        if (!(object instanceof Keyword)) {
            return false;
        } else {
            Keyword rhs = (Keyword) object;
            return (new EqualsBuilder()).append(description, rhs.description).append(id, rhs.id).isEquals();
        }
    }

    public int hashCode() {
        return (new HashCodeBuilder(0x81c1c171, 0x77ab20f7)).append(description).append(id).toHashCode();
    }

    public String toString() {
        return (new ToStringBuilder(this)).append("id", id).append("description", description).toString();
    }

    public String getActivityString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDescription()).append(Constants.ACTIVITY_SEPARATOR);
        return sb.toString();
    }
}
