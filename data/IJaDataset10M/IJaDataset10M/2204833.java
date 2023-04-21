package com.zhiyun.estore.dao;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.zhiyun.estore.common.Page;

public class BaseDao extends HibernateDaoSupport {

    public Page listPage(final Page page, final String hql, final List<Object> params) throws Exception {
        if (null == page || page.getPageNo() == 0 || page.getPageSize() == 0) {
            return null;
        }
        getHibernateTemplate().executeFind(new HibernateCallback() {

            @SuppressWarnings("unchecked")
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                int rowNumber = 0;
                Query query = session.createQuery(hql);
                int index = 0;
                for (Object o : params) {
                    query.setParameter(index, o);
                    index++;
                }
                ScrollableResults sr = query.scroll();
                if (sr.last()) {
                    rowNumber = sr.getRowNumber() + 1;
                    sr.first();
                }
                if (rowNumber > 0) {
                    page.setTotalCount(rowNumber);
                    query.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
                    query.setMaxResults(page.getPageSize());
                    page.setData(query.list());
                    page.setPageInfo();
                }
                return null;
            }
        });
        return page;
    }

    public Session getMySession() {
        return getHibernateTemplate().getSessionFactory().openSession();
    }

    public void save(Object object) throws Exception {
        getHibernateTemplate().save(object);
    }

    public void saveOrUpdate(Object object) throws Exception {
        getHibernateTemplate().saveOrUpdate(object);
    }

    public void saveAll(final List<Object> objects) throws Exception {
        getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                for (Object o : objects) {
                    session.saveOrUpdate(o);
                }
                return null;
            }
        });
    }

    public void delete(Object object) throws Exception {
        getHibernateTemplate().delete(object);
    }

    public void deleteAll(List<Object> objects) throws Exception {
        getHibernateTemplate().deleteAll(objects);
    }

    public void update(Object object) throws Exception {
        getHibernateTemplate().update(object);
    }
}
