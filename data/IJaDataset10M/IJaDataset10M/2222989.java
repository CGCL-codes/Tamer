package de.grogra.turtle;

import de.grogra.graph.*;

/**
 * The turtle command
 * <code>M(x)</code>
 * represents a movement along the local z-direction. The length of the movement is
 * defined by {@link #length}.
 * <br>
 * This corresponds to the turtle command <code>f(x)</code>
 * of the GROGRA software.
 *
 * @author Ole Kniemeyer
 */
public class M extends TurtleStep implements TurtleModifier {

    public float length;

    private static void initType() {
        $TYPE.addIdentityAccessor(Attributes.TURTLE_MODIFIER);
        $TYPE.addDependency(Attributes.LENGTH, Attributes.TURTLE_MODIFIER);
    }

    public static final NType $TYPE;

    public static final NType.Field length$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(M.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        public void setFloat(Object o, float value) {
            switch(id) {
                case 0:
                    ((M) o).length = (float) value;
                    return;
            }
            super.setFloat(o, value);
        }

        @Override
        public float getFloat(Object o) {
            switch(id) {
                case 0:
                    return ((M) o).length;
            }
            return super.getFloat(o);
        }
    }

    static {
        $TYPE = new NType(new M());
        $TYPE.addManagedField(length$FIELD = new _Field("length", _Field.PUBLIC | _Field.SCO, de.grogra.reflect.Type.FLOAT, null, 0));
        $TYPE.declareFieldAttribute(length$FIELD, Attributes.LENGTH);
        initType();
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new M();
    }

    public static class Pattern extends de.grogra.xl.impl.base.FieldListPattern {

        public Pattern() {
            super(M.$TYPE, length$FIELD);
        }

        public static void signature(@In @Out M n, float a) {
        }
    }

    public M() {
        this(1);
    }

    public M(float argument) {
        super();
        length = argument;
    }

    @Override
    public float getLength(Object node, GraphState gs) {
        if (node == this) {
            if (gs.getInstancingPathIndex() <= 0) {
                return length;
            } else {
                return (float) gs.checkDouble(this, true, Attributes.LENGTH, length);
            }
        } else {
            return (float) gs.getDouble(node, true, Attributes.LENGTH);
        }
    }

    public void execute(Object node, TurtleState state, GraphState gs) {
        state.relPosition -= getLength(node, gs) / state.length;
    }
}
