package br.com.arsmachina.authentication.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import br.com.arsmachina.authentication.dao.UserGroupDAO;
import br.com.arsmachina.authentication.entity.UserGroup;
import br.com.arsmachina.dao.SortCriterion;
import br.com.arsmachina.dao.hibernate.GenericDAOImpl;

/**
 * {@link UserGroupDAO} implementation using Hibernate
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class UserGroupDAOImpl extends GenericDAOImpl<UserGroup, Integer> implements UserGroupDAO {

    /**
	 * Single constructor of this class.
	 * 
	 * @param sessionFactory a {@link SessionFactory}. It cannot be null.
	 */
    public UserGroupDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
	 * Returns {@link Constants#ASCENDING_NAME_SORT_CRITERIA}.
	 * 
	 * @see br.com.arsmachina.dao.hibernate.GenericDAOImpl#getDefaultSortCriteria()
	 */
    @Override
    public SortCriterion[] getDefaultSortCriteria() {
        return Constants.ASCENDING_NAME_SORT_CRITERIA;
    }

    /**
	 * @see br.com.arsmachina.authentication.dao.UserGroupDAO#findByName(java.lang.String)
	 */
    public UserGroup findByName(String name) {
        final Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("name", name));
        return (UserGroup) criteria.uniqueResult();
    }
}
