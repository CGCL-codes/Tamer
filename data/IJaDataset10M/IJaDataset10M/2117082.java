package net.firstpartners.sample.multiple;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class OoompaLoompaDate {

    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private Date repaymentDate;

    public OoompaLoompaDate() {
        super();
        repaymentDate = new Date();
    }

    public OoompaLoompaDate(long date) {
        super();
        repaymentDate = new Date(date);
    }

    public OoompaLoompaDate(Date newDate) {
        super();
        repaymentDate = newDate;
    }

    /**
	 * Create a new Date
	 * Adjusts for the fact that month is 0 based, yet year
	 * and day (date) are 1 based.
	 * @param year
	 * @param month
	 * @param date
	 */
    public OoompaLoompaDate(int year, int month, int date) {
        super();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(year, month - 1, date);
        repaymentDate = calendar.getTime();
    }

    public void rollForward(int numberOfDays) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(repaymentDate);
        calendar.add(Calendar.DATE, numberOfDays);
        repaymentDate = calendar.getTime();
    }

    public Date getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(Date repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public OoompaLoompaDate getCopy() {
        return new OoompaLoompaDate(repaymentDate.getTime());
    }

    @Override
    public String toString() {
        return dateFormat.format(repaymentDate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((repaymentDate == null) ? 0 : repaymentDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OoompaLoompaDate other = (OoompaLoompaDate) obj;
        if (repaymentDate == null) {
            if (other.repaymentDate != null) {
                return false;
            }
        }
        if (repaymentDate.getTime() != other.getRepaymentDate().getTime()) {
            return false;
        }
        return true;
    }
}
