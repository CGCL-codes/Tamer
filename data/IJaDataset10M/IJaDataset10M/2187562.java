package fvm.impl;

import java.lang.reflect.*;

public class Reflector {

    public Constructor findConstructor(Class type, Object[] args) {
        Constructor[] constructors = type.getConstructors();
        for (int i = 0; i < constructors.length; i++) {
            Constructor c = constructors[i];
            if (isTypeMatch(args, c.getParameterTypes())) {
                return c;
            }
        }
        throw new RuntimeException("Constructor not found: " + type.getName() + "(" + toArgSig(args) + ")");
    }

    public Method findMethod(Class type, String name, Object[] args) {
        Method[] methods = type.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (!m.getName().equals(name)) {
                continue;
            }
            if (isTypeMatch(args, m.getParameterTypes())) {
                return m;
            }
        }
        throw new RuntimeException("Method not found: " + type.getName() + "." + name + "(" + toArgSig(args) + ")");
    }

    private boolean isTypeMatch(Object[] args, Class[] types) {
        int count1 = args == null ? 0 : args.length;
        int count2 = types == null ? 0 : types.length;
        if (count1 != count2) {
            return false;
        }
        if (count1 == 0) {
            return true;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                if (types[i].isPrimitive()) {
                    return false;
                }
            } else {
                Class t1 = args[i].getClass();
                Class t2 = types[i];
                if (t2.isPrimitive()) {
                    if ((t1 == Boolean.class && t2 == boolean.class) || (t1 == Character.class && t2 == char.class) || (t1 == Byte.class && (t2 == byte.class || t2 == short.class || t2 == int.class || t2 == long.class || t2 == float.class || t2 == double.class)) || (t1 == Short.class && (t2 == short.class || t2 == int.class || t2 == long.class || t2 == float.class || t2 == double.class)) || (t1 == Integer.class && (t2 == int.class || t2 == long.class || t2 == float.class || t2 == double.class)) || (t1 == Long.class && (t2 == long.class || t2 == float.class || t2 == double.class)) || (t1 == Float.class && (t2 == float.class || t2 == double.class)) || (t1 == Double.class && t2 == double.class)) {
                    } else {
                        return false;
                    }
                } else if (!t2.isAssignableFrom(t1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private String toArgSig(Object[] args) {
        StringBuffer argCsv = new StringBuffer();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (argCsv.length() > 0) {
                    argCsv.append(", ");
                }
                if (args[i] == null) {
                    argCsv.append("null");
                } else {
                    argCsv.append(args[i].getClass().getName());
                }
            }
        }
        if (argCsv.length() == 0) {
            argCsv.append("[noargs]");
        }
        return argCsv.toString();
    }
}
