package org.jsf2jpa.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * <br/>$LastChangedRevision:$
 * <br/>$LastChangedDate:$
 *
 * @author ASementsov
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "DT_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractAttribute extends BaseEntity implements Serializable {

    /**
     * Subversion revision number it will be changed automatically when commited
     */
    private static final String REV_NUMBER = "$Revision:$";

    @Column(name = "DT_TYPE")
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Column(name = "STR_VAL")
    private String stringValue;

    @Column(name = "NUM_VAL")
    private Number numberValue;

    @Column(name = "DT_VAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValue;

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        setDataType(DataType.DATE);
        this.dateValue = dateValue;
    }

    public Number getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(Number numberValue) {
        setDataType(DataType.NUMBER);
        this.numberValue = numberValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        setDataType(DataType.STRING);
        this.stringValue = stringValue;
    }

    public Object getAsObject() {
        switch(dataType) {
            case DATE:
                return dateValue;
            case NUMBER:
                return numberValue;
            case STRING:
                return stringValue;
            default:
                return null;
        }
    }

    public String getAsString() {
        Object ret = getAsObject();
        if (ret != null) return ret.toString(); else return "";
    }
}
