package org.t2framework.commons.meta;

import java.lang.reflect.Method;

/**
 * <#if locale="en">
 * <p>
 * {@link PropertyDesc} is a descriptor interface for method as JavaBean's
 * accessor. Without JavaBean's property, PropertyDesc should not be created,
 * and MethodDesc should be created instead.
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
public interface PropertyDesc<T> extends ConfigContainer, DescType, ExpressionAware {

    /**
	 * <#if locale="en">
	 * <p>
	 * Set read method.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param readMethod
	 */
    void setReadMethod(Method readMethod);

    /**
	 * <#if locale="en">
	 * <p>
	 * Set read {@link MethodDesc}.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param readMethodDesc
	 */
    void setReadMethodDesc(MethodDesc readMethodDesc);

    /**
	 * <#if locale="en">
	 * <p>
	 * Set property name.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param propertyName
	 */
    void setPropertyName(String propertyName);

    /**
	 * <#if locale="en">
	 * <p>
	 * Set write method.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param writeMethod
	 */
    void setWriteMethod(Method writeMethod);

    /**
	 * <#if locale="en">
	 * <p>
	 * Set write {@link MethodDesc}.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param writeMethodDesc
	 */
    void setWriteMethodDesc(MethodDesc writeMethodDesc);

    /**
	 * <#if locale="en">
	 * <p>
	 * Set property type.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param propertyType
	 */
    void setPropertyType(Class<?> propertyType);

    /**
	 * <#if locale="en">
	 * <p>
	 * Get property name.
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
    String getPropertyName();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get property type.
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
    Class<?> getPropertyType();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get read method.
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
    Method getReadMethod();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get read {@link MethodDesc}.
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
    MethodDesc getReadMethodDesc();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get write method.
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
    Method getWriteMethod();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get write {@link MethodDesc}.
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
    MethodDesc getWriteMethodDesc();

    /**
	 * <#if locale="en">
	 * <p>
	 * Set value for the instance.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param instance
	 * @param args
	 */
    void setValue(T instance, Object value);

    /**
	 * <#if locale="en">
	 * <p>
	 * Get value for the instance
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param instance
	 * @return
	 */
    Object getValue(T instance);

    /**
	 * <#if locale="en">
	 * <p>
	 * True if there is read method.
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
    boolean isReadable();

    /**
	 * <#if locale="en">
	 * <p>
	 * True if there is write method.
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
    boolean isWritable();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get the class which these property contains.
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
    Class<T> getTargetClass();

    /**
	 * <#if locale="en">
	 * <p>
	 * True if the property is enum.
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
    boolean isEnum();
}
