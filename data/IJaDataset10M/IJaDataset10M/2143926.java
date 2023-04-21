package org.tinymarbles.model.value;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.joda.time.LocalTime;
import org.tinymarbles.model.PAttribute;
import org.tinymarbles.model.PObject;
import org.tinymarbles.model.PValue;

/**
 * The value class that stores <code>TimeOfDay</code> information. TimeOfDay is a <a href="http://www.joda.org/">Joda Time</a> type.
 * <p>TimeOfDay keeps hour, minute, second and millisecond information.
 * @author Felipe Kamakura
 * @see org.joda.time.LocalDate
 * 
 */
@Entity
@DiscriminatorValue(value = "time")
public class PLocalTime extends PValue<LocalTime> {

    public PLocalTime(PAttribute att, PObject owner) {
        super(att, owner);
    }

    protected PLocalTime() {
    }

    @Column(name = "v_time")
    @Temporal(TemporalType.TIME)
    public Date getTime() {
        if (this.getValue() == null) return null; else return this.getValue().toDateTimeToday().toDate();
    }

    /**
	 * Sets the value based on a java.util.Date object
	 * @param newValue the desired date
	 */
    protected void setTime(Date newValue) {
        if (newValue == null) this.setValue(null); else this.setValue(LocalTime.fromDateFields(newValue));
    }

    @Override
    protected String encode() {
        return Encoder.encodeLocalTime(this.getValue());
    }
}
