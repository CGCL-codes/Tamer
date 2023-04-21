package com.googlecode.beauti4j.core.web.gwt.client.data.search;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <tt>Criteria</tt> is a simplified API for retrieving entities by composing
 * <tt>Criterion</tt> objects. This is a very convenient approach for
 * functionality like "search" screens where there is a variable number of
 * conditions to be placed upon the result set.<br>
 * <br>
 * <tt>Criterion</tt> instances are usually obtained via the factory methods
 * on <tt>Restrictions</tt>. eg.
 * 
 * <pre>
 * Criteria cats = new Criteria(Cat.class).add(Restrictions.like(&quot;name&quot;, &quot;Iz%&quot;))
 * 		.add(Restrictions.gt(&quot;weight&quot;, new Float(minWeight))).addOrder(
 * 				Order.asc(&quot;age&quot;));
 * </pre>
 */
public class Criteria implements IsSerializable {

    private List<Criterion> criterions = new ArrayList<Criterion>();

    private List<Order> orders = new ArrayList<Order>();

    private int maxResults;

    private int firstResult;

    /**
	 * Add a {@link Criterion restriction} to constrain the results to be
	 * retrieved.
	 * 
	 * @param criterion
	 *            The {@link Criterion criterion} object representing the
	 *            restriction to be applied.
	 * @return this (for method chaining)
	 */
    public Criteria add(Criterion criterion) {
        criterions.add(criterion);
        return this;
    }

    /**
	 * Add an {@link Order ordering} to the result set.
	 * 
	 * @param order
	 *            The {@link Order order} object representing an ordering to be
	 *            applied to the results.
	 * @return this (for method chaining)
	 */
    public Criteria addOrder(Order order) {
        orders.add(order);
        return this;
    }

    /**
	 * Set a limit upon the number of objects to be retrieved.
	 * 
	 * @param maxResults
	 *            the maximum number of results
	 * @return this (for method chaining)
	 */
    public Criteria setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
	 * Set the first result to be retrieved.
	 * 
	 * @param firstResult
	 *            the first result to retrieve, numbered from <tt>0</tt>
	 * @return this (for method chaining)
	 */
    public Criteria setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public List<Criterion> getCriterions() {
        return criterions;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getFirstResult() {
        return firstResult;
    }
}
