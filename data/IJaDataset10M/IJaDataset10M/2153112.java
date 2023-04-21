package org.identifylife.key.engine.core.model.features;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author dbarnier
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DatasetRef {

    @XmlAttribute
    private String ref;

    public DatasetRef() {
    }

    public DatasetRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DatasetRef) {
            DatasetRef other = (DatasetRef) obj;
            if (getRef() != null) {
                return getRef().equals(other.getRef());
            } else if (this == other) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ref", getRef()).toString();
    }
}
