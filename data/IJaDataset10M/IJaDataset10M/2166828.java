package org.nicocube.airain.domain.server.criteria;

import com.db4o.query.Query;

/**
 * Define a common interface to create Criterias on {@link Query} 
 * 
 * @author nicolas
 * @version 0.1
 */
public interface Criteria {

    /**
	 * Apply the criteria on the given query
	 * @param q a query this method will modify to add a criteria on
	 */
    public abstract void apply(Query q);
}
