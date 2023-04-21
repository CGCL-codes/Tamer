package com.xavax.xstore.qt;

import java.util.Collection;

/**
 * QtList represents a list of constant values which can be used as the
 * right hand side of an "In" or "Not In" expression.
 */
public class QtList extends Constant {

    /**
   * Construct a QtList from the specified collection of values.
   * The collection must contain only strings and numeric values
   * represented using wrapper classes.
   *
   * @param collection  a collection of string or numeric values.
   */
    public QtList(Collection<? extends Object> collection) {
        _collection = collection;
    }

    /**
   * Append the list of values to the query string buffer.  The list
   * of values is comma-separated and enclosed in parenthesis. String
   * values are enclosed in single quotes as per the SQL standard.
   *
   * @param sb  the query string buffer.
   */
    public void query(StringBuffer sb) {
        sb.append("(");
        boolean flag = false;
        for (Object o : _collection) {
            if (flag) {
                sb.append(", ");
            } else {
                flag = true;
            }
            if (String.class.isInstance(o)) {
                sb.append("'").append(o.toString()).append("'");
            } else {
                sb.append(o.toString());
            }
        }
        sb.append(")");
    }

    /**
   * Returns the node type of this node.
   * @return the node type of this node.
   * @see <a href="NodeType.html">NodeType</a>
   */
    public int nodeType() {
        return NodeType.LIST;
    }

    /**
   * Returns the value type of this node.
   * @return the value type of this node.
   * @see <a href="ValueType.html">ValueType</a>
   */
    public int valueType() {
        return ValueType.INVALID;
    }

    Collection<? extends Object> _collection;
}
