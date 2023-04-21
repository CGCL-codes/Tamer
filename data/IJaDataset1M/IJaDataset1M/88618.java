package com.fivebrms.hibernate.entity.dao;

import java.util.Collection;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import com.fivebrms.hibernate.entity.model.W9Form;

public class W9FormDaoHibernate implements W9FormDAO {

    final Logger log = LoggerFactory.getLogger(W9FormDaoHibernate.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public Collection<W9Form> findAll() {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), true);
        try {
            log.info("start W9Form find all..");
            Collection<W9Form> products = session.createQuery("from w9Form").list();
            log.info("end W9Form find all..");
            return products;
        } catch (HibernateException e) {
            throw SessionFactoryUtils.convertHibernateAccessException(e);
        }
    }

    public void createW9Form(W9Form w9Form) throws DataAccessException {
        log.info("..1..createW9Form.." + w9Form);
        log.info("..2..createW9Form.." + this.getSessionFactory());
        Session session = SessionFactoryUtils.getSession(this.getSessionFactory(), true);
        log.info("..3..createW9Form..->" + session);
        session.save(w9Form);
        log.info("..4..createW9Form.." + w9Form);
    }

    public void updateW9Form(W9Form w9Form) throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(this.getSessionFactory(), true);
        session.update(w9Form);
    }

    public void deleteW9Form(W9Form w9Form) throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(this.getSessionFactory(), true);
        session.delete(w9Form);
    }
}
