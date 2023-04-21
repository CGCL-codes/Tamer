package booksandfilms.server.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public abstract class LoginSuperServlet extends HttpServlet {

    private static final long serialVersionUID = -2753523174100327353L;

    public LoginSuperServlet() {
        super();
    }

    protected String buildCallBackURL(HttpServletRequest request, Integer provider) {
        StringBuffer requestURL = request.getRequestURL();
        String callbackURL = requestURL.toString();
        callbackURL += "callback";
        return callbackURL;
    }
}
