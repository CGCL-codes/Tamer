package net.sourceforge.greenvine.testmodel.data.entities.comparators;

import java.util.Comparator;
import net.sourceforge.greenvine.testmodel.data.entities.Employee;

public class EmployeeHourlyCostComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee thisObj, Employee thatObj) {
        int cmp = thisObj.getHourlyCost().compareTo(thatObj.getHourlyCost());
        if (cmp != 0) return cmp;
        return thisObj.getEmployeeId().compareTo(thatObj.getEmployeeId());
    }
}
