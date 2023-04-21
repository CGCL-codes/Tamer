package com.techstar.dmis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.techstar.framework.dao.IBaseHbnDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dao.IStdModelDao;
import com.techstar.dmis.entity.StdModel;

public class StdModelDaoImpl implements IStdModelDao {

    private IBaseHbnDao baseHbnDao;

    public void delete(StdModel stdModel) {
        baseHbnDao.delete(stdModel);
    }

    public void deleteAll(List pos) {
        baseHbnDao.deleteAll(pos);
    }

    public void saveOrUpdate(StdModel stdModel) {
        baseHbnDao.saveOrUpdate("StdModel", stdModel);
    }

    public StdModel findByPk(Object id) {
        return (StdModel) baseHbnDao.findById(StdModel.class, (Serializable) id);
    }

    public QueryListObj getQueryList() {
        return baseHbnDao.getQueryListByEntityName("StdModel");
    }

    public QueryListObj getQueryList(int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByEntityName("StdModel", beginPage, pageSize);
    }

    public void merge(StdModel stdModel) {
        baseHbnDao.merge("StdModel", stdModel);
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
