package de.grogra.rgg;

public final class Axiom extends de.grogra.graph.impl.Node {

    public static final NType $TYPE;

    static {
        $TYPE = new NType(new Axiom());
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Axiom();
    }
}
