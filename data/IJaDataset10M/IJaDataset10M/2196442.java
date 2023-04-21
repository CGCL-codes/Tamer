package com.manning.sdmia.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.manning.sdmia.dataaccess.MyRepository;

/**
 * Servlet uses an OSGi service.
 * This service is declared by a dataaccess bundle, exported and
 * then consumed through the web application Spring context.
 * @author acogoluegnes
 *
 */
public class AnswerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        answer(resp, req.getMethod());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        answer(resp, req.getMethod());
    }

    public String getServletInfo() {
        return "Servlet using an OSGi service";
    }

    private void answer(HttpServletResponse response, String method) throws IOException {
        response.setContentType("text/html");
        ServletOutputStream out = response.getOutputStream();
        out.println("<html>");
        out.println("<head><title>The answer to life, the universe and everything</title></head>");
        out.println("<body>");
        out.println("<h1>The answer to life, the universe and everything is:</h1>");
        out.println("<p>" + getRepository().getAnswerToLifeUniverseAndEverything() + "</p>");
        out.println("</body></html>");
    }

    private MyRepository getRepository() {
        MyRepository repo = (MyRepository) WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean("repository");
        return repo;
    }
}
