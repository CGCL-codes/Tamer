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
public abstract class GfrDlgOkImportFileAbs extends GfrDlgOkImportAbs {

    protected GfrDlgOkImportFileAbs(JFrame frmOwner, String strTitleSuffix, int intDimW, int intDimH, String[] strsExistingItems) {
        super(frmOwner, strTitleSuffix, intDimW, intDimH, strsExistingItems);
    }
}
