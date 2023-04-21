package org.t2framework.t2.spi;

import org.t2framework.commons.annotation.Published;
import org.t2framework.t2.contexts.WebContext;

/**
 * <#if locale="en">
 * <p>
 * Navigation is an interface to notify where next transition goes and how it is
 * executed. You can create your own {@link Navigation} concrete class.
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
public interface Navigation {

    /**
	 * <#if locale="en">
	 * <p>
	 * Execute navigation processing.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param context
	 * @throws Exception
	 */
    void execute(WebContext context) throws Exception;
}
