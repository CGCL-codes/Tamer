package org.jcvi.glk.ctm;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ctm_reference_attribute")
public class ReferenceAttribute {

    private ReferenceAttributeId id;

    private String value;

    public ReferenceAttribute() {
        super();
    }

    public ReferenceAttribute(Reference reference, ReferenceAttributeType type, String value) {
        this(new ReferenceAttributeId(reference, type), value);
    }

    public ReferenceAttribute(ReferenceAttributeId id, String value) {
        this();
        setId(id);
        setValue(value);
    }

    /**
     * Retrieves the {@link ReferenceAttributeId} for this attribute.
     *
     * @return A {@link ReferenceAttributeId}
     */
    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "reference", column = @Column(name = "ctm_reference_id")), @AttributeOverride(name = "type", column = @Column(name = "ctm_attribute_type_id")) })
    public ReferenceAttributeId getId() {
        return id;
    }

    /**
     * Sets the {@link ReferenceAttributeId} for this attribute.
     * @param id A ReferenceAttributeId
     */
    public void setId(ReferenceAttributeId id) {
        this.id = id;
    }

    /**
     * Retrieves the value for this attribute.
     *
     * @return A {@link String}
     */
    @Column(name = "value")
    public String getValue() {
        return value;
    }

    /**
     * Sets the value for this attribute.
     * @param value A String
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReferenceAttribute)) return false;
        final ReferenceAttribute other = (ReferenceAttribute) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getId() + " value =" + getValue();
    }
}
