package org.geoforge.guitlcgsi.frame.secrun.menubar;

import org.geoforge.guitlc.frame.secrun.menubar.MbrWindowViewerAbs;
import org.geoforge.guitlcgsi.frame.secrun.menu.*;
import java.awt.event.ActionListener;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class MbrWindowViewerGsiGlobeGlg extends MbrWindowViewerAbs {

    public MbrWindowViewerGsiGlobeGlg(ActionListener actListenerController, ActionListener actListenerFrame) throws Exception {
        super();
        super._menFile_ = new MenFileWinViewGsiGlobeGlg(actListenerController);
        super._menView_ = new MenViewWinViewGsiGlobeGlg(actListenerFrame);
        super._menTools_ = new MenToolsWinViewGsiGlobeGlg(actListenerController);
        super._menWindow_ = new MenWindowWinViewGsiGlobeGlg(actListenerController);
        super._menHelp_ = new MenHelpWinViewGsiGlobeGlg();
    }
}
