package com.apelon.apps.dts.editor;

import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.beans.apelapp.ApelApp;
import com.apelon.beans.apelconfig.ApelConfig;
import com.apelon.beans.apeldlg.ApelDlg;
import com.apelon.beans.apelhelp.ApelHelpPanel;
import com.apelon.beans.apelhelp.ApelHelp;
import com.apelon.beans.apelpanel.ApelPanel;
import com.apelon.beans.apelversion.ApelVersion;
import com.apelon.beans.dts.BeanContext;
import com.apelon.beans.dts.classify.ClassifyErrorsPanel;
import com.apelon.beans.dts.classify.ClassifyMgr;
import com.apelon.beans.dts.connect.ConnectMgr;
import com.apelon.beans.dts.connect.ConnectOptionPanel;
import com.apelon.beans.dts.detailspanel.DetailsTree;
import com.apelon.beans.dts.editors.AssociationEditor;
import com.apelon.beans.dts.editors.PropertyEditor;
import com.apelon.beans.dts.editors.QualifierEditor;
import com.apelon.beans.dts.editors.SynonymEditor;
import com.apelon.beans.dts.kb.*;
import com.apelon.beans.dts.panels.SelectCodeAndIdGeneratorPanel;
import com.apelon.beans.dts.panels.SetCurrentLocalNamespacePanel;
import com.apelon.beans.dts.querymanager.NamespaceManager;
import com.apelon.beans.dts.search.SearchViewer;
import com.apelon.beans.dts.subset.SubsetListView;
import com.apelon.beans.dts.subset.builder.SETreeEditor;
import com.apelon.beans.dts.tree.TreeViewer;
import com.apelon.beans.dts.walker.WalkerViewer;
import com.apelon.common.log4j.Categories;
import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.namespace.Namespace;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

public class MainMenu extends JMenuBar {

    private DTSEditor_AppFrame fAppFrame = null;

    private ApelApp fApelApp = null;

    private ApelConfig fApelConfig = null;

    private ConnectMgr fConnectMgr = ConnectMgr.getInstance();

    private ConnectOptionPanel connOption = null;

    private SetCurrentLocalNamespacePanel setLocNmspPanel = null;

    private SelectCodeAndIdGeneratorPanel selCodeAndIdGenPanel = null;

    private static final String TITLE_ASSOCIATION_TYPE = "Association Type Editor";

    private static final String TITLE_PROPERTY_TYPE = "Property Type Editor";

    private static final String TITLE_QUALIFIER_TYPE = "Qualifier Type Editor";

    private static final String TITLE_KIND = "Kind Viewer";

    private static final String TITLE_ROLE_TYPE = "Role Type Editor";

    private static final String TITLE_NAMESPACE_EDITOR = "Namespace Editor";

    private JMenu menuFile = null;

    private JMenuItem mItemFileConnect = null;

    private JMenuItem mItemFileDisconnect = null;

    private JMenuItem mItemFileConnOpts = null;

    private JMenuItem mItemFileExit = null;

    private JMenu menuEdit = null;

    private JMenuItem mItemEditUndo = null;

    private JMenuItem mItemEditRedo = null;

    private JMenuItem mItemEditCut = null;

    private JMenuItem mItemEditCopy = null;

    private JMenuItem mItemEditPaste = null;

    private JMenuItem mItemEditDel = null;

    private JMenuItem mItemEditSelAll = null;

    private JMenu menuView = null;

    private JCheckBoxMenuItem mItemViewStatus = null;

    private JMenu menuTools = null;

    private JMenuItem mItemViewDTSTree = null;

    private JMenuItem mItemViewWalker = null;

    private JMenuItem mItemViewSearch = null;

    private JMenuItem mItemViewDetails = null;

    private JMenuItem mItemViewAssocEd = null;

    private JMenuItem mItemViewSynonEd = null;

    private JMenuItem mItemViewPropEd = null;

    private JMenuItem mItemViewQualEd = null;

    private JMenuItem mItemViewAssocType = null;

    private JMenuItem mItemViewPropType = null;

    private JMenuItem mItemViewQualType = null;

    private JMenuItem fKindMenuItem = null;

    private JMenuItem fRoleTypeMenuItem = null;

    private JMenuItem mItemViewNmspEd = null;

    private JMenuItem mItemViewClassify = null;

    private JMenuItem mItemViewClasErrors = null;

    private JMenuItem mItemViewSubsetList = null;

    private JMenuItem mItemViewSubsetEditor = null;

    private JMenu menuOptions = null;

    private JMenuItem mItemCodeAndId = null;

    private JMenuItem mItemLocNmsp = null;

    private JMenu menuHelp = null;

    private JMenuItem mItemHelpTopics = null;

    private JMenuItem mItemMatchOverview = null;

    private JMenuItem mItemHelpAbout = null;

    private static final String HELPTOPICFILE = "../docs/help/dtseditorhelptopics.htm";

    private static final String MATCHOVERVIEWFILE = "../docs/help/dtsmatching.htm";

    public boolean okayToClose;

    public MainMenu(DTSEditor_AppFrame parent, ApelConfig dtsEditorConfig, ApelApp dtsEditorApp) {
        try {
            this.fAppFrame = parent;
            this.fApelConfig = dtsEditorConfig;
            this.fApelApp = dtsEditorApp;
            initialize();
        } catch (Exception ex) {
            Categories.uiController().error("Exception in MainMenu Constructor", ex);
        }
    }

    void initialize() throws Exception {
        this.add(getFileMenu());
        this.add(getViewMenu());
        this.add(getToolsMenu());
        this.add(getOptionsMenu());
        this.add(getMyHelpMenu());
    }

    public JMenu getFileMenu() {
        if (menuFile == null) {
            menuFile = new JMenu();
            menuFile.setName("FileMenu");
            menuFile.setText("File");
            menuFile.setMnemonic('f');
            menuFile.setActionCommand("FileMenu");
            menuFile.add(getConnectMenuItem());
            menuFile.add(getDisconnectMenuItem());
            menuFile.add(getConnOpsMenuItem());
            menuFile.addSeparator();
            menuFile.add(getExitMenuItem());
        }
        return menuFile;
    }

    private JMenuItem getConnectMenuItem() {
        if (mItemFileConnect == null) {
            mItemFileConnect = new JMenuItem();
            mItemFileConnect.setName("connectMenuItem");
            mItemFileConnect.setText("Connect");
            mItemFileConnect.setToolTipText("Connect to DTS database");
            mItemFileConnect.setMnemonic('c');
            mItemFileConnect.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, Event.CTRL_MASK));
            mItemFileConnect.setActionCommand("Connect");
            mItemFileConnect.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_Connect(e);
                }
            });
        }
        return mItemFileConnect;
    }

    private JMenuItem getDisconnectMenuItem() {
        if (mItemFileDisconnect == null) {
            mItemFileDisconnect = new JMenuItem();
            mItemFileDisconnect.setName("disconnectMenuItem");
            mItemFileDisconnect.setText("Disconnect");
            mItemFileDisconnect.setToolTipText("Disconnect from the database");
            mItemFileDisconnect.setMnemonic('d');
            mItemFileDisconnect.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, Event.CTRL_MASK));
            mItemFileDisconnect.setActionCommand("Disconnect");
            mItemFileDisconnect.setEnabled(false);
            mItemFileDisconnect.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_Disconnect(e);
                }
            });
        }
        return mItemFileDisconnect;
    }

    private JMenuItem getConnOpsMenuItem() {
        if (mItemFileConnOpts == null) {
            mItemFileConnOpts = new JMenuItem();
            mItemFileConnOpts.setName("connOpsMenuItem");
            mItemFileConnOpts.setText("Connect Options ...");
            mItemFileConnOpts.setToolTipText("Configure COnnection to DTS Database");
            mItemFileConnOpts.setMnemonic('o');
            mItemFileConnOpts.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, (Event.CTRL_MASK + Event.SHIFT_MASK)));
            mItemFileConnOpts.setActionCommand("ConnectOptions");
            mItemFileConnOpts.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_ConnectOptions(e);
                }
            });
        }
        return mItemFileConnOpts;
    }

    private JMenuItem getExitMenuItem() {
        if (mItemFileExit == null) {
            mItemFileExit = new JMenuItem();
            mItemFileExit.setName("exitMenuItem");
            mItemFileExit.setText("Exit");
            mItemFileExit.setMnemonic('x');
            mItemFileExit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, Event.ALT_MASK));
            mItemFileExit.setActionCommand("Exit");
            mItemFileExit.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_FileExit(e);
                }
            });
        }
        return mItemFileExit;
    }

    public JMenu getEditMenu() {
        if (menuEdit == null) {
            menuEdit = new JMenu();
            menuEdit.setName("EditMenu");
            menuEdit.setText("Edit");
            menuEdit.setMnemonic('e');
            menuEdit.setActionCommand("EditMenu");
            menuEdit.add(getCutMenuItem());
            menuEdit.add(getCopyMenuItem());
            menuEdit.add(getPasteMenuItem());
            menuEdit.add(getDelMenuItem());
        }
        return menuEdit;
    }

    private JMenuItem getUndoMenuItem() {
        if (mItemEditUndo == null) {
            mItemEditUndo = new JMenuItem();
            mItemEditUndo.setName("undoMenuItem");
            mItemEditUndo.setText("Undo");
            mItemEditUndo.setMnemonic('u');
            mItemEditUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, Event.CTRL_MASK));
            mItemEditUndo.setActionCommand("Undo");
            mItemEditUndo.setEnabled(false);
        }
        return mItemEditUndo;
    }

    private JMenuItem getRedoMenuItem() {
        if (mItemEditRedo == null) {
            mItemEditRedo = new JMenuItem();
            mItemEditRedo.setName("redoMenuItem");
            mItemEditRedo.setText("Redo");
            mItemEditRedo.setMnemonic('r');
            mItemEditRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, (Event.CTRL_MASK + Event.SHIFT_MASK)));
            mItemEditRedo.setActionCommand("Redo");
            mItemEditRedo.setEnabled(false);
        }
        return mItemEditRedo;
    }

    private JMenuItem getCutMenuItem() {
        if (mItemEditCut == null) {
            mItemEditCut = new JMenuItem();
            mItemEditCut.setName("cutMenuItem");
            mItemEditCut.setText("Cut");
            mItemEditCut.setMnemonic('t');
            mItemEditCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, Event.CTRL_MASK));
            mItemEditCut.setActionCommand("Cut");
            mItemEditCut.setEnabled(false);
        }
        return mItemEditCut;
    }

    private JMenuItem getCopyMenuItem() {
        if (mItemEditCopy == null) {
            mItemEditCopy = new JMenuItem();
            mItemEditCopy.setName("copyMenuItem");
            mItemEditCopy.setText("Copy");
            mItemEditCopy.setMnemonic('c');
            mItemEditCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, Event.CTRL_MASK));
            mItemEditCopy.setActionCommand("Copy");
            mItemEditCopy.setEnabled(false);
        }
        return mItemEditCopy;
    }

    private JMenuItem getPasteMenuItem() {
        if (mItemEditPaste == null) {
            mItemEditPaste = new JMenuItem();
            mItemEditPaste.setName("pasteMenuItem");
            mItemEditPaste.setText("Paste");
            mItemEditPaste.setMnemonic('p');
            mItemEditPaste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, Event.CTRL_MASK));
            mItemEditPaste.setActionCommand("Paste");
            mItemEditPaste.setEnabled(false);
        }
        return mItemEditPaste;
    }

    private JMenuItem getDelMenuItem() {
        if (mItemEditDel == null) {
            mItemEditDel = new JMenuItem();
            mItemEditDel.setName("deleteMenuItem");
            mItemEditDel.setText("Delete");
            mItemEditDel.setMnemonic('d');
            mItemEditDel.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
            mItemEditDel.setActionCommand("Delete");
            mItemEditDel.setEnabled(false);
        }
        return mItemEditDel;
    }

    private JMenuItem getSelAllMenuItem() {
        if (mItemEditSelAll == null) {
            mItemEditSelAll = new JMenuItem();
            mItemEditSelAll.setName("selAllMenuItem");
            mItemEditSelAll.setText("Select All");
            mItemEditSelAll.setMnemonic('s');
            mItemEditSelAll.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, Event.CTRL_MASK));
            mItemEditSelAll.setActionCommand("SelectAll");
            mItemEditSelAll.setEnabled(false);
        }
        return mItemEditSelAll;
    }

    public JMenu getViewMenu() {
        if (menuView == null) {
            menuView = new JMenu();
            menuView.setName("View Menu");
            menuView.setText("View");
            menuView.setMnemonic('v');
            menuView.setActionCommand("ViewMenu");
            menuView.add(getStatusMenuItem());
        }
        return menuView;
    }

    public JMenu getToolsMenu() {
        if (menuTools == null) {
            menuTools = new JMenu();
            menuTools.setName("Tools Menu");
            menuTools.setText("Tools");
            menuTools.setMnemonic('t');
            menuTools.setActionCommand("ToolsMenu");
            menuTools.add(getDTSTreeMenuItem());
            menuTools.add(getWalkerMenuItem());
            menuTools.add(getSearchMenuItem());
            menuTools.addSeparator();
            menuTools.add(getDetailsMenuItem());
            menuTools.add(getAssocEdMenuItem());
            menuTools.add(getPropEdMenuItem());
            menuTools.add(getSynonEdMenuItem());
            menuTools.addSeparator();
            menuTools.add(getAssocTypeMenuItem());
            menuTools.add(getPropTypeMenuItem());
            menuTools.add(getQualTypeMenuItem());
            menuTools.add(getKindMenuItem());
            menuTools.add(getRoleTypeMenuItem());
            menuTools.addSeparator();
            menuTools.add(getNamespaceMenuItem());
            menuTools.addSeparator();
            menuTools.add(getClassifyMenuItem());
            menuTools.add(getClasErrorsMenuItem());
            menuTools.addSeparator();
            menuTools.add(getSubsetListMenuItem());
            menuTools.add(getSubsetEditorMenuItem());
        }
        return menuTools;
    }

    private JMenuItem getDTSTreeMenuItem() {
        if (mItemViewDTSTree == null) {
            mItemViewDTSTree = new JMenuItem("New Concept Tree");
            mItemViewDTSTree.setMnemonic('T');
            mItemViewDTSTree.setEnabled(false);
            mItemViewDTSTree.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_NewTree(e);
                }
            });
        }
        return mItemViewDTSTree;
    }

    private JMenuItem getWalkerMenuItem() {
        if (mItemViewWalker == null) {
            mItemViewWalker = new JMenuItem("New Concept Walker");
            mItemViewWalker.setMnemonic('W');
            mItemViewWalker.setEnabled(false);
            mItemViewWalker.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_NewWalker(e);
                }
            });
        }
        return mItemViewWalker;
    }

    private JMenuItem getSearchMenuItem() {
        if (mItemViewSearch == null) {
            mItemViewSearch = new JMenuItem("New Search");
            mItemViewSearch.setMnemonic('S');
            mItemViewSearch.setEnabled(false);
            mItemViewSearch.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_NewSearch(e);
                }
            });
        }
        return mItemViewSearch;
    }

    private JMenuItem getDetailsMenuItem() {
        if (mItemViewDetails == null) {
            mItemViewDetails = new JMenuItem();
            mItemViewDetails.setEnabled(false);
            mItemViewDetails.setText("Concept / Term Details");
            mItemViewDetails.setToolTipText("Open New Concept / Term Details Panel");
            mItemViewDetails.setMnemonic('d');
            mItemViewDetails.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_NewDetails(e);
                }
            });
        }
        return mItemViewDetails;
    }

    private JMenuItem getAssocEdMenuItem() {
        if (mItemViewAssocEd == null) {
            mItemViewAssocEd = new JMenuItem();
            mItemViewAssocEd.setEnabled(false);
            mItemViewAssocEd.setName("assocEdMenuItem");
            mItemViewAssocEd.setText("Associations");
            mItemViewAssocEd.setToolTipText("Toggle Associations Editor Panel");
            mItemViewAssocEd.setMnemonic('a');
            mItemViewAssocEd.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_NewAssocEd(e);
                }
            });
        }
        return mItemViewAssocEd;
    }

    private JMenuItem getSynonEdMenuItem() {
        if (mItemViewSynonEd == null) {
            mItemViewSynonEd = new JMenuItem();
            mItemViewSynonEd.setEnabled(false);
            mItemViewSynonEd.setName("syonymEdMenuItem");
            mItemViewSynonEd.setText("Synonyms");
            mItemViewSynonEd.setToolTipText("Toggle Synonyms Editor Panel");
            mItemViewSynonEd.setMnemonic('y');
            mItemViewSynonEd.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_NewSynonEd(e);
                }
            });
        }
        return mItemViewSynonEd;
    }

    private JMenuItem getPropEdMenuItem() {
        if (mItemViewPropEd == null) {
            mItemViewPropEd = new JMenuItem();
            mItemViewPropEd.setEnabled(false);
            mItemViewPropEd.setName("propEdMenuItem");
            mItemViewPropEd.setText("Properties");
            mItemViewPropEd.setToolTipText("Toggle Properties Editor Panel");
            mItemViewPropEd.setMnemonic('p');
            mItemViewPropEd.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_NewPropEd(e);
                }
            });
        }
        return mItemViewPropEd;
    }

    private JMenuItem getQualEdMenuItem() {
        if (mItemViewQualEd == null) {
            mItemViewQualEd = new JMenuItem();
            mItemViewQualEd.setEnabled(false);
            mItemViewQualEd.setName("qualEdMenuItem");
            mItemViewQualEd.setText("Qualifiers");
            mItemViewQualEd.setToolTipText("Toggle Qualifiers Editor Panel");
            mItemViewQualEd.setMnemonic('q');
            mItemViewQualEd.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_NewQualEd(e);
                }
            });
        }
        return mItemViewQualEd;
    }

    private JMenuItem getAssocTypeMenuItem() {
        if (mItemViewAssocType == null) {
            mItemViewAssocType = new JMenuItem();
            mItemViewAssocType.setName("assocTypeMenuItem");
            mItemViewAssocType.setText("Association Types");
            mItemViewAssocType.setToolTipText("Toggle Assocaition Types Editor Panel");
            mItemViewAssocType.setEnabled(false);
            mItemViewAssocType.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_AssocTypeMenuItem(e);
                }
            });
        }
        return mItemViewAssocType;
    }

    private JMenuItem getPropTypeMenuItem() {
        if (mItemViewPropType == null) {
            mItemViewPropType = new JMenuItem();
            mItemViewPropType.setSelected(false);
            mItemViewPropType.setName("propTypeMenuItem");
            mItemViewPropType.setText("Property Types");
            mItemViewPropType.setToolTipText("Toggle Property Types Editor Panel");
            mItemViewPropType.setEnabled(false);
            mItemViewPropType.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_PropTypeMenuItem(e);
                }
            });
        }
        return mItemViewPropType;
    }

    private JMenuItem getQualTypeMenuItem() {
        if (mItemViewQualType == null) {
            mItemViewQualType = new JMenuItem();
            mItemViewQualType.setSelected(false);
            mItemViewQualType.setName("qualTypeMenuItem");
            mItemViewQualType.setText("Qualifier Types");
            mItemViewQualType.setToolTipText("Toggle Qualifier Types Editor Panel");
            mItemViewQualType.setEnabled(false);
            mItemViewQualType.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_QualTypeMenuItem(e);
                }
            });
        }
        return mItemViewQualType;
    }

    private JMenuItem getKindMenuItem() {
        if (fKindMenuItem == null) {
            fKindMenuItem = new JMenuItem();
            fKindMenuItem.setSelected(false);
            fKindMenuItem.setName("kindMenuItem");
            fKindMenuItem.setText("Kinds");
            fKindMenuItem.setToolTipText("View Kinds");
            fKindMenuItem.setEnabled(false);
            fKindMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_KindMenuItem(e);
                }
            });
        }
        return fKindMenuItem;
    }

    private JMenuItem getRoleTypeMenuItem() {
        if (fRoleTypeMenuItem == null) {
            fRoleTypeMenuItem = new JMenuItem();
            fRoleTypeMenuItem.setSelected(false);
            fRoleTypeMenuItem.setName("roleTypeMenuItem");
            fRoleTypeMenuItem.setText("Role Types");
            fRoleTypeMenuItem.setToolTipText("Toggle Role Type Editor Panel");
            fRoleTypeMenuItem.setEnabled(false);
            fRoleTypeMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_RoleTypeMenuItem(e);
                }
            });
        }
        return fRoleTypeMenuItem;
    }

    private JMenuItem getNamespaceMenuItem() {
        if (mItemViewNmspEd == null) {
            mItemViewNmspEd = new JMenuItem();
            mItemViewNmspEd.setSelected(false);
            mItemViewNmspEd.setName("namespaceMenuItem");
            mItemViewNmspEd.setText("Namespace");
            mItemViewNmspEd.setToolTipText("Toggle Namespace Editor Panel");
            mItemViewNmspEd.setMnemonic('n');
            mItemViewNmspEd.setEnabled(false);
            mItemViewNmspEd.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_NameSpaceMenuItem(e);
                }
            });
        }
        return mItemViewNmspEd;
    }

    public JMenuItem getClassifyMenuItem() {
        if (mItemViewClassify == null) {
            mItemViewClassify = new JMenuItem();
            mItemViewClassify.setSelected(false);
            mItemViewClassify.setName("classifyMenuItem");
            mItemViewClassify.setText("Classify");
            mItemViewClassify.setToolTipText("Classify Ontylog Extension Concepts");
            mItemViewClassify.setMnemonic('c');
            mItemViewClassify.setEnabled(false);
            mItemViewClassify.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_classifyMenuItem(e);
                }
            });
        }
        return mItemViewClassify;
    }

    public JMenuItem getClasErrorsMenuItem() {
        if (mItemViewClasErrors == null) {
            mItemViewClasErrors = new JMenuItem();
            mItemViewClasErrors.setSelected(false);
            mItemViewClasErrors.setName("clasErrorsMenuItem");
            mItemViewClasErrors.setText("Classify Errors");
            mItemViewClasErrors.setToolTipText("View Classifyier Error Messages");
            mItemViewClasErrors.setMnemonic('e');
            mItemViewClasErrors.setEnabled(false);
            mItemViewClasErrors.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_clasErrorsMenuItem(e);
                }
            });
        }
        return mItemViewClasErrors;
    }

    public JMenuItem getSubsetListMenuItem() {
        if (mItemViewSubsetList == null) {
            mItemViewSubsetList = new JMenuItem();
            mItemViewSubsetList.setSelected(false);
            mItemViewSubsetList.setName("subsetListMenuItem");
            mItemViewSubsetList.setText("New Subset List");
            mItemViewSubsetList.setToolTipText("Open Subset List Panel");
            mItemViewSubsetList.setMnemonic('s');
            mItemViewSubsetList.setEnabled(false);
            mItemViewSubsetList.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_subsetListMenuItem(e);
                }
            });
        }
        return mItemViewSubsetList;
    }

    public JMenuItem getSubsetEditorMenuItem() {
        if (mItemViewSubsetEditor == null) {
            mItemViewSubsetEditor = new JMenuItem();
            mItemViewSubsetEditor.setSelected(false);
            mItemViewSubsetEditor.setName("subsetEditorMenuItem");
            mItemViewSubsetEditor.setText("New Subset Editor");
            mItemViewSubsetEditor.setToolTipText("Open Subset Expression Editor");
            mItemViewSubsetEditor.setMnemonic('e');
            mItemViewSubsetEditor.setEnabled(false);
            mItemViewSubsetEditor.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_subsetEditorMenuItem(e);
                }
            });
        }
        return mItemViewSubsetEditor;
    }

    private JMenuItem getStatusMenuItem() {
        if (mItemViewStatus == null) {
            mItemViewStatus = new JCheckBoxMenuItem();
            mItemViewStatus.setSelected(true);
            mItemViewStatus.setName("statusBarMenuItem");
            mItemViewStatus.setText("Status Bar");
            mItemViewStatus.setToolTipText("Toggle Status Bar On / Off");
            mItemViewStatus.setMnemonic('b');
            mItemViewStatus.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_StatusBarMenuItem(e);
                }
            });
        }
        return mItemViewStatus;
    }

    public JMenu getOptionsMenu() {
        if (menuOptions == null) {
            menuOptions = new JMenu();
            menuOptions.setName("OptionsMenu");
            menuOptions.setText("Options");
            menuOptions.setMnemonic('o');
            menuOptions.setActionCommand("OptionsMenu");
            menuOptions.add(getCodeAndIdMenuItem());
            menuOptions.add(getLocNmspMenuItem());
        }
        return menuOptions;
    }

    private JMenuItem getCodeAndIdMenuItem() {
        if (mItemCodeAndId == null) {
            mItemCodeAndId = new JMenuItem();
            mItemCodeAndId.setName("codeAndIdMenuItem");
            mItemCodeAndId.setText("Code and ID Generator");
            mItemCodeAndId.setMnemonic('c');
            mItemCodeAndId.setActionCommand("SelCodeId");
            mItemCodeAndId.setEnabled(false);
            mItemCodeAndId.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_CodeAndIdMenuItem(e);
                }
            });
        }
        return mItemCodeAndId;
    }

    private JMenuItem getLocNmspMenuItem() {
        if (mItemLocNmsp == null) {
            mItemLocNmsp = new JMenuItem();
            mItemLocNmsp.setEnabled(false);
            mItemLocNmsp.setText("Set Current Local Namespace");
            mItemLocNmsp.setToolTipText("Set Current Local Namespace");
            mItemLocNmsp.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_LocNmsp(e);
                }
            });
        }
        return mItemLocNmsp;
    }

    public JMenu getMyHelpMenu() {
        if (menuHelp == null) {
            menuHelp = new JMenu();
            menuHelp.setName("Help Menu");
            menuHelp.setMnemonic('h');
            menuHelp.setText("Help");
            menuHelp.setActionCommand("HelpMenu");
            menuHelp.add(getHelpTopicsMenuItem());
            menuHelp.add(getMatchOverviewMenuItem());
            menuHelp.addSeparator();
            menuHelp.add(getHelpAboutMenuItem());
        }
        return menuHelp;
    }

    private JMenuItem getHelpTopicsMenuItem() {
        if (mItemHelpTopics == null) {
            mItemHelpTopics = new JMenuItem();
            mItemHelpTopics.setName("helpTopicsItem");
            mItemHelpTopics.setMnemonic('h');
            mItemHelpTopics.setText("Help Topics");
            mItemHelpTopics.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
            mItemHelpTopics.setActionCommand("HelpTopics");
            mItemHelpTopics.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_HelpTopics(e);
                }
            });
        }
        return mItemHelpTopics;
    }

    private JMenuItem getMatchOverviewMenuItem() {
        if (mItemMatchOverview == null) {
            mItemMatchOverview = new JMenuItem();
            mItemMatchOverview.setName("matchOverviewItem");
            mItemMatchOverview.setMnemonic('m');
            mItemMatchOverview.setText("Matching Overview");
            mItemMatchOverview.setActionCommand("MatchingOverview");
            mItemMatchOverview.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_MatchOverview(e);
                }
            });
        }
        return mItemMatchOverview;
    }

    private JMenuItem getHelpAboutMenuItem() {
        if (mItemHelpAbout == null) {
            mItemHelpAbout = new JMenuItem();
            mItemHelpAbout.setName("helpAboutItem");
            mItemHelpAbout.setMnemonic('a');
            mItemHelpAbout.setText("About DTS Editor");
            mItemHelpAbout.setActionCommand("About");
            mItemHelpAbout.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_HelpAbout(e);
                }
            });
        }
        return mItemHelpAbout;
    }

    public void actionPerformed_Connect(ActionEvent e) {
        fConnectMgr.doConnect();
    }

    public void actionPerformed_Disconnect(ActionEvent e) {
        fConnectMgr.doDisconnect();
    }

    public void actionPerformed_ConnectOptions(ActionEvent e) {
        doConnectOptions();
        fConnectMgr.doConnect();
    }

    public void actionPerformed_NewTree(ActionEvent e) {
        doNewTree();
    }

    public void actionPerformed_NewWalker(ActionEvent e) {
        doNewWalker();
    }

    public void actionPerformed_NewSearch(ActionEvent e) {
        doNewSearch();
    }

    public void actionPerformed_LocNmsp(ActionEvent e) {
        int locNmspsCount = 0;
        try {
            Namespace[] namespaces = NamespaceManager.getInstance().getNamespaces();
            for (int i = 0; i < namespaces.length; i++) {
                if (namespaces[i].isWritable() && namespaces[i].isLocal()) {
                    locNmspsCount++;
                }
            }
            if (locNmspsCount > 0) {
                doSetLocNmspDlog();
            } else {
                ApelApp.showWarningMsg("No Current Local Namespace is defined. \n" + "You must create a Writable Local Namespace before you \n" + "can set the Current Local Namespace.", fAppFrame);
                NamespaceEditor fNmspEditor = fAppFrame.getNamespacePanel();
                fNmspEditor.configure();
                BeanContext.getInstance().showDialog(fNmspEditor, TITLE_NAMESPACE_EDITOR);
            }
        } catch (DTSException ex) {
            if (checkServerConnection()) {
                String s = "DTS Exception in MainMenu.actionPerformed_LocNmsp(). ";
                Categories.data().error(s, ex);
                ApelApp.showWarningMsg("DTS Exception in MainMenu.actionPerformed_LocNmsp(). \n" + "Please see the log file for error message.", this);
            }
        }
    }

    public void actionPerformed_NewDetails(ActionEvent e) {
        doNewDetails();
    }

    /**
	 * Largely obsolete; artifact of Float/Dock UI experiment from v3.0.
	 * @param e ActionEvent
	 */
    public void actionPerformed_DetailsMenuItem(ActionEvent e) {
        if (mItemViewDetails.isSelected()) {
            fAppFrame.getTabPaneEditors().add("Details", fAppFrame.getConceptHolder());
        } else {
            fAppFrame.getTabPaneEditors().remove(fAppFrame.getConceptHolder());
        }
        fAppFrame.getContentPane().validate();
        return;
    }

    public void actionPerformed_NewAssocEd(ActionEvent e) {
        doNewAssocEd();
    }

    public void actionPerformed_NewSynonEd(ActionEvent e) {
        doNewSynonEd();
    }

    public void actionPerformed_NewPropEd(ActionEvent e) {
        doNewPropEd();
    }

    public void actionPerformed_NewQualEd(ActionEvent e) {
        doNewQualEd();
    }

    public void actionPerformed_AssocTypeMenuItem(ActionEvent e) {
        AssocTypeEditor fAssocType = fAppFrame.getAssocTypePanel();
        if (checkServerConnection()) {
            fAssocType.configure();
            BeanContext.getInstance().showDialog(fAssocType, TITLE_ASSOCIATION_TYPE);
        }
    }

    public void actionPerformed_PropTypeMenuItem(ActionEvent e) {
        PropTypeEditor fPropType = fAppFrame.getPropTypePanel();
        if (checkServerConnection()) {
            fPropType.configure();
            BeanContext.getInstance().showDialog(fPropType, TITLE_PROPERTY_TYPE);
        }
    }

    public void actionPerformed_QualTypeMenuItem(ActionEvent e) {
        QualTypeEditor fQualType = fAppFrame.getQualTypePanel();
        if (checkServerConnection()) {
            fQualType.configure();
            BeanContext.getInstance().showDialog(fQualType, TITLE_QUALIFIER_TYPE);
        }
    }

    public void actionPerformed_KindMenuItem(ActionEvent e) {
        KindViewer kindPanel = fAppFrame.getKindPanel();
        if (checkServerConnection()) {
            kindPanel.configure();
            BeanContext.getInstance().showDialog(kindPanel, TITLE_KIND);
        }
    }

    public void actionPerformed_RoleTypeMenuItem(ActionEvent e) {
        RoleTypeEditor roleTypePanel = fAppFrame.getRoleTypePanel();
        if (checkServerConnection()) {
            roleTypePanel.configure();
            BeanContext.getInstance().showDialog(roleTypePanel, TITLE_ROLE_TYPE);
        }
    }

    public void actionPerformed_NameSpaceMenuItem(ActionEvent e) {
        NamespaceEditor fNmspEditor = fAppFrame.getNamespacePanel();
        if (checkServerConnection()) {
            fNmspEditor.configure();
            BeanContext.getInstance().showDialog(fNmspEditor, TITLE_NAMESPACE_EDITOR);
        }
    }

    public void actionPerformed_classifyMenuItem(ActionEvent e) {
        ClassifyMgr.getInstance().doClassify();
    }

    public void actionPerformed_clasErrorsMenuItem(ActionEvent e) {
        ClassifyErrorsPanel classifyErrorPanel = new ClassifyErrorsPanel();
        BeanContext.getInstance().showPanel(classifyErrorPanel, "Classify Errors", 450, 300);
    }

    public void actionPerformed_subsetListMenuItem(ActionEvent e) {
        BeanContext.getInstance().showPanel(new SubsetListView(), "Subset List", 500, 400, true);
    }

    public void actionPerformed_subsetEditorMenuItem(ActionEvent e) {
        BeanContext.getInstance().showPanel(new SETreeEditor(), "Subset Expression Editor", 500, 400, true);
    }

    public void actionPerformed_StatusBarMenuItem(ActionEvent e) {
        fAppFrame.getStatusBar().toggle();
        fAppFrame.getContentPane().validate();
        return;
    }

    public void actionPerformed_FileExit(ActionEvent e) {
        if (isOkayToClose()) {
            getApelApp().endApp();
            ServerConnection sc = fApelApp.getServerConn();
            if (sc != null) {
                fApelApp.closeConnection();
            }
            System.exit(0);
        }
    }

    public boolean isOkayToClose() {
        if (ConnectMgr.getInstance().isConnected()) {
            okayToClose = false;
            okayToClose = ConnectMgr.getInstance().doDisconnect();
            return okayToClose;
        }
        return true;
    }

    public void actionPerformed_CodeAndIdMenuItem(ActionEvent e) {
        doSelectCodeAndIdGenerator();
    }

    public void actionPerformed_HelpAbout(ActionEvent e) {
        ApelApp.showAboutBox("About Apelon DTS Editor", "Apelon DTS Editor", ApelVersion.getInstance().getVersion("dts.version"), 2009, null);
    }

    public void actionPerformed_HelpTopics(ActionEvent e) {
        ApelHelp.showHelp(HELPTOPICFILE);
    }

    public void actionPerformed_MatchOverview(ActionEvent e) {
        ApelHelp.showHelp(MATCHOVERVIEWFILE);
    }

    public URL getRelativePath(String filename) {
        String absPath = null;
        String Parent = null;
        String sep = null;
        String helpFile = null;
        URL url = null;
        try {
            StringBuffer urlSbf = new StringBuffer();
            File wd = new File(System.getProperty("user.dir"));
            absPath = wd.getCanonicalPath();
            sep = System.getProperty("file.separator");
            File parent = new File(absPath);
            Parent = parent.getParent();
            helpFile = filename;
            if (helpFile != null && helpFile.trim().length() > 0) {
                urlSbf.append("file:" + sep + sep + Parent + sep + "docs" + sep + "help" + sep + helpFile);
                url = new URL(urlSbf.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Help file dtseditor.htm not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            Categories.uiController().error("Exception getting help file dtseditor.htm", e);
            ApelApp.showErrorMsg("Exception getting help file dtseditor.htm", fAppFrame);
        }
        return url;
    }

    public ApelApp getApelApp() {
        return fApelApp;
    }

    public ApelConfig getApelConfig() {
        return fApelConfig;
    }

    public void doConnectOptions() {
        ApelPanel myPanel = getConnOption();
        ApelDlg myDlog = new ApelDlg(myPanel, this, fAppFrame);
        myDlog.setTitle("Connect Options");
        myDlog.setSize(270, 170);
        myDlog.centerInWindow(fAppFrame);
        myDlog.pack();
        if (myDlog.showApelDlg() == ApelPanel.OK_CLICKED) myDlog.dispose();
        return;
    }

    private ConnectOptionPanel getConnOption() {
        if (connOption == null) {
            try {
                connOption = new ConnectOptionPanel(fApelConfig);
            } catch (java.lang.Throwable ex) {
                Categories.uiController().error("Exception getting ConnectOptionPanel", ex);
                ApelApp.showErrorMsg("Error getting Connect Option Panel.", fAppFrame);
            }
        }
        return connOption;
    }

    public void doNewTree() {
        if (checkServerConnection()) {
            TreeViewer fNewTree = new TreeViewer();
            fNewTree.configure();
            ApelDlg dlg = BeanContext.getInstance().createDialog(fNewTree, "Concept Tree", 400, 600, true);
            dlg.setModal(false);
            dlg.showApelDlg();
            ApelApp.registerDialog(dlg);
        }
    }

    public void doNewWalker() {
        if (checkServerConnection()) {
            WalkerViewer fNewWalker = new WalkerViewer();
            fNewWalker.configureWalker();
            ApelDlg dlg = BeanContext.getInstance().createDialog(fNewWalker, "Concept Walker", 400, 600, true);
            dlg.setModal(false);
            dlg.showApelDlg();
            ApelApp.registerDialog(dlg);
        }
    }

    public void doNewSearch() {
        if (checkServerConnection()) {
            SearchViewer fNewSearch = new SearchViewer();
            fNewSearch.configure();
            ApelDlg dlg = BeanContext.getInstance().createDialog(fNewSearch, "Search", 400, 600, true);
            dlg.setModal(false);
            dlg.showApelDlg();
            ApelApp.registerDialog(dlg);
            fNewSearch.setDialog(dlg);
        }
    }

    public void doSetLocNmspDlog() {
        SetCurrentLocalNamespacePanel myPanel = getSetLocalNamespacePanel();
        ApelDlg myDlog = new ApelDlg(myPanel, this, fAppFrame);
        myDlog.setTitle("Set Current Local Namespace");
        myDlog.setSize(300, 180);
        myDlog.centerInWindow(fAppFrame);
        myDlog.pack();
        if (myDlog.showApelDlg() == ApelPanel.OK_CLICKED) {
            myDlog.dispose();
        }
        return;
    }

    private SetCurrentLocalNamespacePanel getSetLocalNamespacePanel() {
        try {
            setLocNmspPanel = new SetCurrentLocalNamespacePanel();
        } catch (java.lang.Throwable ex) {
            Categories.uiController().error("Exception loading Set Current Local Namespace Panel.", ex);
            ApelApp.showErrorMsg("Exception loading Set Current Local Namespace Panel.", fAppFrame);
        }
        return setLocNmspPanel;
    }

    public void doNewDetails() {
        if (checkServerConnection()) {
            DetailsTree newDetailsPanel = new DetailsTree();
            newDetailsPanel.configure();
            ApelDlg dlg = BeanContext.getInstance().createDialog(newDetailsPanel, "Concept / Term Details", 450, 600, true);
            dlg.setModal(false);
            dlg.showApelDlg();
            ApelApp.registerDialog(dlg);
            ApelApp.registerDialog(dlg);
        }
    }

    public void doNewAssocEd() {
        try {
            if (checkServerConnection()) {
                AssociationEditor newAssocEditor = new AssociationEditor(false);
                if (!newAssocEditor.configure()) {
                    return;
                }
                newAssocEditor.getCloseButton().setEnabled(true);
                BeanContext.getInstance().showPanel(newAssocEditor, "Association Editor", 500, 350);
            }
        } catch (Exception ex) {
            Categories.uiController().error("Exception getting Association Editor", ex);
            ApelApp.showErrorMsg("Error getting Association Editor Panel. " + "See log for details.", fAppFrame);
        }
    }

    public void doNewSynonEd() {
        try {
            if (checkServerConnection()) {
                SynonymEditor newSynonEditor = new SynonymEditor(true);
                if (!newSynonEditor.configure()) {
                    return;
                }
                newSynonEditor.getCloseButton().setEnabled(true);
                BeanContext.getInstance().showPanel(newSynonEditor, "Synonym Editor", 500, 225);
            }
        } catch (Exception ex) {
            Categories.uiController().error("Exception getting Synonym Editor", ex);
            ApelApp.showErrorMsg("Error getting Synonym Editor Panel. " + "See log for details.", fAppFrame);
        }
    }

    public void doNewPropEd() {
        try {
            if (checkServerConnection()) {
                PropertyEditor newPropEditor = new PropertyEditor(false);
                if (!newPropEditor.configure()) {
                    return;
                }
                newPropEditor.getCloseButton().setEnabled(true);
                BeanContext.getInstance().showPanel(newPropEditor, "Property Editor", 500, 350);
            }
        } catch (Exception ex) {
            Categories.uiController().error("Exception getting Property Editor", ex);
            ApelApp.showErrorMsg("Error getting Property Editor Panel. " + "See log for details.", fAppFrame);
        }
    }

    public void doNewQualEd() {
        QualifierEditor fNewQualEditor = new QualifierEditor();
        BeanContext.getInstance().showPanel(fNewQualEditor, "Qualifier Editor", 500, 250);
    }

    public void doSelectCodeAndIdGenerator() {
        ApelPanel myPanel = getSelectCodeAndIdGeneratorPanel();
        ApelDlg myDlog = new ApelDlg(myPanel, this, fAppFrame);
        myDlog.setTitle("Select Code And ID Generator");
        myDlog.pack();
        myDlog.centerInWindow(fAppFrame);
        if (myDlog.showApelDlg() == ApelPanel.OK_CLICKED) {
            myDlog.dispose();
        }
        return;
    }

    private SelectCodeAndIdGeneratorPanel getSelectCodeAndIdGeneratorPanel() {
        try {
            selCodeAndIdGenPanel = new SelectCodeAndIdGeneratorPanel(fApelConfig, fApelApp.getServerConn());
        } catch (java.lang.Throwable ivjExc) {
            String errorMsg = "Error creating Select Code and ID Generator panel.";
            Categories.uiController().error(errorMsg, ivjExc);
            ApelApp.showErrorMsg(errorMsg, fAppFrame);
        }
        return selCodeAndIdGenPanel;
    }

    public void setUIConnected() {
        getConnectMenuItem().setEnabled(false);
        getConnOpsMenuItem().setEnabled(false);
        getDisconnectMenuItem().setEnabled(true);
        getDTSTreeMenuItem().setEnabled(true);
        getWalkerMenuItem().setEnabled(true);
        getSearchMenuItem().setEnabled(true);
        getLocNmspMenuItem().setEnabled(true);
        getDetailsMenuItem().setEnabled(true);
        getAssocEdMenuItem().setEnabled(true);
        getSynonEdMenuItem().setEnabled(true);
        getPropEdMenuItem().setEnabled(true);
        getAssocTypeMenuItem().setEnabled(true);
        getNamespaceMenuItem().setEnabled(true);
        getPropTypeMenuItem().setEnabled(true);
        getQualTypeMenuItem().setEnabled(true);
        getKindMenuItem().setEnabled(true);
        getRoleTypeMenuItem().setEnabled(true);
        getSubsetListMenuItem().setEnabled(true);
        getSubsetEditorMenuItem().setEnabled(true);
        getCodeAndIdMenuItem().setEnabled(true);
    }

    public void setUIDisconnected() {
        getConnectMenuItem().setEnabled(true);
        getConnOpsMenuItem().setEnabled(true);
        getDisconnectMenuItem().setEnabled(false);
        getDTSTreeMenuItem().setEnabled(false);
        getWalkerMenuItem().setEnabled(false);
        getSearchMenuItem().setEnabled(false);
        getLocNmspMenuItem().setEnabled(false);
        getDetailsMenuItem().setEnabled(false);
        getAssocEdMenuItem().setEnabled(false);
        getSynonEdMenuItem().setEnabled(false);
        getPropEdMenuItem().setEnabled(false);
        getAssocTypeMenuItem().setEnabled(false);
        getNamespaceMenuItem().setEnabled(false);
        getPropTypeMenuItem().setEnabled(false);
        getQualTypeMenuItem().setEnabled(false);
        getKindMenuItem().setEnabled(false);
        getRoleTypeMenuItem().setEnabled(false);
        getClassifyMenuItem().setEnabled(false);
        getClasErrorsMenuItem().setEnabled(false);
        getSubsetListMenuItem().setEnabled(false);
        getSubsetEditorMenuItem().setEnabled(false);
        getCodeAndIdMenuItem().setEnabled(false);
    }

    protected boolean checkServerConnection() {
        if (fApelApp.getServerConn().ping() == null && fApelConfig.getConnectionType() == ApelConfig.SOCKET_CONNECT) {
            ApelApp.showErrorMsg("Server Connection lost. " + "Please check if the DTS Server is still running.", this);
            return false;
        }
        return true;
    }
}
