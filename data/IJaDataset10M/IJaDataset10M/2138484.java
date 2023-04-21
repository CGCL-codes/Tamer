package ca.ubc.icics.mss.dgms.dao.hibernate;

import java.util.List;
import org.hibernate.Query;
import ca.ubc.icics.mss.dgms.dao.hibernate.GenericDAOHibernate;
import ca.ubc.icics.mss.dgms.model.Group;
import ca.ubc.icics.mss.dgms.model.GroupManagerName;
import ca.ubc.icics.mss.dgms.dao.GroupDAO;

/**
 *
 * @author shruti
 */
public class GroupDAOHibernate extends GenericDAOHibernate<Group, Integer> implements GroupDAO {

    public GroupDAOHibernate() {
        super(Group.class);
    }

    public List<GroupManagerName> getGroups() {
        return getSession().getNamedQuery("getManagerName").list();
    }

    public List<Group> findByLastName(String groupName) {
        return getHibernateTemplate().find("from Group where groupName=?", groupName);
    }

    /** 
     * {@inheritDoc}
    */
    public List<Group> getRequestedGroups() {
        return getHibernateTemplate().find("from Group where groupStatus=1");
    }

    public List<Group> getBlogs() {
        return getHibernateTemplate().find("from Group where componentId='blog' and groupStatus=3");
    }

    public List<Group> getBBs() {
        return getHibernateTemplate().find("from Group where componentId='bb' and groupStatus=3");
    }

    public List<Group> getBlogsByUser(Integer userId) {
        Query blogQuery = getSession().getNamedQuery("findAllBlogsByUserId");
        blogQuery.setParameter("userId", userId);
        return blogQuery.list();
    }

    public List<Group> getBBsByUser(Integer userId) {
        Query bbQuery = getSession().getNamedQuery("findAllBBsByUserId");
        bbQuery.setParameter("userId", userId);
        return bbQuery.list();
    }
}
