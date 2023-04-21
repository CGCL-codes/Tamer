package net.sf.btb;

import java.util.Set;

/**
 * Filter interface used in Bridge.fill method.
 *
 * @author Jean-Philippe Gravel
 */
public interface Filter {

    /**
     * Translates this filter to its SQL query equivalent.
     *
     * @return a query that will be used afer a "WHERE" in an SQL Query.
     */
    public String toQueryString();

    /**
     * Gets the set of properties in this filter statement.
     *
     * @return a set of properties used in this filter statement.
     */
    public Set<String> getProperties();
}
