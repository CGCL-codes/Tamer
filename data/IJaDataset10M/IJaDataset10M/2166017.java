package org.geoforge.guitlcolg.dialog.edit.data.oxp.panel;

import javax.swing.event.DocumentListener;
import org.geoforge.guitlc.dialog.edit.data.panel.PnlContentsOkTextNewSettingsTloLclAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PnlContentsOkTextNewSettingsTlcOlgSurvey2d extends PnlContentsOkTextNewSettingsTloLclAbs {

    private static final String _STR_WHAT_PLURAL = "Profiles";

    public PnlContentsOkTextNewSettingsTlcOlgSurvey2d(String strWhat, DocumentListener dlr, java.awt.event.KeyListener klr, String[] strsExistingItems) {
        super(strWhat, PnlContentsOkTextNewSettingsTlcOlgSurvey2d._STR_WHAT_PLURAL, dlr, klr, strsExistingItems);
        super._pnlSettings = new PnlSettingsNewTloOlgSurvey2d(dlr);
    }
}
