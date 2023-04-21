package org.mitre.rt.client.ui.platform.specification.logicaltest;

import org.mitre.rt.client.ui.cchecks.*;
import org.mitre.rt.client.ui.files.ReferencedFilesDialog;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;
import org.mitre.cpe.language.x20.CheckFactRefType;
import org.mitre.cpe.language.x20.LogicalTestType;
import org.mitre.rt.client.exceptions.RTClientException;
import org.mitre.rt.client.ui.requiredcomponents.RequiredComponent;
import org.mitre.rt.client.ui.requiredcomponents.RequiredComponentFactory;
import org.mitre.rt.client.ui.requiredcomponents.RequiredSet;
import org.mitre.rt.client.ui.requiredcomponents.RequiredSetItem;
import org.mitre.rt.client.util.GlobalUITools;
import org.mitre.rt.client.util.StringUtils;
import org.mitre.rt.client.xml.CheckLanguageHelper;
import org.mitre.rt.client.xml.ComplianceCheckHelper;
import org.mitre.rt.client.xml.FileTypeHelper;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.ApplicationType.Files;
import org.mitre.rt.rtclient.CheckContentType;
import org.mitre.rt.rtclient.CheckLanguageType;
import org.mitre.rt.rtclient.FileType;

/**
 *
 * @author BAKERJ, BWORRELL
 */
public class CheckFactRefPanel extends javax.swing.JPanel {

    private static Logger logger = Logger.getLogger(CheckFactRefPanel.class.getPackage().getName());

    private ApplicationType application = null;

    private CheckFactRefType checkFactRef = null;

    private LogicalTestType logicalTest = null;

    private String checkFilePath = null;

    private RequiredComponent requiredText = RequiredComponentFactory.getRequiredComponent(RequiredComponentFactory.TEXT), requiredCombo = RequiredComponentFactory.getRequiredComponent(RequiredComponentFactory.COMBOBOX);

    private RequiredSet rs = new RequiredSet();

    private LanguageIdToLanguageConverter languageIdToLanguageConverter = null;

    private boolean changed = false;

    private boolean fileImported = false;

    private File changedFile = null;

    private String initialName = "";

    private int initialSelectedLanguage = -1;

    private final ComplianceCheckHelper complianceCheckHelper = new ComplianceCheckHelper();

    private final CheckLanguageHelper checkLanguageHelper = new CheckLanguageHelper();

    private final FileTypeHelper fileTypeHelper = new FileTypeHelper();

    /** Creates new form CheckContentTypePanel */
    public CheckFactRefPanel() {
        initComponents();
    }

    /** Creates new form CheckContentTypePanel */
    public CheckFactRefPanel(final ApplicationType application, LogicalTestType logicalTest, final CheckFactRefType checkFactRef) {
        this.application = application;
        this.checkFactRef = checkFactRef;
        this.logicalTest = logicalTest;
        this.languageIdToLanguageConverter = new LanguageIdToLanguageConverter(application);
        initComponents();
        setupUI();
        this.setInitialChangeValues();
        this.registerRequiredComponents();
        this.addChangeListeners();
        super.validate();
    }

    private void setupUI() {
        if (this.checkFactRef.getHref() != null) {
            List<FileType> allFiles = this.application.getFiles().getFileList();
            String href = this.checkFactRef.getHref();
            FileType ft = this.fileTypeHelper.getItem(allFiles, href);
            String origFilename = ft.getOrigFileName();
            this.txtFile.setText(origFilename);
        }
        List<CheckLanguageType> allCheckLanguages = (this.application.isSetCheckLanguages()) ? this.application.getCheckLanguages().getCheckLanguageList() : Collections.<CheckLanguageType>emptyList();
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(allCheckLanguages.toArray(new CheckLanguageType[0]));
        this.comboLanguage.setModel(comboModel);
        this.comboLanguage.repaint();
        if (this.checkFactRef.getSystem() != null) {
            String system = this.checkFactRef.getSystem();
            for (int i = 0; i < allCheckLanguages.size(); ++i) {
                CheckLanguageType clt = allCheckLanguages.get(i);
                if (clt.getNamespaceURI().equals(system)) {
                    this.comboLanguage.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (allCheckLanguages.isEmpty()) {
            GlobalUITools.enableComponentsWithinContainer(panelBottom, false);
            GlobalUITools.enableComponentsWithinContainer(panelTop, false);
        }
        if (this.checkFactRef.getIdRef() != null) {
            String idref = this.checkFactRef.getIdRef();
            this.txtName.setText(idref);
        }
    }

    private void setInitialChangeValues() {
        this.initialName = this.checkFactRef.getIdRef();
        this.initialSelectedLanguage = this.comboLanguage.getSelectedIndex();
        this.checkFilePath = this.txtFile.getText();
    }

    private void registerRequiredComponents() {
        RequiredSetItem checkLanguageItem = new RequiredSetItem(this.comboLanguage, null, this.requiredCombo), fileItem = new RequiredSetItem(this.txtFile, null, this.requiredText), nameItem = new RequiredSetItem(this.txtName, null, this.requiredText);
        this.rs.registerSetItems(this.txtFile, checkLanguageItem);
        this.rs.registerSetItems(this.comboLanguage, fileItem);
        this.rs.registerSetItems(this.txtName, checkLanguageItem);
        this.rs.registerSetItems(this.txtName, fileItem);
        this.requiredText.registerComponent(this.txtName, null);
    }

    private void addChangeListeners() {
        this.txtFile.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent evt) {
                setChanged(true);
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                setChanged(true);
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                setChanged(true);
            }
        });
        this.txtName.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent evt) {
                setChanged(txtName.getText().equals(initialName) == false);
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                setChanged(txtName.getText().equals(initialName) == false);
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                return;
            }
        });
        this.comboLanguage.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                setChanged(comboLanguage.getSelectedIndex() != initialSelectedLanguage);
            }
        });
    }

    /**
     * Commit any changes.
     */
    public void commitChanges() throws Exception {
        logger.debug("commiting check-fact-ref");
        if (this.fileImported) {
            handleFileChoice(changedFile);
        }
        if (!StringUtils.isWhitespaceOnlyOrNull(this.txtName.getText())) {
            this.checkFactRef.setIdRef(this.txtName.getText());
        }
        if (this.comboLanguage.getSelectedIndex() != -1) {
            Object obj = this.comboLanguage.getSelectedItem();
            CheckLanguageType checkLanguage = (CheckLanguageType) obj;
            this.checkFactRef.setSystem(checkLanguage.getNamespaceURI());
        }
        CheckFactRefType newCheckFactRef = this.logicalTest.addNewCheckFactRef();
        newCheckFactRef.set(this.checkFactRef);
    }

    public void handleFileChoice(File file) throws Exception {
        FileTypeHelper helper = new FileTypeHelper();
        String filePath = file.getAbsolutePath();
        if (helper.isValidFile(filePath) == true) {
            Files files = (application.isSetFiles() == true) ? application.getFiles() : application.addNewFiles();
            FileType newFile = helper.getNewItem(application);
            FileType tmpFile = files.addNewFile();
            newFile.setOrigFileName(file.getName());
            newFile.setOrigPath(helper.getFileDir(filePath));
            helper.relocateFile(application, newFile, file);
            tmpFile.set(newFile);
            this.checkFactRef.setHref(tmpFile.getId());
        } else {
            throw new RTClientException("The specified file is not valid!");
        }
    }

    /**
     * Validate that all required fields are set correctly.
     *
     * @param errorList
     * @return
     */
    public boolean validateFields(List<String> errorList) {
        boolean valid = true;
        if (this.fileImported) {
            if (this.changedFile == null) {
                errorList.add("Content - File has not been set.");
                valid = false;
            } else {
                if (!this.changedFile.exists()) {
                    errorList.add("Content - File does not exist.");
                    valid = false;
                }
                if (!this.changedFile.canRead()) {
                    errorList.add("Content - File cannot be read.");
                    valid = false;
                }
            }
        } else {
            if (StringUtils.isWhitespaceOnlyOrNull(this.txtFile.getText())) {
                errorList.add("Content - File is required.");
                valid = false;
            }
        }
        if (this.comboLanguage.getSelectedIndex() == -1) {
            errorList.add("Content - Languge is required.");
            valid = false;
        }
        if (this.txtName.getText().isEmpty()) {
            errorList.add("Content - Name is required");
            valid = false;
        }
        return valid;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        fileOpenDialog = new javax.swing.JFileChooser();
        panelTop = new javax.swing.JPanel();
        panelFileName = new javax.swing.JPanel();
        lblFile = new javax.swing.JLabel();
        txtFile = new javax.swing.JTextField();
        panelButtons = new javax.swing.JPanel();
        browseButton = new javax.swing.JButton();
        separator = new javax.swing.JSeparator();
        reuseButton = new javax.swing.JButton();
        separatorMain = new javax.swing.JSeparator();
        panelBottom = new javax.swing.JPanel();
        panelLanguage = new javax.swing.JPanel();
        lblLanguage = new javax.swing.JLabel();
        comboLanguage = new javax.swing.JComboBox();
        panelName = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        setMinimumSize(new java.awt.Dimension(625, 53));
        setPreferredSize(new java.awt.Dimension(625, 205));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        panelTop.setLayout(new javax.swing.BoxLayout(panelTop, javax.swing.BoxLayout.LINE_AXIS));
        panelFileName.setMaximumSize(new java.awt.Dimension(2147483647, 24));
        panelFileName.setMinimumSize(new java.awt.Dimension(62, 24));
        panelFileName.setPreferredSize(new java.awt.Dimension(350, 24));
        panelFileName.setLayout(new javax.swing.BoxLayout(panelFileName, javax.swing.BoxLayout.LINE_AXIS));
        lblFile.setText("Source File:");
        lblFile.setMaximumSize(new java.awt.Dimension(75, 24));
        lblFile.setMinimumSize(new java.awt.Dimension(75, 24));
        lblFile.setPreferredSize(new java.awt.Dimension(75, 24));
        panelFileName.add(lblFile);
        lblFile.getAccessibleContext().setAccessibleName("");
        txtFile.setEditable(false);
        txtFile.setMaximumSize(new java.awt.Dimension(300, 23));
        txtFile.setMinimumSize(new java.awt.Dimension(6, 23));
        txtFile.setPreferredSize(new java.awt.Dimension(300, 23));
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, this, org.jdesktop.beansbinding.ELProperty.create("${checkFilePath}"), txtFile, org.jdesktop.beansbinding.BeanProperty.create("text_ON_FOCUS_LOST"));
        bindingGroup.addBinding(binding);
        panelFileName.add(txtFile);
        panelTop.add(panelFileName);
        panelButtons.setLayout(new javax.swing.BoxLayout(panelButtons, javax.swing.BoxLayout.LINE_AXIS));
        browseButton.setText("Import");
        browseButton.setToolTipText("Browse the local file system and import the selected file into the RT.");
        browseButton.setMaximumSize(new java.awt.Dimension(75, 23));
        browseButton.setMinimumSize(new java.awt.Dimension(75, 23));
        browseButton.setPreferredSize(new java.awt.Dimension(75, 23));
        browseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        panelButtons.add(browseButton);
        separator.setMaximumSize(new java.awt.Dimension(2, 0));
        separator.setMinimumSize(new java.awt.Dimension(2, 0));
        separator.setPreferredSize(new java.awt.Dimension(2, 0));
        panelButtons.add(separator);
        reuseButton.setText("Select");
        reuseButton.setToolTipText("Select from a list of files used in other compliance checks");
        reuseButton.setMaximumSize(new java.awt.Dimension(75, 23));
        reuseButton.setMinimumSize(new java.awt.Dimension(75, 23));
        reuseButton.setPreferredSize(new java.awt.Dimension(75, 23));
        reuseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reuseButtonActionPerformed(evt);
            }
        });
        panelButtons.add(reuseButton);
        panelTop.add(panelButtons);
        add(panelTop);
        separatorMain.setMaximumSize(new java.awt.Dimension(0, 5));
        separatorMain.setMinimumSize(new java.awt.Dimension(0, 5));
        separatorMain.setPreferredSize(new java.awt.Dimension(0, 5));
        add(separatorMain);
        panelBottom.setLayout(new javax.swing.BoxLayout(panelBottom, javax.swing.BoxLayout.LINE_AXIS));
        panelLanguage.setMaximumSize(new java.awt.Dimension(2147483647, 24));
        panelLanguage.setMinimumSize(new java.awt.Dimension(261, 24));
        panelLanguage.setPreferredSize(new java.awt.Dimension(261, 24));
        panelLanguage.setLayout(new javax.swing.BoxLayout(panelLanguage, javax.swing.BoxLayout.LINE_AXIS));
        lblLanguage.setText("Language:");
        lblLanguage.setMaximumSize(new java.awt.Dimension(75, 24));
        lblLanguage.setMinimumSize(new java.awt.Dimension(75, 24));
        lblLanguage.setPreferredSize(new java.awt.Dimension(75, 24));
        panelLanguage.add(lblLanguage);
        lblLanguage.getAccessibleContext().setAccessibleName("");
        comboLanguage.setMaximumSize(new java.awt.Dimension(200, 24));
        comboLanguage.setMinimumSize(new java.awt.Dimension(100, 24));
        comboLanguage.setPreferredSize(new java.awt.Dimension(200, 24));
        comboLanguage.setRenderer(new CheckLanguageTypeListCellRenderer());
        panelLanguage.add(comboLanguage);
        panelBottom.add(panelLanguage);
        panelName.setMaximumSize(new java.awt.Dimension(345, 24));
        panelName.setMinimumSize(new java.awt.Dimension(345, 24));
        panelName.setPreferredSize(new java.awt.Dimension(345, 24));
        panelName.setRequestFocusEnabled(false);
        panelName.setLayout(new javax.swing.BoxLayout(panelName, javax.swing.BoxLayout.LINE_AXIS));
        lblName.setText("Name:");
        lblName.setMaximumSize(new java.awt.Dimension(45, 24));
        lblName.setMinimumSize(new java.awt.Dimension(45, 24));
        lblName.setPreferredSize(new java.awt.Dimension(45, 24));
        panelName.add(lblName);
        txtName.setFont(new java.awt.Font("Tahoma", 0, 13));
        txtName.setMaximumSize(new java.awt.Dimension(300, 24));
        txtName.setMinimumSize(new java.awt.Dimension(200, 200));
        txtName.setPreferredSize(new java.awt.Dimension(300, 24));
        panelName.add(txtName);
        panelBottom.add(panelName);
        add(panelBottom);
        bindingGroup.bind();
    }

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int fileOpenResult = JFileChooser.CANCEL_OPTION;
        File openFile = null;
        String fileName = null;
        this.fileOpenDialog = new JFileChooser();
        fileOpenResult = this.fileOpenDialog.showOpenDialog(this);
        if (fileOpenResult == JFileChooser.APPROVE_OPTION) {
            openFile = this.fileOpenDialog.getSelectedFile();
            fileName = openFile.getPath();
            this.txtFile.setText(fileName);
            this.setCheckFilePath(fileName);
            this.fileImported = true;
            this.changedFile = openFile;
        }
    }

    /**
     * Point to a file used by a compliance check (this is sorta weird but i think it works within the context of 
     * check-fact-refs)
     * @param evt 
     */
    private void reuseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Collection<FileType> files = complianceCheckHelper.getAllReferencedFiles(application);
        logger.debug("reuseButtonActionPerformed: found " + files.size() + " files used in compliance checks.");
        ReferencedFilesDialog dialog = new ReferencedFilesDialog(null, true, files);
        dialog.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("data_changed") == true) {
                    Object value = evt.getNewValue();
                    if (value != null && value instanceof FileType) {
                        setCheckFactRefHref((FileType) value);
                        fileImported = false;
                    }
                }
            }
        });
        dialog.setVisible(true);
    }

    /**
     * + Sets the checkcontent element of the Compliance Check if it is not present.
     * + Adds file reference to the checkcontent element if it is new or different than a previous ref
     * + Marks the FileType as modified (if it isn't new [this is handled in the VersionedItemTypeHelper])
     *   - This helps with synchronization problems that might arise if one client deletes the FileType while
     *     this compliance check is still using it.
     *
     * @param file
     */
    private void setCheckFactRefHref(FileType file) {
        this.checkFactRef.setHref(file.getId());
        fileTypeHelper.markModified(file);
        txtFile.setText(file.getOrigFileName());
        setCheckFilePath(file.getOrigFileName());
        setChanged(true);
    }

    private javax.swing.JButton browseButton;

    private javax.swing.JComboBox comboLanguage;

    private javax.swing.JFileChooser fileOpenDialog;

    private javax.swing.JLabel lblFile;

    private javax.swing.JLabel lblLanguage;

    private javax.swing.JLabel lblName;

    private javax.swing.JPanel panelBottom;

    private javax.swing.JPanel panelButtons;

    private javax.swing.JPanel panelFileName;

    private javax.swing.JPanel panelLanguage;

    private javax.swing.JPanel panelName;

    private javax.swing.JPanel panelTop;

    private javax.swing.JButton reuseButton;

    private javax.swing.JSeparator separator;

    private javax.swing.JSeparator separatorMain;

    private javax.swing.JTextField txtFile;

    private javax.swing.JTextField txtName;

    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    /**
     * @return the application
     */
    public ApplicationType getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(ApplicationType application) {
        this.application = application;
    }

    public CheckFactRefType getCheckFactRef() {
        return checkFactRef;
    }

    public void setCheckFactRef(CheckFactRefType checkFactRef) {
        this.checkFactRef = checkFactRef;
    }

    /**
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * @return the checkFile
     */
    public String getCheckFilePath() {
        return this.checkFilePath;
    }

    /**
     * @param checkFilePath the checkFilePath to set
     */
    public void setCheckFilePath(String checkFilePath) {
        this.checkFilePath = checkFilePath;
    }
}
