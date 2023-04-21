package com.org.daoImp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.org.dao.MenuInterface;
import com.org.model.Department;
import com.org.model.Designation;
import com.org.model.Employee;
import com.org.util.HibernateUtil;

public class MenuDao implements MenuInterface {

    @SuppressWarnings("unchecked")
    public ArrayList<Employee> empProfile() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        ArrayList<Employee> arrayList = new ArrayList<Employee>();
        try {
            transaction = session.beginTransaction();
            List<Employee> empList = session.createQuery("from Employee").list();
            for (Iterator<Employee> iterator = empList.iterator(); iterator.hasNext(); ) {
                Employee emp = iterator.next();
                emp.getEmployeeName();
                emp.getEmpId();
                arrayList.add(emp);
            }
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return arrayList;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Department> departmentMethod() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        ArrayList<Department> deptList = new ArrayList<Department>();
        try {
            transaction = session.beginTransaction();
            List<Department> list = session.createQuery("from Department").list();
            for (Iterator<Department> iterator = list.iterator(); iterator.hasNext(); ) {
                Department dept = iterator.next();
                dept.getDept_name();
                dept.getDept_id();
                deptList.add(dept);
            }
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return deptList;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Designation> designationMethod() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        ArrayList<Designation> desigList = new ArrayList<Designation>();
        try {
            transaction = session.beginTransaction();
            List<Designation> list = session.createQuery("from Designation").list();
            for (Iterator<Designation> it = list.iterator(); it.hasNext(); ) {
                Designation desig = it.next();
                desig.getDesig_id();
                desig.getDesign_name();
                desigList.add(desig);
            }
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return desigList;
    }
}
