package com.generalynx.ecos.data.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.dao.DataAccessException;
import org.hibernate.Filter;

public class HibernateFilterSupport extends HibernateDaoSupport implements IFilterSupport {

    public Filter enableFilter(String filterName) throws DataAccessException {
        return getSession(true).enableFilter(filterName);
    }

    public Filter getEnabledFilter(String filterName) throws DataAccessException {
        return getSession(true).getEnabledFilter(filterName);
    }

    public void disableFilter(String filterName) throws DataAccessException {
        getSession(true).disableFilter(filterName);
    }
}
