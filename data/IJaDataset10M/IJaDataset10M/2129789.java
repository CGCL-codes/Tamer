package ca.qc.adinfo.rouge.server.page;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ca.qc.adinfo.rouge.RougeServer;
import ca.qc.adinfo.rouge.server.core.SessionContext;
import ca.qc.adinfo.rouge.server.core.SessionManager;
import ca.qc.adinfo.rouge.server.servlet.RougeServerPage;

public class SessionsPage extends RougeServerPage {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionManager sessionManager = RougeServer.getInstance().getSessionManager();
        PrintWriter out = response.getWriter();
        this.drawHeader(out);
        this.startBox("Sessions", out);
        this.startList(out);
        for (SessionContext session : sessionManager.getSessions()) {
            if (session.getUser() == null) {
                out.println("<li><a href=\"/secure?action=session&id=" + session.getId() + "\">" + session.getId() + "</a>: </li>");
            } else {
                out.println("<li><a href=\"/secure?action=session&id=" + session.getId() + "\">" + session.getId() + "</a>: " + session.getUser().getUsername() + "</li>");
            }
        }
        this.endList(out);
        this.endBox(out);
        this.drawFooter(out);
    }
}
