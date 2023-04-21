package com.techstar.dmis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.techstar.framework.dao.IBaseHbnDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dao.IParaVQCDao;
import com.techstar.dmis.entity.ParaVQC;

public class ParaVQCDaoImpl implements IParaVQCDao {

    private IBaseHbnDao baseHbnDao;

    public void delete(ParaVQC paraVQC) {
        baseHbnDao.delete(paraVQC);
    }

    public void deleteAll(List pos) {
        baseHbnDao.deleteAll(pos);
    }

    public void saveOrUpdate(ParaVQC paraVQC) {
        baseHbnDao.saveOrUpdate("ParaVQC", paraVQC);
    }

    public ParaVQC findByPk(Object feqpid) {
        return (ParaVQC) baseHbnDao.findById(ParaVQC.class, (Serializable) feqpid);
    }

    public QueryListObj getQueryList() {
        return baseHbnDao.getQueryListByEntityName("ParaVQC");
    }

    public QueryListObj getQueryList(int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByEntityName("ParaVQC", beginPage, pageSize);
    }

    public void merge(ParaVQC paraVQC) {
        baseHbnDao.merge("ParaVQC", paraVQC);
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
