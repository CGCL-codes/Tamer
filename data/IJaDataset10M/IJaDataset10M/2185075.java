package org.t2framework.t2.contexts;

import org.t2framework.commons.annotation.Published;

/**
 * <#if locale="en">
 * <p>
 * AttributeContainer holds attributes and have set/get/remove methods.
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
@Published
public interface AttributeContainer {

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Set attribute.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param <V>
	 * @param key
	 * @param value
	 */
    <V> void setAttribute(String key, V value);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Get attribute.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param <V>
	 * @param key
	 * @return attribute value
	 */
    <V> V getAttribute(String key);

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Remove attribute.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param <V>
	 * @param key
	 * @return old attribute value
	 */
    <V> V removeAttribute(String key);
}
