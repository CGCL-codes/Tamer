package icm.unicore.plugins.amber;

import java.io.*;
import java.util.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.net.*;
import org.unicore.ajo.ScriptType;
import org.unicore.resources.*;
import org.unicore.sets.*;
import com.pallas.unicore.client.UserDefaults;
import com.pallas.unicore.client.editor.RemoteTextEditor;
import com.pallas.unicore.client.panels.*;
import com.pallas.unicore.client.remotefilechooser.*;
import com.pallas.unicore.connection.*;
import com.pallas.unicore.container.*;
import com.pallas.unicore.extensions.*;
import com.pallas.unicore.resourcemanager.*;
import com.pallas.unicore.utility.UserMessages;
import com.pallas.unicore.client.UserDefaults;

/**
 *  Panel to define, load or save a script and set its parameters This class represents the lsMD
 *  plugin container in the client GUI's data area
 *
 *  Podziekowania za pomoc dla pycia :-)
 *
 *@author     Lukasz Skorwider
 *@created    12-12-2001
 */
public class lsMDJPAPanel extends JPAPanel {

    Vector versions;

    DefaultComboBoxModel versionComboBoxModel;

    JComboBox versionComboBox;

    private FileImportPanel importPanel;

    private FileExportPanel exportPanel;

    private OptionPanel optionsPanel;

    private JPanel filesPanel;

    private JPanel advancedPanel;

    private JTabbedPane tabbedPane;

    private blink generateBlink;

    private FileImport[] mdinFiles;

    private FileExport[] mdoutFiles;

    static String _defaultTAUTP = new String("1.0");

    static String _defaultTEMP0 = new String("300.0");

    static String _defaultNSTLIM = new String("1");

    static String _defaultDT = new String("0.001");

    private String strEnterJN, strSimulationType, strEditor, strFiles, strSave, strSaveAs, strSimulationTypeTT, strSaveTT, strSaveAsTT, strGenerate, strFind, strFindTT, strGenerateTT, strOptions, strChooseOne, strAdvanced, strAdvancedBorder, strHideTab, strYes, strNo, strAlways, strNever, strCheckParameters, strCheckFiles, strNotFound, strVersion;

    String allParams;

    GridBagLayout gridbag = new GridBagLayout();

    GridBagConstraints constraints = new GridBagConstraints();

    JPanel labelTextPanel = new JPanel(gridbag);

    private JButton[] codeButton;

    private JButton loadButton, saveButton, saveAsButton, resourceButton, browseButton, moleculeButton, generateButton, findButton, inpcrdButton, prmtopButton, mdoutButton, refcButton, mdcrdButton, mdvelButton, mdenButton, mdinfoButton, radButton, restartButton;

    private Vector sources;

    private ComboBoxModel sourceComboBoxModel;

    private JComboBox sourceComboBox;

    private Vector types;

    private ComboBoxModel typeComboBoxModel;

    private JComboBox typeComboBox;

    private JComboBox stComboBox, modelComboBox, theoryComboBox, basisComboBox, chargeComboBox, multiplicityComboBox, parameterComboBox;

    private JTextField jnTextField, filejnTextField, statusTextField, inpcrdPath, refcPath, prmtopPath, radPath, restartPath, mdoutPath, mdcrdPath, mdvelPath, mdenPath, mdinfoPath;

    private JTextField[] param;

    private MDParameter[] parameters;

    private int[] sortedParam;

    private MDPanel[] panels;

    private JPanel[] paramPanel;

    private JButton[] defaultButton;

    private JButton[] helpButton;

    private JLabel[] labelOption;

    private JLabel[] labelText;

    private JPanel[] panel;

    private JScrollPane[] spanel;

    private JLabel[] label;

    private int numParameters, numPanels;

    private String codeContent;

    private int numCodes;

    private String[] codeName;

    private JLabel stLabel;

    private JTextArea editArea;

    private JScrollPane editScrollPane;

    private boolean modelb;

    private String filename;

    private lsMDContainer container;

    private ParamListener paramListener = new ParamListener();

    private ButtonListener buttonListener = new ButtonListener();

    private helpButtonListener HelpButtonListener = new helpButtonListener();

    private defaultButtonListener DefaultButtonListener = new defaultButtonListener();

    private String defaultDirectory;

    private lsMDDefaults lsMDDefaults;

    int removeTab = 2;

    /**
   *  Constructor
   *
   *@param  parentFrame  reference to main JFrame for modal sub dialogs
   *@param  container    container that this panel represents
   */
    public lsMDJPAPanel(JFrame parentFrame, lsMDContainer container) {
        super(parentFrame, container);
        this.container = container;
        buildComponents();
        initValues();
        resetValues();
    }

    /**
   *  Sets the Defaults attribute of the lsMDJPAPanel object
   *
   *@param  lsMDDefaults  The new Defaults value
   */
    public void setDefaults(lsMDDefaults lsMDDefaults) {
        this.lsMDDefaults = lsMDDefaults;
    }

    private void checkSoftwareResources() {
        boolean softwareAvailable = false;
        NamedResourceSet namedSiteResources = null;
        ResourceSet siteResources = null;
        namedSiteResources = ResourceManager.getResourceSet(container.getVsite());
        siteResources = namedSiteResources.getResourceSet();
        ResourceEnumeration siteEnum = siteResources.elements();
        while (siteEnum.hasMoreElements()) {
            Resource siteResource = (Resource) siteEnum.nextElement();
            if (siteResource instanceof SoftwareResource) {
                String siteSoftware = ((SoftwareResource) siteResource).getName();
                if (siteSoftware.startsWith("Amber")) softwareAvailable = true;
            }
        }
    }

    String lastVSite = "";

    private void updateSoftwareResources() {
        Object selectedItem = versionComboBox.getSelectedItem();
        versions.clear();
        if (container.getVsite() != null) {
            ResourceSet siteResources = ResourceManager.getResourceSet(container.getVsite()).getResourceSet();
            String softwareName = "Amber";
            String vsiteName = container.getVsite().getName();
            if (siteResources == null) {
            } else {
                ResourceEnumeration siteEnum = siteResources.elements();
                while (siteEnum.hasMoreElements()) {
                    Resource siteResource = (Resource) siteEnum.nextElement();
                    if (siteResource instanceof SoftwareResource) {
                        String siteSoftware = ((SoftwareResource) siteResource).getName();
                        if (siteSoftware.startsWith(softwareName)) {
                            versions.add(siteResource);
                            if (siteResource instanceof Application) {
                                System.err.println("meta data <\n" + ((Application) siteResource).getMetaData() + "\n>");
                            }
                        }
                    }
                }
            }
            if (versions.isEmpty()) {
            } else {
                if (selectedItem != null && versions.contains(selectedItem)) {
                    versionComboBox.setSelectedItem(selectedItem);
                } else versionComboBox.setSelectedIndex(0);
            }
            lastVSite = vsiteName;
        }
    }

    /**
   *  Apply values from GUI to container
   */
    public void applyValues() {
        int numImportFiles = 1;
        int numExportFiles = 0;
        int w = 0;
        String prmtopPar = "", inpcrdPar = "", restartPar = "", radPar = "", refcPar = "", mdoutPar = "", mdenPar = "", mdvelPar = "", mdcrdPar = "", mdinfoPar = "";
        updateSoftwareResources();
        Resource software = (Resource) versionComboBox.getSelectedItem();
        if (software != null) {
            container.setPreinstalledSoftware(software);
            container.setCommand(null);
        }
        container.test = "my test";
        jnTextField.setText(jnTextField.getText().trim().replace(' ', '_'));
        container.setName(jnTextField.getText());
        container.setScriptType(getScriptType());
        FileImport scriptImport = null;
        FileExport scriptExport = null;
        saveLocalScript("mdin");
        File prmtopFile = new File(prmtopPath.getText());
        File inpcrdFile = new File(inpcrdPath.getText());
        File refcFile = new File(refcPath.getText());
        File restartFile = new File(restartPath.getText());
        File radFile = new File(radPath.getText());
        File mdoutFile = new File(mdoutPath.getText());
        File mdcrdFile = new File(mdcrdPath.getText());
        File mdenFile = new File(mdenPath.getText());
        File mdvelFile = new File(mdvelPath.getText());
        File mdinfoFile = new File(mdinfoPath.getText());
        if (checkFile(inpcrdPath.getText(), inpcrdButton, true)) numImportFiles++;
        if (checkFile(prmtopPath.getText(), prmtopButton, true)) numImportFiles++;
        if (checkFile(refcPath.getText(), refcButton, true)) numImportFiles++;
        if (checkFile(radPath.getText(), radButton, true)) numImportFiles++;
        if (checkFile(mdoutPath.getText(), mdoutButton, false)) numExportFiles++;
        if (checkFile(restartPath.getText(), restartButton, false)) numExportFiles++;
        if (checkFile(mdenPath.getText(), mdoutButton, false)) numExportFiles++;
        if (checkFile(mdvelPath.getText(), mdvelButton, false)) numExportFiles++;
        if (checkFile(mdcrdPath.getText(), mdcrdButton, false)) numExportFiles++;
        if (checkFile(mdinfoPath.getText(), mdinfoButton, false)) numExportFiles++;
        mdinFiles = new FileImport[numImportFiles];
        FileImport mdinFile = new FileImport(sourceComboBox.getSelectedItem(), "mdin", "mdin");
        mdinFiles[w] = mdinFile;
        w++;
        if (checkFile(inpcrdPath.getText(), inpcrdButton, true)) {
            FileImport inpcrdFileImport = new FileImport(sourceComboBox.getSelectedItem(), inpcrdFile.getPath(), inpcrdFile.getName());
            mdinFiles[w] = inpcrdFileImport;
            w++;
            inpcrdPar = " -c " + inpcrdFile.getName();
        }
        if (checkFile(prmtopPath.getText(), prmtopButton, true)) {
            FileImport prmtopFileImport = new FileImport(sourceComboBox.getSelectedItem(), prmtopFile.getPath(), prmtopFile.getName());
            mdinFiles[w] = prmtopFileImport;
            w++;
            prmtopPar = " -p " + prmtopFile.getName();
        }
        if (checkFile(radPath.getText(), radButton, true)) {
            FileImport radFileImport = new FileImport(sourceComboBox.getSelectedItem(), radFile.getPath(), radFile.getName());
            mdinFiles[w] = radFileImport;
            w++;
            radPar = " -rad " + radFile.getName();
        }
        if (checkFile(refcPath.getText(), refcButton, true)) {
            FileImport refcFileImport = new FileImport(sourceComboBox.getSelectedItem(), refcFile.getPath(), refcFile.getName());
            mdinFiles[w] = refcFileImport;
            w++;
            refcPar = " -ref " + refcFile.getName();
        }
        mdoutFiles = new FileExport[numExportFiles];
        w = 0;
        if (checkFile(mdoutPath.getText(), mdoutButton, false)) {
            FileExport mdoutFileExport = new FileExport(container, sourceComboBox.getSelectedItem(), mdoutFile.getName(), mdoutFile.getPath());
            mdoutFiles[w] = mdoutFileExport;
            w++;
            mdoutPar = " -o " + mdoutFile.getName();
        }
        if (checkFile(restartPath.getText(), restartButton, false)) {
            FileExport restartFileExport = new FileExport(container, sourceComboBox.getSelectedItem(), restartFile.getName(), restartFile.getPath());
            mdoutFiles[w] = restartFileExport;
            w++;
            restartPar = " -r " + restartFile.getName();
        }
        if (checkFile(mdcrdPath.getText(), mdcrdButton, false)) {
            FileExport mdcrdFileExport = new FileExport(container, sourceComboBox.getSelectedItem(), mdcrdFile.getName(), mdcrdFile.getPath());
            mdoutFiles[w] = mdcrdFileExport;
            w++;
            mdcrdPar = " -x " + mdcrdFile.getName();
        }
        if (checkFile(mdvelPath.getText(), mdvelButton, false)) {
            FileExport mdvelFileExport = new FileExport(container, sourceComboBox.getSelectedItem(), mdvelFile.getName(), mdvelFile.getPath());
            mdoutFiles[w] = mdvelFileExport;
            w++;
            mdvelPar = " -v " + mdvelFile.getName();
        }
        if (checkFile(mdinfoPath.getText(), mdinfoButton, false)) {
            FileExport mdinfoFileExport = new FileExport(container, sourceComboBox.getSelectedItem(), mdinfoFile.getName(), mdinfoFile.getPath());
            mdoutFiles[w] = mdinfoFileExport;
            w++;
            mdinfoPar = " -inf " + mdinfoFile.getName();
        }
        container.setOutcomeFiles(mdinfoFile.getName(), restartFile.getName());
        if (checkFile(mdenPath.getText(), mdenButton, false)) {
            FileExport mdenFileExport = new FileExport(container, sourceComboBox.getSelectedItem(), mdenFile.getName(), mdenFile.getPath());
            mdoutFiles[w] = mdenFileExport;
            w++;
            mdenPar = " -e " + mdenFile.getName();
        }
        if (sourceComboBox.getSelectedItem().equals(FileStorage.NSPACE_STRING)) {
            scriptImport = new FileImport(sourceComboBox.getSelectedItem(), filejnTextField.getText(), jnTextField.getText() + container.getIdentifier().getValue());
        } else {
            scriptImport = new FileImport(sourceComboBox.getSelectedItem(), filejnTextField.getText(), filejnTextField.getText());
        }
        container.setScriptImport(scriptImport);
        if (scriptImport.isLocal()) {
            container.setScriptContents("$AMBERHOME/exe/sander -O -i mdin " + prmtopPar + inpcrdPar + radPar + restartPar + refcPar + mdoutPar + mdenPar + mdvelPar + mdcrdPar + mdinfoPar + "\necho ooo ls ooo\nls -l\ncat " + mdoutFile.getName());
        }
        container.setModifiedTime(new Date(System.currentTimeMillis()));
        container.setFileImports(mdinFiles);
        container.setFileExports(mdoutFiles);
        checkFiles();
    }

    /**
   *  Fill values from container into gui
   */
    public void resetValues() {
        if (container.getIdentifier() == null) {
            return;
        }
        optionsPanel.resetValues();
        jnTextField.setText(container.getName());
        FileImport scriptImport = container.getScriptImport();
        if (scriptImport == null) {
            return;
        }
        String filename = scriptImport.getSourceName();
        sourceComboBox.setSelectedItem(scriptImport.getStorageItem());
    }

    /**
   *  Method will be called whenever this panel is activated
   *
   *@param  vsiteChanged  true, if vsite has changed since last update or reset
   */
    public void updateValues(boolean vsiteChanged) {
        if (container.getVsite() != null) {
            updateStorageResources();
            if (vsiteChanged) {
            }
        }
        editArea.setFont(lsMDDefaults.getEditorFont());
        editArea.repaint();
    }

    /**
   *  Get script type from gui
   *
   *@return    ScriptType
   */
    private ScriptType getScriptType() {
        ScriptType type = null;
        String s = (String) typeComboBox.getSelectedItem();
        Enumeration e = ScriptType.elements();
        while (e.hasMoreElements()) {
            ScriptType t = (ScriptType) e.nextElement();
            if (s.equals(t.getName())) {
                type = t;
            }
        }
        return type;
    }

    /**
   *  Method will be called for first time initalization
   */
    private void initValues() {
        if (container.getVsite() == null) {
            updateOfflineResources();
        } else {
            updateStorageResources();
        }
        defaultDirectory = container.getScriptDirectory();
    }

    /**
   *  If we are working in offline mode only allow local script sources to be selected
   */
    private void updateOfflineResources() {
        sources.clear();
        sources.add(FileStorage.NSPACE_STRING);
        sourceComboBox.setSelectedIndex(0);
    }

    /**
   *  Get storage resources from resource manager and add them to combo box
   */
    private void updateStorageResources() {
        Object selectedItem = sourceComboBox.getSelectedItem();
        sources.clear();
        sources.add(FileStorage.NSPACE_STRING);
        if (sources.contains(selectedItem)) {
            sourceComboBox.setSelectedItem(selectedItem);
        } else {
            sourceComboBox.setSelectedIndex(0);
        }
    }

    /**
   *  Build GUI
   */
    private void buildComponents() {
        int w;
        UserDefaults UserDefaultsObject = new UserDefaults();
        parameters = new MDParameter[300];
        label = new JLabel[300];
        codeName = new String[100];
        panels = new MDPanel[40];
        panel = new JPanel[40];
        spanel = new JScrollPane[40];
        codeButton = new JButton[40];
        versions = new Vector();
        versionComboBoxModel = new DefaultComboBoxModel(versions);
        versionComboBox = new JComboBox(versionComboBoxModel);
        for (w = 0; w < 40; w++) {
            panel[w] = new JPanel();
            spanel[w] = new JScrollPane(panel[w], JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
        loadCodes();
        loadLanguage();
        loadParameters();
        buildPanels();
        jnTextField = new JTextField(30);
        jnTextField.setToolTipText(strEnterJN);
        jnTextField.setForeground(new java.awt.Color(150, 10, 70));
        typeComboBox = new JComboBox();
        Enumeration e = ScriptType.elements();
        while (e.hasMoreElements()) {
            ScriptType t = (ScriptType) e.nextElement();
            typeComboBox.addItem(t.getName());
        }
        typeComboBox.setToolTipText(strSimulationTypeTT);
        typeComboBox.setSelectedItem(ScriptType.SH.getName());
        sources = new Vector();
        sourceComboBoxModel = new DefaultComboBoxModel(sources);
        sourceComboBox = new JComboBox(sourceComboBoxModel);
        sourceComboBox.addItemListener(new ComboBoxListener());
        filejnTextField = new JTextField(30);
        filejnTextField.setToolTipText("Script file name within your " + "local or remote directory");
        filejnTextField.setEditable(true);
        stComboBox = new JComboBox();
        stComboBox.addItem(strChooseOne);
        for (w = 0; w < numCodes; w++) {
            stComboBox.addItem(codeName[w]);
        }
        stComboBox.setToolTipText("Choose the simulation type");
        stComboBox.addItemListener(new ComboBoxListener());
        stComboBox.setSelectedIndex(0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        addToGridBag(new JLabel(strEnterJN + ": "), jnTextField, constraints, gridbag, labelTextPanel);
        addToGridBag(stLabel = new JLabel(strSimulationType + ": "), stComboBox, constraints, gridbag, labelTextPanel);
        addToGridBag(new JLabel(strVersion + ": "), versionComboBox, constraints, gridbag, labelTextPanel);
        saveButton = new JButton(strSave);
        saveButton.setToolTipText(strSaveTT);
        saveButton.addActionListener(buttonListener);
        saveAsButton = new JButton(strSaveAs);
        saveAsButton.setToolTipText(strSaveAsTT);
        saveAsButton.addActionListener(buttonListener);
        generateButton = new JButton(strGenerate);
        generateButton.setToolTipText(strGenerateTT);
        generateButton.setEnabled(false);
        generateButton.addActionListener(buttonListener);
        findButton = new JButton(strFind);
        findButton.setToolTipText(strFindTT);
        findButton.setEnabled(false);
        findButton.addActionListener(buttonListener);
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(generateButton);
        buttonPanel.add(findButton);
        JPanel subPanel = new JPanel();
        subPanel.add(buttonPanel);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        controlPanel.add(labelTextPanel);
        controlPanel.add(subPanel);
        JPanel southPanel = new JPanel();
        southPanel.add(saveButton);
        southPanel.add(saveAsButton);
        editArea = new JTextArea(40, 3);
        editArea.setToolTipText("Edit input file to be executed in Amber");
        editArea.setEditable(true);
        editScrollPane = new JScrollPane(editArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        editScrollPane.setBorder(new TitledBorder("Amber Input"));
        JPanel editPanel = new JPanel(new BorderLayout());
        editPanel.add(editScrollPane, BorderLayout.CENTER);
        editPanel.add(southPanel, BorderLayout.SOUTH);
        optionsPanel = new OptionPanel(container);
        tabbedPane = new JTabbedPane();
        advancedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 7));
        advancedPanel.setBorder(new TitledBorder(strAdvancedBorder));
        advancedPanel.setToolTipText(strAdvancedBorder);
        for (w = 0; w < numPanels; w++) {
            advancedPanel.add(codeButton[w]);
        }
        filesPanel = new JPanel(new BorderLayout());
        filesPanel.setToolTipText("Choose your files");
        JPanel filesPanel2 = new JPanel(new BorderLayout());
        JPanel inputFilesPanel = new JPanel();
        JPanel outputFilesPanel = new JPanel();
        inputFilesPanel.setLayout(new BoxLayout(inputFilesPanel, BoxLayout.Y_AXIS));
        outputFilesPanel.setLayout(new BoxLayout(outputFilesPanel, BoxLayout.Y_AXIS));
        inputFilesPanel.setBorder(new TitledBorder("Choose input files"));
        outputFilesPanel.setBorder(new TitledBorder("Choose output files"));
        java.awt.Dimension buttonSize = new java.awt.Dimension(180, 25);
        java.awt.Dimension textSize = new java.awt.Dimension(300, 25);
        prmtopButton = new JButton("Choose prmtop file");
        prmtopPath = new JTextField(20);
        prmtopButton.setToolTipText("input molecular topology, force field, periodic box type, atom and residue names");
        prmtopButton.setPreferredSize(buttonSize);
        prmtopButton.addActionListener(buttonListener);
        JPanel prmtopPanel = new JPanel(new FlowLayout());
        prmtopPath.setPreferredSize(textSize);
        prmtopPanel.add(prmtopPath);
        prmtopPanel.add(prmtopButton);
        inpcrdButton = new JButton("Choose inpcrd file");
        inpcrdPath = new JTextField(20);
        inpcrdButton.setToolTipText("input initial coordinates and (optionally) velocities and periodic box size");
        inpcrdButton.setPreferredSize(buttonSize);
        inpcrdButton.addActionListener(buttonListener);
        JPanel inpcrdPanel = new JPanel(new FlowLayout());
        inpcrdPath.setPreferredSize(textSize);
        inpcrdPanel.add(inpcrdPath);
        inpcrdPanel.add(inpcrdButton);
        radButton = new JButton("Choose rad file");
        radPath = new JTextField(20);
        radButton.setToolTipText("input dielectric radii for GB calculations");
        radButton.setPreferredSize(buttonSize);
        radButton.addActionListener(buttonListener);
        JPanel radPanel = new JPanel(new FlowLayout());
        radPath.setPreferredSize(textSize);
        radPanel.add(radPath);
        radPanel.add(radButton);
        refcButton = new JButton("Choose refc file");
        refcPath = new JTextField(20);
        refcButton.setToolTipText(" input (optional) reference coords for position constraint");
        refcButton.setPreferredSize(buttonSize);
        refcButton.addActionListener(buttonListener);
        JPanel refcPanel = new JPanel(new FlowLayout());
        refcPath.setPreferredSize(textSize);
        refcPanel.add(refcPath);
        refcPanel.add(refcButton);
        restartButton = new JButton("Choose restart file");
        restartPath = new JTextField(20);
        restartButton.setToolTipText("output final coordinates, velocity, and box dimensions if any - for restarting run");
        restartButton.setPreferredSize(buttonSize);
        restartButton.addActionListener(buttonListener);
        JPanel restartPanel = new JPanel(new FlowLayout());
        restartPath.setPreferredSize(textSize);
        restartPanel.add(restartPath);
        restartPanel.add(restartButton);
        mdoutButton = new JButton("Choose output file");
        mdoutPath = new JTextField(20);
        mdoutButton.setToolTipText("output user readable state info and diagnostics");
        mdoutButton.setPreferredSize(buttonSize);
        mdoutButton.addActionListener(buttonListener);
        JPanel mdoutPanel = new JPanel(new FlowLayout());
        mdoutPath.setPreferredSize(textSize);
        mdoutPanel.add(mdoutPath);
        mdoutPanel.add(mdoutButton);
        mdcrdButton = new JButton("Choose mdcrd file");
        mdcrdPath = new JTextField(20);
        mdcrdButton.setToolTipText("output coordinate sets saved over trajectory");
        mdcrdButton.setPreferredSize(buttonSize);
        mdcrdButton.addActionListener(buttonListener);
        JPanel mdcrdPanel = new JPanel(new FlowLayout());
        mdcrdPath.setPreferredSize(textSize);
        mdcrdPanel.add(mdcrdPath);
        mdcrdPanel.add(mdcrdButton);
        mdvelButton = new JButton("Choose mdvel file");
        mdvelPath = new JTextField(20);
        mdvelButton.setToolTipText("output velocity sets saved over trajectory");
        mdvelButton.setPreferredSize(buttonSize);
        mdvelButton.addActionListener(buttonListener);
        JPanel mdvelPanel = new JPanel(new FlowLayout());
        mdvelPath.setPreferredSize(textSize);
        mdvelPanel.add(mdvelPath);
        mdvelPanel.add(mdvelButton);
        mdenButton = new JButton("Choose mden file");
        mdenPath = new JTextField(20);
        mdenButton.setToolTipText("output extensive energy data over trajectory");
        mdenButton.setPreferredSize(buttonSize);
        mdenButton.addActionListener(buttonListener);
        JPanel mdenPanel = new JPanel(new FlowLayout());
        mdenPath.setPreferredSize(textSize);
        mdenPanel.add(mdenPath);
        mdenPanel.add(mdenButton);
        mdinfoButton = new JButton("Choose mdinfo file");
        mdinfoPath = new JTextField(20);
        mdinfoButton.setToolTipText("mdinfo - output latest mdout-format energy info");
        mdinfoButton.setPreferredSize(buttonSize);
        mdinfoButton.addActionListener(buttonListener);
        JPanel mdinfoPanel = new JPanel(new FlowLayout());
        mdinfoPath.setPreferredSize(textSize);
        mdinfoPanel.add(mdinfoPath);
        mdinfoPanel.add(mdinfoButton);
        File DefaultImportDirectory = new File(UserDefaultsObject.getImportDirectory());
        mdoutPath.setText(DefaultImportDirectory.getPath() + File.separator + "mdout");
        mdinfoPath.setText(DefaultImportDirectory.getPath() + File.separator + "mdinfo");
        restartPath.setText(DefaultImportDirectory.getPath() + File.separator + "restrt");
        mdcrdPath.setText(DefaultImportDirectory.getPath() + File.separator + "mdcrd");
        mdenPath.setText(DefaultImportDirectory.getPath() + File.separator + "mden");
        mdvelPath.setText(DefaultImportDirectory.getPath() + File.separator + "mdvel");
        inputFilesPanel.add(prmtopPanel);
        inputFilesPanel.add(inpcrdPanel);
        inputFilesPanel.add(radPanel);
        inputFilesPanel.add(refcPanel);
        outputFilesPanel.add(mdoutPanel);
        outputFilesPanel.add(restartPanel);
        outputFilesPanel.add(mdcrdPanel);
        outputFilesPanel.add(mdenPanel);
        outputFilesPanel.add(mdvelPanel);
        outputFilesPanel.add(mdinfoPanel);
        filesPanel2.add(outputFilesPanel, BorderLayout.NORTH);
        filesPanel.add(inputFilesPanel, BorderLayout.NORTH);
        filesPanel.add(filesPanel2, BorderLayout.CENTER);
        JScrollPane filesScrollPane = new JScrollPane(filesPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tabbedPane.add(editPanel, strEditor);
        tabbedPane.add(advancedPanel, strAdvanced);
        tabbedPane.add(filesScrollPane, strFiles);
        tabbedPane.setBackgroundAt(0, new java.awt.Color(255, 255, 255));
        tabbedPane.setForegroundAt(0, new java.awt.Color(80, 0, 0));
        tabbedPane.setBackgroundAt(1, new java.awt.Color(255, 255, 255));
        tabbedPane.setForegroundAt(1, new java.awt.Color(80, 0, 0));
        tabbedPane.setBackgroundAt(2, new java.awt.Color(255, 255, 255));
        tabbedPane.setForegroundAt(2, new java.awt.Color(80, 0, 0));
        add(controlPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        setBorder(new TitledBorder("lsMD Plugin"));
        generateBlink = new blink(generateButton, new java.awt.Color(230, 230, 230), 500);
        tabbedPane.setEnabled(false);
        editScrollPane.setEnabled(false);
        saveButton.setEnabled(false);
        saveAsButton.setEnabled(false);
        revalidate();
    }

    /**
	/  Helper methods
	*/
    private void buildPanels() {
        JPanel[] tmpPanel;
        JPanel emptyPanel;
        tmpPanel = new JPanel[numPanels];
        emptyPanel = new JPanel(new FlowLayout());
        int w, q;
        paramPanel = new JPanel[numParameters];
        defaultButton = new JButton[numParameters];
        helpButton = new JButton[numParameters];
        labelOption = new JLabel[numParameters];
        labelText = new JLabel[numParameters];
        param = new JTextField[numParameters];
        for (w = 0; w < numPanels; w++) {
            tmpPanel[w] = new JPanel();
            tmpPanel[w].setLayout(new BoxLayout(tmpPanel[w], BoxLayout.Y_AXIS));
            panel[w].setLayout(new BorderLayout());
            panel[w].setBorder(new TitledBorder(panels[w].name));
            panel[w].setAutoscrolls(true);
            for (q = panels[w].startParameter; q < panels[w].startParameter + panels[w].numParameters; q++) {
                paramPanel[q] = new JPanel(new FlowLayout());
                labelOption[q] = new JLabel();
                labelOption[q].setForeground(java.awt.Color.blue);
                labelOption[q].setPreferredSize(new java.awt.Dimension(60, 17));
                labelOption[q].setMinimumSize(new java.awt.Dimension(50, 17));
                labelOption[q].setMaximumSize(new java.awt.Dimension(80, 17));
                labelOption[q].setText(parameters[q].shortName);
                paramPanel[q].add(labelOption[q]);
                labelText[q] = new JLabel();
                labelText[q].setPreferredSize(new java.awt.Dimension(120, 17));
                labelText[q].setMinimumSize(new java.awt.Dimension(80, 17));
                labelText[q].setMaximumSize(new java.awt.Dimension(250, 17));
                labelText[q].setText(parameters[q].name);
                paramPanel[q].add(labelText[q]);
                param[q] = new JTextField();
                param[q].setColumns(10);
                param[q].setText("");
                paramPanel[q].add(param[q]);
                param[q].getDocument().addDocumentListener(paramListener);
                defaultButton[q] = new JButton("Default");
                paramPanel[q].add(defaultButton[q]);
                defaultButton[q].addActionListener(DefaultButtonListener);
                helpButton[q] = new JButton("?");
                paramPanel[q].add(helpButton[q]);
                helpButton[q].addActionListener(HelpButtonListener);
                tmpPanel[w].add(paramPanel[q]);
            }
            panel[w].add(tmpPanel[w], BorderLayout.NORTH);
            panel[w].add(emptyPanel, BorderLayout.CENTER);
        }
    }

    private void setForm(String type) {
        int w;
        String txt;
        loadCodeFromGallery(type);
        generateButton.setEnabled(true);
        findButton.setEnabled(true);
        stComboBox.setEnabled(false);
        readInput(codeContent);
        revalidate();
    }

    private String ChooseFile(JComponent component, String text, String current) {
        JFileChooser fileChooser = new JFileChooser(current);
        UserDefaults domyslne = new UserDefaults();
        File katalog = new File(domyslne.getImportDirectory());
        if (current.equals("")) fileChooser.setCurrentDirectory(katalog);
        int returnVal = fileChooser.showDialog(component, text);
        File file = fileChooser.getSelectedFile();
        return (file.getPath());
    }

    /**
	 *  Read Code Names from Code Galery
	 */
    private void loadCodes() {
        int w;
        try {
            ClassLoader loader = getClass().getClassLoader();
            InputStream plik = loader.getResourceAsStream("icm/unicore/plugins/amber/resources/codegallery.md");
            BufferedReader in = new BufferedReader(new InputStreamReader(plik));
            String txt;
            numCodes = 0;
            txt = in.readLine();
            if (!txt.equals("Do NOT edit this file.")) {
                System.err.println("Corrupted codegalery filename.");
                in.close();
                return;
            }
            txt = in.readLine();
            txt = in.readLine();
            numCodes = Integer.parseInt(txt);
            for (w = 0; w < numCodes; w++) {
                codeName[w] = in.readLine();
            }
            txt = in.readLine();
            if (!txt.equals("###")) {
                System.err.println("Corrupted codegalery filename.");
                in.close();
                return;
            }
            in.close();
        } catch (java.io.IOException e) {
            System.err.println("Can't open or read codegalery file");
        }
        ;
    }

    /**
	 *  Reads first code from reader
	 */
    private String loadCode(BufferedReader in) throws IOException {
        String txt;
        int w, numLines;
        String name;
        codeContent = "";
        name = in.readLine();
        txt = in.readLine();
        numLines = Integer.parseInt(txt);
        for (w = 0; w < numLines; w++) {
            txt = in.readLine();
            codeContent += txt + "\n";
        }
        txt = in.readLine();
        if (!txt.equals("###")) System.err.println("Corrupted codegalery file.");
        return (name);
    }

    /**
	 *  Read Code from Code Galery
	 */
    private void loadCodeFromGallery(String TxtName) {
        int w;
        String myCodeName;
        try {
            ClassLoader loader = getClass().getClassLoader();
            InputStream plik = loader.getResourceAsStream("icm/unicore/plugins/amber/resources/codegallery.md");
            BufferedReader in = new BufferedReader(new InputStreamReader(plik));
            String txt;
            numCodes = 0;
            txt = in.readLine();
            if (!txt.equals("Do NOT edit this file.")) {
                System.err.println("Corrupted codegalery file.");
                in.close();
                return;
            }
            txt = in.readLine();
            txt = in.readLine();
            numCodes = Integer.parseInt(txt);
            for (w = 0; w < numCodes; w++) {
                codeName[w] = in.readLine();
            }
            txt = in.readLine();
            if (!txt.equals("###")) {
                System.err.println("Corrupted codegalery file.");
                in.close();
                return;
            }
            for (w = 0; w < numCodes; w++) {
                myCodeName = loadCode(in);
                if (myCodeName.equals(TxtName)) {
                    in.close();
                    System.err.println("lsMD: Loaded " + myCodeName);
                    return;
                }
            }
            in.close();
        } catch (java.io.IOException e) {
            System.err.println("Can't open or read codegalery file");
        }
        ;
    }

    private void loadParametersToPanel(int panelno, BufferedReader in) throws IOException {
        String txt;
        int w, numPar;
        String name;
        txt = in.readLine();
        numPar = Integer.parseInt(txt);
        panels[panelno].numParameters = numPar;
        txt = in.readLine();
        panels[panelno].type = Integer.parseInt(txt);
        panels[panelno].startParameter = numParameters;
        panels[panelno].blockName = in.readLine();
        panels[panelno].active = 0;
        txt = in.readLine();
        for (w = 0; w < numPar; w++) {
            parameters[numParameters] = new MDParameter(panelno);
            parameters[numParameters].shortName = in.readLine();
            txt = in.readLine();
            parameters[numParameters].type = Integer.parseInt(txt);
            parameters[numParameters].name = in.readLine();
            parameters[numParameters].unit = in.readLine();
            parameters[numParameters].description = in.readLine();
            parameters[numParameters].defaultValue = in.readLine();
            txt = in.readLine();
            numParameters++;
        }
        txt = in.readLine();
        if (!txt.equals("###")) System.err.println("Corrupted codegalery file.");
    }

    /**
	 *  Read Parameters
	 */
    private void loadParameters() {
        int w;
        try {
            ClassLoader loader = getClass().getClassLoader();
            InputStream plik = loader.getResourceAsStream("icm/unicore/plugins/amber/resources/param.md");
            BufferedReader in = new BufferedReader(new InputStreamReader(plik));
            String txt;
            numParameters = 0;
            txt = in.readLine();
            if (!txt.equals("Do NOT edit this file.")) {
                System.err.println("Corrupted parameters file.");
                in.close();
                return;
            }
            txt = in.readLine();
            txt = in.readLine();
            numPanels = Integer.parseInt(txt);
            for (w = 0; w < numPanels; w++) {
                panels[w] = new MDPanel();
            }
            for (w = 0; w < numPanels; w++) {
                panels[w].name = in.readLine();
                codeButton[w] = new JButton(panels[w].name);
                codeButton[w].addActionListener(buttonListener);
            }
            txt = in.readLine();
            if (!txt.equals("###")) {
                System.err.println("Corrupted parameters file.");
                in.close();
                return;
            }
            for (w = 0; w < numPanels; w++) {
                loadParametersToPanel(w, in);
            }
            in.close();
            System.err.println("lsMD: Loaded " + numParameters + " parameters");
        } catch (java.io.IOException e) {
            System.err.println("Can't open or read parameters file");
        }
        ;
        sortedParam = new int[numParameters];
        int pom2;
        int q;
        for (w = 0; w < numParameters; w++) {
            sortedParam[w] = w;
        }
        for (w = 0; w < numParameters; w++) for (q = w; q < numParameters; q++) {
            if (parameters[sortedParam[w]].shortName.compareTo(parameters[sortedParam[q]].shortName) > 0) {
                pom2 = sortedParam[w];
                sortedParam[w] = sortedParam[q];
                sortedParam[q] = pom2;
            }
        }
        allParams = "<html><body bgcolor='white'><center><b><font size=+1 color='red'>All parameters:</font></b><br><table border=1>";
        allParams += "<tr><th align=center>Parameter</th><th align=center>Description</th><th align=center>Tab</th></tr>";
        for (w = 0; w < numParameters; w++) {
            allParams += "<tr><td><a href='" + parameters[sortedParam[w]].shortName + "'><b><font color='blue'>" + parameters[sortedParam[w]].shortName + "</font></b></a></td><td>" + parameters[sortedParam[w]].name + "</td><td><font size='-1'>" + panels[parameters[sortedParam[w]].pane].name + "</font></td></tr>";
        }
        allParams += "</table></center></body></html>";
    }

    /**
   *  Load Language File
   */
    private void loadLanguage() {
        strEnterJN = "Enter Job Name";
        strSimulationType = "Simulation Type";
        strSimulationTypeTT = "Choose the script type here";
        strEditor = "Editor";
        strFiles = "Files";
        strSave = "Save";
        strSaveAs = "Save As";
        strSaveTT = "Use this button to save a script file under the previous name to your local file system";
        strSaveAsTT = "Use this button to save a script file under a new name to your local file system";
        strGenerate = "Generate Amber Input";
        strGenerateTT = "Use this button to generate input for Amber";
        strFind = "Find parameter";
        strFindTT = "";
        strOptions = "Options";
        strChooseOne = "please choose one";
        strAdvanced = "Advanced Code";
        strAdvancedBorder = "Advanced Code Generation";
        strHideTab = "Hide unused tabs?";
        strYes = "Yes";
        strNo = "No";
        strAlways = "Always";
        strNever = "Never";
        strVersion = "Amber version";
        strCheckParameters = "Code generated, but you should check the parameters.";
        strCheckFiles = "Check the files";
        strNotFound = "<html><body bgcolor='white'><p><h2 color='red' align='center'>Parameter not found.</h2><p align='center'><a href='allparams'>All parameters</a></p></body></html>";
    }

    /**
   *  Helper method to handle GridBagLayout
   *
   *@param  label        The feature to be added to the ToGridBag attribute
   *@param  component    The feature to be added to the ToGridBag attribute
   *@param  constraints  The feature to be added to the ToGridBag attribute
   *@param  gridbag      The feature to be added to the ToGridBag attribute
   *@param  panel        The feature to be added to the ToGridBag attribute
   */
    private void addToGridBag(JLabel label, JComponent component, GridBagConstraints constraints, GridBagLayout gridbag, JPanel panel) {
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(label, constraints);
        panel.add(label);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(component, constraints);
        panel.add(component);
    }

    private void addToGridBag2(JComponent button, JComponent component, GridBagConstraints constraints, GridBagLayout gridbag, JPanel panel) {
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(button, constraints);
        panel.add(button);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(component, constraints);
        panel.add(component);
    }

    private void addToGridBag3(JLabel label, JTextField component, GridBagConstraints constraints, GridBagLayout gridbag, JPanel panel) {
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(label, constraints);
        panel.add(label);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(component, constraints);
        panel.add(component);
    }

    /**
   *  Show file chooser to select local filename and save script
   *
   *@param  filename  Description of Parameter
   */
    private void saveLocalScript(String filename) {
        try {
            if (filename == null) {
                JFileChooser d = new JFileChooser(container.getScriptDirectory());
                if (container.getFilename() != null) {
                    d.setSelectedFile(new File(container.getFilename()));
                }
                d.setDialogTitle("UNICORE: Save local script");
                int result = d.showSaveDialog(parentFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    filename = d.getSelectedFile().getCanonicalPath();
                }
            }
            if (filename == null) {
                return;
            }
            File file = new File(filename);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(editArea.getText());
            bw.close();
            filejnTextField.setText(filename);
            container.setFilename(filename);
            container.setScriptDirectory(file.getParent());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void showBrowser() {
    }

    private void generateInput(int firstGenerate) {
        int w, q;
        q = 0;
        String wyjscie = new String(codeContent);
        wyjscie = "generated by lsMD\n";
        wyjscie += " &cntrl\n";
        for (w = 0; w < numParameters; w++) {
            if (parameters[w].type == 1 && !param[w].getText().equals("")) {
                wyjscie += " " + parameters[w].shortName.toLowerCase() + "=" + param[w].getText() + ",";
                q++;
                if (q == 3) {
                    q = 0;
                    wyjscie += "\n";
                }
            }
        }
        wyjscie += " /\n &end\n";
        editArea.setText(wyjscie.toString());
        editArea.setEnabled(true);
        tabbedPane.setSelectedIndex(0);
        if (firstGenerate == 0) {
            if (removeTab == 3) removeTabs(); else if (removeTab == 2 && checkTabsToRemove()) askRemoveTabs();
            checkParams();
        }
        checkFiles();
    }

    /**
   * check params dependences
   */
    private boolean checkParams() {
        boolean ok = true;
        int w;
        int iN3B = 0;
        for (w = 0; w < numPanels; w++) panels[w].warning = 0;
        for (w = 0; w < numParameters; w++) if (parameters[w].warning > 0) parameters[w].setWarning(w, 0);
        String cNTC = param[findParameter("ntc")].getText();
        String cNTF = param[findParameter("ntf")].getText();
        String cN3B = param[findParameter("n3b")].getText();
        String cNTCM = param[findParameter("ntcm")].getText();
        String cNSCM = param[findParameter("nscm")].getText();
        String cNDFMIN = param[findParameter("ndfmin")].getText();
        String cNTP = param[findParameter("ntp")].getText();
        String cNTB = param[findParameter("ntb")].getText();
        String cNTX = param[findParameter("ntx")].getText();
        String cIREST = param[findParameter("irest")].getText();
        if (cNTCM.equals("")) cNTCM = "0";
        if (cNSCM.equals("")) cNSCM = "0";
        if (cNDFMIN.equals("")) cNDFMIN = "0";
        if (!cN3B.equals("")) {
            try {
                iN3B = Integer.parseInt(cN3B);
            } catch (java.lang.NumberFormatException e) {
                iN3B = 6;
            }
        }
        if ((cNTF.equals("2") && !cNTC.equals("2")) || (cNTF.equals("3") && !cNTC.equals("3")) || (cNTC.equals("2") && !cNTF.equals("2")) || (cNTC.equals("3") && !cNTF.equals("3"))) {
            setWarning("ntc", 2);
            setWarning("ntf", 2);
            ok = false;
        }
        if ((cNTB.equals("2") && !cNTP.equals("2") && !cNTP.equals("1"))) {
            setWarning("ntp", 2);
            ok = false;
        }
        if (iN3B > 5) {
            setWarning("n3b", 1);
            ok = false;
        }
        if (cNTCM.equals("0") || cNSCM.equals("0")) {
            if (!cNDFMIN.equals("0")) setWarning("ndfmin", 1);
            ok = false;
        } else {
            if (!cNDFMIN.equals("6")) setWarning("ndfmin", 1);
            ok = false;
        }
        if ((cIREST.equals("1") && !cNTX.equals("4") && !cNTX.equals("5") && !cNTX.equals("6") && !cNTX.equals("7"))) {
            setWarning("ntx", 2);
            ok = false;
        }
        return (ok);
    }

    private void setWarning(String parameter, int level) {
        int paramNumber = findParameter(parameter);
        parameters[paramNumber].setWarning(paramNumber, level);
    }

    /**
   * check files
   */
    private boolean checkFiles() {
        boolean ok = true;
        int pom;
        String pom2;
        restartButton.setBackground(null);
        radButton.setBackground(null);
        mdvelButton.setBackground(null);
        mdenButton.setBackground(null);
        mdinfoButton.setBackground(null);
        mdcrdButton.setBackground(null);
        refcButton.setBackground(null);
        if (!checkFile(prmtopPath.getText(), prmtopButton, true)) {
            ok = false;
        }
        if (!checkFile(inpcrdPath.getText(), inpcrdButton, true)) {
            ok = false;
        }
        if (param[findParameter("readrad")].getText().equals("1")) if (!checkFile(radPath.getText(), radButton, true)) {
            ok = false;
        }
        if (param[findParameter("ntr")].getText().equals("1")) if (!checkFile(refcPath.getText(), refcButton, true)) {
            ok = false;
        }
        if (!checkFile(mdoutPath.getText(), mdoutButton, false)) {
            ok = false;
        }
        if (!checkFile(restartPath.getText(), restartButton, false)) {
            ok = false;
        }
        pom = findParameter("ntwe");
        pom2 = param[pom].getText();
        if (pom2.equals("")) pom2 = parameters[pom].defaultValue;
        if (!pom2.equals("0")) if (!checkFile(mdenPath.getText(), mdenButton, false)) {
            ok = false;
        }
        pom = findParameter("ntwv");
        pom2 = param[pom].getText();
        if (pom2.equals("")) pom2 = parameters[pom].defaultValue;
        if (!pom2.equals("0")) if (!checkFile(mdvelPath.getText(), mdvelButton, false)) {
            ok = false;
        }
        pom = findParameter("ntwx");
        pom2 = param[pom].getText();
        if (pom2.equals("")) pom2 = parameters[pom].defaultValue;
        if (!pom2.equals("0")) if (!checkFile(mdcrdPath.getText(), mdcrdButton, false)) {
            ok = false;
        }
        if (!ok) {
            tabbedPane.setBackgroundAt(2, new java.awt.Color(150, 0, 0));
            tabbedPane.setForegroundAt(2, new java.awt.Color(255, 255, 255));
            return (false);
        } else {
            tabbedPane.setBackgroundAt(2, new java.awt.Color(255, 255, 255));
            tabbedPane.setForegroundAt(2, new java.awt.Color(80, 0, 0));
        }
        return (true);
    }

    private boolean checkFile(String name, Component comp, boolean input) {
        comp.setBackground(new java.awt.Color(255, 100, 100));
        if (name.trim().equals("")) return (false);
        File testFile = new File(name);
        if (input) {
            if (!testFile.isFile()) return (false);
            if (!testFile.canRead()) return (false);
            if (testFile.isDirectory()) return (false);
        } else {
            if (testFile.isDirectory()) return (false);
            if (testFile.isFile() && !testFile.canWrite()) return (false);
        }
        comp.setBackground(null);
        return (true);
    }

    private int findParameter(String name) {
        int w;
        for (w = 0; w < numParameters; w++) {
            if (name.toLowerCase().equals(parameters[w].shortName.toLowerCase())) return (w);
        }
        return (-1);
    }

    /**
   * check tabs to remove - return true if any tab is unused
   */
    private boolean checkTabsToRemove() {
        int w, q;
        boolean used;
        for (q = 0; q < numPanels; q++) {
            used = false;
            for (w = panels[q].startParameter; w < panels[q].startParameter + panels[q].numParameters; w++) {
                if (!param[w].getText().equals("")) used = true;
            }
            if (used == false && panels[q].active == 1) return (true);
        }
        return (false);
    }

    private void removeTabs() {
        int w;
        for (w = 0; w < numPanels; w++) {
            panels[w].active = 0;
        }
        for (w = 0; w < numParameters; w++) {
            if (!param[w].getText().equals("")) panels[parameters[w].pane].active = 1;
        }
        for (w = 0; w < numPanels; w++) {
            if (panels[w].active == 0) panels[w].deactivate(w);
        }
    }

    private void askRemoveTabs() {
        Object[] options = { strYes, strNo, strAlways, strNever };
        int n = JOptionPane.showOptionDialog(tabbedPane, strHideTab, "lsMDPlugin", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if (n == 0) removeTabs();
        if (n == 2) {
            removeTab = 3;
            removeTabs();
        }
        if (n == 3) {
            removeTab = 4;
        }
    }

    private void readInput(String input) {
        int pos, q, w, start, end;
        char c;
        String tmp;
        pos = 0;
        try {
            input = input.toLowerCase();
        } catch (NullPointerException e) {
        }
        for (w = 0; w < numParameters; w++) {
            try {
                pos = input.indexOf(" " + parameters[w].shortName.toLowerCase() + "=");
            } catch (NullPointerException e) {
            }
            if (pos > 0) {
                tmp = "";
                panels[parameters[w].pane].active = 1;
                start = pos + parameters[w].shortName.length() + 2;
                c = input.charAt(start);
                for (q = start; ((c <= '9' && c >= '1') || c == '.' || c == '0') && w < input.length(); q++) {
                    c = input.charAt(q);
                    if ((c <= '9' && c >= '1') || c == '.' || c == '0') tmp += c;
                }
                param[w].setText(tmp);
            }
        }
        for (w = 0; w < numPanels; w++) {
            if (panels[w].active == 1) {
                panels[w].activate(w);
            }
        }
        generateInput(1);
        generateBlink.stop();
    }

    JFrame window;

    JButton showButton;

    JTextField findTextField;

    JEditorPane htmlStatus;

    int aktParam;

    boolean findWindowOpened = false;

    private void findParam() {
        showButton.setEnabled(false);
        aktParam = findParameter(findTextField.getText());
        if (aktParam == -1) {
            htmlStatus.setText(strNotFound);
            htmlStatus.setCaretPosition(1);
        } else {
            htmlStatus.setText(parameters[aktParam].htmlString(aktParam));
            htmlStatus.setCaretPosition(1);
            showButton.setEnabled(true);
        }
    }

    private void openFindWindow() {
        MDParameter par;
        aktParam = -1;
        int w;
        JButton closeButton = new JButton("Close");
        JButton findButton = new JButton("Find");
        showButton = new JButton("Go to tab");
        findTextField = new JTextField(20);
        JPanel findPanel = new JPanel(new FlowLayout());
        JPanel showPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel statusPanel = new JPanel(new GridLayout(1, 1, 1, 1));
        JPanel tmpPanel = new JPanel(new BorderLayout());
        htmlStatus = new JEditorPane();
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.getViewport().add(htmlStatus, null);
        htmlStatus.setEditable(false);
        htmlStatus.setContentType("text/html; charset=ISO-8859-1");
        statusPanel.setPreferredSize(new java.awt.Dimension(400, 220));
        htmlStatus.setText(allParams);
        htmlStatus.setCaretPosition(1);
        showButton.setEnabled(false);
        if (findWindowOpened) window.dispose();
        findWindowOpened = true;
        window = new JFrame("Find parameter");
        window.addWindowListener(new WindowAdapter() {

            public void windowdowClosing(WindowEvent e) {
            }

            public void windowdowDeactivated(WindowEvent e) {
                findWindowOpened = false;
                window.dispose();
            }
        });
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                window.dispose();
            }
        });
        showButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (aktParam != -1) {
                    if (panels[parameters[aktParam].pane].active == 0) {
                        tabbedPane.add(spanel[parameters[aktParam].pane], panels[parameters[aktParam].pane].name);
                        panels[parameters[aktParam].pane].active = 1;
                    }
                    window.dispose();
                    tabbedPane.setSelectedComponent(spanel[parameters[aktParam].pane]);
                    param[aktParam].grabFocus();
                }
            }
        });
        findButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                findParam();
            }
        });
        findTextField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                findParam();
            }
        });
        findPanel.add(findTextField);
        findPanel.add(findButton);
        showPanel.add(showButton);
        showPanel.add(closeButton);
        statusPanel.add(scrollPane1);
        tmpPanel.add(showPanel, java.awt.BorderLayout.SOUTH);
        tmpPanel.add(statusPanel, java.awt.BorderLayout.CENTER);
        window.getContentPane().add(findPanel, java.awt.BorderLayout.NORTH);
        window.getContentPane().add(tmpPanel, java.awt.BorderLayout.CENTER);
        class link implements HyperlinkListener {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    if (e.getDescription().equals("allparams")) {
                        htmlStatus.setText(allParams);
                        htmlStatus.setCaretPosition(1);
                        showButton.setEnabled(false);
                    } else {
                        findTextField.setText(e.getDescription());
                        findParam();
                    }
                }
            }
        }
        htmlStatus.addHyperlinkListener(new link());
        window.pack();
        window.setLocation(tabbedPane.getLocationOnScreen());
        window.setVisible(true);
    }

    /**
   *  Listener class listens to Button events
   */
    private class ButtonListener implements ActionListener {

        private Object lock = new Object();

        private String tmp;

        private int w;

        public void actionPerformed(ActionEvent event) {
            synchronized (lock) {
                Object source = event.getSource();
                if (source == saveButton) {
                    saveLocalScript(container.getFilename());
                } else if (source == saveAsButton) {
                    saveLocalScript(null);
                } else if (source == resourceButton) {
                } else if (source == browseButton) {
                    showBrowser();
                } else if (source == generateButton) {
                    generateInput(0);
                    generateBlink.stop();
                } else if (source == findButton) {
                    openFindWindow();
                }
                if (source == prmtopButton) {
                    tmp = ChooseFile(filesPanel, "Choose prmtop", prmtopPath.getText());
                    prmtopPath.setText(tmp);
                }
                if (source == inpcrdButton) {
                    tmp = ChooseFile(filesPanel, "Choose inpcrd", inpcrdPath.getText());
                    inpcrdPath.setText(tmp);
                }
                if (source == refcButton) {
                    tmp = ChooseFile(filesPanel, "Choose refc", refcPath.getText());
                    refcPath.setText(tmp);
                }
                if (source == restartButton) {
                    tmp = ChooseFile(filesPanel, "Choose restart", restartPath.getText());
                    restartPath.setText(tmp);
                }
                if (source == radButton) {
                    tmp = ChooseFile(filesPanel, "Choose rad", radPath.getText());
                    radPath.setText(tmp);
                }
                if (source == mdoutButton) {
                    tmp = ChooseFile(filesPanel, "Choose mdout", mdoutPath.getText());
                    mdoutPath.setText(tmp);
                }
                if (source == mdinfoButton) {
                    tmp = ChooseFile(filesPanel, "Choose mdinfo", mdinfoPath.getText());
                    mdinfoPath.setText(tmp);
                }
                if (source == mdenButton) {
                    tmp = ChooseFile(filesPanel, "Choose mden", mdenPath.getText());
                    mdenPath.setText(tmp);
                }
                if (source == mdvelButton) {
                    tmp = ChooseFile(filesPanel, "Choose mdvel", mdvelPath.getText());
                    mdvelPath.setText(tmp);
                }
                if (source == mdcrdButton) {
                    tmp = ChooseFile(filesPanel, "Choose mdcrd", mdcrdPath.getText());
                    mdcrdPath.setText(tmp);
                }
                for (w = 0; w < numPanels; w++) {
                    if (source == codeButton[w]) {
                        panels[w].activate(w);
                    }
                }
            }
        }
    }

    /**
   * open help window
   */
    private class helpWindow {

        private JFrame win;

        private JPanel helpPanel;

        private JButton okButton;

        public helpWindow(MDParameter par) {
            win = new JFrame("Help - " + par.shortName);
            okButton = new JButton("OK");
            win.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                }
            });
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    win.dispose();
                }
            });
            helpPanel = new JPanel(new BorderLayout());
            helpPanel.setPreferredSize(new java.awt.Dimension(400, 220));
            JEditorPane htmlHelp = new JEditorPane();
            JScrollPane scrollPane1 = new JScrollPane();
            scrollPane1.getViewport().add(htmlHelp, null);
            htmlHelp.setEditable(false);
            htmlHelp.setContentType("text/html; charset=ISO-8859-1");
            htmlHelp.setText("<html><body bgcolor='white'><center><a name='top'><font color=red size=+1><b>" + par.shortName + "</b></font></a><br>Default: <font color=blue><b>" + par.defaultValue + "</b></font><br></center><table align=center cellpadding=7><tr><td align='left'>" + par.description + "</tr></td></table></body></html>");
            htmlHelp.setCaretPosition(1);
            helpPanel.add(scrollPane1);
            win.getContentPane().add(helpPanel, BorderLayout.CENTER);
            win.getContentPane().add(okButton, BorderLayout.SOUTH);
            win.pack();
            win.setLocation(tabbedPane.getLocationOnScreen());
            win.setVisible(true);
        }
    }

    private class helpButtonListener implements ActionListener {

        private Object lock = new Object();

        private String tmp;

        private int w;

        private helpWindow win;

        public void actionPerformed(ActionEvent event) {
            synchronized (lock) {
                Object source = event.getSource();
                for (w = 0; w < numParameters; w++) {
                    if (source == helpButton[w]) {
                        win = new helpWindow(parameters[w]);
                    }
                }
            }
        }
    }

    private class defaultButtonListener implements ActionListener {

        private Object lock = new Object();

        private String tmp;

        private int w;

        public void actionPerformed(ActionEvent event) {
            synchronized (lock) {
                Object source = event.getSource();
                for (w = 0; w < numParameters; w++) {
                    if (source == defaultButton[w]) {
                        if (!param[w].getText().equals(parameters[w].defaultValue)) {
                            param[w].setText(parameters[w].defaultValue);
                            generateBlink.start();
                        }
                    }
                }
            }
        }
    }

    private class ParamListener implements DocumentListener {

        private Object lock = new Object();

        public void insertUpdate(DocumentEvent event) {
            synchronized (lock) {
                generateBlink.start();
            }
        }

        public void removeUpdate(DocumentEvent event) {
            synchronized (lock) {
                generateBlink.start();
            }
        }

        public void changedUpdate(DocumentEvent event) {
            synchronized (lock) {
            }
        }
    }

    /**
   *  Listener enables/disables controls according to selected script source
   */
    private class ComboBoxListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            int w;
            for (w = 0; w < numCodes; w++) if (e.getItem().equals(codeName[w])) {
                setForm(codeName[w]);
                tabbedPane.setEnabled(true);
                editScrollPane.setEnabled(true);
                saveButton.setEnabled(true);
                saveAsButton.setEnabled(true);
            }
        }
    }

    /**
   * Class to blink generate button
   */
    private class blink {

        private int active;

        private int on;

        private int delay;

        private JComponent object;

        private Color color;

        private javax.swing.Timer tim;

        public blink(JComponent obj, Color col, int del) {
            object = obj;
            delay = del;
            active = 0;
            color = col;
            on = 0;
            tim = new javax.swing.Timer(delay, taskPerformer);
        }

        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (on == 1) object.setBackground(color); else object.setBackground(null);
                on = 1 - on;
            }
        };

        public void stop() {
            tim.stop();
            object.setBackground(null);
            on = 0;
            active = 0;
        }

        public void start() {
            if (active == 0) {
                tim.start();
                object.setBackground(color);
                active = 1;
            }
        }
    }

    /**
   *  Class of parameters
   */
    private class MDParameter {

        public int type, pane, warning;

        public String defaultValue, description, unit, name, shortName;

        public MDParameter(int p) {
            pane = p;
            warning = 0;
        }

        public void setWarning(int parameter, int level) {
            int pom;
            java.awt.Color paramLevels[] = { new java.awt.Color(255, 255, 255), new java.awt.Color(255, 255, 0), new java.awt.Color(255, 60, 60) };
            java.awt.Color panelLevels[] = { null, new java.awt.Color(255, 255, 0), new java.awt.Color(255, 100, 100) };
            param[parameter].setBackground(paramLevels[level]);
            warning = level;
            if (panels[pane].active == 0) panels[pane].activate(pane);
            if (panels[pane].warning <= level) {
                panels[pane].warning = level;
                pom = tabbedPane.indexOfTab(panels[pane].name);
                if (pom != -1) tabbedPane.setBackgroundAt(pom, panelLevels[level]);
            }
        }

        public String htmlString(int parameter) {
            String htmlText;
            String actValue = "none";
            String isActive = " (active)";
            if (panels[pane].active == 0) isActive = " (not active)";
            if (!param[parameter].getText().equals("")) actValue = "<b>" + param[parameter].getText() + "</b>";
            htmlText = "<html><body bgcolor='white'><center>";
            htmlText += "<b><font size=+1 color='red'>" + shortName + "</font><br>";
            htmlText += "<font color='blue'>" + name + "</b><br></center>";
            htmlText += "<table cellpadding=5 width='100%'><tr><td>Default value: <b>" + defaultValue + "</b><br>";
            htmlText += "Actual value: <b>" + actValue + "</b><p>";
            htmlText += "Tab: <b>" + panels[pane].name + "</b>" + isActive + "<p>";
            htmlText += description;
            htmlText += "<p align='right'><a href='allparams'>All parameters</a></p>";
            htmlText += "</td></tr></table></body></html>";
            return (htmlText);
        }
    }

    /**
   *  Class of panels
   */
    private class MDPanel {

        public int type, active, numParameters, startParameter, warning;

        public String name, blockName;

        public MDPanel() {
            active = 0;
            warning = 0;
        }

        public void activate(int w) {
            codeButton[w].setEnabled(false);
            tabbedPane.add(spanel[w], panels[w].name);
            active = 1;
        }

        public void deactivate(int w) {
            codeButton[w].setEnabled(true);
            tabbedPane.remove(spanel[w]);
            active = 0;
        }
    }
}
