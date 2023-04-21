package pub.servlets.display;

import pub.servlets.*;
import java.io.*;
import pub.db.PubConnection;
import javax.servlet.*;
import javax.servlet.http.*;

public class AllAnnotationTasks implements RequestHandler {

    public void handleRequest(RequestHandlerState state) throws IOException, ServletException {
        handleRequest(state.getRequest(), state.getResponse(), state.getConn(), state.getParent(), state);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, PubConnection conn, PubServlet parentServlet, RequestHandlerState state) throws IOException, ServletException {
        parentServlet.include("/jsp/display/AllAnnotationTasks.jsp", request, response);
    }
}
