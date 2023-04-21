package uk.co.fortunecookie.timesheet.data.entities.comparators;

import java.util.Comparator;
import uk.co.fortunecookie.timesheet.data.entities.Employee;

public class EmployeeLastNameComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee thisObj, Employee thatObj) {
        int cmp = thisObj.getLastName().compareTo(thatObj.getLastName());
        if (cmp != 0) return cmp;
        return thisObj.getEmployeeId().compareTo(thatObj.getEmployeeId());
    }
}
