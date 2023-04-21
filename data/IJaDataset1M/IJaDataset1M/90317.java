package org.aiotrade.platform.core.ui.netbeans.actions;

import java.net.MalformedURLException;
import java.net.URL;
import org.openide.ErrorManager;
import org.openide.awt.HtmlBrowser.URLDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author Caoyuan Deng
 */
public class OnlineHelpAction extends CallableSystemAction {

    /** Creates a new instance
     */
    public OnlineHelpAction() {
    }

    public void performAction() {
        try {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    try {
                        String urlStr = "https://humaitrader.dev.java.net/manual_user/usermanual.html";
                        URLDisplayer.getDefault().showURL(new URL(urlStr));
                    } catch (MalformedURLException mue) {
                        ErrorManager.getDefault().notify(mue);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    public String getName() {
        return "Online Help";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected boolean asynchronous() {
        return false;
    }
}
