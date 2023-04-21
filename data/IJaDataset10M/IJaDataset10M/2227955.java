package kenwudi.java.GUI.systray;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import kenwudi.java.GUI.BalloonWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import snoozesoft.systray4j.SysTrayMenuListener;

public class SysTrayController {

    private SysTray tray;

    private JFrame parent;

    public SysTrayController(Shell parent) {
        tray = new SysTray();
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.setSize(150, 100);
        shell.setVisible(true);
        SysTrayController control = new SysTrayController(shell);
    }

    public void setIcon(int icons) {
        tray.setIcon(tray.icons[icons]);
    }

    public void setStatus(int status) {
        System.out.println(status);
        switch(status) {
            case 1:
                tray.itmChk.setEnabled(false);
                tray.itmSearch.setEnabled(false);
                tray.itmLogout.setEnabled(false);
                tray.itmLogin.setEnabled(true);
                setIcon(1);
                break;
            case 0:
                tray.itmChk.setEnabled(true);
                tray.itmLogout.setEnabled(true);
                tray.itmLogin.setEnabled(false);
                setIcon(0);
                break;
            case 3:
                tray.itmChk.setEnabled(false);
                tray.itmSearch.setEnabled(false);
                tray.itmLogout.setEnabled(false);
                tray.itmLogin.setEnabled(false);
                setIcon(3);
                break;
            default:
        }
    }

    public void addSysTrayMenuListener(SysTrayMenuListener l) {
        for (int i = 0; i < tray.icons.length; i++) tray.icons[i].addSysTrayMenuListener(l);
        tray.itmChk.addSysTrayMenuListener(l);
        tray.itmAbout.addSysTrayMenuListener(l);
        tray.itmExit.addSysTrayMenuListener(l);
        tray.itmLogin.addSysTrayMenuListener(l);
        tray.itmLogout.addSysTrayMenuListener(l);
        tray.itmOpenBrowser.addSysTrayMenuListener(l);
    }

    public void setToolTip(final String message) {
        Runnable r = new Runnable() {

            public void run() {
                tray.setToolTip(message);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    public String getToolTip() {
        return tray.getToolTip();
    }

    public boolean isAvaliable() {
        return tray.isAvailable();
    }
}
