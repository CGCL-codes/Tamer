package com.techstar.dmis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.techstar.framework.dao.IBaseHbnDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dao.IFsLfeqpfixinformdetailDao;
import com.techstar.dmis.entity.FsLfeqpfixinformdetail;

public class FsLfeqpfixinformdetailDaoImpl implements IFsLfeqpfixinformdetailDao {

    private IBaseHbnDao baseHbnDao;

    public void delete(FsLfeqpfixinformdetail fsLfeqpfixinformdetail) {
        baseHbnDao.delete(fsLfeqpfixinformdetail);
    }

    public void deleteAll(List pos) {
        baseHbnDao.deleteAll(pos);
    }

    public void saveOrUpdate(FsLfeqpfixinformdetail fsLfeqpfixinformdetail) {
        baseHbnDao.saveOrUpdate("FsLfeqpfixinformdetail", fsLfeqpfixinformdetail);
    }

    public FsLfeqpfixinformdetail findByPk(Object fixinformdetailid) {
        return (FsLfeqpfixinformdetail) baseHbnDao.findById(FsLfeqpfixinformdetail.class, (Serializable) fixinformdetailid);
    }

    public QueryListObj getQueryList() {
        return baseHbnDao.getQueryListByEntityName("FsLfeqpfixinformdetail");
    }

    public QueryListObj getQueryList(int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByEntityName("FsLfeqpfixinformdetail", beginPage, pageSize);
    }

    public void merge(FsLfeqpfixinformdetail fsLfeqpfixinformdetail) {
        baseHbnDao.merge("FsLfeqpfixinformdetail", fsLfeqpfixinformdetail);
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
