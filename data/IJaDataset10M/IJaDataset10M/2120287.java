package org.jgentleframework.core.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgentleframework.configure.REF;
import org.jgentleframework.configure.annotation.Inject;
import org.jgentleframework.configure.annotation.Outject;
import org.jgentleframework.configure.enums.Scope;
import org.jgentleframework.configure.enums.WrapperPrimitiveTypeList;
import org.jgentleframework.context.beans.Builder;
import org.jgentleframework.context.beans.Filter;
import org.jgentleframework.context.injecting.AbstractBeanCacher;
import org.jgentleframework.context.injecting.Provider;
import org.jgentleframework.context.injecting.scope.InvalidAddingOperationException;
import org.jgentleframework.context.injecting.scope.ScopeImplementation;
import org.jgentleframework.core.GenericException;
import org.jgentleframework.core.InvalidOperationException;
import org.jgentleframework.reflection.metadata.Definition;
import org.jgentleframework.utils.Assertor;
import org.jgentleframework.utils.ReflectUtils;
import org.jgentleframework.utils.Utils;
import org.jgentleframework.utils.data.NullClass;

/**
 * This is an abstract class that is responsible for dependency data controller
 * and processer.
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Oct 17, 2007
 */
public abstract class InOutExecutor {

    /** The log. */
    private static final Log log = LogFactory.getLog(InOutExecutor.class);

    /**
	 * Executes field inject.
	 * 
	 * @param fields
	 *            the fields
	 * @param provider
	 *            the provider
	 * @param target
	 *            the target
	 * @param definition
	 *            the definition
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 */
    public static Map<Field, Object> executesFieldInjecting(Field[] fields, Provider provider, Object target, Definition definition) throws IllegalArgumentException, IllegalAccessException {
        Map<Field, Object> result = new HashMap<Field, Object>();
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                Definition defField = definition.getMemberDefinition(fields[i]);
                if (defField != null && defField.isAnnotationPresent(Inject.class)) {
                    Inject inject = defField.getAnnotation(Inject.class);
                    Object injected = InOutExecutor.getInjectedDependency(inject, fields[i].getType(), provider);
                    fields[i].setAccessible(true);
                    Object current = fields[i].get(target);
                    if (inject.alwaysInject() == false && current != null) {
                        continue;
                    }
                    result.put(fields[i], current);
                    fields[i].set(target, injected);
                } else continue;
            }
        }
        return result;
    }

    /**
	 * Executes field outject.
	 * 
	 * @param fields
	 *            the fields
	 * @param provider
	 *            the provider
	 * @param target
	 *            the target
	 * @param definition
	 *            the definition
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 */
    public static void executesFieldOutjecting(Field[] fields, Provider provider, Object target, Definition definition) throws IllegalArgumentException, IllegalAccessException {
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                Object outjectObj = null;
                Definition defField = definition.getMemberDefinition(fields[i]);
                if (defField != null && defField.isAnnotationPresent(Outject.class)) {
                    Outject outject = defField.getAnnotation(Outject.class);
                    fields[i].setAccessible(true);
                    outjectObj = fields[i].get(target);
                    if (ReflectUtils.isCast(Builder.class, target)) {
                        Builder builder = (Builder) target;
                        if (builder.getOutjectValue(fields[i]) != null) outjectObj = builder.getOutjectValue(fields[i]);
                    }
                    InOutExecutor.setOutjectedDependency(outject, outjectObj, provider, fields[i].getClass());
                } else continue;
            }
        }
    }

    /**
	 * Executes method inject.
	 * 
	 * @param setters
	 *            the setters
	 * @param provider
	 *            the provider
	 * @param target
	 *            the target
	 * @param definition
	 *            the definition
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @return returns the {@link HashMap} containing all previous values of all
	 *         injected fields.
	 */
    public static Map<Field, Object> executesMethodInjecting(Method[] setters, Provider provider, Object target, Definition definition) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Map<Field, Object> result = new HashMap<Field, Object>();
        if (setters != null) {
            for (int i = 0; i < setters.length; i++) {
                Definition defMethod = definition.getMemberDefinition(setters[i]);
                if (defMethod == null) continue; else {
                    Object[] args = Utils.getInjectedParametersOf(setters[i], defMethod, provider);
                    setters[i].setAccessible(true);
                    Field field = null;
                    try {
                        field = Utils.getFieldOfDefaultSetGetter(setters[i], (Class<?>) definition.getKey());
                        field.setAccessible(true);
                        result.put(field, field.get(target));
                    } catch (InvalidOperationException e) {
                        throw new InOutDependencyException(e.getMessage());
                    } catch (NoSuchFieldException e) {
                    }
                    setters[i].invoke(target, args);
                }
            }
        }
        return result;
    }

    /**
	 * Executes injecting and filtering.
	 * 
	 * @param fields
	 *            the fields
	 * @param setters
	 *            the setters
	 * @param provider
	 *            the provider
	 * @param target
	 *            the target
	 * @param definition
	 *            the definition
	 * @return the map< field, object>
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 */
    public static Map<Field, Object> executesInjectingAndFiltering(Field[] fields, Method[] setters, Provider provider, Object target, Definition definition) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Map<Field, Object> map = new HashMap<Field, Object>();
        map.putAll(executesFieldInjecting(fields, provider, target, definition));
        map.putAll(executesMethodInjecting(setters, provider, target, definition));
        if (ReflectUtils.isCast(Filter.class, target)) {
            Filter filter = (Filter) target;
            filter.filters(map);
        }
        return map;
    }

    /**
	 * Executes disinjection.
	 * 
	 * @param map
	 *            the map containing all previous values of injected fields
	 *            before they are injected.
	 * @param target
	 *            the target
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
    public static void executesDisinjection(Map<Field, Object> map, Object target) throws IllegalArgumentException, IllegalAccessException {
        Assertor.notNull(map, "The current map must not be null!");
        Assertor.notNull(target, "The current target object must not be null!");
        synchronized (target) {
            for (Entry<Field, Object> entry : map.entrySet()) {
                Field field = entry.getKey();
                field.setAccessible(true);
                field.set(target, entry.getValue());
            }
        }
    }

    /**
	 * Executes method outject.
	 * 
	 * @param getters
	 *            the getters
	 * @param provider
	 *            the provider
	 * @param target
	 *            the target
	 * @param definition
	 *            the definition
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 */
    public static void executesMethodOutjecting(Method[] getters, Provider provider, Object target, Definition definition) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (getters != null) {
            for (int i = 0; i < getters.length; i++) {
                Definition defMethod = definition.getMemberDefinition(getters[i]);
                if (defMethod == null) continue; else {
                    Class<?> returnType = getters[i].getReturnType();
                    Object[] args = Utils.getInjectedParametersOf(getters[i], defMethod, provider);
                    Object outjectObj = null;
                    getters[i].setAccessible(true);
                    outjectObj = args.length == 0 ? getters[i].invoke(target) : getters[i].invoke(target, args);
                    Outject outject = defMethod.getAnnotation(Outject.class);
                    Field field = null;
                    try {
                        field = Utils.getFieldOfDefaultSetGetter(getters[i], (Class<?>) definition.getKey());
                    } catch (InvalidOperationException e) {
                        throw new InOutDependencyException(e.getMessage());
                    } catch (NoSuchFieldException e) {
                    }
                    if (ReflectUtils.isCast(Builder.class, target)) {
                        Builder builder = (Builder) target;
                        if (builder.getOutjectValue(field) != null) outjectObj = builder.getOutjectValue(field);
                    }
                    Class<?> type = field != null ? field.getClass() : returnType;
                    InOutExecutor.setOutjectedDependency(outject, outjectObj, provider, type);
                }
            }
        }
    }

    /**
	 * Returns the specified injected dependency instance.
	 * 
	 * @param inject
	 *            the corresponding {@link Inject} instance
	 * @param type
	 *            the type of original target.
	 * @param provider
	 *            the given {@link Provider}.
	 * @return returns the dependency instance if it exists, if not, returns
	 *         <b>null</b>.
	 */
    public static Object getInjectedDependency(Inject inject, Class<?> type, Provider provider) {
        Object result = null;
        String value = inject.value();
        if (provider != null) {
            if (value != null && !value.isEmpty()) {
                result = provider.getBean(value);
                if (result == null || result == NullClass.class || result == AbstractBeanCacher.NULL_SHAREDOBJECT) result = provider.getBean(type);
            } else {
                result = provider.getBean(type);
            }
        } else {
            if (log.isFatalEnabled()) {
                log.fatal("The given Provider (see " + Provider.class + ")must not be null !! ", new InOutDependencyException());
            }
        }
        if ((result == null || result == NullClass.class || result == AbstractBeanCacher.NULL_SHAREDOBJECT) && inject.required() == true) {
            throw new RequiredException("Injected dependency object must not be null.");
        }
        if (result != null && result != NullClass.class && type.isPrimitive() && result != AbstractBeanCacher.NULL_SHAREDOBJECT) {
            int i = 0;
            for (WrapperPrimitiveTypeList wt : WrapperPrimitiveTypeList.values()) {
                if (type.equals(wt.getPrimitive())) {
                    Class<?> wrappingType = wt.getWrapper();
                    if (!ReflectUtils.isCast(wrappingType, result)) {
                        throw new InOutDependencyException("The injected dependency instance can not be cast to '" + wrappingType + "'");
                    }
                }
                i++;
            }
            if (i == 0) {
                throw new InOutDependencyException("Can not identify the primitive type '" + type + "'");
            }
        } else if (result != null && result != NullClass.class && result != AbstractBeanCacher.NULL_SHAREDOBJECT) {
            if (!ReflectUtils.isCast(type, result)) {
                throw new InOutDependencyException("The injected dependency instance can not be cast to '" + type + "'");
            }
        }
        return result;
    }

    /**
	 * Sets the outjected dependency.
	 * 
	 * @param outject
	 *            the {@link Outject} annotation.
	 * @param object
	 *            the object
	 * @param provider
	 *            the provider
	 */
    public static void setOutjectedDependency(Outject outject, Object object, Provider provider, Class<?> type) {
        if (object == null && outject.required() == true) {
            throw new RequiredException("Outjected instance must not be null.");
        }
        ScopeImplementation scopeImpl = outject.scope();
        if (scopeImpl.equals(Scope.PROTOTYPE)) {
            throw new InvalidOperationException("The outjected dependency must not be prototype-scoped!");
        }
        String scopeName = null;
        if (outject.value() == null || outject.value().isEmpty()) {
            scopeName = REF.refMapping(type);
        } else {
            try {
                scopeName = Utils.createScopeName(outject.value(), provider);
            } catch (GenericException e1) {
                throw new InvalidOperationException(e1.getMessage());
            }
        }
        if (scopeName == null || scopeName.isEmpty()) throw new InvalidOperationException("Could not create the scope name from outject value name '" + outject.value() + "'");
        synchronized (scopeImpl) {
            try {
                scopeImpl.putBean(scopeName, object, provider.getObjectBeanFactory());
            } catch (InvalidAddingOperationException e) {
                e.printStackTrace();
            }
        }
    }
}
