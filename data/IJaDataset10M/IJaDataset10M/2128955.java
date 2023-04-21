package org.apache.jackrabbit.demo.mu.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.jcr.RepositoryException;
import java.io.IOException;

/**
 * TODO write comment
 *
 * @author Pavel Konnikov
 * @version $Revision$ $Date$
 */
public class DumpServlet extends MuServlet {

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            loginToRepository();
            session.exportSystemView("/mu-root", httpServletResponse.getOutputStream(), true, false);
        } catch (RepositoryException e) {
            e.printStackTrace();
        } finally {
            session.logout();
        }
    }
}
