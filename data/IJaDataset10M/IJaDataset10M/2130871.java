package com.magic.magicstore.core.entity;

import com.magic.magicstore.core.permission.User;

/**
 * The class <code>Staff</code> is a model. <br>
 * ��˾Ա���࣬ÿ��Ա�������Լ��Ĺ��� ְ�� ��н����Ϣ��
 *
 * @author  madawei
 * @version 0.1 2011-7-13
 * @since   Ver0.1
 */
public class Staff extends User {

    /**����*/
    private String staffNumber;

    /**ְ��*/
    private String position;

    /**����*/
    private String department;

    /**��н*/
    private String salary;

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
