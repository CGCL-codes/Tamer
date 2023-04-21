package net.sourceforge.jdefprog.mcl.interpret.context.factories;

import net.sourceforge.jdefprog.mcl.interpret.context.valued.ValuedAccessContext;
import net.sourceforge.jdefprog.reflection.values.Value;

public class ComposedValuedCtxFactory implements ValuedAccessContextFactory {

    private ValuedAccessContextFactory[] elements;

    public ComposedValuedCtxFactory(ValuedAccessContextFactory[] elements) {
        this.elements = elements;
    }

    @Override
    public String getCtxDesc(Value v) {
        for (ValuedAccessContextFactory e : elements) {
            if (e.isCtxAvailable(v)) {
                return e.getCtxDesc(v);
            }
        }
        throw new IllegalArgumentException("Unknown " + v);
    }

    @Override
    public ValuedAccessContext getObjContext(Value v) {
        for (ValuedAccessContextFactory e : elements) {
            if (e.isCtxAvailable(v)) {
                return e.getObjContext(v);
            }
        }
        throw new IllegalArgumentException("Unknown " + v);
    }

    @Override
    public boolean isCtxAvailable(Value v) {
        for (ValuedAccessContextFactory e : elements) {
            if (e.isCtxAvailable(v)) {
                return true;
            }
        }
        return false;
    }
}
