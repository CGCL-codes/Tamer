package org.homemotion.calendar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.homemotion.dao.AbstractNamedItem;

@Entity
@Table(name = "HMCalendarEntry")
public class CalendarEntry extends AbstractNamedItem implements Serializable {

    private static final long serialVersionUID = -2399984431325262397L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private boolean allDay = true;

    public CalendarEntry() {
    }

    public CalendarEntry(String name, Date date) {
        this(name, date, date, true);
    }

    public CalendarEntry(String name, Date start, Date end) {
        setName(name);
        this.startDate = start;
        this.endDate = end;
    }

    public CalendarEntry(String name, Date start, Date end, boolean allDay) {
        setName(name);
        this.startDate = start;
        this.endDate = end;
        this.allDay = allDay;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CalendarEntry other = (CalendarEntry) obj;
        if ((this.getName() == null) ? (other.getName() != null) : !this.getName().equals(other.getName())) {
            return false;
        }
        if (this.startDate != other.startDate && (this.startDate == null || !this.startDate.equals(other.startDate))) {
            return false;
        }
        if (this.endDate != other.endDate && (this.endDate == null || !this.endDate.equals(other.endDate))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 61 * hash + (this.startDate != null ? this.startDate.hashCode() : 0);
        hash = 61 * hash + (this.endDate != null ? this.endDate.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CalendarEntry{name=" + getName() + ",startDate=" + startDate + ",endDate=" + endDate + "}";
    }

    public int getDays() {
        GregorianCalendar startCal = new GregorianCalendar();
        startCal.setTime(this.startDate);
        GregorianCalendar endCal = new GregorianCalendar();
        endCal.setTime(this.endDate);
        return (endCal.get(Calendar.DAY_OF_YEAR) - startCal.get(Calendar.DAY_OF_YEAR)) + 1;
    }
}
