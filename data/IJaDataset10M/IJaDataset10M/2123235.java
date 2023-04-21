package org.slasoi.businessManager.common.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.slasoi.businessManager.common.dao.RoleDAO;
import org.slasoi.businessManager.common.model.EmPartyRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(value = "roleDAO")
public class RoleDAOImpl extends AbstractHibernateDAOImpl<EmPartyRole, Long> implements RoleDAO {

    private static final Logger log = Logger.getLogger(RoleDAOImpl.class);

    @Autowired
    public RoleDAOImpl(SessionFactory factory) {
        setSessionFactory(factory);
    }

    @Override
    protected Class<EmPartyRole> getDomainClass() {
        return EmPartyRole.class;
    }
}
