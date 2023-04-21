package at.ac.tuwien.vitalab.log4ws.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A test servlet that writes all content and header attributes received by a
 * client to System.out.
 * 
 * @author Hannes
 * 
 */
public class SubscriberTestApp extends HttpServlet {

    public SubscriberTestApp() {
    }

    /**
   * @see GenericServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   */
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        System.out.println("receiving Servlet call...");
        System.out.println("========== HEADERS begin ==========");
        String headerName;
        for (Enumeration<?> headerNames = req.getHeaderNames(); headerNames.hasMoreElements(); System.out.println((new StringBuilder()).append(headerName).append(": ").append(req.getHeader(headerName)).toString())) headerName = (String) headerNames.nextElement();
        System.out.println("========== HEADERS end ==========");
        System.out.println("========== CONTENT begin ==========");
        BufferedReader reader = req.getReader();
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) sb.append(line + "\n");
        System.out.println(sb.toString());
        System.out.println("========== CONTENT end ==========");
        System.out.println("Servlet call received!");
    }
}
