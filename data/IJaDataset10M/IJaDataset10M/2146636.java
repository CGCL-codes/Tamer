package org.geoforge.guitlc.dialog.panel;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import org.geoforge.guillc.label.GfrLbl;
import org.geoforge.guillc.panel.GfrPnl;
import org.geoforge.guillc.panel.PnlAbs;
import org.geoforge.guitlc.dialog.edit.label.LblWarning;
import org.geoforge.io.finder.GfrFactoryIconAbs;
import org.geoforge.io.finder.GfrFactoryIconShared;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class PnlProblem extends PnlAbs {

    public PnlProblem() {
        this._lblStatus_ = new GfrLbl();
        this._iin_ = GfrFactoryIconShared.s_getWarningFieldErrorDialog(GfrFactoryIconAbs.INT_SIZE_SMALL);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._lblStatus_.init()) return false;
        this._lblStatus_.setForeground(LblWarning.COL_FG);
        GfrPnl pnl = new GfrPnl();
        pnl.init();
        pnl.setLayout(new BorderLayout());
        pnl.add(this._lblStatus_, BorderLayout.WEST);
        this.add(pnl);
        return true;
    }

    public boolean isOk() {
        String str = this._lblStatus_.getText();
        if ("".equals(str)) {
            return true;
        }
        return false;
    }

    public void showMessage(String strMessage) {
        this._lblStatus_.setText(strMessage);
        this._lblStatus_.setIcon(this._iin_);
    }

    public void makeSpace() {
        this._lblStatus_.setText("");
        this._lblStatus_.setIcon(GfrFactoryIconShared.s_getOptionalFieldDialog(GfrFactoryIconAbs.INT_SIZE_SMALL));
    }

    public void hideMessage() {
        this._lblStatus_.setText("");
        this._lblStatus_.setIcon(null);
    }

    @Override
    public void loadTransient() throws Exception {
    }

    @Override
    public void releaseTransient() throws Exception {
    }

    private ImageIcon _iin_;

    private GfrLbl _lblStatus_;
}
