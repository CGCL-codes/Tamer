package com.novasurv.turtle.frontend.swing.prefs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import com.novasurv.turtle.frontend.swing.data.SwingDataManager;

/**
 * Dialog that allows the user to edit certain preferences.
 *
 * @author Jason Dobies
 */
public class PreferencesDialog extends JDialog {

    /** Constraints used to dictate how the labels should be arranged. */
    private static final GridBagConstraints NAME_BAG = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 3, 10), 0, 0);

    /** Constraints used to dictate how the components should be arranged. */
    private static final GridBagConstraints VALUE_BAG = new GridBagConstraints(1, GridBagConstraints.RELATIVE, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 3, 10), 0, 0);

    /** Constraints used to dictate how the descriptions should be arranged. */
    private static final GridBagConstraints DESCRIPTION_BAG = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 5), 0, 0);

    private SwingDataManager dataManager;

    private JTextField txtCompilerBin;

    public PreferencesDialog(JFrame parent, SwingDataManager dataManager) {
        this(parent, dataManager, false);
    }

    public PreferencesDialog(JFrame parent, SwingDataManager dataManager, boolean displayFirstRunHelp) {
        super(parent);
        this.dataManager = dataManager;
        super.setModal(true);
        if (displayFirstRunHelp) {
            super.setTitle("Initial Setup");
        } else {
            super.setTitle("Preferences");
        }
        JLabel lblCompilerName = new JLabel("Compiler Executable");
        JLabel lblCompilerDescription = new JLabel("Full path to the compiler (javac) executable. Example: /opt/jdk1.6/bin/javac");
        lblCompilerDescription.setFont(lblCompilerDescription.getFont().deriveFont(Font.ITALIC));
        txtCompilerBin = new JTextField();
        TurtlePreferences preferences = dataManager.getPreferences();
        txtCompilerBin.setText(preferences.getString(TurtlePreferences.COMPILER_BIN, ""));
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.add(lblCompilerName, NAME_BAG);
        contentPanel.add(txtCompilerBin, VALUE_BAG);
        contentPanel.add(lblCompilerDescription, DESCRIPTION_BAG);
        JButton saveButton = new JButton("Save");
        saveButton.setMnemonic('S');
        saveButton.addActionListener(new SaveActionListener());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(new CancelActionListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        JPanel assemblyPanel = new JPanel(new BorderLayout(0, 20));
        if (displayFirstRunHelp) {
            JTextArea txtFirstRunHelp = new JTextArea();
            txtFirstRunHelp.setEditable(false);
            txtFirstRunHelp.setWrapStyleWord(true);
            txtFirstRunHelp.setLineWrap(true);
            txtFirstRunHelp.setOpaque(false);
            txtFirstRunHelp.setText("Welcome to CodeTurtle!\n" + "In order to take full use of CodeTurtle's features, your user preferences must be " + "set up. We made some guesses on the best values, but we're not perfect. Please take a moment " + "to review the settings to make sure they are valid for your environment before " + "preceding to the awesomeness of CodeTurtle.");
            JPanel helpPanel = new JPanel(new BorderLayout());
            helpPanel.add(txtFirstRunHelp, BorderLayout.NORTH);
            assemblyPanel.add(helpPanel, BorderLayout.NORTH);
        }
        assemblyPanel.add(contentPanel, BorderLayout.CENTER);
        assemblyPanel.add(buttonPanel, BorderLayout.SOUTH);
        assemblyPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        this.setContentPane(assemblyPanel);
        this.pack();
        if (displayFirstRunHelp) {
            this.setSize(this.getWidth(), this.getHeight() + 50);
        }
    }

    /** Action listener for cancelling the create process. */
    private class CancelActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            PreferencesDialog.this.setVisible(false);
            PreferencesDialog.this.dispose();
        }
    }

    /** Action listener for saving changes to preferences values. */
    private class SaveActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            TurtlePreferences preferences = dataManager.getPreferences();
            String newCompilerPath = txtCompilerBin.getText();
            preferences.putString(TurtlePreferences.COMPILER_BIN, newCompilerPath);
            PreferencesDialog.this.setVisible(false);
            PreferencesDialog.this.dispose();
        }
    }
}
