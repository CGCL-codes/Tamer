package org.decisiondeck.jmcda.xws.transformer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.decisiondeck.jmcda.exc.FunctionWithInputCheck;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Contains a set of functions, called transformers, accessible by return type. One transformer maximum for a given
 * return type.
 * 
 * @author Olivier Cailloux
 * 
 */
public class TransformersWithInputCheck {

    /**
     * Creates a transformers object holding the given functions. No reference is kept to the given set. The functions
     * must each have a different return type.
     * 
     * @param functions
     *            not <code>null</code>.
     */
    public TransformersWithInputCheck(Set<FunctionWithInputCheck<?, ?>> functions) {
        checkNotNull(functions);
        addAll(functions);
    }

    /**
     * Adds all the given functions to this set of functions. The functions must each have a return type that is not
     * already associated with a function.
     * 
     * @param functions
     *            not <code>null</code>.
     */
    public void addAll(Set<FunctionWithInputCheck<?, ?>> functions) {
        for (FunctionWithInputCheck<?, ?> function : functions) {
            add(function);
        }
    }

    /**
     * Adds the given function to this set of functions. The function must have a return type that is not already
     * associated with a function.
     * 
     * @param function
     *            not <code>null</code>.
     */
    public void add(FunctionWithInputCheck<?, ?> function) {
        checkNotNull(function);
        @SuppressWarnings("unchecked") final Class<? extends FunctionWithInputCheck<?, ?>> fctClass = (Class<? extends FunctionWithInputCheck<?, ?>>) function.getClass();
        final Method applyMethod = getApplyMethod(fctClass);
        final Type returnType = applyMethod.getGenericReturnType();
        final Type parameterType = applyMethod.getGenericParameterTypes()[0];
        checkArgument(!m_functionsByReturnTypes.containsKey(returnType));
        m_functionsByReturnTypes.put(returnType, function);
        m_parametersByReturnTypes.put(returnType, parameterType);
    }

    public TransformersWithInputCheck() {
    }

    /**
     * Tests whether the given return type has an associated function.
     * 
     * @param returnType
     *            not <code>null</code>.
     * @return <code>true</code> iff a function in this object has the given return type.
     */
    public boolean contains(Type returnType) {
        checkNotNull(returnType);
        return m_functionsByReturnTypes.containsKey(returnType);
    }

    private final Map<Type, FunctionWithInputCheck<?, ?>> m_functionsByReturnTypes = Maps.newHashMap();

    private final Map<Type, Type> m_parametersByReturnTypes = Maps.newHashMap();

    /**
     * Retrieves the apply method of the given class implementing a function. Only one apply method must be declared, or
     * one with object types and one with non-object types, the later one being chosen in this case. The returned method
     * is guaranteed to have only one parameter.
     * 
     * @param function
     *            not <code>null</code>.
     * @return not <code>null</code>.
     */
    public static Method getApplyMethod(Class<? extends FunctionWithInputCheck<?, ?>> function) {
        final Method[] declaredMethods = function.getDeclaredMethods();
        Set<Method> applyMethods = Sets.newHashSet();
        for (Method method : declaredMethods) {
            if (!"apply".equals(method.getName())) {
                continue;
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                continue;
            }
            applyMethods.add(method);
        }
        if (applyMethods.size() >= 3) {
            throw new IllegalArgumentException("Given function " + function + " has " + applyMethods.size() + " apply methods, that's a bit too much.");
        }
        if (applyMethods.isEmpty()) {
            throw new IllegalArgumentException("Given function " + function + " does not seem to implement apply.");
        }
        if (applyMethods.size() == 2) {
            final Iterator<Method> iterator = applyMethods.iterator();
            final Method first = iterator.next();
            final Method second = iterator.next();
            final boolean firstObj = first.getReturnType().equals(Object.class);
            final boolean secondObj = second.getReturnType().equals(Object.class);
            if (firstObj && secondObj) {
                throw new IllegalArgumentException("Given function " + function + " has " + applyMethods.size() + " apply methods, that's a bit too much.");
            }
            if (!firstObj && !secondObj) {
                throw new IllegalArgumentException("Given function " + function + " has " + applyMethods.size() + " apply methods, that's a bit too much.");
            }
            if (firstObj) {
                applyMethods.remove(first);
            } else {
                applyMethods.remove(second);
            }
        }
        return Iterables.getOnlyElement(applyMethods);
    }

    /**
     * Retrieves the type of the required parameter to use the function associated to the given return type. The return
     * type must have an associated function.
     * 
     * @param returnType
     *            not <code>null</code>, must be contained in this object.
     * @return not <code>null</code>.
     * @see #contains(Type)
     */
    public Type getRequired(Type returnType) {
        checkNotNull(returnType);
        checkArgument(contains(returnType));
        return m_parametersByReturnTypes.get(returnType);
    }

    /**
     * Retrieves the transformer function associated with the given return type. The return type must be contained in
     * this object.
     * 
     * @param returnType
     *            not <code>null</code>, in this object.
     * @return not <code>null</code>.
     */
    public FunctionWithInputCheck<?, ?> getTransformer(Type returnType) {
        checkNotNull(returnType);
        checkArgument(contains(returnType));
        return m_functionsByReturnTypes.get(returnType);
    }
}
