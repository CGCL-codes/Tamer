package de.grogra.lsystem;

import de.grogra.graph.*;

/**
 * The turtle command
 * <code>F(x)</code>
 * represents a cylinder along the local z-direction.
 * In addition, this command translates the local coordinate system
 * along the axis of the cylinder such that the origin of the
 * children's coordinate system coincides with the center of the cylinder's top.
 * <br>
 * The diameter of the cylinder
 * is {@link #diameter}, if this value
 * is non-negative, otherwise it
 * is taken from the field
 * <code>localDiameter</code> of the current {@link de.grogra.lsystem.TurtleState}.
 * The shader of the cylinder
 * is defined by the {@link #color}, if this value
 * is non-negative, otherwise it
 * is taken from the the current {@link de.grogra.lsystem.TurtleState}.
 * The length of the axis is
 * defined by {@link #length}.
 * <br>
 * This corresponds to the turtle command <code>F(x)</code>
 * of the GROGRA software.
 *
 * @author Ole Kniemeyer
 */
public class F extends Shoot {

    public float length;

    public float diameter = -1;

    public int color = -1;

    private static void initType() {
        $TYPE.addDependency(Attributes.LENGTH, Attributes.TURTLE_COMMAND);
    }

    public static final NType $TYPE;

    public static final NType.Field length$FIELD;

    public static final NType.Field diameter$FIELD;

    public static final NType.Field color$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(F.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        public void setInt(Object o, int value) {
            switch(id) {
                case 2:
                    ((F) o).color = (int) value;
                    return;
            }
            super.setInt(o, value);
        }

        @Override
        public int getInt(Object o) {
            switch(id) {
                case 2:
                    return ((F) o).color;
            }
            return super.getInt(o);
        }

        @Override
        public void setFloat(Object o, float value) {
            switch(id) {
                case 0:
                    ((F) o).length = (float) value;
                    return;
                case 1:
                    ((F) o).diameter = (float) value;
                    return;
            }
            super.setFloat(o, value);
        }

        @Override
        public float getFloat(Object o) {
            switch(id) {
                case 0:
                    return ((F) o).length;
                case 1:
                    return ((F) o).diameter;
            }
            return super.getFloat(o);
        }
    }

    static {
        $TYPE = new NType(new F());
        $TYPE.addManagedField(length$FIELD = new _Field("length", _Field.PUBLIC | _Field.SCO, de.grogra.reflect.Type.FLOAT, null, 0));
        $TYPE.addManagedField(diameter$FIELD = new _Field("diameter", _Field.PUBLIC | _Field.SCO, de.grogra.reflect.Type.FLOAT, null, 1));
        $TYPE.addManagedField(color$FIELD = new _Field("color", _Field.PUBLIC | _Field.SCO, de.grogra.reflect.Type.INT, null, 2));
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
        return new F();
    }

    public static class Predicate extends de.grogra.xl.qnp.FieldListPredicate {

        public Predicate() {
            super(F.$TYPE, length$FIELD);
        }

        public static void signature(InOut io, F n, float a) {
        }
    }

    public F() {
        this(1);
    }

    public F(float argument) {
        super();
        length = argument;
    }

    public F(float length, float diameter, int color) {
        this(length);
        this.diameter = diameter;
        this.color = color;
    }

    public F(float length, float diameter) {
        this(length);
        this.diameter = diameter;
    }

    @Override
    protected float getFloat(FloatAttribute a, GraphState gs) {
        return ((a == Attributes.RADIUS) && (diameter >= 0)) ? 0.5f * diameter : super.getFloat(a, gs);
    }

    @Override
    protected int getInt(IntAttribute a, GraphState gs) {
        return ((a == Attributes.DTG_COLOR) && (color >= 0)) ? color : super.getInt(a, gs);
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
}
