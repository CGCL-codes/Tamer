package edu.regis.jprobe.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.StringTokenizer;

public final class ClassFormatter {

    private static final String NL = System.getProperty("line.separator");

    private static final String TAB = "\t";

    private static final String PRIMITIVES[][] = { { "Z", "boolean" }, { "B", "byte" }, { "C", "char" }, { "D", "double" }, { "F", "float" }, { "I", "int" }, { "J", "long" }, { "S", "short" } };

    public static String getClassDefinition(Class clazz, ClassLoader loader) {
        StringBuilder sb = new StringBuilder(4096);
        sb.append("//Loaded By " + loader.getClass().getName() + NL + NL);
        try {
            sb.append(getPackages(clazz));
            sb.append(getClassInfo(clazz));
            sb.append(getFieldInfo(clazz));
            sb.append(getConstructorInfo(clazz));
            sb.append(getMethodInfo(clazz));
            sb.append("}" + NL);
        } catch (Exception e) {
            sb.append(Utilities.formatException(e, e));
        }
        return sb.toString();
    }

    private static String getPackages(Class clazz) {
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            return "package " + pkg.getName() + NL + NL;
        }
        return NL;
    }

    private static String getClassInfo(Class clazz) {
        StringBuilder sb = new StringBuilder();
        int mod = clazz.getModifiers();
        sb.append(getModifiers(mod));
        sb.append("class ");
        sb.append(clazz.getSimpleName() + " ");
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            String superName = superClazz.getName();
            sb.append("extends " + superName + " ");
        }
        Class interfaces[] = clazz.getInterfaces();
        sb.append(NL + TAB);
        if (interfaces.length > 0) {
            sb.append("implements ");
            for (int i = 0; i < interfaces.length; i++) {
                sb.append(interfaces[i].getName());
                if ((i + 1) == interfaces.length) {
                    sb.append(" ");
                } else {
                    sb.append(", ");
                }
            }
        }
        sb.append(NL + "{" + NL + NL);
        return sb.toString();
    }

    private static String getFieldInfo(Class clazz) {
        Field field[] = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        if (field.length > 0) sb.append(TAB + "//Declared Fields" + NL);
        for (int i = 0; i < field.length; i++) {
            sb.append(TAB);
            sb.append(getModifiers(field[i].getModifiers()));
            sb.append(formatType(field[i].getType().getName()));
            sb.append(" ");
            sb.append(field[i].getName());
            sb.append(";" + NL);
        }
        return sb.toString();
    }

    private static String getConstructorInfo(Class clazz) {
        Constructor method[] = clazz.getConstructors();
        StringBuilder sb = new StringBuilder();
        if (method.length > 0) sb.append(NL + TAB + "//Constructors" + NL);
        for (int i = 0; i < method.length; i++) {
            sb.append(TAB);
            sb.append(getModifiers(method[i].getModifiers()));
            sb.append(clazz.getSimpleName());
            sb.append("(");
            Class parms[] = method[i].getParameterTypes();
            for (int k = 0; k < parms.length; k++) {
                sb.append(formatType(parms[k].getName()));
                if ((k + 1) == parms.length) {
                    sb.append(" ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append(")");
            Class exc[] = method[i].getExceptionTypes();
            if (exc.length > 0) sb.append("throws ");
            for (int k = 0; k < exc.length; k++) {
                sb.append(exc[k].getName());
                if ((k + 1) == exc.length) {
                    sb.append(" ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append(";" + NL);
        }
        return sb.toString();
    }

    private static String getMethodInfo(Class clazz) {
        Method method[] = clazz.getDeclaredMethods();
        StringBuilder sb = new StringBuilder();
        if (method.length > 0) sb.append(NL + TAB + "//Declared Methods" + NL);
        for (int i = 0; i < method.length; i++) {
            sb.append(TAB);
            sb.append(getModifiers(method[i].getModifiers()));
            sb.append(formatType(method[i].getReturnType().getName()));
            sb.append(" ");
            sb.append(method[i].getName());
            sb.append("(");
            Class parms[] = method[i].getParameterTypes();
            for (int k = 0; k < parms.length; k++) {
                sb.append(formatType(parms[k].getName()));
                if ((k + 1) == parms.length) {
                    sb.append(" ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append(")");
            Class exc[] = method[i].getExceptionTypes();
            if (exc.length > 0) sb.append("throws ");
            for (int k = 0; k < exc.length; k++) {
                sb.append(exc[k].getName());
                if ((k + 1) == exc.length) {
                    sb.append(" ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append(";" + NL);
        }
        return sb.toString();
    }

    private static String getModifiers(int mod) {
        StringBuilder sb = new StringBuilder();
        if ((mod & Modifier.PUBLIC) == Modifier.PUBLIC) sb.append("public ");
        if ((mod & Modifier.PRIVATE) == Modifier.PRIVATE) sb.append("private ");
        if ((mod & Modifier.PROTECTED) == Modifier.PROTECTED) sb.append("protected ");
        if ((mod & Modifier.STATIC) == Modifier.STATIC) sb.append("static ");
        if ((mod & Modifier.ABSTRACT) == Modifier.ABSTRACT) sb.append("abstract ");
        if ((mod & Modifier.FINAL) == Modifier.FINAL) sb.append("final ");
        if ((mod & Modifier.NATIVE) == Modifier.NATIVE) sb.append("native ");
        if ((mod & Modifier.SYNCHRONIZED) == Modifier.SYNCHRONIZED) sb.append("synchronized ");
        if ((mod & Modifier.TRANSIENT) == Modifier.TRANSIENT) sb.append("final ");
        if ((mod & Modifier.VOLATILE) == Modifier.VOLATILE) sb.append("volatile ");
        if ((mod & Modifier.INTERFACE) == Modifier.INTERFACE) sb.append("interface ");
        return sb.toString();
    }

    private static String formatType(String type) {
        if (!type.startsWith("[")) return type;
        StringTokenizer st = new StringTokenizer(type, "[");
        char vals[] = type.toCharArray();
        int count = 0;
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] == '[') count++;
        }
        String name = null;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals("]")) continue;
            name = token;
        }
        StringBuilder arrays = new StringBuilder();
        for (int i = 0; i < count; i++) {
            arrays.append("[]");
        }
        if (name.startsWith("L")) {
            return name.substring(1).replace(';', ' ').trim() + arrays.toString();
        }
        String primative = "unknown";
        for (int i = 0; i < PRIMITIVES.length; i++) {
            if (name.equals(PRIMITIVES[i][0])) {
                primative = PRIMITIVES[i][1];
            }
        }
        return primative + arrays.toString();
    }

    public static void main(String args[]) {
        ProbeCommunicationsManager pcm = new ProbeCommunicationsManager("localhost", 3022, "none", "None");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.println(getClassDefinition(pcm.getClass(), loader));
        ClassFormatter pr = new ClassFormatter();
        System.out.println(getClassDefinition(pr.getClass(), loader));
    }
}
