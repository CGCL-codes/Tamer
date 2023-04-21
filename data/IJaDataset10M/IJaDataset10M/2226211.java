package org.objectstyle.cayenne.util;

import java.util.List;

/**
 * Provides access to protected fields of WebApplicationResourceLocator.
 * 
 * @author Andrei Adamchik
 */
public class WebApplicationResourceLocatorAccess {

    protected WebApplicationResourceLocator locator;

    public WebApplicationResourceLocatorAccess(WebApplicationResourceLocator locator) {
        this.locator = locator;
    }

    public List getAdditionalContextPaths() {
        return locator.additionalContextPaths;
    }

    public List getAdditionalClassPaths() {
        return locator.additionalClassPaths;
    }

    public List getAdditionalFilesystemPaths() {
        return locator.additionalFilesystemPaths;
    }
}
