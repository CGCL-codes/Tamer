package br.ufpe.cin.stp.mass.view.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.ufpe.cin.stp.mass.model.MassFacade;
import br.ufpe.cin.stp.mass.model.session.Session;
import br.ufpe.cin.stp.mass.persistence.PersistentObjectNotFoundException;
import br.ufpe.cin.stp.mass.xmlpull.ExistingSessionsXMLPull;
import br.ufpe.cin.stp.mass.xmlpull.ResponseXMLPull;

/**
 * @created 15/08/2004 13:12:06
 * @author Marcello Sales Jr. <a href='masj2@cin.ufpe.br'>masj2@cin.ufpe.br</a>
 * @version 1.0
 */
public class ExistingSessionsServlet extends HttpServlet {

    /**
     * Respond to a GET request for the content produced by
     * this servlet.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are producing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/xml");
        try {
            Session[] sessions = MassFacade.getInstance().getSessions();
            response.getWriter().println(ExistingSessionsXMLPull.getInstance().pullExistingSessions(sessions).toString());
        } catch (PersistentObjectNotFoundException e) {
            response.getWriter().println(ResponseXMLPull.getInstance().pullResponse(false, e.getMessage()).toString());
        }
    }
}
