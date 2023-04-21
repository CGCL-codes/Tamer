package Utilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * Handles all help related inquires and contexts.
 * @author Adam
 */
public class DocumentationHelper {

    /**
     * Holds all the information about where the help docs are.
     */
    private HelpSet helpSet;

    /**
     * The middle man between the help set and components
     */
    private HelpBroker helpBroker;

    /**
     * Folder where the help docs are located
     */
    private String helpUrlFileBase = "";

    /**
     * Archive of the help docs
     */
    private String helpJarFile = "help.jar";

    /**
     * Main file entry point in the help docs
     */
    private String helpFileEntry = "main.hs";

    /**
     * The help context for the component. Where the help docs will open to
     */
    private String helpContext = "";

    /**
     * Pre-made button for use.
     */
    private JButton jButtonHelp = new JButton();

    public DocumentationHelper() {
        initialize();
    }

    /**
     * Setup the initial defaults for the class.
     * 1) connecting to the help docs
     * 2) setting up the button
     */
    private void initialize() {
        helpUrlFileBase = new File("help" + File.separatorChar + "FolderMonitor").getAbsolutePath() + File.separatorChar;
        helpSet = getHelpSet();
        if (helpSet != null) {
            helpBroker = helpSet.createHelpBroker();
        }
        setHelpButton();
    }

    /**
     * @return the helpset for the help docs
     */
    private HelpSet getHelpSet() {
        HelpSet hsLocal = null;
        try {
            URL url = getHelpURL();
            hsLocal = new HelpSet(null, url);
        } catch (javax.help.HelpSetException ex) {
            Logger.getLogger(DocumentationHelper.class).error(ex.getMessage());
            return null;
        }
        return hsLocal;
    }

    /**
     * Get a Action listener for the helpset. Use this by adding it as an
     * actionListener to a button
     * @return the ActionListener
     * TODO: return action listener that pops up a notification dialog.
     */
    public ActionListener getHelpSetListener() {
        if (helpBroker != null) {
            return new CSH.DisplayHelpFromSource(helpBroker);
        }
        return new HelpErrorListener();
    }

    /**
     * Set the help context for a component
     * @param component in question
     * @param context is the label in the help documentation
     */
    public void setHelpSetContext(Component component, String context) {
        helpContext = context;
        CSH.setHelpIDString(component, helpContext);
    }

    /**
     * Get the location of the help docs
     * @return a URL for the help docs
     */
    public URL getHelpURL() {
        try {
            if (helpJarFile == null || helpJarFile.isEmpty()) {
                return new URL("file:" + helpUrlFileBase + helpFileEntry);
            } else {
                return new URL("jar:file:" + File.separatorChar + helpUrlFileBase + helpJarFile + "!/" + helpFileEntry);
            }
        } catch (Exception ee) {
            return null;
        }
    }

    /**
     * Enable F1 help key for a component and a specific context.
     * @param component the will have F1 access
     * @param context is the label in the help documentation
     */
    public void enableHelpKey(Component component, String context) {
        if (helpBroker != null) {
            helpBroker.enableHelpKey(component, context, helpSet);
        }
    }

    /**
     * Default setting for the help button.
     */
    public void setHelpButton() {
        jButtonHelp.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButtonHelp.setPreferredSize(new Dimension(125, 22));
        jButtonHelp.setOpaque(false);
        jButtonHelp.setFocusPainted(false);
        jButtonHelp.setBorderPainted(false);
        jButtonHelp.setContentAreaFilled(false);
        jButtonHelp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jButtonHelp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButtonHelp.setIcon(Utilities.getImageIconSmall("help"));
        jButtonHelp.setText("Help");
        jButtonHelp.setToolTipText("Click to receive help on this page.");
        jButtonHelp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonHelp.addActionListener(getHelpSetListener());
    }

    /**
     * Get the button with a specific context.
     * @param context id in the help file.
     * @return the help button
     */
    public JButton getHelpButton(String context) {
        setHelpSetContext(jButtonHelp, context);
        return jButtonHelp;
    }

    /**
     * The help context for the component. Where the help docs will open to
     * @param helpContext the helpContext to set
     */
    public void setHelpContext(String helpContext) {
        this.helpContext = helpContext;
    }

    /**
     * Gets passed if the help files can not be loaded.
     */
    public class HelpErrorListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String helpLocation = helpUrlFileBase + helpFileEntry;
            if (helpJarFile != null && !helpJarFile.isEmpty()) {
                helpLocation = helpUrlFileBase + helpJarFile;
            }
            JOptionPane.showConfirmDialog(null, "<html>Could not find help documents.<br/>Make sure the following file is accessible.<br/>" + helpLocation + " </html>", "Information", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
