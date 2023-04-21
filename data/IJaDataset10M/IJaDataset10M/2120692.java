package org.stheos.pos.service;

import org.stheos.pos.config.CoreConfig;
import org.stheos.pos.core.HibernateInit;
import org.stheos.pos.object.Table;
import java.util.List;
import org.hibernate.Session;
import org.stheos.pos.object.Customer;

public class CustomerMgr {

    private Session hibSession;

    /** Constructor, new instance of Table */
    public CustomerMgr() {
    }

    private void loadHibernate() {
        hibSession = HibernateInit.getSessionFactory().getCurrentSession();
    }

    public static CustomerMgr getManager() {
        return new CustomerMgr();
    }

    public Customer loadCustomer(int customerID) {
        Customer cust;
        loadHibernate();
        hibSession.beginTransaction();
        cust = (Customer) hibSession.load(Customer.class, customerID);
        hibSession.getTransaction().commit();
        return cust;
    }
}
