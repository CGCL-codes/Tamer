package projectatlast.frontend;

import projectatlast.student.AuthController;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Requires that the current user is not logged in.
 */
public class LogoutRequiredFilter extends FilterBase {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (shouldFilter(req)) {
            if (AuthController.isLoggedIn()) {
                resp.sendRedirect("/home");
                return;
            }
        }
        chain.doFilter(req, resp);
    }
}
