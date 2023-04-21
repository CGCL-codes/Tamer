package org.localstorm.mcc.web.people.filter.security;

import javax.servlet.http.HttpServletResponse;
import org.localstorm.mcc.web.filter.SecurityCheckFilter;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.localstorm.mcc.ejb.users.User;

/**
 *
 * @author Alexey Kuznetsov
 */
public class PeoplePersonSecurityCheckFilter extends SecurityCheckFilter {

    public static final String PERSON_ID_PARAM = "personId";

    @Override
    @SuppressWarnings("unchecked")
    public void doFilter(HttpServletRequest req, HttpServletResponse res, User user) throws IOException, ServletException {
        String pid = req.getParameter(PERSON_ID_PARAM);
        if (pid != null) {
            Integer personId = Integer.parseInt(pid);
            SecurityUtil.checkPersonSecurity(req.getSession(true), personId, user, log);
        }
    }
}
