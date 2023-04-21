package net.taylor.fitnesse.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import net.taylor.fitnesse.GenericQueryDriver;
import net.taylor.fitnesse.el.Util;

public abstract class QueryDriver<T> extends GenericQueryDriver<T> {

    protected List<T> getResults() throws Exception {
        return setParameters(getEntityManager().createQuery(eql())).getResultList();
    }

    protected abstract EntityManager getEntityManager();

    protected String eql() {
        return "from " + Util.getParameterizedType(getClass()).getSimpleName();
    }

    protected Query setParameters(Query qry) {
        return qry;
    }
}
