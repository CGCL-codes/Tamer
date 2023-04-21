package gov.sns.apps.xyzcorrelator;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.net.*;
import gov.sns.application.*;
import gov.sns.xal.smf.application.*;

/**
 * The main program for the xyz correlator  app.
 * @author J. Galambos
 *
 */
public class Main extends ApplicationAdaptor {

    public Main() {
        url = null;
    }

    public Main(String str) {
        try {
            url = new URL(str);
        } catch (MalformedURLException exception) {
            System.err.println(exception);
        }
    }

    public String[] readableDocumentTypes() {
        return new String[] { "xyz" };
    }

    public String[] writableDocumentTypes() {
        return new String[] { "xyz" };
    }

    /** this returns a new document to start working with  */
    public XalDocument newEmptyDocument() {
        XyzDocument md;
        return md = new XyzDocument(url);
    }

    public XalDocument newDocument(java.net.URL theUrl) {
        XyzDocument md = new XyzDocument(theUrl);
        return md;
    }

    public String applicationName() {
        return "xyzCorrelator";
    }

    public boolean usesConsole() {
        return true;
    }

    /** The url to use to create the startup document with */
    private URL url;

    /**
     * Register actions for the Special menu items
     */
    protected void customizeCommands(Commander commander) {
    }

    public void applicationFinishedLaunching() {
        System.out.println("xyzCorrelator finished launching...");
    }

    public static void main(String[] args) {
        String strUrl = null;
        System.out.println("Launching xyzCorrelator...");
        setOptions(args);
        AcceleratorApplication.launch(new Main());
    }
}
