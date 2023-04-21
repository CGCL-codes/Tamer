package com.hs.sfms.dao.user;

import java.io.Serializable;
import com.hs.framework.BaseBean;
import com.hs.framework.db.DAOException;
import com.hs.framework.db.hibernate.BaseDAOHibernate;

/**
 * 
 * @author Ansun
 *
 */
public class BaseUserDAO extends BaseDAOHibernate {

    public String getLoadHQL(BaseBean bean) {
        String hSql = "";
        return hSql;
    }
}
