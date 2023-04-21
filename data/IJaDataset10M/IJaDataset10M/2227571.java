package lokahi.core.gui.www.struts;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import lokahi.core.common.exception.AuthenticationException;
import lokahi.core.common.authentication.AuthenticationFactory;
import lokahi.core.api.user.User;
import lokahi.core.common.interfaces.TMCConstants;
import lokahi.util.PropertiesFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * @author Stephen Toback
 * @version $Id: Authorization.java,v 1.1 2006/03/07 20:18:51 drtobes Exp $
 */
public final class Authorization extends Action implements TMCConstants {

    static final Logger logger = Logger.getLogger(Authorization.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting AUTH SERVLET ");
        }
        ActionMessages messages = new ActionMessages();
        ActionForward actionForward;
        boolean wap = false;
        messages.clear();
        String url = "";
        DynaActionForm f = (DynaActionForm) form;
        String logout = (String) f.get(LOGOUT_PARAM);
        if (f.get("wap") != null && "true".equalsIgnoreCase((String) f.get("wap"))) {
            wap = true;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("logout=" + logout);
        }
        if ("".equals(logout)) {
            if (request.getSession().getAttribute(SESSION_LOGGED_IN_OBJ_PARAM) == null) {
                String userName = (String) f.get("userName");
                String password = (String) f.get("passWord");
                url = (String) f.get("redirectUrl");
                if (logger.isDebugEnabled()) {
                    logger.debug("f.get(\"wap\")=" + f.get("wap"));
                }
                boolean authenticated = false;
                try {
                    authenticated = AuthenticationFactory.authenticate(userName, password);
                } catch (AuthenticationException e) {
                    if (logger.isInfoEnabled()) {
                        logger.info("Exception: " + e.getMessage());
                    }
                    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("login.exception", e.getMessage()));
                }
                if (authenticated) {
                    User u = null;
                    try {
                        u = User.getUser(userName);
                    } catch (SQLException e) {
                        if (logger.isInfoEnabled()) {
                            logger.info("Exception: " + e.getMessage());
                        }
                    }
                    HttpSession session = request.getSession();
                    if (u != null && u.isActive()) {
                        session.setAttribute("User", u);
                    } else {
                        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("login.error.account.inactive", new Object[] { userName }));
                    }
                } else {
                    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("login.error", new Object[] { userName }));
                }
            } else {
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.loggedin"));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("wap=" + wap);
            }
            if (messages.isEmpty()) {
                if (wap) {
                    actionForward = mapping.findForward("wap.success");
                } else {
                    if ("".equals(url)) {
                        actionForward = mapping.findForward("success");
                    } else {
                        url = url.replaceAll("'", "");
                        url = url.replaceAll(PropertiesFile.getConstantValue("application.root"), "");
                        url = url.replaceAll("//", "/");
                        actionForward = new ActionForward(url, true);
                    }
                }
            } else {
                if (wap) {
                    actionForward = mapping.findForward("wap.failure");
                } else {
                    actionForward = mapping.findForward("failure");
                }
            }
            saveMessages(request, messages);
            if (logger.isDebugEnabled()) {
                logger.debug("messages=" + messages);
            }
        } else {
            logout(request.getSession());
            if (wap) {
                actionForward = mapping.findForward("wap.failure");
            } else {
                actionForward = mapping.findForward(FORWARD_LOGGED_OUT);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Ending AUTH SERVLET");
        }
        return actionForward;
    }

    public void logout(HttpSession session) {
        if (session.getAttribute("User") != null) {
            session.removeAttribute("User");
        }
    }
}
