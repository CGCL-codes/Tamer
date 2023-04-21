package com.faceye.core.service.iface;

import com.faceye.core.dao.iface.IBaseDaoSupport;

public interface IBaseService {

    public IBaseHibernateService getBaseHibernateService();

    public IBaseJdbcService getBaseJdbcService();
}
