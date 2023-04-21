package org.t2framework.t2.action.parameter.impl;

import java.lang.annotation.Annotation;
import javax.servlet.FilterConfig;
import org.t2framework.commons.meta.MethodDesc;
import org.t2framework.t2.action.ActionContext;
import org.t2framework.t2.action.parameter.AbstractParameterResolver;
import org.t2framework.t2.contexts.WebContext;

/**
 * <#if locale="en">
 * <p>
 * FilterConfigParameterResolver is a concrete class of ParameterResolver, and
 * is responsible for returning {@link FilterConfig} if applicable.
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
public class FilterConfigParameterResolver extends AbstractParameterResolver {

    public FilterConfigParameterResolver() {
        setTargetClass(FilterConfig.class);
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * Get {@link FilterConfig}.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 */
    @Override
    public Object resolve(ActionContext actionContext, MethodDesc md, int paramIndex, Annotation[] paramAnnotations, Class<?> paramClass) {
        return WebContext.get().getApplication().getFilterConfig();
    }
}
