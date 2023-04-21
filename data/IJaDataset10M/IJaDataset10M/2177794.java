package org.localstorm.mcc.web.filter;

import java.io.IOException;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.localstorm.mcc.ejb.users.User;
import org.localstorm.mcc.web.CommonSessionKeys;
import org.localstorm.mcc.web.ReturnPageBean;
import org.localstorm.mcc.web.Views;

/**
 *
 * @author Alexey Kuznetsov
 */
public class AuthFilter extends SecurityCheckFilter {

    public AuthFilter() {
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, User user) throws IOException, ServletException {
        if (user == null) {
            RequestDispatcher disp = req.getRequestDispatcher(Views.LOGIN);
            if (req.getMethod().equalsIgnoreCase("get")) {
                ReturnPageBean rpb = this.createReturnPageBean(req, res);
                req.getSession().setAttribute(CommonSessionKeys.ORIGINAL_REQUEST, rpb);
            }
            disp.forward(req, res);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    protected ReturnPageBean createReturnPageBean(HttpServletRequest req, HttpServletResponse res) {
        return new ReturnPageBean(req);
    }
}
