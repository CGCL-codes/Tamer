package com.blindeye.model.blindeyes;

import org.apache.commons.lang.time.DateUtils;
import static org.jboss.seam.ScopeType.SESSION;
import static org.jboss.seam.ScopeType.EVENT;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import org.jboss.seam.annotations.*;
import com.blindeye.util.IdGenerator;

@Entity
@Name("timeInterval")
@Role(name = "tempTimeInterval", scope = EVENT)
@Table(name = "timeIntervals")
public class TimeInterval {

    private Integer id;

    private int version;

    private int months = 0;

    private int days = 0;

    private int hours = 0;

    private int minutes = 0;

    private int seconds = 0;

    @Transient
    private static long MILLIS_IN_MONTH = DateUtils.MILLIS_PER_DAY * 30;

    @Transient
    private String formattedTime;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Transient
    public long getTotalMS() {
        long ms = 0;
        ms += (MILLIS_IN_MONTH * months);
        ms += (DateUtils.MILLIS_PER_DAY * days);
        ms += (DateUtils.MILLIS_PER_HOUR * hours);
        ms += (DateUtils.MILLIS_PER_MINUTE * minutes);
        ms += (DateUtils.MILLIS_PER_SECOND * seconds);
        return ms;
    }

    @Transient
    public void reversePopulate(long ms) {
        long x = 0;
        while ((x = (ms - MILLIS_IN_MONTH)) > 0) {
            ms -= MILLIS_IN_MONTH;
            ++months;
        }
        while ((x = (ms - DateUtils.MILLIS_PER_DAY)) > 0) {
            ms -= DateUtils.MILLIS_PER_DAY;
            ++days;
        }
        while ((x = (ms - DateUtils.MILLIS_PER_HOUR)) > 0) {
            ms -= DateUtils.MILLIS_PER_HOUR;
            ++hours;
        }
        while ((x = (ms - DateUtils.MILLIS_PER_MINUTE)) > 0) {
            ms -= DateUtils.MILLIS_PER_MINUTE;
            ++minutes;
        }
        while ((x = (ms - DateUtils.MILLIS_PER_SECOND)) > 0) {
            ms -= DateUtils.MILLIS_PER_SECOND;
            ++seconds;
        }
    }

    @Transient
    public String getFormattedTime() {
        StringBuilder sb = new StringBuilder();
        sb.append(months + "m ");
        sb.append(days + "d ");
        sb.append(hours + "h ");
        sb.append(minutes + "m ");
        sb.append(seconds + "s ");
        formattedTime = sb.toString();
        return formattedTime;
    }
}
