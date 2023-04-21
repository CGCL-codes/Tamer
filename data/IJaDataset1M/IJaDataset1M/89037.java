package au.edu.ausstage.mapping;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * The main driving class for the Browse by Venue Servlet
 */
public class BrowseServlet extends HttpServlet {

    private ServletConfig servletConfig;

    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        servletConfig = conf;
    }

    /**
	 * Method to respond to a get request
	 *
	 * @param request a HttpServletRequest object representing the current request
	 * @param response a HttpServletResponse object representing the current response
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            throw new ServletException("Missing action parameter");
        }
        DataManager dataManager = new DataManager(servletConfig);
        if (action.equals("markers")) {
            BrowseDataBuilder data = new BrowseDataBuilder(dataManager);
            String results = data.getMarkerXMLString();
            response.setContentType("text/xml; charset=UTF-8");
            response.setHeader("Cache-Control", "max-age=0,no-cache,no-store,post-check=0,pre-check=0");
            PrintWriter out = response.getWriter();
            out.print(results);
        } else if (action.equals("lookup")) {
            String id = request.getParameter("id");
            String startYear = request.getParameter("start");
            String finishYear = request.getParameter("finish");
            if (id == null) {
                throw new ServletException("Missing id parameter");
            }
            id = id.trim();
            if (id == null || id.equals("")) {
                throw new ServletException("Missing id parameter");
            }
            try {
                Integer.parseInt(id);
            } catch (Exception ex) {
                throw new ServletException("id paramater must be an integer");
            }
            BrowseDataBuilder data = new BrowseDataBuilder(dataManager);
            String results = null;
            if (startYear == null && finishYear == null) {
                results = data.doSearch(id);
            } else {
                results = data.doSearch(id, startYear, finishYear);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setHeader("Cache-Control", "max-age=0,no-cache,no-store,post-check=0,pre-check=0");
            PrintWriter out = response.getWriter();
            out.print(results);
        } else if (action.equals("timeline")) {
            String id = request.getParameter("id");
            String startYear = request.getParameter("start");
            String finishYear = request.getParameter("finish");
            if (id == null) {
                throw new ServletException("Missing id parameter");
            }
            id = id.trim();
            if (id == null || id.equals("")) {
                throw new ServletException("Missing id parameter");
            }
            try {
                Integer.parseInt(id);
            } catch (Exception ex) {
                throw new ServletException("id paramater must be an integer");
            }
            BrowseDataBuilder data = new BrowseDataBuilder(dataManager);
            String results = null;
            if (startYear == null && finishYear == null) {
                results = data.getTimelineXML(id);
            } else {
                results = data.getTimelineXML(id, startYear, finishYear);
            }
            response.setContentType("text/xml; charset=UTF-8");
            response.setHeader("Cache-Control", "max-age=0,no-cache,no-store,post-check=0,pre-check=0");
            PrintWriter out = response.getWriter();
            out.print(results);
        } else if (action.equals("kml")) {
            BrowseDataBuilder data = new BrowseDataBuilder(dataManager);
            String results = data.getKMLString();
            response.setContentType("application/vnd.google-earth.kml+xml; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=ausstage-venue-data.kml");
            PrintWriter out = response.getWriter();
            out.print(results);
        } else {
            throw new ServletException("Unknown action parameter value");
        }
    }

    /**
	 * Method to respond to a post request
	 *
	 * @param request a HttpServletRequest object representing the current request
	 * @param response a HttpServletResponse object representing the current response
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException("Invalid Request Type");
    }
}
