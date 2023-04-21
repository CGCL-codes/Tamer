package com.company.login.web;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMessage;
import org.architecture.common.web.Input;
import org.architecture.common.web.View;
import com.company.common.web.ProjectBaseActionBean;
import com.company.login.dao.LoginDAO;

/**
 * 
 * @author love
 *
 */
public class LoginBean extends ProjectBaseActionBean {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(LoginBean.class);

    private LoginDAO dao;

    public LoginBean() {
        this.dao = new LoginDAO();
    }

    /**
	 * 로그인을 실행한다.
	 * @param input
	 * @return
	 * @throws Exception
	 */
    public View login(Input input) throws Exception {
        String username = input.getString("username");
        String password = input.getString("password");
        log.info("LoginAction : username=" + username + ", password=" + password);
        boolean isLogin = dao.checkLogin(input);
        if (isLogin) {
            HttpSession session = super.getRequest().getSession();
            List roles = dao.getUserRoles(username);
            session.setAttribute("username", username);
            session.setAttribute("roles", roles);
            log.info("LoginAction : roles=" + roles);
            return fowardView(SUCCESS);
        } else {
            addError(GLOBAL_ERROR, new ActionMessage("login.title.failed"));
            return fowardView(FORM);
        }
    }

    /**
	 * 로그아웃을 실행한다.
	 * @param input
	 * @return
	 * @throws Exception
	 */
    public View logout(Input input) throws Exception {
        HttpSession session = getRequest().getSession();
        log.info("LogoutAction : username=" + session.getAttribute("username"));
        session.removeAttribute("username");
        return fowardView(SUCCESS);
    }
}
