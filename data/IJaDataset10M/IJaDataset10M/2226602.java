package net.tomcatfort.loderogue.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import net.tomcatfort.loderogue.LodeRogueSystem;
import net.tomcatfort.loderogue.LodeRogueSystemListener;

/**
 * Main frame and application main class.
 */
public final class MainFrame extends JFrame implements LodeRogueSystemListener, WindowListener {

    private LodeRogueSystem mLRSystem = new LodeRogueSystem();

    /**
     * Application entry point.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        MainFrame mf = new MainFrame();
        mf.setVisible(true);
    }

    /**
     * Creates the main frame.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    private MainFrame() {
        add(mLRSystem.getTerminal());
        pack();
        mLRSystem.getTerminal().requestFocus();
        setTitle("LodeRogue");
        setIconImage(UIUtils.loadFrameIcon());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        mLRSystem.addLodeRogueSystemListener(this);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (mLRSystem.getScreenMan().quitRequested()) mLRSystem.quit();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void quit() {
        dispose();
    }
}
