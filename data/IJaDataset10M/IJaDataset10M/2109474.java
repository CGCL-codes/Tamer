package jgnash.ui.components.wizard;

import java.util.Map;

/**
 * Interface for a task page in a wizard dialog
 *
 * @author Craig Cavanaugh
 * @version $Id: WizardPage.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
public interface WizardPage {

    boolean isPageValid();

    /**
     * toString must return a valid description for this page that will
     * appear in the task list of the WizardDialog
     *
     * @return page description
     */
    @Override
    String toString();

    /**
     * Called after a page has been made active.  The page
     * can load predefined settings/preferences when called.
     *
     * @param map preferences are accessible here
     */
    void getSettings(Map<Enum<?>, Object> map);

    /**
     * Called on the active prior to switching to the next page.  The page
     * can save settings/preferences to be used later
     *
     * @param map place to put default preferences
     */
    void putSettings(Map<Enum<?>, Object> map);
}
