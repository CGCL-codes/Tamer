package net.rptools.inittool.ui.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import net.rptools.chartool.model.db.PropertyTableXML;
import net.rptools.chartool.model.property.PropertyDescriptor;
import net.rptools.chartool.model.property.PropertyDescriptorSet;
import net.rptools.chartool.model.property.PropertySettings;
import net.rptools.chartool.ui.component.FileSelectorController;
import net.rptools.chartool.ui.component.ImageStorageType;
import net.rptools.chartool.ui.component.MultiScriptController;
import net.rptools.chartool.ui.component.RPIconFactory;
import net.rptools.chartool.ui.component.TabbedPaneController;
import net.rptools.inittool.model.InitToolGameSettings;
import net.rptools.inittool.ui.InitToolFrame;
import net.rptools.lib.FileUtil;
import net.rptools.lib.GUID;
import com.jeta.forms.components.panel.FormPanel;

/**
 * Controller for the miscellaneous settings controller.
 * 
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
public class MiscGameSettingsController extends TabbedPaneController implements ActionListener, ChangeListener {

    /**
   * Working combatant lookup file.
   */
    private String workingCombatantLookup;

    /**
   * Controller used to edit all of the scripts
   */
    private MultiScriptController scripts;

    /**
   * The controller for the file selector components
   */
    private FileSelectorController fileSelector;

    /**
   * Frame containing the settings file
   */
    private GameSettingsFrame frame;

    /**
   * Text field containing the game name. 
   */
    private static final String GAME_NAME = "gameName";

    /**
   * Check box for the init every round flag. 
   */
    private static final String INIT_EVERY_ROUND = "initEveryRound";

    /**
   * Text field containing the initiative display property. 
   */
    private static final String INIT_DISPLAY_PROP = "initDisplayProp";

    /**
   * Text field containing the initiative display property. 
   */
    private static final String MODIFIER_DISPLAY_PROP = "modDisplayProp";

    /**
   * Text field containing the combatant file name. 
   */
    private static final String COMBATANT_LOOKUP_FILE = "combatantLookup";

    /**
   * Button used to find a combatant lookup file. 
   */
    private static final String LOOKUP_BUTTON = "lookupButton";

    /**
   * Radio button used to indicate that a new file is being supplied. 
   */
    private static final String NEW_FILE_FLAG = "newFile";

    /**
   * Radio button used to indicate that the existing file is to be used for combatant lookup. 
   */
    private static final String EXISTING_FILE_FLAG = "existingFile";

    /**
   * Radio button used to indicate that a new file is being supplied. 
   */
    private static final String NO_FILE_FLAG = "noFile";

    /**
   * The script components used by the multiple script controller
   */
    private static final String[] FILE_SELECTOR_COMPONENTS = { COMBATANT_LOOKUP_FILE, LOOKUP_BUTTON };

    /**
   * Button used to reload the current button. 
   */
    private static final String RELOAD_BUTTON = "reload";

    /**
   * Combo box that lists all of the supported game level events.. 
   */
    private static final String SCRIPT_EVENT_COMBO_BOX = "event";

    /**
   * Text area containing the short description 
   */
    private static final String SCRIPT_DESCRIPTION = "description";

    /**
   * JEditor Pane that shows the notes HTML
   */
    private static final String SCRIPT_NOTES = "notes";

    /**
   * Text area used to edit the scripts. 
   */
    private static final String SCRIPT = "script";

    /**
   * The script components used by the multiple script controller
   */
    private static final String[] SCRIPT_COMPONENTS = { SCRIPT_EVENT_COMBO_BOX, SCRIPT_DESCRIPTION, SCRIPT, SCRIPT_NOTES };

    /**
   * The default file type for data files.
   */
    public static final String DATA_FILE_TYPE = ".rpdat";

    /**
   * The file filter used when saving and opening data files 
   */
    public static final FileFilter DATA_FILE_FILTER = new FileFilter() {

        @Override
        public String getDescription() {
            return "RPTools Data Files (*" + DATA_FILE_TYPE + ")";
        }

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) return true;
            String name = file.getName();
            if (name.endsWith(DATA_FILE_TYPE)) return true;
            return false;
        }
    };

    /**
   * Logger instance for this class.
   */
    private static final Logger LOGGER = Logger.getLogger(MiscGameSettingsController.class.getName());

    /**
   * Set the view for this new controller
   * 
   * @param aView The view containing the game settings panels.
   */
    public MiscGameSettingsController(GameSettingsFrame aView) {
        super(aView, new FormPanel("net/rptools/inittool/resources/forms/miscSettings.jfrm"));
        frame = aView;
        getPanel().getButton(RELOAD_BUTTON).addActionListener(this);
        getPanel().getRadioButton(NEW_FILE_FLAG).addActionListener(this);
        getPanel().getRadioButton(NO_FILE_FLAG).addActionListener(this);
        getPanel().getRadioButton(EXISTING_FILE_FLAG).addActionListener(this);
        scripts = new MultiScriptController(getPanel(), SCRIPT_COMPONENTS, frame.getInitToolSettings().getEventScripts());
        fileSelector = new FileSelectorController(getPanel(), FILE_SELECTOR_COMPONENTS, true, DATA_FILE_FILTER) {

            @Override
            protected boolean validateFile(String file) {
                URL url = null;
                try {
                    url = new URL(file);
                } catch (MalformedURLException e) {
                    LOGGER.log(Level.WARNING, "The URL '" + file + "' is not formatted properly.", e);
                    JOptionPane.showMessageDialog(getPanel(), "The URL '" + file + "' is not formatted properly.");
                    return false;
                }
                boolean valid = PropertyTableXML.getInstance().validateXmlFile(url);
                if (valid) workingCombatantLookup = file;
                return valid;
            }
        };
        aView.getTabbedPane().addChangeListener(this);
    }

    /**
   * Check to see what fields have changed, validate them and optionally save the fields to the game settings.  
   * 
   * @param commit When this is <code>true</code> update the game settings.
   * @return Flag indicating that the data has changed.
   */
    private boolean saveSettings(boolean commit) {
        InitToolGameSettings settings = frame.getInitToolSettings();
        boolean change = false;
        String gameName = getString(GAME_NAME);
        if (gameName == null) {
            throw new IllegalStateException("A game name is required but none was set.");
        }
        if (!gameName.equals(settings.getGameName())) change = true;
        URL url = null;
        if (getPanel().getRadioButton(NEW_FILE_FLAG).isSelected()) {
            try {
                url = fileSelector.getUrl();
                change = true;
            } catch (MalformedURLException e) {
                throw new IllegalStateException("The new url is invalid.");
            }
        }
        String initDisplay = getPanel().getText(INIT_DISPLAY_PROP);
        if (initDisplay == null || (initDisplay = initDisplay.trim()).length() == 0) initDisplay = null;
        checkPropertyName(initDisplay, "The initiative display property");
        if (initDisplay != settings.getInitDisplayProperty() && (initDisplay == null || !initDisplay.equals(settings.getInitDisplayProperty()))) change = true;
        String modDisplay = getPanel().getText(MODIFIER_DISPLAY_PROP);
        if (modDisplay == null || (modDisplay = modDisplay.trim()).length() == 0) modDisplay = null;
        checkPropertyName(modDisplay, "The initiative modifier display property");
        if (modDisplay != settings.getInitModifierDisplayProperty() && (modDisplay == null || !modDisplay.equals(settings.getInitModifierDisplayProperty()))) change = true;
        scripts.flushScript(null);
        change |= scripts.isChanged();
        change |= getPanel().getBoolean(INIT_EVERY_ROUND) != settings.isInitEachRound();
        if (!commit) return change;
        if (url != null) {
            try {
                File file = new File(System.getProperty("java.io.tmpdir") + File.separator + new GUID() + ".rpdat");
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                InputStream is = url.openStream();
                FileUtil.copy(is, os);
                frame.getSettingsFile().importCombatantLookupFile(PropertySettings.getInstance().getDatabaseName(), file);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to copy the url file: " + url.toExternalForm(), e);
                throw new IllegalArgumentException("Unable to copy URL " + url.toExternalForm());
            }
        } else if (getPanel().getRadioButton(NO_FILE_FLAG).isSelected() && settings.isCombatantLookupAvailable()) {
            frame.getSettingsFile().removeCombatantLookupFile(PropertySettings.getInstance().getDatabaseName());
        }
        settings.getCustomPropertySet().setGame(gameName);
        settings.setInitEachRound(getPanel().getBoolean(INIT_EVERY_ROUND));
        settings.setInitDisplayProperty(initDisplay);
        settings.setInitModifierDisplayProperty(modDisplay);
        workingCombatantLookup = null;
        scripts.saveScripts(settings.getEventScripts());
        return change;
    }

    /**
   * Check a property name to make sure it is valid.
   * 
   * @param propName Optional property name. The <code>null</code> value is considered valid.
   * @param message Message displayed on error.
   * @throws IllegalStateException Invalid property name
   */
    private void checkPropertyName(String propName, String message) {
        if (propName != null) {
            CustomPropertyController cpc = (CustomPropertyController) getView().getController(CustomPropertyController.CUSTOM_PROPERTIES_PANEL_NAME);
            PropertyDescriptor pd = PropertyDescriptorSet.get(propName, cpc.getProperties());
            if (pd == null) {
                throw new IllegalStateException(message + " '" + propName + "' is not a valid custom property.");
            }
        }
    }

    /**
   * A change has occurred to the list of properties. Make sure my property is still valid.
   * 
   * @param fieldName Name of the field being checked.
   */
    private void changedProperties(String fieldName) {
        String initDisplay = getPanel().getText(fieldName);
        if (initDisplay == null || (initDisplay = initDisplay.trim()).length() == 0) initDisplay = null;
        CustomPropertyController cpc = (CustomPropertyController) getView().getController(CustomPropertyController.CUSTOM_PROPERTIES_PANEL_NAME);
        PropertyDescriptor pd = initDisplay == null ? null : PropertyDescriptorSet.get(initDisplay, cpc.getProperties());
        if (pd != null) {
            getPanel().setText(fieldName, initDisplay);
        } else {
            getPanel().setText(fieldName, null);
        }
    }

    /**
   * Check the the init and modifier property display names
   * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
   */
    public void stateChanged(ChangeEvent aE) {
        if (getView().getTabbedPane().getSelectedComponent() == getPanel() || aE == null) {
            changedProperties(INIT_DISPLAY_PROP);
            changedProperties(MODIFIER_DISPLAY_PROP);
        }
    }

    /**
   * @see net.rptools.chartool.ui.component.TabbedPaneController#flushPanel()
   */
    @Override
    public void flushPanel() {
        if (saveSettings(false)) setChanged(true);
    }

    /**
   * @see net.rptools.chartool.ui.component.TabbedPaneController#setFields()
   */
    @Override
    public void setFields() {
        InitToolGameSettings settings = frame.getInitToolSettings();
        getPanel().setText(GAME_NAME, settings.getGameName());
        getPanel().getCheckBox(INIT_EVERY_ROUND).setSelected(settings.isInitEachRound());
        getPanel().setText(INIT_DISPLAY_PROP, settings.getInitDisplayProperty());
        getPanel().getRadioButton(EXISTING_FILE_FLAG).setEnabled(settings.isCombatantLookupAvailable());
        getPanel().getTextField(COMBATANT_LOOKUP_FILE).setEnabled(false);
        getPanel().getButton(LOOKUP_BUTTON).setEnabled(false);
        if (settings.isCombatantLookupAvailable()) {
            getPanel().getRadioButton(EXISTING_FILE_FLAG).setSelected(true);
            getPanel().getButton(RELOAD_BUTTON).setEnabled(true);
        } else {
            getPanel().getRadioButton(NO_FILE_FLAG).setSelected(true);
            getPanel().getButton(RELOAD_BUTTON).setEnabled(true);
        }
        getPanel().setText(COMBATANT_LOOKUP_FILE, null);
        scripts.setScriptsOwner(settings.getEventScripts());
    }

    /**
   * @see net.rptools.chartool.ui.component.TabbedPaneController#saveSettings()
   */
    @Override
    public void saveSettings() {
        saveSettings(true);
    }

    /**
   * @see net.rptools.chartool.ui.component.TabbedPaneController#getName()
   */
    @Override
    public String getName() {
        return "IT Miscellaneous";
    }

    /**
   * @see net.rptools.chartool.ui.component.TabbedPaneController#getToolTip()
   */
    @Override
    public String getToolTip() {
        return "Set the game name and other miscellaneous properties";
    }

    /**
   * @see net.rptools.chartool.ui.component.TabbedPaneController#getMnemonic()
   */
    @Override
    public int getMnemonic() {
        return KeyEvent.VK_I;
    }

    /**
   * @see net.rptools.chartool.ui.component.TabbedPaneController#getIcon()
   */
    @Override
    public Icon getIcon() {
        return RPIconFactory.getInstance().get(ImageStorageType.createDescriptor(InitToolFrame.LOGO_ICON_NAME), 16, 16);
    }

    /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == getPanel().getRadioButton(NEW_FILE_FLAG) || event.getSource() == getPanel().getRadioButton(NO_FILE_FLAG) || event.getSource() == getPanel().getRadioButton(EXISTING_FILE_FLAG)) {
            if (getPanel().getBoolean(NO_FILE_FLAG) || getPanel().getBoolean(EXISTING_FILE_FLAG)) {
                getPanel().getButton(LOOKUP_BUTTON).setEnabled(false);
                getPanel().getTextComponent(COMBATANT_LOOKUP_FILE).setEnabled(false);
                getPanel().setText(COMBATANT_LOOKUP_FILE, null);
            } else if (getPanel().getBoolean(NEW_FILE_FLAG)) {
                getPanel().getTextComponent(COMBATANT_LOOKUP_FILE).setEnabled(true);
                getPanel().getButton(LOOKUP_BUTTON).setEnabled(true);
                getPanel().setText(COMBATANT_LOOKUP_FILE, workingCombatantLookup);
            }
            if (!getPanel().getBoolean(EXISTING_FILE_FLAG)) {
                getPanel().getButton(RELOAD_BUTTON).setEnabled(false);
            }
        }
    }
}
