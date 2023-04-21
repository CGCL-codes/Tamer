package deesel.lang.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Invoker is a utility class that encapsulates the runtime member calling and
 * field accessors. This class knows about Invokable and GetSet objects, and
 * will pass calls on to them. But those classes may must use the Statically
 * methods, or they will cause infinite recursion. This class is not a singleton
 * (full of static methods) so that it may be overridded if desired.
 *
 * @author <a href="mailto:troyhen@comcast.net>Troy Heninger</a> Date: Oct 15,
 *         2004
 */
public class Invoker {

    /**
     * Default invoker.  May be replaced by a subclass
     */
    protected static Invoker invoker = new Invoker();

    /**
     * Converts the args parameter into the argument arrary needed for
     * invokations.
     *
     * @param args null, single object, or argument array
     * @return argument object array
     */
    protected Object[] getArgs(Object args) {
        return args == null ? null : (args instanceof Object[] ? (Object[]) args : new Object[] { args });
    }

    /**
     * Returns the default invoker.
     *
     * @return default invoker
     */
    public static Invoker getInstance() {
        return invoker;
    }

    /**
     * Performs a runtime get operation on the field requested.  It looks up and
     * uses the field or accessor in the following order: <ol>
     * <li>GetSet.get(name)</li> <li>JavaBean get'Name'()</li> <li>Map-like
     * get(name)</li> <li>Properties-like getProperty(name)</li>
     * <li>Request-like getAttribute(name)</li> </ol>
     * <p/>
     * Warning: GetSet objects must not call this member or an infinite
     * recursion will occur.  They should use getForProxy() instead.
     *
     * @param object object to get from
     * @param name   field name
     * @return value from field
     * @throws GetSetException
     *          if the field could not be accessed
     */
    public Object get(Object object, String name) throws GetSetException {
        if (object instanceof GetSet) {
            return ((GetSet) object).get(name);
        }
        return getForProxy(object, name);
    }

    /**
     * Performs a get operation on the field requested, except that it skips
     * GetSet.get().  It looks up and uses the field or accessor in the
     * following order: <ol> <li>JavaBean get'Name'()</li> <li>Map-like
     * get(name)</li> <li>Properties-like getProperty(name)</li>
     * <li>Request-like getAttribute(name)</li> </ol>
     * <p/>
     * GetSet objects should call this member if needed to perform a get.
     *
     * @param object object to get from
     * @param name   field name
     * @return value from field
     * @throws GetSetException if the field could not be accessed
     */
    public Object getForProxy(Object object, String name) throws GetSetException {
        try {
            Field field = object.getClass().getField(name);
            return field.get(object);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        try {
            for (int ix = 1; ix <= 6; ix++) {
                String methodName = null;
                Object args = null;
                switch(ix) {
                    case 1:
                        methodName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
                        break;
                    case 2:
                        methodName = name;
                        break;
                    case 3:
                        methodName = "is" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
                        break;
                    case 4:
                        methodName = "get";
                        args = name;
                        break;
                    case 5:
                        methodName = "getProperty";
                        break;
                    case 6:
                        methodName = "getAttribute";
                        break;
                }
                try {
                    return invoke(object, methodName, args);
                } catch (InvocationException e) {
                }
            }
        } catch (EnclosingException e) {
            throw new GetSetException("Exception getting " + name, e.getCause());
        }
        throw new GetSetException("No public field or getter found for " + name);
    }

    /**
     * Performs a runtime invocation on the member named passing no arguments.
     * <p/>
     * Warning: Invokable objects must not call this member or an infinite
     * recursion will occur.  They should use invokeForProxy() instead.
     *
     * @param object object to get from
     * @param method member name
     * @return value from member
     * @throws InvocationException if the member could not be invoked
     * @throws EnclosingException  wraps exception thrown by the member
     */
    public Object invoke(Object object, String method) throws InvocationException, EnclosingException {
        return invoke(object, method, null);
    }

    /**
     * Performs a runtime invocation on the member named passing the args. The
     * args may be null or an empty array for 0 arguments, the actual arument or
     * a single element array containing it for 1 argument, or the actual
     * arguments in an Object array for 2 or more.
     * <p/>
     * Warning: Invokable objects must not call this member or an infinite
     * recursion will occur.  They should use invokeForProxy() instead.
     *
     * @param object object to get from
     * @param method member name
     * @param args   argument(s)
     * @return value from member
     * @throws InvocationException if the member could not be invoked
     * @throws EnclosingException  wraps exception thrown by the member
     */
    public Object invoke(Object object, String method, Object args) throws InvocationException, EnclosingException {
        if (object instanceof Invokable) {
            try {
                return ((Invokable) object).invoke(method, args);
            } catch (Exception e) {
                throw new EnclosingException(e);
            }
        }
        return invokeForProxy(object, method, args);
    }

    /**
     * Performs an invocation on the member named passing the args. The args may
     * be null or an empty array for 0 arguments, the actual arument or a single
     * element array containing it for 1 argument, or the actual arguments in an
     * Object array for 2 or more.
     * <p/>
     * Warning: Invokable objects must not call this member or an infinite
     * recursion will occur.  They should use invokeForProxy() instead.
     *
     * @param object object to get from
     * @param method member name
     * @param args   argument(s)
     * @return value from member
     * @throws InvocationException if the member could not be invoked
     * @throws EnclosingException  wraps exception thrown by the member
     */
    public Object invokeForProxy(Object object, String method, Object args) throws InvocationException, EnclosingException {
        Method bestMethod = findMethod(object, method, args);
        if (bestMethod == null) throw new InvocationException("No matching " + method + " member found");
        try {
            return bestMethod.invoke(object, getArgs(args));
        } catch (IllegalAccessException e) {
            throw new InvocationException("Method " + method + " cannot be publicly accessed");
        } catch (InvocationTargetException e) {
            throw new EnclosingException(e.getTargetException());
        }
    }

    /**
     * Returns the best member matching the arguments.  Null is return if none
     * are found. The args may be null or an empty array for 0 arguments, the
     * actual arument or a single element array containing it for 1 argument, or
     * the actual arguments in an Object array for 2 or more.
     * <p/>
     * TODO: Use converters if no member matches
     *
     * @param target class to find member in
     * @param args   argument(s)
     * @return member found or null
     */
    public Constructor findConstructor(Class target, Object args) {
        final Constructor[] constructors = target.getConstructors();
        final Object[] argArray = getArgs(args);
        int argCount = argArray == null ? 0 : argArray.length;
        Constructor bestConstructor = null;
        Class[] bestTypes = null;
        looking: for (int ix = 0, ixz = constructors.length; ix < ixz; ix++) {
            Constructor constructor = constructors[ix];
            if (constructor.getParameterTypes().length == argCount) {
                Class[] paramTypes = constructor.getParameterTypes();
                int rank = 0;
                for (int iy = 0; iy < argCount; iy++) {
                    Object arg = argArray[iy];
                    if (arg == null) continue;
                    final Class argType = arg.getClass();
                    final Class paramType = paramTypes[iy];
                    if (!isAssignableFrom(paramType, argType)) continue looking;
                    if (bestConstructor == null) continue;
                    final Class bestType = bestTypes[iy];
                    if (paramType.equals(bestType)) continue;
                    if (bestType.isAssignableFrom(paramType)) rank++; else if (paramType.isAssignableFrom(bestType)) rank--;
                }
                if (bestConstructor == null || rank > 0) {
                    bestConstructor = constructor;
                    bestTypes = paramTypes;
                }
            }
        }
        return bestConstructor;
    }

    /**
     * Returns a converter that can convert from the source type to the target
     * class. Null is return if none are found.
     * <p/>
     * A search is made in the following order for a converter member or member:
     * <ol> <li>find a toXxxx() member in the source object, where Xxxx is the
     * target class name</li> <li>find a target member which takes only an
     * object of the source type (or its super class)</li> </ol>
     * <p/>
     * TODO: Find a toXxxx() in mixin classes.
     *
     * @param source object to convert
     * @param target desired class
     * @return converter or null
     */
    public Converter findConverter(Object source, Class target) {
        String name = target.getClass().getName();
        int dot = name.lastIndexOf('.');
        if (dot >= 0) {
            name = name.substring(dot + 1);
        }
        Method toMethod = findMethod(source, "to" + name, source);
        if (toMethod != null && isAssignableFrom(target, toMethod.getReturnType())) {
            return new Converter(source, toMethod, target);
        }
        Constructor constructor = findConstructor(target, source);
        if (constructor != null) {
            return new Converter(constructor);
        }
        return null;
    }

    /**
     * Returns the best member matching the name and arguments.  Null is return
     * if none are found. The args may be null or an empty array for 0
     * arguments, the actual arument or a single element array containing it for
     * 1 argument, or the actual arguments in an Object array for 2 or more.
     * <p/>
     * TODO: Use converters if no member matches
     *
     * @param object object to find methods on
     * @param method member name
     * @param args   argument(s)
     * @return member found or null
     */
    public Method findMethod(Object object, String method, Object args) {
        final Method[] methods = object.getClass().getMethods();
        final Object[] argArray = getArgs(args);
        int argCount = argArray == null ? 0 : argArray.length;
        Method bestMethod = null;
        Class[] bestTypes = null;
        looking: for (int ix = 0, ixz = methods.length; ix < ixz; ix++) {
            Method aMethod = methods[ix];
            if (aMethod.getName().equals(method) && aMethod.getParameterTypes().length == argCount) {
                Class[] paramTypes = aMethod.getParameterTypes();
                int rank = 0;
                for (int iy = 0; iy < argCount; iy++) {
                    Object arg = argArray[iy];
                    if (arg == null) continue;
                    final Class argType = arg.getClass();
                    final Class paramType = paramTypes[iy];
                    if (!isAssignableFrom(paramType, argType)) continue looking;
                    if (bestMethod == null) continue;
                    final Class bestType = bestTypes[iy];
                    if (paramType.equals(bestType)) continue;
                    if (bestType.isAssignableFrom(paramType)) rank++; else if (paramType.isAssignableFrom(bestType)) rank--;
                }
                if (bestMethod == null || rank > 0) {
                    bestMethod = aMethod;
                    bestTypes = paramTypes;
                }
            }
        }
        return bestMethod;
    }

    private boolean isAssignableFrom(final Class paramType, final Class argType) {
        boolean result = paramType.equals(Long.TYPE) && (argType.equals(Long.class) || argType.equals(Integer.class) || argType.equals(Short.class) || argType.equals(Byte.class)) || paramType.equals(Integer.TYPE) && (argType.equals(Integer.class) || argType.equals(Short.class) || argType.equals(Byte.class)) || paramType.equals(Short.TYPE) && (argType.equals(Short.class) || argType.equals(Byte.class)) || paramType.equals(Byte.TYPE) && argType.equals(Byte.class) || paramType.equals(Boolean.TYPE) && argType.equals(Boolean.class) || paramType.equals(Float.TYPE) && (argType.equals(Float.class) || argType.equals(Long.class) || argType.equals(Integer.class) || argType.equals(Short.class) || argType.equals(Byte.class)) || paramType.equals(Double.TYPE) && (argType.equals(Double.class) || argType.equals(Float.class) || argType.equals(Long.class) || argType.equals(Integer.class) || argType.equals(Short.class) || argType.equals(Byte.class)) || paramType.equals(Character.TYPE) && argType.equals(Character.class) || paramType.isAssignableFrom(argType);
        return result;
    }

    /**
     * Performs a runtime set operation on the field requested.  It looks up and
     * uses the field or setter in the following order: <ol>
     * <li>GetSet.set(name, value)</li> <li>JavaBean set'Name'(value)</li>
     * <li>Map-like put(name, value)</li> <li>Properties-like setProperty(name,
     * value)</li> <li>Request-like setAttribute(name, value)</li> </ol>
     * <p/>
     * Warning: GetSet objects must not call this member or an infinite
     * recursion will occur.  They should use setForProxy() instead.
     *
     * @param object object to access from
     * @param name   name of the field
     * @param value  value to store
     * @throws GetSetException if the field could not be accessed
     */
    public Object set(Object object, String name, Object value) throws GetSetException {
        if (object == null) {
            throw new NullPointerException("Dynamic member access attempted on a null object.");
        }
        if (invoker instanceof GetSet) {
            ((GetSet) object).set(name, value);
        }
        return setForProxy(object, name, value);
    }

    /**
     * Performs a set operation on the field requested, except that it skips
     * GetSet.set().  It looks up and uses the field or accessor in the
     * following order: <ol> <li>JavaBean get'Name'()</li> <li>Map-like
     * get(name)</li> <li>Properties-like getProperty(name)</li>
     * <li>Request-like getAttribute(name)</li> </ol>
     * <p/>
     * GetSet objects should call this member if needed to perform a set.
     *
     * @param object object to get from
     * @param name   field name
     * @param value  value to store
     * @throws GetSetException if the field could not be accessed
     */
    public Object setForProxy(Object object, String name, Object value) throws GetSetException {
        try {
            Field field = object.getClass().getField(name);
            if (field.getType().isAssignableFrom(value.getClass())) {
                field.set(object, value);
                return value;
            } else {
                Converter converter = findConverter(value, field.getType());
                if (converter != null) {
                    field.set(object, converter.convert(value));
                }
            }
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        try {
            for (int ix = 1; ix <= 5; ix++) {
                String methodName = null;
                Object args = value;
                switch(ix) {
                    case 1:
                        methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
                        break;
                    case 2:
                        methodName = "put";
                        args = new Object[] { name, value };
                        break;
                    case 3:
                        methodName = "set";
                        break;
                    case 4:
                        methodName = "setProperty";
                        break;
                    case 5:
                        methodName = "setAttribute";
                        break;
                }
                try {
                    invoke(object, methodName, args);
                    return value;
                } catch (InvocationException e) {
                }
            }
        } catch (EnclosingException e) {
            throw new GetSetException("Exception setting " + name + " to " + value, e.getCause());
        }
        throw new GetSetException("No public field or setter found for " + name);
    }
}
