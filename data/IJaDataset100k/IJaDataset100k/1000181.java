package es.f2020.osseo.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.f2020.osseo.dao.WebsiteDAO;
import es.f2020.osseo.domain.Website;
import java.util.List;

/**
 * Controller for home page, with a summary of websites information.
 */
public class IndexServlet extends HttpServlet {

    private static final long serialVersionUID = 10743348504190132L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Website> websites = new WebsiteDAO(this.getServletContext()).loadAll();
        int size = websites.size();
        switch(size) {
            case 0:
                request.getRequestDispatcher("editWebsite.do").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("websiteResults.do?id=" + websites.get(0).getId()).forward(request, response);
                break;
        }
    }
}
