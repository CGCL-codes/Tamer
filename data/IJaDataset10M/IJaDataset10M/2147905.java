package org.geoforge.guitlcgsi.frame.main.prsrun.menu;

import java.awt.event.ActionListener;
import org.geoforge.guitlc.frame.main.prsrun.menu.MenViwSpcClsPrjRunAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class MenViwSpcClsPrjRunGsiGlg extends MenViwSpcClsPrjRunAbs {

    public MenViwSpcClsPrjRunGsiGlg(ActionListener actListener) {
        super(actListener);
        super._menViewers = new MenViewersPrjRunGsiGlg();
    }
}
