package org.nodal.util;

import org.nodal.model.Getter;
import org.nodal.model.Node;

/**
 * A SetterUtil variant that properly references the bareNode()
 * versions of Nodes that are set in this context.
 */
public class NodeSetterUtil extends SetterUtil {

    NodeSetterUtil(GetterUtil g, PlainSetter s) {
        super(g, s);
    }

    public Object set(Object val) throws ConstraintFailure {
        if (val == null) {
            ;
        } else if (val instanceof Getter) {
            Getter g = (Getter) val;
            return set(g.get());
        } else if (val instanceof String) {
            return setString((String) val);
        } else if (val instanceof Node) {
            return setNode((Node) val);
        }
        return setValue(val);
    }

    public Node setNode(Node n) throws ConstraintFailure {
        return (Node) setValue(n);
    }

    public boolean setBoolean(boolean b) throws ConstraintFailure {
        throw new TypeConstraintFailure(type(), "doesn't accept boolean: " + b);
    }

    public char setChar(char c) throws ConstraintFailure {
        throw new TypeConstraintFailure(type(), "doesn't accept char: " + c);
    }

    public byte setByte(byte b) throws ConstraintFailure {
        throw new TypeConstraintFailure(type(), "doesn't accept byte: " + b);
    }

    public short setShort(short s) throws ConstraintFailure {
        throw new TypeConstraintFailure(type(), "doesn't accept short: " + s);
    }

    public int setInt(int i) throws ConstraintFailure {
        throw new TypeConstraintFailure(type(), "doesn't accept int: " + i);
    }

    public long setLong(long l) throws ConstraintFailure {
        throw new TypeConstraintFailure(type(), "doesn't accept long: " + l);
    }

    public float setFloat(float f) throws ConstraintFailure {
        throw new TypeConstraintFailure(type(), "doesn't accept float: " + f);
    }

    public double setDouble(double d) throws ConstraintFailure {
        throw new TypeConstraintFailure(type(), "doesn't accept double: " + d);
    }
}
