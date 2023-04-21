package pl.edu.agh.ssm.web.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import pl.edu.agh.ssm.persistence.IUser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Spring Controller that just called GWT's <code>RemoteServiceServlet.doPost(request, response)</code>.
 * 
 * @author David Winterfeldt
 */
public abstract class GwtController extends RemoteServiceServlet implements Controller, ServletContextAware {

    private ServletContext servletContext;

    /**
     * Gets <code>ServletContext</code>.
     */
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Implementation of <code>ServletContextAware</code>.
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Handles request and delegates to GWT's <code>RemoteServiceServlet.doPost(request, response)</code>.
     */
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
        {
            try {
                ServletUtils.setRequest(request);
                ServletUtils.setResponse(response);
                doPost(request, response);
            } finally {
                ServletUtils.setRequest(null);
                ServletUtils.setResponse(null);
            }
            return null;
        }
    }

    public IUser getUserInSession() {
        return (IUser) getSession().getAttribute("logged");
    }

    public boolean isLoggedUserAdmin() {
        return ((getUserInSession().getRole() & IUser.ADMIN) != 0);
    }

    protected void setUserInSession(IUser user) {
        getSession().setAttribute("logged", user);
    }

    protected HttpSession getSession() {
        return this.getThreadLocalRequest().getSession();
    }

    public String getLoggedUserName() {
        if (getUserInSession() == null) return null; else return getUserInSession().getLogin();
    }
}
