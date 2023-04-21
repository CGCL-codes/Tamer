package org.butu.paged;

import javax.persistence.Query;

@SuppressWarnings("serial")
public abstract class AFilter implements IFilter {

    public abstract String getFilterPart(String parameter);

    public String getJoinPart(String parameter) {
        return "";
    }

    public abstract void setParameter(Query query, String parameter);
}
