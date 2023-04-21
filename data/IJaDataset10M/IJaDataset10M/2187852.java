package org.corrib.s3b.sscf.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.corrib.s3b.sscf.filters.RecommendationsHandler;
import org.json.jdk5.simple.JSONObject;

/**
 * @author adagze
 *
 */
public class SscfRecommendations extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7086707393948771847L;

    protected void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getParameter("type");
        String person = req.getParameter("person");
        if (type != null && !"".equals(type) && person != null && !"".equals(person)) {
            RecommendationsHandler rh = new RecommendationsHandler(person, type.equals("new"));
            sendJSONResponse(resp, rh.getJSON());
        } else {
            JSONObject jo = new JSONObject();
            jo.put("count", 0);
            sendJSONResponse(resp, jo.toString());
        }
    }

    public void sendJSONResponse(HttpServletResponse resp, String result) throws IOException {
        resp.setContentType("text/x-javascript;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.write(result);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }
}
