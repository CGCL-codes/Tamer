package com.ideo.sweetdevria.test.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import com.ideo.sweetdevria.test.bean.Book;
import com.ideo.sweetdevria.test.hibernate.HibernateSessionRequestFilter;
import com.ideo.sweetdevria.test.hibernate.HibernateUtil;

public class DataFunction {

    private static DataFunction singleton = null;

    private List orders = new ArrayList();

    private static Log log = LogFactory.getLog(HibernateSessionRequestFilter.class);

    public DataFunction() {
        super();
    }

    public static synchronized DataFunction getInstance() {
        if (singleton == null) {
            singleton = new DataFunction();
        }
        return singleton;
    }

    public Book getBook(int id) throws Exception {
        Book product = null;
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            product = (Book) session.get(Book.class, new Long(id));
        } catch (HibernateException e) {
            log.error("getBook (" + id + ")", e);
        }
        return product;
    }

    public List getBooks() throws Exception {
        List result = new ArrayList();
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Criteria crit = session.createCriteria(Book.class);
            result = crit.list();
        } catch (HibernateException e) {
            log.error("getBooks ()", e);
        }
        return result;
    }

    public List getBooks(int idFrom, int idTo) throws Exception {
        List result = new ArrayList();
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Criteria crit = session.createCriteria(Book.class);
            crit.setFirstResult(idFrom);
            crit.setMaxResults(idTo - idFrom);
            for (int i = 0; i < orders.size(); ++i) crit.addOrder((Order) orders.get(i));
            result = crit.list();
        } catch (HibernateException e) {
            log.error("getBooks (" + idFrom + ", " + idTo + ")", e);
        }
        return result;
    }

    public Serializable insertBook(Book book) throws Exception {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            return session.save(book);
        } catch (HibernateException e) {
            log.error("insertBook (" + book + ")", e);
            return null;
        }
    }

    public void updateBook(Book book) throws Exception {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.update(book);
        } catch (HibernateException e) {
            log.error("updateBook (" + book + ")", e);
        }
    }

    public void deleteBook(Book book) throws Exception {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.delete(book);
        } catch (HibernateException e) {
            log.error("deleteBook (" + book + ")", e);
        }
    }

    public int getSize() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria crit = session.createCriteria(Book.class).setProjection(Projections.count("id"));
        return (Integer) crit.list().get(0);
    }

    public void clearOrders() {
        orders.clear();
    }

    public void addOrders(String property, boolean asc) {
        if (asc) {
            orders.add(Order.asc(property));
        } else orders.add(Order.desc(property));
    }
}
