package org.uefl.ldIntegration;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The LDController servlet implements the controller part of the model view
 * controller architecture of the web publication aplication.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.1 $, $Date: 2008/03/16 07:58:21 $
 */
public class LDCont extends HttpServlet {

    private static final long serialVersionUID = 42L;

    private static final String CONTENT_TYPE = "text/html; charset=\"UTF-8\"";

    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        int reqId = 0;
        try {
            reqId = Integer.parseInt(request.getParameter("requestId"));
        } catch (NumberFormatException e) {
            reqId = 0;
        }
        switch(reqId) {
            case 0:
                {
                    RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/publication.html");
                    requestDispatcher.forward(request, response);
                    break;
                }
            case 1000:
                {
                    RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/LdProcess");
                    requestDispatcher.forward(request, response);
                    break;
                }
            case 1001:
                {
                    RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/progressstatus");
                    requestDispatcher.forward(request, response);
                    break;
                }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
    }
}
