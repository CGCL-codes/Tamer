package org.geoforge.guillc.action;

import org.geoforge.guillc.action.ActDlgLloAbs;

/**
 *
 * 
 */
public abstract class ActionImportAbs extends ActDlgLloAbs {

    public static final String STR_ID_PREFIX = "Import";

    protected ActionImportAbs(String strIdSuffix, String strDescriptionShortSuffix) {
        super(ActionImportAbs.STR_ID_PREFIX.toLowerCase() + strIdSuffix, ActionImportAbs.STR_ID_PREFIX + " " + strDescriptionShortSuffix);
        super.setEnabled(false);
    }
}
