package org.fudaa.fudaa.dimduc;

import com.memoire.bu.BuPreferences;
import org.fudaa.fudaa.commun.FudaaAstuces;
import org.fudaa.fudaa.commun.FudaaAstucesAbstract;

/**
 * @version      $Revision: 1.9 $ $Date: 2007-01-19 13:14:38 $ by $Author: deniger $
 * @author       Axel von Arnim 
 */
public class DimducAstuces extends FudaaAstucesAbstract {

    public static DimducAstuces DIMDUC = new DimducAstuces();

    protected FudaaAstucesAbstract getParent() {
        return FudaaAstuces.FUDAA;
    }

    protected BuPreferences getPrefs() {
        return DimducPreferences.DIMDUC;
    }
}
