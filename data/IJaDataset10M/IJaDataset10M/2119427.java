package org.geoforge.guitlc.dialog.edit.panel;

import javax.swing.event.DocumentListener;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PnlContentsOkTextSave extends PnlContentsOkTextAbs {

    public PnlContentsOkTextSave(DocumentListener dlr, java.awt.event.KeyListener klr, String strWhatPrefix, String strWhatPlural, String[] strsExistingItems) {
        super();
        super._pnlSelChooserValueTarget = new PnlSelChooseValueTargetTextSave(dlr, klr, strWhatPrefix + "'s name");
        if (strsExistingItems != null && strsExistingItems.length > 0) {
            this._pnlSelDisplayValuesCurrent = new PnlSelListValuesCurrentView(strWhatPrefix, strWhatPlural, strsExistingItems);
        }
    }
}
