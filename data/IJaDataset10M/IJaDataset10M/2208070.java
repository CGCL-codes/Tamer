package org.s3b.search.query.expansion;

import org.s3b.search.query.QueryObject;
import org.s3b.search.query.ResultObject;

/**
 * Interface for query object expansion.
 * @author jakdem
 *
 */
public interface QueryExpansion {

    /**
	 * Method for query expansion. It should add new <code>QueryParameterEntries</code> or 
	 * new <code>QueryParameter</code> to <tt>baseQuery</tt>
	 * @param expand <tt>true</true> if the given query should be expanded using wordnet, <tt>false</tt> if just the
	 * meaning of query parameters should be assigned to them (for eg. wordnet synset, taxonomy, author etc ) 
	 * @return expanded QueryObject
	 */
    public QueryObject processQuery(boolean expand, boolean generalize);

    /**
	 * @return Query object for this query
	 */
    public QueryObject getQueryObject();

    /**
	 * @return Result Object associated with this query
	 */
    @SuppressWarnings("unchecked")
    public ResultObject getResultObject();

    @SuppressWarnings("unchecked")
    public void setResultObject(ResultObject _resultObject);

    public void setQueryObject(QueryObject _queryObject);
}
