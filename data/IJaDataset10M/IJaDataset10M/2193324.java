package org.geoforge.guitlc.dialog.edit;

import java.util.logging.Logger;
import javax.swing.JFrame;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class GfrDlgOkOpenAbs extends GfrDlgOkSelectAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrDlgOkOpenAbs.class.getName());

    static {
        GfrDlgOkOpenAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    private static final String _STR_TITLE_PREFIX = "Open";

    protected GfrDlgOkOpenAbs(JFrame frmOwner, String strTitleSuffix, int intDimW, String[] strsExistingItems) {
        super(frmOwner, GfrDlgOkOpenAbs._STR_TITLE_PREFIX, strTitleSuffix, intDimW, strsExistingItems);
    }
}
