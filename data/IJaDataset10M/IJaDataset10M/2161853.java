package icreate.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import icreate.db.*;
import icreate.security.*;
import icreate.comps.User;

public class ValidateUserFilter implements Filter {

    private FilterConfig config;

    private UserManager um;

    private HttpSession session;

    public void init(FilterConfig config) {
        this.config = config;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletContext context = config.getServletContext();
        HttpServletRequest h_request = (HttpServletRequest) request;
        HttpServletResponse h_response = (HttpServletResponse) response;
        session = h_request.getSession(true);
        if (!context.getAttribute("icreate.code").equals("1")) {
            try {
                validate(h_request, h_response);
            } catch (UserException icu) {
                session.removeAttribute("CurrentUserBean");
                throw new LoginException();
            }
        } else {
            User demo = new User();
            demo.setId(1);
            demo.setTitle("demo");
            session.setAttribute("CurrentUserBean", demo);
        }
        chain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request, HttpServletResponse response) throws UserException {
        User sess_User = (User) session.getAttribute("CurrentUserBean");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean no_params = (username == null || password == null);
        boolean b = false;
        try {
            b = sess_User.getId() == 0;
        } catch (NullPointerException npe) {
            b = true;
        }
        if (b) {
            um = (UserManager) session.getAttribute("icreate.validate");
            if (um == null) {
                if (!no_params) {
                    Database d = getDb();
                    try {
                        d.openConnection();
                    } catch (dbException de) {
                        throw new UserException("Unable to validate User on system:  Db connection", UserException.USER);
                    }
                    um = new UserManager(d);
                    session.setAttribute("icreate.validate", um);
                    User u = um.checkUserNamePassword(username, password);
                    session.removeAttribute("icreate.validate");
                    session.setAttribute("CurrentUserBean", u);
                    try {
                        d.closeConnection();
                    } catch (dbException de_1) {
                        ;
                    }
                    return;
                } else {
                    throw new UserException("No parameters supplied", UserException.USER);
                }
            } else {
                if (!no_params) {
                    Database d = getDb();
                    try {
                        d.openConnection();
                    } catch (dbException de) {
                        throw new UserException("Unable to validate User on system:  Db connection", UserException.USER);
                    }
                    User u = um.checkUserNamePassword(username, password);
                    session.setAttribute("CurrentUserBean", u);
                    try {
                        d.closeConnection();
                    } catch (dbException de_1) {
                        ;
                    }
                    return;
                } else {
                    throw new UserException("No parameters supplied", UserException.USER);
                }
            }
        } else {
            return;
        }
    }

    public Database getDb() {
        ServletContext context = config.getServletContext();
        return (Database) context.getAttribute("icreate.db");
    }

    public void destroy() {
    }
}
