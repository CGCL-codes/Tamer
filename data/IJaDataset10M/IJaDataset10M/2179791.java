package scouter.server.configure;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import scouter.server.ServletUtils;

/**
 * Clears memcache when posted to.
 * @author User
 */
public class ClearCache extends HttpServlet {

    private static final long serialVersionUID = 3198985400154663201L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.m.clearAll();
        resp.sendRedirect("/configure.jsp");
    }
}
