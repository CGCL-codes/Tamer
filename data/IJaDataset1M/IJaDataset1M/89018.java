package de.grogra.lsystem;

import de.grogra.graph.*;

/**
 * The turtle command
 * <code>VMul(x)</code>
 * multiplies {@link de.grogra.lsystem.TurtleState#tropism} by
 * the specified {@link de.grogra.lsystem.Assignment#argument argument}
 * <code>x</code>.
 * Then the value of <code>tropism</code> is copied to
 * its local counterpart {@link de.grogra.lsystem.TurtleState#localTropism}.
 * <br>
 * This corresponds to the turtle command <code>V*(x)</code>
 * of the GROGRA software.
 *
 * @author Ole Kniemeyer
 */
public class VMul extends Assignment {

    public static final NType $TYPE;

    static {
        $TYPE = new NType(new VMul());
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new VMul();
    }

    public static class Predicate extends de.grogra.xl.qnp.FieldListPredicate {

        public Predicate() {
            super(VMul.$TYPE, argument$FIELD);
        }

        public static void signature(InOut io, VMul n, float a) {
        }
    }

    public VMul() {
        this(0);
    }

    public VMul(float argument) {
        super(argument);
    }

    public void execute(Object node, TurtleState state, GraphState gs) {
        state.localTropism = state.tropism = (state.tropism * getArgument(node, gs));
    }
}
