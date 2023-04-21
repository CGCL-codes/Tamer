package controller;

import java.util.ArrayList;
import java.util.List;
import entities.*;
import javax.ejb.Stateless;
import crud.EmployeeCRUD;

/**
 * Session Bean implementation class EmployeeDAO
 */
@Stateless
public class EmployeeDAO implements EmployeeDAORemote {

    /**
     * Default constructor. 
     */
    public EmployeeDAO() {
    }

    public Employee readEmp(int idEmpoyee) {
        Employee emp = new Employee();
        EmployeeCRUD EC = new EmployeeCRUD();
        emp = EC.readById(idEmpoyee);
        return emp;
    }

    public List<Employee> getAllEmp() {
        List<Employee> list = new ArrayList<Employee>();
        EmployeeCRUD EC = new EmployeeCRUD();
        list = EC.readAll();
        return list;
    }

    public String creatEmp(int idEmployee, String idUser, String password, String firstName, String lastName, String emgContact, String address) {
        User user = new User(idUser, password, "emp");
        Employee emp = new Employee(address, emgContact, firstName, user, lastName);
        EmployeeCRUD EC = new EmployeeCRUD();
        String msg = EC.create_Employee_User(user, emp);
        return msg;
    }

    public String updateEmp(int idEmployee, String firstName, String lastName, String emgContact, String address) {
        EmployeeCRUD EC = new EmployeeCRUD();
        Employee emp_old = EC.readById(idEmployee);
        emp_old.setAddress(address);
        emp_old.setEmgContact(emgContact);
        emp_old.setFirstName(firstName);
        emp_old.setLastName(lastName);
        String msg = EC.update(emp_old);
        return msg;
    }

    public String deleteEmp(int idEmployee) {
        EmployeeCRUD EC = new EmployeeCRUD();
        Employee emp = EC.readById(idEmployee);
        String msg = EC.delete(emp);
        return msg;
    }
}
