package org.dynalang.mop.beans;

import java.lang.reflect.Member;
import java.lang.reflect.UndeclaredThrowableException;
import org.dynalang.classtoken.ClassToken;
import org.dynalang.mop.CallProtocol;

/**
 * A dynamic method bound to all overloaded methods of the same name in a 
 * class. One instance is created for every (declaring class, method name) 
 * pair. Invoking {@link #call(Object, CallProtocol, Object...)} will perform
 * overload resolution. In addition, you can ask it to create a resolved
 * {@link SimpleDynamicMethod} for specific arguments using 
 * {@link #getResolvedMethodFor(CallProtocol, Object...)}, that will be bound 
 * to a single overloading method and use it repeatedly to avoid multiple 
 * overload resolution.
 * @author Attila Szegedi
 * @version $Id: $
 * @param <T>
 */
public class OverloadedDynamicMethod<T extends Member> extends DynamicMethod<T> {

    static final Object NO_SUCH_METHOD = new Object();

    static final Object AMBIGUOUS_METHOD = new Object();

    private final OverloadedFixArgMethod<T> fixArgMethod = new OverloadedFixArgMethod<T>();

    private final String name;

    private final Class<?> clazz;

    private OverloadedVarArgMethod<T> varArgMethod;

    OverloadedDynamicMethod(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    /**
     * Returns a dynamic method suitable for the passed arguments, selected 
     * among all overloads of the same name. If you are certain that you will 
     * repeatedly call the method with same argument types, you can resolve the
     * method to be called once and then call it repeatedly to speed up 
     * evaluation. It is your responsibility to ensure that compatible argument 
     * types are passed on invocations, otherwise you will get an 
     * IllegalArgumentException from the reflective method invocation. 
     * "Compatible" in this context means "the call protocol object passed in 
     * invocation can marshal the arguments to the types in the method's 
     * signature". Note that the returned dynamic method will take a call 
     * protocol object in its own call() method, and it need not even be the
     * same one you used here to obtain it, as long as it is capable of correct
     * argument marshaling.
     * @param callProtocol the call protocol used to marshal the arguments. 
     * @param args the arguments whose types will be used for resolution.
     * @return a {@link SimpleDynamicMethod} bound to a single method, resolved 
     * among all overloads for the name.
     * @throws IllegalArgumentException if either no method's signature matches
     * the arguments, or if more than one method's signature matches.
     */
    public SimpleDynamicMethod<T> getResolvedMethodFor(CallProtocol callProtocol, Object... args) {
        return new SimpleDynamicMethod<T>(createInvocation(callProtocol, args).getMember());
    }

    /**
     * Returns the overloaded method name. If it represents a class' 
     * constructor, returns "&lt;init>".
     * @return the overloaded method name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the class the overloaded methods belong to.
     * @return the class the overloaded methods belong to.
     */
    public Class<?> getTargetClass() {
        return clazz;
    }

    @Override
    public Object call(Object target, CallProtocol callProtocol, Object... args) {
        try {
            return createInvocation(callProtocol, args).invoke(target);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    void onClassTokensInvalidated(ClassToken[] tokens) {
        fixArgMethod.onClassTokensInvalidated(tokens);
        if (varArgMethod != null) {
            varArgMethod.onClassTokensInvalidated(tokens);
        }
    }

    private Invocation<T> createInvocation(CallProtocol callProtocol, Object... args) {
        Object invocation = fixArgMethod.createInvocation(args, callProtocol);
        if (invocation == NO_SUCH_METHOD) {
            if (varArgMethod != null) {
                invocation = varArgMethod.createInvocation(args, callProtocol);
            }
            if (invocation == NO_SUCH_METHOD) {
                throw new IllegalArgumentException("No signature of method " + name + " on " + clazz + " matches the arguments");
            }
        }
        if (invocation == AMBIGUOUS_METHOD) {
            throw new IllegalArgumentException("Multiple signatures of method " + name + " on " + clazz + " match the arguments");
        }
        return (Invocation<T>) invocation;
    }

    void addMember(T member) {
        fixArgMethod.addMember(member);
        if (isVarArgs(member)) {
            if (varArgMethod == null) {
                varArgMethod = new OverloadedVarArgMethod<T>();
            }
            varArgMethod.addMember(member);
        }
    }
}
