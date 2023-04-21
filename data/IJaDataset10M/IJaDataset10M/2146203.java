package de.grogra.lsystem;

import de.grogra.graph.*;

/**
 * The turtle command
 * <code>Dl(x)</code>
 * sets {@link de.grogra.lsystem.TurtleState#localDiameter} to
 * the specified {@link de.grogra.lsystem.Assignment#argument argument}
 * <code>x</code>.
 * <br>
 * This corresponds to the turtle command <code>Dl(x)</code>
 * of the GROGRA software.
 *
 * @author Ole Kniemeyer
 */
public class Dl extends Assignment {

    public static final NType $TYPE;

    static {
        $TYPE = new NType(new Dl());
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Dl();
    }

    public static class Predicate extends de.grogra.xl.qnp.FieldListPredicate {

        public Predicate() {
            super(Dl.$TYPE, argument$FIELD);
        }

        public static void signature(InOut io, Dl n, float a) {
        }
    }

    public Dl() {
        this(0);
    }

    public Dl(float argument) {
        super(argument);
    }

    public void execute(Object node, TurtleState state, GraphState gs) {
        state.localDiameter = (getArgument(node, gs));
    }
}
