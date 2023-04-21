package gov.sns.apps.opticseditor;

import gov.sns.application.*;
import gov.sns.xal.smf.application.*;
import gov.sns.xal.smf.data.XMLDataManager;

/**
 * Main is the ApplicationAdaptor for the Tuna.
 * @author  t6p
 */
public class Main extends ApplicationAdaptor {

    /**
     * Returns the text file suffixes of files this application can open.
     * @return Suffixes of readable files
     */
    public String[] readableDocumentTypes() {
        return new String[] { "xal" };
    }

    /**
     * Returns the text file suffixes of files this application can write.
     * @return Suffixes of writable files
     */
    public String[] writableDocumentTypes() {
        return new String[] { "xal" };
    }

    /**
     * Implement this method to return an instance of my custom document.
     * @return An instance of my custom document.
     */
    public XalDocument newEmptyDocument() {
        final String defaultPath = XMLDataManager.defaultPath();
        if (defaultPath != null) {
            try {
                return new OpticsEditorDocument(new java.io.File(defaultPath).toURL());
            } catch (Exception exception) {
                exception.printStackTrace();
                return new OpticsEditorDocument();
            }
        } else {
            return new OpticsEditorDocument();
        }
    }

    /**
     * Implement this method to return an instance of my custom document corresponding to the specified URL.
     * @param url The URL of the file to open.
     * @return An instance of my custom document.
     */
    public XalDocument newDocument(final java.net.URL url) {
        return new OpticsEditorDocument(url);
    }

    /** Specifies the name of this application. */
    public String applicationName() {
        return "Optics Editor";
    }

    /** Capture the application launched event and print it. */
    public void applicationFinishedLaunching() {
        System.out.println("Optics Editor has finished launching!");
    }

    /** Constructor */
    public Main() {
    }

    /** The main method of the application. */
    public static void main(final String[] args) {
        try {
            System.out.println("Starting Optics Editor...");
            Application.launch(new Main());
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            exception.printStackTrace();
            Application.displayApplicationError("Launch Exception", "Launch Exception", exception);
            System.exit(-1);
        }
    }
}
