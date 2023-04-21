package org.curjent.impl.agent;

import static java.util.concurrent.TimeUnit.*;
import static org.curjent.agent.MarkerType.ISOLATED;
import static org.curjent.agent.MarkerType.LEADING;
import static org.curjent.agent.MarkerType.TRAILING;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.curjent.agent.AgentException;
import org.curjent.agent.Expiration;
import org.curjent.agent.Isolated;
import org.curjent.agent.Leading;
import org.curjent.agent.Marker;
import org.curjent.agent.MarkerType;
import org.curjent.agent.Reentrant;
import org.curjent.impl.asm.Type;

/**
 * Information for bytecode generation of methods. An instance of this class is
 * created for each unique interface method. The associated task method is
 * included, and the two are evaluated for behavior, such as whether or not the
 * agent call is synchronous.
 * <p>
 * See {@link ResultInfo} for details on identifying unique methods. One or many
 * interface methods map to a single task method. One <code>MethodInfo</code>
 * instance is created for each unique interface method, whereas the associated
 * task method may be assigned to multiple <code>MethodInfo</code> instances.
 * 
 * @see ParamInfo
 * @see ResultInfo
 * @see TypeInfo
 */
final class MethodInfo {

    /**
	 * Returns the unique key for the given <code>method</code>. This includes
	 * the method's name and the JVM descriptor for the method, less the return
	 * type. For example, the key for this method is
	 * <code>"getKey(Ljava/lang/reflect/Method;)"</code>.
	 * <p>
	 * The return type is excluded in order to map interface methods to task
	 * methods. This mapping includes the methods' names and parameters but not
	 * their return types. See {@link ResultInfo} for details on uniquely
	 * identified methods.
	 */
    static String getKey(Method method) {
        return getKey(method.getName(), Type.getMethodDescriptor(method));
    }

    /**
	 * Returns the unique key for a method's <code>name</code> and its JVM
	 * <code>descriptor</code>.
	 * 
	 * @see #getKey(Method)
	 */
    private static String getKey(String name, String descriptor) {
        return name + descriptor.substring(0, descriptor.indexOf(')') + 1);
    }

    /**
	 * Lookup key for this method.
	 * 
	 * @see #getKey(Method)
	 */
    String key;

    /**
	 * Method's name.
	 * 
	 * @see Method#getName()
	 */
    String name;

    /**
	 * Interface's method. There is a one-to-one association between
	 * <code>MethodInfo</code> instances and interface methods.
	 */
    Method typeMethod;

    /**
	 * Associated task method. The same task method may be assigned to multiple
	 * <code>MethodInfo</code> instances.
	 */
    Method taskMethod;

    /**
	 * JVM descriptor for the interface's method. For example, the descriptor
	 * for this class's {@link #getKey(Method)} method is 
	 * <code>"(Ljava/lang/reflect/Method;)Ljava/lang/String;"</code>.
	 */
    String typeDescriptor;

    /**
	 * JVM descriptor for the task's method.
	 */
    String taskDescriptor;

    /**
	 * Information for the method's parameters. Possibly empty but never
	 * <code>null</code>.
	 */
    ParamInfo[] params;

    /**
	 * Total stack size for the method's arguments.
	 * 
	 * @see ParamInfo#size
	 */
    int paramTotal;

    /**
	 * Information on the interface and task results.
	 */
    ResultInfo result;

    /**
	 * Interface method's declared exceptions.
	 * 
	 * @see Method#getExceptionTypes()
	 */
    Class<?>[] typeExceptions;

    /**
	 * Internal JVM names for the interface's declared exceptions. For example,
	 * <code>"java/lang/Exception"</code> for <code>Exception</code>. This value
	 * is <code>null</code> if there are no declared exceptions.
	 */
    String[] typeExceptionInternals;

    /**
	 * Exception blocks are generated only for canonical checked exceptions. For
	 * example, if a method declares that it throws <code>IOException</code> and
	 * <code>FileNotFoundException</code>, a try-catch declaration is only
	 * generated for <code>IOException</code>.
	 */
    boolean[] typeExceptionCanonicals;

    /**
	 * Name of the message class generated for this method.
	 */
    String messageName;

    /**
	 * Internal JVM name for the message class.
	 */
    String messageInternal;

    /**
	 * Type of {@link Marker}. <code>null</code> for non-marker calls.
	 */
    MarkerType markerType;

    /**
	 * <code>true</code> if the agent call is asynchronous and the interface
	 * method returns a <code>Future</code> or <code>AgentCall</code>.
	 * 
	 * @see ResultInfo#messageFuture
	 */
    boolean future;

    /**
	 * <code>true</code> if the agent call is asynchronous and the interface
	 * method returns an <code>AgentCall</code>.
	 * 
	 * @see ResultInfo#messageCall
	 */
    boolean call;

    /**
	 * <code>true</code> if the agent call is synchronous.
	 * 
	 * @see ResultInfo#messageSynchronous
	 */
    boolean synchronous;

    /**
	 * <code>true</code> if the task's method is annotated with
	 * <code>Reentrant</code>.
	 */
    boolean reentrant;

    /**
	 * Default call site expiration timeout. <code>NIL</code> unless the
	 * method is configured with an {@link Expiration} annotation.
	 */
    long expirationTimeout = Expirations.NIL;

    /**
	 * Default call site expiration timeout unit. <code>NANOSECONDS</code>
	 * unless the method is configured with an {@link Expiration} annotation.
	 */
    TimeUnit expirationUnit = NANOSECONDS;

    /**
	 * Initializes this instance with information from the interface's method.
	 */
    MethodInfo(Method typeMethod) {
        this.typeMethod = typeMethod;
        typeDescriptor = Type.getMethodDescriptor(typeMethod);
        name = typeMethod.getName();
        key = getKey(name, typeDescriptor);
        Class<?>[] paramTypes = typeMethod.getParameterTypes();
        params = new ParamInfo[paramTypes.length];
        for (int i = 0; i < params.length; i++) {
            ParamInfo param = new ParamInfo(paramTypes[i], i);
            params[i] = param;
            paramTotal += param.size;
        }
        result = new ResultInfo(typeMethod);
        typeExceptions = typeMethod.getExceptionTypes();
        if (typeExceptions != null && typeExceptions.length > 0) {
            int count = typeExceptions.length;
            typeExceptionInternals = new String[count];
            typeExceptionCanonicals = new boolean[count];
            for (int i = 0; i < count; i++) {
                Class<?> type = typeExceptions[i];
                typeExceptionInternals[i] = Type.getInternalName(type);
                if (RuntimeException.class.isAssignableFrom(type)) {
                    continue;
                }
                if (Error.class.isAssignableFrom(type)) {
                    continue;
                }
                typeExceptionCanonicals[i] = true;
                for (int j = 0; j < i; j++) {
                    Class<?> other = typeExceptions[j];
                    if (other.isAssignableFrom(type)) {
                        typeExceptionCanonicals[i] = false;
                    } else if (type.isAssignableFrom(other)) {
                        typeExceptionCanonicals[j] = false;
                    }
                }
            }
        }
    }

    /**
	 * Creates the name of the message class based on the interface method's
	 * unique index. The order of methods is not significant. The index value is
	 * only used to ensure unique class names.
	 */
    void setTypeIndex(TypeInfo typeInfo, int index) {
        messageName = typeInfo.proxyName + "$" + index;
        messageInternal = TypeInfo.getInternalName(messageName);
    }

    /**
	 * Associates the interface method with the given task method.
	 */
    void setTaskMethod(Method taskMethod) {
        this.taskMethod = taskMethod;
        result.setTaskResult(taskMethod);
        taskDescriptor = Type.getMethodDescriptor(taskMethod);
        future = result.messageFuture;
        call = result.messageCall;
        synchronous = result.messageSynchronous;
        boolean marker = taskMethod.isAnnotationPresent(Marker.class);
        boolean leading = taskMethod.isAnnotationPresent(Leading.class);
        boolean isolated = taskMethod.isAnnotationPresent(Isolated.class);
        if (((marker ? 1 : 0) + (leading ? 1 : 0) + (isolated ? 1 : 0)) > 1) {
            throw new AgentException("Use only one of @Marker, @Leading, and @Isolated: " + taskMethod);
        }
        markerType = (marker ? TRAILING : (leading ? LEADING : (isolated ? ISOLATED : null)));
        reentrant = taskMethod.isAnnotationPresent(Reentrant.class);
        if (reentrant) {
            if (!synchronous) {
                throw new AgentException("Asynchronous method cannot be @Reentrant: " + taskMethod);
            }
            if (markerType != null) {
                throw new AgentException("@Marker, @Leading, and @Isolated methods cannot be @Reentrant: " + taskMethod);
            }
        }
        Expiration expirationAnnotation = taskMethod.getAnnotation(Expiration.class);
        if (expirationAnnotation != null) {
            expirationTimeout = expirationAnnotation.timeout();
            if (expirationTimeout < 1) {
                throw new AgentException("Expiration timeout < 1: " + expirationTimeout + ": " + taskMethod);
            }
            expirationUnit = expirationAnnotation.unit();
            if (expirationUnit == null && expirationTimeout < Long.MAX_VALUE) {
                throw new AgentException("Expiration unit is null: " + taskMethod);
            }
        }
    }
}
