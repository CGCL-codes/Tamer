package fr.loria.ecoo.wooki.web.servlets;

import fr.loria.ecoo.wooki.web.WookiSite;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ViewModifications extends BaseServlet {

    private static final long serialVersionUID = -4120615253521772704L;

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageId = request.getParameter("page");
        if ((pageId == null) || (pageId.trim().length() == 0)) {
            pageId = "Home";
        }
        String result = WookiSite.getInstance().getWookiEngine().getWootEngine().getPageContentModifications(pageId);
        if (result == "") {
            request.getRequestDispatcher("/CreatePage?page=" + pageId).forward(request, response);
            return;
        }
        request.setAttribute("page", pageId);
        request.setAttribute("title", pageId);
        request.setAttribute("content", result);
        request.getRequestDispatcher("/pages/viewModifications.jsp").forward(request, response);
        return;
    }
}
