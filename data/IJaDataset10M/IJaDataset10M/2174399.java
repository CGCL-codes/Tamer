package org.jbug.mcsample.domain;

/**
 * Order data access object (DAO).
 * 
 * @author gaston.scapusio
 *
 */
public interface OrderDAO {

    /**
	 * Add a new order.
	 * 
	 * @param order the new order.
	 */
    void save(Order order);
}
