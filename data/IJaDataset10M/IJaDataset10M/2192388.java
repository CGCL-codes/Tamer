package fi.vtt.noen.mfw.bundle.server.shared.datamodel;

import fi.vtt.noen.mfw.bundle.common.DataObject;
import fi.vtt.noen.mfw.bundle.common.DataType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Data value for a measurement.
 * Annotations are for persistence with JPA (DB storage).
 *
 * @author Teemu Kanstren
 */
@Entity
@Table(name = "bm_value")
public class Value extends DataObject implements Serializable {

    @GeneratedValue
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private BMDescription bm;

    @Column(name = "value_precision")
    private int precision;

    @Column(name = "value_string")
    private String value;

    @Column(name = "value_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Transient
    private Long subscriptionId;

    @Column(name = "error_flag")
    private boolean error;

    public enum SortKey {

        PRECISION, MEASUREURI, VALUE, TIME
    }

    public Value() {
        super(DataType.STRINGVALUE);
    }

    public Value(BMDescription bm, int precision, String value, long time) {
        this(bm, precision, value, time, -1);
    }

    public Value(BMDescription bm, int precision, String value, long time, long subscriptionId) {
        this(bm, precision, value, time, subscriptionId, false);
    }

    public Value(BMDescription bm, int precision, String value, long time, long subscriptionId, boolean error) {
        super(DataType.STRINGVALUE);
        this.bm = bm;
        this.precision = precision;
        this.value = value;
        this.time = new Date(time);
        this.subscriptionId = subscriptionId;
        this.error = error;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    @Transient
    public String getMeasureURI() {
        return bm.getMeasureURI();
    }

    public long getId() {
        return id;
    }

    public BMDescription getBm() {
        return bm;
    }

    public int getPrecision() {
        return precision;
    }

    public String getString() {
        checkType(DataType.STRINGVALUE);
        return value;
    }

    @Transient
    public Date getTime() {
        return time;
    }

    @Transient
    public boolean isError() {
        return error;
    }

    @Transient
    public String getTimeFormatted() {
        return DateFormat.getDateTimeInstance().format(time);
    }

    private void checkType(DataType expected) {
        if (getType() != expected) {
            throw new IllegalArgumentException("Not " + expected + " type value (actually " + getType() + ").");
        }
    }

    @Override
    public String toString() {
        return bm.getMeasureURI() + ":" + value;
    }

    public String valueString() {
        if (getType() == DataType.STRINGVALUE) {
            return "\"" + value + "\"";
        }
        return "" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        if (precision != value1.precision) return false;
        if (bm != null ? !bm.equals(value1.bm) : value1.bm != null) return false;
        if (time != null ? !time.equals(value1.time) : value1.time != null) return false;
        if (value != null ? !value.equals(value1.value) : value1.value != null) return false;
        if (subscriptionId != value1.subscriptionId) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = bm != null ? bm.hashCode() : 0;
        result = 31 * result + precision;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + subscriptionId.hashCode();
        return result;
    }
}
