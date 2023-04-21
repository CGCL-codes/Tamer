package ca.qc.adinfo.rouge.server.servlet;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ca.qc.adinfo.rouge.RougeServer;

public class IndexServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    public IndexServlet() {
        Properties props = RougeServer.getInstance().getProperties();
        this.username = props.getProperty("server.web.username").trim();
        this.password = props.getProperty("server.web.password").trim();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (request.getParameter("username") != null) {
            if (request.getParameter("username").equals(this.username) && request.getParameter("password").equals(this.password)) {
                session.setAttribute("auth", true);
            }
        }
        if (session.getAttribute("auth") == null) {
            response.sendRedirect(response.encodeRedirectURL("/"));
            return;
        }
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "dashboard";
        }
        RougePage page = RougeServer.getInstance().getPage(action);
        if (page != null) {
            page.doGet(request, response);
        }
    }
}
