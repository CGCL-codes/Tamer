package uk.ac.essex.common.pref;

import java.util.Set;

/**
 *
 * <br>
 * Date: 15-Jul-2002 <br>
 * 
 * @author Laurence Smith
 * 
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public interface PreferenceObject {

    /**
     * Sets the preference of the given name
     * @param preferenceName - The pref to set
     * @param value - The value to set it
     */
    public void setPreference(String preferenceName, Object value);

    /**
     * Gets the preference of the given name
     * @param preferenceName - The pref to set
     * @return Object - The value of the pref
     */
    public Object getPreference(String preferenceName);

    /**
     *
     * @return
     */
    public Set getPreferenceNames();

    /**
     *
     * @param preferenceName
     * @return
     */
    public Class getType(String preferenceName);
}
