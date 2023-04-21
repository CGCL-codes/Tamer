package de.kuulware.jooda.dialogs;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Date;
import java.awt.datatransfer.*;
import java.text.DateFormat;
import de.kuulware.jooda.data.IDEClass;
import de.kuulware.tools.util.SortedEnumeration;
import de.kuulware.tools.gui.ExtendedDialog;
import de.kuulware.jooda.browser.AbstractClassBrowser;

/**
 *  Dialog for a new class 
 *
 * @author     charly 
 * @created    17. September 2000 
 * @version $Id: NewClassDialog.java,v 1.2 2000/12/07 22:22:15 ridesmet Exp $
 */
public class NewClassDialog extends ExtendedDialog implements ActionListener {

    private JButton bApply, bOk, bCancel, bAddFile;

    private JCheckBox ckAbstract, ckFinal;

    private AbstractClassBrowser parentFrame;

    private JLabel labelName, labelImplements, labelFileName, labelSuperclass;

    private JList fileList;

    private JTextField tfName, tfImplements, tfFileName, tfSuperclass;

    /**
	 *  I need a parent Frame 
	 *
	 *@param  parent  Description of Parameter 
	 */
    public NewClassDialog(AbstractClassBrowser parent) {
        super(parent, true);
        parentFrame = parent;
        this.setSize(500, 400);
        this.centerAtScreen();
        this.setLayoutAndComponents();
        this.setTitle(System.getProperty("application.languageDictionary.#newSubClass"));
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
	 *  Something happened 
	 *
	 *@param  event  Description of Parameter 
	 */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == bCancel) {
            this.setVisible(false);
            this.dispose();
        }
        if (event.getSource() == bOk) {
            this.createNewClass();
        }
        if (event.getSource() == bAddFile) {
            addFileToList();
        }
    }

    /**
	 *  Sets the LayoutAndComponents attribute of the IDENewClassDialog object 
	 */
    private void setLayoutAndComponents() {
        getContentPane().setLayout(null);
        labelName = new JLabel(System.getProperty("application.languageDictionary.#labelNameOfClass"));
        labelName.setBounds(20, 30, 200, 20);
        getContentPane().add(labelName);
        tfName = new JTextField();
        tfName.setBounds(20, 50, 200, 30);
        getContentPane().add(tfName);
        labelSuperclass = new JLabel(System.getProperty("application.languageDictionary.#labelNameOfSuperclass"));
        labelSuperclass.setBounds(20, 90, 200, 20);
        getContentPane().add(labelSuperclass);
        tfSuperclass = new JTextField(parentFrame.selectedClass.getTitle());
        tfSuperclass.setBounds(20, 110, 200, 30);
        getContentPane().add(tfSuperclass);
        labelImplements = new JLabel(System.getProperty("application.languageDictionary.#labelImplements"));
        labelImplements.setBounds(20, 150, 200, 20);
        getContentPane().add(labelImplements);
        tfImplements = new JTextField();
        tfImplements.setBounds(20, 170, 200, 30);
        getContentPane().add(tfImplements);
        ckAbstract = new JCheckBox(System.getProperty("application.languageDictionary.#abstract"), null, false);
        ckAbstract.setBounds(20, 220, 200, 20);
        getContentPane().add(ckAbstract);
        ckFinal = new JCheckBox(System.getProperty("application.languageDictionary.#final"), null, false);
        ckFinal.setBounds(20, 250, 200, 20);
        getContentPane().add(ckFinal);
        labelFileName = new JLabel(System.getProperty("application.languageDictionary.#fileNameOfNewClass"));
        labelFileName.setBounds(280, 30, 200, 20);
        getContentPane().add(labelFileName);
        fileList = new JList(new DefaultListModel());
        JScrollPane jsp = new JScrollPane(fileList);
        jsp.setBounds(280, 50, 180, 200);
        Hashtable files = (Hashtable) parentFrame.application.project.get("#projectApplicationSourceFiles");
        SortedEnumeration e = new SortedEnumeration(files.keys(), files.size(), false, '.');
        for (int i = 1; i <= files.size(); i++) {
            ((DefaultListModel) fileList.getModel()).addElement((String) e.nextElement());
        }
        getContentPane().add(jsp);
        bAddFile = new JButton(System.getProperty("application.languageDictionary.#bAddFile"));
        bAddFile.setBounds(290, 270, 160, 30);
        getContentPane().add(bAddFile);
        bAddFile.addActionListener(this);
        bOk = new JButton(System.getProperty("application.languageDictionary.#bOk"));
        bOk.setBounds(15, 320, 100, 30);
        getContentPane().add(bOk);
        bCancel = new JButton(System.getProperty("application.languageDictionary.#bCancel"));
        bCancel.setBounds(125, 320, 100, 30);
        getContentPane().add(bCancel);
        bOk.addActionListener(this);
        bCancel.addActionListener(this);
    }

    /**
	 *  bOk is pressed ->if all variables are good ->make class 
	 */
    private void createNewClass() {
        if (tfName.getText().equals("")) {
            JOptionPane.showMessageDialog(null, System.getProperty("application.languageDictionary.#noClassName"), System.getProperty("application.lanugageDictionary.#message"), JOptionPane.ERROR_MESSAGE);
        } else if (tfSuperclass.getText().equals("")) {
            JOptionPane.showMessageDialog(null, System.getProperty("application.languageDictionary.#noSuperclassName"), System.getProperty("application.lanugageDictionary.#message"), JOptionPane.ERROR_MESSAGE);
        } else if (fileList.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, System.getProperty("application.languageDictionary.#noFileSelected"), System.getProperty("application.lanugageDictionary.#message"), JOptionPane.ERROR_MESSAGE);
        } else {
            ((Hashtable) parentFrame.application.project.get("#projectApplicationSourceFiles")).put(fileList.getSelectedValue(), "");
            parentFrame.application.project.put("#amIDirty", "true");
            IDEClass superclass;
            String className = tfName.getText();
            IDEClass newClass = new IDEClass(className, "Application");
            if ((IDEClass) parentFrame.application.projectClasses.get(tfSuperclass.getText()) != null) {
                superclass = (IDEClass) parentFrame.application.projectClasses.get(tfSuperclass.getText());
            } else {
                superclass = new IDEClass(tfSuperclass.getText(), "Virtual");
                superclass.setFileName("");
                parentFrame.application.projectClasses.put(tfSuperclass.getText(), superclass);
                parentFrame.application.rootClass.addSubclass(superclass);
            }
            IDEClass tmpClass = new IDEClass(tfName.getText(), "Application");
            superclass.addSubclass(tmpClass);
            parentFrame.application.projectClasses.put(tmpClass.getTitle(), tmpClass);
            parentFrame.selectedClass = tmpClass;
            parentFrame.updateContent();
            String imp = "";
            if (!tfImplements.getText().equals("")) {
                imp = " implements " + tfImplements.getText().trim();
            }
            String mod = "";
            if (ckAbstract.isSelected()) {
                mod += "abstract ";
            }
            if (ckFinal.isSelected()) {
                mod += "final ";
            }
            String firstLine = mod + "class " + tmpClass.getTitle() + " extends " + superclass.getTitle() + imp + " {";
            parentFrame.textArea.setText("");
            parentFrame.textArea.addLine(firstLine);
            parentFrame.textArea.append("}");
            parentFrame.textArea.setEditable(true);
            long fileLength = new File((String) fileList.getSelectedValue()).length();
            tmpClass.setBeginOfClass(fileLength);
            tmpClass.setEndOfClass(fileLength);
            tmpClass.setFileName((String) fileList.getSelectedValue());
            parentFrame.selectedClass = tmpClass;
            parentFrame.writeNewFileFromClass(parentFrame.selectedClass.getBeginOfClass(), parentFrame.selectedClass.getEndOfClass(), parentFrame.selectedClass.getFileName(), parentFrame.textArea, true);
            parentFrame.application.mainFrame.writeToConsole("..." + System.getProperty("application.languageDictionary.#created") + ' ' + System.getProperty("application.languageDictionary.#class") + ' ' + parentFrame.selectedClass.getTitle() + ' ' + System.getProperty("application.languageDictionary.#in") + ' ' + System.getProperty("application.languageDictionary.#class") + ' ' + parentFrame.selectedClass.getSuperclass().getTitle() + ' ' + DateFormat.getTimeInstance().format(new Date()) + ", " + DateFormat.getDateInstance().format(new Date()) + "...", true);
            parentFrame.application.updateClassesWithFile(tmpClass.getFileName(), tmpClass.getTypeOfClass());
            parentFrame.updateContent();
            this.setVisible(false);
            this.dispose();
        }
    }

    /**
	 *  adds the "Application"-File to the list 
	 */
    private void addFileToList() {
        String fileName;
        JFileChooser chooser = new JFileChooser();
        String currentDirString = parentFrame.application.getPreference("lastJFileChooserDirectory");
        if (!currentDirString.equals("")) {
            File currentDirectory = new File(currentDirString);
            if (currentDirectory.exists() && currentDirectory.isDirectory()) {
                chooser.setCurrentDirectory(currentDirectory);
            }
        }
        int returnVal = chooser.showSaveDialog(parentFrame);
        parentFrame.application.getPreferences().getPreference("lastJFileChooserDirectory").setValue(chooser.getCurrentDirectory().toString());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File newFile = chooser.getSelectedFile();
            fileName = newFile.toString();
            ((DefaultListModel) fileList.getModel()).addElement(fileName);
            if (!newFile.exists()) {
                try {
                    BufferedWriter bW = new BufferedWriter(new FileWriter(newFile));
                    bW.write(("/**") + System.getProperty("line.separator"));
                    bW.write((" *") + System.getProperty("line.separator"));
                    bW.write((" */") + System.getProperty("line.separator"));
                    bW.close();
                } catch (IOException e) {
                }
            }
        }
        parentFrame.application.updateCompileMenus();
    }
}
