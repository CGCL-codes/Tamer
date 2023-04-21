package org.ibit.avanthotel.offer.web.servlet;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Category;
import org.ibit.avanthotel.offer.web.delegate.ReservesOfferDelegate;
import org.ibit.avanthotel.offer.web.util.ServletUtil;
import org.ibit.avanthotel.persistence.logic.util.Calendari;
import org.ibit.avanthotel.persistence.logic.util.DelegateSysException;

/**
 * @created 19 / novembre / 2003
 * @web.servlet name="reserves/informes/CarregaReservaDate"
 *    description="Treu les reserves d'una data concreta"
 * @web.servlet-mapping url-pattern="/avanthotel/reserves/informes/CarregaReservaDate"
 */
public class CarregaReservaDate extends HttpServlet {

    /** */
    static Category logger = Category.getInstance(CarregaReservaDate.class.getName());

    /** */
    ReservesOfferDelegate reservesOfferDelegate;

    /**
    * @exception ServletException
    */
    public void init() throws ServletException {
        try {
            reservesOfferDelegate = ReservesOfferDelegate.getInstance();
        } catch (DelegateSysException e) {
            throw new ServletException(e);
        }
    }

    /**
    * @param req
    * @param resp
    * @exception ServletException
    * @exception IOException
    */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
    * @param req
    * @param resp
    * @exception ServletException
    * @exception IOException
    */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = ServletUtil.getAllotjamentSessio(req);
        String url = "/avanthotel/reserves/informes/reportItem.jsp";
        int dia = new Integer(req.getParameter("dia")).intValue();
        int mes = new Integer(req.getParameter("mes")).intValue();
        int any = new Integer(req.getParameter("any")).intValue();
        Collection reservesCol = null;
        Calendari calendari = new Calendari();
        calendari.setDia(dia);
        calendari.setMes(mes);
        calendari.setAny(any);
        calendari.clearHourTime();
        try {
            reservesCol = reservesOfferDelegate.loadItemsReserva(id, new Calendari(calendari));
        } catch (DelegateSysException e) {
            url = "/avanthotel/basica/error.jsp?missatge=" + e.getMessage();
        }
        req.setAttribute("reservesCol", reservesCol);
        req.setAttribute("hiHaReserves", new Boolean(reservesCol != null && reservesCol.size() > 0));
        RequestDispatcher rd = getServletContext().getRequestDispatcher(resp.encodeURL(url));
        rd.forward(req, resp);
    }
}
