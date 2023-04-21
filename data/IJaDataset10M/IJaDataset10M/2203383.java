package nuts.gae.dao;

import java.io.Serializable;
import nuts.core.lang.StringUtils;
import nuts.core.orm.restriction.Orders;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 */
@SuppressWarnings("serial")
public class GaeOrders implements Orders, Cloneable, Serializable {

    private Query query;

    /**
	 * Constructor
	 * 
	 * @param query query
	 */
    public GaeOrders(Query query) {
        this.query = query;
    }

    private SortDirection getSortDirection(String order) {
        if (ASC.equalsIgnoreCase(order)) {
            return SortDirection.ASCENDING;
        } else if (DESC.equalsIgnoreCase(order)) {
            return SortDirection.DESCENDING;
        } else {
            throw new IllegalArgumentException("Illegal sort direction: " + order);
        }
    }

    /**
	 * addOrder
	 * 
	 * @param column column
	 * @return this
	 */
    public GaeOrders addOrder(String column) {
        if (StringUtils.isNotEmpty(column)) {
            query.addSort(column);
        }
        return this;
    }

    /**
	 * addOrder
	 * 
	 * @param column column
	 * @param direction direction
	 * @return this
	 */
    public GaeOrders addOrder(String column, String direction) {
        if (StringUtils.isNotEmpty(column)) {
            query.addSort(column, getSortDirection(direction));
        }
        return this;
    }

    /**
	 * addOrderAsc
	 * 
	 * @param column column
	 * @return this
	 */
    public GaeOrders addOrderAsc(String column) {
        if (StringUtils.isNotEmpty(column)) {
            query.addSort(column, SortDirection.ASCENDING);
        }
        return this;
    }

    /**
	 * addOrderDesc
	 * 
	 * @param column column
	 * @return this
	 */
    public GaeOrders addOrderDesc(String column) {
        if (StringUtils.isNotEmpty(column)) {
            query.addSort(column, SortDirection.DESCENDING);
        }
        return this;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((query == null) ? 0 : query.hashCode());
        return result;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GaeOrders other = (GaeOrders) obj;
        if (query == null) {
            if (other.query != null) return false;
        } else if (!query.equals(other.query)) return false;
        return true;
    }

    /**
	 * Clone
	 * 
	 * @throws CloneNotSupportedException if clone not supported
	 * @return Clone Object
	 */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
	 * @return a string representation of the object.
	 */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("query: [").append(query).append(" ]");
        sb.append(" }");
        return sb.toString();
    }
}
