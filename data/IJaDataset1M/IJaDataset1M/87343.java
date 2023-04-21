package org.t2framework.commons.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <#if locale="en">
 * <p>
 * {@link MethodDesc} is a descriptor class of {@link Method}.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 */
public interface MethodDesc extends ModifierDesc, ConfigContainer, ExpressionAware, DescType {

    /**
	 * <#if locale="en">
	 * <p>
	 * Get target method.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    Method getMethod();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get method name.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    String getMethodName();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get method parameter types.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    Class<?>[] getParameterTypes();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get exception types.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    Class<?>[] getExceptionTypes();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get return type.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    Class<?> getReturnType();

    /**
	 * <#if locale="en">
	 * <p>
	 * Invoke target method.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param <T>
	 * @param t
	 * @param args
	 * @return
	 */
    <T> Object invoke(T t, Object[] args);

    /**
	 * <#if locale="en">
	 * <p>
	 * Get parameter annotations.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    Annotation[][] getParameterAnnotations();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get parameter {@link AnnotationConfig}[][].
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    AnnotationConfig[][] getParameterAnnotationConfigs();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get parameter {@link AnnotationConfig} by index.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param index
	 * @return
	 */
    AnnotationConfig[] getParameterAnnotationConfigs(int index);

    /**
	 * <#if locale="en">
	 * <p>
	 * Get parameter {@link AnnotationConfig} size.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    int getParameterAnnotationConfigSize();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get declaring class.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    Class<?> getDeclaringClass();

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Contains var args.
	 * </p>
	 * <#else>
	 * <p>
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    boolean isVarArgs();
}
