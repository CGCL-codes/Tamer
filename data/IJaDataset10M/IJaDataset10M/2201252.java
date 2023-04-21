package jweblite.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jweblite.web.application.JWebLiteApplication;
import jweblite.web.dispatcher.JWebLiteRequestDispatchSettings;
import jweblite.web.dispatcher.JWebLiteRequestDispatcher;
import jweblite.web.session.JWebLiteSessionManager;
import jweblite.web.wrapper.JWebLiteRequestWrapper;
import jweblite.web.wrapper.JWebLiteResponseWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet Filter implementation class JWebLiteFilter
 */
public class JWebLiteFilter implements Filter {

    private static final Log _cat = LogFactory.getLog(JWebLiteFilter.class);

    /**
	 * Default constructor.
	 */
    public JWebLiteFilter() {
        super();
    }

    /**
	 * @see Filter#init(FilterConfig)
	 */
    public void init(FilterConfig fConfig) throws ServletException {
        JWebLiteFilterConfig filterConfig = new JWebLiteFilterConfig(fConfig);
        String initClassName = filterConfig.getInitClassName();
        if (initClassName != null) {
            try {
                Class initClass = Class.forName(initClassName);
                if (initClass != null) {
                    Object initClassInstance = initClass.newInstance();
                    if (JWebLiteApplication.class.isAssignableFrom(initClass)) {
                        JWebLiteApplication.application = (JWebLiteApplication) initClassInstance;
                    }
                }
            } catch (Exception e) {
                _cat.warn("Init class failed!", e);
            }
        }
        JWebLiteApplication application = JWebLiteApplication.get();
        application.setFilterConfig(filterConfig);
        application.setRequestDispatcher(new JWebLiteRequestDispatcher(filterConfig.getUrlPathPadding()));
    }

    /**
	 * @see Filter#destroy()
	 */
    public void destroy() {
        JWebLiteApplication.get().destroy();
    }

    /**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        JWebLiteApplication application = JWebLiteApplication.get();
        JWebLiteFilterConfig filterConfig = application.getFilterConfig();
        String attrPrefix = filterConfig.getAttrPrefix();
        try {
            String servletPath = req.getServletPath();
            String reqDispatcherFowardId = attrPrefix.concat("ReqDispatcherFoward");
            JWebLiteRequestDispatchSettings reqDispatchSettings = (JWebLiteRequestDispatchSettings) req.getAttribute(reqDispatcherFowardId);
            if (reqDispatchSettings == null) {
                JWebLiteRequestDispatcher reqDispatcher = application.getRequestDispatcher();
                String refResourcePath = null;
                if (reqDispatcher != null && (reqDispatchSettings = reqDispatcher.getDispatchSettings(servletPath)) != null && (refResourcePath = reqDispatchSettings.getReferenceResourcePath()) != null) {
                    req.setAttribute(reqDispatcherFowardId, reqDispatchSettings);
                    req.getRequestDispatcher(refResourcePath).forward(req, resp);
                    return;
                }
            } else {
                if (_cat.isInfoEnabled()) {
                    _cat.info("JWebLiteRequestDispatchSettings: ".concat(reqDispatchSettings.toString()));
                }
                req.removeAttribute(reqDispatcherFowardId);
            }
            if (_cat.isInfoEnabled()) {
                _cat.info(String.format("RequestInfo: { ClientIP: %s, ReqUri: %s, ReqParam: %s }", req.getRemoteAddr(), req.getRequestURI(), req.getQueryString()));
            }
            String encoding = filterConfig.getEncoding();
            JWebLiteRequestWrapper reqWrapper = null;
            if (req instanceof JWebLiteRequestWrapper) {
                reqWrapper = (JWebLiteRequestWrapper) req;
            } else {
                reqWrapper = new JWebLiteRequestWrapper(req, encoding);
            }
            JWebLiteResponseWrapper respWrapper = null;
            if (resp instanceof JWebLiteResponseWrapper) {
                respWrapper = (JWebLiteResponseWrapper) resp;
            } else {
                respWrapper = new JWebLiteResponseWrapper(req, resp, encoding, filterConfig.isGZipEnabled());
            }
            application.doBeforeRequest(reqWrapper, respWrapper, reqDispatchSettings);
            Class reqClass = null;
            String refClassName = null;
            if (reqDispatchSettings != null && (refClassName = reqDispatchSettings.getReferenceClassName()) != null) {
                try {
                    reqClass = Class.forName(refClassName);
                } catch (Exception e) {
                }
            }
            reqWrapper.setAttribute("ContextPath", reqWrapper.getContextPath());
            boolean isIgnoreView = false;
            if (reqClass != null && JWebLitePage.class.isAssignableFrom(reqClass)) {
                try {
                    JWebLitePage reqClassInstance = (JWebLitePage) reqClass.newInstance();
                    reqWrapper.setAttribute(attrPrefix, reqClassInstance);
                    reqWrapper.setAttribute(attrPrefix.concat("Req"), reqWrapper);
                    reqWrapper.getSession(true).setAttribute(attrPrefix.concat("SessionManager"), JWebLiteSessionManager.get());
                    reqClassInstance.doRequest(reqWrapper, respWrapper);
                } catch (SkipException se) {
                    isIgnoreView = true;
                } catch (Throwable e) {
                    throw new ServletException(e);
                }
            }
            if (!isIgnoreView) {
                application.doBeforeRender(reqWrapper, respWrapper);
                chain.doFilter(reqWrapper, respWrapper);
            }
            application.doAfterRequest(reqWrapper, respWrapper);
            respWrapper.doFinish();
        } catch (Throwable e) {
            _cat.warn("Do filter failed!", e);
            String errorPage = filterConfig.getErrorPage();
            if (errorPage != null) {
                if (errorPage.equalsIgnoreCase("null")) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                } else {
                    String errorDispatcherFowardId = attrPrefix.concat("ExceptionDispatcherFoward");
                    try {
                        if (req.getAttribute(errorDispatcherFowardId) != null) {
                            throw new ServletException();
                        }
                        req.setAttribute(errorDispatcherFowardId, true);
                        req.setAttribute(attrPrefix.concat("Exception"), e);
                        req.getRequestDispatcher(errorPage).forward(req, resp);
                    } catch (Throwable e2) {
                        _cat.warn("Forward error page failed!");
                    }
                }
            } else {
                throw new ServletException(e);
            }
        }
    }
}
