package de.banh.bibo.servlet;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends ControllerServlet {

    private static final long serialVersionUID = 8657038306479991208L;

    private static Logger logger = Logger.getLogger(LogoutServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        loadMessageBeanFromSesssion(req);
        String benutzer = getBenutzer(req).toString();
        if (benutzer == null) benutzer = "(unbekannt)";
        logger.info("Benutzer \"" + getBenutzer(req).toString() + "\" abgemeldet.");
        HttpSession session = req.getSession();
        session.removeAttribute(SESSION_KEY_BIBOMGR);
        session.removeAttribute(SESSION_KEY_BENUTZER);
        MessageBean msg = createSuccessMessageBean(req);
        msg.addDetail("Sie wurden erfolgreich abgemeldet.");
        RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
        rd.forward(req, res);
    }
}
