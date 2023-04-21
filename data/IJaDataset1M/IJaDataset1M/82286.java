package de.grogra.lsystem;

import de.grogra.graph.*;

/**
 * The turtle command
 * <code>LlMul(x)</code>
 * sets {@link de.grogra.lsystem.TurtleState#localLength} to
 * the product of the field {@link de.grogra.lsystem.TurtleState#length} and
 * the specified {@link de.grogra.lsystem.Assignment#argument argument}
 * <code>x</code>.
 * <br>
 * This corresponds to the turtle command <code>Ll*(x)</code>
 * of the GROGRA software.
 *
 * @author Ole Kniemeyer
 */
public class LlMul extends Assignment {

    public static final NType $TYPE;

    static {
        $TYPE = new NType(new LlMul());
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new LlMul();
    }

    public static class Predicate extends de.grogra.xl.qnp.FieldListPredicate {

        public Predicate() {
            super(LlMul.$TYPE, argument$FIELD);
        }

        public static void signature(InOut io, LlMul n, float a) {
        }
    }

    public LlMul() {
        this(0);
    }

    public LlMul(float argument) {
        super(argument);
    }

    public void execute(Object node, TurtleState state, GraphState gs) {
        state.localLength = (state.length * getArgument(node, gs));
    }
}
