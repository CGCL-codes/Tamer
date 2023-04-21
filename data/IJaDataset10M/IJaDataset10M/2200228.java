package org.encuestame.persistence.dao.imp;

import java.util.List;
import org.encuestame.persistence.dao.IGroupDao;
import org.encuestame.persistence.domain.security.Account;
import org.encuestame.persistence.domain.security.Group;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

/**
 * Security Group Dao.
 * @author Picado, Juan Carlos juanATencuestame.org
 * @since May 05, 2009
 * @version $Id$
 */
@Repository("groupDaoImp")
public class GroupDaoImp extends AbstractHibernateDaoSupport implements IGroupDao {

    @Autowired
    public GroupDaoImp(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @SuppressWarnings("unchecked")
    public List<Group> findAllGroups() {
        return getHibernateTemplate().find("from Group");
    }

    /**
     * Load Groups By User.
     * @param secUsers {@link Account}.
     * @return list of groups.
     */
    @SuppressWarnings("unchecked")
    public List<Group> loadGroupsByUser(final Account user) {
        return getHibernateTemplate().findByNamedParam("from Group where account = :user ", "user", user);
    }

    /**
     *
     */
    public Group getGroupById(final Long groupId) throws HibernateException {
        return (Group) getHibernateTemplate().get(Group.class, groupId);
    }

    /**
     * Get Group by Id and User.
     * @param groupId group id
     * @param secUser {@link Account}
     * @return
     */
    @SuppressWarnings("unchecked")
    public Group getGroupById(final Long groupId, final Account secUser) {
        return (Group) DataAccessUtils.uniqueResult(getHibernateTemplate().findByNamedParam("from Group where groupId = :groupId and  account = :account", new String[] { "groupId", "account" }, new Object[] { groupId, secUser }));
    }

    /**
     * Find group by Id.
     * @param groupId group id.
     * @return group
     */
    public Group find(final Long groupId) {
        return (Group) getHibernateTemplate().get(Group.class, groupId);
    }

    /**
     * Get Group by Id and User.
     * @param groupId group id
     * @param secUser {@link Account}
     * @return
     */
    @SuppressWarnings("unchecked")
    public Group getGroupByIdandUser(final Long groupId, final Long userId) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(Group.class);
        criteria.createAlias("account", "account");
        criteria.add(Restrictions.eq("account.uid", userId));
        criteria.add(Restrictions.eq("tweetPollId", groupId));
        return (Group) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    /**
     * Counter Users by Group
     * @param GroupId
     * @return
     */
    @SuppressWarnings("unchecked")
    public Long getCountUserbyGroup(final Long groupId) {
        List<Long> counter = getHibernateTemplate().findByNamedParam("select count(uid) " + " from UserAccount where group.groupId = :groupId", "groupId", groupId);
        return counter.get(0);
    }

    /**
    * Get Users by Groups.
    * @param user
    * @return
    */
    @SuppressWarnings("unchecked")
    public List<Object[]> getUsersbyGroups(final Account user) {
        return getHibernateTemplate().findByNamedParam("SELECT sg.groupName, COUNT(scu.group.groupId) " + "FROM UserAccount as scu, Group as sg " + "WHERE scu.group.groupId = sg.groupId AND " + "scu.account = :account " + "GROUP BY sg.groupName", "account", user);
    }

    /**
     * Get Users by Groups
     * @param user
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> countUsersbyGroups(final Long user) {
        return getHibernateTemplate().findByNamedParam("SELECT sg.groupName, COUNT(scu.group.groupId) " + "FROM UserAccount as scu, Group as sg " + "WHERE scu.group.groupId = sg.groupId AND " + "scu.account.uid = :account " + "GROUP BY sg.groupName", "account", user);
    }
}
