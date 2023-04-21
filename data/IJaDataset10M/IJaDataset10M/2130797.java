package com.c2b2.open286.container;

import java.io.IOException;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;

/**
  * This class implements a container request dispatcher for
  * dispatching requests to servlets and JSPs.
  * <p>
  * Open 286 Portlet Container
  * Copyright 2007 C2B2 Consulting Limited. All rights reserved.
  * </p>
  */
public class ContainerRequestDispatcher implements PortletRequestDispatcher {

    public ContainerRequestDispatcher(RequestDispatcher dispatcher) {
        myDispatcher = dispatcher;
    }

    public void include(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
    }

    public void include(PortletRequest portletRequest, PortletResponse portletResponse) throws PortletException, IOException {
    }

    public void forward(PortletRequest portletRequest, PortletResponse portletResponse) throws PortletException, IOException, IllegalStateException {
    }

    RequestDispatcher myDispatcher;
}
