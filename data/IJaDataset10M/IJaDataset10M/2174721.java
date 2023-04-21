package org.project2cloud.gfx;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.project2cloud.props.PropertiesManager;

public class FileSelectorPanel extends JPanel {

    private JLabel lblText;

    private JButton btnSelectFile;

    private String title;

    private File theFile;

    private JTextField txtFilePath;

    private Properties theProps;

    public FileSelectorPanel() {
        this("poop");
    }

    public FileSelectorPanel(String s) {
        this.title = s;
        setLayout(new BorderLayout());
        buildIHM();
        addListeners();
    }

    private void buildIHM() {
        btnSelectFile = new JButton("Select");
        txtFilePath = new JTextField();
        txtFilePath.setPreferredSize(new Dimension(300, 15));
        lblText = new JLabel(title + " : ");
        lblText.setPreferredSize(new Dimension(100, 20));
        add(lblText, BorderLayout.WEST);
        add(txtFilePath, BorderLayout.CENTER);
        add(btnSelectFile, BorderLayout.EAST);
    }

    private void addListeners() {
        btnSelectFile.addActionListener(new BtnSelectActionListener());
    }

    protected void processProperties(File f) {
        if (f == null) return;
        try {
            theProps = PropertiesManager.load(f);
            String oldValue = txtFilePath.getText();
            txtFilePath.setText(f.getCanonicalPath());
            firePropertyChange("FILE_PATH", oldValue, f.getCanonicalPath());
        } catch (FileNotFoundException e) {
            Utils.showError("Fichier introuvable :\n" + e.getMessage(), "Fichier introuvable");
            e.printStackTrace();
        } catch (IOException e) {
            Utils.showError("I/O Exception :\n" + e.getMessage(), "I/O Exception");
            e.printStackTrace();
        }
    }

    public File getTheFile() {
        return theFile;
    }

    public Properties getTheProps() {
        return theProps;
    }

    class BtnSelectActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            openFile((JButton) arg0.getSource());
        }

        private boolean openFile(Component c) {
            PropertiesFilter fJavaFilter = new PropertiesFilter();
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open File");
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setCurrentDirectory(new File("."));
            fc.setFileFilter(fJavaFilter);
            int result = fc.showOpenDialog(c);
            if (result == JFileChooser.CANCEL_OPTION) {
                return true;
            } else if (result == JFileChooser.APPROVE_OPTION) {
                theFile = fc.getSelectedFile();
                processProperties(theFile);
            } else {
                return false;
            }
            return true;
        }
    }

    class PropertiesFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".properties") || f.isDirectory();
        }

        public String getDescription() {
            return "Properties files (*.properties)";
        }
    }
}
