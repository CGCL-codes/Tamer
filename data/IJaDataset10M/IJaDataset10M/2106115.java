package org.geoforge.guitlc.dialog.edit.panel;

import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.geoforge.guitlc.dialog.edit.textfield.TfdCheckableAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class PnlSelEditTfdMultipleAbs extends PnlSelEditTfdAbs {

    @Override
    public String getWrongFormat() {
        for (int i = 0; i < super._pnl_.getComponentCount(); i++) {
            Component cmpCur = super._pnl_.getComponent(i);
            if (!(cmpCur instanceof TfdCheckableAbs)) continue;
            TfdCheckableAbs obj = (TfdCheckableAbs) cmpCur;
            String strWrong = obj.getWrongFormat();
            if (strWrong != null) return strWrong;
        }
        return null;
    }

    @Override
    public boolean isOk() {
        for (int i = 0; i < super._pnl_.getComponentCount(); i++) {
            Component cmpCur = super._pnl_.getComponent(i);
            if (!(cmpCur instanceof TfdCheckableAbs)) continue;
            TfdCheckableAbs obj = (TfdCheckableAbs) cmpCur;
            if (!obj.isOk()) return false;
        }
        return true;
    }

    @Override
    public boolean belongsTo(Document doc) {
        for (int i = 0; i < super._pnl_.getComponentCount(); i++) {
            Component cmpCur = super._pnl_.getComponent(i);
            if (!(cmpCur instanceof JTextField)) continue;
            JTextField obj = (JTextField) cmpCur;
            if (obj.getDocument() == doc) return true;
        }
        return false;
    }

    protected PnlSelEditTfdMultipleAbs(String strLabel, boolean blnFiedRequired) {
        super(strLabel, blnFiedRequired);
    }
}
