package uk.co.fortunecookie.timesheet.data.entities.comparators;

import java.util.Comparator;
import uk.co.fortunecookie.timesheet.data.entities.Expense;

public class ExpenseDateComparator implements Comparator<Expense> {

    @Override
    public int compare(Expense thisObj, Expense thatObj) {
        int cmp = thisObj.getDate().compareTo(thatObj.getDate());
        if (cmp != 0) return cmp;
        return thisObj.getExpenseId().compareTo(thatObj.getExpenseId());
    }
}
