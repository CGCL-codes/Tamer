package com.cronopista.lightpacker.GUI;

import instresources.InstRes;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import com.cronopista.lightpacker.Main;
import com.cronopista.lightpacker.steps.Installer;

/**
 * @author Eduardo Rodr�guez
 * 
 */
public class MainFrame extends JFrame implements WindowListener {

    private static final long serialVersionUID = 4937896801071581340L;

    public MainFrame() {
        UIManager.put("OptionPane.yesButtonText", Main.getInstance().translate("general.yes"));
        UIManager.put("OptionPane.noButtonText", Main.getInstance().translate("general.no"));
        UIManager.put("OptionPane.cancelButtonText", Main.getInstance().translate("general.cancel"));
        if (!Installer.getInstance().getInstallerIcon().equals("none")) {
            setIconImage(new ImageIcon(InstRes.class.getResource(Installer.getInstance().getInstallerIcon())).getImage());
        }
        setTitle(Main.getInstance().translate("installer.title"));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dim.width - Installer.getInstance().getWidth()) / 2, (dim.height - Installer.getInstance().getHeight()) / 2);
        setResizable(false);
        addWindowListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void setPanel(GenericPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        getContentPane().invalidate();
        getContentPane().validate();
        getContentPane().repaint();
        if (!isVisible()) {
            setVisible(true);
            pack();
        }
        panel.added();
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        Installer.getInstance().cancel();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
