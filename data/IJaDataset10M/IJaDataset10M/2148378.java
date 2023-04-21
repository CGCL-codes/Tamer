package svim;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JMenu;

/**
 * PortlIfc defines the interface required of all Portl compatible applications.
 * The main method must implement this interface to be loaded at runtime by
 * the Portal Desktop.
 */
public interface PortlIfc {

    /**
     * Returns human readable information about the application as a 2D String array.
     * Values in first dimension are Author, Copyright, Name, Description, Comment,
     * Keywords.
     *
     * Example:
     * "Author", "John Doe"
     */
    public String[][] getAppletInfo();

    /**
     * Returns an array containing all JMenus that are to be displayed by the
     * application, or an empty array if the application is to not have its own
     * items in the JMenuBar of its window.
     */
    public JMenu[] getJMenus();

    /**
     * Returns an JComponent that will be displayed in the application window.
     */
    public JComponent getJComponent();

    /**
     * Returns human readable information about the application parameters as a 2D
     * String array. Values in first dimension are command line switches, and values
     * in the second are explainations of the switches.
     * Example:
     * "-f", "Opens file"
     */
    public String[][] getParameterInfo();

    /**
     * Returns the preferred size of the application as a Dimension. This method
     * is called to size the application window.
     */
    public Dimension getPreferredSize();

    /**
     * Returns the version number of the application as a double. This method is
     * called to compare local versions to remote versions for the purpose of
     * downloading the latest version.
     */
    public double getVersion();

    /**
     * Start is called before the application window is visible to initialize any
     * resources used by the application.
     */
    public void start();

    /**
     * Stop is called after the application window is closed to release any
     * resources used by the application.
     */
    public void stop();
}
