package it.univaq.di.chameleonserver.servlet;

import it.univaq.di.chameleonserver.utility.Utility;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 *
 * <p>Title: ListApplication </p>
 *
 * <p>
 * Description: Questa Servlet una volta ricevuta una chiamata restitusce
 * al client richiedente la stringa corrispondente al file applications.xml
 * Il file applications continene la lista delle applicazioni disponibili nel
 * server.
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Gu
 * @version 1.0
 */
public class ListApplication extends HttpServlet {

    /** CONTENT_TYPE */
    private static final String CONTENT_TYPE = "text/xml";

    /** riferimento alla classe Utility */
    private Utility utility = new Utility();

    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ok");
        response.setContentType(CONTENT_TYPE);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Applications/applications.xml");
        dispatcher.include(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
    }
}
