package com.avaje.ebean.util;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.bean.NodeUsageCollector;
import com.avaje.ebean.bean.ObjectGraphNode;

/**
 * Provides non-public API for Serialization support.
 * <p>
 * Implemented by Client and Server implementations of EbeanServer.
 * </p>
 */
public interface InternalEbean extends EbeanServer {

    /**
	 * For BeanCollections to call for lazy loading themselves.
	 * 
	 * @param profilePoint
	 *            the profilePoint is only used when profiling is on. It
	 *            contains the location of the original queryHash and
	 *            codeStackHash to link the profile back to.
	 */
    public void lazyLoadMany(Object parentBean, String propertyName, ObjectGraphNode profilePoint);

    /**
	 * Lazy load an entity bean.
	 * 
	 * @param bean
	 *            the entity bean to lazy load.
	 * @param collector
	 *            the collector is only used when profiling is on. It contains
	 *            the profilePoint of the original queryHash and codeStackHash
	 *            to link the profile back to.
	 */
    public void lazyLoadBean(Object bean, NodeUsageCollector collector);
}
