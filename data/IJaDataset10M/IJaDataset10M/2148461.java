package org.ddth.kiki.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.pluto.PortletContainer;
import org.apache.pluto.internal.InternalActionRequest;
import org.apache.pluto.internal.InternalActionResponse;
import org.apache.pluto.internal.InternalPortletWindow;
import org.apache.pluto.internal.InternalRenderRequest;
import org.apache.pluto.internal.InternalRenderResponse;
import org.apache.pluto.spi.optional.PortletEnvironmentService;

public class PortletEnvironmentServiceImpl implements PortletEnvironmentService {

    public InternalActionRequest createActionRequest(PortletContainer container, HttpServletRequest servletRequest, HttpServletResponse servletResponse, InternalPortletWindow internalPortletWindow) {
        if (true) throw new IllegalStateException("createActionRequest");
        return null;
    }

    public InternalActionResponse createActionResponse(PortletContainer container, HttpServletRequest servletRequest, HttpServletResponse servletResponse, InternalPortletWindow internalPortletWindow) {
        if (true) throw new IllegalStateException("createActionResponse");
        return null;
    }

    public InternalRenderRequest createRenderRequest(PortletContainer container, HttpServletRequest servletRequest, HttpServletResponse servletResponse, InternalPortletWindow internalPortletWindow) {
        if (true) throw new IllegalStateException("createRenderRequest");
        return null;
    }

    public InternalRenderResponse createRenderResponse(PortletContainer container, HttpServletRequest servletRequest, HttpServletResponse servletResponse, InternalPortletWindow internalPortletWindow) {
        if (true) throw new IllegalStateException("createRenderResponse");
        return null;
    }
}
