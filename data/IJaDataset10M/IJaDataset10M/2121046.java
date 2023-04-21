package au.edu.uq.itee.eresearch.dimer.core.util;

import javax.jcr.query.QueryResult;

public interface PagedResult extends QueryResult {

    public int getOffset();

    public int getLimit();

    public int getCount();
}
