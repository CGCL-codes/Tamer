package com.slt.store;

import java.util.List;
import org.hibernate.Session;

public class SearchAllStation {

    public static List searchAllStation() {
        boolean bool = false;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Station").list();
        session.getTransaction().commit();
        return result;
    }
}
