package gleam.util.logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is an {@link InvocationHandler} to wrap an object that
 * implements an interface with a proxy implementing the same interface
 * that logs all method calls to the {@link Log} associated with the
 * object's class. The static {@link #getTraceWrapper} method provides a
 * convenient way to obtain a {@link Proxy} for such an object which
 * implements the same interface.
 * 
 * @author ian
 */
public class TraceWrapper implements InvocationHandler {

    /**
   * Create a proxy for the given <code>realObject</code> which
   * implements the <code>interfaceType</code> interface and wraps all
   * calls to the interface methods with trace-level logging. Log
   * messages go to the {@link Log} associated with the class of
   * <code>realObject</code>.
   * 
   * @param realObject the object to wrap.
   * @param interfaceType the interface type implemented by
   *          <code>realObject</code>.
   * @param logParams should we log the parameters to each method call
   *          as well as the method name?
   * @param logExceptions should we log exceptions thrown by the method?
   * @return a proxy for <code>realObject</code> that provides
   *         logging.
   */
    public static <T> T getTraceWrapper(T realObject, Class<T> interfaceType, boolean logParams, boolean logExceptions) {
        Class<?> realClass = realObject.getClass();
        Log log = LogFactory.getLog(realClass);
        TraceWrapper invocationHandler = new TraceWrapper(realObject, log, logParams, logExceptions);
        return interfaceType.cast(Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class<?>[] { interfaceType }, invocationHandler));
    }

    /**
   * The logger used to log trace messages.
   */
    private Log log;

    /**
   * The real object that receives the method invocations.
   */
    private Object realObject;

    /**
   * Should this wrapper log the parameters passed to each method call?
   */
    private boolean logParams;

    /**
   * Should this wrapper log exceptions that are thrown from method
   * calls?
   */
    private boolean logExceptions;

    /**
   * Construct a tracing wrapper for the given <code>realObject</code>,
   * logging to the given log.
   * 
   * @param realObject the object to wrap.
   * @param log the commons-logging {@link Log} used for log messages.
   * @param logParams should we log parameters passed to each method
   *          call as well as the method name?
   * @param logExceptions should we log exceptions thrown by method
   *          calls?
   */
    public TraceWrapper(Object realObject, Log log, boolean logParams, boolean logExceptions) {
        this.realObject = realObject;
        this.log = log;
        this.logParams = logParams;
        this.logExceptions = logExceptions;
    }

    /**
   * Called when a method is invoked on the proxy object. Invokes the
   * underlying method on the real object, logging entry and exit to the
   * log at trace level. If the proxied method is one declared by
   * {@link Object} (<code>toString</code>, <code>equals</code> or
   * <code>hashCode</code>) the call is not logged.
   */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(realObject, args);
        }
        if (logParams) {
            log.trace("Entering " + method.getName() + "(" + paramsAsString(method, args) + ")");
        } else {
            log.trace("Entering " + method.getName());
        }
        try {
            return method.invoke(realObject, args);
        } catch (InvocationTargetException ite) {
            Throwable thrown = ite.getCause();
            if (logExceptions && !(thrown instanceof Error)) {
                log.error(method.getName() + " threw an exception", thrown);
            }
            throw thrown;
        } finally {
            log.trace("Leaving " + method.getName());
        }
    }

    /**
   * Converts a parameter list to a string representation. Parameters of
   * type {@link String} are escaped as Java string literals, other
   * parameter types are simply <code>toString</code>ed.
   * 
   * @param method
   * @param args
   * @return
   */
    private static String paramsAsString(Method method, Object[] args) {
        Class<?>[] paramTypes = method.getParameterTypes();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paramTypes.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            if (paramTypes[i] == String.class) {
                builder.append('"');
                builder.append(StringEscapeUtils.escapeJava((String) args[i]));
                builder.append('"');
            } else {
                builder.append(args[i]);
            }
        }
        return builder.toString();
    }
}
