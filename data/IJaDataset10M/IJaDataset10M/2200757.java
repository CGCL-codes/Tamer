package org.ibit.avanthotel.end.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Category;
import org.ibit.avanthotel.end.web.delegate.AllotjamentDelegate;
import org.ibit.avanthotel.end.web.delegate.ReservaDelegate;
import org.ibit.avanthotel.end.web.util.InfoSessio;
import org.ibit.avanthotel.persistence.logic.util.DelegateSysException;

/**
 * Aquest servlet carrega tota la informacio que se mostra en la pantalla anterior a la del pagament.
 * Ha de mostrar tots els detalls de la reserva a fer, aix� com els de l'usuari que la fa.
 *
 * @created 7 de octubre de 2003
 * @web.servlet name="PreparaConfirmacioReserva"
 *    description="Carrega les dades de la pantalla per confirmar la reserva"
 * @web.servlet-mapping url-pattern="/preparaConfirmacioReserva"
 */
public class PreparaConfirmacioReserva extends HttpServlet {

    private static final String next = "/reserva/confirmreserv.jsp";

    private Category logger = Category.getInstance(PreparaConfirmacioReserva.class);

    private AllotjamentDelegate allotjamentDelegate;

    private ReservaDelegate reservaDelegate;

    /**
    * @exception ServletException
    */
    public void init() throws ServletException {
        logger.info("init");
        try {
            allotjamentDelegate = AllotjamentDelegate.getInstance();
            reservaDelegate = ReservaDelegate.getInstance();
        } catch (DelegateSysException e) {
            throw new ServletException(e);
        }
    }

    /** */
    public void destroy() {
        logger.info("destroy()");
    }

    /**
    * @param request
    * @param response
    * @exception ServletException
    * @exception IOException
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
    * @param request
    * @param response
    * @exception ServletException
    * @exception IOException
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("executant doPost");
        InfoSessio infoSessio = (InfoSessio) request.getSession().getAttribute("info");
        Integer allotjamentId = new Integer(request.getParameter("allotjamentId"));
        Integer reservaId = new Integer(request.getParameter("reservaId"));
        String idioma = infoSessio.getLang();
        try {
            request.setAttribute("allotjament", allotjamentDelegate.loadAllotjamentConfirmacioData(allotjamentId, idioma));
            request.setAttribute("reserva", reservaDelegate.getReservaVerificacioData(reservaId, idioma));
        } catch (DelegateSysException e) {
            throw new ServletException(e);
        }
        RequestDispatcher rd = getServletContext().getRequestDispatcher(response.encodeURL(next));
        logger.info("redireccionant a " + next);
        rd.include(request, response);
    }
}
