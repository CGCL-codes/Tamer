package org.t2framework.t2.contexts;

import java.util.Set;
import org.t2framework.commons.meta.MethodDesc;

/**
 * <#if locale="en">
 * <p>
 * ActionMethodDesc represents action methods, may or may not be invoked,from
 * user request.
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
public interface ActionMethodDesc extends Iterable<MethodDesc> {

    /**
	 * <#if locale="en">
	 * <p>
	 * Add {@link MethodDesc} for invoking candidate.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param methodDesc
	 */
    void addTargetMethodDesc(MethodDesc methodDesc);

    /**
	 * <#if locale="en">
	 * <p>
	 * Add {@link MethodDesc} for invoking candidate with alias name.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param alias
	 * @param methodDesc
	 */
    void addTargetMethodDesc(String alias, MethodDesc methodDesc);

    /**
	 * <#if locale="en">
	 * <p>
	 * Add {@link MethodDesc} for invoking as default method.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param methodDesc
	 */
    void addDefaultMethodDesc(MethodDesc methodDesc);

    /**
	 * <#if locale="en">
	 * <p>
	 * Get {@link MethodDesc} by method name.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param methodName
	 * @return method desc named this method name
	 */
    MethodDesc getMethodDesc(String methodName);

    /**
	 * <#if locale="en">
	 * <p>
	 * Get list of method names.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return set of all method names of this action method desc
	 */
    Set<String> getMethodNames();

    /**
	 * <#if locale="en">
	 * <p>
	 * If there are no {@link MethodDesc} stored.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return true if {@link MethodDesc} exists, otherwise false
	 */
    boolean isEmpty();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get size of stored {@link MethodDesc} for invoking an action.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return size of all method desc
	 */
    int getMethodDescSize();

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Stop allows to add action method and default method, then sorts and sets
	 * list of these method to be unmodifiable.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return true if frozen successfully, otherwise false
	 */
    boolean freeze();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get {@link MethodDesc} by index.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param index
	 * @return method desc by this index.
	 */
    MethodDesc getMethodDesc(int index);
}
