package org.pyre.foundry.global.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class GlobalSessionDaoSupport extends HibernateDaoSupport {

    @Autowired
    @Qualifier("globalSessionFactory")
    public final void setSessionFactoryAuto(SessionFactory sf) {
        super.setSessionFactory(sf);
    }
}
