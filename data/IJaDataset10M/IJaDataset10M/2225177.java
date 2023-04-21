package de.grogra.lsystem;

import de.grogra.graph.*;

/**
 * The turtle command
 * <code>VlAdd(x)</code>
 * sets {@link de.grogra.lsystem.TurtleState#localTropism} to
 * the sum of {@link de.grogra.lsystem.TurtleState#tropism} and
 * the specified {@link de.grogra.lsystem.Assignment#argument argument}
 * <code>x</code>.
 * <br>
 * This corresponds to the turtle command <code>Vl+(x)</code>
 * of the GROGRA software.
 *
 * @author Ole Kniemeyer
 */
public class VlAdd extends Assignment {

    public static final NType $TYPE;

    static {
        $TYPE = new NType(new VlAdd());
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new VlAdd();
    }

    public static class Predicate extends de.grogra.xl.qnp.FieldListPredicate {

        public Predicate() {
            super(VlAdd.$TYPE, argument$FIELD);
        }

        public static void signature(InOut io, VlAdd n, float a) {
        }
    }

    public VlAdd() {
        this(0);
    }

    public VlAdd(float argument) {
        super(argument);
    }

    public void execute(Object node, TurtleState state, GraphState gs) {
        state.localTropism = (state.tropism + getArgument(node, gs));
    }
}
