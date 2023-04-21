package confreg.ti.war.fragments;

import com.sun.rave.web.ui.appbase.AbstractFragmentBean;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Menu;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.model.DefaultOptionsList;
import javax.faces.FacesException;
import confreg.ti.war.ApplicationBean1;
import confreg.ti.war.SessionBean1;
import confreg.ti.war.RequestBean1;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Fragment bean that corresponds to a similarly named JSP page
 * fragment.  This class contains component definitions (and initialization
 * code) for all components that you have defined on this fragment, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Administrator
 */
public class header extends AbstractFragmentBean {

    /**
     * <p>Automatically managed component initialization. <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    public header() {
    }

    /**
     * <p>Callback method that is called whenever a page containing
     * this page fragment is navigated to, either directly via a URL,
     * or indirectly via page navigation.  Override this method to acquire
     * resources that will be needed for event handlers and lifecycle methods.</p>
     * 
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("Page1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called.  Override this
     * method to release resources acquired in the <code>init()</code>
     * resources that will be needed for event handlers and lifecycle methods.</p>
     * 
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    private static HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    /**
     * 
     * @return With the logged in user's email (userid).
     */
    public String getUserEmail() {
        HttpServletRequest hs;
        hs = getServletRequest();
        try {
            return hs.getUserPrincipal().getName();
        } catch (NullPointerException ne) {
            return "Not logged in!";
        }
    }

    public boolean getUserLoginFalse() {
        String login = getUserEmail();
        if (login.equals("Not logged in!")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getUserLoginTrue() {
        String login = getUserEmail();
        if (login.equals("Not logged in!")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * logout from the session
     */
    public String logout() {
        getServletRequest().getSession().invalidate();
        return "logout";
    }
}
