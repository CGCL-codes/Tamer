package pub.servlets.add;

import pub.db.*;
import pub.servlets.*;
import pub.utils.Log;
import pub.utils.StringUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProtocol implements RequestHandler {

    private PubServlet parentServlet;

    private pub.db.PubConnection conn;

    public void handleRequest(RequestHandlerState state) throws IOException, ServletException {
        handleRequest(state.getRequest(), state.getResponse(), state.getConn(), state.getParent(), state);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, pub.db.PubConnection conn, PubServlet parentServlet, RequestHandlerState state) throws IOException, ServletException {
        this.parentServlet = parentServlet;
        this.conn = conn;
        String action = null;
        action = request.getParameter("add.protocol");
        FormErrors errors = new FormErrors();
        if (action == null) {
            redirectToEmptyForm(request, response);
        } else {
            validate(errors, request);
            if (errors.size() > 0) {
                request.setAttribute("pub.servlets.errors", errors);
                redirectToErrorPage(request, response);
            } else {
                runAddCommand(request);
                redirectToResultsPage(request, response);
            }
        }
    }

    public void validate(FormErrors errors, HttpServletRequest request) {
    }

    private void redirectToEmptyForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        parentServlet.include("/jsp/addnew/AddProtocol.jsp", request, response);
    }

    private void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        parentServlet.include("/jsp/form_error.jsp", request, response);
    }

    private void runAddCommand(HttpServletRequest request) throws IOException, ServletException {
        ProtocolTable table = new pub.db.ProtocolTable(conn);
        HashMap column_values = new HashMap();
        String user_id = Login.getUserId(request);
        String[] params = new String[] { "title", "description", "authors", "temp_protocol_source" };
        for (int i = 0; i < params.length; i++) {
            if ((request.getParameter(params[i]) != null) && (request.getParameter(params[i]).trim().length() > 0)) {
                column_values.put(params[i], (request.getParameter(params[i])).trim());
            }
        }
        column_values.put("entered_by", user_id);
        column_values.put("updated_by", user_id);
        String new_id = table.addEntry(column_values);
        if ((new_id != null) && (new_id.length() > 0)) {
            request.setAttribute("inserted_id", new_id);
        }
    }

    private void redirectToResultsPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        parentServlet.forward("/Display?dispatch=protocol&key=" + request.getAttribute("inserted_id"), request, response);
    }
}
