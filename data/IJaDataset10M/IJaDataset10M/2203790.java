package com.org.dao;

import java.util.ArrayList;
import com.org.model.Department;
import com.org.model.Employee;

public interface MenuInterface {

    public ArrayList<Employee> empProfile();

    public ArrayList<Department> departmentMethod();
}
