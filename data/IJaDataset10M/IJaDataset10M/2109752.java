package com.techstar.dmis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.techstar.framework.dao.IBaseHbnDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dao.IFsLflinkeynumrelDao;
import com.techstar.dmis.entity.FsLflinkeynumrel;

public class FsLflinkeynumrelDaoImpl implements IFsLflinkeynumrelDao {

    private IBaseHbnDao baseHbnDao;

    public void delete(FsLflinkeynumrel fsLflinkeynumrel) {
        baseHbnDao.delete(fsLflinkeynumrel);
    }

    public void deleteAll(List pos) {
        baseHbnDao.deleteAll(pos);
    }

    public void saveOrUpdate(FsLflinkeynumrel fsLflinkeynumrel) {
        baseHbnDao.saveOrUpdate("FsLflinkeynumrel", fsLflinkeynumrel);
    }

    public FsLflinkeynumrel findByPk(Object frelid) {
        return (FsLflinkeynumrel) baseHbnDao.findById(FsLflinkeynumrel.class, (Serializable) frelid);
    }

    public QueryListObj getQueryList() {
        return baseHbnDao.getQueryListByEntityName("FsLflinkeynumrel");
    }

    public QueryListObj getQueryList(int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByEntityName("FsLflinkeynumrel", beginPage, pageSize);
    }

    public void merge(FsLflinkeynumrel fsLflinkeynumrel) {
        baseHbnDao.merge("FsLflinkeynumrel", fsLflinkeynumrel);
    }

    public QueryListObj getQueryListByHql(String hql) {
        return baseHbnDao.getQueryListByHql(hql);
    }

    public QueryListObj getQueryListByHql(String hql, int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByHql(hql, beginPage, pageSize);
    }

    public List getObjPropertySums(String sql) {
        List result = new ArrayList();
        if (sql != null && !"".equals(sql)) result = baseHbnDao.queryList(sql);
        return result;
    }

    public void saveOrUpdateAll(Collection objs) {
        baseHbnDao.saveOrUpdateAll(objs);
    }

    public IBaseHbnDao getBaseHbnDao() {
        return baseHbnDao;
    }

    public void setBaseHbnDao(IBaseHbnDao baseHbnDao) {
        this.baseHbnDao = baseHbnDao;
    }
}
