package org.apache.jackrabbit.demo.blog.servlet;

import java.io.IOException;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jackrabbit.demo.blog.model.BlogManager;

/**
 * Controller class which handles deleting of blog entries
 *
 */
public class BlogRemoveControllerServlet extends ControllerServlet {

    /**
	 * Serial version id
	 */
    private static final long serialVersionUID = 4358955541231867065L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String UUID = request.getParameter("UUID");
        try {
            session = repository.login(new SimpleCredentials("username", "password".toCharArray()));
            String username = (String) request.getSession().getAttribute("username");
            if (username == null) {
                request.setAttribute("msgTitle", "Authentication Required");
                request.setAttribute("msgBody", "Only logged in users are allowed to delete blog entries.");
                request.setAttribute("urlText", "go back to login page");
                request.setAttribute("url", "/jackrabbit-jcr-demo/blog/index.jsp");
                RequestDispatcher requestDispatcher = this.getServletContext().getRequestDispatcher("/blog/userMessage.jsp");
                requestDispatcher.forward(request, response);
                return;
            }
            BlogManager.removeBlogEntry(UUID, username, session);
            request.setAttribute("msgTitle", "Delete");
            request.setAttribute("msgBody", "Blog entry was successfully deleted from your blog");
            request.setAttribute("urlText", "go back to blog page");
            request.setAttribute("url", "/jackrabbit-jcr-demo/blog/view");
            RequestDispatcher requestDispatcher = this.getServletContext().getRequestDispatcher("/blog/userMessage.jsp");
            requestDispatcher.forward(request, response);
        } catch (RepositoryException e) {
            throw new ServletException("Couldn't delete blog entry.Error occured while accessing the repository", e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }
}
