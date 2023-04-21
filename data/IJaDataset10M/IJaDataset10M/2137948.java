package com.jeecms.cms.dao.main.impl;

import org.springframework.stereotype.Repository;
import com.bocoon.common.hibernate3.HibernateBaseDao;
import com.bocoon.entity.cms.main.CmsUserExt;
import com.jeecms.cms.dao.main.CmsUserExtDao;

@Repository
public class CmsUserExtDaoImpl extends HibernateBaseDao<CmsUserExt, Integer> implements CmsUserExtDao {

    public CmsUserExt findById(Integer id) {
        CmsUserExt entity = get(id);
        return entity;
    }

    public CmsUserExt save(CmsUserExt bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    protected Class<CmsUserExt> getEntityClass() {
        return CmsUserExt.class;
    }
}
