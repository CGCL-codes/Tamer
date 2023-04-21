package com.faceye.core.componentsupport.dao.controller;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import com.faceye.core.componentsupport.dao.iface.IDomainDao;
import com.faceye.core.dao.hibernate.controller.BaseHibernateDao;
import com.faceye.core.util.helper.PaginationSupport;

public class DomainDao extends BaseHibernateDao implements IDomainDao {

    public String getAllByDetachedCriteria(DetachedCriteria detachedCriteria) {
        return new PaginationSupport(getAll(detachedCriteria)).json();
    }

    public String getAllByDomain(Class clazz) {
        return new PaginationSupport(super.loadAllObjects(clazz)).json();
    }

    public String getPageByDetachedCriteria(DetachedCriteria detachedCriteria, int startIndex) {
        return super.getPage(detachedCriteria, startIndex).json();
    }

    public String getPageByDetachedCriteria(DetachedCriteria detachedCriteria, int startIndex, int pageSize) {
        return super.getPage(detachedCriteria, pageSize, startIndex).json();
    }

    public String getPageByDomain(Class clazz, int startIndex) {
        return super.getPageByHQL(" from " + clazz.getName(), startIndex).json();
    }

    public String getPageByDomain(Class clazz, int startIndex, int pageSize) {
        return super.getPageByHQL(" from " + clazz.getName(), pageSize, startIndex).json();
    }

    public String getPageByHql(String hql, int startIndex) {
        return super.getPageByHQL(hql, startIndex).json();
    }

    public String getPageByHql(String hql, int startIndex, int pageSize) {
        return super.getPageByHQL(hql, pageSize, startIndex).json();
    }

    public String getPageByHql(String hql, String param, Object value, int startIndex) {
        return super.getPageByHQL(hql, value, startIndex).json();
    }

    public String getPageByHql(String hql, String param, Object value, int startIndex, int pageSize) {
        return super.getPageByHQL(hql, value, pageSize, startIndex).json();
    }

    public String getPageByHql(String hql, String[] params, Object[] values, int startIndex) {
        return super.getPageByHQL(hql, params, values, startIndex).json();
    }

    public String getPageByHql(String hql, String[] params, Object[] values, int startIndex, int pageSize) {
        return super.getPageByHQL(hql, params, values, pageSize, startIndex).json();
    }

    public String getPageByHql(String hql, Object value, int startIndex) {
        return super.getPageByHQL(hql, value, startIndex).json();
    }

    public String getPageByHql(String hql, Object value, int startIndex, int pageSize) {
        return super.getPageByHQL(hql, value, pageSize, startIndex).json();
    }

    public String getPageByHql(String hql, Object[] values, int startIndex) {
        return super.getPageByHQL(hql, values, startIndex).json();
    }

    public String getPageByHql(String hql, Object[] values, int startIndex, int pageSize) {
        return super.getPageByHQL(hql, values, pageSize, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, int startIndex) {
        return super.getPage(queryName, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, int startIndex, int pageSize) {
        return super.getPage(queryName, pageSize, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, String param, Object value, int startIndex) {
        return super.getPage(queryName, param, value, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, String param, Object value, int startIndex, int pageSize) {
        return super.getPage(queryName, param, value, pageSize, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, String[] params, Object[] values, int startIndex) {
        return super.getPage(queryName, params, values, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, String[] params, Object[] values, int startIndex, int pageSize) {
        return super.getPage(queryName, params, values, pageSize, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, Object value, int startIndex) {
        return super.getPage(queryName, value, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, Object value, int startIndex, int pageSize) {
        return super.getPage(queryName, value, pageSize, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, Object[] values, int startIndex) {
        return super.getPage(queryName, values, startIndex).json();
    }

    public String getPageByNamedHql(String queryName, Object[] values, int startIndex, int pageSize) {
        return super.getPage(queryName, values, pageSize, startIndex).json();
    }
}
