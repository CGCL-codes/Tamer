package oss.jthinker.views;

import java.awt.Component;
import java.net.URL;
import javax.swing.JMenuBar;

/**
 * Common interface for platform-specific functions.
 */
public interface EntryPoint {

    /**
     * Is local save/load supported on this platform?
     *
     * @return true if local save/load is supported,
     *         false otherwise (for example in applet environment)
     */
    boolean localPersistence();

    /**
     * Is it possible to save to a remote server?
     *
     * @return true if it's allowed to publish current diagram to a 
     *              remote server
     *         false otherwise
     */
    boolean globalPersistenceWrite();

    /**
     * Returns the URL of the remote server.
     *
     * @return server name or null if no remote server is configured
     */
    String getServerURL();

    /**
     * Can this application be terminated?
     *
     * @return true if {@link System#exit(int)} will terminate
     *         the application properly, false otherwise
     */
    boolean isTerminatable();

    /**
     * Sets menu bar of the application view.
     * This method is actually implemented in
     * {@link javax.swing.JApplet} and {@link javax.swing.JFrame},
     * see that classes for details.
     *
     * @param menuBar menu bar to set
     */
    void setJMenuBar(JMenuBar menuBar);

    /**
     * Adds a widget to the container.
     * This method is actually implemented in
     * {@link javax.swing.JApplet} and {@link javax.swing.JFrame},
     * see that classes for details.
     *
     * @param component widget to add
     * @param param addition parameters
     */
    void add(Component component, Object param);

    /**
     * Removes a widget from the container.
     * This method is actually implemented in
     * {@link javax.swing.JApplet} and {@link javax.swing.JFrame},
     * see that classes for details.
     *
     * @param component widget to remove 
     */
    void remove(Component component);

    /**
     * Validates the layout of the container.
     * This method is actually implemented in
     * {@link javax.swing.JApplet} and {@link javax.swing.JFrame),
     * see that classes for details.
     */
    void validate();

    /**
     * Opens the browser.
     *
     * @param url URL to open
     */
    void openBrowser(URL url);
}
