package net.sourceforge.greenvine.testmodel.data.entities.comparators;

import java.util.Comparator;
import net.sourceforge.greenvine.testmodel.data.entities.Employee;

public class EmployeeFirstNameComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee thisObj, Employee thatObj) {
        int cmp = thisObj.getFirstName().compareTo(thatObj.getFirstName());
        if (cmp != 0) return cmp;
        return thisObj.getEmployeeId().compareTo(thatObj.getEmployeeId());
    }
}
