package de.dlr.davinspector.plugins.exampleviewplugin;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import de.dlr.davinspector.common.Util;
import de.dlr.davinspector.history.AMessage;
import de.dlr.davinspector.plugin.IViewPlugin;
import de.dlr.davinspector.plugin.PluginManager;

/**
 * This class contains the Example-View-Plugin.
 *
 * @version $LastChangedRevision$
 * @author Jochen Wuest
 */
public class ExampleViewPlugin implements IViewPlugin {

    /** The direction the plugin is assigned to. */
    private PluginDirection myPlugInDirection;

    /** State of the plugin. */
    private Boolean isActive = true;

    /** The scroll pane of the view. */
    private JScrollPane myJScrollPane = null;

    /** The text pane of the view. */
    private JTextPane myJTextPane = null;

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IViewPlugin#getUI()
     */
    public JComponent getUI() {
        return myJScrollPane;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#getAuthor()
     */
    public String getAuthor() {
        return "Jochen Wuest";
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#getDescription()
     */
    public String getDescription() {
        return translate("description");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#getName()
     */
    public String getName() {
        return translate("name");
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#getType()
     */
    public PluginType getType() {
        return PluginType.VIEW_GENERAL;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#getVersion()
     */
    public int getVersion() {
        return 1;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#init(de.dlr.davinspector.plugin.PluginManager, 
     * de.dlr.davinspector.plugin.IPlugin.PluginDirection)
     */
    public void init(PluginManager pluginManager, PluginDirection direction) {
        myPlugInDirection = direction;
        if (myJScrollPane == null) {
            myJTextPane = new JTextPane();
            myJScrollPane = new JScrollPane();
            myJScrollPane.setViewportView(myJTextPane);
        }
        myJTextPane.setText("");
        Util.setUIDesign();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#getPluginDirection()
     */
    public PluginDirection getPluginDirection() {
        return myPlugInDirection;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#shutdown()
     */
    public void shutdown() {
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#isActive()
     */
    public Boolean isActive() {
        return isActive;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#setActive(java.lang.Boolean)
     */
    public void setActive(Boolean state) {
        isActive = state;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#update(de.dlr.davinspector.history.AMessage)
     */
    public void update(AMessage msg) {
        if (isActive && myJTextPane != null) {
            myJTextPane.setText(msg.getBody());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.dlr.davinspector.plugin.IPlugin#clear()
     */
    public void clear() {
        if (myJTextPane != null) {
            myJTextPane.setText("");
        }
    }

    /**
     * Reads the translated String of the ResourceBundle. Trys to load local language.
     * 
     * @param key Key for loading
     * @return needed String
     */
    public static String translate(String key) {
        final String bundleName = "de.dlr.davinspector.plugins.exampleviewplugin.TextBundle";
        ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());
        return resourceBundle.getString(key);
    }
}
