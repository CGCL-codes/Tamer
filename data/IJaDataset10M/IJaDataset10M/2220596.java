package net.iTunesDJ.presentation;

import java.awt.Event;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import net.iTunesDJ.application.ApplicationListener;
import net.iTunesDJ.application.IApplication;

/**
 * @author buph0011
 *
 */
public class Presentation implements IPresentation, ApplicationListener, WindowListener {

    private IApplication application;

    private JFrame frame;

    /**
	 * @param application
	 */
    public Presentation(IApplication application) {
        this.application = application;
        this.frame = new JFrame("iTunesDJ");
        this.frame.addWindowListener(this);
    }

    @Override
    public void start() {
        this.frame.setVisible(true);
    }

    @Override
    public void inform(Event event) {
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
        application.getITunes().pause();
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        this.shutdown();
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        this.frame.setVisible(false);
        this.frame.dispose();
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        application.getITunes().play();
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    @Override
    public void shutdown() {
        this.application.shutdown();
        System.exit(0);
    }
}
