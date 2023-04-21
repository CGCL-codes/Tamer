package com.sitescape.team.module.sample.impl;

import java.util.Collections;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import com.sitescape.team.context.request.RequestContextHolder;
import com.sitescape.team.domain.User;
import com.sitescape.team.module.impl.CommonDependencyInjection;
import com.sitescape.team.module.sample.Employee;
import com.sitescape.team.module.sample.EmployeeModule;
import com.sitescape.team.module.sample.SalaryMaxedOutException;
import com.sitescape.team.util.NLT;

public class EmployeeModuleImpl extends CommonDependencyInjection implements EmployeeModule {

    private SortedMap employees = Collections.synchronizedSortedMap(new TreeMap());

    private static final int MAX_SALARY = 10000;

    public EmployeeModuleImpl() {
        addEmployee("Dave", "Griffin", new Integer(MAX_SALARY));
        addEmployee("Peter", "Hurley", new Integer(MAX_SALARY));
        addEmployee("Roy", "Klein", new Integer(MAX_SALARY));
        addEmployee("Janet", "McCann", new Integer(MAX_SALARY));
        addEmployee("Meyer", "Billmers", new Integer(MAX_SALARY));
        addEmployee("Mary", "Utt", new Integer(MAX_SALARY));
        addEmployee("Jong", "Kim", new Integer(MAX_SALARY));
    }

    public Employee getEmployee(Integer key) {
        return (Employee) this.employees.get(key);
    }

    public SortedSet getAllEmployees() {
        User user = RequestContextHolder.getRequestContext().getUser();
        return (SortedSet) new TreeSet(this.employees.values());
    }

    public int addEmployee(Employee employee) {
        int key;
        synchronized (employees) {
            if (employees.isEmpty()) key = 1; else key = ((Integer) employees.lastKey()).intValue() + 1;
            Integer keyObj = new Integer(key);
            employee.setKey(keyObj);
            this.employees.put(keyObj, employee);
        }
        return key;
    }

    public int addEmployee(String author, String title, Integer count) {
        Employee employee = new Employee(author, title, count);
        return addEmployee(employee);
    }

    public void incrementSalary(Integer key, Integer increment) {
        Employee employee = getEmployee(key);
        if (increment.intValue() > 0 && (employee.getSalary().intValue() + increment.intValue() > MAX_SALARY)) throw new SalaryMaxedOutException(employee.getFirstName(), MAX_SALARY);
        employee.incrementSalary(increment);
        User user = RequestContextHolder.getRequestContext().getUser();
    }

    public void updateEmployee(Employee employee) {
        this.employees.put(employee.getKey(), employee);
    }

    public void deleteEmployee(Integer key) {
        this.employees.remove(key);
    }

    public void deleteEmployee(Employee employee) {
        deleteEmployee(employee.getKey());
    }
}
