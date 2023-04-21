package VersionUpdater;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import HTTPBrute.HTTPBruteMainFrame;

/**
 *
 * @author Zarko Coklin
 */
public class UpdateInfoDialog extends javax.swing.JFrame {

    /** Creates new form UpdateInfoDialog */
    public UpdateInfoDialog(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLocationRelativeTo(null);
        HTTPBruteMainFrame.setIcon(this);
        initComponents();
        pack();
        this.setVisible(true);
    }

    private void initComponents() {
        setLayout(new MigLayout(""));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Version checker");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        versionPanel = new JPanel(new MigLayout(""));
        add(versionPanel, "wrap");
        versionInfoLabel = new javax.swing.JLabel("A new version (" + CheckUpdate.newVersionStr + ") of HT is available.");
        versionInfoLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        versionPanel.add(versionInfoLabel, "center, wrap");
        upgradeQuestion = new javax.swing.JLabel("Would you like to upgrade now?");
        upgradeQuestion.setFont(new java.awt.Font("Tahoma", 0, 12));
        versionPanel.add(upgradeQuestion, "center, wrap");
        NoteLabel = new javax.swing.JLabel("p.s. If you upgrade, make sure to unzip and run a new version ;-)");
        NoteLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        versionPanel.add(NoteLabel, "wrap");
        JPanel lowerPanel = new JPanel(new MigLayout("fillx"));
        NoButton = new javax.swing.JToggleButton("No");
        NoButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoButtonActionPerformed(evt);
            }
        });
        lowerPanel.add(NoButton);
        YesButton = new javax.swing.JToggleButton("Yes");
        YesButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YesButtonActionPerformed(evt);
            }
        });
        lowerPanel.add(YesButton, "right");
        add(lowerPanel, "grow");
        pack();
    }

    private void YesButtonActionPerformed(java.awt.event.ActionEvent evt) {
        final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.setDialogTitle("Save new version");
        File file = new File("HTTPBrute_" + CheckUpdate.newVersionStr + ".zip");
        fc.setSelectedFile(file);
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            if (file.exists() == true) {
                Object[] options = { "Yes", "No" };
                int result = JOptionPane.showOptionDialog(this, "Do you really want to overwrite an existing scenario file?", "Save File", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (result != 0) {
                    dispose();
                    mainFrame.setVisible(true);
                    return;
                }
            }
            DownloaderThread myThread = new DownloaderThread(mainFrame, this, fc);
            myThread.start();
        }
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        mainFrame.setVisible(true);
    }

    private void NoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
        mainFrame.setVisible(true);
    }

    private javax.swing.JToggleButton NoButton;

    private javax.swing.JLabel NoteLabel;

    private javax.swing.JToggleButton YesButton;

    private javax.swing.JLabel upgradeQuestion;

    private javax.swing.JLabel versionInfoLabel;

    private javax.swing.JPanel versionPanel;

    private JFrame mainFrame;
}
