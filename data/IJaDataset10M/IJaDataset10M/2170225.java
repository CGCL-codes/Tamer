package com.acv.dao.catalog;

/**
 * Common interface for all objects that can be sorted by replicant order.
 *
 * @author Mickael Guesnon
 */
public interface ObjectWithRepOrder {

    /**
	 * Provide the replicant order (CMS insertion order) of the current object.
	 *
	 * @return The replicant order.
	 */
    public abstract Long getRepOrder();
}
