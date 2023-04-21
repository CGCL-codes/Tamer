package org.geoforge.guitlcgsi.frame.main.prscpdman.menubar;

import org.geoforge.guitlcgsi.frame.main.prscpdman.menu.MenFleSpcClsPrjWwdManGsi;
import java.awt.event.ActionListener;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.guitlcgsi.frame.main.prscpdman.menu.MenPrsSpcPrjWwdManGsi;
import org.geoforge.guitlcgsi.frame.main.prscpdman.menu.MenTlsSpcClsPrjWwdManGsi;
import org.geoforge.guitlcgsi.frame.main.prscpdman.menu.MenWinSpcClsPrjWwdManGsi;
import org.geoforge.guitlc.frame.main.prscpdshared.menubar.MbrSpcClsPrjWwdAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class MbrSpcClsPrjWwdManGsiAbs extends MbrSpcClsPrjWwdAbs {

    protected MbrSpcClsPrjWwdManGsiAbs(ActionListener actListener) throws Exception {
        super();
        super._menFile_ = new MenFleSpcClsPrjWwdManGsi();
        super._menPerspectives = new MenPrsSpcPrjWwdManGsi();
        super._menTools_ = new MenTlsSpcClsPrjWwdManGsi();
        super._menWindow_ = new MenWinSpcClsPrjWwdManGsi();
    }

    @Override
    protected void _find() {
        String str = "TODO: _find() in manage perspective";
        GfrOptionPaneAbs.s_showDialogInfo(null, str);
    }
}
