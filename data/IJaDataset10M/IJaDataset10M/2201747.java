package scouter.server.groups;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the group to the "group" parameter and redirects to the "redirect" parameter.
 * @author User
 */
public class SetGroup extends HttpServlet {

    private static final long serialVersionUID = 514759235790393007L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.getSession().setAttribute("group", GroupManager.getGroup(req.getParameter("group")));
            resp.sendRedirect(req.getParameter("redirect"));
        } catch (GroupNotFoundException e) {
            Logger.getLogger(SetGroup.class.getName()).log(Level.WARNING, "Could not find group '" + req.getParameter("group") + "' that user requested setting to.", e);
            resp.sendRedirect("choose_group.jsp?error=noexist&redirect=" + req.getParameter("redirect"));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
