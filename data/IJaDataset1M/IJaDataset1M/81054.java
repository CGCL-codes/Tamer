package org.ibit.avanthotel.end.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Category;
import org.ibit.avanthotel.end.web.delegate.AllotjamentDelegate;
import org.ibit.avanthotel.persistence.logic.util.DelegateSysException;

/**
 * @created 7 de octubre de 2003
 * @web.servlet name="Foto"
 *    description="Carga las imagenes."
 * @web.servlet-mapping url-pattern="/foto"
 */
public class Foto extends HttpServlet {

    private static Category logger = Category.getInstance(Foto.class.getName());

    private AllotjamentDelegate allotjamentDelegate;

    /**
    * @exception ServletException
    */
    public void init() throws ServletException {
        logger.info("init()");
        try {
            allotjamentDelegate = allotjamentDelegate.getInstance();
        } catch (DelegateSysException e) {
            throw new ServletException(e);
        }
    }

    /** */
    public void destroy() {
        logger.info("destroy()");
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
        String url = "/multimed/foto.jsp";
        Integer idImatge = new Integer(req.getParameter("id"));
        try {
            req.setAttribute("nom", allotjamentDelegate.getNomAllotjamentAmbImatge(idImatge));
        } catch (DelegateSysException e) {
            throw new ServletException(e);
        }
        req.setAttribute("id", idImatge);
        RequestDispatcher rd = getServletContext().getRequestDispatcher(resp.encodeURL(url));
        rd.forward(req, resp);
    }
}
