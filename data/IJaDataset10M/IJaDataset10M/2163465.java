package pedro.desktopDeployment;

import pedro.soa.alerts.AlertsBundle;
import pedro.mda.config.*;
import pedro.io.RecordModelViewCoordinator;
import pedro.metaData.DocumentMetaData;
import pedro.mda.model.*;
import pedro.soa.ontology.provenance.OntologyTermProvenanceManager;
import pedro.soa.ontology.sources.OntologyContext;
import pedro.soa.id.IDGeneratorService;
import pedro.soa.plugins.PluginManager;
import pedro.system.*;
import pedro.util.SystemLog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class PedroDialog extends JDialog implements ActionListener {

    private PedroUIFactory pedroUIFactory;

    private ArrayList alertsBundles;

    private PedroMenuBar menuBar;

    /**
     * the tree that appears in the left portion of the dialog
     */
    private NavigationTree navigationTree;

    private NavigationTreePanel navigationTreePanel;

    /**
     * the record form area that appears in the right portion of the dialog
     */
    private RecordView recordView;

    /**
     * experimental class for managing provenance
     */
    private OntologyTermProvenanceManager ontologyTermProvenanceManager;

    /**
     * flag that indicates whether the dialog has been instantiated without
     * a file
     */
    private boolean isNewFile;

    /**
     * the file viewed in the dialog
     */
    private File file;

    private DialogCloser closer;

    private DocumentMetaData documentMetaData;

    private IDGeneratorService idGeneratorService;

    /**
     * used to hold all the variables for a session driven off
     * a model
     */
    private PedroApplicationContext pedroApplicationContext;

    private PedroDocumentContext pedroDocumentContext;

    private PedroFormContext pedroFormContext;

    private StatusBar statusBar;

    private boolean isComponentMode;

    public PedroDialog(PedroApplicationContext pedroApplicationContext) {
        init(pedroApplicationContext);
    }

    public PedroDialog(PedroApplicationContext pedroApplicationContext, JDialog parentDialog) {
        super(parentDialog);
        init(pedroApplicationContext);
    }

    /**
	* specifies a listener that monitors for when someone clicks the 
	* "X" in the top right icons in the dialog
	*/
    public void setWindowListener(WindowListener windowListener) {
        removeWindowListener(closer);
        if (windowListener != null) {
            addWindowListener(windowListener);
        }
    }

    private void init(PedroApplicationContext pedroApplicationContext) {
        this.pedroApplicationContext = pedroApplicationContext;
        pedroDocumentContext = pedroApplicationContext.createDocumentContext();
        pedroFormContext = pedroDocumentContext.getFormContext();
        pedroDocumentContext.setProperty(PedroDocumentContext.PEDRO_DIALOG, this);
        this.pedroUIFactory = (PedroUIFactory) pedroFormContext.getApplicationProperty(PedroApplicationContext.USER_INTERFACE_FACTORY);
        initialiseTitle();
        closer = new DialogCloser(this);
        addWindowListener(closer);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.setInitialDelay(2000);
        toolTipManager.setReshowDelay(2000);
        navigationTree = new NavigationTree(pedroUIFactory);
        pedroDocumentContext.setProperty(PedroDocumentContext.NAVIGATION_VIEW, navigationTree);
        recordView = new RecordView(pedroFormContext);
        DocumentMetaData documentMetaData = new DocumentMetaData();
        setDocumentMetaData(documentMetaData);
        navigationTree.setRecordView(recordView);
        RecordModelFactory recordFactory = (RecordModelFactory) pedroApplicationContext.getProperty(PedroApplicationContext.RECORD_MODEL_FACTORY);
        RecordModel topLevelRecordModel = recordFactory.createTopLevelRecordModel();
        if (topLevelRecordModel != null) {
            topLevelRecordModel.computeDisplayName();
            topLevelRecordModel.addChangeListener(navigationTree);
            navigationTree.setRoot(topLevelRecordModel);
            navigationTree.setActiveNode(topLevelRecordModel);
            recordView.setTopLevelRecord(topLevelRecordModel);
            statusBar = new StatusBar(pedroUIFactory);
            pedroDocumentContext.setProperty(PedroDocumentContext.STATUS_BAR, statusBar);
            ArrayList plugins = (ArrayList) pedroApplicationContext.getProperty(PedroApplicationContext.PLUGINS);
            PluginManager pluginManager = new PluginManager(pedroFormContext, plugins, statusBar, recordFactory);
            recordView.setPluginSelectorComponent(pluginManager);
            recordView.setModel(topLevelRecordModel);
            createBody();
            menuBar = new PedroMenuBar(pedroUIFactory, this);
            setJMenuBar(menuBar);
            if (PedroResources.isSchemaAmbiguous()) {
                menuBar.allowXMLImport(false);
            } else {
                menuBar.allowXMLImport(true);
            }
            isNewFile = false;
            setSize(900, 600);
        } else {
            System.exit(-1);
        }
    }

    private void createBody() {
        JPanel panel = pedroUIFactory.createPanel(new BorderLayout());
        JPanel rightPanel = pedroUIFactory.createPanel(new BorderLayout());
        rightPanel.add(recordView, BorderLayout.NORTH);
        Dimension recordViewSize = rightPanel.getPreferredSize();
        int minimumHeight = (int) recordViewSize.getHeight();
        minimumHeight = 600;
        rightPanel.setMinimumSize(new Dimension(600, minimumHeight));
        JScrollPane rightScrollPane = pedroUIFactory.createScrollPane(rightPanel);
        recordView.setViewPort(rightScrollPane.getViewport());
        rightScrollPane.setMinimumSize(new Dimension(600, minimumHeight));
        navigationTreePanel = new NavigationTreePanel(pedroFormContext, navigationTree);
        navigationTreePanel.setMinimumSize(new Dimension(200, minimumHeight));
        JSplitPane splitPane = pedroUIFactory.createSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationTreePanel, rightScrollPane);
        splitPane.setDividerLocation(0.5);
        panel.add(splitPane, BorderLayout.CENTER);
        panel.add(statusBar.getPanel(), BorderLayout.SOUTH);
        getContentPane().add(panel);
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    public NavigationTree getNavigationTree() {
        return navigationTree;
    }

    /**
	* @return a collection of alert bundles which can be used
	* to enhance Pedro's validation facilities
	*/
    public ArrayList getAlertBundles() {
        return alertsBundles;
    }

    /**
	* @return the main form for displaying the current record
	*/
    public RecordView getRecordView() {
        return recordView;
    }

    public DocumentMetaData getDocumentMetaData() {
        return documentMetaData;
    }

    /**
	* @return whether the file is a new blank file or an existing
	* one that is being modified
	*/
    public boolean isNewFile() {
        return isNewFile;
    }

    /**
	* @return returns the file being used by the dialog
	*/
    public File getFile() {
        return file;
    }

    public IDGeneratorService getIDValueGenerator() {
        return idGeneratorService;
    }

    /**
	* @return the navigation tree that appears in the left part of the
	* screen
	*/
    public NavigationTree getTree() {
        return navigationTree;
    }

    public RecordModel getData() {
        NavigationTreeNode rootNode = navigationTree.getRoot();
        if (rootNode == null) {
            return null;
        }
        RecordModel rootModel = rootNode.getRecordModel();
        return rootModel;
    }

    /**
	* @return a HashMap that contains all the global variables
	* relating to managing the dialog
	*/
    public PedroFormContext getFormContext() {
        return pedroDocumentContext.getFormContext();
    }

    public void openFile(URL url) {
        menuBar.openFile(url);
    }

    public void openFile(File file) {
        menuBar.openFile(file);
    }

    public boolean isComponentMode() {
        return isComponentMode;
    }

    public void setAlertsBundles(ArrayList alertsBundles) {
        this.alertsBundles = alertsBundles;
    }

    public void setData(String dialogTitle, RecordModel rootModel) {
        if (dialogTitle.equals(PedroResources.EMPTY_STRING) == false) {
            setTitle(dialogTitle);
        }
        RecordModelViewCoordinator reader = new RecordModelViewCoordinator();
        reader.read(rootModel, navigationTree);
        NavigationTreeNode rootNode = reader.getRoot();
        setRoot(rootNode);
        setNewFile(false);
    }

    /**
     * the tree has two mechanisms for detecting when changes
     * were made.  One of them is checking saved and current data field states
     * and the other is to check if nodes were added or deleted.  This method
     * helps reset save changes in the tree.
     */
    public void acknowledgeChangesMade(boolean resetDisplay) {
        navigationTree.acknowledgeChangesMade();
        if (resetDisplay == true) {
            navigationTree.resetDisplay();
        }
        repaint();
    }

    /**
     * closes the dialog
     */
    public boolean close() {
        return menuBar.close();
    }

    /**
     * saves changes.  The method specifies whether the user is prompted
     * to save changes or if changes are automatically saved without the user's
     * consent.  For example, in a "Save As..." operation, you would want
     * to not prompt the user to save changes.  In a Close or Exit operation
     * you would want to prompt the user.
     *
     * @param promptForSaveChanges - true if the user gets prompted to save
     *                             any detected changes or false if the changes get saved without prompting.
     */
    public int saveChanges(boolean promptForSaveChanges) throws PedroException {
        if (promptForSaveChanges) {
            ArrayList recordModels = navigationTree.getRecordModels();
            RecordModelUtility recordModelUtility = new RecordModelUtility();
            if (recordModelUtility.saveChanges(recordModels) || recordView.isDirty() || navigationTree.nodesAddedOrDeleted()) {
                String warningMessage = PedroResources.getMessage("pedro.fileMenu.saveFileChanges", getTitle());
                String warningMessageTitle = PedroResources.getMessage("pedro.fileMenu.saveChangesDialogTitle");
                int answer = JOptionPane.showConfirmDialog(null, warningMessage, warningMessageTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null);
                if (answer == JOptionPane.YES_OPTION) {
                    if (navigationTree.nodesAddedOrDeleted() == true) {
                        navigationTree.setNodesAddedOrDeleted(false);
                    }
                    recordView.keepValues();
                }
                return answer;
            }
            return JOptionPane.NO_OPTION;
        }
        recordView.keepValues();
        return JOptionPane.YES_OPTION;
    }

    /**
     * sets the file being viewed in the dialog
     *
     * @param file name of the file being viewed
     */
    public void setFile(File file) {
        this.file = file;
        initialiseTitle();
    }

    private void initialiseTitle() {
        if (file == null) {
            String title = PedroResources.getMessage("pedro.pedroDialog.title");
            setTitle(title);
        }
        if (file != null) {
            PedroConfigurationReader configurationReader = (PedroConfigurationReader) pedroApplicationContext.getProperty(PedroApplicationContext.CONFIGURATION_READER);
            StringBuffer titleBuffer = new StringBuffer();
            titleBuffer.append(file.getName());
            titleBuffer.append("-");
            titleBuffer.append(configurationReader.getDialogTitle());
            setTitle(titleBuffer.toString());
        }
    }

    /**
     * establishes whether file is a dialog that has been created through a
     * "New" operation and not yet populated with a file.
     *
     * @param isNewFile indicates whether the dialog has been instantiated without a file.
     */
    public void setNewFile(boolean isNewFile) {
        this.isNewFile = isNewFile;
    }

    /**
     * sets the root of the tree
     *
     * @param root the new root of the tree
     */
    public void setRoot(NavigationTreeNode root) {
        navigationTree.setRoot(root);
        navigationTree.setActiveNode(root.getRecordModel());
        recordView.setModel(root.getRecordModel());
        repaint();
    }

    /**
     * a method that updates the contents of the PedroDialog's "Windows" menu
     * to reflect new files opened, or existing files deleted or renamed
     */
    public void updateWindowList(ArrayList activeWindows) {
        menuBar.updateWindowList(activeWindows);
    }

    public void setDocumentMetaData(DocumentMetaData documentMetaData) {
        this.documentMetaData = documentMetaData;
        pedroFormContext.setDocumentProperty(PedroDocumentContext.DOCUMENT_META_DATA, documentMetaData);
        OntologyTermProvenanceManager ontologyTermProvenanceManager = documentMetaData.getOntologyTermProvenanceManager();
        OntologyContext ontologyContext = new OntologyContext(ontologyTermProvenanceManager);
        recordView.setOntologyContext(ontologyContext);
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof JCheckBoxMenuItem) {
            JCheckBoxMenuItem enableHelp = (JCheckBoxMenuItem) source;
            boolean enableContextHelp = enableHelp.getState();
            navigationTreePanel.enableContextHelp(enableContextHelp);
            recordView.enableContextHelp(enableContextHelp);
            menuBar.enableContextHelp(enableContextHelp);
        }
    }

    class DialogCloser extends WindowAdapter {

        private PedroDialog dialog;

        public DialogCloser(PedroDialog dialog) {
            this.dialog = dialog;
        }

        public void windowClosing(WindowEvent event) {
            if (dialog.close() == true) {
                Window window = (Window) event.getSource();
                window.dispose();
            }
        }
    }
}
