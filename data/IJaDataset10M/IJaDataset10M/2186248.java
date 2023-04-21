package jfun.yan;

import jfun.yan.function.Function;

/**
 * Zephyr Business Solutions Corp.
 *
 * @author Ben Yu
 *
 */
final class ConstructorFunction implements Function {

    public boolean isConcrete() {
        return true;
    }

    public Object call(Object[] args) throws Throwable {
        try {
            return ctor.getConstructor().newInstance(args);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw Utils.wrapInvocationException(e);
        }
    }

    public Class[] getParameterTypes() {
        return ctor.getConstructor().getParameterTypes();
    }

    public Class getReturnType() {
        return ctor.getConstructor().getDeclaringClass();
    }

    ConstructorFunction(final java.lang.reflect.Constructor ctor) {
        this.ctor = new jfun.util.SerializableConstructor(ctor);
    }

    private final jfun.util.SerializableConstructor ctor;

    public boolean equals(Object other) {
        if (other instanceof ConstructorFunction) {
            final ConstructorFunction m2 = (ConstructorFunction) other;
            return ctor.equals(m2.ctor);
        } else return false;
    }

    public String getName() {
        return ctor.getConstructor().getName();
    }

    public int hashCode() {
        return ctor.hashCode();
    }

    public String toString() {
        return ctor.toString();
    }
}
