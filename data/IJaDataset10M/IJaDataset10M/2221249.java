package net.sourceforge.htmlunit.testapplets;

import java.applet.Applet;

/**
 * A simple with a single method called "doIt" that echoes the method call in the window's status of the containing page.
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class AppletDoIt extends Applet {

    private static final long serialVersionUID = -3986100495006620079L;

    public AppletDoIt() {
        super();
    }

    public void doIt(final String message) {
        getAppletContext().showStatus("Called: doIt('" + message + "')");
    }

    public void showCodeBase() {
        getAppletContext().showStatus("CodeBase: '" + getCodeBase().toExternalForm() + "'");
    }

    public void showDocumentBase() {
        getAppletContext().showStatus("DocumentBase: '" + getDocumentBase().toExternalForm() + "'");
    }

    public void showParam(final String paramName) {
        getAppletContext().showStatus(paramName + ": '" + getParameter(paramName) + "'");
    }
}
