package de.grogra.turtle;

import de.grogra.graph.*;

/**
 * The turtle command
 * <code>C(x)</code>
 * sets {@link de.grogra.turtle.TurtleState#carbon} to
 * the specified {@link de.grogra.turtle.Assignment#argument argument}
 * <code>x</code>.
 * Then the value of <code>carbon</code> is copied to
 * its local counterpart {@link de.grogra.turtle.TurtleState#localCarbon}.
 * <br>
 * This corresponds to the turtle command <code>C(x)</code>
 * of the GROGRA software.
 *
 * @author Ole Kniemeyer
 */
public class C extends Assignment {

    public static final NType $TYPE;

    static {
        $TYPE = new NType(new C());
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new C();
    }

    public static class Pattern extends de.grogra.xl.impl.base.FieldListPattern {

        public Pattern() {
            super(C.$TYPE, argument$FIELD);
        }

        public static void signature(@In @Out C n, float a) {
        }
    }

    public C() {
        this(0);
    }

    public C(float argument) {
        super(argument);
    }

    public void execute(Object node, TurtleState state, GraphState gs) {
        state.localCarbon = state.carbon = (getArgument(node, gs));
    }
}
