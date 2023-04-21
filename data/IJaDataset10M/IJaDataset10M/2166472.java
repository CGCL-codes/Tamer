package com.tysanclan.site.projectewok.entities.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.dao.filters.GameAccountFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GameAccountDAOImpl extends EwokHibernateDAO<GameAccount> implements com.tysanclan.site.projectewok.entities.dao.GameAccountDAO {

    @Override
    protected Criteria createCriteria(SearchFilter<GameAccount> filter) {
        Criteria criteria = getSession().createCriteria(GameAccount.class);
        if (filter instanceof GameAccountFilter) {
            GameAccountFilter cf = (GameAccountFilter) filter;
            if (cf.getAccountName() != null) {
                criteria.add(Restrictions.eq("name", cf.getAccountName()));
            }
            if (cf.getChannelWebServiceUID() != null || cf.getBotLinked() != null) {
                criteria.createAlias("userGameRealm", "userGameRealm");
                criteria.createAlias("userGameRealm.realm", "realm");
                if (cf.getChannelWebServiceUID() != null) {
                    criteria.createAlias("realm.channels", "channel");
                    criteria.add(Restrictions.eq("channel.webServiceUserId", cf.getChannelWebServiceUID()));
                }
            }
        }
        return criteria;
    }
}
