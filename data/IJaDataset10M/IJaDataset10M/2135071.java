package de.kuulware.jooda.panels.project;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import de.kuulware.jooda.panels.ConfigurationPanel;
import de.kuulware.jooda.Jooda;

/**
 *  GeneralOptionsPanel 
 *
 *@author     charly 
 *@created    25. Juli 2000 
 */
public class GeneralProjectOptionsPanel extends ConfigurationPanel implements ActionListener {

    private JTextField tfProjTit;

    private JTextField tfRealProjectDirectory;

    private JTextField tfProjDir;

    private JTextField tfsaveLog;

    private JTextField tfDiffJavaInt;

    private JButton realProjectDirectoryButton;

    private JButton projectClassesDirectoryButton;

    private JButton saveLogButton;

    /**
	 *  Constructor for the GeneralProjectOptionsPanel object 
	 *
	 *@param  parentApp  Description of Parameter 
	 */
    public GeneralProjectOptionsPanel(Jooda parentApp) {
        super(parentApp);
        this.setLayout(null);
        JLabel projectTitleLabel = new JLabel(System.getProperty("application.languageDictionary.#ProjectTitle"));
        projectTitleLabel.setBounds(30, 10, 250, 20);
        this.add(projectTitleLabel);
        tfProjTit = new JTextField("", 50);
        tfProjTit.setBounds(30, 30, 320, 30);
        this.add(tfProjTit);
        JLabel realProjectDirectoryLabel = new JLabel(System.getProperty("application.languageDictionary.#realProjectDirectory"));
        realProjectDirectoryLabel.setBounds(30, 70, 250, 20);
        this.add(realProjectDirectoryLabel);
        tfRealProjectDirectory = new JTextField("", 50);
        tfRealProjectDirectory.setBounds(30, 90, 320, 30);
        this.add(tfRealProjectDirectory);
        realProjectDirectoryButton = new JButton("...");
        realProjectDirectoryButton.setBounds(355, 90, 30, 30);
        realProjectDirectoryButton.addActionListener(this);
        this.add(realProjectDirectoryButton);
        JLabel projectDirectoryLabel = new JLabel(System.getProperty("application.languageDictionary.#ProjectDirectory"));
        projectDirectoryLabel.setBounds(30, 130, 250, 20);
        this.add(projectDirectoryLabel);
        tfProjDir = new JTextField("", 50);
        tfProjDir.setBounds(30, 150, 320, 30);
        this.add(tfProjDir);
        projectClassesDirectoryButton = new JButton("...");
        projectClassesDirectoryButton.setBounds(355, 150, 30, 30);
        projectClassesDirectoryButton.addActionListener(this);
        this.add(projectClassesDirectoryButton);
        JLabel lab5 = new JLabel(System.getProperty("application.languageDictionary.#SaveLog"));
        lab5.setBounds(30, 190, 250, 20);
        this.add(lab5);
        tfsaveLog = new JTextField("", 50);
        tfsaveLog.setBounds(30, 210, 320, 30);
        this.add(tfsaveLog);
        saveLogButton = new JButton("...");
        saveLogButton.setBounds(355, 210, 30, 30);
        saveLogButton.addActionListener(this);
        this.add(saveLogButton);
        JLabel runClassLabel = new JLabel(System.getProperty("application.languageDictionary.#ProjectRunClassOrWebpage"));
        runClassLabel.setBounds(30, 250, 250, 20);
        this.add(runClassLabel);
        tfDiffJavaInt = new JTextField("", 50);
        tfDiffJavaInt.setBounds(30, 270, 320, 30);
        this.add(tfDiffJavaInt);
    }

    /**
	 *  Description of the Method 
	 */
    public void save() {
        String stringValue = tfProjTit.getText();
        parentApp.project.put("#projectTitle", stringValue);
        parentApp.project.put("#realProjectDirectory", tfRealProjectDirectory.getText());
        stringValue = tfProjDir.getText();
        parentApp.project.put("#projectDirectory", stringValue);
        stringValue = tfsaveLog.getText();
        parentApp.project.put("#saveLog", stringValue);
        stringValue = tfDiffJavaInt.getText();
        parentApp.project.put("#projectRunClass", stringValue);
    }

    /**
	 *  Description of the Method 
	 */
    public void loadValues() {
        tfProjTit.setText(getFromProjectHashtable("#projectTitle"));
        tfRealProjectDirectory.setText(getFromProjectHashtable("#realProjectDirectory"));
        tfProjDir.setText(getFromProjectHashtable("#projectDirectory"));
        tfsaveLog.setText(getFromProjectHashtable("#saveLog"));
        tfDiffJavaInt.setText(getFromProjectHashtable("#projectRunClass"));
    }

    /**
	 *  Description of the Method 
	 *
	 *@param  event  Description of Parameter 
	 */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == saveLogButton) {
            openFileDialogForTextField(tfsaveLog, JFileChooser.FILES_ONLY);
            return;
        }
        if (event.getSource() == projectClassesDirectoryButton) {
            openFileDialogForTextField(tfProjDir, JFileChooser.DIRECTORIES_ONLY);
            return;
        }
        if (event.getSource() == realProjectDirectoryButton) {
            openFileDialogForTextField(tfRealProjectDirectory, JFileChooser.DIRECTORIES_ONLY);
            return;
        }
    }
}
