package uk.ac.essex.common.gui;

import uk.ac.essex.common.pref.PreferenceObject;
import javax.swing.*;
import java.util.prefs.Preferences;
import java.util.Set;

/**
 * An pref handler looks after any available pref for a GUI application. This means providing access
 * to a gui to allow users to update pref. These must then be stored somehow. This could either be through
 * the new Java 1.4 Preferences API, or some custom method.
 * <br>
 * Date: 15-Jul-2002 <br>
 * 
 * @author Laurence Smith
 * 
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public interface OptionsHandler {

    /**
     * Gets all the panels this gui needs to set its preferences
     * @return
     */
    public JPanel getPreferencePanel();

    /**
     * This class manages a viewers preferences so this preference object should be used to
     * write preferences
     * @return
     */
    public Preferences getPreferences();

    /**
     * Save the pref for this manager
     */
    public void saveOptions();

    /**
     *
     * @return
     */
    public PreferenceObject readOptions();
}
