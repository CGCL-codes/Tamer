package au.gov.naa.digipres.xena.litegui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import au.gov.naa.digipres.xena.util.MemoryFileChooser;

/**
 * Simple dialog to set preferences for the Xena Viewer. Currently
 * there are only two preferences that can be set - the Xena plugin
 * directory, and the xena destination directory. Each of these options
 * has an associated entry field, and a browse button which will bring
 * up a file chooser that populates the appropriate entry field with the 
 * selected directory.
 * The entry fields can be pre-populated from the calling window, and
 * thus previously saved preferences can be automatically restored.
 * created 1/12/2005
 * xena
 * Short desc of class:
 */
public class LitePreferencesDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private String xenaDestDir;

    private JTextField xenaDestTF;

    private String xenaLogFileDir;

    private JTextField xenaLogTF;

    private boolean approved = false;

    public LitePreferencesDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title, true);
        initGUI();
    }

    /**
	 * One-time GUI initialisation
	 */
    private void initGUI() {
        JPanel prefsPanel = new JPanel(new BorderLayout());
        prefsPanel.setBorder(new EtchedBorder());
        prefsPanel.setLayout(new GridLayout(2, 1));
        JLabel xenaDestLabel = new JLabel("Xena destination directory:");
        xenaDestTF = new JTextField(30);
        JButton xenaDestBrowseButton = new JButton("Browse");
        JPanel xenaDestPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        xenaDestPanel.setBorder(new EmptyBorder(4, 6, 0, 6));
        xenaDestPanel.add(xenaDestLabel);
        xenaDestPanel.add(xenaDestTF);
        xenaDestPanel.add(xenaDestBrowseButton);
        prefsPanel.add(xenaDestPanel);
        xenaDestBrowseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MemoryFileChooser fileChooser = new MemoryFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int retVal = fileChooser.showOpenDialog(LitePreferencesDialog.this, LitePreferencesDialog.class, "destination.directory");
                if (retVal == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
                    setXenaDestDir(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        JLabel xenaLogLabel = new JLabel("Xena log directory:");
        xenaLogTF = new JTextField(30);
        JButton xenaLogBrowseButton = new JButton("Browse");
        JPanel xenaLogPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        xenaLogPanel.setBorder(new EmptyBorder(4, 6, 6, 6));
        xenaLogPanel.add(xenaLogLabel);
        xenaLogPanel.add(xenaLogTF);
        xenaLogPanel.add(xenaLogBrowseButton);
        prefsPanel.add(xenaLogPanel);
        xenaLogBrowseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MemoryFileChooser fileChooser = new MemoryFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int retVal = fileChooser.showOpenDialog(LitePreferencesDialog.this, LitePreferencesDialog.class, "log.directory");
                if (retVal == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
                    setXenaLogFileDir(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        this.add(prefsPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                doCloseDialog();
            }
        });
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                approved = false;
                boolean destDirValid = false;
                String destDirString = xenaDestTF.getText();
                if (destDirString != null && destDirString.length() != 0) {
                    File destDir = new File(destDirString);
                    if (destDir.exists() && destDir.isDirectory()) {
                        destDirValid = true;
                    }
                }
                if (destDirValid == false) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid folder name for the destination folder.", "Invalid Destination folder", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String logString = xenaLogTF.getText();
                boolean logDirValid = false;
                if (logString != null && logString.length() != 0) {
                    File logDir = new File(logString);
                    if (logDir.exists() && logDir.isDirectory()) {
                        logDirValid = true;
                    }
                }
                if (logDirValid == false) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid location for the log file.", "Invalid Log location", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                approved = true;
                xenaDestDir = xenaDestTF.getText();
                xenaLogFileDir = xenaLogTF.getText();
                doCloseDialog();
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doCloseDialog();
            }
        });
        this.addComponentListener(new java.awt.event.ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent event) {
                LitePreferencesDialog.this.pack();
            }
        });
    }

    private void doCloseDialog() {
        this.setVisible(false);
    }

    /**
	 * @return Returns the xenaDestDir.
	 */
    public String getXenaDestDir() {
        return xenaDestDir;
    }

    /**
	 * @param xenaDestDir
	 * The xenaDestDir to set.
	 */
    public void setXenaDestDir(String xenaDestDir) {
        this.xenaDestDir = xenaDestDir;
        xenaDestTF.setText(xenaDestDir);
    }

    /**
	 * @return Returns the xenaLogFile.
	 */
    public String getXenaLogFileDir() {
        return xenaLogFileDir;
    }

    /**
	 * @param xenaLogFileDir The xenaLogFile to set.
	 */
    public void setXenaLogFileDir(String xenaLogFileDir) {
        this.xenaLogFileDir = xenaLogFileDir;
        xenaLogTF.setText(xenaLogFileDir);
    }

    /**
	 * @return Returns the approved.
	 */
    public boolean isApproved() {
        return approved;
    }
}
