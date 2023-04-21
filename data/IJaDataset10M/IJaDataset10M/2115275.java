package org.uk2005.servlets;

import org.uk2005.dialog.*;
import org.uk2005.data.*;
import org.uk2005.util.*;
import org.uk2005.store.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.*;

/**
 * Dialog servlet
 *
 * @author	<a href="mailto:niklas@saers.com">Niklas Saers</a>
 * @version	$Id: ServletDialog.java,v 1.10 2002/11/08 11:23:12 niklasjs Exp $
 */
public class ServletDialog extends HttpServlet {

    /**
	 * Logger
	 */
    protected Category logger = Category.getInstance(this.getClass().getName());

    /**
	 * Filenames for template and flow definitions
	 */
    String templateFilename;

    String flowFilename;

    /**
	 * Determines if we are in development mode
	 */
    boolean debug = false;

    /**
	 * Store
	 */
    ThingStore store;

    /**
	 * Saves the flows that are registered in different sessions.
	 * Be sure to clean this one out when sessions have expired
	 */
    Hashtable sessionFlows = new Hashtable();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        PropertyConfigurator.configure(getServletConfig().getInitParameter("logConfig"));
        store = storeFactory(config);
        templateFilename = getServletConfig().getInitParameter("templateFilename");
        flowFilename = getServletConfig().getInitParameter("flowFilename");
        if (getServletConfig().getInitParameter("debug").equals("true")) debug = true;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Flow flow = retrieveFlow(request);
        response.addCookie(new Cookie("session_id", flow.toString()));
        Control pressedControl = null;
        String pageName = request.getParameter("__condb__page__");
        Page page = flow.getPage(pageName);
        Enumeration e = request.getParameterNames();
        Context context = flow.getContext();
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();
            String paramValue = request.getParameter(paramName);
            logger.debug(paramName + " = " + paramValue);
            Enumeration ec = page.getControlNames();
            while (ec.hasMoreElements()) if (paramName.equals((String) ec.nextElement())) pressedControl = page.getControl(paramName);
            if (context.has(paramName)) {
                Input input = flow.getInput(paramName);
                input.setValue(paramValue);
            }
        }
        logger.debug("Found control that was pressed: " + pressedControl.getName());
        if (pressedControl != null) {
            PriorityBag pages = new PriorityBag();
            Enumeration en = pressedControl.getDecisions();
            while (en.hasMoreElements()) {
                Decision decision = (Decision) en.nextElement();
                Page decidedPage = decision.decide(context);
                if (decidedPage != null) {
                    pages.put(decision.getName(), decidedPage, decision.getPriority());
                    if (decision instanceof Transaction) ((Transaction) decision).serialize();
                }
            }
            if (pages.size() != 0) {
                e = pages.elements();
                page = (Page) e.nextElement();
                flow.setCurrentPage(page.getName());
                page.updateContext(flow.getContext(), flow.getStore());
            }
        }
        showPage(response, flow);
    }

    public void showPage(HttpServletResponse response, Flow flow) {
        response.setContentType("text/html");
        XHTMLConversation conversation = new XHTMLConversation();
        flow.fillConversation(conversation);
        conversation.present();
        try {
            PrintWriter out = response.getWriter();
            Template template = new FileTemplate(templateFilename);
            out.println(template.present(conversation.getBuffer()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Flow flow = retrieveFlow(request);
        response.addCookie(new Cookie("session_id", flow.toString()));
        showPage(response, flow);
        if (debug) {
            Context context = flow.getContext();
            Enumeration atoms = context.elements();
            while (atoms.hasMoreElements()) {
            }
        }
    }

    /**
	 * Retrieves Flow from sessionFlow through use of a session
	 * cookie. If it is not set, a new flow is created 
	 */
    private Flow retrieveFlow(HttpServletRequest request) {
        Flow ret = null;
        if (request.getParameter("__condb__reset__") != null) {
            XMLLoader loader = new XMLLoader(flowFilename);
            ret = loader.load(store);
            sessionFlows.put(ret.toString(), ret);
            return ret;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie cookie;
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("session_id")) if (sessionFlows.containsKey(cookie.getValue())) return (Flow) sessionFlows.get(cookie.getValue());
            }
        }
        if (ret == null) {
            XMLLoader loader = new XMLLoader(flowFilename);
            ret = loader.load(store);
            sessionFlows.put(ret.toString(), ret);
        }
        return ret;
    }

    /**
	 * Creates a store based on the config
	 */
    private ThingStore storeFactory(ServletConfig config) {
        String type = getInitParameter("storeType");
        if (type.equals("XML")) {
            String filename = config.getInitParameter("XML.Filename");
            XMLStore xmlstore = new XMLStore();
            xmlstore.setFilename(filename);
            return xmlstore;
        } else if (type.equals("SQL")) {
            String host = config.getInitParameter("SQL.Host");
            String port = config.getInitParameter("SQL.Port");
            String username = config.getInitParameter("SQL.Username");
            String password = config.getInitParameter("SQL.Password");
            String dbname = config.getInitParameter("SQL.DBName");
            String dbtype = config.getInitParameter("SQL.DBType");
            String dbURL;
            String dbDriver;
            if (dbtype.equals("MySQL")) {
                dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?user=" + username + "&password=" + password;
                dbDriver = "com.mysql.jdbc.Driver";
            } else throw new Error("Unkown DB Type " + dbtype);
            SQLStore sqlstore = new SQLStore();
            sqlstore.initDB(dbURL, dbDriver);
            return sqlstore;
        } else {
            throw new Error("Unknown store type " + type);
        }
    }
}
