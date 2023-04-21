package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.CompoundVote;
import com.tysanclan.site.projectewok.entities.dao.filters.CompoundVoteFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class CompoundVoteDAOImpl extends EwokHibernateDAO<CompoundVote> implements com.tysanclan.site.projectewok.entities.dao.CompoundVoteDAO {

    /**
	 * @see com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO#createCriteria(com.tysanclan.site.projectewok.dataaccess.SearchFilter)
	 */
    @Override
    protected Criteria createCriteria(SearchFilter<CompoundVote> filter) {
        Criteria criteria = getSession().createCriteria(CompoundVote.class);
        if (filter instanceof CompoundVoteFilter) {
            CompoundVoteFilter cf = (CompoundVoteFilter) filter;
            if (cf.getVoter() != null) {
                criteria.add(Restrictions.eq("caster", cf.getVoter()));
            }
            if (cf.getElection() != null) {
                criteria.add(Restrictions.eq("election", cf.getElection()));
            }
        }
        return criteria;
    }
}
