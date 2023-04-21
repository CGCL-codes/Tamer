package com.i3sp.www.authserver.handlers;

import org.mortbay.util.Code;
import com.i3sp.www.authserver.AuthHandler;
import com.i3sp.www.authserver.AuthHandlerException;
import com.i3sp.www.authserver.AuthHandlerRequest;
import com.i3sp.www.authserver.AuthHandlerResponse;
import java.util.HashMap;
import java.io.IOException;

/** Redirect the user to the url given in the redirect param */
public class Redirect implements AuthHandler {

    private String redirect;

    public void init(HashMap params) throws AuthHandlerException {
        this.redirect = (String) params.get("redirect");
        if (redirect == null) throw new AuthHandlerException("redirect param not set!");
    }

    public boolean check(AuthHandlerRequest request, AuthHandlerResponse response) throws AuthHandlerException, IOException {
        response.getHttpServletResponse().sendRedirect(redirect);
        return false;
    }

    public void destroy() {
    }
}
