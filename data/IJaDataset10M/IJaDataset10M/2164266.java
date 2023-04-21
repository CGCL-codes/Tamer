package etp.server.login;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import com.google.appengine.api.users.*;

public class FilterTeste implements javax.servlet.Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user != null) {
            System.out.println(user.getAuthDomain());
            resp.getWriter().println(user.toString());
            resp.getOutputStream().println("<a href=\"http://localhost:8080" + userService.createLogoutURL("http://localhost:8080/teste") + "\">logout</a>");
        } else {
            ((HttpServletResponse) resp).sendRedirect(userService.createLoginURL("http://localhost:8080/teste"));
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
