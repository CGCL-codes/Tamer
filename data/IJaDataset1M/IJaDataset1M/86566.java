package com.homeautomate.manager;

import com.homeautomate.dao.IUnitDao;
import com.homeautomate.unit.AbstractUnit;

public class UnitManager implements IUnitManager {

    IUnitDao dao;

    public IUnitDao getDao() {
        return dao;
    }

    public void setDao(IUnitDao dao) {
        this.dao = dao;
    }

    public AbstractUnit getUnitById(Integer id) {
        return dao.findById(AbstractUnit.class, id);
    }

    public void save(AbstractUnit unit) {
        getDao().save(unit);
    }
}
