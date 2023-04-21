package simpleorm.simpleweb.context;

import simpleorm.simpleweb.core.WPage;
import simpleorm.simpleweb.core.WException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Non-JSP servelet.
 */
public class WPageContextServlet extends WPageContext {

    private HttpServletRequest request;

    private HttpServletResponse response;

    /**
      * Redirects to a new page, typically after a successful CRUD form.
      */
    @Override
    public void redirect(String absoluteUrl) {
        try {
            String contexted = absoluteUrl;
            String encoded = getResponse().encodeRedirectURL(contexted);
            getResponse().sendRedirect(encoded);
        } catch (Exception ex) {
            throw new WException(ex);
        }
    }

    /** Currently not used, but should be used to make submitted crud go back to the correct place. */
    public String getRefererUrl() {
        return getRequest().getHeader("referer");
    }

    public WPageContextServlet(WPage page, HttpServletRequest request, HttpServletResponse response) {
        super(page);
        this.request = request;
        this.response = response;
    }

    public String getParameter(String key) {
        return getRequest().getParameter(key);
    }

    @Override
    String getContextPath() {
        return getRequest().getContextPath();
    }

    /** Add context and session id to urls iff appropriate. */
    @Override
    public String encodeContextUrl(String url) {
        return getResponse().encodeRedirectURL(addContextToUrl(url));
    }

    @Override
    String getCharacterEncoding() {
        return getResponse().getCharacterEncoding();
    }

    /** Ie. the username of the current logged in user.  */
    @Override
    public String getRemoteUser() {
        return getRequest().getRemoteUser();
    }

    @Override
    public boolean isUserInRole(String role) {
        return getRequest().isUserInRole(role);
    }

    private PrintWriter getOut() throws Exception {
        return response.getWriter();
    }

    @Override
    public void println(String msg) {
        try {
            getOut().print(msg);
            getOut().println("<BR>");
        } catch (Exception ex) {
            throw new WException(ex);
        }
    }

    /**
     * Keep this private, we do not want webserver data leaking out
     * into the rest of the web bean.
     * Overriden by JSP.
     */
    private HttpServletRequest getRequest() {
        return request;
    }

    private HttpServletResponse getResponse() {
        return response;
    }
}
