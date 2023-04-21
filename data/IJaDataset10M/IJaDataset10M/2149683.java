package com.hand.dao.impl;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.hand.dao.IVersionDao;
import com.hand.model.TVersionAdm;

/**
 * 系统名：HCSMobileApp
 * 子系统名：版本DAO实现
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 11, 2011
 */
public class VersionDao extends HibernateDaoSupport implements IVersionDao {

    /**
	 * 根据版本号获取版本信息
	 * 
	 * @param versionName
	 *           版本号
	 * @return TVersionAdm 
	 * 			 版本信息
	 */
    public TVersionAdm getVersionByVersionName(String versionName) {
        String hql = "from TVersionAdm where versionName<>?";
        this.getHibernateTemplate().setCacheQueries(true);
        List list = this.getHibernateTemplate().find(hql, new Object[] { versionName });
        if (null != list && !list.isEmpty()) {
            return (TVersionAdm) list.get(0);
        }
        return null;
    }
}
