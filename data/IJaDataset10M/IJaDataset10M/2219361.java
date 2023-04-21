package frontend;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logs.LogFactory;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class Logout
 */
public class Logout extends HttpServlet {

    private static final Logger logger = LogFactory.getLog(Logout.class);

    private static final long serialVersionUID = 1L;

    /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        logger.debug("Logout");
        request.getSession().invalidate();
        logger.debug("logged out");
        response.sendRedirect(request.getContextPath() + "/");
    }
}
