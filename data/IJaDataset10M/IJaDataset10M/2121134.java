package com.magicpwd.e.tray;

import com.magicpwd.__a.tray.ATrayAction;
import com.magicpwd._cons.ConsEnv;
import com.magicpwd._cons.LangRes;
import com.magicpwd._util.Lang;
import com.magicpwd._util.Logs;

/**
 *
 * @author Amon
 */
public class BlogAction extends ATrayAction {

    public BlogAction() {
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            Lang.showMesg(trayPtn.getMpwdPtn(), LangRes.P30F7A0F, "");
        }
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(ConsEnv.BLOGSITE));
        } catch (Exception exp) {
            Logs.exception(exp);
        }
    }

    @Override
    public void doInit(String value) {
    }

    @Override
    public void reInit(javax.swing.AbstractButton button, String value) {
    }
}
