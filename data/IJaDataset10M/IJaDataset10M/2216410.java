package org.modyna.modyna.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTextPane;

/**
 * Root application frame of dynsyssim. Provides means to close the application
 * after giving the user the possibility to confirm the closing operation.
 * <p>
 * <img src="doc-files/MainAppFrame.jpg">
 */
public class MainAppFrame extends JFrame {

    private static final long serialVersionUID = 2492888396665313391L;

    protected GuiManager guiManager = null;

    protected JTextPane pane = null;

    /**
	 * Constructor to instantiate and populate the main app frame.
	 * 
	 * @param guiManager
	 *            reference to the guiManager which contains the presentation
	 *            logic.
	 */
    public MainAppFrame(GuiManager guiManager) {
        super("DynSysSim");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.guiManager = guiManager;
        setJMenuBar(new MainMenuBar(guiManager.getActionManager()));
        getContentPane().add(new MainToolbar(guiManager.getActionManager()), BorderLayout.NORTH);
        pane = new JTextPane();
        pane.setPreferredSize(new Dimension(550, 350));
        getContentPane().add(pane, BorderLayout.CENTER);
        getContentPane().add(guiManager.getStatusBar(), BorderLayout.SOUTH);
        this.pack();
        MainAppFrame.locateOnScreenCenter(this);
    }

    /**
	 * Show a confirmation dialog for closing the application.
	 * 
	 * @see java.awt.Window#processWindowEvent(java.awt.event.WindowEvent)
	 */
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            guiManager.closeApplication();
        }
    }

    /**
     * Locates the given component on the screen's center.
     * 
     * @param component   the component to be centered
     */
    public static void locateOnScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation((screenSize.width - paneSize.width) / 2, (screenSize.height - paneSize.height) / 2);
    }
}
