package net.sourceforge.mededis.dataaccess.hibernate;

import net.sourceforge.mededis.central.MDException;
import net.sourceforge.mededis.central.UserException;
import net.sourceforge.mededis.dataaccess.DataAccessFactory;
import net.sourceforge.mededis.dataaccess.LogEventAccess;
import net.sourceforge.mededis.model.User;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import java.util.List;
import java.util.Collections;

/**
 * Hibernate access to LogEvents.
 */
public class HibernateLogEventAccess extends HibernateAccessBase implements LogEventAccess {

    public HibernateLogEventAccess(User user, String name) throws MDException {
        super(user, name);
    }

    public List findByUsername(String username) throws MDException {
        Session dataSession = null;
        Transaction trans = null;
        try {
            dataSession = ((HibernateAccessFactory) DataAccessFactory.getInstance()).getDataSession();
            trans = dataSession.beginTransaction();
            List list = dataSession.find("from " + forClass + " where username=?", username, Hibernate.STRING);
            trans.commit();
            trans = null;
            return list;
        } catch (HibernateException e) {
            rollback(trans);
            throw new UserException(user, e);
        } finally {
            closeDataSession(dataSession);
        }
    }

    public List findSessionIds(String username) throws MDException {
        Session dataSession = null;
        Transaction trans = null;
        try {
            dataSession = ((HibernateAccessFactory) DataAccessFactory.getInstance()).getDataSession();
            trans = dataSession.beginTransaction();
            List list = dataSession.find("select le.sessionId from " + forClass + " le " + " where le.username=? ", username, Hibernate.STRING);
            trans.commit();
            trans = null;
            return list;
        } catch (HibernateException e) {
            rollback(trans);
            throw new UserException(user, e);
        } finally {
            closeDataSession(dataSession);
        }
    }

    public Long findLastSessionId(String username) throws MDException {
        List list = findSessionIds(username);
        Long ret = null;
        if (list != null && list.size() > 0) {
            ret = (Long) Collections.max(list);
        }
        return ret;
    }

    public List findBySessionId(Long sessionId) throws MDException {
        Session dataSession = null;
        Transaction trans = null;
        try {
            dataSession = ((HibernateAccessFactory) DataAccessFactory.getInstance()).getDataSession();
            trans = dataSession.beginTransaction();
            List list = dataSession.find("from " + forClass + " le" + " where le.sessionId=?", sessionId, Hibernate.LONG);
            trans.commit();
            trans = null;
            return list;
        } catch (HibernateException e) {
            rollback(trans);
            throw new UserException(user, e);
        } finally {
            closeDataSession(dataSession);
        }
    }
}
