package pub.servlets.summary;

import pub.beans.*;
import pub.servlets.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** Displays all of the annotations associated with a particular term.
 */
public class Term_Annotations implements RequestHandler {

    public void handleRequest(RequestHandlerState state) throws IOException, ServletException {
        handleRequest(state.getRequest(), state.getResponse(), state.getConn(), state.getParent(), state);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse res, pub.db.PubConnection conn, PubServlet parentServlet, RequestHandlerState state) throws IOException, ServletException {
        TermBean termBean = pub.beans.BeanFactory.getTermBean(conn, "" + req.getAttribute("key"));
        req.setAttribute("termBean", termBean);
        parentServlet.include("/jsp/summary/Term_Annotations.jsp", req, res);
    }
}
