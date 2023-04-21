package com.techstar.dmis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.techstar.framework.dao.IBaseHbnDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dao.IZdhRmrecDao;
import com.techstar.dmis.entity.ZdhRmrec;

public class ZdhRmrecDaoImpl implements IZdhRmrecDao {

    private IBaseHbnDao baseHbnDao;

    public void delete(ZdhRmrec zdhRmrec) {
        baseHbnDao.delete(zdhRmrec);
    }

    public void deleteAll(List pos) {
        baseHbnDao.deleteAll(pos);
    }

    public void saveOrUpdate(ZdhRmrec zdhRmrec) {
        baseHbnDao.saveOrUpdate("ZdhRmrec", zdhRmrec);
    }

    public ZdhRmrec findByPk(Object recordno) {
        return (ZdhRmrec) baseHbnDao.findById(ZdhRmrec.class, (Serializable) recordno);
    }

    public QueryListObj getQueryList() {
        return baseHbnDao.getQueryListByEntityName("ZdhRmrec");
    }

    public QueryListObj getQueryList(int beginPage, int pageSize) {
        return baseHbnDao.getQueryListByEntityName("ZdhRmrec", beginPage, pageSize);
    }

    public void merge(ZdhRmrec zdhRmrec) {
        baseHbnDao.merge("ZdhRmrec", zdhRmrec);
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
