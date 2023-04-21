package org.xaware.ide.xadev.tools.gui.packagetool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xaware.ide.shared.AuthenticationPan;
import org.xaware.ide.shared.UserPrefs;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.bizview.publish.PublishTableData;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.common.ResourceUtils;
import org.xaware.ide.xadev.gui.FileChooserWithLabeling;
import org.xaware.ide.xadev.gui.XAChooser;
import org.xaware.ide.xadev.gui.dialog.ViewTransformerFileDlg;
import org.xaware.ide.xadev.table.contentprovider.PackageTableContentProvider;
import org.xaware.ide.xadev.tools.metadata.BizCompDescriptor;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.FileUtils;
import org.xaware.shared.util.PublishResponse;
import org.xaware.shared.util.PublishUsingSOAP;
import org.xaware.shared.util.XAHttpClient;
import org.xaware.shared.util.XASystemProps;
import org.xaware.shared.util.XAwareConfig;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Creates archive of bizview files and publishes archive file(s) to host server.
 * 
 * @author Saritha
 * @version 1.0
 */
public class PackageTool extends Dialog implements SelectionListener, FocusListener, MouseListener, ModifyListener {

    /** Class name used for logging purpose. */
    protected static final String className = PackageTool.class.getName();

    /** Translator instance for localization. */
    private static final Translator translator = XA_Designer_Plugin.getTranslator();

    /** Identifier for Publishing to File System. */
    public static final String PUBLISH_TO_FILE_SYSTEM = "File system";

    /** Identifier for Publishing to Database. */
    public static final String PUBLISH_TO_DATABASE = "Database";

    /** Identifier for Publishing to Adaptive. */
    public static final String PUBLISH_TO_ADAPTIVE = "Adaptive";

    /** Default value for file to publish. */
    protected static String fileToPublish = "";

    /** Part of url for identifying publish related request. */
    public static String BIZVIEW = "publish";

    /** URL string used for SOAP actions. */
    public static String bizViewString = "xaware/XAServlet?_BIZVIEW=" + BIZVIEW;

    /** URL string for direct URI address of BizDoc. */
    public static String bizViewURIString = "/xaware/bizview/";

    /** NameSpace constant value. */
    public static final Namespace ns = XAwareConstants.xaNamespace;

    /** Flag used to know the status of publishing to server. */
    protected static boolean published = false;

    /** Directory for retained the previous file dialog directory. */
    protected static File m_currentDir = null;

    /** Temporary directory used while publishing the bizview files. */
    protected static final String OUT_TEMP_DIR = System.getProperty("xaware.home") + File.separator + "temp" + File.separator + "xarFiles";

    /**Constant to hold the string charset_encoding.*/
    private static final String CHARSET_ENCODING = "charset_encoding";

    /**Constant to hold the string request_type.*/
    private static final String REQUEST_TYPE = "request_type";

    /** Enumeration value for HTTP Request Methods. */
    private static enum RequestType {

        GET, PUT, POST, DELETE
    }

    ;

    /** Constant for Package Deployer Section*/
    private static final String PACKAGE_DEPLOYER_SECTION = "Package_Deployer_Settings";

    /** Constant for Package Deployer Charset */
    private static final String PACKAGE_DEPLOYER_CHARSET = "Package_Deployer_Charset";

    /** Constant for Package Deployer XML FileName */
    private static final String PACKAGE_DEPLOYER_XML_FILENAME = "Package_Deployer_XML_FileName";

    /** Constant for Package Deployer XML Content */
    private static final String PACKAGE_DEPLOYER_XML_CONTENT = "Package_Deployer_XML_Content";

    /** Class level logger. */
    protected XAwareLogger logger = XAwareLogger.getXAwareLogger(PackageTool.class.getName());

    /** For selecting archive file location. */
    protected Button packageButton;

    /** For deploying the archive file to server. */
    protected Button deployButton;

    /** Deleting the document from added documents list. */
    protected Button deleteButton;

    /** To exit from the dialog. */
    protected Button cancelButton;

    /** To exit from the dialog. */
    protected Button cancelTestButton;

    /** Host server URL. */
    public Text hostTF;

    /** For retriving roles from the server. */
    protected Button getRolesButton;

    /** Lists the avialable publish types. */
    public Combo publishTypesCB;

    /** Package contents table model. */
    public PubFileListTableModel fileTableModel;

    /** Packge content table. */
    protected Table fileTable;

    /** Package file name label. */
    protected Label publishPathLabel;

    /** Represents package file name. */
    protected Text packageTF;

    /** Choice whether to overwrite the archive file while deploying or not. */
    protected Button overwriteCB;

    /** For setting the focus to package file name text field. */
    protected boolean isPackageTextFocus = false;

    /** Default value for archive file. */
    protected String packageFileName = "";

    /** Used for selecting the host server directory. */
    protected Button browseHostJB;

    /** Used for selecting archive file location. */
    protected Button browsePackageFile;

    /** set true to Auto Select referenced components. */
    protected Button autoSelectCB;

    /** For seleting the bizview files to be included in the archive file. */
    protected Button addDocuments;

    /** Stores the supported publishing options. */
    protected Vector publishTypes;

    /** Stored file name and its absolute path details. */
    protected Hashtable<String, String> nameVsFullName = null;

    /** PackageBuilder instance. */
    protected PackageBuilder pkgBuilder = null;

    /** Tabfolder instance contains Package and Test tab items. */
    protected TabFolder tabPan;

    /** Dialog title. */
    protected String title = "Packager/Deployer Tool";

    /** Represents Package tab. */
    protected TabItem packageTab;

    /** Represents Deploy tab item. */
    protected TabItem deployTab;

    /** Represents Test tab item. */
    protected TabItem testTab;

    /** Roles available on the server. */
    protected Vector<String> availableRoles;

    /** Holds invalid files details. */
    protected Vector<Object> invalidFileList;

    /** Holds invalid XML files details. */
    protected Vector<Object> invalidXMLList;

    /** BizView file details. */
    public Vector otherBizView = null;

    /** Type of the bizview. */
    protected String bizViewName = "";

    /** Holds duplicate files details. */
    public Vector<String> duplicateFileList;

    /** For differentiating Publush type. */
    private String publishOption;

    /** Authentication panel instance. */
    protected AuthenticationPan authPan;

    /** Roles registered with server. */
    protected List rolesList;

    /** To retrieve roles from server. */
    protected Button getRoles;

    /** To apply the selected roles to selected documents. */
    protected Button applyToSelected;

    /** To apply the selected roles to all documents. */
    protected Button applyToAll;

    /** To clear the roles selection. */
    protected Button rolesCB;

    /** Publish dialog shell. */
    private Shell pkgDeployerShell;

    /** Attributes Hashmap. */
    protected HashMap attrsMap = null;

    /** Table content provider instance.*/
    protected PackageTableContentProvider contentProvider;

    /** Progress Monitor instance. */
    private ProgressMonitorPart progressMonitor;

    /**combo for the listing the http request types.*/
    private Combo reqTypeCombo;

    /**File chooser for selecting the xml file to be used with http post/put. */
    private FileChooserWithLabeling xmlFileChooser;

    /**Text box to display the contents of the xml file selected using the xmlFileChooser.*/
    private Text xmlFileContent;

    /**button to invoke the http operation.*/
    private Button invokeButton;

    /**editable combo to hold the url, to which the request is to be sent. */
    private Combo urlCombo;

    /** button to refresh the package contents.*/
    protected Button refreshButton;

    /**combo to list the charset of the current platform.*/
    private Combo charsetCombo;

    /**
     * Creates a new PackageTool object.
     * 
     * @param parent
     *            Shell
     * @param title
     *            String title to be displayed on packager tool dialog.
     */
    public PackageTool(final Shell parent, final String title) {
        super(parent, 0);
        super.setText(title);
        invalidFileList = new Vector<Object>();
        invalidXMLList = new Vector<Object>();
        duplicateFileList = new Vector<String>();
    }

    /**
     * Creates a new PackageTool object.
     */
    public PackageTool() {
        this(XA_Designer_Plugin.getShell(), "Package Deployer Tool");
    }

    /**
     * Clears the data from arraylists.
     */
    public void resetData() {
        clearInvalidFileList();
        clearInvalidXMLList();
        clearDuplicateFileList();
        fileToPublish = "";
        nameVsFullName.clear();
        published = false;
    }

    /**
     * Adds the files to list after parsing.
     * 
     * @param fileList
     *            ArrayList
     */
    public void setFiles(final ArrayList fileList) {
        for (int i = 0; i < fileList.size(); i++) {
            parseFile(fileList.get(i).toString(), nameVsFullName);
        }
    }

    /**
     * Updates invalid files list.
     * 
     * @param fileList
     *            ArrayList
     */
    public void setFilesForDataDiscovery(final ArrayList fileList) {
        for (int i = 0; i < fileList.size(); i++) {
            parseFileForDataDiscovery(fileList.get(i).toString(), nameVsFullName, ((BizCompDescriptor) fileList.get(i)).getName());
        }
    }

    /**
     * Shows Package Assemblt Tool dialog.
     */
    public void open() {
        final Shell parentShell = getParent();
        pkgDeployerShell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        pkgDeployerShell.setLayout(new GridLayout());
        pkgDeployerShell.setSize(800, 600);
        XA_Designer_Plugin.alignDialogToCenter(pkgDeployerShell);
        pkgDeployerShell.setImage(UserPrefs.getImageDescriptorIconFor(UserPrefs.XA_DESIGNER_DIALOG_IMAGE).createImage());
        pkgDeployerShell.setText("Package Assembly Tool");
        authPan = new AuthenticationPan(pkgDeployerShell, "XAware Engine Authentication");
        tabPan = new TabFolder(pkgDeployerShell, SWT.NONE);
        tabPan.setLayoutData(new GridData(GridData.FILL_BOTH));
        init();
        resetData();
        setSelected(0);
        tabPan.addSelectionListener(this);
        pkgDeployerShell.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                saveTestTabsettings();
            }
        });
        pkgDeployerShell.open();
    }

    /**
     * creats tabs and populates default values.
     */
    private void init() {
        setupPackageTab(tabPan);
        setupTestTab(tabPan);
        bizViewString = XASystemProps.getDefaultServlet();
        hostTF.setText(UserPrefs.getDefaultServerHost());
        final Element publishElem = UserPrefs.getPublishOption();
        if (publishElem == null) {
            publishOption = "XML";
        } else {
            publishOption = publishElem.getText().trim();
        }
    }

    /**
     * Creates controls of Package tab.
     * 
     * @param pkgTestTabFolder
     *            TabFolder
     */
    public void setupPackageTab(final TabFolder pkgTestTabFolder) {
        final Composite pkgControl = new Composite(pkgTestTabFolder, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        pkgControl.setLayout(gridLayout_1);
        pkgControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        getHostPanel(pkgControl);
        getRolesPanel(pkgControl);
        getPackageContentsTable(pkgControl);
        progressMonitor = new ProgressMonitorPart(pkgControl, new GridLayout());
        progressMonitor.setVisible(false);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        progressMonitor.setLayoutData(gridData);
        final Composite BtnsComp = new Composite(pkgControl, SWT.NONE);
        final GridData gridData_16 = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        gridData_16.horizontalSpan = 2;
        gridData_16.horizontalAlignment = SWT.CENTER;
        BtnsComp.setLayoutData(gridData_16);
        final GridLayout gridLayout_5 = new GridLayout();
        gridLayout_5.numColumns = 3;
        BtnsComp.setLayout(gridLayout_5);
        packageButton = ControlFactory.createButton(BtnsComp, "Create &Package");
        packageButton.addSelectionListener(this);
        deployButton = ControlFactory.createButton(BtnsComp, "&Deploy");
        deployButton.addSelectionListener(this);
        cancelButton = ControlFactory.createButton(BtnsComp, "&Close");
        cancelButton.addSelectionListener(this);
        cancelButton.addMouseListener(this);
        packageTab = new TabItem(tabPan, SWT.NONE);
        packageTab.setText("Package");
        packageTab.setImage(UserPrefs.getImageDescriptorIconFor(UserPrefs.PUBLISH).createImage());
        packageTab.setControl(pkgControl);
    }

    /**
     * Make the auto select check boxstatus visible.
     * 
     * @return Returns true if the check box is enabled.
     */
    public boolean isAutoSelectEnabled() {
        if (autoSelectCB != null) {
            return autoSelectCB.getSelection();
        }
        return false;
    }

    /**
     * Creates Package contents table.
     * 
     * @param pkgControl
     *            Composite
     */
    private void getPackageContentsTable(final Composite pkgControl) {
        final Group pkgGrp = new Group(pkgControl, SWT.NONE);
        final GridData gridData_15 = new GridData(GridData.FILL_BOTH);
        gridData_15.horizontalSpan = 2;
        pkgGrp.setLayoutData(gridData_15);
        pkgGrp.setText("Package Contents");
        pkgGrp.setLayout(new GridLayout());
        final int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;
        nameVsFullName = new Hashtable<String, String>();
        fileTableModel = new PubFileListTableModel();
        final int[] columnWidths = new int[] { 115, 300, 115, 115, 115 };
        final java.util.List tableDataList = new PublishTableData(fileTableModel.getElements()).getTableData();
        contentProvider = new PackageTableContentProvider(pkgGrp, style, false, 4, tableDataList, fileTableModel.getColumnIdentifiers(), columnWidths);
        contentProvider.enableSortTable(new boolean[] { true, true, true, true, true });
        fileTable = contentProvider.getTable();
        final GridData gridData_1 = new GridData(GridData.FILL_BOTH);
        gridData_1.heightHint = 100;
        fileTable.setLayoutData(gridData_1);
        fileTable.setHeaderVisible(true);
        fileTable.setLinesVisible(true);
        final Composite btnsComp = new Composite(pkgGrp, SWT.NONE);
        final GridData btnsGD = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        btnsGD.horizontalAlignment = SWT.END;
        btnsComp.setLayoutData(btnsGD);
        final GridLayout btnsGL = new GridLayout();
        btnsGL.numColumns = 4;
        btnsComp.setLayout(btnsGL);
        autoSelectCB = new Button(btnsComp, SWT.CHECK);
        autoSelectCB.setText("Auto-select referenced files");
        autoSelectCB.setToolTipText("Checked will automaticaly select dependent BizView files");
        autoSelectCB.setSelection(true);
        addDocuments = ControlFactory.createButton(btnsComp, "&Add Document...");
        pkgDeployerShell.setDefaultButton(addDocuments);
        addDocuments.addSelectionListener(this);
        refreshButton = ControlFactory.createButton(btnsComp, "&Refresh");
        refreshButton.addSelectionListener(this);
        deleteButton = ControlFactory.createButton(btnsComp, "De&lete");
        deleteButton.addSelectionListener(this);
    }

    /**
     * Create Host panel and return the panel.
     * 
     * @param panel
     *            Composite
     */
    public void getHostPanel(final Composite panel) {
        final Group hostFileGrp = new Group(panel, SWT.NONE);
        hostFileGrp.setText("Host and File Information");
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.verticalSpacing = 13;
        hostFileGrp.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 115;
        gridData.widthHint = 400;
        hostFileGrp.setLayoutData(gridData);
        final Label packageFileNameLbl = new Label(hostFileGrp, SWT.NONE);
        packageFileNameLbl.setText("Package file name:");
        packageTF = ControlFactory.createText(hostFileGrp, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 15;
        gridData.widthHint = 150;
        packageTF.setLayoutData(gridData);
        packageTF.addFocusListener(this);
        packageTF.addKeyListener(new PackageKeyListener());
        browsePackageFile = ControlFactory.createButton(hostFileGrp, "&Browse...");
        browsePackageFile.setLayoutData(new GridData());
        browsePackageFile.addSelectionListener(this);
        overwriteCB = new Button(hostFileGrp, SWT.CHECK);
        gridData = new GridData();
        gridData.horizontalSpan = 3;
        overwriteCB.setLayoutData(gridData);
        overwriteCB.setText("Overwrite package file on deploy");
        final Label XAiServerHostLbl = new Label(hostFileGrp, SWT.NONE);
        XAiServerHostLbl.setText("XAware Engine Host:");
        hostTF = ControlFactory.createText(hostFileGrp, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 15;
        gridData.widthHint = 150;
        hostTF.setLayoutData(gridData);
    }

    /**
     * Build roles panel and return the panel.
     * 
     * @param panel
     *            Composite
     */
    public void getRolesPanel(final Composite panel) {
        final Group RolesGrp = new Group(panel, SWT.NONE);
        RolesGrp.setText("Roles");
        GridData gridData = new GridData();
        gridData.heightHint = 115;
        gridData.widthHint = 220;
        RolesGrp.setLayoutData(gridData);
        GridLayout gridLayout = new GridLayout(2, false);
        RolesGrp.setLayout(gridLayout);
        rolesList = new List(RolesGrp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 100;
        rolesList.setLayoutData(gridData);
        final Composite ButtonsComp = new Composite(RolesGrp, SWT.NONE);
        gridData = new GridData();
        gridData.widthHint = 108;
        ButtonsComp.setLayoutData(gridData);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        ButtonsComp.setLayout(gridLayout);
        getRoles = ControlFactory.createButton(ButtonsComp, "Get &Roles");
        gridData = new GridData();
        gridData.widthHint = 98;
        getRoles.setLayoutData(gridData);
        getRoles.addSelectionListener(this);
        applyToSelected = ControlFactory.createButton(ButtonsComp, "Apply To &Selected");
        applyToSelected.addSelectionListener(this);
        applyToAll = ControlFactory.createButton(ButtonsComp, "Apply &To All");
        gridData = new GridData();
        gridData.widthHint = 98;
        applyToAll.setLayoutData(gridData);
        applyToAll.addSelectionListener(this);
        rolesCB = new Button(ButtonsComp, SWT.CHECK);
        gridData = new GridData();
        gridData.widthHint = 98;
        rolesCB.setLayoutData(gridData);
        rolesCB.setText("Clear Roles");
        rolesCB.addSelectionListener(this);
    }

    /**
     * Constructs Test Tab item.
     * 
     * @param publishTabFolder
     *            TabFolder
     */
    private void setupTestTab(final TabFolder publishTabFolder) {
        final Composite testTabControl = new Composite(publishTabFolder, SWT.NONE);
        testTabControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        testTabControl.setLayout(gridLayout);
        final Label urlLbl = new Label(testTabControl, SWT.FILL);
        GridData gridData = new GridData();
        urlLbl.setLayoutData(gridData);
        urlLbl.setText("URL: ");
        urlCombo = new Combo(testTabControl, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        urlCombo.setLayoutData(gridData);
        final Label reqTypeLabel = new Label(testTabControl, SWT.FILL);
        gridData = new GridData();
        reqTypeLabel.setLayoutData(gridData);
        reqTypeLabel.setText("Request Type:    ");
        reqTypeCombo = new Combo(testTabControl, SWT.BORDER | SWT.READ_ONLY);
        gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        reqTypeCombo.setLayoutData(gridData);
        reqTypeCombo.addModifyListener(this);
        Group xmlFileGroup = new Group(testTabControl, SWT.NONE);
        xmlFileGroup.setLayout(new GridLayout(2, false));
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 2;
        gridData.verticalIndent = 10;
        xmlFileGroup.setLayoutData(gridData);
        xmlFileGroup.setText("POST/PUT options");
        xmlFileChooser = new FileChooserWithLabeling(xmlFileGroup, SWT.NONE, "XML File Name: ", XA_Designer_Plugin.getActiveEditedInternalFrameFileDirectory(), "Browse...", XAChooser.XML);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        xmlFileChooser.setLayoutData(gridData);
        xmlFileChooser.setMode(SWT.OPEN);
        xmlFileChooser.setChooserType(XAChooser.INPUT_TYPE);
        xmlFileChooser.modifyListenerForTextField(this);
        final Label xmlFileContentLabel = new Label(xmlFileGroup, SWT.FILL);
        gridData = new GridData();
        gridData.widthHint = xmlFileChooser.getLabel().computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        xmlFileContentLabel.setLayoutData(gridData);
        xmlFileContentLabel.setText("XML Content: ");
        xmlFileContent = ControlFactory.createText(xmlFileGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        gridData = new GridData(GridData.FILL_BOTH);
        xmlFileContent.setLayoutData(gridData);
        final Label charsetLabel = new Label(xmlFileGroup, SWT.FILL);
        gridData = new GridData();
        charsetLabel.setLayoutData(gridData);
        charsetLabel.setText("Charset: ");
        charsetCombo = new Combo(xmlFileGroup, SWT.BORDER | SWT.READ_ONLY);
        gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        charsetCombo.setLayoutData(gridData);
        populateCombo(reqTypeCombo, REQUEST_TYPE);
        populateCombo(charsetCombo, CHARSET_ENCODING);
        reqTypeCombo.select(0);
        loadTestTabSettings();
        final GridData invokeButtonGridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        invokeButtonGridData.horizontalSpan = 2;
        invokeButton = ControlFactory.createButton(testTabControl, "Invoke", false, invokeButtonGridData);
        invokeButton.addSelectionListener(this);
        final Label empty13Lbl = new Label(testTabControl, SWT.NONE);
        final GridData gridData_19 = new GridData(GridData.FILL_BOTH);
        gridData_19.horizontalSpan = 2;
        empty13Lbl.setLayoutData(gridData_19);
        final GridData bizViewCloseBtnGridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        bizViewCloseBtnGridData.horizontalSpan = 2;
        cancelTestButton = ControlFactory.createButton(testTabControl, "&Close", false, bizViewCloseBtnGridData);
        cancelTestButton.addSelectionListener(this);
        testTab = new TabItem(publishTabFolder, SWT.NONE);
        testTab.setText("Test");
        testTab.setImage(UserPrefs.getImageDescriptorIconFor(UserPrefs.CLASS_NODE).createImage());
        testTab.setControl(testTabControl);
    }

    /**
     * Populates the given combo as per the given type.
     * 
     * @param inCombo combo to be populated.
     * @param population_type type of population.
     */
    private void populateCombo(Combo inCombo, String population_type) {
        Object[] items = null;
        if (population_type.equals(REQUEST_TYPE)) items = UserPrefs.getHTTPMethodList().toArray(); else if (population_type.equals(CHARSET_ENCODING)) items = Charset.availableCharsets().keySet().toArray();
        if (items != null && items.length > 0) {
            for (Object obj : items) {
                inCombo.add(obj.toString());
            }
        }
    }

    /**
     * Returns Vector object that holds BizView Files.
     * 
     * @return Vector object.
     */
    public static Vector getOtherPublishBizView() {
        final Element publishTypesElem = XAwareConfig.GetInstance().getPublishTypesElement();
        final java.util.List childList = publishTypesElem.getChildren();
        Vector<String> others = null;
        if (childList != null) {
            others = new Vector<String>(childList.size());
            for (int i = 0; i < childList.size(); i++) {
                final Element childElem = (Element) childList.get(i);
                if (childElem.getName().equals("Other")) {
                    final String otherType = childElem.getText().trim();
                    if (otherType.equals(PUBLISH_TO_DATABASE) || otherType.equals(PUBLISH_TO_ADAPTIVE)) {
                        others.add(otherType);
                    }
                }
            }
        }
        return others;
    }

    /**
     * Deletes the selected documents from the table.
     */
    public void deleteDocuments() {
        final int[] selRows = fileTable.getSelectionIndices();
        final String[] deleteFiles = new String[selRows.length];
        for (int i = 0; i < selRows.length; i++) {
            final String name = (String) contentProvider.getValueAt(selRows[i], PubFileListTableModel.FULLNAME);
            deleteFiles[i] = name;
            final File delFile = new File(OUT_TEMP_DIR + File.separator + new File(name).getName());
            if (delFile.exists()) {
                deleteParentDirs(delFile);
            }
        }
        contentProvider.removeSelectedRows();
        getPackageBuilder().deleteDocuments(deleteFiles);
    }

    /**
     * Clean the parent directory if the selected file is in temporary directory.
     * 
     * @param dir
     *            File
     */
    protected void deleteParentDirs(File dir) {
        final String methodName = "deleteParentDirs";
        boolean dirsExist = dir.exists();
        while (dirsExist) {
            String dirName = dir.getAbsolutePath();
            if (dir.exists() && (dirName.length() > OUT_TEMP_DIR.length())) {
                logger.finer("Deleting directory: " + dirName, className, methodName);
                if (dir.delete()) {
                    dirName = dir.getParent();
                    dir = new File(dirName);
                    if (dir == null) {
                        dirsExist = false;
                    }
                } else {
                    dirsExist = false;
                }
            } else {
                dirsExist = false;
                logger.finest("Directory deleting stopped at dir: " + dir.getAbsolutePath(), className, methodName);
            }
        }
    }

    /**
     * Called when package name text field receives focus and sets the focus flag.
     * 
     * @param e
     *            FocusEvent
     */
    public void focusGained(final FocusEvent e) {
        if (e.getSource() == packageTF) {
            isPackageTextFocus = true;
        }
    }

    /**
     * Called when package name text field losts focus and sets the focus flag.
     * 
     * @param e
     *            FocusEvent
     */
    public void focusLost(final FocusEvent e) {
        if (e.getSource() == packageTF) {
            isPackageTextFocus = false;
        }
    }

    /**
     * Returns PackageBuilder instance.
     * 
     * @return PackageBuilder
     */
    protected PackageBuilder getPackageBuilder() {
        if (pkgBuilder == null) {
            pkgBuilder = new PackageBuilder(this, nameVsFullName, fileTableModel, contentProvider);
        }
        PackageBuilder.progressMonitorRunning = false;
        return pkgBuilder;
    }

    /**
     * Retrurns the file name after performing check for adding to duplicate list.
     * 
     * @param inFile
     *            String
     * 
     * @return String
     */
    public String publishNameForFile(final String inFile) {
        int indexOfSep = inFile.lastIndexOf(File.separator);
        if (inFile.lastIndexOf('/') > indexOfSep) {
            indexOfSep = inFile.lastIndexOf('/');
        }
        if (nameVsFullName.containsKey(inFile.substring(indexOfSep + 1))) {
            if (!duplicateFileList.contains(nameVsFullName.get(inFile.substring(indexOfSep + 1)))) {
                duplicateFileList.add((String) nameVsFullName.get(inFile.substring(indexOfSep + 1)));
            }
            duplicateFileList.add(inFile);
            String aliasName = inFile.substring(indexOfSep + 1) + 1;
            while (nameVsFullName.containsKey(aliasName)) {
                aliasName = aliasName + 1;
            }
            return aliasName;
        } else {
            return inFile.substring(indexOfSep + 1);
        }
    }

    /**
     * Retrurns the file name.
     * 
     * @param inFile
     *            String
     * 
     * @return String
     */
    public String getFileNameFromPath(final String inFile) {
        int indexOfSep = inFile.lastIndexOf(File.separator);
        if (inFile.lastIndexOf('/') > indexOfSep) {
            indexOfSep = inFile.lastIndexOf('/');
        }
        return inFile.substring(indexOfSep + 1);
    }

    /**
     * Used by either applyToAll or applyToSelected buttons. If true is sent in then only selected rows will have roles
     * applied. Otherwise all rows will have the selected roles applied.
     * 
     * @param applyToSelectedFlag
     *            boolean
     */
    protected void applyRolesToSelectedRows(final boolean applyToSelectedFlag) {
        final Object[] selRoles = rolesList.getSelection();
        if ((rolesCB.getSelection() == false) && (selRoles.length == 0)) {
            ControlFactory.showMessageDialog(translator.getString("No roles selected.  Please select the roles to apply."), translator.getString("Information"));
            return;
        }
        String valRoles = new String();
        for (int i = 0; i < selRoles.length; i++) {
            if (valRoles.length() > 0) {
                valRoles += ("," + (String) selRoles[i]);
            } else {
                valRoles = (String) selRoles[i];
            }
        }
        if (applyToSelectedFlag) {
            final int[] selRows = fileTable.getSelectionIndices();
            if (selRows.length == 0) {
                ControlFactory.showMessageDialog(translator.getString("No rows selected.  Please select the rows to set roles."), translator.getString("Information"));
            }
            for (int i = 0; i < selRows.length; i++) {
                fileTableModel.setValueAt(valRoles, selRows[i], PubFileListTableModel.ROLE);
                contentProvider.setValueAt(valRoles, selRows[i], PubFileListTableModel.ROLE);
            }
        } else {
            final int numRows = fileTable.getItemCount();
            if (numRows == 0) {
                ControlFactory.showMessageDialog(translator.getString("Please add files to your package before applying roles to your non-existent files."), translator.getString("Information"));
            }
            for (int i = 0; i < numRows; i++) {
                fileTableModel.setValueAt(valRoles, i, PubFileListTableModel.ROLE);
                contentProvider.setValueAt(valRoles, i, PubFileListTableModel.ROLE);
            }
        }
        getPackageBuilder().setIsSaved(false);
    }

    /**
     * Returns the registered user roles with the server.
     * 
     * @return Vector
     */
    public Vector getAvailableRoles() {
        availableRoles = new Vector<String>();
        availableRoles.add("");
        final String getRolesBizDoc = "xaware/admin/publish/getDesignerRoles.xbd";
        try {
            XAHostFileTreeNode.hostString = hostTF.getText().trim();
            XAHostFileTreeNode.bizViewString = bizViewString + "?_BIZVIEW=" + getRolesBizDoc;
            String urlString = hostTF.getText().trim();
            final String bizViewStr = bizViewString + "?_BIZVIEW=" + getRolesBizDoc;
            if (!urlString.endsWith("/")) {
                urlString += "/";
            }
            urlString += bizViewStr;
            final Element res = postMessageWithAuthentication("<GetAvailableRoles />", urlString, authPan.getUid(), authPan.getPwd());
            Element rolesElem = res.getChild("list");
            String roles = "";
            if (rolesElem != null) {
                roles = rolesElem.getText().trim();
            }
            StringTokenizer st = new StringTokenizer(roles, ",");
            while (st.hasMoreTokens()) {
                availableRoles.add(st.nextToken());
            }
        } catch (final Exception exception) {
            final String message = "Error getting roles from host. Exception: " + exception;
            availableRoles = new Vector<String>();
            logger.severe(message);
            ControlFactory.showInfoDialog(translator.getString("Error getting roles from host."), message);
            return availableRoles;
        }
        if (availableRoles.size() <= 0) {
            ControlFactory.showMessageDialog(translator.getString("No roles received from server."), translator.getString("Information"));
        }
        return availableRoles;
    }

    /**
     * Parses the given file and updates the invalid file list if it is not a file.
     * 
     * @param inFile
     *            String
     * @param inHash
     *            Hashtable
     */
    private void parseFile(final String inFile, final Hashtable<String, String> inHash) {
        try {
            if (inFile.trim().equals("")) {
                return;
            }
            if (!new File(inFile).isFile()) {
                if (!invalidFileList.contains(inFile)) {
                    invalidFileList.add(inFile);
                    logger.finest("File " + inFile + " not found.");
                }
                return;
            }
            final Document mainDoc = UserPrefs.getSAXBuilder().build(new File(inFile));
            inHash.put(publishNameForFile(inFile), inFile);
            parseElement(mainDoc.getRootElement(), inHash);
        } catch (final org.jdom.JDOMException exception) {
            if (!publishOption.equals("SOAP")) {
                logger.finest("Exception parsing file to publish : " + exception);
                if (!invalidXMLList.contains(inFile)) {
                    invalidXMLList.add(inFile);
                    logger.finest("File " + inFile + " not found.");
                }
            } else {
                inHash.put(publishNameForFile(inFile), inFile);
            }
        } catch (final Exception exception) {
            logger.finest("Exception parsing file to publish : " + exception);
            if (!invalidFileList.contains(inFile)) {
                invalidFileList.add(inFile);
                logger.finest("File " + inFile + " cannot be parsed");
            }
        }
    }

    /**
     * Parses the file recursively.
     * 
     * @param inFile
     *            String
     * @param inHash
     *            Hashtable
     * @param name
     *            String
     */
    private void parseFileForDataDiscovery(final String inFile, final Hashtable<String, String> inHash, final String name) {
        try {
            if (inFile.trim().equals("")) {
                return;
            }
            if (!new File(inFile).isFile()) {
                if (!invalidFileList.contains(inFile)) {
                    invalidFileList.add(inFile);
                    logger.finest("File " + inFile + " not found.");
                }
                return;
            }
            final Document mainDoc = UserPrefs.getSAXBuilder().build(new File(inFile));
            inHash.put(name, inFile);
            parseElement(mainDoc.getRootElement(), inHash);
        } catch (final org.jdom.JDOMException exception) {
            if (!publishOption.equals("SOAP")) {
                logger.finest("Exception parsing file to publish : " + exception);
                if (!invalidXMLList.contains(inFile)) {
                    invalidXMLList.add(inFile);
                    logger.finest("File " + inFile + " not found.");
                }
            } else {
                inHash.put(name, inFile);
            }
        } catch (final Exception exception) {
            logger.finest("Exception parsing file to publish : " + exception);
            if (!invalidFileList.contains(inFile)) {
                invalidFileList.add(inFile);
                logger.finest("File " + inFile + " cannot be parsed");
            }
        }
    }

    /**
     * Parses the XML file element.
     * 
     * @param e
     *            Element
     * @param ht
     *            Hashtable
     */
    private void parseElement(final Element e, final Hashtable<String, String> ht) {
        final Iterator itr = e.getAttributes().iterator();
        while (itr.hasNext()) {
            final Attribute att = (Attribute) itr.next();
            final Namespace curNS = att.getNamespace();
            if (curNS != null) {
                if ((att.getName().equals("bizdoc") && curNS.equals(ns)) || (att.getName().equals("bizcomp") && curNS.equals(ns)) || (att.getName().equals("bizdriver") && curNS.equals(ns))) {
                    if (fileCount(att.getValue().trim()) == 0) {
                        parseFile(att.getValue().trim(), ht);
                    }
                } else if ((att.getName().equals("transform") && curNS.equals(ns)) || (att.getName().equals("file") && curNS.equals(ns)) || (att.getName().equals("source") && curNS.equals(ns))) {
                    if (fileCount(att.getValue().trim()) == 0) {
                        final File filename = new File(att.getValue().trim());
                        if (!filename.isFile()) {
                            if (!invalidFileList.contains(filename)) {
                                invalidFileList.add(filename);
                                logger.finest("File " + filename + " not found.");
                            }
                            continue;
                        }
                        ht.put(publishNameForFile(att.getValue().trim()), att.getValue().trim());
                    }
                }
            }
        }
        final Iterator children = e.getChildren().iterator();
        while (children.hasNext()) {
            parseElement((Element) children.next(), ht);
        }
    }

    /**
     * Returns the files selected for archiving.
     * 
     * @param fileName
     *            String
     * 
     * @return int
     */
    public int fileCount(final String fileName) {
        int count = 0;
        final Collection collection = nameVsFullName.values();
        if (collection != null) {
            final File file1 = new File(fileName);
            final Iterator iter = collection.iterator();
            while (iter.hasNext()) {
                final String fname = (String) iter.next();
                final File file2 = new File(fname);
                try {
                    if (file1.getCanonicalPath().equals(file2.getCanonicalPath())) {
                        count++;
                    }
                } catch (final Exception exception) {
                    logger.finest("Error converting filename to canonical name." + exception);
                }
            }
        }
        return count;
    }

    /**
     * Returns true if the given String contains special characters else false.
     * 
     * @param name
     *            String
     * @param testString
     * 
     * @return boolean
     */
    public boolean containsSpecialChar(final String name, final String testString) {
        if ((testString == null) || (testString.length() <= 0)) {
            return false;
        }
        if ((testString.indexOf('%') > -1) || (testString.indexOf('#') > -1) || (testString.indexOf('\"') > -1) || (testString.indexOf('<') > -1) || (testString.indexOf('>') > -1) || (testString.indexOf('&') > -1)) {
            final String msg = name + " contains an invalid special character.";
            final String detailMsg = msg + " Special character % # \" & < > found in string: " + testString;
            ControlFactory.showInfoDialog(translator.getString(msg), detailMsg);
            return true;
        }
        final String numtest = testString.substring(0, 1);
        try {
            Integer.parseInt(numtest);
            final String msg = testString + " begins with an integer.";
            final String detailMsg = msg + " Alias names beginning with numbers cannot be published.";
            ControlFactory.showInfoDialog(translator.getString(msg), detailMsg);
            return true;
        } catch (final NumberFormatException n) {
        }
        return false;
    }

    /**
     * Given dummy implementation
     */
    public void publishUsingSOAP() {
    }

    /**
     * Replace special chars with relative text for parsing.
     * 
     * @param string
     *            String
     * 
     * @return String
     */
    public static String escapeSpecialChars(final String string) {
        if (string == null) {
            return null;
        }
        final StringBuffer sb = new StringBuffer(string.length());
        final int len = string.length();
        char c;
        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == '"') {
                sb.append("&quot;");
            } else if (c == '&') {
                sb.append("&amp;");
            } else if (c == '<') {
                sb.append("&lt;");
            } else if (c == '>') {
                sb.append("&gt;");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Returns the alias name if the publish type is adaptive.
     * 
     * @param bizView
     *            String
     * @param type
     *            String
     * 
     * @return String
     */
    protected String generateAdaptiveAliasName(final String bizView, final String type) {
        String adaptiveAliasName = null;
        if (this.publishTypesCB.getItem(publishTypesCB.getSelectionIndex()).trim().equals(PUBLISH_TO_ADAPTIVE)) {
            int firstPos = 0;
            adaptiveAliasName = bizView;
            if (!type.equals("BizDriver")) {
                firstPos = adaptiveAliasName.indexOf(".") + 1;
            }
            final int lastPos = adaptiveAliasName.lastIndexOf(".");
            adaptiveAliasName = adaptiveAliasName.substring(firstPos, lastPos);
        }
        return adaptiveAliasName;
    }

    /**
     * build hashtables used in validating and publishing files. Use fileTableModel for required actual filename and
     * alias name.
     * 
     * @param bizViewVsFullFile
     *            Hashtable
     * @param fullFileVsBizViewHash
     *            Hashtable
     * 
     * @throws Exception
     *             Thrown when alias name is not unique
     */
    protected void buildFileHashtables(final Hashtable<String, String> bizViewVsFullFile, final Hashtable<String, String> fullFileVsBizViewHash) throws Exception {
        final Iterator itr1 = contentProvider.getDataVector().iterator();
        while (itr1.hasNext()) {
            final Vector curRow = (Vector) itr1.next();
            String tmpFileName = (String) curRow.elementAt(PubFileListTableModel.FILE);
            String bizView = (String) curRow.elementAt(PubFileListTableModel.ALIAS) + File.separator + (String) curRow.elementAt(PubFileListTableModel.FILE);
            if (bizViewVsFullFile.get(bizView) != null) {
                throw new Exception(translator.getString("Publish cancelled.  Name must be unique: " + bizView));
            }
            final String adaptiveAliasName = generateAdaptiveAliasName(bizView, curRow.elementAt(PubFileListTableModel.TYPE).toString());
            if (adaptiveAliasName != null) {
                bizView = adaptiveAliasName;
            }
            bizViewVsFullFile.put(bizView, tmpFileName);
            final File tmpFile = new File(tmpFileName);
            tmpFileName = tmpFile.getCanonicalPath();
            fullFileVsBizViewHash.put(tmpFileName, bizView);
        }
    }

    /**
     * For each row extract the table values and update fileAttrs Vector with the correct attributes. If all is correct
     * return the filename.
     * 
     * @param xarFile
     *            File
     * 
     * @return Hashtable
     * 
     * @throws Exception
     *             Thrown if the attributes are not set properly
     */
    protected Hashtable<String, String> buildFileAttrs(final File xarFile) throws Exception {
        final String methodName = "buildFileAttrs";
        logger.entering(className, methodName);
        Hashtable<String, String> fileAttrs = new Hashtable<String, String>();
        final String xarName = xarFile.getName();
        try {
            String overwrite = "no";
            if (overwriteCB.getSelection()) {
                overwrite = "yes";
            }
            fileAttrs.put(PublishUsingSOAP.propAttrs[PublishUsingSOAP.OVERWRITE], overwrite);
            fileAttrs.put(PublishUsingSOAP.propAttrs[PublishUsingSOAP.ALIAS], xarName);
            fileAttrs.put(PublishUsingSOAP.propAttrs[PublishUsingSOAP.TYPE], PublishUsingSOAP.XAR_FILE);
            fileAttrs.put(PublishUsingSOAP.propAttrs[PublishUsingSOAP.CHECKIN], "false");
            fileAttrs.put(PublishUsingSOAP.propAttrs[PublishUsingSOAP.SOAPACTION], "");
            String loc = "/";
            if (loc.equals("") || (!loc.endsWith("/") && !loc.endsWith("\\"))) {
                loc += "/";
            }
            final String description = "XAR File";
            fileAttrs.put(PublishUsingSOAP.propAttrs[PublishUsingSOAP.DESCRIPTION], description);
            fileAttrs.put(PublishUsingSOAP.propAttrs[PublishUsingSOAP.LOCATION], xarName);
            fileAttrs.put(PublishUsingSOAP.propAttrs[PublishUsingSOAP.LOC_LOCATION], xarFile.getAbsolutePath());
            final String defaultRole = "";
            if ((defaultRole != null) && !defaultRole.equals("")) {
                fileAttrs.put(PublishUsingSOAP.propAttrs[PublishUsingSOAP.ROLE], defaultRole);
            }
            logAttrs(fileAttrs);
        } catch (final Exception exception) {
            logger.severe("Exception caught trying to create attributes vector. Exception: " + exception);
            logger.printStackTrace(exception);
            fileAttrs = null;
            throw exception;
        }
        return fileAttrs;
    }

    /**
     * A method for logging debug information about publish attributes.
     * 
     * @param attrsTable
     *            Vector
     */
    protected void logAttrs(final Hashtable attrsTable) {
        final String methodName = "logAttrs";
        logger.entering(className, methodName);
        logger.finer("Publish attributes for " + attrsTable.get(PublishUsingSOAP.propAttrs[PublishUsingSOAP.ALIAS]));
        for (int i = 0; i < PublishUsingSOAP.propAttrs.length; i++) {
            final String attrName = PublishUsingSOAP.propAttrs[i];
            if (attrName != null) {
                logger.finer("   attribute " + attrName + " = " + attrsTable.get(attrName));
            }
        }
    }

    /**
     * Use the fileTableModel to build a Vector of attributes for publishing and create temporary files for the
     * PublishUsingSOAP class to send to the server.
     * 
     * @param packageXar
     *            String
     */
    protected void deploy(final String packageXar) {
        final String methodName = "deploy";
        logger.entering(className, methodName);
        try {
            final File packageFile = new File(packageXar);
            if (packageFile.exists() == false) {
                throw new Exception("Package file does not exist: " + packageXar);
            }
            final Hashtable<String, String> fileAttrs = buildFileAttrs(packageFile);
            final Vector<Hashtable<String, String>> deployFiles = new Vector<Hashtable<String, String>>();
            deployFiles.add(fileAttrs);
            if (fileAttrs != null) {
                final String host = hostTF.getText().trim();
                final PublishUsingSOAP soap = new PublishUsingSOAP(host, deployFiles, authPan.getUid(), authPan.getPwd());
                final Element resp = soap.publish();
                displayReturnDlg(resp);
            }
        } catch (final Exception exception) {
            final String msg = "Error deploying file. Exception: " + exception;
            logger.severe(msg);
            ControlFactory.showInfoDialog(translator.getString("Error deploying file(s)."), msg);
        } finally {
            duplicateFileList.clear();
        }
    }

    /**
     * Display the response returned from publishing files.
     * 
     * @param reply
     *            Element
     */
    protected void displayReturnDlg(final Element reply) {
        final PublishResponse pr = new PublishResponse(reply);
        final String dtlMsg = pr.getMessage();
        final String summaryMsg = pr.getStatus();
        ControlFactory.showInfoDialog(summaryMsg, dtlMsg, true);
    }

    /**
     * Sends request URL to the server with authentication details.
     * 
     * @param updatedString
     *            String
     * @param urlString
     *            String
     * @param uid
     *            String
     * @param pwd
     *            String
     * 
     * @return Element
     */
    public static Element postMessageWithAuthentication(final String updatedString, final String urlString, final String uid, final String pwd) {
        Element res = null;
        try {
            final XAHttpClient httpConnector = new XAHttpClient(urlString);
            httpConnector.init(uid, pwd);
            final SAXBuilder xsb = new SAXBuilder();
            final Element requestElem = xsb.build(new StringReader(updatedString)).getRootElement();
            final InputStream responseStream = httpConnector.executePost(requestElem);
            res = xsb.build(responseStream).getRootElement();
        } catch (final Exception exception) {
            ControlFactory.showInfoDialog(translator.getString("Error posting message to server."), exception.toString());
        }
        return res;
    }

    /**
     * Sends request in XML element format to the server with authentication details.
     * 
     * @param requestElem
     *            Element
     * @param urlString
     *            String
     * @param uid
     *            String
     * @param pwd
     *            String
     * 
     * @return Element
     */
    public Element postMessageWithAuthentication(final Element requestElem, final String urlString, final String uid, final String pwd) {
        Element res = null;
        try {
            final XAHttpClient httpConnector = new XAHttpClient(urlString);
            httpConnector.init(uid, pwd);
            final InputStream responseStream = httpConnector.executePost(requestElem);
            final SAXBuilder xsb = new SAXBuilder();
            res = xsb.build(responseStream).getRootElement();
        } catch (final Exception exception) {
            ControlFactory.showInfoDialog(translator.getString("Error posting message to server."), exception.toString());
        }
        return res;
    }

    /**
     * Returns true if the type any of the bizview files type.
     * 
     * @param type
     *            String
     * 
     * @return boolean
     */
    public boolean isModifyReference(final String type) {
        boolean retBool = false;
        if (type.equals("xa:bizdoc")) {
            retBool = true;
        } else if (type.equals("xa:bizcomp")) {
            retBool = true;
        } else if (type.equals("xa:bizcomponent")) {
            retBool = true;
        } else if (type.equals("xa:bizdriver")) {
            retBool = true;
        }
        return retBool;
    }

    /**
     * Parses the result sent by the server.
     * 
     * @param inRes
     *            Element
     * 
     * @return String
     */
    public String parsePublishResult(final Element inRes) {
        String errorString = null;
        if (inRes.getName().equals("XAwareError")) {
            final Element error = inRes.getChild("error");
            if (error != null) {
                errorString = error.getText();
            } else {
                errorString = translator.getString("Unknown server error occurred.\n");
            }
        }
        final Iterator itr = inRes.getChildren("fileToPublish").iterator();
        while (itr.hasNext()) {
            final Element aFileToPub = (Element) itr.next();
            final Element componentElem = aFileToPub.getChild("component", ns);
            if (componentElem == null) {
                if (errorString == null) {
                    errorString = translator.getString("The following errors occurred : \n");
                }
                errorString += (aFileToPub.getName() + translator.getString(" failed : Unknown Status") + "\n");
                continue;
            }
            final Element statusElem = componentElem.getChild("status", ns);
            if ((statusElem == null) || (statusElem.getText() == null) || statusElem.getText().equals("")) {
                if (errorString == null) {
                    errorString = translator.getString("The following errors occurred : \n");
                }
                errorString += (aFileToPub.getName() + translator.getString(" failed : Unknown Status") + "\n");
            } else if (!statusElem.getText().equals("SUCCESS")) {
                if (errorString == null) {
                    errorString = translator.getString("The following errors occurred : \n");
                }
                errorString += (statusElem.getText() + "\n");
            }
        }
        return errorString;
    }

    /**
     * Returns the attribute name for the given type.
     * 
     * @param inFile
     *            String
     * 
     * @return String
     */
    public static String getType(final String inFile) {
        if (inFile.equals("BizDoc")) {
            return "xa:bizdoc";
        } else if (inFile.equals("BizComp")) {
            return "xa:bizcomponent";
        } else if (inFile.equals("BizDriver")) {
            return "xa:bizdriver";
        } else if (inFile.equals("WSDL")) {
            return "xa:wsdl";
        } else if (inFile.equals("Schema")) {
            return "xa:schema";
        } else {
            return "xa:other";
        }
    }

    /**
     * Removes the extra characters and stores the namespace prefix and URI.
     * 
     * @param inBuff
     *            StringBuffer
     * 
     * @return Hashtable
     */
    protected static Hashtable stripNamespaces(final StringBuffer inBuff) {
        final int indOfDocDecl = inBuff.indexOf("<?xml");
        if (indOfDocDecl != -1) {
            inBuff.delete(indOfDocDecl, inBuff.indexOf(">", indOfDocDecl) + 1);
        }
        final Hashtable<String, String> retVal = new Hashtable<String, String>();
        int curIndex = inBuff.indexOf("xmlns");
        while (curIndex != -1) {
            final int indexOfFirstQuote = inBuff.indexOf("\"", curIndex);
            final int indexOfSecondQuote = inBuff.indexOf("\"", indexOfFirstQuote + 1);
            final String aNamespaceDecl = inBuff.substring(curIndex, indexOfSecondQuote + 1);
            final int start = aNamespaceDecl.indexOf(":") + 1;
            final int end = aNamespaceDecl.indexOf("=");
            if (start < end) {
                final String prefix = aNamespaceDecl.substring(aNamespaceDecl.indexOf(":") + 1, aNamespaceDecl.indexOf("="));
                final int firstQuote = aNamespaceDecl.indexOf("\"");
                final String uri = aNamespaceDecl.substring(firstQuote + 1, aNamespaceDecl.indexOf("\"", firstQuote + 1));
                retVal.put(prefix, uri);
                inBuff.delete(curIndex, indexOfSecondQuote + 1);
                curIndex = inBuff.indexOf("xmlns", curIndex);
            } else {
                curIndex = inBuff.indexOf("xmlns", curIndex + 5);
            }
        }
        return retVal;
    }

    /**
     * Sets the tab selection.
     * 
     * @param index
     *            int
     */
    public void setSelected(final int index) {
        tabPan.setSelection(index);
    }

    /**
     * Updates the Get and Post bizdoc URL details.
     */
    public void updateTestFields() {
        String host = null;
        if (hostTF.getText().trim().length() > 0) {
            host = hostTF.getText().trim();
        } else {
            host = "<host>";
        }
        buildTestCB(host + bizViewURIString);
    }

    /**
     * Updates BizDoc URLs.
     * 
     * @param hostStr
     *            String
     */
    private void buildTestCB(String hostStr) {
        urlCombo.removeAll();
        int count = fileTable.getItemCount();
        for (int i = 0; i < count; i++) {
            if (((String) contentProvider.getValueAt(i, PubFileListTableModel.TYPE)).equals("xa:bizdoc")) {
                String bizName = (String) contentProvider.getValueAt(i, PubFileListTableModel.FULLNAME);
                bizName = getPackageBuilder().replaceBackslash(bizName);
                final String cbStr = hostStr + bizName;
                urlCombo.add(cbStr);
            }
        }
        final int[] selRows = fileTable.getSelectionIndices();
        int selRow = -1;
        String selBizName = null;
        for (int i = 0; i < selRows.length; i++) {
            selRow = selRows[i];
            final String type = (String) contentProvider.getValueAt(selRow, PubFileListTableModel.TYPE);
            if (type.equals("xa:bizdoc")) {
                selBizName = (String) contentProvider.getValueAt(selRow, PubFileListTableModel.FULLNAME);
                break;
            }
        }
        count = urlCombo.getItemCount();
        if (selBizName != null) {
            for (int x = 0; x < count; x++) {
                final String listName = urlCombo.getItem(x);
                if (listName.endsWith(selBizName)) {
                    urlCombo.select(x);
                    break;
                }
            }
        } else if (count > 0) {
            urlCombo.select(0);
        }
    }

    /**
     * Clears invalid file list.
     */
    public void clearInvalidFileList() {
        invalidFileList.clear();
    }

    /**
     * Clears invalid XML file list.
     */
    public void clearInvalidXMLList() {
        invalidXMLList.clear();
    }

    /**
     * Clears duplicate file list.
     */
    public void clearDuplicateFileList() {
        duplicateFileList.clear();
    }

    /**
     * Called whenever a control that is registered with this SelectionListener is selected.
     * 
     * @param e
     *            SelectionEvent
     */
    public void widgetSelected(final SelectionEvent e) {
        final String methodName = "widgetSelected";
        try {
            if (e.getSource() == publishTypesCB) {
                if (publishTypesCB.getItem(publishTypesCB.getSelectionIndex()).equalsIgnoreCase("File System")) {
                    publishPathLabel.setVisible(true);
                    browseHostJB.setVisible(true);
                } else {
                    publishPathLabel.setVisible(false);
                    browseHostJB.setVisible(false);
                }
            } else if (e.getSource() == tabPan) {
                updateTestFields();
            } else if (e.getSource() == deployButton) {
                if (getPackageBuilder().isSaved() == false) {
                    final int result = ControlFactory.showConfirmDialog(translator.getString("Latest XAR file changes not in xar file. Continue to deploy?"), translator.getString("Information"), MessageDialog.QUESTION, 0);
                    if (result == MessageDialog.CANCEL) {
                        return;
                    }
                }
                deploy(packageTF.getText());
            } else if ((e.getSource() == cancelButton) || (e.getSource() == cancelTestButton)) {
                if (getPackageBuilder().isSaved() == false) {
                    final int result = ControlFactory.showConfirmDialog(translator.getString("Latest XAR file changes not in xar file. Continue to close?"), translator.getString("Information"), MessageDialog.QUESTION, 0);
                    if (result == MessageDialog.CANCEL) {
                        return;
                    }
                }
                try {
                    getPackageBuilder().cleanTempFiles();
                } catch (final Exception exception) {
                    logger.info("Exception in cleaning temp files. " + exception.getMessage());
                }
                pkgDeployerShell.dispose();
            } else if (e.getSource() == packageButton) {
                String xarFilename = packageTF.getText();
                final int dotIndex = xarFilename.indexOf(".");
                if ((dotIndex < 0) && (xarFilename.length() > 0)) {
                    xarFilename = xarFilename + ".xar";
                    packageTF.setText(xarFilename);
                }
                if (packageFileName.equals(xarFilename) == false) {
                    final File xarFile = new File(xarFilename);
                    if (xarFile.exists()) {
                        final int result = ControlFactory.showConfirmDialog(translator.getString("XAR file " + xarFilename + " exists. Do you wish to overwrite it?"), translator.getString("Information"), MessageDialog.QUESTION, 0);
                        if (result == MessageDialog.CANCEL) {
                            return;
                        }
                    }
                }
                getPackageBuilder().createXarFile(xarFilename);
                XA_Designer_Plugin.refreshResource(new File(xarFilename));
            } else if (e.getSource() == addDocuments) {
                getPackageBuilder().processAddDocuments();
                addDocuments.forceFocus();
            } else if (e.getSource() == deleteButton) {
                deleteDocuments();
                deleteButton.forceFocus();
            } else if (e.getSource() == refreshButton) {
                pkgBuilder.refreshPackageContents();
                refreshButton.forceFocus();
            } else if (e.getSource() == browsePackageFile) {
                String filename = getPackageBuilder().getPackageFile(packageTF.getText());
                if ((filename != null) && (filename.length() > 0)) {
                    if (!filename.endsWith(".xar") && (filename.indexOf(".") < 0)) {
                        filename += ".xar";
                    }
                }
                packageTF.setText(filename);
                packageTF.setSelection(filename.length());
                packageTF.forceFocus();
                packageFileName = filename;
            } else if (e.getSource() == rolesCB) {
                if (rolesCB.getSelection()) {
                    rolesList.deselectAll();
                    rolesList.setEnabled(false);
                } else {
                    rolesList.setEnabled(true);
                }
                final Vector availRoles = getAvailableRoles();
                if ((rolesList != null) & (rolesList.getItemCount() > 0)) {
                    rolesList.removeAll();
                }
                final int rolesCount = availRoles.size();
                for (int i = 0; i < rolesCount; i++) {
                    final String roleName = (String) availRoles.get(i);
                    if (roleName.trim() != "") {
                        rolesList.add(roleName);
                    }
                }
            } else if (e.getSource() == getRoles) {
                final Vector availRoles = getAvailableRoles();
                if ((rolesList != null) & (rolesList.getItemCount() > 0)) {
                    rolesList.removeAll();
                }
                final int rolesCount = availRoles.size();
                for (int i = 0; i < rolesCount; i++) {
                    final String roleName = (String) availRoles.get(i);
                    if (roleName.trim() != "") {
                        rolesList.add(roleName);
                    }
                }
            } else if (e.getSource() == applyToAll) {
                applyRolesToSelectedRows(false);
            } else if (e.getSource() == applyToSelected) {
                applyRolesToSelectedRows(true);
            } else if (e.getSource() == invokeButton) {
                invokeOperation(reqTypeCombo.getText());
            }
        } catch (final Exception exception) {
            logger.fine("Unknown Exception caught: " + exception.getMessage(), className, methodName);
            exception.printStackTrace();
        }
    }

    /**
     * Given dummy implementation.
     * 
     * @param e
     *            SelectionEvent
     */
    public void widgetDefaultSelected(final SelectionEvent e) {
    }

    /**
     * To get ProgressMonitorPart instance.
     * 
     * @return progress monitor.
     */
    public ProgressMonitorPart getProgressMonitor() {
        return progressMonitor;
    }

    /**
     * To disable controls.
     */
    public void disableControls() {
        addDocuments.setEnabled(false);
        deleteButton.setEnabled(false);
        packageButton.setEnabled(false);
        deployButton.setEnabled(false);
    }

    /**
     * To enable controls.
     */
    public void enableControls() {
        addDocuments.setEnabled(true);
        deleteButton.setEnabled(true);
        packageButton.setEnabled(true);
        deployButton.setEnabled(true);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDoubleClick(final MouseEvent e) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDown(final MouseEvent e) {
        if (PackageBuilder.progressMonitorRunning) {
            getPackageBuilder().endProcessMonitor();
            return;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseUp(final MouseEvent e) {
    }

    /**
     * To get instance of Table.
     */
    public Table getFileTable() {
        return fileTable;
    }

    /**
     * KeyEventListener for packageTF for detecting when a user enters a 
     * value should the table be updated. Currently
     * Enter is the only event captured.
     */
    class PackageKeyListener implements KeyListener {

        /**
         * No implementation
         * 
         * @param e
         *            KeyEvent
         */
        public void keyTyped(final KeyEvent e) {
        }

        /**
         * No implementation
         * 
         * @param e
         *            KeyEvent
         */
        public void keyPressed(final KeyEvent e) {
        }

        /**
         * Check the archive file name is valid or not.
         * 
         * @param e
         *            KeyEvent
         */
        public void keyReleased(final KeyEvent e) {
            final String methodName = "keyReleased";
            if (isPackageTextFocus) {
                if ((e.keyCode == SWT.CR) || (e.keyCode == SWT.KEYPAD_CR)) {
                    final String filename = packageTF.getText();
                    if ((filename.length() > 0) && (packageFileName.equals(filename) == false)) {
                        packageFileName = filename;
                        if (getPackageBuilder().buildTableFromXarFile(filename) == false) {
                            final String msg = "Invalid XAR file: " + filename;
                            logger.severe(msg, className, methodName);
                            ControlFactory.showMessageDialog(msg, "XAR File", MessageDialog.ERROR);
                        }
                    }
                }
            }
        }
    }

    /**
	 * Performs the requested operation as per the selected http request type.
	 * @param requestType http requestType selected.
	 */
    private void invokeOperation(String requestType) {
        XAHttpClient httpConnector = null;
        RequestType key = RequestType.valueOf(requestType.toUpperCase());
        try {
            final String argumentToConnector = urlCombo.getText().replaceAll(" ", "+");
            httpConnector = new XAHttpClient(argumentToConnector);
            httpConnector.init(authPan.getUid(), authPan.getPwd());
            InputStream ipStream = null;
            switch(key) {
                case GET:
                    ipStream = httpConnector.executeGet();
                    break;
                case DELETE:
                    httpConnector.executeDelete();
                    break;
                case PUT:
                    httpConnector.executePut(xmlFileContent.getText(), charsetCombo.getText());
                    break;
                case POST:
                    ipStream = httpConnector.executePost(xmlFileContent.getText(), charsetCombo.getText());
                    break;
            }
            final SAXBuilder saxBuilder = UserPrefs.getSAXBuilder();
            if (ipStream != null) {
                Element resElement = null;
                switch(key) {
                    case GET:
                        StringBuffer out = new StringBuffer();
                        byte[] b = new byte[4096];
                        for (int n; (n = ipStream.read(b)) != -1; ) {
                            out.append(new String(b, 0, n));
                        }
                        logger.finer("Result Buffer:" + out.toString());
                        resElement = saxBuilder.build(new StringReader(out.toString())).getRootElement();
                        break;
                    case POST:
                        resElement = saxBuilder.build(ipStream).getRootElement();
                        break;
                }
                if (resElement != null) {
                    final ViewTransformerFileDlg viewTransFileDlg = new ViewTransformerFileDlg(getParent());
                    viewTransFileDlg.open(resElement, "Test Publish Result For : " + argumentToConnector);
                }
            }
        } catch (final IllegalArgumentException ilgex) {
            ControlFactory.showInfoDialog(translator.getString("Error posting message to server."), ilgex.toString());
        } catch (final Exception exception) {
            ControlFactory.showInfoDialog(translator.getString("Error posting message to server."), exception.toString());
        } finally {
            if (httpConnector != null) {
                switch(key) {
                    case GET:
                        httpConnector.closeGet();
                        break;
                    case DELETE:
                        httpConnector.closeDelete();
                        break;
                    case PUT:
                        httpConnector.closePut();
                        break;
                    case POST:
                        httpConnector.close();
                        break;
                }
            }
        }
    }

    public void modifyText(ModifyEvent e) {
        if (e.widget == reqTypeCombo) {
            String requestTypeStr = reqTypeCombo.getText();
            if (requestTypeStr.equals(XAwareConstants.BIZCOMPONENT_HTTP_POST) || requestTypeStr.equals(XAwareConstants.BIZCOMPONENT_HTTP_PUT)) {
                updateXMLGroupButtons(true);
            } else {
                updateXMLGroupButtons(false);
            }
        } else if (e.widget == xmlFileChooser.getTextField()) {
            try {
                String absInputFilePath = xmlFileChooser.getFileString();
                if (!(new File(absInputFilePath).isAbsolute())) {
                    absInputFilePath = ResourceUtils.getAbsolutePath(absInputFilePath);
                }
                if (absInputFilePath != null && new File(absInputFilePath).exists()) {
                    xmlFileContent.setText(FileUtils.getFileContentsAsString(new File(absInputFilePath)));
                }
            } catch (IOException ioe) {
                logger.severe("Exception while validation the xml content: " + ioe, className, "modifyText");
            }
        }
    }

    /**
     * Returns the dialog settings object used to share state between several Package assembly tool dialog.
     * 
     * @return the dialog settings to be used
     */
    private IDialogSettings getDialogSettings() {
        final IDialogSettings settings = XA_Designer_Plugin.getDefault().getDialogSettings();
        IDialogSettings fDialogSettings = settings.getSection(PACKAGE_DEPLOYER_SECTION);
        if (fDialogSettings == null) {
            fDialogSettings = settings.addNewSection(PACKAGE_DEPLOYER_SECTION);
        }
        return fDialogSettings;
    }

    /**
	 * Enables or disables the controls in the xmlFileGroup group according as 
	 * the parameter is true or false.
	 * @param enabled param for setEnabled(); for enabling/disabling 
	 * 						controls in xmlFileGroup.
	 */
    private void updateXMLGroupButtons(boolean enabled) {
        xmlFileChooser.setEnabled(enabled);
        xmlFileContent.setEnabled(enabled);
        charsetCombo.setEnabled(enabled);
    }

    /**
	 * Saves the state test tab controls to the dialog settings so that they can 
	 * be populated when the dialog is opened next time. 
	 */
    private void saveTestTabsettings() {
        final IDialogSettings s = getDialogSettings();
        s.put(PACKAGE_DEPLOYER_CHARSET, charsetCombo.getText());
        s.put(PACKAGE_DEPLOYER_XML_FILENAME, xmlFileChooser.getFileString());
        s.put(PACKAGE_DEPLOYER_XML_CONTENT, xmlFileContent.getText());
    }

    /**
	 * Resets the state of test tab controls from the from the dailog settings.
	 */
    private void loadTestTabSettings() {
        final IDialogSettings s = getDialogSettings();
        String previousCharset = s.get(PACKAGE_DEPLOYER_CHARSET);
        if (previousCharset == null) previousCharset = XAwareConstants.DEFAULT_ENCODING_CHARSET;
        charsetCombo.setText(previousCharset);
        xmlFileChooser.getTextField().removeModifyListener(this);
        String previousXMLFileName = s.get(PACKAGE_DEPLOYER_XML_FILENAME);
        if (previousXMLFileName != null) xmlFileChooser.getTextField().setText(previousXMLFileName);
        String previousXMLContent = s.get(PACKAGE_DEPLOYER_XML_CONTENT);
        if (previousXMLContent != null) xmlFileContent.setText(previousXMLContent);
        xmlFileChooser.getTextField().addModifyListener(this);
    }
}
