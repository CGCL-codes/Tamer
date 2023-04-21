package org.light.portal.controller;

import static org.light.portal.util.Constants._DISABLE_PAGE_REFRESH;
import static org.light.portal.util.Constants._PORTLET_MODE_HEADER;
import static org.light.portal.util.Constants._PORTLET_RENDER_ID_PREFIX;
import static org.light.portal.util.Constants._PORTLET_TITLE_ID_PREFIX;
import java.io.IOException;
import java.util.Iterator;
import javax.portlet.Portlet;
import javax.portlet.PortletMode;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.light.portal.core.model.PortalTab;
import org.light.portal.core.model.PortletObject;
import org.light.portal.portlet.core.impl.PortletEnvironment;
import org.light.portal.portlet.core.impl.PortletWindow;
import org.light.portal.portlet.factory.PortletContainerFactory;

/**
 * 
 * @author Jianmin Liu
 **/
public class PortletController extends GenericController {

    public void execute(HttpServletRequest request, HttpServletResponse response, ControllerChain chain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        int index = uri.lastIndexOf("/");
        String path = uri.substring(index + 1);
        index = path.indexOf(".");
        if (index > 0) path = path.substring(0, index);
        request.setAttribute("portletName", path);
        Portlet portlet = PortletContainerFactory.getPortletContainer().getPortlet(path);
        if (portlet != null) {
            String portletId = (String) request.getParameter("portletId");
            String responseId = (String) request.getParameter("responseId");
            if (responseId != null) {
                try {
                    request.setAttribute("portletId", portletId);
                    request.setAttribute("responseId", responseId);
                    PortalTab tab = getPortalTab(request);
                    PortletEnvironment env = new PortletEnvironment(request, response);
                    PortletWindow portletWindow = env.getPortletWindow();
                    String isRenderUrl = (String) request.getParameter("isRenderUrl");
                    if (isRenderUrl == null) PortletContainerFactory.getPortletContainer().processPortletAction(portletWindow, request, response);
                    response.getWriter().print("<ajax-response>");
                    if (portletWindow.getPortletMode().equals(new PortletMode(_PORTLET_MODE_HEADER))) {
                        String titleId = _PORTLET_TITLE_ID_PREFIX + responseId.split("_")[1];
                        portletWindow.setPortletMode(new PortletMode(_PORTLET_MODE_HEADER));
                        response.getWriter().print("<response type='element' id='" + titleId + "'>");
                        PortletContainerFactory.getPortletContainer().renderPortlet(portletWindow, request, response);
                        response.getWriter().print("</response>");
                    } else {
                        response.getWriter().print("<response type='element' id='" + responseId + "'>");
                        PortletContainerFactory.getPortletContainer().renderPortlet(portletWindow, request, response);
                        response.getWriter().print("</response>");
                        if (portletWindow.getPortletMode().equals(PortletMode.VIEW)) {
                            String titleId = _PORTLET_TITLE_ID_PREFIX + responseId.split("_")[1];
                            portletWindow.setPortletMode(new PortletMode(_PORTLET_MODE_HEADER));
                            response.getWriter().print("<response type='element' id='" + titleId + "'>");
                            PortletContainerFactory.getPortletContainer().renderPortlet(portletWindow, request, response);
                            response.getWriter().print("</response>");
                        }
                    }
                    if (portletWindow.getPortletObject() != null && portletWindow.getPortletObject().isSupportPageRefreshed() && request.getSession().getAttribute(_DISABLE_PAGE_REFRESH) == null && request.getParameter(_DISABLE_PAGE_REFRESH) == null) {
                        Iterator<PortletObject> portlets = this.getPortletsByTab(request, tab.getId()).iterator();
                        while (portlets.hasNext()) {
                            PortletObject po = portlets.next();
                            if (po.getId() != portletWindow.getPortletObject().getId()) {
                                String id = _PORTLET_RENDER_ID_PREFIX + po.getId();
                                request.setAttribute("portletId", String.valueOf(po.getId()));
                                request.setAttribute("portletName", po.getName());
                                request.setAttribute("responseId", id);
                                request.setAttribute("mode", "view");
                                StringBuffer resultBuffer2 = new StringBuffer();
                                resultBuffer2.append("<response type='element' id='" + id + "'>");
                                response.getWriter().print(resultBuffer2.toString());
                                PortletEnvironment env2 = new PortletEnvironment(request, response);
                                PortletWindow pWindow = env2.getPortletWindow();
                                PortletContainerFactory.getPortletContainer().renderPortlet(pWindow, request, response);
                                response.getWriter().print("</response>");
                            }
                        }
                    }
                    response.getWriter().print("</ajax-response>");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Expires", "0");
                    response.setHeader("Cache-Control", "no-store");
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            } else {
                try {
                    PortletEnvironment env = new PortletEnvironment(request, response);
                    PortletWindow portletWindow = env.getPortletWindow();
                    String isRenderUrl = (String) request.getParameter("isRenderUrl");
                    if (isRenderUrl == null) PortletContainerFactory.getPortletContainer().processPortletAction(portletWindow, request, response);
                    PortletContainerFactory.getPortletContainer().renderPortlet(portletWindow, request, response);
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }
        }
        chain.execute(request, response);
    }
}
