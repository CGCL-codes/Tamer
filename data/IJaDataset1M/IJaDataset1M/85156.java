package frontend;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logs.LogFactory;
import org.apache.log4j.Logger;
import backend.db.ORM;

/**
 * Servlet implementation class RemoveStudent
 */
public class RemoveStudent extends HttpServlet {

    private static final Logger logger = LogFactory.getLog(RemoveStudent.class);

    private static final long serialVersionUID = 1L;

    /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        logger.debug("RemoveStudent");
        final String username = request.getParameter("username");
        ORM.removeStudent(username);
        logger.debug("student " + username + " removed");
    }
}
