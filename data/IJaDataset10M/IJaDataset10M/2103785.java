package com.natpryce.piazza;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import static com.natpryce.piazza.BuildMonitorController.BUILD_TYPE_ID;
import static com.natpryce.piazza.BuildMonitorController.PROJECT_ID;
import static java.util.Collections.emptyList;
import jetbrains.buildServer.web.openapi.PageExtension;

public class PiazzaLinkPageExtension implements PageExtension {

    private final Piazza piazza;

    public PiazzaLinkPageExtension(Piazza piazza) {
        this.piazza = piazza;
    }

    public String getIncludeUrl() {
        return piazza.resourcePath("piazza-link.jsp");
    }

    public List<String> getCssPaths() {
        return emptyList();
    }

    public List<String> getJsPaths() {
        return emptyList();
    }

    public String getPluginName() {
        return Piazza.PLUGIN_NAME;
    }

    public boolean isAvailable(HttpServletRequest request) {
        return isBuildTypeView(request) || isProjectView(request);
    }

    public void fillModel(Map<String, Object> model, HttpServletRequest request) {
        String queryParameter = queryParameter(request);
        model.put("piazzaHref", request.getContextPath() + Piazza.PATH + "?" + queryParameter + "=" + request.getParameter(queryParameter));
    }

    private String queryParameter(HttpServletRequest request) {
        if (isBuildTypeView(request)) {
            return BUILD_TYPE_ID;
        } else if (isProjectView(request)) {
            return PROJECT_ID;
        } else {
            throw new IllegalStateException("cannot create link for page at " + request.getRequestURI());
        }
    }

    private boolean isProjectView(HttpServletRequest request) {
        return request.getRequestURI().endsWith("/project.html");
    }

    private boolean isBuildTypeView(HttpServletRequest request) {
        return request.getRequestURI().endsWith("/viewType.html");
    }
}
