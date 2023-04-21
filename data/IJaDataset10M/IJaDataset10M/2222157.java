package org.nakedobjects.example.expenses.employee;

import java.util.List;
import org.nakedobjects.applib.AbstractService;
import org.nakedobjects.applib.annotation.Executed;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Named;

/**
 * Defines the user actions available from the 'Employees' desktop icon or tab.
 * 
 * @author Richard
 * 
 */
@Named("Employees")
public class EmployeeStartPoints extends AbstractService {

    private static final int MAX_NUM_EMPLOYEES = 10;

    public String getId() {
        return getClass().getName();
    }

    public String iconName() {
        return Employee.class.getSimpleName();
    }

    private EmployeeRepository employeeRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected EmployeeRepository getEmployeeRepository() {
        return this.employeeRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @MemberOrder(sequence = "2")
    public List<Employee> findEmployeeByName(@Named("Name or start of Name") final String name) {
        List<Employee> results = employeeRepository.findEmployeeByName(name);
        if (results.isEmpty()) {
            warnUser("No employees found matching name: " + name);
            return null;
        } else if (results.size() > MAX_NUM_EMPLOYEES) {
            warnUser("Too many employees found matching name: " + name + "\n Please refine search.");
            return null;
        }
        return results;
    }

    @Executed(Executed.Where.LOCALLY)
    public Employee me() {
        Employee me = employeeRepository.me();
        if (me == null) {
            warnUser("No Employee representing current user");
        }
        return me;
    }
}
