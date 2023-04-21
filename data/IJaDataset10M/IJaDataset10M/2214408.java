package au.gov.qld.dnr.dss.v1.framework.interfaces;

import org.swzoo.log2.core.*;
import au.gov.qld.dnr.dss.v1.util.opd.interfaces.Dispatcher;
import au.gov.qld.dnr.dss.v1.util.window.interfaces.WindowManager;
import au.gov.qld.dnr.dss.v1.report.interfaces.ReportManager;
import au.gov.qld.dnr.dss.v1.util.browser.interfaces.BrowserManager;
import au.gov.qld.dnr.dss.v1.util.help.interfaces.HelpManager;

/**
 * Interface for obtaining application wide information.
 */
public interface GlobalManager {

    /**
     * Get a resource manager.
     *
     * @return a reference to a resource manager.
     */
    public ResourceManager getResourceManager();

    /**
     * Get a window manager.
     *
     * @return a reference to a window manager.
     */
    public WindowManager getWindowManager();

    /**
     * Get a button factory.
     *
     * @return a reference to a button factory.
     */
    public ButtonFactory getButtonFactory();

    /**
     * Get a dispatcher.
     *
     * @return a reference to a dispatcher.
     */
    public Dispatcher getDispatcher();

    /**
     * Get report manager.
     *
     * @return a reference to a report manager.
     */
    public ReportManager getReportManager();

    /**
     * Get user properties manager.
     *
     * @return a reference to a user properties manager.
     */
    public UserPropertiesManager getUserPropertiesManager();

    /**
     * Get browser manager.
     *
     * @return a reference to a browser manager.
     */
    public BrowserManager getBrowserManager();

    /**
     * Get help manager.
     *
     * @return a reference to a help manager.
     */
    public HelpManager getHelpManager();
}
