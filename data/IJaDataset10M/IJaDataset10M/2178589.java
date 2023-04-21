package com.xsm.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.xsm.io.IOUtil;
import com.xsm.lite.util.Logg;

public class StaticContentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logg log = Logg.getLogg("StaticContentServlet");

    private static int MAX_AGE_IN_SECONDS = 3600;

    /**
     * ReInitializes the above MAX_AGE_IN_SECONDS if a positive value set in System Properties.
     * 
     * @author Sony Mathew
     */
    private static final String MAX_AGE_IN_SECONDS_KEY = "static.content.max.age.seconds";

    static {
        try {
            String ageStr = System.getProperty(MAX_AGE_IN_SECONDS_KEY);
            if (ageStr != null && (ageStr = ageStr.trim()).length() > 0) {
                int age = Integer.parseInt(ageStr);
                if (age > 0) {
                    MAX_AGE_IN_SECONDS = age;
                    log.info("Static content max age in seconds = [" + MAX_AGE_IN_SECONDS + "]");
                }
            }
        } catch (Throwable t) {
            log.error("Failed reading System Property [" + MAX_AGE_IN_SECONDS_KEY + "]", t);
        }
    }

    /**
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        log.debug("Static Content Servlet Initialized...");
    }

    /**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        String path = ServletUtil.getRequestPath(httpRequest);
        log.debug("Requested = [" + path + "]");
        InputStream contentStream = null;
        try {
            setExpiresAge(httpResponse, MAX_AGE_IN_SECONDS);
            String contentType = getContentType(path);
            contentStream = createStream(path);
            httpResponse.setContentType(contentType);
            httpResponse.setHeader("Content-Disposition", "inline; filename=\"" + path + "\"");
            IOUtil.copyBytes(contentStream, httpResponse.getOutputStream());
            httpResponse.getOutputStream().flush();
            log.debug("Resolved = [" + path + "]");
        } catch (Throwable e) {
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
            log.debug("Failed = [" + path + "]");
        } finally {
            if (contentStream != null) contentStream.close();
        }
    }

    /**
     * Sets the appropriate headers for expiring the sent static content.
     * 
     * author Sony Mathew
     */
    private void setExpiresAge(HttpServletResponse resp, int ageInSeconds) {
        resp.setHeader("Cache-Control", "max-age=" + ageInSeconds + ", public");
    }

    private void logServletPaths(HttpServletRequest httpRequest) {
        log.debug("ContextPath = " + httpRequest.getContextPath());
        log.debug("ServletPath = " + httpRequest.getServletPath());
        log.debug("PathInfo = " + httpRequest.getPathInfo());
        log.debug("RequestURI = " + httpRequest.getRequestURI());
        log.debug("RequestURL = " + httpRequest.getRequestURL());
    }

    /**
     * author Sony Mathew
     */
    protected InputStream createStream(String resource) {
        try {
            URL resURL = this.getClass().getResource(resource);
            if (resURL != null) {
                File resFile = new File(resURL.toURI());
                if (resFile.exists() && !resFile.isDirectory()) {
                    return resURL.openStream();
                }
            }
        } catch (Exception x) {
            throw new IllegalStateException(x);
        }
        return null;
    }

    /**
     * XXX: Optimize ...
     * 
     * author Sony Mathew
     */
    public String getContentType(String resource) {
        resource = resource.toLowerCase();
        if (resource.endsWith(".gif")) {
            return "image/gif";
        } else if (resource.endsWith(".jpg") || resource.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (resource.endsWith(".js")) {
            return "application/x-javascript";
        } else if (resource.endsWith(".css")) {
            return "text/css";
        } else if (resource.endsWith(".html") || resource.endsWith(".htm") || resource.endsWith(".htmx")) {
            return "text/html";
        } else if (resource.endsWith(".pdf")) {
            return "application/pdf";
        } else {
            return "";
        }
    }
}
