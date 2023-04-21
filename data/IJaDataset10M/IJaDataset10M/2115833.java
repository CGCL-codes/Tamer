package org.geoforge.guitlc.dialog.mainframe.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.geoforge.guillc.panel.PnlAbs;
import org.geoforge.io.finder.GfrFactoryIconAbs;
import org.geoforge.io.finder.GfrFactoryIconShared;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class PnlConfirmExit extends PnlAbs {

    private static String _STR_WHAT_ = "Do you really want to exit?";

    private static ImageIcon _IIN_ = GfrFactoryIconShared.s_getGeoforge(GfrFactoryIconAbs.INT_SIZE_XLARGE);

    public PnlConfirmExit() {
        super();
        this._lblWhat_ = new JLabel(_STR_WHAT_);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        this._lblWhat_.setIcon(_IIN_);
        this.setPreferredSize(new Dimension(250, 80));
        this.add(this._lblWhat_, BorderLayout.CENTER);
        return true;
    }

    @Override
    public void loadTransient() throws Exception {
    }

    @Override
    public void releaseTransient() throws Exception {
    }

    private JLabel _lblWhat_;
}
