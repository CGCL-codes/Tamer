package com.versant.core.jdo.query;

import com.versant.core.common.BindingSupportImpl;

/**
 * A comparision operation (other than equals and not equals).
 */
public class CompareOpNode extends BinaryNode {

    public static final int GT = 1;

    public static final int LT = 2;

    public static final int GE = 3;

    public static final int LE = 4;

    /**
     * The operation.
     */
    public int op;

    public CompareOpNode(Node left, Node right, int op) {
        super(left, right);
        this.op = op;
    }

    public Object accept(NodeVisitor visitor, Object[] results) {
        return visitor.visitCompareOpNode(this, results);
    }

    public String toString() {
        return super.toString() + " " + toOpString(op);
    }

    public static String toOpString(int op) {
        switch(op) {
            case GT:
                return ">";
            case LT:
                return "<";
            case GE:
                return ">=";
            case LE:
                return "<=";
        }
        return "Unknown(" + op + ")";
    }

    /**
     * Swap left and right nodes.
     */
    protected void swapLeftAndRight() {
        super.swapLeftAndRight();
        switch(op) {
            case GT:
                op = LT;
                break;
            case LT:
                op = GT;
                break;
            case GE:
                op = LE;
                break;
            case LE:
                op = GE;
                break;
            default:
                throw BindingSupportImpl.getInstance().internal("Unknown op: " + toOpString(op));
        }
    }

    public Field visit(MemVisitor visitor, Object obj) {
        return visitor.visitCompareOpNode(this, obj);
    }

    public Object arrive(NodeVisitor v, Object msg) {
        return v.arriveCompareOpNode(this, msg);
    }
}
