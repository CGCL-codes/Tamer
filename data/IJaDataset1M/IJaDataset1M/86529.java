package com.c2b2.ipoint.processing;

import com.c2b2.ipoint.model.ExternalLink;
import com.c2b2.ipoint.model.PersistentModelException;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.IOException;

/**
  * $Id: LinkDirector.java,v 1.4 2006/12/02 17:26:26 steve Exp $
  * 
  * Copyright 2006 C2B2 Consulting Limited. All rights reserved.
  * Use of this code is subject to license.
  * Please check your license agreement for usage restrictions
  * 
  * This class implements portal link processing. 
  * 
  * @author $Author: steve $
  * @version $Revision: 1.4 $
  * $Date: 2006/12/02 17:26:26 $
  * 
  */
public class LinkDirector extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("Link");
        String type = request.getParameter("LinkType");
        if (id != null && type != null) {
            if (type.equals("Page")) {
                String url = id + ".page";
                response.sendRedirect(url);
            } else if (type.equals("External")) {
                try {
                    ExternalLink link = ExternalLink.findLink(Long.parseLong(id));
                    String linkStr = link.getHref();
                    String testLinkStr = linkStr.toUpperCase();
                    if (!testLinkStr.startsWith("HTTP://") && !testLinkStr.startsWith("HTTPS://") && !testLinkStr.startsWith("FTP://") && !testLinkStr.startsWith("FTPS://")) {
                        linkStr = "HTTP://" + linkStr;
                    }
                    response.sendRedirect(linkStr);
                } catch (PersistentModelException e) {
                    response.sendError(response.SC_NOT_FOUND, "Link Not Found");
                }
            } else if (type.equals("Content")) {
                String page = request.getParameter("Page");
                String portlet = request.getParameter("Portlet");
                String url = page + ".page?" + portlet + "ContentID=" + id;
                Enumeration names = request.getParameterNames();
                while (names.hasMoreElements()) {
                    String name = (String) names.nextElement();
                    if (!name.equals("Page") && !name.equals("Portlet") && !name.equals("Link") && !name.equals("LinkType")) {
                        url += "&" + name + "=" + request.getParameter(name);
                    }
                }
                response.sendRedirect(url);
            }
        } else {
            throw new ServletException("Invalid request as Link had value " + id + " and LinkType had value " + type);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
