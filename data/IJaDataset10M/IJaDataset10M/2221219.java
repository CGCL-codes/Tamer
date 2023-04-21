package org.nakedobjects.app.budget;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.value.Date;

public class EveryMonth extends AbstractDomainObject implements When {

    private int dayOfMonth;

    public int getDayOfMonth() {
        resolve();
        return dayOfMonth;
    }

    public String validateDayOfMonth(int dayOfMonth) {
        if (dayOfMonth >= 1 && dayOfMonth <= 31) {
            return null;
        } else {
            return "Day must be between 1 and 31 inclusive";
        }
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        objectChanged();
    }

    public String toString() {
        return "Monthly (" + dayOfMonth + ")";
    }

    public float applyFor(Date start, Date end) {
        Date date = new Date(start.getYear(), start.getMonth(), dayOfMonth);
        return apply(date, start, end);
    }

    private float apply(Date date, Date start, Date end) {
        boolean includesDay = date.isGreaterThanOrEqualTo(start) && date.isLessThanOrEqualTo(end);
        if (includesDay) {
            Date startNextMonth = start.add(0, 1, 0);
            return 1.0f + applyFor(startNextMonth, end);
        } else {
            return 0.0f;
        }
    }
}
