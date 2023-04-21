package org.apache.myfaces.trinidadinternal.context;

import org.apache.myfaces.trinidad.context.PageResolver;

/**
 * A default implementation of the page resolver that returns the view ID
 * as the physical page name.
 */
public class PageResolverDefaultImpl extends PageResolver {

    public static PageResolver sharedInstance() {
        return _SHARED_INSTANCE;
    }

    private PageResolverDefaultImpl() {
    }

    /**
   * {@inheritDoc}
   * @param viewId {@inheritDoc}
   * @return {@inheritDoc}
   */
    @Override
    public String getPhysicalURI(String viewId) {
        return viewId;
    }

    /**
   * Encodes an action URI.
   */
    @Override
    public String encodeActionURI(String actionURI) {
        return actionURI;
    }

    private static PageResolver _SHARED_INSTANCE = new PageResolverDefaultImpl();
}
