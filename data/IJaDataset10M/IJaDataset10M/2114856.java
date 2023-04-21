package org.statefive.feedstate.ui.swing;

import java.io.IOException;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

/**
 * Provides a 'help' menubasic components for an application - menu items for
 * help contents, 'about' dialog and license information.
 *
 * @author rich
 */
public abstract class AbstractFeedStateFrame extends SingleFrameApplication {

    /** If set this represents the contents value that launches the specified
   help. */
    private String helpValue;

    private boolean standAlone = true;

    protected JFrame mainFrame;

    protected JButton buttonHelp;

    protected JButton buttonAbout;

    protected JButton buttonLicense;

    protected JMenu menuHelp;

    protected JMenuItem menuItemHelp;

    protected JMenuItem menuItemLicense;

    protected JMenuItem menuItemAbout;

    protected JMenuItem menuItemQuit;

    private ActionMap map;

    /**
   * 
   *
   * @param helpString the JavaHelp string for the entry in the contents
   * of the Java Help Set. If set to <tt>null</tt> imples that the
   * standard (non-topic) related JavaHelp will be invoked when the user
   * selects the help menu item or button.
   */
    public AbstractFeedStateFrame(String helpString) {
        this.helpValue = helpString;
    }

    /**
   * Displays information 'about' this application.
   */
    @Action
    public void about() {
        JOptionPane.showMessageDialog(mainFrame, SwingUtils.getAboutPanel(), "About Feed State", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
   * Does nothing. Why? Because individual help topics are assigned to buttons
   * on a help topic basis.
   */
    @Action
    public void help() {
    }

    /**
   * Displays license information in a panel.
   */
    @Action
    public void license() {
        JOptionPane.showMessageDialog(mainFrame, SwingUtils.getLicensePanel(), "License Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
   * Quits the application - the results of which will vary dependingon whether
   * this app is stand alone or not.
   *
   * @see #isStandAlone()
   */
    @Action
    public void quit() {
        if (this.standAlone) {
            super.exit();
        } else {
            this.mainFrame.dispose();
        }
    }

    /**
   * Determines if this app is stand alone or not.
   * @return {@code true} if the app is stand alone; {@code false} otherwise.
   * A stand alone application exits the JVM when {@link #quit()} is called;
   * otherwise, only the frame for this application (or it's subclass) is
   * disposed of.
   */
    public boolean isStandAlone() {
        return standAlone;
    }

    /**
   * Sets if this app is stand alone or not.
   *
   * @param standAlone if {@code false}, treat this application as having been
   * started by another application - if quit is called, quit the frame for
   * this app only, do not quit the entire JVM.
   */
    public void setStandAlone(boolean standAlone) {
        this.standAlone = standAlone;
    }

    /**
   * 
   * @param args
   */
    @Override
    protected void initialize(String[] args) {
        super.initialize(args);
        Options options = new Options();
        options.addOption("noquit", false, "Do not quit the JVM when the " + "GUI is exited.");
        options.addOption("version", false, "Displays version information.");
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("noquit")) {
                this.setStandAlone(false);
            }
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
    }

    /**
   * Saves the frame state then quits.
   */
    @Override
    protected void shutdown() {
        super.shutdown();
        try {
            ApplicationContext appContext = Application.getInstance().getContext();
            appContext.getSessionStorage().save(mainFrame, "session.xml");
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (IllegalArgumentException illex) {
            illex.printStackTrace();
        }
        System.exit(0);
    }

    /**
   *
   */
    @Override
    protected void startup() {
        mainFrame = new JFrame();
        buttonHelp = new JButton();
        buttonAbout = new JButton();
        buttonLicense = new JButton();
        ResourceMap resource = getContext().getResourceMap(AbstractFeedStateFrame.class);
        resource.injectComponents(mainFrame);
        map = Application.getInstance().getContext().getActionMap(this);
        buttonAbout.setAction(map.get("about"));
        buttonLicense.setAction(map.get("license"));
        buttonHelp.setAction(map.get("help"));
        menuHelp = new JMenu("Help");
        menuHelp.setMnemonic('H');
        menuItemHelp = new JMenuItem();
        menuItemHelp.setAction(map.get("help"));
        menuItemQuit = new JMenuItem();
        menuItemQuit.setAction(map.get("quit"));
        menuItemLicense = new JMenuItem();
        menuItemLicense = new JMenuItem();
        menuItemLicense.setAction(map.get("license"));
        menuHelp.add(menuItemLicense);
        menuHelp.add(menuItemHelp);
        menuHelp.addSeparator();
        menuItemAbout = new JMenuItem();
        menuItemAbout.setAction(map.get("about"));
        menuHelp.add(menuItemAbout);
        buttonHelp.addActionListener(JavaHelp.getHelper(buttonHelp, this.helpValue));
        menuItemHelp.addActionListener(JavaHelp.getHelper(menuItemHelp, this.helpValue));
        if (this.isStandAlone()) {
            mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }
}
