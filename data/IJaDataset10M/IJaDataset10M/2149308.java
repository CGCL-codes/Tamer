package com.apelon.beans.apelapp;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JFrame;

/**
 * AppCloser.java
 *
 *
 * Created: Thu Feb 21 09:06:14 2002
 *
 * @author <a href="mailto:mmunz@apelon.com">Matt Munz</a>
 */
public class AppCloser extends WindowAdapter {

    AppManager fManager;

    public AppCloser(AppManager newManager) {
        super();
        setManager(newManager);
    }

    public void windowClosing(WindowEvent e) {
        System.out.println("ApelCloser.windowClosing()");
        if (manager().application().isOkayToClose()) {
            Window w = e.getWindow();
            w.setVisible(false);
            w.dispose();
        }
    }

    public void windowDeiconified(WindowEvent e) {
        manager().restoreDialogs();
    }

    public void windowClosed(WindowEvent e) {
        boolean saveProps = manager().application().endApplication(e);
        manager().endApp(saveProps);
        System.exit(0);
    }

    protected AppManager manager() {
        return fManager;
    }

    protected void setManager(AppManager newManager) {
        fManager = newManager;
    }
}
