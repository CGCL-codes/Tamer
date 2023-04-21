package org.vosao.plugins.csdata.dao;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.plugins.csdata.entity.CsdataUserEntity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class CsdataUserDaoImpl extends BaseDaoImpl<CsdataUserEntity> implements CsdataUserDao {

    public CsdataUserDaoImpl() {
        super(CsdataUserEntity.class, "csdata_CsdataUserEntity");
    }

    @Override
    public CsdataUserEntity getByEmail(String email) {
        Query q = newQuery();
        q.addFilter("email", FilterOperator.EQUAL, email);
        return selectOne(q, "getByEmail", params(email));
    }

    @Override
    public CsdataUserEntity getBySessionKey(String key) {
        Query q = newQuery();
        q.addFilter("sessionKey", FilterOperator.EQUAL, key);
        return selectOne(q, "getByEmail", params(key));
    }
}
