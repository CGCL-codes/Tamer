package ps.client.plugin.eq2.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import ps.client.gui.util.DialogEx;
import ps.client.gui.util.GblPanel;
import ps.client.plugin.eq2.gui.Eq2Plugin;
import ps.client.plugin.eq2.log.LogFileFinder;

@SuppressWarnings("serial")
public class LogFileOptionsDialog extends DialogEx {

    private static final String VALOR_LOG_PATH = "logs\\Valor";

    GblPanel contentPanel = new GblPanel();

    JPanel eq2PathPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

    TitledBorder eq2PathBorder = new TitledBorder(new EtchedBorder(), " Everquest II - Verzeichnis ");

    JLabel eq2PathLabel = new JLabel("Pfad: ");

    JTextField eq2PathField = new JTextField(30);

    JButton eq2PathBrowseButton = new JButton("...");

    JPanel logPathPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

    TitledBorder logPathBorder = new TitledBorder(new EtchedBorder(), " Log - Verzeichnis ");

    JLabel logPathLabel = new JLabel("Pfad: ");

    JTextField logPathField = new JTextField(30);

    JButton logPathBrowseButton = new JButton("...");

    JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));

    JButton okButton = new JButton("Ok");

    JButton cancelButton = new JButton("Abbrechen");

    JFileChooser eq2PathChooser = new JFileChooser();

    JFileChooser logPathChooser = new JFileChooser();

    Eq2Plugin eq2Plugin;

    public LogFileOptionsDialog(Eq2Plugin eq2Plugin, String prefNode) {
        super(eq2Plugin.getMainFrame(), prefNode + "/LogFileOptionsDialog");
        this.eq2Plugin = eq2Plugin;
        setTitle("Everquest II - Verzeichnis einstellen");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setInsets(new Insets(5, 5, 5, 5));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.addLastComponent(eq2PathPanel);
        contentPanel.addLastComponent(logPathPanel);
        eq2PathPanel.setBorder(eq2PathBorder);
        eq2PathPanel.add(eq2PathLabel);
        eq2PathPanel.add(eq2PathField);
        eq2PathPanel.add(eq2PathBrowseButton);
        logPathPanel.setBorder(logPathBorder);
        logPathPanel.add(logPathLabel);
        logPathPanel.add(logPathField);
        logPathPanel.add(logPathBrowseButton);
        contentPanel.setAnchor(GblPanel.ANCHOR_EAST);
        contentPanel.addLastComponent(buttonPanel);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        pack();
        eq2PathChooser.setDialogTitle("Everquest II - Verzeichnis bestimmen");
        eq2PathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        logPathChooser.setDialogTitle("Everquest II - Log-Verzeichnis bestimmen");
        logPathChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        logPathChooser.setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                if (f.isFile()) {
                    return LogFileFinder.PATTERN_REGEX_LOGFILE.matcher(f.getName()).matches();
                } else {
                    return true;
                }
            }

            public String getDescription() {
                return "Everquest 2 Log File";
            }
        });
        eq2PathField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                logPathField.setText(eq2PathField.getText() + "\\" + VALOR_LOG_PATH);
            }
        });
        eq2PathBrowseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (eq2PathChooser.showOpenDialog(LogFileOptionsDialog.this) == JFileChooser.APPROVE_OPTION) {
                    eq2PathField.setText(eq2PathChooser.getSelectedFile().getAbsolutePath());
                    logPathField.setText(eq2PathField.getText() + "\\" + VALOR_LOG_PATH);
                }
            }
        });
        logPathField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButton.doClick();
            }
        });
        logPathBrowseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (logPathChooser.showOpenDialog(LogFileOptionsDialog.this) == JFileChooser.APPROVE_OPTION) {
                    logPathField.setText(logPathChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        eq2PathField.requestFocus();
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            String eq2Path = eq2Plugin.getEq2Path();
            if (eq2Path != null) {
                eq2PathField.setText(eq2Path);
                eq2PathChooser.setCurrentDirectory(new File(eq2Path));
            } else {
                logPathField.setText("");
            }
            String logPath = eq2Plugin.getLogPath();
            if (logPath != null) {
                logPathField.setText(logPath);
                logPathChooser.setCurrentDirectory(new File(logPath));
            } else {
                logPathField.setText("");
            }
        }
        super.setVisible(b);
    }

    private void ok() {
        try {
            setVisible(false);
            eq2Plugin.saveEq2Directory(eq2PathField.getText().trim());
            eq2Plugin.saveLogFile(null);
            eq2Plugin.saveLogPath(null);
            eq2Plugin.setupLogParsing(logPathField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Setzen des Log-Pfades: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
