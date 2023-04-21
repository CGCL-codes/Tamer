package iwork.eheap2.servlet;

import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;
import iwork.*;
import iwork.eheap2.*;

/**
 * Servlet that provides some basic functions for all IWork servlets
 * such as checking authorization and returning errors etc
 *
 **/
public class IWorkServlet extends HttpServlet {

    boolean DEBUG = true;

    protected EventHeap eh;

    protected String host;

    Vector allowedIPs;

    /**
     * Initializes the servlet. Reads the configuration parameters
     * from the servlet properties file. The event heap name and host
     * name are the initial parameters to the servlet. Default are
     * "iwork" and "localhost" 
     */
    public void init() throws ServletException {
        if (DEBUG) {
            System.out.println("IWorkServlet: Init called");
        }
        host = getInitParameter("hostname");
        if (host == null) {
            host = "localhost";
        }
        String allowedIPstr;
        allowedIPstr = getInitParameter("allowedIPs");
        allowedIPs = new Vector();
        try {
            parse(allowedIPstr);
        } catch (Exception e) {
            e.printStackTrace();
            allowedIPs = null;
        }
        eh = new EventHeap(host);
    }

    /** 
     * Parses the allowed ip addresses given in the config file
     * @param allowedIPstr String giving the allowed IP addresses  
     */
    protected void parse(String allowedIPstr) {
        StringTokenizer ipTok;
        String nextIP, givenIP;
        StringTokenizer rangeTok;
        if (DEBUG) {
            System.out.println("IWorkServlet: Parse called with " + allowedIPstr);
        }
        if (allowedIPstr == null) return;
        ipTok = new StringTokenizer(allowedIPstr.trim(), ";");
        while (ipTok.hasMoreTokens()) {
            nextIP = ipTok.nextToken();
            try {
                IPStr ipStr = new IPStr(nextIP);
                if (DEBUG) {
                    System.out.println("IWorkServlet: parse adding " + nextIP);
                }
                allowedIPs.addElement(ipStr);
            } catch (ParseException e) {
                if (DEBUG) System.out.println("IWorkServlet: Parse rejected invalid ip string " + nextIP);
            }
        }
    }

    /** 
     * Checks if the given IP address is allowed access 
     * @param ipAddr The IP address 
     * @return true if allowed false otherwise
     */
    protected boolean checkAccess(String ipAddr) {
        int i;
        IPStr ipStr;
        if (allowedIPs.size() == 0) return true;
        for (i = 0; i < allowedIPs.size(); i++) {
            ipStr = (IPStr) allowedIPs.elementAt(i);
            if (ipStr.check(ipAddr)) return true;
        }
        return false;
    }

    /** 
     * Handles the Get request
     * @param request HTTP request object
     * @param response HTTP response object
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    /** 
     * Handles the Post request. Does nothing excpet authorization
     * check. Subclasses should override this method
     * @param request HTTP request object
     * @param response HTTP response object 
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (DEBUG) {
                System.out.println("IWorkServlet: post called");
                System.out.println("IWorkServlet: Address = " + request.getRemoteAddr());
                System.out.println("IWorkServlet: Host = " + request.getRemoteHost());
            }
            if (!checkAccess(request.getRemoteAddr())) {
                accessDeniedReturn(response);
                return;
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            errorReturn(response, null, sw.toString());
            return;
        }
    }

    /** 
     * Returns a page containing a default error message and also a
     * clickable URL given in dest
     * @param response HTTP response object
     * @param dest Destination URL 
     */
    protected void errorReturn(HttpServletResponse response, String dest, String errMsg) {
        try {
            if (DEBUG) System.out.println("IWorkServlet: Error routine called");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<HTML>");
            out.println("<HEAD><TITLE> Error </TITLE></HEAD>");
            out.println("<BODY>");
            out.println("<H1>There was an error while processing " + "the form!</H1>");
            out.println("<p> " + errMsg + " </p>");
            if (dest != null) out.println("<H1>Click <A HREF=\"" + dest + "\">here</A> " + "to go back to the submit form</H1>");
            out.println("</BODY></HTML>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * Returns Access Denied Page
     * @param response HTTP response object
     * @exception java.io.IOException
     */
    protected void accessDeniedReturn(HttpServletResponse response) throws IOException {
        if (DEBUG) System.out.println("Access denied routine called");
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "IWorkServlet: You don't have access to these pages");
    }

    /** 
     * Sets up the response such that the user is redirected to the url
     * given by the parameter "dest"
     * @param response HTTP response object
     * @param dest Destination URL
     */
    protected void redirect(HttpServletResponse response, String dest) {
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", dest);
    }
}
