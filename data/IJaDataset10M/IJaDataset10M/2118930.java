package com.mjs_svc.possibility.controllers;

import com.mjs_svc.possibility.exceptions.PermissionDeniedException;
import com.mjs_svc.possibility.models.*;
import com.mjs_svc.possibility.util.*;
import java.util.*;
import org.hibernate.Session;

/**
 *
 * @author Matthew Scott
 * @version $Id: EmployeeDetailController.java 31 2010-04-14 22:31:28Z matthew.joseph.scott $
 */
public class EmployeeDetailController {

    public static Employee createEmployee(String username, String firstname, String lastname, String email, Position p) throws PermissionDeniedException {
        if (!UserContainer.getUser().hasPermission("create_employee")) {
            throw new PermissionDeniedException();
        }
        ResourceBundle rb = ResourceBundle.getBundle("FieldTitles", Locale.getDefault());
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.beginTransaction();
        User u = new User();
        Address a = new Address();
        Employee emp = new Employee();
        u.setUsername(username);
        u.setFirstName(firstname);
        u.setLastName(lastname);
        u.setEmail(email);
        u.setPassword(u.generateNewPasswordHash(username));
        u.setLastLogin(new Date());
        u.setDateJoined(new Date());
        a.setName(rb.getString("address.name"));
        a.setPhone(rb.getString("address.phone"));
        a.setAddress1(rb.getString("address.address1"));
        a.setCity(rb.getString("address.city"));
        a.setState(rb.getString("address.state"));
        a.setCountry("United States of America");
        emp.setUser(u);
        emp.setAddress(a);
        emp.setPosition(p);
        sess.save(u);
        sess.save(a);
        sess.save(emp);
        sess.getTransaction().commit();
        return emp;
    }

    public static Employee updateEmployee(int id, String username, String firstname, String lastname, String email, Position p) throws PermissionDeniedException {
        if (!UserContainer.getUser().hasPermission("change_employee")) {
            throw new PermissionDeniedException();
        }
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.beginTransaction();
        Employee emp = (Employee) sess.load(Employee.class, id);
        User u = emp.getUser();
        u.setUsername(username);
        u.setFirstName(firstname);
        u.setLastName(lastname);
        u.setEmail(email);
        emp.setPosition(p);
        sess.update(u);
        sess.update(emp);
        sess.getTransaction().commit();
        return emp;
    }

    public static void deleteEmployee(int id) {
        Session sess = HibernateUtil.getSessionFactory().getCurrentSession();
        sess.beginTransaction();
        Employee emp = (Employee) sess.load(Employee.class, id);
        sess.delete(emp);
        sess.getTransaction().commit();
    }
}
