package org.ibit.avanthotel.admin.web.servlet;

import org.ibit.avanthotel.persistence.logic.util.Calendari;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @web.servlet
 *    name="reserves/CarregaEstadistica"
 *    description="Calcula les comisisons de la asociacio hotelera"
 *
 * @web.servlet-mapping
 *    url-pattern="/avanthotel/estadistica/Estadistica"
 *
 */
public class CarregaEstadistica extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/avanthotel/estadistica/index.jsp";
        Calendari calendari = new Calendari();
        req.setAttribute("anyActual", new Integer(calendari.getAny() + 1));
        req.setAttribute("opcio", new Integer(7));
        RequestDispatcher rd = getServletContext().getRequestDispatcher(resp.encodeURL(url));
        rd.forward(req, resp);
    }
}
