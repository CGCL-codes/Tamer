package org.arch4j.ui;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import org.arch4j.util.Performer;
import org.arch4j.ui.util.Transcript;
import org.arch4j.ui.components.JStatusBar;

public class JTabbedApplicationShell extends JApplicationShell implements ChangeListener {

    private Vector applicationNames;

    protected JTabbedPane tabbedPane;

    protected JPanel mainPanel;

    /**
     * Constructor.
     */
    public JTabbedApplicationShell() {
        super();
    }

    /**
     * Listener callback method.
     * <p>
     * JApplication implements the ActionListener interface so it can
     * a listener for a varity of Components.
     * <p>
     * The default action in the use the Performer to perform a method
     * within the JApplication of subclass thereof.
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        performer.perform(e.getActionCommand());
    }

    /**
     * Cause the application to terminate.
     */
    public void exitApplication() {
        System.exit(0);
    }

    /**
     * Get the icon that is displayed in the Window titlebar
     * from the resource manager.
     */
    public ImageIcon getIcon() {
        return resourceMgr.getIcon("titleBarIcon");
    }

    /**
     * Get the title of the application from the resource manager.
     */
    public String getTitle() {
        return resourceMgr.getString("title");
    }

    /**
     * Initialize the JApplication.
     */
    protected void initialize() {
        performer = new Performer(this);
        setLayout(new BorderLayout());
        initializeLookAndFeel();
        initializePanels();
    }

    /**	Initialize the Windows look & feel.*/
    protected void initializeLookAndFeel() {
        try {
            String sLookAndFeel = (String) System.getProperties().get("LookAndFeel");
            if (sLookAndFeel == null) sLookAndFeel = "Windows";
            if (sLookAndFeel.equals("Windows")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else if (sLookAndFeel.equals("Metal")) {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } else if (sLookAndFeel.equals("Motif")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            } else {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initialize the panels.
     */
    protected void initializePanels() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(toolbar = new JToolBar(), "North");
        mainPanel.add(statusbar = new JStatusBar(), "South");
        add(menubar = new JMenuBar(), "North");
        add(mainPanel, "Center");
    }

    /**
     * Main method for JTabbedApplicationShell.
     */
    public static void main(String[] args) {
        try {
            JTabbedApplicationShell app = new JTabbedApplicationShell();
            Vector appNames = new Vector(3);
            int numArgs = args.length;
            int i;
            for (i = 0; i < numArgs; i++) {
                appNames.addElement(args[i]);
            }
            app.setApplications(appNames);
            JFrame frame = new JFrame();
            frame.setTitle(app.getTitle());
            frame.setIconImage(app.getIcon().getImage());
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add("Center", app);
            frame.addWindowListener(new JAppCloser(app));
            frame.pack();
            frame.setBounds(50, 50, 800, 600);
            frame.show();
        } catch (Throwable t) {
            System.out.println("uncaught exception: " + t);
            t.printStackTrace();
        }
    }

    /**
     * Set the content panel for the JApplicationShell.
     * Update the command manager, menubar, toolbar & statusbar.
     */
    public void setApplications(Vector appNames) {
        String appName;
        JApplicationPanel app = null;
        applicationNames = appNames;
        tabbedPane = new JTabbedPane();
        for (Enumeration e = appNames.elements(); e.hasMoreElements(); ) {
            appName = (String) e.nextElement();
            try {
                app = (JApplicationPanel) (java.lang.Class.forName(appName)).newInstance();
                Transcript.show(appName + " created");
            } catch (Throwable ee) {
                System.out.println(ee.toString());
            }
            if (app != null) {
                tabbedPane.addTab(app.getTitle(), (JPanel) app);
            }
        }
        JPanel tabContainer = new JPanel();
        tabContainer.setLayout(new BorderLayout());
        tabContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
        tabContainer.add(tabbedPane, "Center");
        mainPanel.add(tabContainer, "Center");
        tabbedPane.addChangeListener(this);
        update();
    }

    /**
     * Find the top level frame and set its title..
     */
    public void setWindowTitleAndIcon() {
        Frame theWindow;
        theWindow = (Frame) getTopLevelAncestor();
        if (theWindow != null) {
            theWindow.setTitle(getTitle());
            theWindow.setIconImage(getIcon().getImage());
        }
    }

    /**
     * Listener callback from the tabbed panel.
     */
    public void stateChanged(ChangeEvent arg1) {
        update();
    }

    /**
     * Listener callback from the tabbed panel.
     */
    public void update() {
        JApplicationPanel selectedApp = (JApplicationPanel) tabbedPane.getSelectedComponent();
        commandMgr = selectedApp.getCommandManager();
        resourceMgr = selectedApp.getResourceManager();
        updatePanels();
    }

    /**
     * Initialize the panels.
     */
    protected void updatePanels() {
        if (menubar != null) {
            remove(menubar);
        }
        if (toolbar != null) {
            mainPanel.remove(toolbar);
        }
        if (commandMgr != null) {
            add(menubar = commandMgr.createMenubar(), "North");
            mainPanel.add(toolbar = commandMgr.createToolbar(), "North");
        }
        validate();
        repaint();
    }
}
