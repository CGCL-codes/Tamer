package eu.roelbouwman.housestyle;

import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.app.Window;

/**
 * Application instance implementation.
 */
public class Application extends ApplicationInstance {

    private String version = "";

    private String language = "en";

    /** Main window of user interface. */
    private Window mainWindow;

    /**
     * Returns the active <code>ApplicationInstance</code> cast to the 
     * appropriate type.
     * 
     * @return the active <code>ApplicationInstance</code>
     */
    public static Application getApp() {
        return (Application) getActive();
    }

    /**
     * @see nextapp.echo2.app.ApplicationInstance#init()
     */
    public Window init() {
        setStyleSheet(Styles.DEFAULT_STYLE_SHEET);
        mainWindow = new Window();
        mainWindow.setContent(new MainForm());
        return mainWindow;
    }

    /**
     * @return Returns the version String, obtained from {@link EchoAppServlet#VERSIONFILENAME} 
     */
    public String getVersion() {
        return version;
    }

    /**
     * This method is typically called from the EchoAppServlet to set the version information
     * param Sets the version String, obtained from {@link EchoAppServlet#VERSIONFILENAME} 
     */
    public void setVersion(String v) {
        if (v != null) this.version = v;
    }

    /**
	 * @return Returns the language String, this can be used to open localized properties file 
	 */
    public String getLanguage() {
        return language;
    }

    /**
	 * This method is typically called from the main window, where a user can set the preferred language 
	 */
    public void setLanguage(String lang) {
        if (lang != null) this.language = lang;
        mainWindow.removeAll();
        mainWindow.setContent(new MainForm());
    }
}
