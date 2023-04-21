package pub.servlets.display;

import pub.db.PubConnection;
import pub.servlets.*;
import pub.utils.ListUtils;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Germplasm implements RequestHandler {

    public void handleRequest(RequestHandlerState state) throws IOException, ServletException {
        handleRequest(state.getRequest(), state.getResponse(), state.getConn(), state.getParent(), state);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, PubConnection conn, PubServlet parentServlet, RequestHandlerState state) throws IOException, ServletException {
        parentServlet.include("/jsp/display/DisplayGermplasm.jsp", request, response);
    }
}
