package org.tolven.doc.bean;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Properties which travel with a message payload.
 * 
 * @author Joseph Isaac
 */
@Entity
@Table
public class TolvenMessageProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DOC_SEQ_GEN")
    private long id;

    @Version
    @Column
    private Integer version;

    @Column
    private String propertyName;

    @Column
    private Boolean booleanValue;

    @Column
    private Byte byteValue;

    @Column
    private Short shortValue;

    @Column
    private Integer integerValue;

    @Column
    private Long longValue;

    @Column
    private Float floatValue;

    @Column
    private Double doubleValue;

    @Column
    private String string;

    @ManyToOne
    private TolvenMessage tolvenMessage;

    public TolvenMessageProperty() {
    }

    public TolvenMessageProperty(String name) {
        this.propertyName = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public TolvenMessage getTolvenMessage() {
        return tolvenMessage;
    }

    public void setTolvenMessage(TolvenMessage tolvenMessage) {
        this.tolvenMessage = tolvenMessage;
    }

    public Boolean getBoolean() {
        return booleanValue;
    }

    public void setBoolean(Boolean booleanValue) {
        clear();
        this.booleanValue = booleanValue;
    }

    public Byte getByte() {
        return byteValue;
    }

    public void setByte(Byte byteValue) {
        clear();
        this.byteValue = byteValue;
    }

    public Short getShort() {
        return shortValue;
    }

    public void setShort(Short shortValue) {
        clear();
        this.shortValue = shortValue;
    }

    public Integer getInteger() {
        return integerValue;
    }

    public void setInteger(Integer integerValue) {
        clear();
        this.integerValue = integerValue;
    }

    public Long getLong() {
        return longValue;
    }

    public void setLong(Long longValue) {
        clear();
        this.longValue = longValue;
    }

    public Float getFloat() {
        return floatValue;
    }

    public void setFloat(Float floatValue) {
        clear();
        this.floatValue = floatValue;
    }

    public Double getDouble() {
        return doubleValue;
    }

    public void setDouble(Double doubleValue) {
        clear();
        this.doubleValue = doubleValue;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        clear();
        this.string = string;
    }

    public void setObject(Object obj) {
        if (obj instanceof Boolean) {
            setBoolean(((Boolean) obj).booleanValue());
        } else if (obj instanceof Byte) {
            setByte(((Byte) obj));
        } else if (obj instanceof Short) {
            setShort(((Short) obj));
        } else if (obj instanceof Integer) {
            setInteger(((Integer) obj));
        } else if (obj instanceof Long) {
            setLong(((Long) obj));
        } else if (obj instanceof Float) {
            setFloat(((Float) obj));
        } else if (obj instanceof Double) {
            setDouble(((Double) obj));
        } else if (obj instanceof String) {
            setString(((String) obj));
        } else {
            throw new RuntimeException("Unknown type for " + getClass() + ": " + obj.getClass());
        }
    }

    public Object getObject() {
        if (getBoolean() != null) {
            return getBoolean();
        } else if (getByte() != null) {
            return getByte();
        } else if (getShort() != null) {
            return getShort();
        } else if (getInteger() != null) {
            return getInteger();
        } else if (getLong() != null) {
            return getLong();
        } else if (getFloat() != null) {
            return getFloat();
        } else if (getDouble() != null) {
            return getDouble();
        } else if (getString() != null) {
            return getString();
        } else {
            return null;
        }
    }

    private void clear() {
        booleanValue = null;
        byteValue = null;
        shortValue = null;
        integerValue = null;
        longValue = null;
        floatValue = null;
        doubleValue = null;
        string = null;
    }

    public int hashCode() {
        return getPropertyName().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof TolvenMessageProperty) {
            TolvenMessageProperty other = (TolvenMessageProperty) obj;
            if (getTolvenMessage() != null && other.getTolvenMessage() != null && getTolvenMessage().getId() == other.getTolvenMessage().getId() && getPropertyName().equals(other.getPropertyName())) return true;
        }
        return false;
    }
}
