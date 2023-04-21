package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.UntenabilityVoteChoice;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class UntenabilityVoteChoiceDAO extends EwokHibernateDAO<UntenabilityVoteChoice> implements com.tysanclan.site.projectewok.entities.dao.UntenabilityVoteChoiceDAO {

    @Override
    protected Criteria createCriteria(SearchFilter<UntenabilityVoteChoice> filter) {
        Criteria criteria = getSession().createCriteria(UntenabilityVoteChoice.class);
        return criteria;
    }
}
