package com.c2b2.ipoint.processing;

import com.c2b2.ipoint.management.ManagementMBeans;
import com.c2b2.ipoint.model.Portlet;
import com.c2b2.ipoint.model.Property;
import com.c2b2.ipoint.presentation.PortletRenderer;
import com.c2b2.ipoint.presentation.PresentationException;
import com.c2b2.ipoint.presentation.portlets.ResourceURL;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
  * Id:
  *
  * Copyright 2006 C2B2 Consulting Limited. All rights reserved.
  * Use of this code is subject to license.
  * Please check your license agreement for usage restrictions
  *
  * This servlets directs resource requests directly to a portlet for rendering .
  *
  * @author $Author: steve $
  * @version $Revision: 1.2 $
  * $Date: 2007/07/03 16:57:47 $
  *
  */
public class ResourceRequestServlet extends HttpServlet {

    private Logger myLogger;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLogger = Logger.getLogger(getClass().getName());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String portletID = request.getParameter(ResourceURL.PORTLET_PARAMETER);
        try {
            Portlet portlet = Portlet.getPortlet(portletID);
            PortalRequest portalRequest = PortalRequest.getCurrentRequest();
            if (portlet.isVisibleTo(portalRequest.getCurrentUser())) {
                myLogger.info("Handling Resource Request for Portlet " + portlet.getID());
                request.setAttribute(ELConstants.IPOINT_USER, portalRequest.getCurrentUser());
                request.setAttribute(ELConstants.IPOINT_PORTAL_PROPERTIES, Property.getPropertiesMap());
                request.setAttribute(ELConstants.IPOINT_USER_PROPERTIES, portalRequest.getCurrentUser().getProperties());
                request.setAttribute(ELConstants.IPOINT_PORTLET, portlet);
                request.setAttribute(ELConstants.IPOINT_PORTLET_PROPERTIES, portlet.getProperties());
                PortletRenderer portletRenderer = portalRequest.getRenderFactory().getPortletRenderer(portlet);
                try {
                    long start = System.currentTimeMillis();
                    portletRenderer.preProcess();
                    portalRequest.getMBeans().incrementStatistics(ManagementMBeans.StatisticsType.PortletPreProcess, portlet.getName() + "-" + portlet.getID(), System.currentTimeMillis() - start, false);
                    portletRenderer.serveResource();
                    portalRequest.getMBeans().incrementStatistics(ManagementMBeans.StatisticsType.PortletResourceServe, portlet.getName() + "-" + portlet.getID(), System.currentTimeMillis() - start, false);
                } catch (PresentationException pe) {
                    myLogger.log(Level.WARNING, "Failed to preprocess portlet " + portlet.getID(), pe);
                    portletRenderer.setPreProcessingFailed(true);
                }
            } else {
                response.setStatus(response.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            myLogger.log(Level.WARNING, "Unable to process the Resource request setting the response to 404", e);
            response.setStatus(response.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
