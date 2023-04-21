package net.sourceforge.pmd.runtime.preferences;

/**
 * Factory for all the preferences package objects
 * 
 * @author Herlin
 * @version $Revision$
 * 
 * $Log$
 * Revision 1.1  2006/05/22 21:37:36  phherlin
 * Refactor the plug-in architecture to better support future evolutions
 *
 *
 */
public interface IPreferencesFactory {

    /**
     * @return the instance of the preferences manager
     */
    IPreferencesManager getPreferencesManager();

    /**
     * Return a new instance, not intialized, of a preferences information structure for the
     * specified preferences manager. 
     * @param prefencesManager a instance of a preferencs manager
     */
    IPreferences newPreferences(IPreferencesManager prefencesManager);
}
