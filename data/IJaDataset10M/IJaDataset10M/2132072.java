package com.semp.jadoma.core.dao;

import org.hibernate.Session;
import com.semp.jadoma.core.model.Family;
import com.semp.jadoma.core.util.HibernateUtil;

public class FamilyDAO {

    public static final Family find(Long familyId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Family toReturn = (Family) session.get(Family.class, familyId);
        session.close();
        return toReturn;
    }

    public static final void update(Family family) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(family);
        session.getTransaction().commit();
        session.close();
    }
}
