package net.sf.gilead.test.dao.hibernate;

import java.util.List;
import net.sf.gilead.test.HibernateContext;
import net.sf.gilead.test.dao.IMessageDAO;
import net.sf.gilead.test.domain.interfaces.IMessage;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * DAO for message beans.
 * This implementation use HQL to work seamlessly with all implementation of the Message domain class
 * (Java 1.4 _ stateful or stateless _ and Java5)
 * @author bruno.marchesson
 *
 */
public class MessageDAO implements IMessageDAO {

    public IMessage loadLastMessage() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateContext.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Message order by date desc");
            query.setMaxResults(1);
            IMessage message = (IMessage) query.uniqueResult();
            transaction.commit();
            return message;
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public List<IMessage> loadAllMessage(int startIndex, int maxResult) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateContext.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Message order by date desc");
            query.setFirstResult(startIndex);
            query.setMaxResults(maxResult);
            List<IMessage> result = (List<IMessage>) query.list();
            transaction.commit();
            return result;
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }

    public IMessage loadDetailedMessage(Integer id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateContext.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            StringBuffer hqlQuery = new StringBuffer();
            hqlQuery.append("from Message message");
            hqlQuery.append(" inner join fetch message.author");
            hqlQuery.append(" left join fetch message.keywords");
            hqlQuery.append(" where message.id = :id");
            Query query = session.createQuery(hqlQuery.toString());
            query.setInteger("id", id);
            IMessage message = (IMessage) query.uniqueResult();
            transaction.commit();
            return message;
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }

    public void saveMessage(IMessage message) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateContext.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(message);
            transaction.commit();
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }

    public void deleteMessage(IMessage message) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateContext.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            session.delete(message);
            transaction.commit();
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }

    public void lockMessage(IMessage message) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateContext.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            if (message.getId() > 0) {
                session.lock(message, LockMode.UPGRADE);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }

    public int countAllMessages() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateContext.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("select count(*) from Message");
            int result = ((Long) query.uniqueResult()).intValue();
            transaction.commit();
            return result;
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }
}
