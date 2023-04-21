package net.sourceforge.cobertura.util;

import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

public abstract class TypeHelper {

    public static Type getType(Class cl) {
        if (cl.equals(boolean.class)) {
            return Type.BOOLEAN;
        } else if (cl.equals(char.class)) {
            return Type.CHAR;
        } else if (cl.equals(byte.class)) {
            return Type.BYTE;
        } else if (cl.equals(short.class)) {
            return Type.SHORT;
        } else if (cl.equals(int.class)) {
            return Type.INT;
        } else if (cl.equals(long.class)) {
            return Type.LONG;
        } else if (cl.equals(float.class)) {
            return Type.FLOAT;
        } else if (cl.equals(double.class)) {
            return Type.DOUBLE;
        } else if (cl.isArray()) {
            return new ArrayType(getType(cl.getComponentType()), 1);
        } else if (cl.equals(void.class)) {
            return Type.VOID;
        } else {
            return new ObjectType(cl.getName());
        }
    }

    public static Type[] getTypes(Class[] cls) {
        Type[] types = new Type[cls.length];
        for (int i = 0; i < cls.length; i++) {
            types[i] = getType(cls[i]);
        }
        return types;
    }
}
