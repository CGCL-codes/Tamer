package net.sourceforge.gunner.commons.gui.config;

import javax.swing.JPanel;

/**
 * A panel that is to be placed within a ConfigurationDialog, allowing the user
 * to manipulate application data and settings from a user-friendly GUI.
 * @author Adam Barclay
 * @version $Revision: 1.4 $
 */
public abstract class ConfigurationPanel {

    /** Holds the content of the configuration panel. */
    private JPanel content = new JPanel();

    /** The parent into which this panel has been placed. */
    private ConfigurationDialog parent;

    /** Indicates whether or not this panels GUI elements have been created
     * and initialized
     */
    private boolean built = false;

    /** Package private method used by the ConfigurationDialog to set this
     * panels parent.
     * @param parent parent of the panel
     */
    void setParent(ConfigurationDialog parent) {
        this.parent = parent;
    }

    /** Returns the parent of this panel, into which it has been placed. The parent
     * is onyl set once it has been added to a configuration dialog.
     * @return parent of panel
     */
    public ConfigurationDialog getParent() {
        return parent;
    }

    /** Returns a short, consise description of this panel used in various
     * locations throughout the dialog, including the selection tree and
     * title bar. The title should ideally only be one word, and at a maximum
     * be two words.
     * @return short description of panel
     */
    public abstract String getTitle();

    /** Provides the configuration tree with the required method to show the
     * panels name, this method simply returning the value of <tt>getTitle()</tt>
     * @see java.lang.Object#toString()
     */
    public final String toString() {
        return getTitle();
    }

    /** Saves the values from the GUI, allowing for clients that
     * depend on the values to update their state as required. The input values
     * are assumed to be correct as the values are only saved when no errors are
     * registered with the dialog.
     */
    public abstract void saveValues();

    /** Loads values from an external source into the GUI. The values should ideally
     * be those that were specified at the last save, or default values in the case that
     * no save has been performed. */
    public abstract void loadValues();

    /** Builds the content panel that is used for configuration data input. Subclasses
     * must implement the abstract method buildPanel() to actually create the GUI buttons as
     * this method handles the setup of the dialog, setting various variables state. */
    public final void buildContent() {
        built = true;
        buildPanel();
    }

    /** Returns this panels 'built' status, indicating whether or not the GUI elements have
     * been built yet.
     * @return whether or not the GUI has been built
     */
    public boolean isBuilt() {
        return built;
    }

    /** Implementation method in which the GUI components are initialized and built. The
     * panels have no control over when they will be built, although it's guarenteed they will
     * have a parent dialog set.
     */
    protected abstract void buildPanel();

    /** Returns the panel containing the various components of configuration data
     * manipulation.
     * @return content panel
     */
    public JPanel getContent() {
        return content;
    }

    /** Returns the location of the tree into which this panel should be placed,
     * whereby the location is specified similar to a file path. If for example
     * the panel should be placed in the top level component (ie. not a sub node)
     * simply return "", whereas to place it under the node 'Classpath' which is in
     * turn under 'Java' return "Java/Classpath".
     * The default operation is to return "" indicating the panel should be placed
     * at the top level.
     * @return location of panel
     */
    public String getTreeLocation() {
        return "";
    }
}
