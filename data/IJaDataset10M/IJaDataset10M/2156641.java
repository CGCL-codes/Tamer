package com.oktiva.mogno;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;

/** This is the base class for the TopLevel Visual components,
 * like Page or Included
 * @version $Id: TopLevel.java,v 1.1.1.1 2005/01/05 16:52:48 ruoso Exp $
 */
public class TopLevel extends Container {

    static Logger logger = Logger.getLogger(TopLevel.class.getName());

    protected Application application = null;

    public void setApplication(Application app) {
        application = app;
    }

    /**
	 * Event dispatched when some exception is not caught in the
	 * inner components
	 */
    public String evOnUncaughtError = "";

    public String getEvOnUncaughtError() {
        return evOnUncaughtError;
    }

    public void setEvOnUncaughtError(String evOnUncaughtError) {
        this.evOnUncaughtError = evOnUncaughtError;
    }

    /**
	 * Event dispatched when a message is received.
	 */
    public String evOnMessage = "";

    public String getEvOnMessage() {
        return evOnMessage;
    }

    public void setEvOnMessage(String evOnMessage) {
        this.evOnMessage = evOnMessage;
    }

    public void message(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String serialized = request.getParameter("mognoSerializedBag_" + name);
        if (serialized != null && !"___Unserializable___".equals(serialized) && !"".equals(serialized)) {
            unserializeBag(serialized);
        }
        dispatch("evOnMessage");
        Enumeration e = listChilds();
        while (e.hasMoreElements()) {
            Component component = (Component) getChild((String) e.nextElement());
            if (component instanceof Visual) {
                Visual visual = (Visual) component;
                visual.receiveRequest(request);
            }
        }
        try {
            e = listChilds();
            while (e.hasMoreElements()) {
                Component component = (Component) getChild((String) e.nextElement());
                if (component != null) {
                    if (component instanceof Visual) {
                        Visual visual = (Visual) component;
                        visual.dispatchAll();
                    }
                }
            }
        } catch (SyntaxErrorException ex) {
            dispatch("evOnSyntaxError");
            application.outHtml(show());
        } catch (Exception ex) {
            if (evOnUncaughtError == null || evOnUncaughtError.equals("")) {
                throw ex;
            } else {
                dispatch("evOnUncaughtError");
            }
        }
    }

    /** This function is overrided to check the syntax of owned components.
	 * @param fields A Vector with the name of the components to be tested
	 * @throws SyntaxErrorException
	 */
    public void checkSyntax(Vector fields) throws SyntaxErrorException {
        boolean err = false;
        for (int i = 0; i < fields.size(); i++) {
            Component component = getChild((String) fields.get(i));
            if (component instanceof Visual) {
                Visual visual = (Visual) component;
                try {
                    visual.checkSyntax();
                } catch (SyntaxErrorException ex) {
                    err = true;
                    visual.problematic = true;
                    visual.lastError = "syntaxError";
                    try {
                        visual.dispatch("evOnSyntaxError");
                    } catch (Exception exc) {
                        throw new SyntaxErrorException(exc.getMessage());
                    }
                }
            }
        }
        if (err) {
            throw new SyntaxErrorException();
        }
    }

    /**
	 * @return the application of this TopLevel.
	 * @see Component#getApplication()
	 */
    public Application getApplication() {
        logger.debug("Getting application from TopLevel " + this.name);
        return application;
    }

    /** Verify if this component has an ancestor named <i>name</i><br>
	 * <b>In TopLevel, this is always false, because <i>there is no parent</i>.</b>
	 * @param name The name of the ancestor component
	 * @return boolean always false
	 */
    public boolean descendentOf(String name) {
        return false;
    }

    public void showPage(String destiny) throws Exception {
        TopLevel tl = application.getTopLevel(destiny);
        application.outHtml(tl.show());
    }
}
