package net.spatula.tally_ho.ui.signin;

import javax.servlet.http.HttpSession;
import net.spatula.tally_ho.service.beans.UserBean;
import wicket.protocol.http.WebRequest;
import wicket.protocol.http.WebRequestCycle;

/**
 * @author spatula
 *
 */
public class Utils {

    private static final String USER_BEAN_ATTR = "userBean";

    public static final UserBean getUserBean() {
        HttpSession session = getSession();
        Object userBean = session.getAttribute(USER_BEAN_ATTR);
        if (userBean == null) {
            return null;
        }
        if (!(userBean instanceof UserBean)) {
            setUserBean(null);
            return null;
        }
        return (UserBean) userBean;
    }

    private static HttpSession getSession() {
        HttpSession session = ((WebRequest) (WebRequestCycle.get().getRequest())).getHttpServletRequest().getSession();
        return session;
    }

    static final void setUserBean(UserBean userBean) {
        HttpSession session = getSession();
        session.setAttribute(USER_BEAN_ATTR, userBean);
    }
}
