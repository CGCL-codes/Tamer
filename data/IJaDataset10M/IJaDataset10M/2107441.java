package org.nakedobjects.example.expenses.employee;

import java.util.List;

public interface EmployeeRepository {

    List<Employee> findEmployeeByName(final String name);

    Employee me();
}
