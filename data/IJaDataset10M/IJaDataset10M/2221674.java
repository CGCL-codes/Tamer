package org.geoforge.guitlc.dialog.edit.data.panel;

import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import org.geoforge.guitlc.dialog.edit.panel.PnlContentsOkTextNewSettingsAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class PnlContentsOkTextNewSettingsTlcAbs extends PnlContentsOkTextNewSettingsAbs {

    public String getValueDescShort() {
        return ((PnlSettingsNewTloAbs) super._pnlSettings).getValueDescShort();
    }

    public String getValueUrl() {
        return ((PnlSettingsNewTloAbs) super._pnlSettings).getValueUrl();
    }

    protected PnlContentsOkTextNewSettingsTlcAbs(String strWhat, String strWhatPlural, DocumentListener dlr, java.awt.event.KeyListener klr, String[] strsExistingItems) {
        super(dlr, klr, strWhat, strWhatPlural, strsExistingItems);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        JTextField tfd = super._pnlSelChooserValueTarget.getTextField();
        ((PnlSettingsNewTloAbs) super._pnlSettings).setSizeWidthScrollText(tfd.getPreferredSize().width);
        return true;
    }
}
