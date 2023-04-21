package org.geoforge.guitlcgsi.frame.main.prscpdman.menu;

import org.geoforge.guitlc.frame.main.prsshared.menu.MenPrsSpcPrjAbs;
import org.geoforge.guitlc.frame.main.prsshared.menuitem.MimSwitchToPerspRun;
import org.geoforge.guitlc.frame.main.prsshared.menuitem.MimSwitchToPerspSel;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class MenPrsSpcPrjWwdManGsi extends MenPrsSpcPrjAbs {

    public MenPrsSpcPrjWwdManGsi() {
        super();
        super._mimSwitchToFirst = new MimSwitchToPerspSel();
        super._mimSwitchToSecond = new MimSwitchToPerspRun();
    }
}
