package uk.ac.soton.grophysics;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import de.grogra.graph.impl.Node;

public class MassObject extends Node {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2751727913919556288L;

    Object object;

    float mass;

    boolean initialised = false;

    CollisionShape shape;

    CollisionObject body;

    Matrix4d initialTransform;

    Matrix4d initialPhysicsTransform;

    Point3d offsetCoG;

    Matrix4d currentGlobalTransform;

    Matrix4d parentFrameTransform;

    public MassObject() {
        super();
        offsetCoG = new Point3d(0, 0, 0);
    }

    public MassObject(String name, Object object, float mass) {
        super();
        super.setName(name);
        this.object = object;
        this.mass = mass;
        offsetCoG = new Point3d(0, 0, 0);
    }

    public CollisionShape getShape() {
        return shape;
    }

    public void setShape(CollisionShape shape) {
        this.shape = shape;
    }

    public CollisionObject getBody() {
        return body;
    }

    public void setBody(RigidBody body) {
        this.body = body;
    }

    public boolean isInitialised() {
        return initialised;
    }

    public void setInitialised(boolean initialised) {
        this.initialised = initialised;
    }

    public Matrix4d getCurrentGlobalTransform() {
        Matrix4d result = new Matrix4d(currentGlobalTransform);
        return result;
    }

    public void setCurrentGlobalTransform(Matrix4d m) {
        this.currentGlobalTransform = new Matrix4d(m);
    }

    public static final NType $TYPE;

    public static final NType.Field object$FIELD;

    public static final NType.Field mass$FIELD;

    public static final NType.Field initialTransform$FIELD;

    public static final NType.Field initialPhysicsTransform$FIELD;

    public static final NType.Field offsetCoG$FIELD;

    public static final NType.Field parentFrameTransform$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(MassObject.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        public void setFloat(Object o, float value) {
            switch(id) {
                case 1:
                    ((MassObject) o).mass = (float) value;
                    return;
            }
            super.setFloat(o, value);
        }

        @Override
        public float getFloat(Object o) {
            switch(id) {
                case 1:
                    return ((MassObject) o).getMass();
            }
            return super.getFloat(o);
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((MassObject) o).object = (Object) value;
                    return;
                case 2:
                    ((MassObject) o).initialTransform = (Matrix4d) value;
                    return;
                case 3:
                    ((MassObject) o).initialPhysicsTransform = (Matrix4d) value;
                    return;
                case 4:
                    ((MassObject) o).offsetCoG = (Point3d) value;
                    return;
                case 5:
                    ((MassObject) o).parentFrameTransform = (Matrix4d) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((MassObject) o).getObject();
                case 2:
                    return ((MassObject) o).getInitialTransform();
                case 3:
                    return ((MassObject) o).getInitialPhysicsTransform();
                case 4:
                    return ((MassObject) o).getOffsetCoG();
                case 5:
                    return ((MassObject) o).getParentFrameTransform();
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new MassObject());
        $TYPE.addManagedField(object$FIELD = new _Field("object", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(Object.class), null, 0));
        $TYPE.addManagedField(mass$FIELD = new _Field("mass", 0 | _Field.SCO, de.grogra.reflect.Type.FLOAT, null, 1));
        $TYPE.addManagedField(initialTransform$FIELD = new _Field("initialTransform", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(Matrix4d.class), null, 2));
        $TYPE.addManagedField(initialPhysicsTransform$FIELD = new _Field("initialPhysicsTransform", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(Matrix4d.class), null, 3));
        $TYPE.addManagedField(offsetCoG$FIELD = new _Field("offsetCoG", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(Point3d.class), null, 4));
        $TYPE.addManagedField(parentFrameTransform$FIELD = new _Field("parentFrameTransform", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(Matrix4d.class), null, 5));
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new MassObject();
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float value) {
        this.mass = (float) value;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object value) {
        object$FIELD.setObject(this, value);
    }

    public Matrix4d getInitialTransform() {
        return initialTransform;
    }

    public void setInitialTransform(Matrix4d value) {
        initialTransform$FIELD.setObject(this, value);
    }

    public Matrix4d getInitialPhysicsTransform() {
        return initialPhysicsTransform;
    }

    public void setInitialPhysicsTransform(Matrix4d value) {
        initialPhysicsTransform$FIELD.setObject(this, value);
    }

    public Point3d getOffsetCoG() {
        return offsetCoG;
    }

    public void setOffsetCoG(Point3d value) {
        offsetCoG$FIELD.setObject(this, value);
    }

    public Matrix4d getParentFrameTransform() {
        return parentFrameTransform;
    }

    public void setParentFrameTransform(Matrix4d value) {
        parentFrameTransform$FIELD.setObject(this, value);
    }
}
