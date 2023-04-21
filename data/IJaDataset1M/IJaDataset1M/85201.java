package de.t5book.services.impl;

import de.t5book.entities.Employee;
import de.t5book.services.EmployeeService;

public class EmployeeServiceImpl implements EmployeeService {

    private Employee employee;

    public EmployeeServiceImpl() {
        employee = new Employee(1l, "Max", "Mustermann");
    }

    public Employee findById(Long id) {
        return employee;
    }

    public Employee[] findByName(String name) {
        return new Employee[] { employee };
    }
}
