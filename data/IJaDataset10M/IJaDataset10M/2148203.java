package com.odo.spring.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.odo.spring.services.LoginManager;

/**
 * @author Sreejith VS <sreejith@odo.co.in>
 *
 */
@Controller
public class LoginController {

    @Resource(name = "loginManager")
    LoginManager loginManager;

    /**
	 * Constructor
	 */
    public LoginController() {
    }

    /**
	 * @param req
	 * @return
	 */
    @RequestMapping("auth.htm")
    public String handleLogin(HttpServletRequest req) {
        if (req.getMethod().equals("GET")) {
            return "login";
        }
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        if (this.loginManager.authenticateUser(userName, password)) {
            return "userhome";
        }
        req.setAttribute("message", "Authentication Failure !");
        return "login";
    }

    @RequestMapping("logout.htm")
    public String handleLogout(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session != null) session.invalidate();
        req.setAttribute("message", "You have been loggged out successfully !");
        return "logout";
    }
}
