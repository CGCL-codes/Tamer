package org.util.reflection;

import com.sun.beans.ObjectHandler;
import org.apache.commons.collections.map.LRUMap;
import org.functor.Tuple;
import sun.reflect.misc.MethodUtil;
import java.beans.ExceptionListener;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;

/**
 * TAKEN from java.beans.Statement so we could expose the return value.
 *
 * A <code>Statement</code> object represents a primitive statement
 * in which a single method is applied to a target and
 * a set of arguments - as in <code>"a.setFoo(b)"</code>.
 * Note that where this example uses names
 * to denote the target and its argument, a statement
 * object does not require a name space and is constructed with
 * the values themselves.
 * The statement object associates the named method
 * with its environment as a simple set of values:
 * the target and an array of argument values.
 *
 * @author Philip Milne
 * @version 1.32 05/29/05
 * @since 1.4
 */
public class Statement {

    private static Map<Tuple, AccessibleObject> methodCache = new LRUMap(5000);

    private static Object[] emptyArray = new Object[] {};

    static ExceptionListener defaultExceptionListener = new ExceptionListener() {

        public void exceptionThrown(Exception e) {
            System.err.println(e);
            System.err.println("Continuing ...");
        }
    };

    Object target;

    String methodName;

    Object[] arguments;

    /**
	 * Creates a new <code>Statement</code> object with a <code>target</code>,
	 * <code>methodName</code> and <code>arguments</code> as per the parameters.
	 *
	 * @param target		 The target of this statement.
	 * @param methodName The methodName of this statement.
	 * @param arguments	The arguments of this statement. If <code>null</code> then an empty array will be used.
	 */
    public Statement(Object target, String methodName, Object[] arguments) {
        this.target = target;
        this.methodName = methodName;
        this.arguments = (arguments == null) ? emptyArray : arguments;
    }

    /**
	 * Returns the target of this statement.
	 *
	 * @return The target of this statement.
	 */
    public Object getTarget() {
        return target;
    }

    /**
	 * Returns the name of the method.
	 *
	 * @return The name of the method.
	 */
    public String getMethodName() {
        return methodName;
    }

    /**
	 * Returns the arguments of this statement.
	 *
	 * @return the arguments of this statement.
	 */
    public Object[] getArguments() {
        return arguments;
    }

    /**
	 * The execute method finds a method whose name is the same
	 * as the methodName property, and invokes the method on
	 * the target.
	 * <p/>
	 * When the target's class defines many methods with the given name
	 * the implementation should choose the most specific method using
	 * the algorithm specified in the Java Language Specification
	 * (15.11). The dynamic class of the target and arguments are used
	 * in place of the compile-time type information and, like the
	 * <code>java.lang.reflect.Method</code> class itself, conversion between
	 * primitive values and their associated wrapper classes is handled
	 * internally.
	 * <p/>
	 * The following method types are handled as special cases:
	 * <ul>
	 * <li>
	 * Static methods may be called by using a class object as the target.
	 * <li>
	 * The reserved method name "new" may be used to call a class's constructor
	 * as if all classes defined static "new" methods. Constructor invocations
	 * are typically considered <code>Expression</code>s rather than <code>Statement</code>s
	 * as they return a value.
	 * <li>
	 * The method names "get" and "set" defined in the <code>java.util.List</code>
	 * interface may also be applied to array instances, mapping to
	 * the static methods of the same name in the <code>Array</code> class.
	 * </ul>
	 */
    public Object execute() throws Exception {
        return invoke();
    }

    Object invoke() throws Exception {
        Object target = getTarget();
        String methodName = getMethodName();
        if (target == null || methodName == null) {
            throw new NullPointerException((target == null ? "target" : "methodName") + " should not be null");
        }
        Object[] arguments = getArguments();
        Class[] argClasses = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) argClasses[i] = (arguments[i] == null) ? null : arguments[i].getClass();
        final Tuple key = new Tuple(target.getClass(), methodName, Arrays.asList(argClasses));
        AccessibleObject m = methodCache.get(key);
        if (m == null) {
            if (target == Class.class && methodName.equals("forName")) {
                return ObjectHandler.classForName((String) arguments[0]);
            }
            m = null;
            if (target instanceof Class) {
                if (methodName.equals("new")) {
                    methodName = "newInstance";
                }
                if (methodName.equals("newInstance") && ((Class) target).isArray()) {
                    Object result = Array.newInstance(((Class) target).getComponentType(), arguments.length);
                    for (int i = 0; i < arguments.length; i++) {
                        Array.set(result, i, arguments[i]);
                    }
                    return result;
                }
                if (methodName.equals("newInstance") && arguments.length != 0) {
                    if (target == Character.class && arguments.length == 1 && argClasses[0] == String.class) {
                        return new Character(((String) arguments[0]).charAt(0));
                    }
                    m = ReflectionUtils.getConstructor((Class) target, argClasses);
                }
                if (m == null && target != Class.class) {
                    m = ReflectionUtils.getMethod((Class) target, methodName, argClasses);
                }
                if (m == null) {
                    m = ReflectionUtils.getMethod(Class.class, methodName, argClasses);
                }
            } else {
                if (target.getClass().isArray() && (methodName.equals("set") || methodName.equals("get"))) {
                    int index = ((Integer) arguments[0]).intValue();
                    if (methodName.equals("get")) {
                        return Array.get(target, index);
                    } else {
                        Array.set(target, index, arguments[1]);
                        return null;
                    }
                }
                m = ReflectionUtils.getMethod(target.getClass(), methodName, argClasses);
            }
            if (m != null) methodCache.put(key, m);
        }
        if (m != null) {
            try {
                if (m instanceof Method) {
                    return MethodUtil.invoke((Method) m, target, arguments);
                } else {
                    return ((Constructor) m).newInstance(arguments);
                }
            } catch (IllegalAccessException iae) {
                throw new Exception("Statement cannot invoke: " + methodName + " on " + target.getClass(), iae);
            } catch (InvocationTargetException ite) {
                Throwable te = ite.getTargetException();
                if (te instanceof Exception) {
                    throw (Exception) te;
                } else {
                    throw ite;
                }
            }
        }
        throw new NoSuchMethodException(toString());
    }

    String instanceName(Object instance) {
        if (instance == null) {
            return "null";
        } else if (instance.getClass() == String.class) {
            return "\"" + (String) instance + "\"";
        } else {
            return NameGenerator.unqualifiedClassName(instance.getClass());
        }
    }

    /** Prints the value of this statement using a Java-style syntax. */
    public String toString() {
        Object target = getTarget();
        String methodName = getMethodName();
        Object[] arguments = getArguments();
        StringBuffer result = new StringBuffer(instanceName(target) + "." + methodName + "(");
        int n = arguments.length;
        for (int i = 0; i < n; i++) {
            result.append(instanceName(arguments[i]));
            if (i != n - 1) {
                result.append(", ");
            }
        }
        result.append(");");
        return result.toString();
    }
}
