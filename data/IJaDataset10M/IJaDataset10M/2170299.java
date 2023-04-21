package org.ibit.avanthotel.end.web.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Category;
import org.ibit.avanthotel.end.web.delegate.TendesDelegate;
import org.ibit.avanthotel.end.web.util.InfoSessio;
import org.ibit.avanthotel.persistence.logic.util.DelegateSysException;

/**
 * Avanthotel Booking Engine
 * Copyright (C) 2005 Fundaci� IBIT
 *
 * @created 7 de octubre de 2003
 * @web.servlet name="PrivacyPolicy"
 *    description="Carga la pagina de Politica de Privacidad."
 * @web.servlet-mapping url-pattern="/privacypolicy"
 */
public class PrivacyPolicy extends HttpServlet {

    private static Category logger = Category.getInstance(PrivacyPolicy.class.getName());

    private TendesDelegate tendesDelegate;

    /**
    * @exception ServletException
    */
    public void init() throws ServletException {
        logger.info("init()");
        try {
            tendesDelegate = TendesDelegate.getInstance();
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
        String lang;
        if (req.getParameter("lang") == null) {
            req.setAttribute("nouIdioma", Boolean.FALSE);
            InfoSessio infoSessio = (InfoSessio) req.getSession().getAttribute("info");
            lang = infoSessio.getLang();
        } else {
            req.setAttribute("nouIdioma", Boolean.TRUE);
            req.setAttribute("lang", req.getParameter("lang"));
            lang = req.getParameter("lang");
        }
        Integer entityId = new Integer(req.getParameter("caixa"));
        try {
            req.setAttribute("entityName", tendesDelegate.getEntityName(entityId));
        } catch (DelegateSysException e) {
            throw new ServletException(e);
        }
        if (entityId.intValue() == 0) {
            if (lang.equals("es")) {
                req.setAttribute("entityAddress", "Palma de Mallorca, en el Pol�gono de Son Fuster en la C/ Ter n�mero 16");
            } else if (lang.equals("ca")) {
                req.setAttribute("entityAddress", "Palma de Mallorca, al Pol�gon de Son Fuster, al C/ Ter n�m. 16");
            } else if (lang.equals("en")) {
                req.setAttribute("entityAddress", "Palma de Mallorca, at Pol�gon de Son Fuster, C/ Ter, number 16");
            } else if (lang.equals("de")) {
                req.setAttribute("entityAddress", "Palma de Mallorca, Pol�gon de Son Fuster in C/ Ter n�m. 16");
            } else if (lang.equals("fr")) {
                req.setAttribute("entityAddress", "Palma de Mallorca, dans le Pol�gon de Son Fuster en la C/ Ter num�ro 16");
            } else if (lang.equals("it")) {
                req.setAttribute("entityAddress", "Palma di Maiorca, nel Pol�gon di Son Fuster, in C/Ter n�m. 16");
            }
            req.setAttribute("entityEmail", "sanostra@sanostra.es");
        } else if (entityId.intValue() == 1) {
            if (lang.equals("es")) {
                req.setAttribute("entityAddress", "Alicante, en la C/ San Fernando n�mero 40");
            } else if (lang.equals("ca")) {
                req.setAttribute("entityAddress", "Alicante, al C/ San Fernando n�m. 40");
            } else if (lang.equals("en")) {
                req.setAttribute("entityAddress", "Alicante, C/ San Fernando number 40");
            } else if (lang.equals("de")) {
                req.setAttribute("entityAddress", "Alicante, in C/ San Fernando n�m. 40");
            } else if (lang.equals("fr")) {
                req.setAttribute("entityAddress", "Alicante, en la C/ San Fernando n�mero 40");
            } else if (lang.equals("it")) {
                req.setAttribute("entityAddress", "Alicante, in C/ San Fernando n�m. 40");
            }
            req.setAttribute("entityEmail", "cam@cam.es");
        }
        String url = "/reserva/privacypolicy.jsp";
        RequestDispatcher rd = getServletContext().getRequestDispatcher(resp.encodeURL(url));
        rd.forward(req, resp);
    }
}
