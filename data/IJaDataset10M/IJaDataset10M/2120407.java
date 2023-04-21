package org.bastion.example.domain;

import java.util.Date;

public class Period {

    private static final int SECONDS_PER_DAY = 24 * 3600 * 1000;

    private Date startDate;

    private Date endDate;

    private Period() {
    }

    public Period(Date startDate, Date endDate) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Period(Date startDate, int days) {
        super();
        this.startDate = startDate;
        this.endDate = new Date(startDate.getTime() + (SECONDS_PER_DAY * days));
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getDays() {
        return (int) ((endDate.getTime() - startDate.getTime()) / (SECONDS_PER_DAY));
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate;
    }

    public boolean overlaps(Period period) {
        return period.getEndDate().after(this.getStartDate()) && period.getStartDate().before(this.getEndDate());
    }
}
