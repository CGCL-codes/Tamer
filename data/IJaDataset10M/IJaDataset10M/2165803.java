package de.grogra.turtle;

import de.grogra.graph.*;

/**
 * The turtle command
 * <code>NAdd(x)</code>
 * increments {@link de.grogra.turtle;TurtleState#parameter} by
 * the specified {@link de.grogra.turtle;Assignment#argument argument}
 * <code>x</code>.
 * Then the value of <code>parameter</code> is copied to
 * its local counterpart {@link de.grogra.turtle;TurtleState#localParameter}.
 * <br>
 * This corresponds to the turtle command <code>N+(x)</code>
 * of the GROGRA software.
 *
 * @author Ole Kniemeyer
 */
public class NAdd extends Assignment {

    public static final NType $TYPE;

    static {
        $TYPE = new NType(new NAdd());
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new NAdd();
    }

    public static class Pattern extends de.grogra.xl.impl.base.FieldListPattern {

        public Pattern() {
            super(NAdd.$TYPE, argument$FIELD);
        }

        public static void signature(@In @Out NAdd n, float a) {
        }
    }

    public NAdd() {
        this(0);
    }

    public NAdd(float argument) {
        super(argument);
    }

    public void execute(Object node, TurtleState state, GraphState gs) {
        state.localParameter = state.parameter = (state.parameter + getArgument(node, gs));
    }
}
