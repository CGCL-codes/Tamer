package org.sf.alpenstock.dao;

import java.util.*;
import org.apache.commons.logging.*;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.sf.alpenstock.vo.Process;

/**
 * Home object for domain model class Jobrole.
 *
 * @see org.sf.alpenstock.dao.Process
 * @author Hibernate Tools
 */
public class ProcessDAO {

    private static final Log log = LogFactory.getLog(ProcessDAO.class);

    private final SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();

    public long save(Process process) {
        long id = -1;
        if (process.getWorkItems().size() > 0) {
            id = this.update(process);
        } else {
            id = this.persist(process);
        }
        return id;
    }

    private long persist(Process transientInstance) {
        log.debug("persisting Jobrole instance");
        Session session = sessionFactory.getCurrentSession();
        try {
            Transaction t = session.beginTransaction();
            session.persist(transientInstance);
            t.commit();
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
        return transientInstance.getProcessId();
    }

    private long update(Process transientInstance) {
        log.debug("updating Jobrole instance");
        Session session = sessionFactory.getCurrentSession();
        try {
            Transaction t = session.beginTransaction();
            session.saveOrUpdate(transientInstance);
            t.commit();
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
        return transientInstance.getProcessId();
    }

    private void attachDirty(Process instance) {
        log.debug("attaching dirty Jobrole instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    private void attachClean(Process instance) {
        log.debug("attaching clean Jobrole instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void delete(Process persistentInstance) {
        log.debug("deleting Jobrole instance");
        Session session = sessionFactory.getCurrentSession();
        try {
            Transaction t = session.beginTransaction();
            session.delete(persistentInstance);
            t.commit();
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    private Process merge(Process detachedInstance) {
        log.debug("merging Jobrole instance");
        try {
            Process result = (Process) sessionFactory.getCurrentSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Process findById(long id) {
        log.debug("getting Process instance with id: " + id);
        Session session = sessionFactory.getCurrentSession();
        try {
            Transaction t = session.beginTransaction();
            Process instance = (Process) session.createQuery("select process from Process process left join fetch process.workItems where process.processId = :processId").setParameter("processId", id).uniqueResult();
            t.commit();
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

    public List<Process> findByExample(Process instance) {
        log.debug("finding Jobrole instance by example");
        Session session = sessionFactory.getCurrentSession();
        try {
            Transaction t = session.beginTransaction();
            List<Process> results = session.createCriteria("org.sf.alpenstock.vo.Jobrole").add(Example.create(instance)).list();
            t.commit();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List<Process> findAll() {
        log.debug("finding all Process instance");
        Session session = sessionFactory.getCurrentSession();
        try {
            Transaction t = session.beginTransaction();
            Query queryObject = session.createQuery("from Process");
            List<Process> results = queryObject.list();
            t.commit();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
