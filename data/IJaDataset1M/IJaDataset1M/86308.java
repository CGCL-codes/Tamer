package com.jgoodies.forms.tutorial.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * A base class for tutorial applications. It provides a light version
 * of the startup behavior from the JSR 296 "Swing Application Framework".
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
public abstract class TutorialApplication {

    private static final Logger LOGGER = Logger.getLogger(TutorialApplication.class.getName());

    protected TutorialApplication() {
    }

    /**
     * Instantiates the given TutorialApplication class, then invokes
     * {@code #startup} with the given arguments. Typically this method
     * is called from an application's #main method.
     *
     * @param appClass the class of the application to launch
     * @param args optional launch arguments, often the main method's arguments.
     */
    public static synchronized void launch(final Class appClass, final String[] args) {
        Runnable runnable = new Runnable() {

            public void run() {
                TutorialApplication application = null;
                try {
                    application = (TutorialApplication) appClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    LOGGER.log(Level.SEVERE, "Can't instantiate " + appClass, e);
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    LOGGER.log(Level.SEVERE, "Illegal Access during launch of " + appClass, e);
                    return;
                }
                try {
                    application.initializeLookAndFeel();
                    application.startup(args);
                } catch (Exception e) {
                    String message = "Failed to launch " + appClass;
                    LOGGER.log(Level.SEVERE, message, e);
                    throw new Error(message, e);
                }
            }
        };
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            EventQueue.invokeLater(runnable);
        }
    }

    /**
     * Starts this application when the application is launched.
     * A typical application creates and shows the GUI in this method.
     * This method runs on the event dispatching thread.<p>
     *
     * Called by the static {@code launch} method.
     *
     * @param args optional launch arguments, often the main method's arguments.
     *
     * @see #launch(Class, String[])
     */
    protected abstract void startup(String[] args);

    protected void initializeLookAndFeel() {
        try {
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Windows")) {
                UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
            } else if (osName.startsWith("Mac")) {
            } else {
                UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
            }
        } catch (Exception e) {
        }
    }

    protected JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    protected final void packAndShowOnScreenCenter(JFrame frame) {
        frame.pack();
        locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }

    /**
     * Locates the given component on the screen's center.
     *
     * @param component   the component to be centered
     */
    protected final void locateOnOpticalScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation((screenSize.width - paneSize.width) / 2, (int) ((screenSize.height - paneSize.height) * 0.45));
    }
}
