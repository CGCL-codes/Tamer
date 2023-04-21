package org.torweg.pulse.webdav;

import java.io.IOException;
import java.net.URI;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.accesscontrol.User;
import org.torweg.pulse.service.AbstractPulseFilter;
import org.torweg.pulse.webdav.request.DAVMethod;
import org.torweg.pulse.webdav.request.Depth;
import org.torweg.pulse.webdav.request.HttpDAVServletRequest;
import org.torweg.pulse.webdav.request.PropFindRequest;
import org.torweg.pulse.webdav.response.HttpDAVServletResponse;
import org.torweg.pulse.webdav.response.PropFindResponse;
import org.torweg.pulse.webdav.util.BasicAuthentication;

/**
 * filters WebDAV related request (i.e. <tt>OPTIONS</tt>, <tt>PROPFIND</tt>) to
 * the webapp's root.
 * <p>
 * This filter is necessary for clients like Microsoft Web Folders that require
 * the WebDAV enabled resource to be in a base directory of the host and thus
 * want to perform WebDAV related requests on the webapp's base.
 * </p>
 * <p>
 * However, even when using the filter Windows Vista will fail to connect to
 * server using Basic authentication. It will not even display a login dialog.
 * Vista requires SSL / HTTPS connection to be used with Basic. However you
 * still can connect using Vista if you set the following registry key on a
 * client machine:<br/>
 * {@code
 * HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\WebClient\Parameters\
 * BasicAuthLevel} to <tt>2</tt>.
 * </p>
 * <p>
 * The BasicAuthLevel can be set to the following values:
 * </p>
 * <ul type="disc">
 * <li><tt>0</tt> - Basic authentication disabled</li>
 * <li>
 * <tt>1</tt> - Basic authentication enabled for SSL</li>
 * <li><tt>2</tt> - Basic authentication enabled for SSL shares and for non-SSL
 * shares</li>
 * </ul>
 * <p>
 * To actually connect to the web folder use one of the following connect
 * strings:
 * </p>
 * <ul type="disc">
 * <li>for non-SSL setups: {@code \\hostname\webapp-name\}</li>
 * <li>for SSL setups: {@code \\hostname@ssl\webapp-name\}</li>
 * </ul>
 * <p>
 * If you are still having problems to connect, you might need the
 * "Software Update for Web Folders (KB907306)", which can be downloaded from
 * Microsoft's website.
 * </p>
 * 
 * @author Thomas Weber
 * @version $Revision: 1822 $
 */
public final class WebDAVFilter extends AbstractPulseFilter {

    /**
	 * the logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebDAVFilter.class);

    /**
	 * the name of the WebDAV servlet.
	 */
    private String webDavServletName = "WEBDAV";

    /**
	 * initialises the filter with the context name and the name of the WebDAV
	 * servlet (optional <tt>init-param</tt> named <tt>webdav-servlet-name</tt>,
	 * defaults to <tt>WEBDAV</tt>).
	 * 
	 * @param conf
	 *            the filter configuration
	 * @throws ServletException
	 *             on errors extracting the name of the webapp
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
    public void init(final FilterConfig conf) throws ServletException {
        String srvName = conf.getInitParameter("webdav-servlet-name");
        if (srvName != null) {
            this.webDavServletName = srvName;
        }
    }

    /**
	 * actually does nothing.
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
    public void destroy() {
        return;
    }

    /**
	 * filters for WebDAV related request to the webapp's root.
	 * 
	 * @param servletRequest
	 *            the request
	 * @param servletResponse
	 *            the response
	 * @param chain
	 *            the filter chain
	 * @throws IOException
	 *             on errors
	 * @throws ServletException
	 *             on errors
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getServletPath();
        boolean continueChain = true;
        if ((path != null) && (path.equals("/index.jsp") || path.equals("/"))) {
            continueChain = applyFilter(request, response);
        }
        if (continueChain) {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
	 * does the actual work.
	 * 
	 * @param request
	 *            the current request
	 * @param response
	 *            the current response
	 * @return {@code true}, if the filter chain shall be processed further.
	 *         Otherwise {@code false}.
	 * @throws IOException
	 *             on errors aquiring an {@code InputStream} from the
	 *             {@code HttpServletRequest} while handling the PROPFIND
	 *             request.
	 */
    private boolean applyFilter(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        User user = null;
        if (!method.equals("GET") && !method.equals("POST")) {
            user = BasicAuthentication.authenticate(request, response);
            if (user == null) {
                LOGGER.debug("Authentication failed!");
                return false;
            }
        }
        response.addHeader("DAV", "1");
        response.addHeader("MS-AUTHOR-VIA", "DAV");
        if (method.equals(DAVMethod.OPTIONS.getMethodName())) {
            response.addHeader("Allow", "OPTIONS, TRACE, PROPFIND");
            return false;
        } else if (method.equals(DAVMethod.PROPFIND.getMethodName())) {
            handlePropFind(request, response, user);
            return false;
        }
        return true;
    }

    /**
	 * handles {@code PROPFIND} request to the webapp's root.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @param user
	 *            the current user
	 * @throws IOException
	 *             on errors
	 */
    private void handlePropFind(final HttpServletRequest request, final HttpServletResponse response, final User user) throws IOException {
        HttpDAVServletRequest davRequest = new HttpDAVServletRequest(request);
        PropFindRequest propFindRequest = new PropFindRequest(request.getInputStream(), null, Depth.ONE, null);
        try {
            String displayName = request.getContextPath();
            URI uri = new URI(request.getRequestURI());
            PropFindResponse propFindResponse;
            propFindResponse = new PropFindResponse(propFindRequest, user, new HttpDAVServletResponse(response));
            propFindResponse.add(PropFindResponse.buildDAVResponse(propFindRequest, displayName, uri));
            if (davRequest.getDepth() != Depth.ZERO) {
                propFindResponse.add(PropFindResponse.buildDAVResponse(propFindRequest, this.webDavServletName, new URI(new StringBuilder("/").append(request.getContextPath()).append('/').append(this.webDavServletName).append('/').toString())));
            }
            propFindResponse.closeResponse();
        } catch (Exception e) {
            LOGGER.error("Unexpected Error:", e);
        }
    }
}
