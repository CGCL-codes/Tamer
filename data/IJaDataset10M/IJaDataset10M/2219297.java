package org.marcont2.filters;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marcont2.usermanagement.User;

/**
 * Filters every request to verify that the user is logged in.  If the
 * user has not logged in and tries requests
 * a page that is not exempt from the filter, the server redirects to the
 * login page.
 * <br><br>Exempt pages are listed as a comma-delimted context parameter
 * in the web.xml under the key <b>FILTER_EXEMPTIONS</b>.
 * @author qkerby
 *
 */
public class LoginFilter implements Filter {

    protected final Log log = LogFactory.getLog(this.getClass());

    private FilterConfig config = null;

    private ServletContext context = null;

    private RequestDispatcher dispatcherLogin;

    private RequestDispatcher dispatcherRegister;

    private static Map exemptions = null;

    private static int contextPathLength = 0;

    private static final String LOGIN_PAGE = "/MarcOnt";

    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        dispatcherLogin = config.getServletContext().getRequestDispatcher(LOGIN_PAGE);
        context = config.getServletContext();
        StringTokenizer tok = new StringTokenizer(context.getInitParameter("FILTER_EXEMPTIONS"), ",");
        exemptions = new HashMap();
        while (tok.hasMoreTokens()) {
            String exempt = (String) tok.nextElement();
            exemptions.put(exempt, null);
        }
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        User lf = (User) request.getSession(true).getAttribute("User");
        if (contextPathLength == 0) {
            contextPathLength = request.getContextPath().length();
        }
        if (lf != null) if (!lf.isLoggedIn()) response.sendRedirect(request.getContextPath() + "/MarcOntServlet"); else chain.doFilter(req, res); else response.sendRedirect(request.getContextPath() + "/MarcOntServlet");
    }

    public void destroy() {
        exemptions.clear();
    }
}
