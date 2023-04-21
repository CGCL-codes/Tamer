package org.ibit.avanthotel.admin.web.servlet;

import org.ibit.avanthotel.admin.web.delegate.AllotjamentsAdminDelegate;
import org.ibit.avanthotel.persistence.logic.util.DelegateSysException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @web.servlet
 *    name="BaixaPropietats"
 *    description="Elimina un conjunt d'allotjaments de la llista de propietats d'un usuari"
 *
 * @web.servlet-mapping
 *    url-pattern="/avanthotel/usuaris/baixaPropietats"
 *
 */
public class BaixaPropietats extends HttpServlet {

    private AllotjamentsAdminDelegate allotjamentsAdminDelegate;

    public void init() throws ServletException {
        try {
            allotjamentsAdminDelegate = AllotjamentsAdminDelegate.getInstance();
        } catch (DelegateSysException e) {
            throw new ServletException(e);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String propietari = req.getParameter("propietari");
        String[] allotjamentIdsComStrings = req.getParameterValues("allotjament");
        Integer[] allotjamentsIds = new Integer[allotjamentIdsComStrings.length];
        for (int i = 0; i < allotjamentsIds.length; i++) {
            allotjamentsIds[i] = new Integer(allotjamentIdsComStrings[i]);
        }
        String url = "/avanthotel/usuaris/seleccionaUsuariOAllotjament?usuari=" + propietari + "&allotjament=-1";
        try {
            allotjamentsAdminDelegate.baixaPropietats(propietari, allotjamentsIds);
        } catch (DelegateSysException e) {
            throw new ServletException(e);
        }
        RequestDispatcher rd = getServletContext().getRequestDispatcher(resp.encodeURL(url));
        rd.forward(req, resp);
    }
}
