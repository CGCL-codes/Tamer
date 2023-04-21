package com.google.gwt.rpc.client.ast;

import com.google.gwt.rpc.client.ast.RpcCommandVisitor.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates an array of values.
 */
public class ArrayValueCommand extends IdentityValueCommand {

    private final Class<?> componentType;

    private final List<ValueCommand> values = new ArrayList<ValueCommand>();

    public ArrayValueCommand(Class<?> componentType) {
        this.componentType = componentType;
    }

    public void add(ValueCommand x) {
        values.add(x);
    }

    @Override
    public void clear() {
        values.clear();
    }

    public Class<?> getComponentType() {
        return componentType;
    }

    public List<ValueCommand> getComponentValues() {
        return values;
    }

    @Override
    public void traverse(RpcCommandVisitor visitor, Context ctx) {
        if (visitor.visit(this, ctx)) {
            visitor.accept(values);
        }
        visitor.endVisit(this, ctx);
    }
}
