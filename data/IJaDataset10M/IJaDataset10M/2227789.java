package org.jcvi.glk;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * An <code>TrimSequenceAttribute</code> is a free-form tag/value pair which 
 * allows a wide variety of external information to be applied to a
 * {@link TrimSequence}.
 * 
 * @author jsitz
 * @author dkatzel
 */
@Entity
@Table(name = "TrimSequenceAttribute")
public class TrimSequenceAttribute implements Attribute<TrimSequence, TrimSequenceAttributeType> {

    private TrimSequenceAttributeId id;

    private String value;

    /**
     * Creates a new <code>TrimSequenceAttribute</code>. 
     *
     */
    public TrimSequenceAttribute() {
        super();
    }

    /**
     * Creates a new <code>TrimSequenceAttribute</code>. 
     *
     * @param trimseq
     * @param type
     * @param value
     */
    public TrimSequenceAttribute(TrimSequence trimseq, TrimSequenceAttributeType type, String value) {
        this();
        this.id = new TrimSequenceAttributeId(trimseq, type);
        this.value = value;
    }

    /**
     * Retrieves the value of this  {@link TrimSequenceAttribute}.
     *
     * @return A {@link String}
     */
    @Column(name = "value", unique = false, nullable = false, insertable = true, updatable = true)
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of this  {@link TrimSequenceAttribute}.
     * @param value A String
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Retrieves the {@link TrimSequenceAttributeId}.
     *
     * @return A {@link TrimSequenceAttributeId}
     */
    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "trimSequence", column = @Column(name = "TrimSequence_id")), @AttributeOverride(name = "type", column = @Column(name = "TrimSequenceType_id")) })
    protected TrimSequenceAttributeId getId() {
        return id;
    }

    /**
     * Sets the {@link TrimSequenceAttributeId}.
     * @param trimSequenceAttributeId A TrimSequenceAttributeId
     */
    protected void setId(TrimSequenceAttributeId trimSequenceAttributeId) {
        this.id = trimSequenceAttributeId;
    }

    /**
     * Retrieves the {@link TrimSequence} to which this
     * {@link TrimSequenceAttributeType} belongs.
     *
     * @return A {@link TrimSequence}
     */
    @Transient
    public TrimSequence getTrimSequence() {
        return this.id.getTrimSequence();
    }

    /**
     * Sets the {@link TrimSequence} to associate this attribute with.
     * 
     * @param trimSequence The {@link TrimSequence} to describe.
     */
    protected void setTrimSequence(TrimSequence trimSequence) {
        this.id.setTrimSequence(trimSequence);
    }

    /**
     * Retrieves the {@link TrimSequenceAttributeType}.
     *
     * @return A {@link TrimSequenceAttributeType}
     */
    @Transient
    public TrimSequenceAttributeType getType() {
        return this.id.getType();
    }

    /**
     * Sets the type of this attribute.
     * 
     * @param type The {@link TrimSequenceAttributeType} to use.
     */
    protected void setType(TrimSequenceAttributeType type) {
        this.id.setType(type);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final TrimSequenceAttribute other = (TrimSequenceAttribute) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }
}
