package com.codebitches.spruce.module.bb.dao.hibernate;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import com.codebitches.spruce.module.bb.dao.IUserDAO;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbRank;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbTopic;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbUser;

/**
 * @author Stuart Eccles
 */
public class UserHibernateDAO extends HibernateDaoSupport implements IUserDAO {

    private static Log log = LogFactory.getLog(UserHibernateDAO.class);

    public SprucebbUser createOrUpdateUser(SprucebbUser user) throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        try {
            session.saveOrUpdate(user);
            return user;
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }

    public SprucebbUser getUserById(long id) throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        try {
            return (SprucebbUser) session.get(SprucebbUser.class, new Long(id));
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }

    public SprucebbUser findUserByUsername(String username) throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        try {
            SprucebbUser result = null;
            List userList = session.find("from com.codebitches.spruce.module.bb.domain.hibernate.SprucebbUser user where user.username=?", username, Hibernate.STRING);
            return (SprucebbUser) DataAccessUtils.uniqueResult(userList);
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }

    public Collection getAllUsers() throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        try {
            List userList = session.find("from com.codebitches.spruce.module.bb.domain.hibernate.SprucebbUser user where user.userId > 0");
            return userList;
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }

    public Collection getAllUsersSorted(String sortField, boolean asc) throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        try {
            Criteria cr = session.createCriteria(SprucebbUser.class);
            cr.add(Expression.gt("userId", new Long(0)));
            if (asc) {
                cr.addOrder(Order.asc(sortField));
            } else {
                cr.addOrder(Order.desc(sortField));
            }
            return cr.list();
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }

    public Collection getAllUserRanks() throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        try {
            List rankList = session.find("from com.codebitches.spruce.module.bb.domain.hibernate.SprucebbRank rank order by rank.rankSpecial, rank.rankMin asc");
            return rankList;
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }

    public SprucebbRank getRankById(long rankId) throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        try {
            return (SprucebbRank) session.get(SprucebbRank.class, new Long(rankId));
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }

    public SprucebbRank createOrUpdateRank(SprucebbRank rank) throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        try {
            session.saveOrUpdate(rank);
            return rank;
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }

    public void permanentlyDeleteRank(SprucebbRank rank) throws DataAccessException {
        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        try {
            session.delete(rank);
        } catch (HibernateException ex) {
            throw SessionFactoryUtils.convertHibernateAccessException(ex);
        }
    }
}
