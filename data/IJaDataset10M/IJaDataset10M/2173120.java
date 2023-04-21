package com.potix.web.util.resource;

import java.util.Map;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This interface defines an extended context that
 * {@link com.potix.web.servlet.Servlets#getRequestDispatcher} will try to
 * resolve.
 * When {@link com.potix.web.servlet.Servlets#getRequestDispatcher} is
 * called, it detects whether ~xxx/ is specified.
 * If specified, it looks up any extended context
 * is registered as xxx. If found, the extend context is used.
 * If not found, it looks up any servlet context called xxx.
 *
 * <p>To registers an extended context, use 
 * {@link com.potix.web.servlet.Servlets#addExtendedWebContext}.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public interface ExtendedWebContext {

    /** Returns the encoded URL.
	 * The returned URL is also encoded with HttpServletResponse.encodeURL.
	 *
	 * <p>It resolves "*" contained in URI, if any, to the proper Locale,
	 * and the browser code.
	 * Refer to {@link com.potix.web.servlet.Servlets#locate(javax.servlet.ServletContext, ServletRequest, String, com.potix.util.resource.Locator)}
	 * for details. 
	 *
	 * @param uri it must be empty or starts with "/". It might contain
	 * "*" for current browser code and Locale.
	 * @return the complete URI (excluding the machine name).
	 * It includes the context path and the servelt to interpret
	 * this extended resource.
	 */
    public String encodeURL(ServletRequest request, ServletResponse response, String uri) throws ServletException, UnsupportedEncodingException;

    /** Returns  the encoded URL for send redirect.
	 * The URL is also encoded with HttpServletResposne.encodeRedirectURL.
	 */
    public String encodeRedirectURL(HttpServletRequest request, HttpServletResponse response, String uri, Map params, int mode);

    public RequestDispatcher getRequestDispatcher(String uri);

    /** Returns the URL of the specified URI, or null if not found.
	 */
    public URL getResource(String uri);

    /** Returns the resource of the specified URI as input stream,
	 * or null if not found.
	 */
    public InputStream getResourceAsStream(String uri);
}
