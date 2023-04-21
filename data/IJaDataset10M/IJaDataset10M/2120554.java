package com.pcmsolutions.system;

import com.pcmsolutions.gui.UserMessaging;
import com.pcmsolutions.gui.FlashMsg;
import com.pcmsolutions.gui.ZoeosFrame;
import javax.swing.*;

/**
 * User: paulmeehan
 * Date: 26-Mar-2004
 * Time: 15:18:09
 */
public class SystemErrors {

    public static void internal(Object e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                UserMessaging.showError("ZoeOS has encountered an internal error. Please restart the application.");
            }
        });
        System.out.println(e);
    }

    public static void flashWarning(String msg) {
        new FlashMsg(ZoeosFrame.getInstance(), ZoeosFrame.getInstance(), 2000, 500, FlashMsg.colorWarning, msg);
    }
}
