package org.light.portlets.connection.dao.hibernate;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.light.portal.core.dao.hibernate.BaseDaoImpl;
import org.light.portal.util.StringUtil;
import org.light.portlets.connection.dao.ConnectionDao;
import org.light.portlets.connection.model.Connection;
import org.light.portlets.connection.model.Follow;

/**
 * 
 * @author Jianmin Liu
 **/
public class ConnectionDaoImpl extends BaseDaoImpl implements ConnectionDao {

    public int getBuddyCount(long userId) {
        String hql = "select count(*) from Connection buddy where buddy.userId='" + userId + "'";
        Session session = this.getHibernateTemplate().getSessionFactory().openSession();
        Query query = session.createQuery(hql);
        Long count = (Long) query.uniqueResult();
        session.close();
        return count.intValue();
    }

    public List<Connection> getBuddysByUser(long userId) {
        List<Connection> list = null;
        if (userId > 0) list = this.getHibernateTemplate().find("select buddy from Connection buddy where buddy.userId =? order by createDate desc", userId);
        return list;
    }

    public List<Connection> getBuddysByUser(long userId, String city, String province) {
        List<Connection> list = null;
        if (userId > 0) {
            if (!StringUtil.isEmpty(city) && !StringUtil.isEmpty(province)) {
                Object[] params = new Object[3];
                params[0] = userId;
                params[1] = city;
                params[2] = province;
                list = this.getHibernateTemplate().find("select buddy from Connection buddy where buddy.userId =? and buddy.connectionUser.city=? and buddy.connectionUser.province=? order by createDate desc", params);
            } else if (!StringUtil.isEmpty(province)) {
                Object[] params = new Object[2];
                params[0] = userId;
                params[1] = province;
                list = this.getHibernateTemplate().find("select buddy from Connection buddy where buddy.userId =? and buddy.connectionUser.city is null and buddy.connectionUser.province=? order by createDate desc", params);
            } else if (!StringUtil.isEmpty(city)) {
                Object[] params = new Object[2];
                params[0] = userId;
                params[1] = city;
                list = this.getHibernateTemplate().find("select buddy from Connection buddy where buddy.userId =? and buddy.connectionUser.city=? and buddy.connectionUser.province is null order by createDate desc", params);
            } else {
                list = this.getHibernateTemplate().find("select buddy from Connection buddy where buddy.userId =? and buddy.connectionUser.city is null and buddy.connectionUser.province is null order by createDate desc", userId);
            }
        }
        return list;
    }

    public List<Connection> getOnlineBuddysByUser(long userId) {
        List<Connection> list = null;
        if (userId > 0) list = this.getHibernateTemplate().find("select buddy from Connection buddy where buddy.userId =? and buddy.connectionUser.currentStatus = 1", userId);
        return list;
    }

    public List<Connection> getUpdatedBuddysByUser(long userId) {
        List<Connection> list = null;
        if (userId > 0) list = this.getHibernateTemplate().find("select buddy from Connection buddy where buddy.userId =? order by buddy.connectionUser.lastLoginDate desc", userId);
        return list;
    }

    public List<Connection> getBuddysByUserAndType(long userId, int type) {
        Object[] params = new Object[2];
        params[0] = userId;
        params[1] = type;
        List<Connection> list = null;
        if (userId > 0) list = this.getHibernateTemplate().find("select buddy from Connection buddy where buddy.userId =? and buddy.type =? order by createDate desc", params);
        return list;
    }

    public Connection getChatBuddy(long userId, long buddyUserId) {
        Connection buddy = null;
        Object[] params = new Object[2];
        params[0] = userId;
        params[1] = buddyUserId;
        List<Connection> list = this.getHibernateTemplate().find("from Connection c where c.userId=? and c.connectionUser.id =?", params);
        if (list != null && list.size() > 0) buddy = list.get(0);
        return buddy;
    }

    public List<Follow> getUserFollowings(long userId) {
        List<Follow> followings = this.getHibernateTemplate().find("from Follow userFollows where userFollows.followerId=? order by createDate asc", userId);
        return followings;
    }

    public List<Follow> getUserFollowers(long userId) {
        List<Follow> followers = this.getHibernateTemplate().find("from Follow userFollows where userFollows.userId=? order by createDate asc", userId);
        return followers;
    }

    public boolean isFollowing(long userId, long followerId) {
        String hql = "select count(*) from Follow where userId=" + userId + " and followerId=" + followerId;
        Session session = this.getHibernateTemplate().getSessionFactory().openSession();
        Query query = session.createQuery(hql);
        Long count = (Long) query.uniqueResult();
        session.close();
        return count.intValue() > 0;
    }
}
