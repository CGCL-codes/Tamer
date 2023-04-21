package com.xavax.xstore.qt;

/**
 * QtLong represents a long integer constant in a query tree.
 */
public class QtLong extends Constant {

    /**
   * Construct a QtLong node with the specified value.
   *
   * @param i  the long value.
   */
    public QtLong(long i) {
        _value = i;
    }

    /**
   * Append a string representation of this node to the query string buffer.
   *
   * @param sb  the query string buffer.
   */
    public void query(StringBuffer sb) {
        sb.append(_value);
    }

    /**
   * Returns the node type of this node.
   * @return the node type of this node.
   * @see <a href="NodeType.html">NodeType</a>
   */
    public int nodeType() {
        return NodeType.LONG;
    }

    /**
   * Returns the value type of this node.
   * @return the value type of this node.
   * @see <a href="ValueType.html">ValueType</a>
   */
    public int valueType() {
        return ValueType.LONG;
    }

    /**
   * Returns the boolean value of this node which is true if the integer
   * value is not zero.
   *
   * @return the boolean value of this node.
   */
    public boolean booleanValue() {
        return _value != 0;
    }

    /**
   * Returns the integer value of this node.  It is possible that some
   * information will be lost when casting a long to an int.
   *
   * @return the integer value of this node.
   */
    public int integerValue() {
        return (int) _value;
    }

    /**
   * Returns the long value of this node.
   * @return the long value of this node.
   */
    public long longValue() {
        return _value;
    }

    /**
   * Returns the value of this node converted to a double.
   * @return the value of this node converted to a double.
   */
    public double doubleValue() {
        return _value;
    }

    /**
   * Returns a string representation of the integer value.
   * @return a string representation of the integer value.
   */
    public String stringValue() {
        return Long.toString(_value);
    }

    private long _value;
}
