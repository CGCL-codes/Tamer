package com.ikarkharkov.dictour.ui;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ikarkharkov.dictour.db.DBService;
import com.ikarkharkov.dictour.data.User;

/**  Allows user to login
 *   Used by login.jsp
 */
public class LoginAction extends Action {

    static Logger log = Logger.getLogger("com.ikarkharkov.dictour.ui.PhraseListAction");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Form f = (Form) form;
        String login = f.getLogin();
        String password = f.getPassword();
        if ((login == null) || (password == null)) {
            return mapping.findForward("fail");
        }
        if (password.length() < 3) {
            return mapping.findForward("fail");
        }
        User user = (User) DBService.getSession().createCriteria(User.class).add(Restrictions.and(Restrictions.eq("login", login), Restrictions.eq("password", password))).uniqueResult();
        if (user != null) {
            if (user.getRole().getName().equals("unregistered")) {
                request.setAttribute("Error", "Email confirmation missing");
                return mapping.findForward("fail");
            } else {
                request.getSession().setAttribute("user", user);
            }
            return mapping.findForward("success");
        }
        request.setAttribute("Error", "Wrong Login/Password");
        return mapping.findForward("fail");
    }
}
