package authregistration;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import authregistration.impl.SpecCookie;
import crypto.*;
import server.player.storage.PlayerStorage;

/**
 *
 * @author  alexog
 * @version
 */
public class LoginFilter implements Filter {

    private FilterConfig filterConfig = null;

    private SpecCookie cookie = null;

    private boolean isLogin = false;

    PlayerStorage<String, String> ipbase = null;

    public LoginFilter() {
    }

    private String username = "";

    private void doBeforeProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (debug) log("LoginFilter:DoBeforeProcessing");
        cookie = new SpecCookie();
        cookie.readCookies((HttpServletRequest) request);
        username = cookie.getUsername((HttpServletRequest) request);
        if (username != "") {
            ipbase = (PlayerStorage<String, String>) this.getFilterConfig().getServletContext().getAttribute("server.player.ip");
            if (ipbase != null) {
                if (!ipbase.entrySet().contains(username)) {
                    if (this.checkCookies((HttpServletRequest) request)) this.isLogin = true;
                } else this.isLogin = true;
            }
        }
    }

    private boolean checkCookies(HttpServletRequest request) {
        if (debug) log("LoginFilter:Cheking Cookies");
        return cookie.checkCookies();
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (debug) log("LoginFilter:DoAfterProcessing");
        if (this.isLogin) {
            log("LoginFilter:add user " + username + " to ipbase");
            ipbase.put(username, request.getLocalAddr());
            this.getFilterConfig().getServletContext().setAttribute("server.player.ip", ipbase);
        } else if (debug) log("LoginFilter:User are not login");
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (debug) log("LoginFilter:doFilter()");
        doBeforeProcessing(request, response);
        Throwable problem = null;
        try {
            if (this.isLogin) {
                chain.doFilter(request, response);
            } else {
                if (debug) log("LoginFilter:User is not login yet");
                response.setContentType("text/html");
                chain.doFilter(request, response);
            }
        } catch (Throwable t) {
            problem = t;
            t.printStackTrace();
        }
        doAfterProcessing(request, response);
        if (problem != null) {
            if (problem instanceof ServletException) throw (ServletException) problem;
            if (problem instanceof IOException) throw (IOException) problem;
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     *
     */
    public void destroy() {
        this.cookie = null;
        this.filterConfig = null;
    }

    /**
     * Init method for this filter
     *
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("LoginFilter:Initializing filter");
            }
            this.cookie = new SpecCookie();
        }
    }

    /**
     * Return a String representation of this object.
     */
    public String toString() {
        if (filterConfig == null) return ("LoginFilter()");
        StringBuffer sb = new StringBuffer("LoginFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n");
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>");
                pw.close();
                ps.close();
                response.getOutputStream().close();
                ;
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
                ;
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    private static final boolean debug = true;
}
