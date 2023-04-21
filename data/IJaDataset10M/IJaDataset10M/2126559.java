package org.geoforge.guitlcolg.frame.main.prsrun.tabbedpane;

import org.geoforge.guitlcolg.frame.main.prsrun.panel.PnlSpcClsPrjRunGtrOlgDskPrd;
import org.geoforge.guitlcolg.frame.main.prsrun.panel.PnlSpcClsPrjRunGtrOlgSynPrd;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class TabSpcClsPrjRunOlgEqp extends TabSpcClsPrjRunOlgAbs {

    public TabSpcClsPrjRunOlgEqp() throws Exception {
        super();
        super._pnlDesktopViewers = new PnlSpcClsPrjRunGtrOlgDskPrd();
        super._pnlSynchroViewers = new PnlSpcClsPrjRunGtrOlgSynPrd();
        super._altPanels_.add(super._pnlWelcome);
    }
}
