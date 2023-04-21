package icm.unicore.plugins.amber;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Vector;
import java.net.URL;
import javax.swing.*;
import javax.help.HelpSet;
import org.unicore.resources.*;
import com.pallas.unicore.client.Client;
import com.pallas.unicore.client.panels.*;
import com.pallas.unicore.client.util.*;
import com.pallas.unicore.container.*;
import com.pallas.unicore.resourcemanager.ResourceManager;

/**
 *  This class manages the execute lsMD plugin
 *
 *@author     Lukasz Skorwider
 *@created    14 January 2002
 *@version    $Id: AmberPlugin.java,v 1.2 2004/09/22 14:36:24 golbi Exp $
 */
public class AmberPlugin extends TaskPlugable {

    private Client client;

    private lsMDDefaults lsMDDefaults;

    private static int counter = 1;

    public lsMDDefaults getDefaults() {
        return lsMDDefaults;
    }

    protected static Image pluginImage;

    /**
   * Return the name of the plugin
   *@return String with plugin name
   */
    public String getName() {
        return "Amber";
    }

    /**
   *  Get a short info message for the plugin
   *
   *@return    String with info message
   */
    public String getPluginInfo() {
        return "Amber Plugin 1.0\n" + "aka lsMD Plugin 1.0\n" + "(C)2001-2003 Lukasz Skorwider\n" + "(C)2002-2003 Jaroslaw Wypychowski <jarwyp@icm.edu.pl>";
    }

    /**
   *  Help
   *
   *@return    javax.help.HelpSet or null
   */
    public HelpSet getHelpSet() {
        return null;
    }

    /**
   *  Gets the JPAItem attribute of the lsMDPlugin object
   *
   *@return    The JPAItem value
   */
    public JMenuItem getJPAItem() {
        return new JMenuItem("Add Amber", 'M');
    }

    /**
   *  Gets the SettingsItem attribute of the lsMDPlugin object
   *
   *@return    The SettingsItem value
   */
    public JMenuItem getSettingsItem() {
        JMenuItem settingsItem = new JMenuItem("Amber Defaults", 'M');
        settingsItem.addActionListener(new SettingsListener());
        return settingsItem;
    }

    /**
   *  Gets the CustomMenu attribute of the lsMDPlugin object
   *
   *@return    The CustomMenu value
   */
    public JMenu getCustomMenu() {
        return null;
    }

    /**
   *  Return a new container for this plugin
   *
   *@param  parentContainer  Description of Parameter
   *@return                  new initialized instance of plugin container
   */
    public ActionContainer getContainerInstance(GroupContainer parentContainer) {
        lsMDContainer container = new lsMDContainer(parentContainer);
        container.setName("MD_job_" + counter);
        container.setScriptDirectory(lsMDDefaults.getScriptDirectory());
        container.setIcon(new ImageIcon(getIconImage()));
        counter++;
        return container;
    }

    /**
   *  Build a new panel that manages the settings of a plugin container
   *
   *@param  tCont  plugin container to build panel for
   *@return        JPAPanel that represents container settings in GUI
   */
    public JPAPanel getPanelInstance(ActionContainer tCont) {
        lsMDJPAPanel panel = new lsMDJPAPanel(client, (lsMDContainer) tCont);
        panel.setDefaults(lsMDDefaults);
        return panel;
    }

    /**
   *  Gets the iconPath attribute of the FilterPlugin object
   * <img src="doc-files/md.gif">
   *
   *@return    The iconPath value
   */
    public String getIconPath() {
        return "icm/unicore/plugins/amber/resources/md.gif";
    }

    /**
   *  Called to inititialize the plugin
   *
   *@param  client  reference to main frame
   */
    public void startPlugin() {
        this.client = (Client) client;
        lsMDDefaults = new lsMDDefaults();
    }

    /**
   *  Called when terminating
   */
    public void stopPlugin() {
    }

    /**
   *  Can provide JPAPanels for lsMDContainer
   *
   *@param  tCont  Description of Parameter
   *@return        Description of the Returned Value
   */
    public boolean canProvideJPAPanel(ActionContainer tCont) {
        boolean match = false;
        return (tCont instanceof lsMDContainer);
    }

    /**
   *  Show a dialog that configures plugin defaults
   */
    private void showDefaultDialog() {
        lsMDDefaultsDialog lsMDDefaultsDialog = new lsMDDefaultsDialog(client, lsMDDefaults);
        lsMDDefaultsDialog.show();
        client.updateDataArea();
    }

    /**
   *  Listener class for settings menu entry
   *
   *@author     rrate
   *@created    27. August 2001
   */
    private class SettingsListener implements ActionListener {

        /**
     *  Description of the Method
     *
     *@param  event  Description of Parameter
     */
        public void actionPerformed(ActionEvent event) {
            showDefaultDialog();
        }
    }
}
