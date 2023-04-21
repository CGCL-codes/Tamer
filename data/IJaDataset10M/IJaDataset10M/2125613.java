package net.sourceforge.signal.tools.core.context.generator.source.java;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JavaSourceUtils {

    protected static class Wrapper {

        private Class<?> primitiveClass;

        private Class<?> wrapperClass;

        private String literalPrefix;

        private String literalSuffix;

        Wrapper(Class<?> primitiveClass, Class<?> wrapperClass) {
            this(primitiveClass, wrapperClass, null, null);
        }

        Wrapper(Class<?> primitiveClass, Class<?> wrapperClass, String literalPrefix, String literalSuffix) {
            this.primitiveClass = primitiveClass;
            this.wrapperClass = wrapperClass;
            this.literalPrefix = literalPrefix;
            this.literalSuffix = literalSuffix;
        }

        String formatWrapPrimitive(String primitive) {
            return "new " + wrapperClass.getName() + "(" + primitive + ")";
        }

        String formatUnwrapPrimitive(String wrapper) {
            return wrapper + "." + primitiveClass.getSimpleName() + "Value()";
        }

        Class<?> getPrimitiveClass() {
            return primitiveClass;
        }

        Class<?> getWrapperClass() {
            return wrapperClass;
        }

        String formatLiteral(Object value) {
            StringBuilder buf = new StringBuilder();
            if (literalPrefix != null) {
                buf.append(literalPrefix);
            }
            buf.append(value.toString());
            if (literalSuffix != null) {
                buf.append(literalSuffix);
            }
            return buf.toString();
        }
    }

    private static DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance();

    private static Map<Class<?>, Wrapper> primitiveClasses = new HashMap<Class<?>, Wrapper>();

    private static Map<Class<?>, Wrapper> wrapperClasses = new HashMap<Class<?>, Wrapper>();

    static {
        registerWrapper(new Wrapper(int.class, Integer.class));
        registerWrapper(new Wrapper(long.class, Long.class, null, "l"));
        registerWrapper(new Wrapper(short.class, Short.class, "(short) ", null));
        registerWrapper(new Wrapper(byte.class, Byte.class, "(byte) ", null));
        registerWrapper(new Wrapper(float.class, Float.class, null, "f"));
        registerWrapper(new Wrapper(double.class, Double.class));
        registerWrapper(new Wrapper(char.class, Character.class, "'", "'"));
        registerWrapper(new Wrapper(boolean.class, Boolean.class));
    }

    private static void registerWrapper(Wrapper wrapper) {
        primitiveClasses.put(wrapper.getPrimitiveClass(), wrapper);
        wrapperClasses.put(wrapper.getWrapperClass(), wrapper);
    }

    public static boolean isPrimitiveWrapper(Class<?> cls) {
        return wrapperClasses.keySet().contains(cls);
    }

    public static boolean isPrimitive(Class<?> cls) {
        return primitiveClasses.keySet().contains(cls);
    }

    public static String formatWrapPrimitive(String p, Class<?> cls) {
        if (isPrimitiveWrapper(cls)) {
            cls = wrapperToPrimitive(cls);
        }
        return primitiveClasses.get(cls).formatWrapPrimitive(p);
    }

    public static String formatUnwrapPrimitive(String w, Class<?> cls) {
        if (isPrimitive(cls)) {
            cls = primitiveToWrapper(cls);
        }
        return wrapperClasses.get(cls).formatUnwrapPrimitive(w);
    }

    public static String formatLiteral(Object value) {
        return wrapperClasses.get(value.getClass()).formatLiteral(value);
    }

    public static String formatLiteral(String str) {
        return "\"" + str + "\"";
    }

    public static String formatLiteral(Class cls) {
        return cls.getName() + ".class";
    }

    public static Class<?> primitiveToWrapper(Class<?> p) {
        return primitiveClasses.get(p).getWrapperClass();
    }

    public static Class<?> wrapperToPrimitive(Class<?> w) {
        return wrapperClasses.get(w).getPrimitiveClass();
    }

    public static synchronized String getGeneratedOnComment() {
        return "/* Generated on " + DATE_FORMAT.format(new Date()) + " */";
    }

    public static String formatCast(String expression, Class<?> cls) {
        return "((" + cls.getName() + ") " + expression + ")";
    }

    public static String formatClasses(Class<?>[] parameterTypes) {
        StringBuilder buf = new StringBuilder("new Class[]{");
        for (int i = 0; i < parameterTypes.length; i++) {
            buf.append(formatClass(parameterTypes[i]));
            if (i < parameterTypes.length - 1) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    public static String formatClass(Class<?> cls) {
        return cls.getName() + ".class";
    }

    public static String formatHandlerName(int index) {
        return "_H" + index;
    }
}
