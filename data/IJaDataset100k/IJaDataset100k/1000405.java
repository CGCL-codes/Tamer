package com.nokia.ats4.appmodel.perspective.testdesign.swing;

import com.nokia.ats4.appmodel.grapheditor.swing.JGraphEditor;
import com.nokia.ats4.appmodel.main.swing.MetadataTablePanel;
import com.nokia.ats4.appmodel.main.swing.ModelTreePanel;
import com.nokia.ats4.appmodel.main.swing.TabbedGraphPanel;
import com.nokia.ats4.appmodel.main.swing.UseCaseTreePanel;
import com.nokia.ats4.appmodel.main.swing.UseCaseTreePopup;
import com.nokia.ats4.appmodel.model.KendoModel;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCase;
import com.nokia.ats4.appmodel.model.swing.KeywordTableModel;
import com.nokia.ats4.appmodel.model.swing.UseCaseTreeModel;
import com.nokia.ats4.appmodel.perspective.DesignPerspective;
import com.nokia.ats4.appmodel.util.KendoResources;
import com.nokia.ats4.appmodel.event.EventQueue;
import com.nokia.ats4.appmodel.grapheditor.GraphEditor;
import com.nokia.ats4.appmodel.grapheditor.event.SelectCellEvent;
import com.nokia.ats4.appmodel.main.controller.simulator.AbstractPlayer;
import com.nokia.ats4.appmodel.main.event.GraphEditorChangedEvent;
import com.nokia.ats4.appmodel.main.event.OpenModelEvent;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCaseModel;
import com.nokia.ats4.appmodel.model.impl.MainApplicationModel;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCasePath;
import com.nokia.ats4.appmodel.perspective.DesignPanel;
import com.nokia.ats4.appmodel.perspective.testdesign.event.NewJGraphFormedEvent;
import com.nokia.ats4.appmodel.util.UseCasePathGenerator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Swing implementation of the test design view.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class TestDesignPanel extends javax.swing.JPanel implements DesignPanel {

    /** Action command for the system state tool button */
    public static final String CMD_STATE_TOOL = "ActionCommand.StateTool";

    /** Action command for the submodel tool button */
    public static final String CMD_SUBMODEL_TOOL = "ActionCommand.SubModelTool";

    /** Action command for the execute button */
    public static final String CMD_EXECUTE = "ActionCommand.StateTool";

    /** Kendo models tree component */
    private ModelTreePanel modelTreePanel = null;

    /*** Keno tets tree component */
    private UseCaseTreePanel testCaseTreePanel = null;

    /** Tab panel for graph editors */
    private TabbedGraphPanel tabbedGraphPanel = null;

    /** Keywords table panel */
    private MetadataTablePanel keywordTablePanel = null;

    /**
     * Creates new form TestDesignPanel, a Swing implementation of the test
     * design view.
     */
    public TestDesignPanel() {
        initComponents();
        this.modelTreePanel = new ModelTreePanel();
        this.testCaseTreePanel = new UseCaseTreePanel();
        this.modelTreePanel.addTreeListener(new ModelTreeListener());
        this.sidePanel.add(this.modelTreePanel);
        this.sidePanel.add(this.testCaseTreePanel);
        this.testCaseTreePanel.addTreeMouseListener(new TestCaseTreeMouseListener());
        this.testCaseTreePanel.setRootVisible(false);
        this.tabbedGraphPanel = new TabbedGraphPanel();
        this.tabbedGraphPanel.setToolButtonsVisible(false);
        this.tabbedGraphPanel.addChangeListener(new TabChangeListener());
        this.horizontalSplitter.setLeftComponent(this.tabbedGraphPanel);
        String kwTitle = KendoResources.getString("panel.keywords.title");
        this.keywordTablePanel = new MetadataTablePanel(kwTitle);
        this.keywordTablePanel.setEnabled(false);
        this.tablePanel.add(this.keywordTablePanel);
    }

    private void initComponents() {
        verticalSplitter = new javax.swing.JSplitPane();
        horizontalSplitter = new javax.swing.JSplitPane();
        editorPanel = new javax.swing.JPanel();
        idField = new javax.swing.JTextField();
        tablePanel = new javax.swing.JPanel();
        sidePanel = new javax.swing.JPanel();
        verticalSplitter.setDividerLocation(200);
        verticalSplitter.setResizeWeight(0.1);
        verticalSplitter.setPreferredSize(new java.awt.Dimension(800, 600));
        horizontalSplitter.setDividerLocation(500);
        horizontalSplitter.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        horizontalSplitter.setResizeWeight(0.9);
        idField.setEditable(false);
        idField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idField.setText("id-field");
        idField.setBorder(null);
        idField.setFocusable(false);
        idField.setOpaque(false);
        tablePanel.setLayout(new java.awt.GridLayout(1, 3, 5, 1));
        javax.swing.GroupLayout editorPanelLayout = new javax.swing.GroupLayout(editorPanel);
        editorPanel.setLayout(editorPanelLayout);
        editorPanelLayout.setHorizontalGroup(editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(editorPanelLayout.createSequentialGroup().addGroup(editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(editorPanelLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)).addGroup(editorPanelLayout.createSequentialGroup().addContainerGap().addComponent(idField, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE))).addContainerGap()));
        editorPanelLayout.setVerticalGroup(editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(editorPanelLayout.createSequentialGroup().addContainerGap().addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)));
        horizontalSplitter.setBottomComponent(editorPanel);
        verticalSplitter.setRightComponent(horizontalSplitter);
        sidePanel.setLayout(new java.awt.GridLayout(2, 1, 0, 4));
        sidePanel.setPreferredSize(new java.awt.Dimension(200, 600));
        verticalSplitter.setLeftComponent(sidePanel);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(verticalSplitter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(verticalSplitter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    }

    private javax.swing.JPanel editorPanel;

    private javax.swing.JSplitPane horizontalSplitter;

    private javax.swing.JTextField idField;

    private javax.swing.JPanel sidePanel;

    private javax.swing.JPanel tablePanel;

    private javax.swing.JSplitPane verticalSplitter;

    /**
     * Set the Document for event id text field.
     *
     * @param document Document to set
     */
    public void setIdDocument(Document document) {
        this.idField.setDocument(document);
    }

    /**
     * Set the table model for the keywords table.
     *
     * @param model TableModel to set.
     */
    public void setKeywordTableModel(KeywordTableModel model) {
        this.keywordTablePanel.setKeywordTableModel(model);
    }

    /**
     * Set the model for the tree panel that displays the KendoModels of the
     * current project.
     *
     * @param model TreeModel to set.
     */
    public void setTreeModel(TreeModel model) {
        this.modelTreePanel.setTreeModel(model);
    }

    public void setTestCaseTreeModel(UseCaseTreeModel model) {
        this.testCaseTreePanel.setUseCaseTreeModel(model);
    }

    /**
     * Add a the given graph editor in a new tab
     *
     * @param editor GraphEditor to add
     * @param title Title for the graph editor tab.
     */
    @Override
    public void addGraphEditor(GraphEditor editor, String title) {
        this.tabbedGraphPanel.addGraphEditor(editor, title);
    }

    /**
     * Set a tab visible by given tab index.
     *
     * @param i Index of the tab to bring on top
     */
    @Override
    public void showGraphEditor(int i) {
        this.tabbedGraphPanel.showTab(i);
    }

    /**
     * Remove all graph editor tabs from the tab panel.
     */
    @Override
    public void removeGraphEditors() {
        this.tabbedGraphPanel.removeAllTabs();
    }

    public void activatePlayer(AbstractPlayer model) {
        this.tabbedGraphPanel.insertPlayerToolBar(model);
    }

    /**
     * Remove a tab by given tab index.
     *
     * @param index Tab index
     */
    @Override
    public void removeGraphEditor(int index) {
        this.tabbedGraphPanel.removeTab(index);
    }

    public void setControlsEnabled(boolean isEnabled) {
        this.keywordTablePanel.setEnabled(isEnabled);
    }

    @Override
    public void setEntryAndExitPointToolsEnabled(boolean b) {
        this.tabbedGraphPanel.setEntryAndExitPointToolsEnabled(b);
    }

    @Override
    public void setInAndOutGateToolsEnabled(boolean b) {
        this.tabbedGraphPanel.setInGateAndOutGateToolsEnabled(b);
    }

    @Override
    public JSplitPane getVerticalSplitPane() {
        return this.verticalSplitter;
    }

    /**
     * Listener for tabbed pane
     */
    private class TabChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            int i = pane.getSelectedIndex();
            if (i > -1 && pane.getTabCount() > 0 && i < pane.getTabCount()) {
                EventQueue.postEvent(new GraphEditorChangedEvent(pane, i));
                EventQueue.postEvent(new NewJGraphFormedEvent(e.getSource(), null));
            }
        }
    }

    /**
     * Listener for model tree mouse events. Handles the opening of a model
     * in new tab when a model node is double-clicked.
     */
    private class ModelTreeListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JTree tree = (JTree) e.getSource();
                if (tree != null) {
                    DefaultMutableTreeNode n = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (n == null) {
                        n = (DefaultMutableTreeNode) tree.getPathForLocation(e.getX(), e.getY()).getLastPathComponent();
                    }
                    Object userObject = n.getUserObject();
                    if (userObject instanceof KendoModel) {
                        KendoModel m = (KendoModel) userObject;
                        EventQueue.postEvent(new OpenModelEvent(n, m));
                    }
                }
            }
        }
    }

    private class TestCaseTreeMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            JTree tree = (JTree) e.getSource();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() != null) {
                Object userObject = node.getUserObject();
                tree.setSelectionPath(new TreePath(node.getPath()));
                if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1 && tree != null) {
                    boolean isEditable = true;
                    UseCaseModel tcm = MainApplicationModel.getInstance().getActiveProject().getTestCaseModel();
                    if (userObject instanceof UseCase && tcm.getUseCase(((UseCase) userObject).getId()) != userObject) {
                        isEditable = false;
                    } else if (userObject instanceof UseCasePath && tcm.getUseCasePath(((UseCasePath) userObject).getId()) != userObject) {
                        isEditable = false;
                    }
                    UseCaseTreePopup pop = new UseCaseTreePopup(tree, userObject, isEditable);
                    if (pop.getComponents().length > 0) {
                        pop.show(tree, e.getX(), e.getY());
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                    DesignPerspective p = (DesignPerspective) MainApplicationModel.getInstance().getActivePerspective();
                    JGraphEditor je = p.getActiveGraphEditor().getJGraphEditor();
                    if (userObject instanceof UseCase) {
                        List<UseCasePath> paths = ((UseCase) userObject).getUseCasePaths();
                        UseCasePathGenerator generator = new UseCasePathGenerator();
                        generator.displayPreview(generator.pathsToTransitions(paths), je);
                    }
                    if (userObject instanceof UseCasePath) {
                        List<UseCasePath> paths = new ArrayList<UseCasePath>();
                        UseCasePath path = ((UseCasePath) userObject);
                        paths.add(path);
                        UseCasePathGenerator generator = new UseCasePathGenerator();
                        generator.displayPreview(generator.pathsToTransitions(paths), je);
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    DesignPerspective p = (DesignPerspective) MainApplicationModel.getInstance().getActivePerspective();
                    JGraphEditor je = p.getActiveGraphEditor().getJGraphEditor();
                    if (userObject instanceof UseCase) {
                        List<UseCasePath> paths = ((UseCase) userObject).getUseCasePaths();
                        UseCasePathGenerator generator = new UseCasePathGenerator();
                        if (paths.size() > 0 && paths.get(0).getFirstState() != null) {
                            KendoModel toBeOpen = paths.get(0).getFirstState().getContainingModel().getContainingModel();
                            EventQueue.dispatchEvent(new OpenModelEvent(je, toBeOpen));
                            p = (DesignPerspective) MainApplicationModel.getInstance().getActivePerspective();
                            je = p.getActiveGraphEditor().getJGraphEditor();
                        }
                        generator.displayPreview(generator.pathsToTransitions(paths), je);
                    } else if (userObject instanceof UseCasePath) {
                        List<UseCasePath> paths = new ArrayList<UseCasePath>();
                        UseCasePath path = ((UseCasePath) userObject);
                        paths.add(path);
                        if (path != null && path.getFirstState() != null) {
                            KendoModel toBeOpen = path.getFirstState().getContainingModel().getContainingModel();
                            EventQueue.dispatchEvent(new OpenModelEvent(je, toBeOpen));
                            p = (DesignPerspective) MainApplicationModel.getInstance().getActivePerspective();
                            je = p.getActiveGraphEditor().getJGraphEditor();
                        }
                        UseCasePathGenerator generator = new UseCasePathGenerator();
                        generator.displayPreview(generator.pathsToTransitions(paths), je);
                    } else if (node == tree.getModel().getRoot()) {
                        List<UseCase> cases = MainApplicationModel.getInstance().getActiveProject().getUseCaseModel().getUseCases();
                        List<UseCasePath> paths = new ArrayList<UseCasePath>();
                        for (UseCase uc : cases) {
                            paths.addAll(uc.getUseCasePaths());
                        }
                        UseCasePathGenerator generator = new UseCasePathGenerator();
                        generator.displayPreview(generator.pathsToTransitions(paths), je);
                    }
                }
                EventQueue.postEvent(new SelectCellEvent(this, userObject));
            }
        }
    }
}
