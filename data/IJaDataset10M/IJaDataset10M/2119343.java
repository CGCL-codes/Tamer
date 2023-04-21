package com.techstar.dmis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.techstar.framework.dao.IBaseHbnDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dao.IEQPParaDescDao;
import com.techstar.dmis.entity.EQPParaDesc;

public class EQPParaDescDaoImpl implements IEQPParaDescDao {

    private IBaseHbnDao baseHbnDao;

    public void delete(EQPParaDesc eQPParaDesc) {
        baseHbnDao.delete(eQPParaDesc);
    }

    public void deleteAll(List pos) {
        baseHbnDao.deleteAll(pos);
    }

    public void saveOrUpdate(EQPParaDesc eQPParaDesc) {
        baseHbnDao.saveOrUpdate("EQPParaDesc", eQPParaDesc);
    }

    public EQPParaDesc findByPk(Object fid) {
        return (EQPParaDesc) baseHbnDao.findById(EQPParaDesc.class, (Serializable) fid);
    }

    public QueryListObj getQueryList() {
        return baseHbnDao.getQueryListByEntityName("EQPParaDesc");
    }

    public QueryListObj getQueryList(int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByEntityName("EQPParaDesc", beginPage, pageSize);
    }

    public void merge(EQPParaDesc eQPParaDesc) {
        baseHbnDao.merge("EQPParaDesc", eQPParaDesc);
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
