package view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

public class JTresWindowListener implements WindowListener {

    private JFrame activeFrame;

    public JTresWindowListener(JFrame activeFrame) {
        this.activeFrame = activeFrame;
    }

    public void windowActivated(WindowEvent e) {
        this.requestFocus();
    }

    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
        this.requestFocus();
    }

    public void windowOpened(WindowEvent e) {
        this.requestFocus();
    }

    private void requestFocus() {
        this.activeFrame.requestFocus();
    }
}
