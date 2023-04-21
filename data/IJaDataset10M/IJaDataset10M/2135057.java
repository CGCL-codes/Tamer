package com.io_software.catools.search.capability;

import com.io_software.catools.search.Query;
import com.io_software.catools.search.CompositeQuery;
import com.io_software.catools.search.Searchable;
import java.util.Set;

/** A production is a structural and possibly recursive description of
    a query object that can be used in a {@link Grammar} to describe
    one possible way of what that grammar can match / produce.

    @author Axel Uhl
    @version $Id: Production.java,v 1.5 2001/01/15 09:45:07 aul Exp $ */
public interface Production extends java.io.Serializable {

    /** checks whether a query is matched by this production

	@param q the query to be matched by this production
	@return <tt>true</tt> in case the passed query is matched by
	this production, <tt>false</tt> otherwise
    */
    public boolean matches(Query q);

    /** transitively retrieves all {@link Query} classes that can be used
	to construct queries that will be matched by this production.
	For a composite query, for example, this means that all allowed
	subquery types have to be collected and returned, including
	the allowed {@link CompositeQuery} subclasses themselves.
	
	@return a set containing {@link Class} objects that are all
		assignment compatible to {@link Query}
      */
    public Set getQueryTypes();
}
