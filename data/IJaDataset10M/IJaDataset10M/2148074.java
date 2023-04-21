package org.geoforge.guillc.menuitem;

import org.geoforge.io.finder.GfrFactoryIconShared;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class MimHlpOfflineOnthisAbs extends MimHlpOfflineAbs {

    private static final long serialVersionUID = 1L;

    public static final String F_STR_WHAT = "Help on";

    protected MimHlpOfflineOnthisAbs(String strSuffix) {
        super(MimHlpOfflineOnthisAbs.F_STR_WHAT + " " + strSuffix, GfrFactoryIconShared.s_getHelpOfflineHint(GfrFactoryIconShared.INT_SIZE_SMALL));
    }
}
