package se.vgregion.tycktill.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for testing the Tyck till system.
 */
public class TyckTillTestServlet extends HttpServlet {

    private static final long serialVersionUID = -1814042617856695712L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("throwExceptionInGet") != null) {
            throw new RuntimeException("An error occured in doGet!");
        }
    }
}
