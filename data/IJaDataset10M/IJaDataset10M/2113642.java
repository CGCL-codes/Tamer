package de.grogra.math;

import javax.vecmath.*;
import de.grogra.persistence.*;

public class Matrix4fType extends SCOType {

    public static final Matrix4fType $TYPE;

    public static final Field m00$FIELD;

    public static final Field m01$FIELD;

    public static final Field m02$FIELD;

    public static final Field m03$FIELD;

    public static final Field m10$FIELD;

    public static final Field m11$FIELD;

    public static final Field m12$FIELD;

    public static final Field m13$FIELD;

    public static final Field m20$FIELD;

    public static final Field m21$FIELD;

    public static final Field m22$FIELD;

    public static final Field m23$FIELD;

    public static final Field m30$FIELD;

    public static final Field m31$FIELD;

    public static final Field m32$FIELD;

    public static final Field m33$FIELD;

    static {
        $TYPE = new Matrix4fType(Matrix4f.class, SCOType.$TYPE);
        m00$FIELD = $TYPE.addManagedField("m00", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 0);
        m01$FIELD = $TYPE.addManagedField("m01", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 1);
        m02$FIELD = $TYPE.addManagedField("m02", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 2);
        m03$FIELD = $TYPE.addManagedField("m03", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 3);
        m10$FIELD = $TYPE.addManagedField("m10", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 4);
        m11$FIELD = $TYPE.addManagedField("m11", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 5);
        m12$FIELD = $TYPE.addManagedField("m12", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 6);
        m13$FIELD = $TYPE.addManagedField("m13", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 7);
        m20$FIELD = $TYPE.addManagedField("m20", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 8);
        m21$FIELD = $TYPE.addManagedField("m21", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 9);
        m22$FIELD = $TYPE.addManagedField("m22", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 10);
        m23$FIELD = $TYPE.addManagedField("m23", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 11);
        m30$FIELD = $TYPE.addManagedField("m30", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 12);
        m31$FIELD = $TYPE.addManagedField("m31", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 13);
        m32$FIELD = $TYPE.addManagedField("m32", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 14);
        m33$FIELD = $TYPE.addManagedField("m33", 0 | Field.SCO, de.grogra.reflect.Type.FLOAT, null, SCOType.FIELD_COUNT + 15);
        $TYPE.validate();
    }

    public Matrix4fType(Class c, de.grogra.persistence.SCOType supertype) {
        super(c, supertype);
    }

    public Matrix4fType(Matrix4f representative, de.grogra.persistence.SCOType supertype) {
        super(representative, supertype);
    }

    protected static final int FIELD_COUNT = SCOType.FIELD_COUNT + 16;

    @Override
    protected void setFloat(Object o, int id, float value) {
        switch(id) {
            case SCOType.FIELD_COUNT + 0:
                ((Matrix4f) o).m00 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 1:
                ((Matrix4f) o).m01 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 2:
                ((Matrix4f) o).m02 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 3:
                ((Matrix4f) o).m03 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 4:
                ((Matrix4f) o).m10 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 5:
                ((Matrix4f) o).m11 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 6:
                ((Matrix4f) o).m12 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 7:
                ((Matrix4f) o).m13 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 8:
                ((Matrix4f) o).m20 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 9:
                ((Matrix4f) o).m21 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 10:
                ((Matrix4f) o).m22 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 11:
                ((Matrix4f) o).m23 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 12:
                ((Matrix4f) o).m30 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 13:
                ((Matrix4f) o).m31 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 14:
                ((Matrix4f) o).m32 = (float) value;
                return;
            case SCOType.FIELD_COUNT + 15:
                ((Matrix4f) o).m33 = (float) value;
                return;
        }
        super.setFloat(o, id, value);
    }

    @Override
    protected float getFloat(Object o, int id) {
        switch(id) {
            case SCOType.FIELD_COUNT + 0:
                return ((Matrix4f) o).m00;
            case SCOType.FIELD_COUNT + 1:
                return ((Matrix4f) o).m01;
            case SCOType.FIELD_COUNT + 2:
                return ((Matrix4f) o).m02;
            case SCOType.FIELD_COUNT + 3:
                return ((Matrix4f) o).m03;
            case SCOType.FIELD_COUNT + 4:
                return ((Matrix4f) o).m10;
            case SCOType.FIELD_COUNT + 5:
                return ((Matrix4f) o).m11;
            case SCOType.FIELD_COUNT + 6:
                return ((Matrix4f) o).m12;
            case SCOType.FIELD_COUNT + 7:
                return ((Matrix4f) o).m13;
            case SCOType.FIELD_COUNT + 8:
                return ((Matrix4f) o).m20;
            case SCOType.FIELD_COUNT + 9:
                return ((Matrix4f) o).m21;
            case SCOType.FIELD_COUNT + 10:
                return ((Matrix4f) o).m22;
            case SCOType.FIELD_COUNT + 11:
                return ((Matrix4f) o).m23;
            case SCOType.FIELD_COUNT + 12:
                return ((Matrix4f) o).m30;
            case SCOType.FIELD_COUNT + 13:
                return ((Matrix4f) o).m31;
            case SCOType.FIELD_COUNT + 14:
                return ((Matrix4f) o).m32;
            case SCOType.FIELD_COUNT + 15:
                return ((Matrix4f) o).m33;
        }
        return super.getFloat(o, id);
    }

    @Override
    protected Object newInstance(Object repr) {
        return ((Matrix4f) repr).clone();
    }

    @Override
    protected Object cloneNonsharedObject(Object o, boolean deep) {
        return ((Matrix4f) o).clone();
    }

    static {
        $TYPE.setSerializationMethod(ManageableType.LIST_SERIALIZATION);
    }
}
