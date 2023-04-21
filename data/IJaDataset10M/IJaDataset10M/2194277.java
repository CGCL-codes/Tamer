package org.skabacca.gui.services;

import org.apache.tapestry5.services.*;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AppServerLoginService;
import org.skabacca.gui.data.EquandaWebSessionUser;
import java.io.IOException;

/**
 * Filter which assures the application is also logged into the appserver (not just web)
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class AppServerLoginFilter implements ComponentRequestFilter {

    private final ApplicationStateManager asoManager;

    private final AppServerLoginService appServerLoginService;

    public AppServerLoginFilter(ApplicationStateManager asoManager, AppServerLoginService appServerLoginService) {
        this.asoManager = asoManager;
        this.appServerLoginService = appServerLoginService;
    }

    public void handleComponentEvent(ComponentEventRequestParameters componentEventRequestParameters, ComponentRequestHandler componentRequestHandler) throws IOException {
        System.out.println("AppServerLoginFilter.handle compo");
        WebSessionUser wsu = asoManager.getIfExists(WebSessionUser.class);
        System.out.println("webSessionUser=" + wsu);
        if (null != wsu) appServerLoginService.appServerLogin(wsu);
        componentRequestHandler.handleComponentEvent(componentEventRequestParameters);
    }

    public void handlePageRender(PageRenderRequestParameters pageRenderRequestParameters, ComponentRequestHandler componentRequestHandler) throws IOException {
        System.out.println("AppServerLoginFilter.handle page");
        WebSessionUser wsu = asoManager.getIfExists(WebSessionUser.class);
        System.out.println("webSessionUser=" + wsu);
        if (null != wsu) appServerLoginService.appServerLogin(wsu);
        componentRequestHandler.handlePageRender(pageRenderRequestParameters);
    }
}
