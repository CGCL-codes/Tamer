package com.huntersoftwaregroup.genericdataaccess.test.cayenne.data.impl;

import com.huntersoftwaregroup.genericdataaccess.test.cayenne.data.impl.auto._Employee;
import com.huntersoftwaregroup.genericdataaccess.test.cayenne.data.IEmployee;
import com.huntersoftwaregroup.genericdataaccess.test.cayenne.data.IDepartment;

public class Employee extends _Employee implements IEmployee {

    /**
     * Sets the employee's department.
     *
     * @param department the department property to set.
     */
    public void setDepartment(IDepartment department) {
        super.setDepartment((Department) department);
    }
}
