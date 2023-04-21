package org.openje.http.servlet;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RequestDispatcherTest extends HttpServlet {

    Vector includedServletPathByServletContext, forwardedServletPathByServletContext, includedServletPath, forwardedServletPath;

    static Vector tokenizeByComma(String s) {
        Vector v = new Vector();
        if (s != null) {
            StringTokenizer st = new StringTokenizer(s);
            while (st.hasMoreElements()) v.add(st.nextToken());
        }
        return v;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String s;
        s = getInitParameter("IncludedServletPathsByServletContext");
        includedServletPathByServletContext = tokenizeByComma(s);
        if (includedServletPathByServletContext.size() <= 0) {
            includedServletPathByServletContext.add("/servlet/IncludedServlet");
            includedServletPathByServletContext.add("/servlet/./IncludedServlet");
            includedServletPathByServletContext.add("/test/../servlet/IncludedServlet");
        }
        s = getInitParameter("ForwardedServletPathsByServletContext");
        forwardedServletPathByServletContext = tokenizeByComma(s);
        if (forwardedServletPathByServletContext.size() <= 0) {
            forwardedServletPathByServletContext.add("/servlet/ForwardedServlet");
            forwardedServletPathByServletContext.add("/servlet/./ForwardedServlet");
            forwardedServletPathByServletContext.add("/test/../servlet/ForwardedServlet");
        }
        s = getInitParameter("IncludedServletPaths");
        includedServletPath = tokenizeByComma(s);
        if (includedServletPath.size() <= 0) {
            includedServletPath.add("/servlet/IncludedServlet");
            includedServletPath.add("/servlet/./IncludedServlet");
            includedServletPath.add("/test/../servlet/IncludedServlet");
            includedServletPath.add("IncludedServlet");
            includedServletPath.add("./IncludedServlet");
            includedServletPath.add("../servlet/IncludedServlet");
        }
        s = getInitParameter("ForwardedServletPaths");
        forwardedServletPath = tokenizeByComma(s);
        if (forwardedServletPath.size() <= 0) {
            forwardedServletPath.add("/servlet/ForwardedServlet");
            forwardedServletPath.add("/servlet/./ForwardedServlet");
            forwardedServletPath.add("/test/../servlet/ForwardedServlet");
            forwardedServletPath.add("ForwardedServlet");
            forwardedServletPath.add("./ForwardedServlet");
            forwardedServletPath.add("../servlet/ForwardedServlet");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getQueryString();
        Hashtable qtbl;
        String what, left, right;
        int lval, rval;
        if (query == null || (what = request.getParameter("what")) == null || (left = request.getParameter("left")) == null || (right = request.getParameter("right")) == null) {
            welcome(request, response);
            return;
        }
        try {
            lval = Integer.parseInt(left);
            rval = Integer.parseInt(right);
        } catch (NumberFormatException ex) {
            PrintWriter writer = response.getWriter();
            response.setContentType("text/html");
            writer.println("<html><body>");
            writer.println("<p>invalid value(s)</p>");
            writer.println("</body></html>");
            return;
        }
        request.setAttribute("left", new Integer(left));
        request.setAttribute("right", new Integer(right));
        String path;
        if (what.startsWith("FS")) {
            path = (String) forwardedServletPath.elementAt(Integer.parseInt(what.substring(2)));
            System.out.println("Forwarded To : " + path);
            forward(request, response, getRequestDispatcherByServletContext(path));
        } else if (what.startsWith("FR")) {
            path = (String) forwardedServletPath.elementAt(Integer.parseInt(what.substring(2)));
            System.out.println("Forwarded To : " + path);
            forward(request, response, getRequestDispatcher(request, path));
        } else if (what.startsWith("IS")) {
            path = (String) includedServletPath.elementAt(Integer.parseInt(what.substring(2)));
            System.out.println("Including : " + path);
            include(request, response, getRequestDispatcherByServletContext(path));
        } else if (what.startsWith("IR")) {
            path = (String) includedServletPath.elementAt(Integer.parseInt(what.substring(2)));
            System.out.println("Including : " + path);
            include(request, response, getRequestDispatcher(request, path));
        } else welcome(request, response);
    }

    protected void welcome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<html>");
        writer.println("<body>");
        writer.println("<h1>Super Calculator</h1><hr>");
        writer.print("<form action=\"");
        writer.print(request.getRequestURI());
        writer.println("\" method=\"GET\" target=\"_self\">");
        writer.println("<input type=\"text\" name=\"left\" size=\"5\" value=\"0\">");
        writer.println("<input type=\"text\" name=\"right\" size=\"5\" value=\"0\">");
        writer.println("<select name=\"what\">");
        Enumeration e = includedServletPathByServletContext.elements();
        String s;
        int i = 0;
        while (e.hasMoreElements()) {
            s = (String) e.nextElement();
            writer.println("<option value=\"IS" + i + "\">Included Function Test By Servlet Context(" + s + ")");
            i++;
        }
        e = includedServletPath.elements();
        i = 0;
        while (e.hasMoreElements()) {
            s = (String) e.nextElement();
            writer.println("<option value=\"IR" + i + "\">Included Function Test By Servlet Request(" + s + ")");
            i++;
        }
        e = forwardedServletPathByServletContext.elements();
        i = 0;
        while (e.hasMoreElements()) {
            s = (String) e.nextElement();
            writer.println("<option value=\"FS" + i + "\">Forward Function Test By Servlet Context(" + s + ")");
            i++;
        }
        e = forwardedServletPath.elements();
        i = 0;
        while (e.hasMoreElements()) {
            s = (String) e.nextElement();
            writer.println("<option value=\"FR" + i + "\">Forward Function Test By Servlet Request(" + s + ")");
            i++;
        }
        writer.println("</select>");
        writer.println("<input type=\"submit\">");
        writer.println("</form>");
        writer.println("</body>");
        writer.println("</html>");
    }

    protected RequestDispatcher getRequestDispatcher(HttpServletRequest request, String url) {
        return request.getRequestDispatcher(url);
    }

    protected RequestDispatcher getRequestDispatcherByServletContext(String url) {
        ServletContext context = (ServletContext) getServletContext();
        return context.getRequestDispatcher(url);
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, RequestDispatcher dispatcher) throws ServletException, IOException {
        dispatcher.forward(request, response);
    }

    protected void include(HttpServletRequest request, HttpServletResponse response, RequestDispatcher dispatcher) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Included Test</title>");
        writer.println("</head>");
        writer.println("<body>");
        dispatcher.include(request, response);
        writer.println("</body>");
        writer.println("</html>");
    }
}
