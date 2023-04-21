package net.woodstock.rockapi.domain.persistence.query.impl;

import java.util.Map;
import net.woodstock.rockapi.domain.persistence.query.BuilderException;
import net.woodstock.rockapi.domain.persistence.query.QueryBuilder;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.DistinctRootEntityResultTransformer;

public class HibernateQueryBuilder extends EJBQLQueryBuilder {

    @Override
    protected Object getQueryLocal(String sql, Object manager) throws BuilderException {
        Session session = (Session) manager;
        Query query = session.createQuery(sql);
        query.setResultTransformer(new DistinctRootEntityResultTransformer());
        return query;
    }

    @Override
    protected void setQueryParameter(Object query, String name, Object value) throws BuilderException {
        this.getLogger().info("Setting parameter[" + name + "] => " + value);
        Query q = (Query) query;
        q.setParameter(name, value);
    }

    @Override
    protected void setQueryOptions(Object query, Map<String, Object> options) throws BuilderException {
        Query q = (Query) query;
        if ((options.containsKey(QueryBuilder.OPTION_FIRST_RESULT)) && (options.get(QueryBuilder.OPTION_FIRST_RESULT) instanceof Integer)) {
            Integer firstResult = (Integer) options.get(QueryBuilder.OPTION_FIRST_RESULT);
            q.setFirstResult(firstResult.intValue());
        }
        if ((options.containsKey(QueryBuilder.OPTION_MAX_RESULT)) && (options.get(QueryBuilder.OPTION_MAX_RESULT) instanceof Integer)) {
            Integer maxResult = (Integer) options.get(QueryBuilder.OPTION_MAX_RESULT);
            q.setMaxResults(maxResult.intValue());
        }
    }
}
