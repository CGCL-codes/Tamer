package org.geoforge.guitlc.dialog.tabs.settings.panel;

import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import org.geoforge.guillc.textfield.TfdTextLarge;

/**
 *
 * @author bantchao
 */
public class PnlKeyValueTfdLarge extends PnlKeyValueAbs {

    public PnlKeyValueTfdLarge(String strKey, String strValue) {
        super(strKey);
        super._objText = new TfdTextLarge((String) null, (DocumentListener) null);
        ((JTextField) super._objText).setText(strValue);
        ((JTextField) super._objText).setEditable(false);
    }
}
