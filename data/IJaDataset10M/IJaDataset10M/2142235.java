package org.geoforge.guitlc.dialog.edit;

import javax.swing.JFrame;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class GfrDlgOkMoveAbs extends GfrDlgOkAbs {

    private static final String _STR_TITLE_PREFIX = "Move";

    protected GfrDlgOkMoveAbs(JFrame frmOwner, String strTitleSuffix, int intDimW, int intDimH) {
        super(frmOwner, GfrDlgOkMoveAbs._STR_TITLE_PREFIX + " " + strTitleSuffix, intDimW, intDimH);
    }
}
