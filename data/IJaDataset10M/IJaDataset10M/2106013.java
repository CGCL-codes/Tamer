package com.jeecms.cms.dao.main.impl;

import org.springframework.stereotype.Repository;
import com.bocoon.common.hibernate3.HibernateBaseDao;
import com.bocoon.entity.cms.main.ContentExt;
import com.jeecms.cms.dao.main.ContentExtDao;

@Repository
public class ContentExtDaoImpl extends HibernateBaseDao<ContentExt, Integer> implements ContentExtDao {

    public ContentExt findById(Integer id) {
        ContentExt entity = get(id);
        return entity;
    }

    public ContentExt save(ContentExt bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    protected Class<ContentExt> getEntityClass() {
        return ContentExt.class;
    }
}
