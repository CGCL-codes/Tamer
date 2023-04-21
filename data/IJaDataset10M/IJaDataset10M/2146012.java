package com.flash.system.core.dao;

import com.flash.system.core.entity.Account;
import com.flash.system.core.entity.Bill;
import com.flash.system.core.entity.Customer;
import com.flash.system.core.entity.Employee;
import com.flash.system.core.entity.EmployeeType;
import com.flash.system.core.entity.Item;
import com.flash.system.core.entity.Order;
import com.flash.system.core.entity.Permission;
import com.flash.system.core.entity.PreOrderForm;
import com.flash.system.core.entity.Service;
import com.flash.system.core.entity.ServiceType;
import com.flash.system.core.entity.Supplier;
import com.flash.system.core.entity.SysUser;
import com.flash.system.core.entity.Vehicle;
import com.flash.system.core.entity.VehicleCategory;
import com.flash.system.core.entity.VehicleModel;
import com.flash.system.core.entity.VehicleType;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author shan
 */
public class BaseDAO {

    @SuppressWarnings("unused")
    private static final Logger log = Logger.getAnonymousLogger();

    @SuppressWarnings("unchecked")
    private static final ThreadLocal session = new ThreadLocal();

    private static final SessionFactory sessionFactory = new AnnotationConfiguration().addPackage("com.flash.system.core.entity").addAnnotatedClass(Account.class).addAnnotatedClass(Bill.class).addAnnotatedClass(Customer.class).addAnnotatedClass(Employee.class).addAnnotatedClass(EmployeeType.class).addAnnotatedClass(Item.class).addAnnotatedClass(Order.class).addAnnotatedClass(Permission.class).addAnnotatedClass(PreOrderForm.class).addAnnotatedClass(Service.class).addAnnotatedClass(ServiceType.class).addAnnotatedClass(Supplier.class).addAnnotatedClass(Vehicle.class).addAnnotatedClass(VehicleCategory.class).addAnnotatedClass(VehicleModel.class).addAnnotatedClass(VehicleType.class).addAnnotatedClass(SysUser.class).configure().buildSessionFactory();

    @SuppressWarnings("unchecked")
    public static Session getSession() {
        Session sessionTemp = (Session) BaseDAO.session.get();
        if (sessionTemp == null) {
            sessionTemp = sessionFactory.openSession();
            BaseDAO.session.set(sessionTemp);
        }
        return sessionTemp;
    }

    protected void begin() {
        getSession().beginTransaction();
    }

    protected void commit() {
        getSession().getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    protected void rollback() {
        try {
            getSession().getTransaction().rollback();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
        }
        try {
            getSession().close();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
        }
        BaseDAO.session.set(null);
    }

    @SuppressWarnings("unchecked")
    public static void close() {
        getSession().close();
        BaseDAO.session.set(null);
    }
}
