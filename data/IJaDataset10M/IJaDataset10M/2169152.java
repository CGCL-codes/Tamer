package org.nodevision.portal.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nodevision.portal.repositories.RepositoryBasic;

public final class AuthenticateFailure extends HttpServlet {

    public final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String redirect = RepositoryBasic.getConfig().getSecurity().getRedirectFailure();
        if (-1 < redirect.indexOf(":")) {
            response.sendRedirect(response.encodeRedirectURL(redirect));
        } else {
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + redirect));
        }
    }
}
