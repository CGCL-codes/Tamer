package com.coltrane.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.coltrane.domain.UserSession;
import com.coltrane.service.LoginService;

@Controller
@RequestMapping("logout.htm")
public class LogoutController {

    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String doLogOut() {
        UserSession userSession = loginService.getUserSession();
        userSession.setSignedIn(false);
        return "redirect:login.htm";
    }
}
