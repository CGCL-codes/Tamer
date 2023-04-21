package org.zkoss.cdi.weld.context;

import org.zkoss.cdi.context.PageScoped;

/**
 * Page context for {@link PageScoped}.
 * @author henrichen
 */
public class PageContext extends AbstractZKContext {

    public PageContext() {
        super(PageScoped.class);
    }

    protected String getZKScopeName() {
        return "page";
    }
}
