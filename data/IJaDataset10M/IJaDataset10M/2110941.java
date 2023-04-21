package com.techstar.dmis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.techstar.framework.dao.IBaseHbnDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dao.IParaRTUDao;
import com.techstar.dmis.entity.ParaRTU;

public class ParaRTUDaoImpl implements IParaRTUDao {

    private IBaseHbnDao baseHbnDao;

    public void delete(ParaRTU paraRTU) {
        baseHbnDao.delete(paraRTU);
    }

    public void deleteAll(List pos) {
        baseHbnDao.deleteAll(pos);
    }

    public void saveOrUpdate(ParaRTU paraRTU) {
        baseHbnDao.saveOrUpdate("ParaRTU", paraRTU);
    }

    public ParaRTU findByPk(Object feqpid) {
        return (ParaRTU) baseHbnDao.findById(ParaRTU.class, (Serializable) feqpid);
    }

    public QueryListObj getQueryList() {
        return baseHbnDao.getQueryListByEntityName("ParaRTU");
    }

    public QueryListObj getQueryList(int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByEntityName("ParaRTU", beginPage, pageSize);
    }

    public void merge(ParaRTU paraRTU) {
        baseHbnDao.merge("ParaRTU", paraRTU);
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
