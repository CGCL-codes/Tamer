package com.potix.zk.ui.impl;

import java.io.Reader;
import java.io.IOException;
import com.potix.idom.Document;
import com.potix.zk.ui.WebApp;
import com.potix.zk.ui.Desktop;
import com.potix.zk.ui.Page;
import com.potix.zk.ui.sys.UiFactory;
import com.potix.zk.ui.sys.RequestInfo;
import com.potix.zk.ui.metainfo.PageDefinition;
import com.potix.zk.ui.metainfo.PageDefinitions;

/**
 * The sketetal implementation of {@link UiFactory}.
 *
 * <p>Derived class must immplement {@link UiFactory#newSession}, which
 * depends on the Web container.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public abstract class AbstractUiFactory implements UiFactory {

    public void start(WebApp wapp) {
    }

    public void stop(WebApp wapp) {
    }

    public Desktop newDesktop(RequestInfo ri, String updateURI, String path) {
        if (path != null) {
            final int j = path.lastIndexOf('/');
            path = j >= 0 ? path.substring(0, j + 1) : null;
        }
        return new DesktopImpl(ri.getWebApp(), updateURI, path);
    }

    public Page newPage(RequestInfo ri, PageDefinition pagedef, String path) {
        return new PageImpl(pagedef);
    }

    /** Returns the page definition of the specified path, or null if not found.
	 *
	 * <p>Dependency: Execution.createComponents -&amp; Execution.getPageDefinition
	 * -&amp; UiFactory.getPageDefiition -&amp; PageDefinitions.getPageDefinition
	 */
    public PageDefinition getPageDefinition(RequestInfo ri, String path) {
        return PageDefinitions.getPageDefinition(ri.getWebApp(), ri.getLocator(), path);
    }

    /** Returns the page definition of the specified content; never null.
	 *
	 * <p>Dependency: Execution.createComponentsDirectly -&amp; Execution.getPageDefinitionDirectly
	 * -&amp; UiFactory.getPageDefiitionDirectly -&amp; PageDefintions.getPageDefinitionDirectly
	 */
    public PageDefinition getPageDefinitionDirectly(RequestInfo ri, String content, String extension) {
        return PageDefinitions.getPageDefinitionDirectly(ri.getWebApp(), ri.getLocator(), content, extension);
    }

    public PageDefinition getPageDefinitionDirectly(RequestInfo ri, Document content, String extension) {
        return PageDefinitions.getPageDefinitionDirectly(ri.getWebApp(), ri.getLocator(), content, extension);
    }

    public PageDefinition getPageDefinitionDirectly(RequestInfo ri, Reader reader, String extension) throws IOException {
        return PageDefinitions.getPageDefinitionDirectly(ri.getWebApp(), ri.getLocator(), reader, extension);
    }
}
