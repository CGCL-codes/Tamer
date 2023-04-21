package org.fudaa.fudaa.taucomac;

import com.memoire.bu.BuPreferences;

/**
 * Preferences pour Taucomac.
 *
 * @version      $Revision: 1.7 $ $Date: 2006-09-19 15:08:56 $ by $Author: deniger $
 * @author       Jean-Yves Riou 
 */
public class TaucomacPreferences extends BuPreferences {

    public static final TaucomacPreferences TAUCOMAC = new TaucomacPreferences();

    public void applyOn(final Object _o) {
        if (!(_o instanceof TaucomacImplementation)) {
            throw new RuntimeException("" + _o + " is not a TaucomacImplementation.");
        }
    }
}
