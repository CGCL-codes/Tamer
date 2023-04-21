package org.homeunix.thecave.buddi.model;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.homeunix.thecave.buddi.i18n.keys.ScheduleFrequency;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.moss.util.DateFunctions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ScheduledTransactionTest {

    @Parameters
    public static List<Object[]> getData() {
        return Arrays.asList(new Object[][] { { ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_DAY.toString(), DateFunctions.getDate(2007, Calendar.JANUARY, 1), DateFunctions.getDate(2007, Calendar.JANUARY, 31), DateFunctions.getDate(2007, Calendar.FEBRUARY, 1), -3100l }, { ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_DAY.toString(), DateFunctions.getDate(2007, Calendar.JANUARY, 1), DateFunctions.getDate(2007, Calendar.JANUARY, 31), DateFunctions.getDate(2007, Calendar.MARCH, 1), -3100l }, { ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_WEEKDAY.toString(), DateFunctions.getDate(2007, Calendar.JANUARY, 1), DateFunctions.getDate(2007, Calendar.JANUARY, 31), DateFunctions.getDate(2007, Calendar.FEBRUARY, 1), -2300l }, { ScheduleFrequency.SCHEDULE_FREQUENCY_BIWEEKLY.toString(), DateFunctions.getDate(2007, Calendar.JANUARY, 1), DateFunctions.getDate(2007, Calendar.JANUARY, 31), DateFunctions.getDate(2007, Calendar.FEBRUARY, 1), -200l }, { ScheduleFrequency.SCHEDULE_FREQUENCY_EVERY_WEEKDAY.toString(), DateFunctions.getDate(2007, Calendar.JANUARY, 1), DateFunctions.getDate(2007, Calendar.JANUARY, 31), DateFunctions.getDate(2007, Calendar.FEBRUARY, 1), -2300l } });
    }

    private final String freq;

    private final Date start;

    private final Date end;

    private final Date test;

    private final Long expected;

    public ScheduledTransactionTest(Object freq, Object start, Object end, Object test, Object expected) {
        this.freq = (String) freq;
        this.start = (Date) start;
        this.end = (Date) end;
        this.test = (Date) test;
        this.expected = (Long) expected;
    }

    @Test
    public void testScheduledTransactions() throws Exception {
        Document d = ModelFactory.createDocument();
        AccountType at = d.getAccountType("Cash");
        Account a1 = ModelFactory.createAccount("Test1", at);
        d.addAccount(a1);
        d.addScheduledTransaction(ModelFactory.createScheduledTransaction("Test", null, start, end, freq, 0, 0, 0, "Description", 100, a1, d.getBudgetCategory("Groceries")));
        d.updateScheduledTransactions();
        d.updateAllBalances();
        assertEquals(expected, a1.getBalance((Date) test));
    }
}
