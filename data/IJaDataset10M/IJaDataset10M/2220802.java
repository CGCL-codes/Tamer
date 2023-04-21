package nuts.core.orm.example;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nuts.core.sql.criterion.AbstractExpression;
import nuts.core.sql.criterion.OrderExpression;
import nuts.core.sql.criterion.Orders;
import nuts.core.sql.criterion.Restrictions;

/**
 * QueryParameter
 */
public class QueryParameter implements Cloneable, Serializable {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 8555782002833614738L;

    private Map<String, Boolean> excludes;

    private Restrictions restrictions;

    private Orders orders;

    /**
     * constructor
     */
    public QueryParameter() {
    }

    /**
     * constructor
     * @param qp queryParameter
     */
    public QueryParameter(QueryParameter qp) {
        orders = qp.orders;
        restrictions = qp.restrictions;
    }

    /**
     * clear
     */
    public void clear() {
        if (orders != null) {
            orders.clear();
        }
        if (restrictions != null) {
            restrictions.clear();
        }
        if (excludes != null) {
            excludes.clear();
        }
    }

    /**
	 * @return the excludes
	 */
    public Map<String, Boolean> getExcludes() {
        if (excludes == null) {
            excludes = new HashMap<String, Boolean>();
        }
        return excludes;
    }

    /**
	 * @param excludes the excludes to set
	 */
    public void setExcludes(Map<String, Boolean> excludes) {
        this.excludes = excludes;
    }

    /**
	 * @return true if the excludes is not empty
	 */
    public boolean isHasExcludes() {
        return excludes != null && !excludes.isEmpty();
    }

    /**
	 * @param column exclude column
	 * @return this
	 */
    public QueryParameter addExclude(String column) {
        getExcludes().put(column, true);
        return this;
    }

    /**
	 * @param column exclude column
	 * @return this
	 */
    public QueryParameter removeExclude(String column) {
        getExcludes().remove(column);
        return this;
    }

    /**
	 * clearExcludes
	 * @return this
	 */
    public QueryParameter clearExcludes() {
        getExcludes().clear();
        return this;
    }

    /**
	 * @return whereClause
	 */
    public Restrictions getRestrictions() {
        if (restrictions == null) {
            restrictions = new Restrictions();
        }
        return restrictions;
    }

    /**
	 * @param whereClause the whereClause to set
	 */
    public void setRestrictions(Restrictions whereClause) {
        this.restrictions = whereClause;
    }

    /**
	 * @return orders
	 */
    public Orders getOrders() {
        if (orders == null) {
            orders = new Orders();
        }
        return orders;
    }

    /**
	 * @param orders the orders to set
	 */
    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    /**
	 * @return orderExpressions
	 */
    public List<OrderExpression> getOrderExpressions() {
        return orders == null ? null : orders.getExpressions();
    }

    /**
	 * @return restrictionExpressions
	 */
    public List<AbstractExpression> getRestrictionExpressions() {
        return restrictions == null ? null : restrictions.getExpressions();
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((excludes == null) ? 0 : excludes.hashCode());
        result = prime * result + ((restrictions == null) ? 0 : restrictions.hashCode());
        result = prime * result + ((orders == null) ? 0 : orders.hashCode());
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
        QueryParameter other = (QueryParameter) obj;
        if (excludes == null) {
            if (other.excludes != null) return false;
        } else if (!excludes.equals(other.excludes)) return false;
        if (restrictions == null) {
            if (other.restrictions != null) return false;
        } else if (!restrictions.equals(other.restrictions)) return false;
        if (orders == null) {
            if (other.orders != null) return false;
        } else if (!orders.equals(other.orders)) return false;
        return true;
    }

    /**
	 * Clone
	 * @throws CloneNotSupportedException if clone not supported
	 * @return Clone Object
	 */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @return  a string representation of the object.
	 */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("excludes: { ");
        sb.append(excludes);
        sb.append(" }, restrictions: { ");
        sb.append(restrictions);
        sb.append(" }, orders: { ");
        sb.append(orders);
        sb.append(" }");
        return sb.toString();
    }
}
