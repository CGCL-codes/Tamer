package net.sf.lightbound.online.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.lightbound.controller.DefaultRequest;
import net.sf.lightbound.controller.InternalRequest;
import net.sf.lightbound.extend.DataSourceProvider;
import net.sf.lightbound.online.LightBound;
import net.sf.lightbound.online.RequestInterface;
import net.sf.lightbound.util.LightBoundUtil;

public class HttpRequestInterface implements RequestInterface {

    public InternalRequest getRequest(HttpServletRequest request, HttpServletResponse response, DataSourceProvider dataSourceProvider, LightBound onlineInterface) {
        return getRequest(request, response, dataSourceProvider, onlineInterface, request.getPathInfo());
    }

    public InternalRequest getRequest(HttpServletRequest request, HttpServletResponse response, DataSourceProvider dataSourceProvider, LightBound onlineInterface, String pathInfo) {
        pathInfo = convertPathInfo(onlineInterface, pathInfo);
        return new DefaultRequest(pathInfo, request, response, dataSourceProvider, onlineInterface);
    }

    public InternalRequest getRedirectRequest(InternalRequest oldRequest, String pathInfo, LightBound onlineInterface) {
        pathInfo = convertPathInfo(onlineInterface, pathInfo);
        ((DefaultRequest) oldRequest).setPath(pathInfo);
        return oldRequest;
    }

    private String convertPathInfo(LightBound onlineInterface, String pathInfo) {
        if (pathInfo == null || pathInfo.length() == 0 || pathInfo.trim().equals("/")) {
            pathInfo = onlineInterface.getConfig().getWelcomeFile();
        }
        if (pathInfo.startsWith(LightBoundUtil.WEB_PATH_SEPARATOR)) {
            pathInfo = pathInfo.substring(LightBoundUtil.WEB_PATH_SEPARATOR.length());
        }
        return pathInfo;
    }
}
