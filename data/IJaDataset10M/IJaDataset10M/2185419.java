package ntorrent.trayicon;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import ntorrent.NtorrentApplication;
import org.java.plugin.Plugin;

public class TrayIconHandler extends Plugin implements WindowListener, MouseListener {

    TrayIcon trayIcon;

    SystemTray tray = SystemTray.getSystemTray();

    JFrame window;

    @Override
    protected void doStart() throws Exception {
        if (java.awt.SystemTray.isSupported()) {
            window = NtorrentApplication.MAIN_WINDOW;
            Image image = Toolkit.getDefaultToolkit().getImage("plugins/ntorrent/icons/ntorrent48.png");
            trayIcon = new TrayIcon(image, NtorrentApplication.APP_NAME);
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
            window.addWindowListener(this);
            trayIcon.addMouseListener(this);
        } else {
        }
    }

    @Override
    protected void doStop() throws Exception {
        tray.remove(trayIcon);
        window.removeWindowListener(this);
    }

    public void windowDeiconified(WindowEvent e) {
        e.getWindow().setVisible(true);
    }

    public void windowIconified(WindowEvent e) {
        e.getWindow().setVisible(false);
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        window.setVisible(true);
        window.setExtendedState(JFrame.NORMAL);
        window.requestFocus();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
