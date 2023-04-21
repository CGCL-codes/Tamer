package net.sourceforge.freejava.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import net.sourceforge.freejava.util.Nullables;
import net.sourceforge.freejava.valtype.util.TypeDistance;
import net.sourceforge.freejava.valtype.util.TypeName;

public class MethodSignature {

    private final String name;

    private final Class<?>[] parameterTypes;

    private final Class<?> returnType;

    private Boolean hasNullParameterType;

    /**
     * @param name
     *            If the constructor is referred, the method name is the same to the class name.
     */
    public MethodSignature(String name, Class<?>[] parameterTypes, Class<?> returnType) {
        if (name == null) throw new NullPointerException("name");
        if (parameterTypes == null) throw new NullPointerException("parameterTypes");
        this.name = name;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }

    public MethodSignature(String name, Class<?>... parameterTypes) {
        this(name, parameterTypes, null);
    }

    public MethodSignature(Method method) {
        this(method.getName(), method.getParameterTypes(), method.getReturnType());
    }

    /**
     * The method name is set to the full qualified name of the declaring class of the constructor.
     */
    public MethodSignature(Constructor<?> ctor) {
        this(ctor.getName(), ctor.getParameterTypes());
    }

    public String getMethodName() {
        return name;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public boolean hasNullParameterType() {
        if (hasNullParameterType == null) {
            boolean hasNull = false;
            for (Class<?> t : parameterTypes) if (t == null) {
                hasNull = true;
                break;
            }
            hasNullParameterType = hasNull;
        }
        return hasNullParameterType();
    }

    private transient Integer hash;

    @Override
    public int hashCode() {
        if (this.hash == null) {
            int hash = 0xbae56896;
            hash += name.hashCode();
            hash += Arrays.hashCode(parameterTypes);
            if (returnType != null) hash += returnType.hashCode();
            this.hash = hash;
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("(" + TypeName.join(parameterTypes) + ")");
        if (returnType != null) {
            buf.append(" -> ");
            buf.append(returnType.getSimpleName());
        }
        return buf.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MethodSignature)) return false;
        return equals((MethodSignature) o);
    }

    public boolean equals(MethodSignature o) {
        if (o == this) return true;
        if (!Nullables.equals(name, o.name)) return false;
        return Arrays.equals(parameterTypes, o.parameterTypes);
    }

    public boolean matches(Method method) {
        if (!Nullables.equals(name, method.getName())) return false;
        MethodSignature o = new MethodSignature(method);
        return equals(o);
    }

    public boolean matches(Constructor<?> ctor) {
        Class<?> declaringClass = ctor.getDeclaringClass();
        if (!name.equals(declaringClass.getName())) throw new IllegalArgumentException("Not a constructor method for " + declaringClass);
        MethodSignature o = new MethodSignature(ctor);
        return equals(o);
    }

    public Method matchMethod(Class<?> clazz) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public Method matchDeclaredMethod(Class<?> clazz) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public Constructor<?> matchConstructor(Class<?> clazz) {
        try {
            return clazz.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public Constructor<?> matchedDeclaredConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public Method matchFinestMethod(Iterable<Method> methods) {
        int mindist = -1;
        Method finest = null;
        for (Method m : methods) {
            Class<?>[] o = m.getParameterTypes();
            int dist = TypeDistance.dist(parameterTypes, o);
            if (dist != -1) {
                if (mindist == -1 || dist < mindist) {
                    mindist = dist;
                    finest = m;
                }
            }
        }
        return finest;
    }

    public Constructor<?> matchFinestConstructor(Iterable<Constructor<?>> constructors) {
        int mindist = -1;
        Constructor<?> finest = null;
        for (Constructor<?> ctor : constructors) {
            Class<?>[] o = ctor.getParameterTypes();
            int dist = TypeDistance.dist(parameterTypes, o);
            if (dist != -1) {
                if (mindist == -1 || dist < mindist) {
                    mindist = dist;
                    finest = ctor;
                }
            }
        }
        return finest;
    }
}
