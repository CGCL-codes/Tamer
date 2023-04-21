package com.festo.dao;

import hibernate.Achdat;
import hibernate.DbOrac;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.festo.models.TableBean;

public class DAO {

    private static final Log log = LogFactory.getLog(DAO.class);

    private SessionFactory sessionFactory;

    private SessionFactory sessionFactory_oracle;

    public DAO() {
        try {
            log.debug("Initializing Hibernate");
            System.out.println("------------------------------------------------------");
            System.out.println("Initializing Hibernate");
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            System.out.println("Finished Initializing Hibernate");
            System.out.println("------------------------------------------------------");
        } catch (HibernateException ex) {
            ex.printStackTrace();
            System.exit(5);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public List<Object> getDbNames() {
        String hql;
        hql = "select distinct orac.datenbank from DbOrac orac order by orac.datenbank asc";
        List<Object> names = new ArrayList<Object>();
        Transaction trx = null;
        log.debug("getting Table query");
        try {
            trx = getSessionFactory().getCurrentSession().beginTransaction();
            Query q = getSessionFactory().getCurrentSession().createQuery(hql);
            names = q.list();
            trx.commit();
            log.debug("query successful");
        } catch (RuntimeException re) {
            log.error("query failed", re);
            throw re;
        }
        return names;
    }

    public List<DbOrac> getTest() {
        String hql;
        hql = "from DbOrac";
        List<DbOrac> table1 = new ArrayList<DbOrac>();
        Transaction trx = null;
        log.debug("getting Table query");
        try {
            trx = getSessionFactory().getCurrentSession().beginTransaction();
            Query q = getSessionFactory().getCurrentSession().createQuery(hql);
            table1 = q.list();
            trx.commit();
            log.debug("query successful");
        } catch (RuntimeException re) {
            log.error("query failed", re);
            throw re;
        }
        return table1;
    }

    public List<TableBean> getTable() {
        String hql;
        hql = "select new com.festo.models.TableBean(orac.datenbank,ach.achsName,orac.einheit) from DbOrac orac, Achdat ach where ach.nummer=orac.achdat.nummer";
        List<TableBean> table = new ArrayList<TableBean>();
        Transaction trx = null;
        log.debug("getting Table query");
        try {
            trx = getSessionFactory().getCurrentSession().beginTransaction();
            Query q = getSessionFactory().getCurrentSession().createQuery(hql);
            table = q.list();
            trx.commit();
            log.debug("query successful");
        } catch (RuntimeException re) {
            log.error("query failed", re);
            throw re;
        }
        return table;
    }

    public List<TableBean> getConverterTable(String datenbank) {
        String hql;
        hql = "select new com.festo.models.TableBean(orac.text, orac.name, ach.achsName,orac.einheit) from DbOrac orac, Achdat ach where ach.nummer=orac.achdat.nummer and orac.datenbank='" + datenbank + "'";
        List<TableBean> table = new ArrayList<TableBean>();
        Transaction trx = null;
        log.debug("getting Table query");
        try {
            trx = getSessionFactory().getCurrentSession().beginTransaction();
            Query q = getSessionFactory().getCurrentSession().createQuery(hql);
            table = q.list();
            trx.commit();
            log.debug("query successful");
        } catch (RuntimeException re) {
            log.error("query failed", re);
            throw re;
        }
        return table;
    }

    public void insert() {
        String hql;
        Transaction trx = null;
        log.debug("getting Table query");
        try {
            trx = getSessionFactory().getCurrentSession().beginTransaction();
            hql = "INSERT INTO achdat (nummer,prio, achs_durch) select '13', prio, achs_durch from achdat where nummer='5'";
            Query q = getSessionFactory().getCurrentSession().createSQLQuery(hql);
            int a = q.executeUpdate();
            trx.commit();
            log.debug("query successful");
        } catch (RuntimeException re) {
            log.error("query failed", re);
            throw re;
        }
    }
}
