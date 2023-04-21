package org.mmon.web.service;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.mmon.web.ServletUtilities;
import org.mmon.data.*;

public class Addobject extends HttpServlet {

    private static String title = "Add Object";

    private void addForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<FORM method=get action=\"addobj\">");
        out.println("<INPUT type=text name=ObjName></INPUT><p>");
        out.println("Object Type:<SELECT name=ObjType>" + "<OPTION>host</OPTION>" + "<OPTION>router</OPTION>" + "</SELECT>");
        out.println("<INPUT type=SUBMIT VALUE=\"ADD\"></INPUT>");
        out.println("</FORM>");
    }

    private void addObject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = null;
        String type = null;
        PrintWriter out = response.getWriter();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramName.equals("ObjName")) {
                name = paramValues[0];
            }
            if (paramName.equals("ObjType")) {
                type = paramValues[0];
            }
        }
        org.mmon.data.AddObject ao = new org.mmon.data.AddObject();
        ao.setName(name);
        ao.setType(type);
        ao.Add();
        response.sendRedirect("getobj");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Enumeration paramNames = request.getParameterNames();
        if (paramNames.hasMoreElements()) {
            addObject(request, response);
        } else {
            addForm(request, response);
        }
    }
}
