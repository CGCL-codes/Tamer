package com.spring66.training.dao.hibernate;

import com.spring66.training.dao.VisitDao;
import com.spring66.training.entity.Visit;
import java.util.Collection;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author TwinP
 */
public class VisitDaoHibernate extends HibernateDaoSupport implements VisitDao {

    @Override
    public void create(Visit visit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void storeVisit(Visit visit) {
        getHibernateTemplate().saveOrUpdate(visit);
    }

    @Override
    public Visit loadVisit(Integer pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Visit visit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Visit visit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Visit> readAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Visit> findOwner(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
