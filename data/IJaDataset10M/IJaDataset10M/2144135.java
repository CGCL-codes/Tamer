package com.avaje.ebean.bean;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.server.deploy.BeanDescriptor;

/**
 * Holds the information available for a bean query.
 */
public interface BeanQueryRequest<T> {

    /**
	 * Return the server processing the request.
	 */
    public EbeanServer getEbeanServer();

    /**
	 * Return the BeanDescriptor for the associated bean.
	 */
    public BeanDescriptor<T> getBeanDescriptor();

    /**
	 * Return the Transaction associated with this request.
	 */
    public Transaction getTransaction();

    /**
	 * Returns the query.
	 */
    public Query<T> getQuery();

    /**
	 * Return the type of query result (Set,List,Map or single bean).
	 */
    public QueryType getQueryType();
}
