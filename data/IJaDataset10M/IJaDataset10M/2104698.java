package org.demis.orc.emailRecipient;

import java.util.Collection;
import org.demis.orc.*;
import org.hibernate.criterion.*;
import org.hibernate.*;

public class EmailRecipientDAOHibernateImpl implements EmailRecipientDAO {

    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(EmailRecipientDAOHibernateImpl.class);

    public EmailRecipient findById(java.lang.String emailRecipientId) {
        EmailRecipient result = null;
        try {
            result = (EmailRecipient) getCurrentSession().get(EmailRecipient.class, emailRecipientId);
        } catch (HibernateException he) {
        }
        return result;
    }

    public Collection<EmailRecipient> findByExemple(final EmailRecipient emailRecipient) {
        Criteria criteria = getCriteria(emailRecipient);
        Collection result = null;
        try {
            result = criteria.list();
        } catch (HibernateException he) {
        }
        return (Collection<EmailRecipient>) result;
    }

    public int findCount(final EmailRecipient emailRecipient) {
        return 0;
    }

    public void save(EmailRecipient emailRecipient) {
        getCurrentSession().saveOrUpdate(emailRecipient);
    }

    public void saveAll(final Collection<EmailRecipient> emailRecipients) {
        for (EmailRecipient emailRecipient : emailRecipients) {
            getCurrentSession().saveOrUpdate(emailRecipient);
        }
    }

    public void delete(EmailRecipient emailRecipient) {
        getCurrentSession().delete(emailRecipient);
    }

    public void deleteAll(final Collection<EmailRecipient> emailRecipients) {
        for (EmailRecipient emailRecipient : emailRecipients) {
            getCurrentSession().delete(emailRecipient);
        }
    }

    private Criteria getCriteria(final EmailRecipient emailRecipient) {
        Criteria criteria = getCurrentSession().createCriteria(EmailRecipient.class);
        if (emailRecipient != null) {
            criteria.add(getExample(emailRecipient));
        }
        return criteria;
    }

    private Example getExample(final EmailRecipient emailRecipient) {
        Example example = Example.create(emailRecipient);
        return example;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
