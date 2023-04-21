package com.apelon.apps.dts.editor;

import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.beans.apelapp.ApelApp;
import com.apelon.beans.apelconfig.ApelConfig;
import com.apelon.beans.apelpanel.ApelPanel;
import com.apelon.beans.apelres.ApelResourceMgr;
import com.apelon.beans.apelstatus.ApelStatusBar;
import com.apelon.beans.apelversion.ApelVersion;
import com.apelon.beans.dts.BeanContext;
import com.apelon.beans.dts.DTSAppContext;
import com.apelon.beans.dts.detailspanel.DetailsTree;
import com.apelon.beans.dts.detailspanel.DetailsTreeState;
import com.apelon.beans.dts.dnd.DtsEditable;
import com.apelon.beans.dts.editors.AssociationEditor;
import com.apelon.beans.dts.editors.PropertyEditor;
import com.apelon.beans.dts.editors.SynonymEditor;
import com.apelon.beans.dts.kb.*;
import com.apelon.beans.dts.search.SearchViewer;
import com.apelon.beans.dts.tree.TreeViewer;
import com.apelon.beans.dts.walker.WalkerViewer;
import com.apelon.common.log4j.Categories;
import com.apelon.dts.client.namespace.Namespace;
import com.apelon.dts.client.namespace.NamespaceType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * <p>Title: Apelon DTS Editor</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003 </p>
 * <p>Company: Apelon, Inc.</p>
 * @author John Szinger
 * @version 1.0
 */
public class DTSEditor_AppFrame extends JFrame implements DTSAppContext {

    private ApelConfig dtsEditorConfig = null;

    private ApelApp dtsEditorApp = null;

    private JPanel contentPane;

    private BorderLayout borderLayoutMain = new BorderLayout();

    private MainMenu mainMenu = null;

    private MainToolbar mainToolbar = null;

    private JSplitPane splitPanel = null;

    private JTabbedPane tabPaneViews = null;

    private JTabbedPane tabPaneEditors = null;

    private JPanel treeHolder = null;

    private JToolBar treeToolBar = null;

    private TreeViewer treePanel = null;

    private JPanel walkerHolder = null;

    private JToolBar walkerToolBar = null;

    private WalkerViewer walkerPanel = null;

    private JPanel searchHolder = null;

    private JToolBar searchToolBar = null;

    private SearchViewer searchPanel = null;

    private JPanel conceptHolder = null;

    private JToolBar conceptToolBar = null;

    private DetailsTree detailsPanel = null;

    private AssociationEditor associationPanel = null;

    private SynonymEditor synonymPanel = null;

    private PropertyEditor propertyPanel = null;

    private AssocTypeEditor assocTypePanel = null;

    private PropTypeEditor propTypePanel = null;

    private QualTypeEditor qualTypePanel = null;

    private KindViewer fKindPanel = null;

    private RoleTypeEditor fRoleTypePanel = null;

    private NamespaceEditor namespacePanel = null;

    private AuthorityEditor authorityPanel = null;

    private ApelStatusBar appStatusBar = null;

    public DTSEditor_AppFrame(ApelConfig dtsEditorConfig, ApelApp dtsEditorApp) {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        this.dtsEditorConfig = dtsEditorConfig;
        this.dtsEditorApp = dtsEditorApp;
        try {
            initialize();
        } catch (Exception e) {
            Categories.ui().error("Error initializing DTSEditor AppFrame");
        }
    }

    private void initialize() throws Exception {
        BeanContext.getInstance().setApelConfig(dtsEditorConfig);
        BeanContext.getInstance().setApelApp(dtsEditorApp);
        BeanContext.getInstance().setMainAppFrame(this);
        BeanContext.getInstance().setAppContext(this);
        ApelApp.fAppJFrame = this;
        dtsEditorApp.registerClosing(ApelApp.fAppJFrame);
        setIconImage(ApelResourceMgr.getImage("ontylogtermeditor.gif", this));
        this.setSize(new Dimension(1000, 700));
        this.setTitle("Apelon DTS Editor " + ApelVersion.getInstance().getVersion("dts.release"));
        this.setJMenuBar(getMainMenu());
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayoutMain);
        contentPane.add(getMainToolbar(), BorderLayout.NORTH);
        contentPane.add(getMainSplitPane(), BorderLayout.CENTER);
        contentPane.add(getStatusBar(), BorderLayout.SOUTH);
        tabPaneViews.add("Tree", getTreeHolder());
        tabPaneViews.add("Walker", getWalkerHolder());
        tabPaneViews.add("Search", getSearchHolder());
        tabPaneEditors.add("Concept / Term Details", getConceptHolder());
        loadDTSEditorModules();
    }

    private void loadDTSEditorModules() {
        DTSEditorModuleMgrImpl moduleMgr = new DTSEditorModuleMgrImpl(this);
        DTSEditorModuleLoader.getInstance().loadModules(moduleMgr, this);
    }

    public MainMenu getMainMenu() {
        if (mainMenu == null) {
            try {
                mainMenu = new MainMenu(this, dtsEditorConfig, dtsEditorApp);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return mainMenu;
    }

    public MainToolbar getMainToolbar() {
        if (mainToolbar == null) {
            try {
                mainToolbar = new MainToolbar(this);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return mainToolbar;
    }

    public JSplitPane getMainSplitPane() {
        if (splitPanel == null) {
            try {
                splitPanel = new JSplitPane();
                splitPanel.setDividerLocation(400);
                splitPanel.setMinimumSize(new Dimension(1, 1));
                splitPanel.add(getTabPaneViews(), JSplitPane.LEFT);
                splitPanel.add(getTabPaneEditors(), JSplitPane.RIGHT);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return splitPanel;
    }

    public JTabbedPane getTabPaneViews() {
        if (tabPaneViews == null) {
            try {
                tabPaneViews = new JTabbedPane();
                tabPaneViews.setPreferredSize(new Dimension(360, 600));
                tabPaneViews.setMinimumSize(new Dimension(1, 1));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return tabPaneViews;
    }

    public JTabbedPane getTabPaneEditors() {
        if (tabPaneEditors == null) {
            try {
                tabPaneEditors = new JTabbedPane();
                tabPaneEditors.setMinimumSize(new Dimension(1, 1));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return tabPaneEditors;
    }

    public ApelApp getApelApp() {
        return dtsEditorApp;
    }

    protected JPanel getTreeHolder() {
        if (treeHolder == null) {
            treeHolder = new JPanel();
            treeHolder.setLayout(new BorderLayout());
            treeHolder.add(getTreeToolBar(), BorderLayout.CENTER);
        }
        return treeHolder;
    }

    private JToolBar getTreeToolBar() {
        if (treeToolBar == null) {
            treeToolBar = new JToolBar("Concept Tree");
            treeToolBar.setPreferredSize(new Dimension(360, 600));
            treeToolBar.setMargin(new Insets(6, 6, 6, 6));
            treeToolBar.add(getTreePanel());
            treeToolBar.setFloatable(false);
        }
        return treeToolBar;
    }

    public TreeViewer getTreePanel() {
        if (treePanel == null) {
            try {
                treePanel = new TreeViewer();
                treePanel.setMinimumSize(new Dimension(1, 1));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return treePanel;
    }

    protected JPanel getWalkerHolder() {
        if (walkerHolder == null) {
            walkerHolder = new JPanel();
            walkerHolder.setLayout(new BorderLayout());
            walkerHolder.add(getWalkerToolBar(), BorderLayout.CENTER);
        }
        return walkerHolder;
    }

    private JToolBar getWalkerToolBar() {
        if (walkerToolBar == null) {
            walkerToolBar = new JToolBar("Concept Walker");
            walkerToolBar.setPreferredSize(new Dimension(360, 540));
            walkerToolBar.setMargin(new Insets(6, 6, 6, 6));
            walkerToolBar.add(getWalkerPanel());
            walkerToolBar.setFloatable(false);
        }
        return walkerToolBar;
    }

    public WalkerViewer getWalkerPanel() {
        if (walkerPanel == null) {
            try {
                walkerPanel = new WalkerViewer();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return walkerPanel;
    }

    protected JPanel getSearchHolder() {
        if (searchHolder == null) {
            searchHolder = new JPanel();
            searchHolder.setLayout(new BorderLayout());
            searchHolder.add(getSearchToolBar(), BorderLayout.CENTER);
        }
        return searchHolder;
    }

    private JToolBar getSearchToolBar() {
        if (searchToolBar == null) {
            searchToolBar = new JToolBar("Search");
            searchToolBar.setPreferredSize(new Dimension(360, 540));
            searchToolBar.setMargin(new Insets(6, 6, 6, 6));
            searchToolBar.add(getSearchPanel());
            searchToolBar.setFloatable(false);
        }
        return searchToolBar;
    }

    public SearchViewer getSearchPanel() {
        if (searchPanel == null) {
            try {
                searchPanel = new SearchViewer();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return searchPanel;
    }

    public JPanel getConceptHolder() {
        if (conceptHolder == null) {
            conceptHolder = new JPanel();
            conceptHolder.setLayout(new BorderLayout());
            conceptHolder.add(getConceptToolBar(), BorderLayout.CENTER);
        }
        return conceptHolder;
    }

    public JToolBar getConceptToolBar() {
        if (conceptToolBar == null) {
            conceptToolBar = new JToolBar("Concept / Term Details");
            conceptToolBar.setPreferredSize(new Dimension(630, 540));
            conceptToolBar.setMargin(new Insets(6, 6, 6, 6));
            conceptToolBar.add(getDetailsPanel());
            conceptToolBar.setFloatable(false);
        }
        return conceptToolBar;
    }

    /**
	 * NOTE: See DTSEditorModuleMgr and DTSEditorModuleMgrImpl for mechanism to overide detailsPanel
	 */
    public DetailsTree getDetailsPanel() {
        if (detailsPanel == null) {
            try {
                detailsPanel = new DetailsTree();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return detailsPanel;
    }

    public AssociationEditor getAssocPanel() {
        if (associationPanel == null) {
            try {
                associationPanel = new AssociationEditor();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return associationPanel;
    }

    public SynonymEditor getSynonymPanel() {
        if (synonymPanel == null) {
            try {
                synonymPanel = new SynonymEditor();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return synonymPanel;
    }

    public PropertyEditor getPropertyPanel() {
        if (propertyPanel == null) {
            try {
                propertyPanel = new PropertyEditor();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return propertyPanel;
    }

    public AssocTypeEditor getAssocTypePanel() {
        if (assocTypePanel == null) {
            try {
                assocTypePanel = new AssocTypeEditor();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return assocTypePanel;
    }

    public PropTypeEditor getPropTypePanel() {
        if (propTypePanel == null) {
            try {
                propTypePanel = new PropTypeEditor();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return propTypePanel;
    }

    public QualTypeEditor getQualTypePanel() {
        if (qualTypePanel == null) {
            try {
                qualTypePanel = new QualTypeEditor();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return qualTypePanel;
    }

    public KindViewer getKindPanel() {
        if (fKindPanel == null) {
            try {
                fKindPanel = new KindViewer();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return fKindPanel;
    }

    public RoleTypeEditor getRoleTypePanel() {
        if (fRoleTypePanel == null) {
            try {
                fRoleTypePanel = new RoleTypeEditor();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return fRoleTypePanel;
    }

    public NamespaceEditor getNamespacePanel() {
        if (namespacePanel == null) {
            try {
                namespacePanel = new NamespaceEditor();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return namespacePanel;
    }

    public AuthorityEditor getAuthorityPanel() {
        if (authorityPanel == null) {
            try {
                authorityPanel = new AuthorityEditor();
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return authorityPanel;
    }

    public ApelStatusBar getStatusBar() {
        if (appStatusBar == null) {
            try {
                appStatusBar = new ApelStatusBar();
                appStatusBar.setName("StatusBar");
                appStatusBar.setPreferredSize(new java.awt.Dimension(600, 21));
                appStatusBar.setConnectionMsgText("");
                appStatusBar.setNameMsgText("");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return appStatusBar;
    }

    public void setStatus(final String status) {
        getStatusBar().setStatusMsgText(status);
        ApelApp.repaintNow(getStatusBar());
    }

    public void setStatus2(final String status) {
        getStatusBar().setConnectionMsgText(status);
        ApelApp.repaintNow(getStatusBar());
    }

    public void setStatus3(final String status) {
        getStatusBar().setNameMsgText(status);
        ApelApp.repaintNow(getStatusBar());
    }

    public void addTabPanelLeft(ApelPanel panel, String panelName) {
        tabPaneViews.add(panelName, panel);
    }

    public void addTabPanelRight(ApelPanel panel, String panelName) {
        tabPaneEditors.add(panelName, panel);
    }

    /**
	 * Overridden so we can exit when window is closed
	 * @param e WindowEvent
	 */
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (this.mainMenu.okayToClose) {
                dtsEditorApp.endApp();
                ServerConnection sc = dtsEditorApp.getServerConn();
                if (sc != null) {
                    dtsEditorApp.closeConnection();
                }
                System.exit(0);
            }
        }
    }

    /**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
    private void handleException(java.lang.Throwable exception) {
        Categories.uiView().error("Exception in DTSEditor_AppFrame.", exception);
    }

    private boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        if (connected) {
            getMainMenu().setUIConnected();
            getMainToolbar().setUIConnected();
            String status2String = dtsEditorApp.getServerConn().toString();
            if (dtsEditorConfig.getConnectionType() == 1 && dtsEditorConfig.getAuthBoolean()) {
                status2String += " User: " + dtsEditorConfig.getProperty(ApelConfig.CONNECTED_USER);
            }
            setStatus("Connected.");
            setStatus2(status2String);
        } else {
            getMainMenu().setUIDisconnected();
            getMainToolbar().setUIDisconnected();
            setStatus("Not connected.");
            setStatus2("");
        }
    }

    public void setStatus(String msg, String toolTip) {
        setStatus(msg != null ? msg : "");
        getStatusBar().setToolTipText(toolTip != null ? toolTip : "");
    }

    Namespace localNamespace = null;

    public Namespace getLocalNamespace() {
        return localNamespace;
    }

    public void setLocalNamespace(Namespace ns) {
        localNamespace = ns;
        if (localNamespace != null) {
            setStatus3(ns.getName());
            getStatusBar().setNameMsgToolTip("Current Local Namespace: " + ns.getName());
            if (ns.getNamespaceType().equals(NamespaceType.ONTYLOG_EXTENSION)) {
                getMainToolbar().getClassifyBtn().setEnabled(true);
                getMainMenu().getClassifyMenuItem().setEnabled(true);
                getMainMenu().getClasErrorsMenuItem().setEnabled(true);
            } else {
                getMainToolbar().getClassifyBtn().setEnabled(false);
                getMainMenu().getClassifyMenuItem().setEnabled(false);
                getMainMenu().getClasErrorsMenuItem().setEnabled(false);
            }
        } else {
            if (isConnected()) {
                setStatus3("Undefined Current Local Namespace");
                getStatusBar().setNameMsgToolTip("Current Local Namespace is undefined.");
            } else {
                setStatus3("");
                getStatusBar().setNameMsgToolTip("");
            }
        }
    }

    public void editDtsEditable(DtsEditable editable) {
        getDetailsPanel().setDtsEditable(editable, true);
    }

    public boolean isEditingDefinedView() {
        return getDetailsPanel().getDetailsTreeState().equals(DetailsTreeState.DEFINED_VIEW);
    }
}
