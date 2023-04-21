package org.openorb.orb.rmi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.ObjectStreamClass;
import org.openorb.util.ExceptionTool;
import org.openorb.util.JREVersion;

/**
 * This is an implementation of the interface DeserializationKernel
 * for the Sun Java Runtime Environment version 1.5 onwards, which support
 * the package local newInstance method on the ObjectStreamClass.
 *
 * @version $Revision: 1.2 $ $Date: 2005/03/26 06:20:10 $
 * @author Richard G Clark
 */
final class DeserializationKernelSun15 implements DeserializationKernel {

    private static final Method NEW_INSTANCE_METHOD;

    static {
        Method method = null;
        try {
            method = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] {});
            method.setAccessible(true);
            if (!Object.class.equals(method.getReturnType())) {
                method = null;
            }
        } catch (final NoSuchMethodException e) {
        }
        NEW_INSTANCE_METHOD = method;
    }

    /**
     * Constructor is package protected so that it can be instantiated via the factory only.
     */
    DeserializationKernelSun15() {
        if (!isSupportedPlatform()) {
            throw new Error("Unsupported platform");
        }
    }

    static boolean isSupportedPlatform() {
        return JREVersion.V1_5 && (null != NEW_INSTANCE_METHOD);
    }

    private Field getAccessibleField(final Class c, final String n) {
        try {
            final Field field = c.getDeclaredField(n);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) {
            throw ExceptionTool.initCause(new Error("Field [" + n + "] could not be found on class [" + c + "]"), e);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public Object allocateNewObject(final Class c, final Class base) throws InstantiationException, IllegalAccessException {
        final ObjectStreamClass osc = ObjectStreamClass.lookup(c);
        try {
            return NEW_INSTANCE_METHOD.invoke(osc, null);
        } catch (final InvocationTargetException e) {
            final Throwable cause = e.getTargetException();
            if (cause instanceof InstantiationException) {
                throw (InstantiationException) cause;
            }
            if (cause instanceof IllegalAccessException) {
                throw (IllegalAccessException) cause;
            }
            throw ExceptionTool.initCause(new Error("Unexpected exception (" + cause + ")"), cause);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public void setObjectField(final Class c, final String n, final Object o, final Object v) {
        final Field field = getAccessibleField(c, n);
        try {
            field.set(o, v);
        } catch (final IllegalAccessException e) {
            throw ExceptionTool.initCause(new Error("Invocation not allowed! (" + e + ")"), e);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public void setBooleanField(final Class c, final String n, final Object o, final boolean v) {
        final Field field = getAccessibleField(c, n);
        try {
            field.setBoolean(o, v);
        } catch (final IllegalAccessException e) {
            throw ExceptionTool.initCause(new Error("Invocation not allowed! (" + e + ")"), e);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public void setByteField(final Class c, final String n, final Object o, final byte v) {
        final Field field = getAccessibleField(c, n);
        try {
            field.setByte(o, v);
        } catch (final IllegalAccessException e) {
            throw ExceptionTool.initCause(new Error("Invocation not allowed! (" + e + ")"), e);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public void setCharField(final Class c, final String n, final Object o, final char v) {
        final Field field = getAccessibleField(c, n);
        try {
            field.setChar(o, v);
        } catch (final IllegalAccessException e) {
            throw ExceptionTool.initCause(new Error("Invocation not allowed! (" + e + ")"), e);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public void setShortField(final Class c, final String n, final Object o, final short v) {
        final Field field = getAccessibleField(c, n);
        try {
            field.setShort(o, v);
        } catch (final IllegalAccessException e) {
            throw ExceptionTool.initCause(new Error("Invocation not allowed! (" + e + ")"), e);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public void setIntField(final Class c, final String n, final Object o, final int v) {
        final Field field = getAccessibleField(c, n);
        try {
            field.setInt(o, v);
        } catch (final IllegalAccessException e) {
            throw ExceptionTool.initCause(new Error("Invocation not allowed! (" + e + ")"), e);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public void setLongField(final Class c, final String n, final Object o, final long v) {
        final Field field = getAccessibleField(c, n);
        try {
            field.setLong(o, v);
        } catch (final IllegalAccessException e) {
            throw ExceptionTool.initCause(new Error("Invocation not allowed! (" + e + ")"), e);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public void setFloatField(final Class c, final String n, final Object o, final float v) {
        final Field field = getAccessibleField(c, n);
        try {
            field.setFloat(o, v);
        } catch (final IllegalAccessException e) {
            throw ExceptionTool.initCause(new Error("Invocation not allowed! (" + e + ")"), e);
        }
    }

    /**
     * @see DeserializationKernel
     */
    public void setDoubleField(final Class c, final String n, final Object o, final double v) {
        final Field field = getAccessibleField(c, n);
        try {
            field.setDouble(o, v);
        } catch (final IllegalAccessException e) {
            throw ExceptionTool.initCause(new Error("Invocation not allowed! (" + e + ")"), e);
        }
    }
}
