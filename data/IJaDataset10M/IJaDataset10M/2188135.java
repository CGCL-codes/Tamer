package org.compiere.wstore;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Click Counter.
 * 	Counts the click and forwards.
 * 	<code>
	http://www.adempiere.com/wstore/click?target=www.yahoo.com
	http://www.adempiere.com/wstore/click/www.yahoo.com
	http://www.adempiere.com/wstore/click?www.yahoo.com
 *  </code>
 *
 *  @author Jorg Janke
 *  @version $Id: Click.java,v 1.2 2006/07/30 00:53:21 jjanke Exp $
 */
public class Click extends HttpServlet {

    /**	Logging						*/
    private CLogger log = CLogger.getCLogger(getClass());

    /** Name						*/
    public static final String NAME = "click";

    /** Target Parameter			*/
    public static final String PARA_TARGET = "target";

    /**	Fallback Target				*/
    public static final String DEFAULT_TARGET = "http://www.adempiere.org/";

    /**
	 *	Initialize global variables
	 *
	 *  @param config Configuration
	 *  @throws ServletException
	 */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (!WebEnv.initWeb(config)) throw new ServletException("Click.init");
    }

    /**
	 * Get Servlet information
	 * @return Info
	 */
    public String getServletInfo() {
        return "Adempiere Click Servlet";
    }

    /**
	 * Clean up resources
	 */
    public void destroy() {
        log.fine("destroy");
    }

    /**************************************************************************
	 *  Process the HTTP Get request.
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long time = System.currentTimeMillis();
        request.getSession(true);
        String url = getTargetURL(request);
        if (!response.isCommitted()) response.sendRedirect(url);
        response.flushBuffer();
        log.fine("redirect - " + url);
        saveClick(request, url);
        log.fine(url + " - " + (System.currentTimeMillis() - time) + "ms");
    }

    /**
	 *  Process the HTTP Post request
	 *
	 *  @param request request
	 *  @param response response
	 *  @throws ServletException
	 *  @throws IOException
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
	 * 	Get Target URL.
	 * 	1 - target parameter
	 *  3 - parameter
	 *  2 - path
	 *	@param request request
	 *	@return URL
	 */
    private String getTargetURL(HttpServletRequest request) {
        String url = WebUtil.getParameter(request, PARA_TARGET);
        if (url == null || url.length() == 0) {
            Enumeration e = request.getParameterNames();
            if (e.hasMoreElements()) url = (String) e.nextElement();
        }
        if (url == null || url.length() == 0) {
            url = request.getPathInfo();
            if (url != null) url = url.substring(1);
        }
        if (url == null || url.length() == 0) url = DEFAULT_TARGET;
        if (url.indexOf("://") == -1) url = "http://" + url;
        return url;
    }

    /**
	 * 	Save Click
	 */
    private boolean saveClick(HttpServletRequest request, String url) {
        Properties ctx = JSPEnv.getCtx(request);
        MClick mc = new MClick(ctx, url, null);
        mc.setRemote_Addr(request.getRemoteAddr());
        mc.setRemote_Host(request.getRemoteHost());
        String ref = request.getHeader("referer");
        if (ref == null || ref.length() == 0) ref = request.getRequestURL().toString();
        mc.setReferrer(ref);
        mc.setAcceptLanguage(request.getHeader("accept-language"));
        mc.setUserAgent(request.getHeader("user-agent"));
        HttpSession session = request.getSession(false);
        if (session != null) {
            WebUser wu = (WebUser) session.getAttribute(WebUser.NAME);
            if (wu != null) {
                mc.setEMail(wu.getEmail());
                mc.setAD_User_ID(wu.getAD_User_ID());
            }
        }
        return mc.save();
    }
}
