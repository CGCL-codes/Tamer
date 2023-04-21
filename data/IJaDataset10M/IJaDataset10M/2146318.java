package payroll;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class BiweeklySchedule implements PaymentSchedule {

    public boolean IsPaydate(Calendar payDate) {
        Calendar firstPayableFriday = new GregorianCalendar(2001, 11, 9);
        long tm = payDate.getTimeInMillis() - firstPayableFriday.getTimeInMillis();
        int daysSinceFirstPayableFriday = (int) tm / (24 * 60 * 60 * 1000);
        return (daysSinceFirstPayableFriday % 14) == 0;
    }

    public Calendar GetPayPeriodStartDate(Calendar payPeriodEndDate) {
        payPeriodEndDate.add(Calendar.DAY_OF_MONTH, -13);
        return payPeriodEndDate;
    }
}
