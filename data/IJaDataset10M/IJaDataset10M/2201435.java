package gui;

import javax.swing.*;
import collection.Collection;
import dataExporters.MSAnalyzeDataSetExporter;
import database.Database;
import database.InfoWarehouse;
import errorframework.DisplayException;
import errorframework.ErrorLogger;
import externalswing.SwingWorker;
import java.awt.event.*;

/**
 * @author jtbigwoo
 */
public class ExportMSAnalyzeDialog extends JDialog implements ActionListener {

    private JButton okButton;

    private JButton cancelButton;

    private JTextField parFileField;

    private JButton parDotDotDot;

    private JTextField accessFileField;

    private JButton accessDotDotDot;

    private InfoWarehouse db;

    private JFrame parent = null;

    private Collection collection = null;

    public ExportMSAnalyzeDialog(JFrame parent, InfoWarehouse db, Collection c) {
        super(parent, "Export to MS-Analyze", true);
        this.db = db;
        this.parent = parent;
        this.collection = c;
        setSize(450, 150);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JLabel parFileLabel = new JLabel(".par File: ");
        parFileField = new JTextField(25);
        parDotDotDot = new JButton("...");
        parDotDotDot.addActionListener(this);
        JLabel accessFileLabel = new JLabel("MS Analyze DB File (Optional): ");
        accessFileField = new JTextField(25);
        accessDotDotDot = new JButton("...");
        accessDotDotDot.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        JPanel mainPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        mainPanel.setLayout(layout);
        mainPanel.add(parFileLabel);
        mainPanel.add(parFileField);
        mainPanel.add(parDotDotDot);
        mainPanel.add(accessFileLabel);
        mainPanel.add(accessFileField);
        mainPanel.add(accessDotDotDot);
        mainPanel.add(buttonPanel);
        layout.putConstraint(SpringLayout.WEST, parFileLabel, 10, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, parFileLabel, 15, SpringLayout.NORTH, mainPanel);
        layout.putConstraint(SpringLayout.WEST, parFileField, 170, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, parFileField, 10, SpringLayout.NORTH, mainPanel);
        layout.putConstraint(SpringLayout.WEST, parDotDotDot, 375, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, parDotDotDot, 10, SpringLayout.NORTH, mainPanel);
        layout.putConstraint(SpringLayout.WEST, accessFileLabel, 10, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, accessFileLabel, 15, SpringLayout.SOUTH, parFileField);
        layout.putConstraint(SpringLayout.WEST, accessFileField, 170, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, accessFileField, 10, SpringLayout.SOUTH, parFileField);
        layout.putConstraint(SpringLayout.WEST, accessDotDotDot, 375, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, accessDotDotDot, 10, SpringLayout.SOUTH, parFileField);
        layout.putConstraint(SpringLayout.WEST, buttonPanel, 160, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, buttonPanel, 10, SpringLayout.SOUTH, accessFileLabel);
        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == parDotDotDot) {
            parFileField.setText((new FileDialogPicker("Choose .par file destination", "par", this)).getFileName());
        } else if (source == accessDotDotDot) {
            accessFileField.setText((new FileDialogPicker("Choose .mdb file", "mdb", this)).getFileName());
        } else if (source == okButton) {
            if (!parFileField.getText().equals("") && !parFileField.getText().equals("*.par")) {
                if (accessFileField.getText().equals("") || accessFileField.getText().endsWith(".mdb")) {
                    final JDialog parentRef = this;
                    final Database dbRef = (Database) db;
                    final ProgressBarWrapper progressBar = new ProgressBarWrapper(parent, MSAnalyzeDataSetExporter.TITLE, 100);
                    final MSAnalyzeDataSetExporter dse = new MSAnalyzeDataSetExporter(this, dbRef, progressBar);
                    progressBar.constructThis();
                    final String accessFileName = accessFileField.getText().equals("") ? null : accessFileField.getText();
                    final SwingWorker worker = new SwingWorker() {

                        public Object construct() {
                            try {
                                dse.exportToPar(collection, parFileField.getText(), accessFileName);
                            } catch (DisplayException e1) {
                                ErrorLogger.displayException(progressBar, e1.toString());
                            }
                            return null;
                        }

                        public void finished() {
                            progressBar.disposeThis();
                            ErrorLogger.flushLog(parent);
                            parent.validate();
                        }
                    };
                    worker.start();
                    dispose();
                } else JOptionPane.showMessageDialog(this, "Please enter a valid access database.");
            } else JOptionPane.showMessageDialog(this, "Please enter a par file name.");
        } else dispose();
    }
}
