package com.protomatter.pas;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import com.protomatter.util.*;
import com.protomatter.syslog.*;
import com.protomatter.pas.jndi.*;
import com.protomatter.pas.init.StartupException;

/**
 *  The PAS Servlet.  All things related to PAS start here.  When
 *  this servlet is initialized, it reads the configuration file
 *  (specified with the "properties" initialization argument) and
 *  starts all the configured services.  All the real work
 *  is performed by the <tt>PASCore</tt> class.
 *
 *  @see PASCore
 */
public final class PASServlet extends HttpServlet {

    private PASCore pasCoreInstance = null;

    private static PASServlet pasServletInstance = null;

    private String propertiesFile = null;

    /**
   *  Default constructor.
   */
    public PASServlet() {
        super();
        pasServletInstance = this;
        pasCoreInstance = new PASCore();
    }

    /**
   *  Runs all registered <tt>PASShutdown</tt> classes.
   */
    public void destroy() {
        pasCoreInstance.shutdown();
        super.destroy();
    }

    /**
   *  Return information about this servlet.
   *
   *  @see javax.servlet.GenericSevlet
   */
    public String getServletInfo() {
        return "PASServlet -- Protomatter Application Server version " + pasCoreInstance.getVersion() + " Copyright (c) 1998, 1999 Nate Sammons <nate@protomatter.com>";
    }

    /**
   *  Reads the configuration file (specified with the "properties"
   *  initialization parameter) and starts all configured services.
   */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (config.getInitParameter("properties") == null) {
            throw new ServletException("You must specify the \"properties\" init parameter");
        }
        ProtoProperties serverProperties = new ProtoProperties();
        propertiesFile = config.getInitParameter("properties");
        try {
            File file = new File(propertiesFile);
            serverProperties.load(new FileInputStream(file));
        } catch (IOException x) {
            x.printStackTrace();
            throw new ServletException("IOException in init(): " + x.getMessage());
        }
        try {
            pasCoreInstance.startup(propertiesFile, serverProperties);
        } catch (StartupException x) {
            x.printStackTrace();
            throw new ServletException("StartupException from PASCore: " + x);
        }
    }

    /**
   *  Prints out the configuration file as HTML.
   */
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter pw = new PrintWriter(resp.getOutputStream(), true);
        pw.println("<html>");
        pw.println("<head><title>Protomatter Application Server</title></head>");
        pw.println("<body bgcolor=\"#FFFFFF\">");
        pw.println("<center>");
        pw.println("<h2>Protomatter Application Server</h2>");
        pw.println("<i>Version " + pasCoreInstance.getVersion() + "</i>");
        pw.println("</center><P>");
        pw.println("<table border=1>");
        pw.println("<tr><th colspan=2 bgcolor=\"#CCCCCC\">Configuration</th></tr>");
        Properties props = getProperties();
        Enumeration e = props.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String val = props.getProperty(key);
            pw.println("<tr>");
            pw.println("<td align=left valign=top><tt>");
            pw.println(key);
            pw.println("</tt></td>");
            pw.println("<td><tt>");
            StringTokenizer st = new StringTokenizer(val, ",");
            while (st.hasMoreTokens()) {
                pw.print(st.nextToken());
                if (st.hasMoreTokens()) pw.print(",<br>");
                pw.println("");
            }
            pw.println("</tt></td>");
            pw.println("</tr>");
        }
        pw.println("</table>");
        pw.println("</body>");
        pw.println("</html>");
        pw.flush();
    }

    /**
   *  Get the system configuration.  Returns a Properties object
   *  describing the configuration of the system when it was started.
   */
    public Properties getProperties() {
        return this.pasCoreInstance.getProperties();
    }

    /**
   *  Get the globally unique instance of PAS.
   */
    public static PASServlet getInstance() {
        return pasServletInstance;
    }
}
