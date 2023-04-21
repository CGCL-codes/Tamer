package com.mangobop.impl.types;

import com.mangobop.types.Operand;
import com.mangobop.types.Type;

/**
 *
 * @author Stefan Meyer
 */
public abstract class AbstractOperandChain extends SimpleAbstractOperand {

    public AbstractOperandChain() {
    }

    protected java.util.List op_list = new java.util.ArrayList();

    private String name = null;

    public Type getType() {
        return first().getType();
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return last().getReturnType();
    }

    public void addOperand(Operand op) {
        op_list.add(op);
        if (name == null) {
            name = op.getName();
        } else {
            name.concat(op.getName());
        }
    }

    public Operand last() {
        return (Operand) op_list.get(op_list.size() - 1);
    }

    public Operand first() {
        return (Operand) op_list.get(0);
    }

    public int size() {
        return op_list.size();
    }
}
