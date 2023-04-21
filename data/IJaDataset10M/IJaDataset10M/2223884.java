package br.net.woodstock.rockframework.web.struts2.security;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;

public class JAASLogonValidator implements LogonValidator {

    @Override
    public boolean isLogged(final HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            return true;
        }
        return false;
    }
}
