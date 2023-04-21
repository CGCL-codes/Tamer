package org.jbudget.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.jbudget.Core.Budget;
import org.jbudget.Core.Month;
import org.jbudget.io.DataIndex;
import static org.junit.Assert.*;
import org.jbudget.Core.Account;
import org.jbudget.util.Pair;

/**
 *
 * @author petrov
 */
public class DataIndexTest {

    Budget budget1 = new Budget("Budget 1");

    Budget budget2 = new Budget("Budget 2");

    Budget budget3 = new Budget("Budget 3");

    Account account1 = new Account("account 1");

    Account account2 = new Account("account 2");

    Account account3 = new Account("account 3");

    DataIndex dataIndex1;

    DataIndex dataIndex2;

    List<Month> months;

    /** Creates a new instance of DataIndexTest */
    public DataIndexTest() {
    }

    /** Recreating budgets before every test */
    @Before
    public void setUp() {
        dataIndex1 = new DataIndex(true);
        dataIndex2 = new DataIndex(false);
        dataIndex1.addBudget(budget1);
        dataIndex1.addBudget(budget2);
        dataIndex1.addBudget(budget3);
        dataIndex2.registerBudget(budget1.getID(), budget1.getName());
        dataIndex2.registerBudget(budget2.getID(), budget2.getName());
        dataIndex2.registerBudget(budget3.getID(), budget3.getName());
        dataIndex1.addAccount(account1);
        dataIndex1.addAccount(account2);
        dataIndex1.addAccount(account3);
        dataIndex2.registerAccount(account1.getID(), account1.getName());
        dataIndex2.registerAccount(account2.getID(), account2.getName());
        dataIndex2.registerAccount(account3.getID(), account3.getName());
        Random rnd = new Random(System.currentTimeMillis());
        months = new ArrayList<Month>();
        int year = 2000 + (int) (rnd.nextFloat() * 10);
        for (int m = Calendar.JANUARY; m <= Calendar.DECEMBER; m++) {
            Month month = new Month(budget1, m, year);
            months.add(month);
            dataIndex1.addMonth(month);
            dataIndex2.registerMonth(m, year);
        }
        year = 2020 + (int) (rnd.nextFloat() * 10);
        for (int m = Calendar.JANUARY; m <= Calendar.DECEMBER; m++) {
            Month month = new Month(budget2, m, year);
            months.add(month);
            dataIndex1.addMonth(month);
            dataIndex2.registerMonth(m, year);
        }
        year = 2050 + (int) (rnd.nextFloat() * 10);
        for (int m = Calendar.JANUARY; m <= Calendar.DECEMBER; m++) {
            Month month = new Month(budget3, m, year);
            months.add(month);
            dataIndex1.addMonth(month);
            dataIndex2.registerMonth(m, year);
        }
    }

    /** Test that things work as expeced */
    @Test
    public void test1() {
        for (Month month : months) {
            int cYear = month.getYear();
            int cMonth = month.getMonth();
            assertTrue(dataIndex1.isValidMonth(cMonth, cYear));
            assertTrue(dataIndex2.isValidMonth(cMonth, cYear));
            assertTrue(dataIndex1.getValidYears().contains(new Integer(cYear)));
            assertTrue(dataIndex2.getValidYears().contains(new Integer(cYear)));
            assertTrue(dataIndex1.getValidMonths(cYear).contains(new Integer(cMonth)));
            assertTrue(dataIndex2.getValidMonths(cYear).contains(new Integer(cMonth)));
            assertEquals(month, dataIndex1.getMonth(cMonth, cYear));
            dataIndex1.removeMonth(month);
            dataIndex2.unregisterMonth(cMonth, cYear);
        }
        List<Pair<Integer, String>> budgetIndices = dataIndex1.getValidBudgets();
        assertTrue(budgetIndices.contains(new Pair(new Integer(budget1.getID()), budget1.getName())));
        assertTrue(budgetIndices.contains(new Pair(new Integer(budget2.getID()), budget2.getName())));
        assertTrue(budgetIndices.contains(new Pair(new Integer(budget3.getID()), budget3.getName())));
        List<Pair<Integer, String>> accountIndices = dataIndex1.getAccountInfo();
        assertTrue(accountIndices.contains(new Pair(new Integer(account1.getID()), account1.getName())));
        assertTrue(accountIndices.contains(new Pair(new Integer(account2.getID()), account2.getName())));
        assertTrue(accountIndices.contains(new Pair(new Integer(account3.getID()), account3.getName())));
        assertTrue(dataIndex1.getValidYears().isEmpty());
        assertTrue(dataIndex2.getValidYears().isEmpty());
        for (Month month : months) {
            int cYear = month.getYear();
            int cMonth = month.getMonth();
            assertFalse(dataIndex1.isValidMonth(cMonth, cYear));
            assertFalse(dataIndex2.isValidMonth(cMonth, cYear));
        }
    }

    /** Make sure that there are no valid months/budgets in an emty budget */
    @Test
    public void emptyTests() {
        DataIndex dataIndex = new DataIndex(true);
        assertFalse(dataIndex.isValidMonth(Calendar.NOVEMBER, 2007));
        assertFalse(dataIndex.isValidMonth(Calendar.JULY, 2003));
        assertTrue(dataIndex.getValidYears().isEmpty());
        assertTrue(dataIndex.getValidBudgets().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test2() {
        DataIndex dataIndex = new DataIndex(false);
        dataIndex.addMonth(new Month(budget1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test3() {
        DataIndex dataIndex = new DataIndex(false);
        dataIndex.removeMonth(new Month(budget1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test4() {
        DataIndex dataIndex = new DataIndex(true);
        dataIndex.registerMonth(2006, Calendar.JANUARY);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test5() {
        DataIndex dataIndex = new DataIndex(true);
        dataIndex.unregisterMonth(2007, Calendar.JANUARY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test6() {
        DataIndex dataIndex = new DataIndex(false);
        dataIndex.unregisterMonth(2007, Calendar.JANUARY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test7() {
        DataIndex dataIndex = new DataIndex(true);
        dataIndex.removeMonth(new Month(budget2));
    }
}
