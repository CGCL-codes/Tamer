package com.sleepsocial.web;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.sleepsocial.authentication.AuthenticationManager;

@Controller
public class Home {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(HttpServletRequest req, HttpServletResponse response) throws IOException {
        if (AuthenticationManager.isUserAuthenticated(req)) {
            response.sendRedirect(Web.DASHBOARD_URL);
        }
        return "home";
    }

    @RequestMapping(value = "/")
    public String root(HttpServletRequest req, HttpServletResponse response) throws IOException {
        return home(req, response);
    }
}
