package com.techstar.dmis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.techstar.framework.dao.IBaseHbnDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dao.IZdhSrappeqprelDao;
import com.techstar.dmis.entity.ZdhSrappeqprel;

public class ZdhSrappeqprelDaoImpl implements IZdhSrappeqprelDao {

    private IBaseHbnDao baseHbnDao;

    public void delete(ZdhSrappeqprel zdhSrappeqprel) {
        baseHbnDao.delete(zdhSrappeqprel);
    }

    public void deleteAll(List pos) {
        baseHbnDao.deleteAll(pos);
    }

    public void saveOrUpdate(ZdhSrappeqprel zdhSrappeqprel) {
        baseHbnDao.saveOrUpdate("ZdhSrappeqprel", zdhSrappeqprel);
    }

    public ZdhSrappeqprel findByPk(Object fid) {
        return (ZdhSrappeqprel) baseHbnDao.findById(ZdhSrappeqprel.class, (Serializable) fid);
    }

    public QueryListObj getQueryList() {
        return baseHbnDao.getQueryListByEntityName("ZdhSrappeqprel");
    }

    public QueryListObj getQueryList(int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByEntityName("ZdhSrappeqprel", beginPage, pageSize);
    }

    public void merge(ZdhSrappeqprel zdhSrappeqprel) {
        baseHbnDao.merge("ZdhSrappeqprel", zdhSrappeqprel);
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
