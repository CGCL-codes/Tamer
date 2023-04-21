package net.sf.webstrap.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An example servlet to prove that the webdoclet is working. It should print a
 * plain text message when you hit it.
 * 
 * @author kylev
 * @version $Revision: 1.2 $
 * 
 * @web.servlet name="example"
 * @web.servlet-mapping url-pattern="/exampleServlet"
 */
public class ExampleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(ExampleServlet.class);

    public void init() throws ServletException {
        super.init();
        log.info("Initialized");
    }

    public void destroy() {
        super.destroy();
        log.info("Destoyed");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.print("You've reached my servlet");
    }
}
