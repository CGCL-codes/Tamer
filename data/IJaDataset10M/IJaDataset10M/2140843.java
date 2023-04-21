package mya_dc.client.gui;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import mya_dc.client.core.ClientSettings;
import mya_dc.client.core.ClientSettings.EEditors;
import mya_dc.client.gui.TextMessages.EMessageBoxType;
import mya_dc.shared_classes.MYA_File;
import mya_dc.shared_classes.CompilationFlags.BasicOptimization;
import mya_dc.shared_classes.CompilationFlags.MachimeDepOptimization;
import mya_dc.shared_classes.gui.SharedComponents;

/**
 * The client's properties window
 * 
 * @author      Adam Levi
 * <br>			MYA
 */
public class PropertiesWindow extends JDialog {

    private static final long serialVersionUID = -5911790636783925274L;

    private JButton browseOutputDirButton;

    private JTextField outputDirtextField;

    private JTextField outputFilenametextField;

    private JRadioButton athlonK8RadioButton;

    private JRadioButton penitum4RadioButton;

    private JRadioButton genericnRadioButton;

    private JRadioButton optimize3RadioButton;

    private JRadioButton optimize2RadioButton;

    private JRadioButton optimize1RadioButton;

    private JRadioButton noOptimizationRadioButton;

    private ButtonGroup machineDepOptGroup = new ButtonGroup();

    private ButtonGroup baseOptGroup = new ButtonGroup();

    private JTextField editorPathTextLabel;

    private JList editorsList;

    private JTextField homeDirectoryTextField;

    private JPasswordField passwordField;

    private JTextField userNameTextField;

    private JTextField projectNameTextField;

    private JTextField portNumberTextField;

    private JTextField hostNameTextField;

    private JCheckBox savepasswordCheckBox;

    /**
	 * Create the dialog
	 */
    public PropertiesWindow(ClientSettings settings) {
        super();
        getContentPane().setBackground(SystemColor.controlHighlight);
        setTitle("Properties");
        getContentPane().setLayout(null);
        setBounds(100, 100, 500, 375);
        final JButton okButton = new JButton();
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                doOK();
            }
        });
        okButton.setText("OK");
        okButton.setBounds(286, 308, 95, 23);
        getContentPane().add(okButton);
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(10, 10, 472, 292);
        getContentPane().add(tabbedPane);
        final JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        tabbedPane.addTab("Project", null, panel_1, null);
        final JPanel panel_2 = new JPanel();
        panel_2.setBorder(new TitledBorder(null, "Authentication", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SystemColor.activeCaption));
        panel_2.setLayout(null);
        panel_2.setBounds(10, 10, 447, 141);
        panel_1.add(panel_2);
        final JLabel label_1 = new JLabel();
        label_1.setText("Project Name:");
        label_1.setBounds(10, 21, 73, 25);
        panel_2.add(label_1);
        projectNameTextField = new JTextField();
        projectNameTextField.setEditable(false);
        projectNameTextField.setBounds(89, 23, 163, 20);
        panel_2.add(projectNameTextField);
        final JLabel label_2 = new JLabel();
        label_2.setText("User Name:");
        label_2.setBounds(10, 52, 73, 25);
        panel_2.add(label_2);
        userNameTextField = new JTextField();
        userNameTextField.setEditable(false);
        userNameTextField.setBounds(89, 54, 163, 20);
        panel_2.add(userNameTextField);
        passwordField = new JPasswordField();
        passwordField.setBounds(89, 88, 141, 20);
        panel_2.add(passwordField);
        final JLabel label_3 = new JLabel();
        label_3.setText("Password:");
        label_3.setBounds(10, 86, 73, 25);
        panel_2.add(label_3);
        savepasswordCheckBox = new JCheckBox();
        savepasswordCheckBox.setText("Remember Password");
        savepasswordCheckBox.setBounds(236, 87, 125, 23);
        panel_2.add(savepasswordCheckBox);
        final JPanel connectionTabPanel = new JPanel();
        connectionTabPanel.setLayout(null);
        tabbedPane.addTab("Connection", null, connectionTabPanel, null);
        final JPanel masterServerPanel = new JPanel();
        masterServerPanel.setBounds(10, 10, 447, 89);
        connectionTabPanel.add(masterServerPanel);
        masterServerPanel.setLayout(null);
        masterServerPanel.setBorder(new TitledBorder(null, "Master Server", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SystemColor.activeCaption));
        final JLabel label_4 = new JLabel();
        label_4.setBounds(10, 21, 106, 25);
        masterServerPanel.add(label_4);
        label_4.setText("Host/IP address:");
        hostNameTextField = new JTextField();
        hostNameTextField.setBounds(122, 23, 278, 20);
        masterServerPanel.add(hostNameTextField);
        final JLabel label_5 = new JLabel();
        label_5.setBounds(10, 52, 92, 25);
        masterServerPanel.add(label_5);
        label_5.setText("Port number:");
        portNumberTextField = new JTextField();
        portNumberTextField.setBounds(122, 56, 278, 20);
        masterServerPanel.add(portNumberTextField);
        final JPanel panel_2_1 = new JPanel();
        panel_2_1.setBorder(new TitledBorder(null, "Home Directory", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SystemColor.activeCaption));
        panel_2_1.setLayout(null);
        panel_2_1.setBounds(10, 159, 447, 98);
        panel_1.add(panel_2_1);
        homeDirectoryTextField = new JTextField();
        homeDirectoryTextField.setEditable(false);
        homeDirectoryTextField.setBounds(10, 30, 326, 28);
        panel_2_1.add(homeDirectoryTextField);
        final JButton browseButton = new JButton();
        browseButton.setEnabled(false);
        browseButton.setText("Browse");
        browseButton.setBounds(342, 33, 95, 23);
        panel_2_1.add(browseButton);
        final JPanel panel = new JPanel();
        panel.setLayout(null);
        tabbedPane.addTab("Workspace", null, panel, null);
        final JPanel editorPanel = new JPanel();
        editorPanel.setBounds(10, 10, 447, 152);
        panel.add(editorPanel);
        editorPanel.setBorder(new TitledBorder(null, "Text Editor", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SystemColor.activeCaption));
        editorPanel.setLayout(null);
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(26, 30, 183, 66);
        editorPanel.add(scrollPane);
        editorsList = new JList();
        editorsList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(final ListSelectionEvent e) {
                switch(editorsList.getSelectedIndex()) {
                    case 0:
                        editorPathTextLabel.setText(ClientSettings.regEdPathNotepadPlusPlus);
                        break;
                    case 1:
                        editorPathTextLabel.setText(ClientSettings.regEdPathWindowsNotepad);
                        break;
                    case 2:
                        editorPathTextLabel.setText(ClientSettings.regEdPathVisualStudio);
                        break;
                }
            }
        });
        scrollPane.setViewportView(editorsList);
        editorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        editorsList.setModel(new DefaultComboBoxModel(new String[] { "Notepad++", "Notepad", "Visual Studio" }));
        final JLabel pathLabel = new JLabel();
        pathLabel.setText("Path:");
        pathLabel.setBounds(26, 102, 39, 20);
        editorPanel.add(pathLabel);
        editorPathTextLabel = new JTextField();
        editorPathTextLabel.setBounds(73, 102, 263, 20);
        editorPanel.add(editorPathTextLabel);
        final JButton browseButton_1 = new JButton();
        browseButton_1.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                String path = getLocation(JFileChooser.FILES_ONLY);
                if (path == "") return;
                editorPathTextLabel.setText(path);
            }
        });
        browseButton_1.setText("Browse");
        browseButton_1.setBounds(342, 101, 95, 23);
        editorPanel.add(browseButton_1);
        final JPanel panel_3 = new JPanel();
        panel_3.setLayout(null);
        tabbedPane.addTab("Compilation", null, panel_3, null);
        final JPanel basicOptPanel = new JPanel();
        basicOptPanel.setBorder(new TitledBorder(null, "Basic Optimizations", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SystemColor.activeCaption));
        basicOptPanel.setLayout(null);
        basicOptPanel.setBounds(10, 10, 232, 154);
        panel_3.add(basicOptPanel);
        noOptimizationRadioButton = new JRadioButton();
        baseOptGroup.add(noOptimizationRadioButton);
        noOptimizationRadioButton.setText("No Optimization");
        noOptimizationRadioButton.setBounds(27, 28, 113, 23);
        basicOptPanel.add(noOptimizationRadioButton);
        optimize1RadioButton = new JRadioButton();
        baseOptGroup.add(optimize1RadioButton);
        optimize1RadioButton.setText("Optimize 1 (g++ -O1)");
        optimize1RadioButton.setBounds(27, 57, 143, 23);
        basicOptPanel.add(optimize1RadioButton);
        optimize2RadioButton = new JRadioButton();
        baseOptGroup.add(optimize2RadioButton);
        optimize2RadioButton.setText("Optimize 2 (g++ -O2)");
        optimize2RadioButton.setBounds(27, 86, 143, 23);
        basicOptPanel.add(optimize2RadioButton);
        optimize3RadioButton = new JRadioButton();
        baseOptGroup.add(optimize3RadioButton);
        optimize3RadioButton.setText("Max Optimization (g++ -O3)");
        optimize3RadioButton.setBounds(27, 115, 192, 23);
        basicOptPanel.add(optimize3RadioButton);
        final JPanel macDepOptPanel = new JPanel();
        macDepOptPanel.setToolTipText("Change only if you plan to use the code on specific hardware");
        macDepOptPanel.setLayout(null);
        macDepOptPanel.setLayout(null);
        macDepOptPanel.setBorder(new TitledBorder(null, "Machine dependent optimizations", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SystemColor.activeCaption));
        macDepOptPanel.setBounds(248, 10, 209, 154);
        panel_3.add(macDepOptPanel);
        genericnRadioButton = new JRadioButton();
        machineDepOptGroup.add(genericnRadioButton);
        genericnRadioButton.setBounds(26, 25, 107, 23);
        genericnRadioButton.setText("Generic (Default)");
        macDepOptPanel.add(genericnRadioButton);
        penitum4RadioButton = new JRadioButton();
        machineDepOptGroup.add(penitum4RadioButton);
        penitum4RadioButton.setText("Pentium 4/4M");
        penitum4RadioButton.setBounds(26, 54, 91, 23);
        macDepOptPanel.add(penitum4RadioButton);
        athlonK8RadioButton = new JRadioButton();
        machineDepOptGroup.add(athlonK8RadioButton);
        athlonK8RadioButton.setText("Athlon K8/64");
        athlonK8RadioButton.setBounds(26, 83, 87, 23);
        macDepOptPanel.add(athlonK8RadioButton);
        final JPanel outputFileNamePanel = new JPanel();
        outputFileNamePanel.setLayout(null);
        outputFileNamePanel.setBorder(new TitledBorder(null, "Output filename", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        outputFileNamePanel.setBounds(306, 170, 151, 81);
        panel_3.add(outputFileNamePanel);
        outputFilenametextField = new JTextField();
        outputFilenametextField.setBounds(21, 33, 107, 20);
        outputFileNamePanel.add(outputFilenametextField);
        final JPanel outputDirPanel = new JPanel();
        outputDirPanel.setBounds(10, 170, 290, 81);
        panel_3.add(outputDirPanel);
        outputDirPanel.setBorder(new TitledBorder(null, "Output directory", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        outputDirPanel.setLayout(null);
        outputDirtextField = new JTextField();
        outputDirtextField.setBounds(21, 33, 162, 20);
        outputDirPanel.add(outputDirtextField);
        browseOutputDirButton = new JButton();
        browseOutputDirButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                String dir = getLocation(JFileChooser.DIRECTORIES_ONLY);
                if (dir == "") return;
                outputDirtextField.setText(dir);
            }
        });
        browseOutputDirButton.setText("browse");
        browseOutputDirButton.setBounds(189, 32, 91, 23);
        outputDirPanel.add(browseOutputDirButton);
        m_Settings = settings;
        loadSettings();
        final JButton cancelButton = new JButton();
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                doCancel();
            }
        });
        cancelButton.setBounds(387, 308, 95, 23);
        getContentPane().add(cancelButton);
        cancelButton.setText("Cancel");
    }

    private ClientSettings m_Settings;

    /**
	 * opens a JFileChooser and returns the path the user entered
	 * 
	 * @param type - the type of files to get (see JFileChooser)
	 * 
	 * @return the path the user entered
	 */
    private String getLocation(int type) {
        return SharedComponents.getLocation(type, this);
    }

    /**
	 * checks if the input is valid, if it's not valid an error window
	 * will be opened.
	 * 
	 * @return true if valid, false otherwise
	 */
    private boolean validSettings() {
        LinkedList<EMessageBoxType> errors = new LinkedList<EMessageBoxType>();
        if (passwordField.getPassword().length == 0 && savepasswordCheckBox.isSelected()) errors.add(EMessageBoxType.ERROR_MISSING_PASSWORD);
        if (hostNameTextField.getText().equals("")) errors.add(EMessageBoxType.ERROR_MISSING_MASTER_HOSTNAME);
        try {
            Integer.parseInt(portNumberTextField.getText());
        } catch (Exception e) {
            errors.add(EMessageBoxType.ERROR_MISSING_PORT_NUMBER);
        }
        boolean flag = false;
        try {
            File f = new File(outputDirtextField.getText());
            if (!MYA_File.validFilename(outputFilenametextField.getText())) flag = true; else f.mkdirs();
        } catch (Exception e) {
            flag = true;
        }
        if (flag) errors.add(EMessageBoxType.ERROR_BAD_OUTPUT_FILENAME);
        File editorFile = new File(editorPathTextLabel.getText());
        if (!editorFile.exists() || editorFile.isDirectory()) errors.add(EMessageBoxType.ERROR_BAD_EDITOR_PATH);
        if (errors.size() > 0) {
            TextMessages.showMessageBox(errors, null);
            return false;
        }
        return true;
    }

    /**
	 * closes the window
	 */
    private void doCancel() {
        this.dispose();
    }

    /**
	 * saves the settings and closes the window
	 */
    private void doOK() {
        if (saveSettings() == -1) return;
        this.dispose();
    }

    /**
	 * check if the settings are valid, and saves if they are valid
	 * 
	 * @return 0 for success, -1 otherwise
	 */
    private int saveSettings() {
        if (!validSettings()) return -1;
        m_Settings.masterHostName = hostNameTextField.getText();
        m_Settings.setMasterPort(portNumberTextField.getText());
        saveCompilationFlags();
        m_Settings.outputDirectory = outputDirtextField.getText();
        m_Settings.editorPath = editorPathTextLabel.getText();
        m_Settings.editor = getSelectedEditor();
        m_Settings.savePassword = savepasswordCheckBox.isSelected();
        m_Settings.password = (m_Settings.savePassword ? String.valueOf(passwordField.getPassword()) : "");
        return 0;
    }

    /**
	 * loads the settings from ClientSettings
	 */
    private void loadSettings() {
        projectNameTextField.setText(m_Settings.projectName);
        userNameTextField.setText(m_Settings.username);
        passwordField.setText(m_Settings.password);
        hostNameTextField.setText(m_Settings.masterHostName);
        portNumberTextField.setText(Integer.toString(m_Settings.masterPort));
        homeDirectoryTextField.setText(m_Settings.workingDirectory);
        switch(m_Settings.editor) {
            case NOTEPAD_PLUS_PLUS:
                {
                    editorsList.setSelectedIndex(0);
                    break;
                }
            case NOTEPAD_WINDOWS:
                {
                    editorsList.setSelectedIndex(1);
                    break;
                }
            case MICROSOFT_VISUAL_STUDIO:
                {
                    editorsList.setSelectedIndex(2);
                    break;
                }
        }
        editorPathTextLabel.setText(m_Settings.editorPath);
        savepasswordCheckBox.setSelected(m_Settings.savePassword);
        switch(m_Settings.compFlags.basicOpt) {
            case NONE:
                noOptimizationRadioButton.setSelected(true);
                break;
            case O1:
                optimize1RadioButton.setSelected(true);
                break;
            case O2:
                optimize1RadioButton.setSelected(true);
                break;
            case O3_MAX:
                optimize3RadioButton.setSelected(true);
                break;
        }
        switch(m_Settings.compFlags.machineDepOpt) {
            case GENERIC:
                genericnRadioButton.setSelected(true);
                break;
            case PENTIUM4M:
                penitum4RadioButton.setSelected(true);
                break;
            case ATHLON_K8:
                athlonK8RadioButton.setSelected(true);
                break;
        }
        outputFilenametextField.setText(m_Settings.compFlags.outputFileName);
        outputDirtextField.setText(m_Settings.outputDirectory);
    }

    /**
	 * gets the selected editor from the GUI
	 */
    private EEditors getSelectedEditor() {
        switch(editorsList.getSelectedIndex()) {
            case 0:
                return EEditors.NOTEPAD_PLUS_PLUS;
            case 1:
                return EEditors.NOTEPAD_WINDOWS;
            case 2:
                return EEditors.MICROSOFT_VISUAL_STUDIO;
        }
        return null;
    }

    /**
	 * saves the compilation flags
	 */
    private void saveCompilationFlags() {
        if (noOptimizationRadioButton.isSelected()) m_Settings.compFlags.basicOpt = BasicOptimization.NONE; else if (optimize1RadioButton.isSelected()) m_Settings.compFlags.basicOpt = BasicOptimization.O1; else if (optimize2RadioButton.isSelected()) m_Settings.compFlags.basicOpt = BasicOptimization.O2; else if (optimize3RadioButton.isSelected()) m_Settings.compFlags.basicOpt = BasicOptimization.O3_MAX;
        if (genericnRadioButton.isSelected()) m_Settings.compFlags.machineDepOpt = MachimeDepOptimization.GENERIC; else if (penitum4RadioButton.isSelected()) m_Settings.compFlags.machineDepOpt = MachimeDepOptimization.PENTIUM4M; else if (athlonK8RadioButton.isSelected()) m_Settings.compFlags.machineDepOpt = MachimeDepOptimization.ATHLON_K8;
        m_Settings.compFlags.outputFileName = outputFilenametextField.getText();
    }
}
