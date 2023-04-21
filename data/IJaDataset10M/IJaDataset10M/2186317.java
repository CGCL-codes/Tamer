package org.mandiwala;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that reads the user's locale and prints out a localised message.
 */
public class LocalisedMessageServlet extends HttpServlet implements Servlet {

    private static final long serialVersionUID = 6218513864787594920L;

    /**
     * {@inheritDoc}
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf8");
        Locale locale = request.getLocale();
        String localisedMessage = ResourceBundle.getBundle("bundle", locale).getString("localisedMessage");
        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.println("<head>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("Your locale is: " + locale + "<br />");
        pw.println(localisedMessage);
        pw.println("</body>");
        pw.println("</html>");
        pw.close();
    }
}
