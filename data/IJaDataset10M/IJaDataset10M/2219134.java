package tcMonitor.teamcity.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrains.buildServer.controllers.AuthorizationInterceptor;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.BuildQueue;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.spring.web.UrlMapping;
import org.springframework.web.servlet.ModelAndView;
import tcMonitor.teamcity.utils.MonitoringContentBuilder;

/**
 * Provides a Controller for spring to register and map /tcMonitor/status.html to status.jsp
 * 
 * Author:  Net Wolf
 * Date:	2009-06-17 
 */
public class StatusPageController extends BaseController {

    private final WebControllerManager myManager;

    private final SBuildServer myServer;

    private final BuildQueue myBuildQueue;

    private final ProjectManager myProjectManager;

    private final String myPluginPath;

    private final String myUrl = "/tcMonitor/status.html";

    private final String myJsp = "status.jsp";

    public StatusPageController(SBuildServer server, ProjectManager sProjectManager, WebControllerManager manager, BuildQueue buildQueue, AuthorizationInterceptor authorizationInterceptor, PluginDescriptor pluginDescriptor, UrlMapping urlMapper) {
        super(server);
        myServer = server;
        myProjectManager = sProjectManager;
        myManager = manager;
        myBuildQueue = buildQueue;
        myPluginPath = pluginDescriptor.getPluginResourcesPath();
        authorizationInterceptor.addPathNotRequiringAuth(myUrl);
    }

    /**
     *
     * @param httpServletRequest  http request
     * @param httpServletResponse http response
     * @return object containing model object and view (page aggress)
     * @throws Exception
     */
    protected ModelAndView doHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        MonitoringContentBuilder status = new MonitoringContentBuilder(myServer, myProjectManager, myBuildQueue);
        ModelAndView mV = new ModelAndView(myPluginPath + myJsp, status.getParams());
        return mV;
    }

    public void register() {
        myManager.registerController(myUrl, this);
    }
}
