package org.vosao.dao.impl;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.LanguageDao;
import org.vosao.entity.LanguageEntity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class LanguageDaoImpl extends BaseDaoImpl<LanguageEntity> implements LanguageDao {

    public LanguageDaoImpl() {
        super(LanguageEntity.class);
    }

    public LanguageEntity getByCode(final String code) {
        Query q = newQuery();
        q.addFilter("code", FilterOperator.EQUAL, code);
        return selectOne(q, "getByCode", params(code));
    }
}
