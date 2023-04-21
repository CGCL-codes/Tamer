package ossobook2010.gui.components.settings;

import ossobook2010.gui.components.settings.panels.DatabaseSettings;
import ossobook2010.gui.components.settings.panels.LocalMySqlSettings;
import ossobook2010.gui.components.settings.panels.GeneralSettings;
import ossobook2010.gui.components.settings.panels.ForwarderSettings;
import javax.swing.ImageIcon;
import ossobook2010.Messages;
import ossobook2010.StartOssoBook;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.settings.panels.ASettingsPanel;
import ossobook2010.gui.components.settings.panels.EntryFieldsSettings;
import ossobook2010.gui.stylesheet.Images;

/**
 *
 * @author Daniel Kaltenthaler
 */
public class SettingsFrame {

    /** The JPanel which holds the setting elements.*/
    private SettingsElements settingsElements;

    /**
	 * Constructor of the SettingsFrame class.
	 *
	 * Initialise and set up the setting frame and adds its elements to the SettingsElements object.
	 *
	 * @param mainFrame
	 *		The basic MainFrame object.
	 */
    public SettingsFrame(MainFrame mainFrame) {
        this(mainFrame, false);
    }

    /**
	 * Constructor of the SettingsFrame class.
	 *
	 * Initialise and set up the setting frame and adds its elements to the SettingsElements object.
	 *
	 * @param mainFrame
	 *		The basic MainFrame object.
	 * @param showExtendedElements
	 *		<code>true</code> of the extended elements should be displayed, <code>false</code> else.
	 */
    public SettingsFrame(MainFrame mainFrame, boolean showExtendedElements) {
        settingsElements = new SettingsElements(mainFrame, "OssoBoook " + StartOssoBook.getProgramVersion() + " " + StartOssoBook.OSSOBOOK_STATUS + " - " + Messages.getString("SETTINGS"), showExtendedElements);
        String label;
        ImageIcon image;
        ASettingsPanel panel;
        label = Messages.getString("GENERAL");
        image = Images.SETTINGS_NAV_DEFAULT;
        panel = new GeneralSettings(showExtendedElements);
        settingsElements.addNavigationElement(label, image, panel);
        if (showExtendedElements) {
            label = Messages.getString("DATABASE");
            image = Images.SETTINGS_NAV_DEFAULT;
            panel = new DatabaseSettings();
            settingsElements.addNavigationElement(label, image, panel);
        }
        label = Messages.getString("FORWARDER");
        image = Images.SETTINGS_NAV_DEFAULT;
        panel = new ForwarderSettings(showExtendedElements);
        settingsElements.addNavigationElement(label, image, panel);
        label = Messages.getString("LOCAL_MYSQL");
        image = Images.SETTINGS_NAV_DEFAULT;
        panel = new LocalMySqlSettings(showExtendedElements);
        settingsElements.addNavigationElement(label, image, panel);
        label = Messages.getString("ENTRY_FIELDS");
        image = Images.SETTINGS_NAV_DEFAULT;
        panel = new EntryFieldsSettings(mainFrame);
        settingsElements.addNavigationElement(label, image, panel);
        settingsElements.setInfoText(Messages.getString("CHANGES_ARE_APPLIED_AFTER_RESTART"));
        settingsElements.setActiveElement(0);
    }

    /**
	 * Sets the settings frame visible or invisible.
	 *
	 * @param state
	 *		<code>true</code> to set it visible, <code>false</code> else.
	 */
    public void setVisible(boolean state) {
        settingsElements.setActiveElement(0);
        settingsElements.setVisible(state);
    }
}
