package org.wings.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.externalizer.AbstractExternalizeManager;
import org.wings.externalizer.ExternalizedResource;
import org.wings.externalizer.SystemExternalizeManager;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Central servlet delegating all requests to the according j-wings session servlet.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision: 4401 $
 */
public final class WingServlet extends HttpServlet {

    /**
     * @deprecated use {@link WingsStatistics#getStatistics()} instead
     */
    public static final int getRequestCount() {
        return WingsStatistics.getStatistics().getRequestCount();
    }

    /**
     * @deprecated use {@link WingsStatistics#getStatistics()} instead
     */
    public static final long getUptime() {
        return WingsStatistics.getStatistics().getUptime();
    }

    static {
    }

    protected static final Log logger = LogFactory.getLog("org.wings.session");

    /**
     * used to init session servlets
     */
    protected ServletConfig servletConfig = null;

    /** */
    private String lookupName = "SessionServlet";

    /**
     * TODO: documentation
     */
    public WingServlet() {
    }

    protected void initLookupName(ServletConfig config) throws ServletException {
        lookupName = config.getInitParameter("wings.servlet.lookupname");
        if (lookupName == null || lookupName.trim().length() == 0) {
            lookupName = "SessionServlet:" + config.getInitParameter("wings.mainclass");
        }
        logger.info("use session servlet lookup name " + lookupName);
    }

    /**
     * The following init parameters are known by wings.
     * <p/>
     * <dl compact>
     * <dt>externalizer.timeout</dt><dd> - The time, externalized objects
     * are kept, before they are removed</dd>
     * <p/>
     * <dt>content.maxlength</dt><dd> - Maximum content lengt for form posts.
     * Remember to increase this, if you make use of the SFileChooser
     * component</dd>
     * <p/>
     * <dt>filechooser.uploaddir</dt><dd> - The directory, where uploaded
     * files ar stored temporarily</dd>
     * </dl>
     * <p/>
     * <dt>wings.servlet.lookupname</dt><dd> - The name the wings sessions of
     * this servlet instance are stored in the servlet session hashtable</dd>
     * </dl>
     *
     * @param config
     * @throws ServletException
     */
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);
        servletConfig = config;
        if (logger.isInfoEnabled()) {
            logger.info("init-params:");
            for (Enumeration en = config.getInitParameterNames(); en.hasMoreElements(); ) {
                String param = (String) en.nextElement();
                logger.info(param + " = " + config.getInitParameter(param));
            }
        }
        initLookupName(config);
    }

    /**
     * returns the last modification of an externalized resource to allow the
     * browser to cache it.
     */
    protected long getLastModified(HttpServletRequest request) {
        AbstractExternalizeManager extMgr;
        try {
            extMgr = getExternalizeManager(request);
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
        String pathInfo = request.getPathInfo();
        if (extMgr != null && pathInfo != null && pathInfo.length() > 1) {
            String identifier = pathInfo.substring(1);
            ExternalizedResource info = extMgr.getExternalizedResource(identifier);
            if (info != null) {
                return info.getLastModified();
            }
        }
        return -1;
    }

    /**
     * Parse POST request with <code>MultipartRequest</code> and passes to <code>doGet()</code>
     */
    public final void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        SessionServlet sessionServlet = getSessionServlet(req, res, true);
        if (logger.isDebugEnabled()) logger.debug((sessionServlet != null) ? lookupName : "no session yet ..");
        try {
            int maxContentLength = sessionServlet.getSession().getMaxContentLength();
            req = new MultipartRequest(req, maxContentLength * 1024);
        } catch (Exception e) {
            logger.fatal(null, e);
        }
        if (logger.isTraceEnabled()) {
            if (req instanceof MultipartRequest) {
                MultipartRequest multi = (MultipartRequest) req;
                logger.debug("Files:");
                Iterator files = multi.getFileNames();
                while (files.hasNext()) {
                    String name = (String) files.next();
                    String filename = multi.getFileName(name);
                    String type = multi.getContentType(name);
                    File f = multi.getFile(name);
                    logger.debug("name: " + name);
                    logger.debug("filename: " + filename);
                    logger.debug("type: " + type);
                    if (f != null) {
                        logger.debug("f.toString(): " + f.toString());
                        logger.debug("f.getName(): " + f.getName());
                        logger.debug("f.exists(): " + f.exists());
                        logger.debug("f.length(): " + f.length());
                        logger.debug("\n");
                    }
                }
            }
        }
        doGet(req, res);
    }

    private final SessionServlet newSession(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        long timestamp = System.currentTimeMillis();
        try {
            logger.debug("new session");
            SessionServlet sessionServlet = new SessionServlet();
            sessionServlet.init(servletConfig, request, response);
            Session session = sessionServlet.getSession();
            sessionServlet.setParent(this);
            logger.debug("time to create a new session " + (System.currentTimeMillis() - timestamp));
            return sessionServlet;
        } catch (Exception e) {
            logger.fatal(null, e);
            throw new ServletException(e);
        }
    }

    public final SessionServlet getSessionServlet(HttpServletRequest request, HttpServletResponse response, boolean createSessionServlet) throws ServletException {
        HttpSession httpSession = request.getSession(true);
        synchronized (httpSession) {
            SessionServlet sessionServlet = null;
            if (httpSession != null) {
                sessionServlet = (SessionServlet) httpSession.getAttribute(lookupName);
            }
            if (sessionServlet == null && createSessionServlet) {
                logger.info("no session servlet, create new one");
                sessionServlet = newSession(request, response);
                httpSession.setAttribute(lookupName, sessionServlet);
            }
            if (logger.isTraceEnabled()) {
                logger.debug("session id " + request.getRequestedSessionId());
                logger.debug("session from cookie " + request.isRequestedSessionIdFromCookie());
                logger.debug("session from url " + request.isRequestedSessionIdFromURL());
                logger.debug("session valid " + request.isRequestedSessionIdValid());
                logger.debug("session created at " + new java.util.Date(httpSession.getCreationTime()));
                logger.debug("session httpsession id " + httpSession.getId());
                logger.debug("session httpsession new " + httpSession.isNew());
                logger.debug("session last accessed at " + new java.util.Date(httpSession.getLastAccessedTime()));
                logger.debug("session max inactive interval " + httpSession.getMaxInactiveInterval());
                logger.debug("session contains wings session " + (httpSession.getAttribute(lookupName) != null));
            }
            sessionServlet.getSession().getExternalizeManager().setResponse(response);
            if ((request.getCharacterEncoding() == null)) {
                try {
                    String sessionCharacterEncoding = sessionServlet.getSession().getCharacterEncoding();
                    logger.debug("Advising servlet container to interpret request as " + sessionCharacterEncoding);
                    request.setCharacterEncoding(sessionCharacterEncoding);
                } catch (UnsupportedEncodingException e) {
                    logger.warn("Problem on applying current session character encoding", e);
                }
            }
            return sessionServlet;
        }
    }

    /**
     * returns, whether this request is to serve an externalize request.
     */
    protected boolean isSystemExternalizeRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return (pathInfo != null && pathInfo.length() > 1 && pathInfo.startsWith("/-"));
    }

    /**
     *
     */
    protected AbstractExternalizeManager getExternalizeManager(HttpServletRequest req) throws ServletException {
        if (isSystemExternalizeRequest(req)) {
            return SystemExternalizeManager.getSharedInstance();
        } else {
            SessionServlet sessionServlet = getSessionServlet(req, null, false);
            if (sessionServlet == null) return null;
            return sessionServlet.getSession().getExternalizeManager();
        }
    }

    /**
     * TODO: documentation
     */
    public final void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.length() == 0) {
                StringBuffer pathUrl = req.getRequestURL();
                pathUrl.append('/');
                if (req.getQueryString() != null) {
                    pathUrl.append('?').append(req.getQueryString());
                }
                logger.debug("redirect to " + pathUrl.toString());
                response.sendRedirect(pathUrl.toString());
                return;
            }
            if (isSystemExternalizeRequest(req)) {
                String identifier = pathInfo.substring(1);
                AbstractExternalizeManager extManager = SystemExternalizeManager.getSharedInstance();
                ExternalizedResource extInfo = extManager.getExternalizedResource(identifier);
                if (extInfo != null) {
                    extManager.deliver(extInfo, response, createOutputDevice(req, response, extInfo));
                }
                return;
            }
            logger.debug("session servlet");
            SessionServlet sessionServlet = getSessionServlet(req, response, true);
            sessionServlet.doGet(req, response);
        } catch (ServletException e) {
            logger.fatal("doGet", e);
            throw e;
        } catch (Throwable e) {
            logger.fatal("doGet", e);
            throw new ServletException(e);
        }
    }

    /**
     * create a Device that is used to deliver the content, that is
     * not session specific, i.e. that is delivered by the SystemExternalizer.
     * The default
     * implementation just creates a ServletDevice. You can override this
     * method to decide yourself what happens to the output. You might, for
     * instance, write some device, that logs the output for debugging
     * purposes, or one that creates a gziped output stream to transfer
     * data more efficiently. You get the request and response as well as
     * the ExternalizedResource to decide, what kind of device you want to create.
     * You can rely on the fact, that extInfo is not null.
     * Further, you can rely on the fact, that noting has been written yet
     * to the output, so that you can set you own set of Headers.
     *
     * @param request  the HttpServletRequest that is answered
     * @param response the HttpServletResponse.
     * @param extInfo  the externalized info of the resource about to be
     *                 delivered.
     */
    protected Device createOutputDevice(HttpServletRequest request, HttpServletResponse response, ExternalizedResource extInfo) throws IOException {
        return new ServletDevice(response.getOutputStream(), "ISO-8859-1");
    }
}
