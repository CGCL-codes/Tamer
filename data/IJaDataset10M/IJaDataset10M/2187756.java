package org.w3c.tidy.servlet.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.tidy.servlet.util.HTMLEncode;

/**
 * Servlet used to display jsp source for example pages.
 * @author Fabrizio Giustina
 * @version $Revision: 771 $ ($Author: vlads $)
 */
public class DisplaySourceServlet extends HttpServlet {

    /**
     * D1597A17A6.
     */
    private static final long serialVersionUID = 899149338534L;

    /**
     * the folder containg example pages.
     */
    private static final String EXAMPLE_FOLDER = "";

    /**
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jspFile = request.getServletPath();
        int dotIdx = jspFile.lastIndexOf(".");
        String srcType = jspFile.substring(dotIdx);
        jspFile = jspFile.substring(0, dotIdx);
        if (jspFile.equals("/")) {
            jspFile = "index.jsp";
        } else if (jspFile.lastIndexOf("/") == (jspFile.length() - 1)) {
            jspFile += "index.jsp";
        }
        if ((jspFile.indexOf("..") >= 0) || (jspFile.toUpperCase().indexOf("/WEB-INF/") >= 0) || (jspFile.toUpperCase().indexOf("/META-INF/") >= 0)) {
            throw new ServletException("Invalid file selected: " + jspFile);
        }
        if (srcType.equals(".source")) {
            printResourceSrc(response, jspFile);
        }
    }

    private void printResourceSrc(HttpServletResponse response, String jspFile) throws ServletException, IOException {
        String fullName = EXAMPLE_FOLDER + jspFile;
        InputStream inputStream = getServletContext().getResourceAsStream(fullName);
        if (inputStream == null) {
            throw new ServletException("Unable to find JSP file: [" + jspFile + "]");
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        printHeader(out, jspFile);
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(inputStream));
        String str;
        int ln = 0;
        while ((str = lnr.readLine()) != null) {
            ln = lnr.getLineNumber();
            out.println(HTMLEncode.encode(str));
        }
        printFooter(out, jspFile);
        lnr.close();
        inputStream.close();
    }

    private void printHeader(PrintWriter out, String jspFile) throws IOException {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
        out.println("<head>");
        out.println("<title>");
        out.println("source for " + jspFile);
        out.println("</title>");
        out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" />");
        out.println("</head>");
        out.println("<body>");
        out.println("<pre>");
    }

    private void printFooter(PrintWriter out, String jspFile) throws IOException {
        out.println("</pre>");
        out.println("</body>");
        out.println("</html>");
    }
}
