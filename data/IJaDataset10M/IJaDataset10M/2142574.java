package org.uweschmidt.wiimote.whiteboard.gui;

import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import org.uweschmidt.wiimote.whiteboard.WiimoteWhiteboard;
import org.uweschmidt.wiimote.whiteboard.util.BareBonesBrowserLaunch;
import org.uweschmidt.wiimote.whiteboard.util.Util;

@SuppressWarnings("serial")
public class HelpHandler {

    @Action
    public void help() {
        if (Util.INSIDE_APP_BUNDLE) {
            HelpBook.launchHelpViewer();
        } else {
            if (JOptionPane.showConfirmDialog(null, Util.getResourceMap(HelpHandler.class).getString("helpQuestion"), Util.getResourceMap(HelpHandler.class).getString("help.Action.text"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                BareBonesBrowserLaunch.openURL(WiimoteWhiteboard.getProperty("onlineHelp"));
            }
        }
    }
}
