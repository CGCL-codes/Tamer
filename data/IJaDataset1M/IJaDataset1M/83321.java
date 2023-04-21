package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.dao.filters.JoinApplicationFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class JoinApplicationDAOImpl extends EwokHibernateDAO<JoinApplication> implements com.tysanclan.site.projectewok.entities.dao.JoinApplicationDAO {

    @Override
    protected Criteria createCriteria(SearchFilter<JoinApplication> filter) {
        Criteria criteria = getSession().createCriteria(JoinApplication.class);
        if (filter instanceof JoinApplicationFilter) {
            JoinApplicationFilter jaf = (JoinApplicationFilter) filter;
            if (jaf.getApplicant() != null) {
                criteria.add(Restrictions.eq("applicant", jaf.getApplicant()));
            }
            if (jaf.getMentor() != null) {
                criteria.add(Restrictions.eq("mentor", jaf.getMentor()));
            }
            if (jaf.getJoinThread() != null) {
                criteria.add(Restrictions.eq("joinThread", jaf.getJoinThread()));
            }
            if (jaf.getDateBefore() != null) {
                criteria.add(Restrictions.lt("startDate", jaf.getDateBefore()));
            }
        }
        return criteria;
    }

    /**
	 * @see com.tysanclan.site.projectewok.entities.dao.JoinApplicationDAO#getJoinApplicationByThread(com.tysanclan.site.projectewok.entities.ForumThread)
	 */
    @Override
    public JoinApplication getJoinApplicationByThread(ForumThread thread) {
        Criteria criteria = getSession().createCriteria(JoinApplication.class);
        criteria.add(Restrictions.eq("joinThread", thread));
        criteria.setFetchSize(1);
        return (JoinApplication) criteria.uniqueResult();
    }
}
