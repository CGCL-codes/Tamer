package org.slasoi.businessManager.common.dao.impl;

import org.hibernate.SessionFactory;
import org.slasoi.businessManager.common.dao.PartyRoleDAO;
import org.slasoi.businessManager.common.model.EmPartyPartyrole;
import org.slasoi.businessManager.common.model.EmPartyPartyroleId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(value = "partyRoleDAO")
public class PartyRoleDAOImpl extends AbstractHibernateDAOImpl<EmPartyPartyrole, EmPartyPartyroleId> implements PartyRoleDAO {

    @Autowired
    public PartyRoleDAOImpl(SessionFactory factory) {
        setSessionFactory(factory);
    }

    @Override
    protected Class<EmPartyPartyrole> getDomainClass() {
        return EmPartyPartyrole.class;
    }
}
