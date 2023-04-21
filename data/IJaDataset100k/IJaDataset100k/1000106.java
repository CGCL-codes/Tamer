package pub.servlets.display;

import pub.servlets.*;
import pub.servlets.RequestHandler;
import pub.servlets.PubServlet;
import pub.db.PubConnection;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

/** Thin wrapper to make it compatible with the Display interface
 */
public class Locus implements RequestHandler {

    public void handleRequest(RequestHandlerState state) throws IOException, ServletException {
        handleRequest(state.getRequest(), state.getResponse(), state.getConn(), state.getParent(), state);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, PubConnection conn, PubServlet parentServlet, RequestHandlerState state) throws IOException, ServletException {
        parentServlet.includeWithClear("/DisplayLocus?locus_id=" + request.getAttribute("key"), request, response);
    }
}
