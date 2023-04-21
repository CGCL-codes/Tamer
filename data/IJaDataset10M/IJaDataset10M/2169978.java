package org.identifylife.descriptlet.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author dbarnier
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class Model {

    @XmlAttribute
    protected String uuid;

    protected Model() {
    }

    protected Model(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Model) {
            Model other = (Model) obj;
            if (getUuid() != null) {
                return getUuid().equals(other.getUuid());
            } else if (this == other) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (getUuid() != null) {
            return getUuid().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("uuid", getUuid()).toString();
    }
}
