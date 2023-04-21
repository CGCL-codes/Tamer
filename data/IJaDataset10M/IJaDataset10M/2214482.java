package com.foo.datasource;

import org.apache.wicket.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: Altug Bilgin ALTINTAS
 */
public class MySecurityContextLogoutHandler extends SecurityContextLogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("log out");
        HttpSession session = request.getSession();
        session.invalidate();
        super.logout(request, response, authentication);
    }
}
