package com.gelsanalyzer.business;

import static org.hibernate.criterion.Example.create;
import java.util.*;
import org.apache.commons.logging.*;
import org.hibernate.*;
import com.gelsanalyzer.core.*;

/**
 * Home object for domain model class Department.
 * @see com.gelsanalyzer.business.Department
 * @author Hibernate Tools
 */
public class DepartmentHome {

    private static final Log log = LogFactory.getLog(DepartmentHome.class);

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public void persist(Department transientInstance) {
        log.debug("persisting Department instance");
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.persist(transientInstance);
            session.getTransaction().commit();
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void attachDirty(Department instance) {
        log.debug("attaching dirty Department instance");
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.saveOrUpdate(instance);
            session.getTransaction().commit();
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Department instance) {
        log.debug("attaching clean Department instance");
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.lock(instance, LockMode.NONE);
            session.getTransaction().commit();
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Department persistentInstance) {
        log.debug("deleting Department instance");
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.delete(persistentInstance);
            session.getTransaction().commit();
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Department merge(Department detachedInstance) {
        log.debug("merging Department instance");
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            Department result = (Department) session.merge(detachedInstance);
            session.getTransaction().commit();
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Department findById(int id) {
        log.debug("getting Department instance with id: " + id);
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            Department instance = (Department) session.get("com.gelsanalyzer.business.Department", id);
            session.getTransaction().commit();
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<Department> findByExample(Department instance) {
        log.debug("finding Department instance by example");
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            List<Department> results = (List<Department>) session.createCriteria("com.gelsanalyzer.business.Department").add(create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            session.getTransaction().commit();
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<Department> findAllDepartments() {
        log.debug("finding all Department instances");
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            List<Department> results = (List<Department>) session.createQuery("from Department").list();
            session.getTransaction().commit();
            log.debug("find All Departments successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
