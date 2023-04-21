package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.BattleNetUserPresence;
import com.tysanclan.site.projectewok.entities.dao.filters.BattleNetUserPresenceFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class BattleNetUserPresenceDAOImpl extends EwokHibernateDAO<BattleNetUserPresence> implements com.tysanclan.site.projectewok.entities.dao.BattleNetUserPresenceDAO {

    @Override
    protected Criteria createCriteria(SearchFilter<BattleNetUserPresence> filter) {
        Criteria criteria = getSession().createCriteria(BattleNetUserPresence.class);
        if (filter instanceof BattleNetUserPresenceFilter) {
            BattleNetUserPresenceFilter cf = (BattleNetUserPresenceFilter) filter;
            criteria.add(Restrictions.eq("channel", cf.getChannel()));
            if (cf.getAccountName() != null) {
                criteria.add(Restrictions.eq("username", cf.getAccountName()));
            }
        }
        return criteria;
    }
}
