package test.oued.persistance.daopojo;

import java.util.*;

public class Employee extends Person {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    private Function fonction;

    public Function getFonction() {
        return fonction;
    }

    public void setFonction(Function fonction) {
        this.fonction = fonction;
    }

    private EmployeeType employeeType;

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    private Set listAccount;

    public Set getListAccount() {
        return listAccount;
    }

    public void setListAccount(Set listAccount) {
        this.listAccount = listAccount;
    }

    private EmployeeFC employeeFC;

    public EmployeeFC getEmployeeFC() {
        return employeeFC;
    }

    public void setEmployeeFC(EmployeeFC employeeFC) {
        this.employeeFC = employeeFC;
    }
}
