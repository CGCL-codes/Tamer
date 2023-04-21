package hu.sztaki.lpds.pgportal.wfeditor.client.pstudy.dialog;

import hu.sztaki.lpds.pgportal.wfeditor.client.Job;
import hu.sztaki.lpds.pgportal.wfeditor.client.WorkflowEditor;
import hu.sztaki.lpds.pgportal.wfeditor.client.dialog.PropertyDialogInterface;
import hu.sztaki.lpds.pgportal.wfeditor.client.jdl.dialog.JDLEditorDialog;
import hu.sztaki.lpds.pgportal.wfeditor.client.jdl.util.JDLGenerator;
import hu.sztaki.lpds.pgportal.wfeditor.client.jdl.util.JDLValidator;
import hu.sztaki.lpds.pgportal.wfeditor.client.pstudy.ParameterKey;
import hu.sztaki.lpds.pgportal.wfeditor.client.pstudy.ParameterKeyList;
import hu.sztaki.lpds.pgportal.wfeditor.client.pstudy.ParameterKeyNameParser;
import hu.sztaki.lpds.pgportal.wfeditor.client.utils.GridItem;
import hu.sztaki.lpds.pgportal.wfeditor.common.jdl.JDLDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 *
 * @author  cselti
 */
public class GeneratorJobPropertyDialog extends javax.swing.JDialog implements DocumentListener, PropertyDialogInterface {

    /** Creates new form ParametricPortPropertyDialog */
    public GeneratorJobPropertyDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        postInit();
        dialogProperties = new java.util.HashMap();
    }

    public GeneratorJobPropertyDialog(Job parentJob, boolean isModal) {
        super(parentJob.getParentObj().getParentObj().getFrame(), isModal);
        parent = parentJob;
        initComponents();
        postInit();
        dialogProperties = new java.util.HashMap();
    }

    private void postInit() {
        this.keysList.setModel(listModel);
        keysList.setPrototypeCellValue("keyname");
        this.setDialogTitle();
        this.masterDocument = inputTextArea.getDocument();
        masterDocument.addDocumentListener(this);
        leftField.getDocument().addDocumentListener(this);
        rightField.getDocument().addDocumentListener(this);
    }

    private void checkDialogProperties() {
        if (!dialogProperties.containsKey("leftDelimiter")) {
            this.setProperty("leftDelimiter", "<");
        }
        if (!dialogProperties.containsKey("rightDelimiter")) {
            this.setProperty("rightDelimiter", ">");
        }
        if (!dialogProperties.containsKey("inputText")) {
            this.setProperty("inputText", "");
        }
        if (!dialogProperties.containsKey("parameterKeyList")) {
            dialogProperties.put("parameterKeyList", new ParameterKeyList());
        }
    }

    private void setUpListModel() {
        listModel.clear();
        for (int i = 0; i < parameterList.size(); i++) {
            listModel.addElement(parameterList.get(i));
        }
    }

    private void setGridCombobox() {
        this.gridComboBox.removeAllItems();
        Vector grids = parent.getParentObj().getParentObj().getGrids();
        grids = grids == null ? new Vector() : grids;
        for (int i = 0; i < grids.size(); i++) {
            gridComboBox.addItem(((GridItem) grids.get(i)).getComboBoxItem());
        }
        if (dialogProperties.get("grid") != null) {
            gridComboBox.setSelectedItem(dialogProperties.get("grid"));
        }
    }

    public void setUpDialog() {
        dialogProperties = (HashMap) parent.getJobProperties().clone();
        this.checkDialogProperties();
        portNameTextField.setText((String) dialogProperties.get("name"));
        leftField.setText((String) dialogProperties.get("leftDelimiter"));
        rightField.setText((String) dialogProperties.get("rightDelimiter"));
        inputTextArea.setText((String) dialogProperties.get("inputText"));
        parameterList = (ParameterKeyList) ((ParameterKeyList) dialogProperties.get("parameterKeyList")).clone();
        if (parameterList == null) {
            parameterList = new ParameterKeyList();
        }
        setUpListModel();
        this.setGridCombobox();
        this.setMode();
    }

    private void checkBrokerSelected() {
        String gridName = gridComboBox.getSelectedItem().toString();
        this.jdlButton.setEnabled(JDLValidator.isBrokerResource(gridName));
    }

    public void setProperty(String key, String value) {
        this.dialogProperties.put(key, value);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        jobNameLabel = new javax.swing.JLabel();
        fileBrowserButton = new javax.swing.JButton();
        portNameTextField = new javax.swing.JTextField();
        delimiterPanel = new javax.swing.JPanel();
        leftLabel = new javax.swing.JLabel();
        leftField = new javax.swing.JTextField();
        rightLabel = new javax.swing.JLabel();
        rightField = new javax.swing.JTextField();
        delimiterLabel = new javax.swing.JLabel();
        editPanel = new javax.swing.JPanel();
        keysLabel = new javax.swing.JLabel();
        inputLabel = new javax.swing.JLabel();
        textScroll = new javax.swing.JScrollPane();
        inputTextArea = new javax.swing.JTextArea();
        keysScroll = new javax.swing.JScrollPane();
        keysList = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        parseTextButton = new javax.swing.JButton();
        gridLabel = new javax.swing.JLabel();
        gridComboBox = new javax.swing.JComboBox();
        jdlLabel = new javax.swing.JLabel();
        jdlButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 25, 25, 25));
        jobNameLabel.setText("Job name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(jobNameLabel, gridBagConstraints);
        fileBrowserButton.setText("Load from File...");
        fileBrowserButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBrowserButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel1.add(fileBrowserButton, gridBagConstraints);
        portNameTextField.setMinimumSize(new java.awt.Dimension(50, 20));
        portNameTextField.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(portNameTextField, gridBagConstraints);
        delimiterPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        leftLabel.setText("Left:");
        delimiterPanel.add(leftLabel);
        leftField.setColumns(5);
        leftField.setText("<");
        delimiterPanel.add(leftField);
        rightLabel.setText("Right:");
        delimiterPanel.add(rightLabel);
        rightField.setColumns(5);
        rightField.setText(">");
        delimiterPanel.add(rightField);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(delimiterPanel, gridBagConstraints);
        delimiterLabel.setText("Parametric key delimiter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(delimiterLabel, gridBagConstraints);
        editPanel.setLayout(new java.awt.GridBagLayout());
        keysLabel.setText("Keys:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        editPanel.add(keysLabel, gridBagConstraints);
        inputLabel.setText("Input file text:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        editPanel.add(inputLabel, gridBagConstraints);
        inputTextArea.setLineWrap(true);
        inputTextArea.setMinimumSize(new java.awt.Dimension(100, 100));
        textScroll.setViewportView(inputTextArea);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        editPanel.add(textScroll, gridBagConstraints);
        keysList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        keysList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                keysListMouseClicked(evt);
            }
        });
        keysScroll.setViewportView(keysList);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        editPanel.add(keysScroll, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 0.6;
        jPanel1.add(editPanel, gridBagConstraints);
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));
        okButton.setMnemonic('O');
        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(okButton);
        cancelButton.setMnemonic('C');
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);
        parseTextButton.setMnemonic('p');
        parseTextButton.setText("Parse");
        parseTextButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parseTextButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(parseTextButton);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        jPanel1.add(buttonPanel, gridBagConstraints);
        gridLabel.setText("Grid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 0);
        jPanel1.add(gridLabel, gridBagConstraints);
        gridComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 0);
        jPanel1.add(gridComboBox, gridBagConstraints);
        jdlLabel.setText("Special attributes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(jdlLabel, gridBagConstraints);
        jdlButton.setText("Attributes editor...");
        jdlButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jdlButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(jdlButton, gridBagConstraints);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void jdlButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.jdl_showJDLEditorDialog(evt);
    }

    private void gridComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        checkBrokerSelected();
    }

    private void fileBrowserButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
        }
        int status = fileChooser.showOpenDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            try {
                String text = "";
                File file = fileChooser.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (reader.ready()) {
                    text += reader.readLine();
                    text += '\n';
                }
                inputTextArea.setText(text);
            } catch (IOException e) {
            }
        }
    }

    private void keysListMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() > 1 && javax.swing.SwingUtilities.isLeftMouseButton(evt)) {
            this.showKeyDefinitionDialog();
        }
    }

    private void parseTextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            ParameterKeyNameParser parser = new ParameterKeyNameParser();
            Set set = parser.parse(inputTextArea.getText(), leftField.getText(), rightField.getText());
            int j = 0;
            for (Iterator i = set.iterator(); i.hasNext(); ) {
                String keyName = i.next().toString();
                Object obj = parameterList.getByName(keyName);
                ParameterKey key = (obj == null) ? new ParameterKey(keyName) : (ParameterKey) obj;
                parameterList.add(j, key);
                j++;
            }
            parameterList.subList(j, parameterList.size()).clear();
            this.setUpListModel();
            this.getDialogProperties();
            okButton.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        this.cancelCmd();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.okCmd();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.cancelCmd();
    }

    private void showKeyDefinitionDialog() {
        int keyIndex = keysList.getSelectedIndex();
        ParameterKey key = (ParameterKey) keysList.getSelectedValue();
        keyDefinitionDialog = new KeyDefinitionDialog(parent.getParentObj().getParentObj().getFrame(), (ParameterKey) key.clone());
        keyDefinitionDialog.setSize(600, 480);
        keyDefinitionDialog.setLocation((int) (parent.getParentFrame().getX() + parent.getParentFrame().getWidth() / 2 - keyDefinitionDialog.getWidth() / 2), (int) (parent.getParentFrame().getY() + parent.getParentFrame().getHeight() / 2 - keyDefinitionDialog.getHeight() / 2));
        keyDefinitionDialog.show(parent.getParentObj().getParentObj().getMode());
        if (keyDefinitionDialog.getStatus() == KeyDefinitionDialog.STATUS_VALUE_TRUE) {
            key = keyDefinitionDialog.getParameterKey();
            parameterList.set(keyIndex, key);
            this.listModel.set(keyIndex, key);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        GeneratorJobPropertyDialog dialog = new GeneratorJobPropertyDialog(frame, true);
        dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
        dialog.setSize(600, 520);
        dialog.show();
        System.exit(0);
    }

    public String getProperty(String type) {
        String property = new String("");
        if (type.equals(null)) property = ""; else if (type.equals("name")) property = portNameTextField.getText(); else if (type.equals("type")) {
        }
        return property;
    }

    public HashMap getDialogProperties() {
        dialogProperties.put("name", portNameTextField.getText());
        dialogProperties.put("leftDelimiter", leftField.getText());
        dialogProperties.put("rightDelimiter", rightField.getText());
        dialogProperties.put("inputText", inputTextArea.getText());
        dialogProperties.put("grid", gridComboBox.getSelectedItem() == null ? "" : gridComboBox.getSelectedItem().toString());
        dialogProperties.put("parameterKeyList", parameterList);
        return this.dialogProperties;
    }

    public void setMode() {
        if (parent.getParentObj().getParentObj().getMode() == WorkflowEditor.MODE_EDIT) {
            if (parent.getParentObj().getParentObj().getEditorRole().equals(WorkflowEditor.EDITOR_ROLE_VALUE_DEMO)) this.setDemoEditMode();
        } else if (parent.getParentObj().getParentObj().getMode() == WorkflowEditor.MODE_VIEW) {
            this.setViewMode();
        } else if (parent.getParentObj().getParentObj().getMode() == WorkflowEditor.MODE_RESCUE) {
            this.setRescueMode();
        }
    }

    private void setDemoEditMode() {
        enableComponents(false);
    }

    private void setViewMode() {
        enableComponents(false);
    }

    private void setRescueMode() {
        enableComponents(false);
        if (parent.getStatus() != Job.RUNNING_STATE_VALUE_FINISHED) {
            gridComboBox.setEnabled(true);
            okButton.setEnabled(true);
        }
    }

    private void enableComponents(boolean bool) {
        this.okButton.setEnabled(bool);
        this.leftField.setEnabled(bool);
        this.rightField.setEnabled(bool);
        this.inputTextArea.setEnabled(bool);
        this.fileBrowserButton.setEnabled(bool);
        this.portNameTextField.setEnabled(bool);
        this.parseTextButton.setEnabled(bool);
        this.gridComboBox.setEnabled(bool);
    }

    private void cancelCmd() {
        this.status = STATUS_VALUE_FALSE;
        this.hide();
    }

    private void okCmd() {
        if (isParseProperties()) {
            this.status = STATUS_VALUE_TRUE;
            this.hide();
        }
    }

    private boolean isParseProperties() {
        boolean ret = true;
        String errorStr = new String("");
        boolean nameErrorToCorrect = false;
        switch(parent.parseJobProperties(getDialogProperties())) {
            case Job.JOB_NAME_EXIST:
                nameErrorToCorrect = true;
                errorStr = "This job name exist. Please choose another one.";
                ret = false;
                break;
            case Job.JOB_NAME_FORMAT_WRONG:
                nameErrorToCorrect = true;
                errorStr = "                                   The job name format wrong!\n" + "               The first character must be only word character and digit,\n" + "the next ones must be the same plus underscore, dash and dot characters!\n";
                ret = false;
                break;
            case Job.JOB_EXECUTABLE_NOT_EXIST:
                errorStr = "The file name or the path of job executable is incorrect!";
                ret = false;
                break;
            case Job.PROCESS_NUMBER_FORMAT_WRONG:
                errorStr = "Wrong process number format: value must be in the range 1 - 65535!\n";
                ret = false;
                break;
            case Job.PROCESS_NUMBER_EMPTY:
                errorStr = "When the type of the job is set to MPI or PVM the 'Process number' field must not be empty!\n";
                ret = false;
                break;
            case Job.RESOURCE_NAME_EMPTY:
                errorStr = "No resource has been specified for this job!\n";
                ret = false;
                break;
            case Job.SPECIAL_GRID_PORT_CONFLICT:
                errorStr = " : No port referencing a remote file may be associated to a job submitting in a grid of type " + Job.SPEC_GRID_3GBRIDGE + " or " + Job.SPEC_GRID_PBS + " or " + Job.SPEC_GRID_LSF + "!\n";
                ret = false;
        }
        if (!ret) {
            int rets = JOptionPane.showConfirmDialog(parent.getParentObj().getParentObj().getFrame(), errorStr + "\nDo you want to correct it immediately?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (rets != JOptionPane.OK_OPTION) {
                ret = true;
                if (nameErrorToCorrect) {
                    String nameGenerated;
                    GregorianCalendar d = new GregorianCalendar();
                    nameGenerated = "TMP_" + d.get(Calendar.YEAR) + "_" + ((int) (1 + d.get(Calendar.MONTH))) + "_" + d.get(Calendar.DAY_OF_MONTH) + "_" + d.get(Calendar.HOUR_OF_DAY) + "_" + d.get(Calendar.MINUTE) + "_" + d.get(Calendar.SECOND);
                    parent.setName(nameGenerated);
                    dialogProperties.put(new String("name"), nameGenerated);
                    String message = "The system changed improper user defined Name of the job as \"" + nameGenerated + "\"";
                    JOptionPane.showMessageDialog(parent.getParentObj().getParentObj().getFrame(), message, "Warning", JOptionPane.WARNING_MESSAGE);
                    ret = false;
                    cancelCmd();
                }
            }
        }
        return ret;
    }

    private void jdl_showJDLEditorDialog(java.awt.event.ActionEvent event) {
        JDLDocument jdlDoc = (JDLDocument) dialogProperties.get("jdl");
        jdlDoc = (jdlDoc == null ? new JDLDocument(parent.getName()) : jdlDoc);
        jdlDoc.setJobName(parent.getName());
        JDLGenerator jdlUtil = new JDLGenerator(parent, jdlDoc);
        jdlUtil.generate(getDialogProperties());
        if (jdl_jdlEditorDialog == null) {
            jdl_jdlEditorDialog = new JDLEditorDialog(parent);
            jdl_jdlEditorDialog.setSize(this.getWidth() + 100, this.getHeight() - 20);
            jdl_jdlEditorDialog.setLocation((int) (this.getX() + this.getWidth() / 2 - jdl_jdlEditorDialog.getWidth() / 2), (int) (this.getY() + this.getHeight() / 2 - jdl_jdlEditorDialog.getHeight() / 2));
        }
        jdl_jdlEditorDialog.setJDLDocument((JDLDocument) jdlDoc.clone());
        jdl_jdlEditorDialog.show(parent.getParentObj().getParentObj().getMode());
        if (jdl_jdlEditorDialog.getStatus() == JDLEditorDialog.STATUS_VALUE_TRUE) {
            dialogProperties.put("jdl", jdl_jdlEditorDialog.getJDLDocument());
            System.out.println("JDLEditor - Status true");
        } else {
            System.out.println("JDLEditor - Status false");
        }
    }

    public short getStatus() {
        return status;
    }

    private void setDialogTitle() {
        this.setTitle(parent.getName() + " properties");
    }

    public void show() {
        super.show();
    }

    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        this.textChanged(e);
    }

    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        this.textChanged(e);
    }

    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        this.textChanged(e);
    }

    private void textChanged(DocumentEvent e) {
        try {
            if (e.getDocument().equals(inputTextArea.getDocument())) {
                okButton.setEnabled(dialogProperties.get("inputText").equals(inputTextArea.getText()));
            } else if (e.getDocument().equals(leftField.getDocument())) {
                okButton.setEnabled(dialogProperties.get("leftDelimiter").equals(leftField.getText()));
            } else if (e.getDocument().equals(rightField.getDocument())) {
                okButton.setEnabled(dialogProperties.get("rightDelimiter").equals(rightField.getText()));
            }
        } catch (Exception ex) {
            okButton.setEnabled(false);
        }
    }

    public void setInitFinished(boolean bool) {
    }

    public JDialog getDialog() {
        return this;
    }

    public JFrame getFrame() {
        return parent.getParentObj().getParentObj().getFrame();
    }

    private short status;

    public static final short STATUS_TYPE_VALID = 1;

    public static final short STATUS_VALUE_TRUE = 1;

    public static final short STATUS_VALUE_FALSE = 0;

    private java.util.HashMap dialogProperties;

    private Job parent;

    private String oldPortName;

    private boolean isPortInConnection;

    private ParameterKeyList parameterList;

    private DefaultListModel listModel = new DefaultListModel();

    private KeyDefinitionDialog keyDefinitionDialog;

    private JFileChooser fileChooser;

    private Document masterDocument;

    private JDLEditorDialog jdl_jdlEditorDialog;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JLabel delimiterLabel;

    private javax.swing.JPanel delimiterPanel;

    private javax.swing.JPanel editPanel;

    private javax.swing.JButton fileBrowserButton;

    private javax.swing.JComboBox gridComboBox;

    private javax.swing.JLabel gridLabel;

    private javax.swing.JLabel inputLabel;

    private javax.swing.JTextArea inputTextArea;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JButton jdlButton;

    private javax.swing.JLabel jdlLabel;

    private javax.swing.JLabel jobNameLabel;

    private javax.swing.JLabel keysLabel;

    private javax.swing.JList keysList;

    private javax.swing.JScrollPane keysScroll;

    private javax.swing.JTextField leftField;

    private javax.swing.JLabel leftLabel;

    private javax.swing.JButton okButton;

    private javax.swing.JButton parseTextButton;

    private javax.swing.JTextField portNameTextField;

    private javax.swing.JTextField rightField;

    private javax.swing.JLabel rightLabel;

    private javax.swing.JScrollPane textScroll;
}
