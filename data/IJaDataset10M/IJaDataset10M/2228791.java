package org.charvolant.tmsnet.ui;

import org.charvolant.tmsnet.TMSClientPreferences;
import org.charvolant.tmsnet.i18n.TMSUIBundle;
import org.charvolant.tmsnet.util.ResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A panel that shows the preferences available and allows them to be modified.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class PreferencesPanel extends InformationPanel<TMSClientPreferences> {

    /** The logger for this class */
    private static final Logger logger = LoggerFactory.getLogger(PreferencesPanel.class);

    /**
   * Construct a preferences UI.
   *
   * @param preferences The current preferences instance
   * @param resources The resource manager
   * @param parent The parent shell
   * @param style The SWT style
   * 
   * @throws Exception if unable to build the panel
   */
    public PreferencesPanel(TMSClientPreferences preferences, ResourceManager resources, Composite parent, int style) throws Exception {
        super(preferences, false, resources, parent, style);
    }

    /**
   * @{inheritDoc}
   * <p>
   * Put all the values in the instance variables back into the preferences.
   *
   * @see org.charvolant.tmsnet.ui.Dialogable#processOk()
   */
    @Override
    public void processOk() {
        try {
            super.processOk();
            this.model.save();
        } catch (Exception ex) {
            this.logger.error("Unable to save preferences", ex);
        }
    }

    /**
   * {@inheritDoc}
   * <p>
   * Reload from the preferences.
   * 
   * @see org.charvolant.tmsnet.ui.AbstractModelPanel#processClose()
   */
    @Override
    public void processClose() {
        try {
            super.processClose();
            this.model.load();
        } catch (Exception ex) {
            this.logger.error("Unable to reload preferences", ex);
        }
    }

    /**
   * Check the preferences before closing.
   *
   * @return
   * 
   * @see org.charvolant.tmsnet.ui.AbstractModelPanel#errorCheck()
   */
    @Override
    public String errorCheck() {
        if (!this.model.isValidServer()) return TMSUIBundle.ERROR_SERVER;
        return super.errorCheck();
    }

    /** 
   * The properties that this panel displays
   *
   * @return The property list
   * 
   * @see org.charvolant.tmsnet.ui.InformationPanel#getProperties()
   */
    @Override
    protected String[] getProperties() {
        return new String[] { "server", "port", "save", "update", "epgDaysBefore", "epgDaysAfter", "updateInterval", "pvrTimeZone", "locale", "showPreferencesOnStart", "connectOnStart" };
    }
}
