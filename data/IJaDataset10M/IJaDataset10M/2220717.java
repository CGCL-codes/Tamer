package de.grogra.turtle;

import de.grogra.graph.*;
import de.grogra.imp3d.shading.*;

/**
 * The turtle command
 * <code>Pl(x)</code>
 * sets {@link de.grogra.turtle;TurtleState#localColor} to
 * the specified {@link de.grogra.turtle;Assignment#argument argument}
 * <code>x</code>.
 * <br>
 * This corresponds to the turtle command <code>Pl(x)</code>
 * of the GROGRA software.
 * <br>
 * There exists an extended command
 * <code>Pl(s)</code> where <code>s</code> is a <code>Shader</code>.
 * This sets the field {@link #shader}, which is
 * in turn used to set
 * {@link de.grogra.turtle;TurtleState#localShader}.
 *
 * @author Ole Kniemeyer
 */
public class Pl extends Assignment {

    public Shader shader = null;

    public void setShaders(Shader front, Shader back) {
        setShader(new SideSwitchShader(front, back));
    }

    public Pl(Shader shader) {
        super(-1);
        setShader(shader);
    }

    public Pl(Shader front, Shader back) {
        super(-1);
        setShaders(front, back);
    }

    private static void initType() {
        $TYPE.addDependency(Attributes.SHADER, Attributes.TURTLE_MODIFIER);
    }

    public static final NType $TYPE;

    public static final NType.Field shader$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(Pl.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((Pl) o).shader = (Shader) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((Pl) o).getShader();
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new Pl());
        $TYPE.addManagedField(shader$FIELD = new _Field("shader", _Field.PUBLIC | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(Shader.class), null, 0));
        $TYPE.declareFieldAttribute(shader$FIELD, Attributes.SHADER);
        initType();
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Pl();
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader value) {
        shader$FIELD.setObject(this, value);
    }

    public static class Pattern extends de.grogra.xl.impl.base.FieldListPattern {

        public Pattern() {
            super(Pl.$TYPE, argument$FIELD);
        }

        public static void signature(@In @Out Pl n, float a) {
        }
    }

    public Pl() {
        this(0);
    }

    public Pl(float argument) {
        super(argument);
    }

    public void execute(Object node, TurtleState state, GraphState gs) {
        state.localColor = Math.round(getArgument(node, gs));
        state.localShader = shader;
    }
}
