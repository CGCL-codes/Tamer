package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.GamePetition;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GamePetitionDAO extends EwokHibernateDAO<GamePetition> implements com.tysanclan.site.projectewok.entities.dao.GamePetitionDAO {

    @Override
    protected Criteria createCriteria(SearchFilter<GamePetition> filter) {
        Criteria criteria = getSession().createCriteria(GamePetition.class);
        return criteria;
    }
}
