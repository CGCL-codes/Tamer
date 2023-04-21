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
import org.ibit.avanthotel.persistence.logic.util.DelegateSysException;

/**
 * @created 19 / novembre / 2003
 * @web:servlet name="cancela/CancelaReservas"
 *    description="Reports de cupos insertats, reserves realitzades i estat actual de places"
 * @web:servlet-mapping url-pattern="/avanthotel/reserves/cancela/CancelaReservas"
 */
public class CancelaReservas extends HttpServlet {

    /** */
    static Category logger = Category.getInstance(CancelaReservas.class.getName());

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
        String url = "/avanthotel/reserves/cancela/edit.jsp";
        Integer id = ServletUtil.getAllotjamentSessio(req);
        Collection reservesCol = null;
        String localizador = req.getParameter("localizador");
        String locataFound = req.getParameter("locataFound");
        String[] parametrosBusca = { localizador, null, null, null };
        try {
            if (locataFound != null) {
                reservesOfferDelegate.anulaReserva(locataFound);
                url = "/avanthotel/util/confirm.jsp";
            } else if (localizador != null) {
                reservesCol = reservesOfferDelegate.loadReportCerca(id, parametrosBusca);
            }
        } catch (DelegateSysException e) {
            url = "/avanthotel/basica/error.jsp?missatge=" + e.getMessage();
        }
        req.setAttribute("opcio", new Integer(11));
        req.setAttribute("reservesCol", reservesCol);
        req.setAttribute("hiHaReserves", new Boolean(reservesCol != null && reservesCol.size() > 0));
        logger.info("fent forward a " + url);
        RequestDispatcher rd = getServletContext().getRequestDispatcher(resp.encodeURL(url));
        rd.forward(req, resp);
    }
}
