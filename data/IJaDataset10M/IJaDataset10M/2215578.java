package de.grogra.pf.registry.expr;

import de.grogra.pf.registry.Item;
import de.grogra.pf.registry.RegistryContext;
import de.grogra.util.StringMap;

public final class Or extends Expression {

    public static final NType $TYPE;

    static {
        $TYPE = new NType(new Or());
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Or();
    }

    @Override
    public Object evaluate(RegistryContext ctx, StringMap args) {
        for (Item i = (Item) getBranch(); i != null; i = (Item) i.getSuccessor()) {
            if (evaluateBoolean(i, ctx, args)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
