package plugin.notes.gui;

import gmgen.util.LogUtilities;
import pcgen.core.SettingsHandler;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Panel that tracks the misc preferences
 *
 * @author devon
 * @since April 7, 2003
 */
public class PreferencesNotesPanel extends gmgen.gui.PreferencesPanel {

    private JPanel dirPanel;

    private JPanel loggingPanel;

    private JTextField dataDirField;

    private JCheckBox logging;

    private JButton browseButton;

    /** Creates new form PreferencesNotesPanel */
    public PreferencesNotesPanel() {
        initComponents();
        initPreferences();
    }

    public void applyPreferences() {
        SettingsHandler.setGMGenOption("Notes.DataDir", getDataDir());
        SettingsHandler.setGMGenOption("Logging.On", isLogging());
        LogUtilities.inst().setLoggingOn(isLogging());
    }

    public void initPreferences() {
        setDataDir(SettingsHandler.getGMGenOption("Notes.DataDir", SettingsHandler.getGmgenPluginDir().toString() + File.separator + "Notes"));
        setLogging(SettingsHandler.getGMGenOption("Logging.On", false));
    }

    /**
	 * <p>
	 * Sets the current data directory setting
	 * </p>
	 *
	 * @param dir
	 */
    private void setDataDir(String dir) {
        dataDirField.setText(dir);
    }

    /**
	 * <p>
	 * Gets the current data directory setting
	 * </p>
	 * @return data directory
	 *
	 */
    private String getDataDir() {
        return dataDirField.getText();
    }

    private boolean isLogging() {
        return logging.isSelected();
    }

    private void setLogging(boolean isLogging) {
        logging.setSelected(isLogging);
    }

    public String toString() {
        return "General";
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        dirPanel = new JPanel();
        loggingPanel = new JPanel();
        dataDirField = new JTextField();
        logging = new JCheckBox();
        browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                browseButtonActionPerformed(e);
            }
        });
        JPanel borderPanel = new JPanel();
        borderPanel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        dirPanel = new JPanel();
        dirPanel.setLayout(new BoxLayout(dirPanel, BoxLayout.Y_AXIS));
        dirPanel.setBorder(new TitledBorder("Source Directory"));
        JLabel locationLabel = new JLabel();
        locationLabel.setText("Notes Data Location");
        dataDirField.setPreferredSize(new java.awt.Dimension(100, 21));
        JPanel line1 = new JPanel();
        line1.setLayout(new FlowLayout(FlowLayout.LEFT));
        line1.add(locationLabel);
        line1.add(dataDirField);
        line1.add(browseButton);
        dirPanel.add(line1);
        topPanel.add(dirPanel);
        loggingPanel = new JPanel();
        loggingPanel.setLayout(new BoxLayout(loggingPanel, BoxLayout.Y_AXIS));
        loggingPanel.setBorder(new TitledBorder("Client"));
        logging.setText("Log game data?");
        loggingPanel.add(logging);
        centerPanel.add(loggingPanel);
        borderPanel.add(topPanel, BorderLayout.NORTH);
        borderPanel.add(centerPanel, BorderLayout.CENTER);
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(borderPanel);
        add(jScrollPane1, BorderLayout.CENTER);
    }

    /**
	 * <p>
	 * Handles browsing for a directory.
	 * </p>
	 *
	 * @param e
	 */
    protected void browseButtonActionPerformed(ActionEvent e) {
        JFileChooser dlg = new JFileChooser(getDataDir());
        dlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (dlg.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            setDataDir(dlg.getSelectedFile().getAbsolutePath());
        }
    }
}
