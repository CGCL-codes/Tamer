package com.nyandu.weboffice.site.action;

import com.nyandu.weboffice.common.business.BusinessSession;
import com.nyandu.weboffice.common.business.SessionTimeOutNotifier;
import com.nyandu.weboffice.site.business.User;
import com.nyandu.weboffice.common.util.Consts;
import com.nyandu.weboffice.site.database.IUserDAO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * User: tom
 * Date: 25/01/2005
 * Time: 05:39:16 PM
 */
public class LoginAction extends Action {

    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        String target = "login";
        BusinessSession bSession = (BusinessSession) session.getAttribute(Consts.BUSINESS_SESSION);
        try {
            if (bSession == null) {
                throw new Exception("Business Session cannot be null");
            } else {
                req.setAttribute("conf", bSession.getConfig());
                if (bSession.getUser() != null) return map.findForward("alreadyLogged"); else {
                    String username = req.getUserPrincipal().getName();
                    IUserDAO userDAO = bSession.getUserDAO();
                    User user = userDAO.selectUser(username);
                    bSession.setUser(user);
                    bSession.setServerName(req.getServerName());
                    bSession.setContextPath(req.getContextPath());
                    bSession.setPort(req.getServerPort());
                    session.setAttribute("SessionNotifierObj", new SessionTimeOutNotifier(session.getId(), bSession, System.currentTimeMillis()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map.findForward(target);
    }
}
