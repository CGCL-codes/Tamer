package com.techstar.dmis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.techstar.framework.dao.IBaseHbnDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dao.IMdisconnectorDao;
import com.techstar.dmis.entity.Mdisconnector;

public class MdisconnectorDaoImpl implements IMdisconnectorDao {

    private IBaseHbnDao baseHbnDao;

    public void delete(Mdisconnector mdisconnector) {
        baseHbnDao.delete(mdisconnector);
    }

    public void deleteAll(List pos) {
        baseHbnDao.deleteAll(pos);
    }

    public void saveOrUpdate(Mdisconnector mdisconnector) {
        baseHbnDao.saveOrUpdate("Mdisconnector", mdisconnector);
    }

    public Mdisconnector findByPk(Object fid) {
        return (Mdisconnector) baseHbnDao.findById(Mdisconnector.class, (Serializable) fid);
    }

    public QueryListObj getQueryList() {
        return baseHbnDao.getQueryListByEntityName("Mdisconnector");
    }

    public QueryListObj getQueryList(int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByEntityName("Mdisconnector", beginPage, pageSize);
    }

    public void merge(Mdisconnector mdisconnector) {
        baseHbnDao.merge("Mdisconnector", mdisconnector);
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
