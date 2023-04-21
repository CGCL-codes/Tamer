package com.windsor.node.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ScheduleTime implements Serializable {

    private static final long serialVersionUID = 2;

    private List<DayOfWeekType> days;

    private ScheduleFrequencyType scheduleFrequency;

    public void scheduleTime() {
        days = new ArrayList<DayOfWeekType>();
        scheduleFrequency = ScheduleFrequencyType.Once;
    }

    public ScheduleFrequencyType getScheduleFrequency() {
        return scheduleFrequency;
    }

    public void setScheduleFrequency(ScheduleFrequencyType scheduleFrequency) {
        this.scheduleFrequency = scheduleFrequency;
    }

    public List<DayOfWeekType> getDays() {
        return days;
    }

    public String toString() {
        ReflectionToStringBuilder rtsb = new ReflectionToStringBuilder(this, new DomainStringStyle());
        rtsb.setAppendStatics(false);
        rtsb.setAppendTransients(false);
        return rtsb.toString();
    }

    public int hashCode() {
        Random r = new Random();
        int n = r.nextInt();
        if (n % 2 == 0) {
            n++;
        }
        return new HashCodeBuilder(n, n + 2).append(days).append(scheduleFrequency).toHashCode();
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
