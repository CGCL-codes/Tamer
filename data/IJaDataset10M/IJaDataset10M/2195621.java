package koala.dynamicjava.classinfo;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The instances of this class provides informations about
 * class compiled to JVM bytecode.
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/05/29
 */
public class JavaClassInfo implements ClassInfo {

    /**
     * The boolean info
     */
    public static final JavaClassInfo BOOLEAN = new JavaClassInfo(boolean.class);

    /**
     * The int info
     */
    public static final JavaClassInfo INT = new JavaClassInfo(int.class);

    /**
     * The long info
     */
    public static final JavaClassInfo LONG = new JavaClassInfo(long.class);

    /**
     * The float info
     */
    public static final JavaClassInfo FLOAT = new JavaClassInfo(float.class);

    /**
     * The double info
     */
    public static final JavaClassInfo DOUBLE = new JavaClassInfo(double.class);

    /**
     * The string info
     */
    public static final JavaClassInfo STRING = new JavaClassInfo(String.class);

    /**
     * The Class info
     */
    public static final JavaClassInfo CLASS = new JavaClassInfo(Class.class);

    /**
     * The underlying class
     */
    private Class javaClass;

    /**
     * Creates a new class info
     * @param c the java class
     */
    public JavaClassInfo(Class c) {
        if (c == null) throw new IllegalArgumentException("c == null");
        javaClass = c;
    }

    /**
     * Creates a new class info representing an array
     * @param c the java class
     */
    public JavaClassInfo(JavaClassInfo c) {
        javaClass = Array.newInstance(c.javaClass, 0).getClass();
    }

    /**
     * Returns the underlying class
     */
    public Class getJavaClass() {
        return javaClass;
    }

    /**
     * Whether the underlying class needs compilation
     */
    public boolean isCompilable() {
        return false;
    }

    /**
     * Sets the compilable property
     */
    public void setCompilable(boolean b) {
        throw new IllegalStateException();
    }

    /**
     * Returns the declaring class or null
     */
    public ClassInfo getDeclaringClass() {
        Class c = javaClass.getDeclaringClass();
        return (c == null) ? null : new JavaClassInfo(c);
    }

    /**
     * Returns the declaring class of an anonymous class or null
     */
    public ClassInfo getAnonymousDeclaringClass() {
        return null;
    }

    /**
     * Returns the modifiers flags
     */
    public int getModifiers() {
        return javaClass.getModifiers();
    }

    /**
     * Returns the fully qualified name of the underlying class
     */
    public String getName() {
        return javaClass.getName();
    }

    /**
     * Returns the class info of the superclass of the class
     * represented by this info
     */
    public ClassInfo getSuperclass() {
        Class c = javaClass.getSuperclass();
        return (c == null) ? null : new JavaClassInfo(c);
    }

    /**
     * Returns the class infos of the interfaces implemented by
     * the class this info represents
     */
    public ClassInfo[] getInterfaces() {
        Class[] interfaces = javaClass.getInterfaces();
        ClassInfo[] result = new ClassInfo[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            result[i] = new JavaClassInfo(interfaces[i]);
        }
        return result;
    }

    /**
     * Returns the field infos for the current class
     */
    public FieldInfo[] getFields() {
        Field[] fields = javaClass.getDeclaredFields();
        FieldInfo[] result = new FieldInfo[fields.length];
        for (int i = 0; i < fields.length; i++) {
            result[i] = new JavaFieldInfo(fields[i]);
        }
        return result;
    }

    /**
     * Returns the constructor infos for the current class
     */
    public ConstructorInfo[] getConstructors() {
        Constructor[] constructors = javaClass.getDeclaredConstructors();
        ConstructorInfo[] result = new ConstructorInfo[constructors.length];
        for (int i = 0; i < constructors.length; i++) {
            result[i] = new JavaConstructorInfo(constructors[i]);
        }
        return result;
    }

    /**
     * Returns the method infos for the current class
     */
    public MethodInfo[] getMethods() {
        Method[] methods = javaClass.getDeclaredMethods();
        MethodInfo[] result = new MethodInfo[methods.length];
        for (int i = 0; i < methods.length; i++) {
            result[i] = new JavaMethodInfo(methods[i]);
        }
        return result;
    }

    /**
     * Returns the classes and interfaces declared as members
     * of the class represented by this ClassInfo object.
     */
    public ClassInfo[] getDeclaredClasses() {
        Class[] classes = javaClass.getDeclaredClasses();
        ClassInfo[] result = new ClassInfo[classes.length];
        for (int i = 0; i < classes.length; i++) {
            result[i] = new JavaClassInfo(classes[i]);
        }
        return result;
    }

    /**
     * Returns the array type that contains elements of this class
     */
    public ClassInfo getArrayType() {
        return new JavaClassInfo(this);
    }

    /**
     * Indicates whether some other object is "equal to" this one
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ClassInfo)) {
            return false;
        }
        return javaClass.getName().equals(((ClassInfo) obj).getName());
    }

    /**
     * Whether this object represents an interface
     */
    public boolean isInterface() {
        return javaClass.isInterface();
    }

    /**
     * Whether this object represents an array
     */
    public boolean isArray() {
        return javaClass.isArray();
    }

    /**
     * Whether this object represents a primitive type
     */
    public boolean isPrimitive() {
        return javaClass.isPrimitive();
    }

    /**
     * Returns the component type of this array type
     * @exception IllegalStateException if this type do not represent an array
     */
    public ClassInfo getComponentType() {
        if (!isArray()) throw new IllegalStateException();
        return new JavaClassInfo(javaClass.getComponentType());
    }
}
