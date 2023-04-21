package org.t2framework.commons.aop.spi;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * <#if locale="en">
 * <p>
 * PointCut, hook point for Interceptor.
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
public interface Pointcut {

    String DEFAULT_REGEX = ".*";

    /**
	 * <#if locale="en">
	 * <p>
	 * Pattern for this pointcut.
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
    Pattern getPattern();

    /**
	 * <#if locale="en">
	 * <p>
	 * Regex string for this pointcut.
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
    String getPatternString();

    /**
	 * <#if locale="en">
	 * <p>
	 * If the given method is applied to Aspect.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param method
	 * @return
	 */
    boolean isApplied(Method method);
}
