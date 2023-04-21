package org.t2framework.confeito.navigation;

import org.t2framework.confeito.HttpVersion;
import org.t2framework.confeito.contexts.WebContext;

/**
 * <#if locale="en">
 * <p>
 * No operation response.
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
public class NoOperation extends WebNavigation<NoOperation> {

    /**
	 * <#if locale="en">
	 * <p>
	 * singleton instance for this navigation.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 */
    public static final NoOperation INSTANCE = new NoOperation();

    public static NoOperation noOp() {
        return INSTANCE;
    }

    @Override
    public void execute(WebContext context) throws Exception {
    }

    @Override
    public NoOperation setHttpVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
        return this;
    }
}
