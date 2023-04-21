package samples.servlet.xml.login;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.controller.servlet.ServletContext;

/**
 * <P>
 *
 * Session handling related to login. Simple impl with no roles etc: users are
 * either logged in or not. Simple! </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:48 $
 */
public class LoginManager {

    private static final String LOGGED_IN = "LoggedIn";

    private static final String REDIRECT = "Redirect";

    /**
     * Gets the logged in
     *
     * @return The loggedIn value
     */
    public static boolean isLoggedIn() {
        HttpServletRequest request = ((ServletContext) ViewContext.getViewContext()).getHttpRequest();
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(LOGGED_IN) != null) {
            return true;
        }
        return false;
    }

    /**
     * Gets the redirect
     *
     * @return The redirect value
     */
    public static HashMap getRedirect() {
        HttpServletRequest request = ((ServletContext) ViewContext.getViewContext()).getHttpRequest();
        HttpSession session = request.getSession(true);
        return (HashMap) session.getAttribute(REDIRECT);
    }

    /**
     * TODO: document the method
     */
    public static void logIn() {
        HttpServletRequest request = ((ServletContext) ViewContext.getViewContext()).getHttpRequest();
        HttpSession session = request.getSession(true);
        session.setAttribute(LOGGED_IN, new Object());
    }

    /**
     * TODO: document the method
     */
    public static void saveRedirect() {
        HttpServletRequest request = ((ServletContext) ViewContext.getViewContext()).getHttpRequest();
        HttpSession session = request.getSession(true);
        HashMap parameters = ((ServletContext) ViewContext.getViewContext()).getFormParameters();
        session.setAttribute(REDIRECT, parameters);
    }
}
