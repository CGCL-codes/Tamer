package com.ehs.common;

import com.ehs.AppFrame;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author swaram
 */
public class HibernateManager {

    private static HibernateManager hibernateManager;

    private static Logger log = AppFrame.getLogger();

    static {
        hibernateManager = new HibernateManager();
    }

    public static HibernateManager getInstance() {
        return hibernateManager;
    }

    public boolean saveObject(Object obj) {
        if (obj == null) {
            log.debug("Object (" + obj.getClass().getSimpleName() + ") is null");
            return false;
        }
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(obj);
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            log.error("Error while saving the object (" + obj.getClass().getSimpleName() + ") :" + ex.toString());
            return false;
        }
        return true;
    }

    public List<Object> getObjects(Object obj) {
        List<Object> result = new ArrayList<Object>();
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            result = session.createQuery("from " + obj.getClass().getSimpleName()).list();
            session.close();
        } catch (Exception ex) {
            log.error("Error while getting Objects:" + ex.toString());
        }
        return result;
    }

    public List<Object> getObjects(Object obj, String userid) {
        List<Object> result = new ArrayList<Object>();
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            result = session.createQuery("from " + obj.getClass().getSimpleName() + " where userid=").list();
            session.close();
        } catch (Exception ex) {
            log.error("Error while getting Objects:" + ex.toString());
        }
        return result;
    }
}
