package org.apache.log4j.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.ivoa.util.LogUtil;
import org.ivoa.util.SystemLogUtil;

/**
 * A servlet used to dynamically adjust package logging levels while an application is running. NOTE: This servlet is
 * only aware of pre-configured packages and packages that contain objects that have logged at least one message since
 * application startup.
 * <p>
 * web.xml configuration:
 * </p>
 *
 * <pre>
 * &lt;servlet>
 *   &lt;servlet-name>log4j&lt;/servlet-name>
 *   &lt;display-name>Log4j configuration Servlet&lt;/display-name>
 *   &lt;servlet-class>org.apache.log4j.servlet.ConfigurationServlet&lt;/servlet-class>
 * &lt;/servlet>
 * </pre>
 *
 * <p>
 * The <code>fragment</code> parameter can be added if you don't want a full xhtml page in output, but only the
 * content of the body tag, so that it can be used in portlets or struts tiles.
 * </p>
 *
 * <pre>
 * &lt;servlet>
 *   &lt;servlet-name>log4j&lt;/servlet-name>
 *   &lt;display-name>Log4j configuration Servlet&lt;/display-name>
 *   &lt;servlet-class>org.apache.log4j.servlet.ConfigurationServlet&lt;/servlet-class>
 *   &lt;init-param>
 *     &lt;param-name>fragment&lt;/param-name>
 *     &lt;param-value>true&lt;/param-value>
 *   &lt;/init-param>
 * &lt;/servlet>
 * </pre>
 *
 * @author Luther E. Birdzell lebirdzell@yahoo.com
 * @author Yoav Shapira yoavs@apache.org
 * @author Fabrizio Giustina
 * @since 1.3
 * @version $Revision: 1.2 $ ($Author: root $)
 */
public class ConfigurationServlet extends HttpServlet {

    /** serial UID for Serializable interface */
    private static final long serialVersionUID = 1L;

    /**
     * The response content type: text/html
     */
    private static final String CONTENT_TYPE = "text/html";

    /**
     * Should not print html head and body?
     */
    private static final String CONFIG_FRAGMENT = "fragment";

    /**
     * The root appender.
     */
    private static final String ROOT = "Root";

    /**
     * The name of the class / package.
     */
    private static final String PARAM_CLASS = "class";

    /**
     * The logging level.
     */
    private static final String PARAM_LEVEL = "level";

    /**
     * Sort by level?
     */
    private static final String PARAM_SORTBYLEVEL = "sortbylevel";

    /**
     * All the log levels.
     */
    private static String[] LEVELS = new String[] { Level.OFF.toString(), Level.FATAL.toString(), Level.ERROR.toString(), Level.WARN.toString(), Level.INFO.toString(), Level.DEBUG.toString(), Level.ALL.toString() };

    /**
     * Don't include html head.
     */
    private boolean isFragment;

    /**
     * Print the status of all current <code>Logger</code> s and an option to change their respective logging levels.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sortByLevelParam = request.getParameter(PARAM_SORTBYLEVEL);
        boolean sortByLevel = ("true".equalsIgnoreCase(sortByLevelParam) || "yes".equalsIgnoreCase(sortByLevelParam));
        synchronized (this) {
            List<Logger> loggers = getSortedLoggers(sortByLevel);
            int loggerNum = 0;
            PrintWriter out = response.getWriter();
            if (!isFragment) {
                response.setContentType(CONTENT_TYPE);
                printHeader(out, request);
            }
            out.println("<a href=\"" + request.getRequestURI() + "\">Refresh</a>");
            out.println("<table class=\"log4jtable\">");
            out.println("<thead><tr>");
            out.println("<th title=\"Logger name\">");
            out.println("<a href=\"?" + PARAM_SORTBYLEVEL + "=false\">Class</a>");
            out.println("</th>");
            out.println("<th title=\"Is logging level inherited from parent?\" style=\"text-align:right\" >*</th>");
            out.println("<th title=\"Logger level\">");
            out.println("<a href=\"?" + PARAM_SORTBYLEVEL + "=true\">Level</a>");
            out.println("</th>");
            out.println("</tr></thead>");
            out.println("<tbody>");
            displayLogger(out, Logger.getRootLogger(), loggerNum++, request);
            Iterator<Logger> iterator = loggers.iterator();
            while (iterator.hasNext()) {
                displayLogger(out, iterator.next(), loggerNum++, request);
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("<a href=\"\">Refresh</a>");
            if (!isFragment) {
                out.println("</body></html>");
                out.flush();
                out.close();
            }
        }
    }

    /**
     * Change a <code>Logger</code>'s level, then call <code>doGet</code> to refresh the page.
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String className = request.getParameter(PARAM_CLASS);
        String level = request.getParameter(PARAM_LEVEL);
        synchronized (this) {
            if (className != null) {
                setClass(className, level);
            }
            doGet(request, response);
        }
    }

    /**
     * Print a Logger and its current level.
     * @param out the output writer.
     * @param logger the logger to output.
     * @param row the row number in the table this logger will appear in.
     * @param request the servlet request.
     */
    private void displayLogger(PrintWriter out, Logger logger, int row, HttpServletRequest request) {
        String color = null;
        String loggerName = (logger.getName().equals("") ? ROOT : logger.getName());
        color = ((row % 2) == 1) ? "even" : "odd";
        out.println("<tr class=\"" + color + "\">");
        out.println("<td>");
        out.println(loggerName);
        out.println("</td>");
        out.println("<td style=\"text-align:right\">");
        if ((logger.getLevel() == null)) {
            out.println("*");
        }
        out.println("</td>");
        out.println("<td>");
        out.println("<form action=\"\" method=\"post\">");
        printLevelSelector(out, logger.getEffectiveLevel().toString());
        out.println("<input type=\"hidden\" name=\"" + PARAM_CLASS + "\" value=\"" + loggerName + "\">");
        out.print("<input type=\"submit\" name=\"Set\" value=\"Set \">");
        out.println("</form>");
        out.println("</td>");
        out.println("</tr>");
    }

    /**
     * Set a logger's level.
     * @param className class name of the logger to set.
     * @param level the level to set the logger to.
     * @return String return message for display.
     */
    private synchronized String setClass(String className, String level) {
        Logger logger = null;
        try {
            logger = (ROOT.equalsIgnoreCase(className) ? Logger.getRootLogger() : Logger.getLogger(className));
            LogUtil.setLevel(logger, Level.toLevel(level));
            return "Message Set For " + (logger.getName().equals("") ? ROOT : logger.getName());
        } catch (Throwable th) {
            if (SystemLogUtil.isErrorEnabled()) {
                SystemLogUtil.error("ConfigurationServlet.setClass : Error setting LOG4J Logger level : ", th);
            }
        }
        return "";
    }

    /**
     * Get a sorted list of all current loggers.
     * @param sortByLevel if <code>true</code> sort loggers by level instead of name.
     * @return List the list of sorted loggers.
     */
    private List<Logger> getSortedLoggers(boolean sortByLevel) {
        @SuppressWarnings("unchecked") Enumeration<Logger> e = LogManager.getCurrentLoggers();
        Comparator<Logger> comp = new LoggerComparator(sortByLevel);
        List<Logger> list = new ArrayList<Logger>();
        while (e.hasMoreElements()) {
            list.add(e.nextElement());
        }
        Collections.sort(list, comp);
        return list;
    }

    /**
     * Prints the page header.
     * @param out The output writer
     * @param request The request
     */
    private void printHeader(PrintWriter out, HttpServletRequest request) {
        out.println("<html><head><title>Log4J Control Console</title>");
        out.println("<style type=\"text/css\">");
        out.println("body{ background-color:#fff; }");
        out.println("body, td, th, select, input{ font-family:Verdana, Geneva, Arial, sans-serif; font-size: 8pt;}");
        out.println("select, input{ border: 1px solid #ccc;}");
        out.println("table.log4jtable, table.log4jtable td { border-collapse:collapse; border: 1px solid #ccc; ");
        out.println("white-space: nowrap; text-align: left; }");
        out.println("form { margin:0; padding:0; }");
        out.println("table.log4jtable thead tr th{ background-color: #5991A6; padding: 2px; }");
        out.println("table.log4jtable tr.even { background-color: #eee; }");
        out.println("table.log4jtable tr.odd { background-color: #fff; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h3>Log4J Control Console</h3>");
    }

    /**
     * Prints the Level select HTML.
     * @param out The output writer
     * @param currentLevel the current level for the log (the selected option).
     */
    private void printLevelSelector(PrintWriter out, String currentLevel) {
        out.println("<select id=\"" + PARAM_LEVEL + "\" name=\"" + PARAM_LEVEL + "\">");
        for (int j = 0; j < LEVELS.length; j++) {
            out.print("<option");
            if (LEVELS[j].equals(currentLevel)) {
                out.print(" selected=\"selected\"");
            }
            out.print(">");
            out.print(LEVELS[j]);
            out.println("</option>");
        }
        out.println("</select>");
    }

    /**
     * Compare the names of two <code>Logger</code>s. Used for sorting.
     */
    private final class LoggerComparator implements Comparator<Logger> {

        /**
         * Sort by level? (default is sort by class name)
         */
        private boolean sortByLevel;

        /**
         * instantiate a new LoggerComparator
         * @param pSortByLevel if <code>true</code> sort loggers by level instead of name.
         */
        public LoggerComparator(boolean pSortByLevel) {
            this.sortByLevel = pSortByLevel;
        }

        /**
         * Compare the names of two <code>Logger</code>s.
         * @param logger1 an <code>Object</code> value
         * @param logger2 an <code>Object</code> value
         * @return an <code>int</code> value
         */
        public int compare(final Logger logger1, final Logger logger2) {
            if (!sortByLevel) {
                return logger1.getName().compareTo(logger2.getName());
            }
            return logger1.getEffectiveLevel().toInt() - logger2.getEffectiveLevel().toInt();
        }

        /**
         * Return <code>true</code> if the <code>Object</code> is a <code>LoggerComparator</code> instance.
         * @param object an <code>Object</code> value
         * @return a <code>boolean</code> value
         */
        @Override
        public boolean equals(final Object object) {
            if (!(object instanceof LoggerComparator)) {
                return false;
            }
            return this.sortByLevel == ((LoggerComparator) object).sortByLevel;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        String fragmentParam = config.getInitParameter(CONFIG_FRAGMENT);
        isFragment = ("true".equalsIgnoreCase(fragmentParam) || "yes".equalsIgnoreCase(fragmentParam));
        super.init(config);
    }
}
