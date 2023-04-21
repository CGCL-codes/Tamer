package de.kajoa.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author joachim
 *
 */
public class LoadDatabaseDriverServlet extends GenericServlet {

    public String getInitParameterDef(ServletConfig config, String param, String defStr) {
        String res = config.getInitParameter(param);
        if (isEmpty(res)) res = defStr;
        return res;
    }

    public static final boolean isEmpty(String s) {
        return ((s == null) || (s.trim().length() == 0));
    }

    public static final String CLASSNAMEREGEX = "[a-zA-Z0-9_\\.]*";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        StringTokenizer tok = new StringTokenizer(getInitParameterDef(config, "initDriverClasses", ""), ";");
        String className = null;
        while (tok.hasMoreTokens()) {
            className = tok.nextToken();
            log("Initialize: " + loadDriver(className));
        }
    }

    /**
	 *
	 */
    private static final long serialVersionUID = 438984701385015678L;

    public static final String loadDriver(String driverClassName) {
        String res = "";
        if (!isEmpty(driverClassName)) {
            if (!driverClassName.matches(CLASSNAMEREGEX)) {
                res = "illegal driver name: not shown";
            } else {
                try {
                    Class driverClass = Class.forName(driverClassName);
                    if (Driver.class.isAssignableFrom(driverClass)) {
                        driverClass.newInstance();
                        res = "driver loaded: '" + driverClassName + "'";
                    } else {
                        res = "Illegal driver class: '" + driverClassName + "'";
                    }
                } catch (Exception e) {
                    res = "error loading driver '" + driverClassName + "': " + e.getMessage();
                }
            }
        } else {
            res = "empty class name";
        }
        return res;
    }

    public static final String correctClassName(String s) {
        if ((!isEmpty(s)) && (s.matches(CLASSNAMEREGEX))) return s;
        return "";
    }

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        if (response instanceof HttpServletResponse) {
            response.setContentType("text/html");
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setHeader("x", "y");
        }
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            StringWriter w = new StringWriter();
            PrintWriter out = new PrintWriter(w);
            try {
                String loadDriver = correctClassName(req.getParameter("loadDriver"));
                if (!isEmpty(loadDriver)) out.println("<p>" + loadDriver(loadDriver) + "</p>");
                out.println("<html>");
                out.println("<head>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Registered Drivers</h1>");
                out.println("<ul>");
                for (Enumeration it = DriverManager.getDrivers(); it.hasMoreElements(); ) {
                    Driver d = (Driver) it.nextElement();
                    String driverName = d.getClass().getCanonicalName();
                    out.println("<li>" + driverName + "</li>");
                }
                out.println("</ul>");
                out.println("<hr/>");
                out.println("<h1>Load Driver</h1>");
                out.println("<form action=\"" + req.getRequestURI() + "\">");
                out.print("<input type=\"text\" name=\"loadDriver\" size=\"40\"");
                out.print(" value=\"" + loadDriver + "\"");
                out.println("/>");
                out.println("<input type=\"submit\" value=\"load\" />");
                out.println("</form>");
                out.println("<form action=\"" + req.getRequestURI() + "\">");
                out.println("<select name=\"loadDriver\" size=\"1\">");
                printDriverClasses(out, loadDriver, "initDriverClasses");
                printDriverClasses(out, loadDriver, "driverClasses");
                out.println("</select>");
                out.println("<br/>");
                out.println("<input type=\"submit\" value=\"load\" />");
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
                response.getWriter().print(w.toString());
            } finally {
                out.close();
                w.close();
            }
            response.getWriter().flush();
        }
    }

    /**
	 * @param out
	 * @param loadDriver
	 */
    private void printDriverClasses(PrintWriter out, String loadDriver, String parameter) {
        if (!isEmpty(parameter)) {
            String paramVal = getInitParameterDef(getServletConfig(), parameter, "");
            StringTokenizer tok = new StringTokenizer(paramVal, ";");
            while (tok.hasMoreTokens()) {
                String currClass = correctClassName(tok.nextToken());
                if (!isEmpty(currClass)) {
                    out.print("<option value=\"" + currClass + "\"");
                    if (currClass.equals(loadDriver)) out.print(" selected=\"true\"");
                    out.println(">" + currClass + "</option>");
                }
            }
        }
    }
}
