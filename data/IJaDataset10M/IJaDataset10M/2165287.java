package net.sourceforge.gda.test.cayenne.data.impl;

import net.sourceforge.gda.test.cayenne.data.impl.auto._Department;
import net.sourceforge.gda.test.data.IDepartment;
import net.sourceforge.gda.test.data.IEmployee;
import java.util.List;

public class Department extends _Department implements IDepartment {

    /**
     * Adds the employee to the department.
     *
     * @param employee the employee property to set.
     */
    public void addEmployee(IEmployee employee) {
        addToEmployees((Employee) employee);
    }

    /**
     * Removes the employee from the department.
     *
     * @param employee the employee property to set.
     */
    public void removeEmployee(IEmployee employee) {
        removeFromEmployees((Employee) employee);
    }

    public List<IEmployee> employeeList() {
        return super.getEmployees();
    }
}
