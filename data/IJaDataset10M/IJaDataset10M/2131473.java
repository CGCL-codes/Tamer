package pedro.tabletDeployment;

import pedro.system.*;
import pedro.desktopDeployment.*;
import pedro.mda.model.*;
import pedro.io.RecordModelViewCoordinator;
import pedro.util.SystemLog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class WhereAmIPanel extends JPanel implements ActionListener, ChangeListener {

    private NavigationTree navigationTree;

    private JScrollPane navigationTreePane;

    private JEditorPane comments;

    private JScrollPane commentPane;

    private JButton go;

    private JButton close;

    private PedroUIFactory pedroUIFactory;

    private FileSpace fileSpace;

    private TabletPedroDialog dialog;

    private CardLayout cardLayout;

    private boolean isTreeInitialised;

    public WhereAmIPanel(PedroFormContext pedroFormContext, CardLayout cardLayout) {
        this.dialog = (TabletPedroDialog) pedroFormContext.getDocumentProperty(PedroDocumentContext.TABLET_PEDRO_DIALOG);
        this.cardLayout = cardLayout;
        setLayout(new GridBagLayout());
        this.pedroUIFactory = (PedroUIFactory) pedroFormContext.getApplicationProperty(PedroApplicationContext.USER_INTERFACE_FACTORY);
        String titleText = PedroResources.getMessage("tabletPedro.whereAmI.title");
        JLabel titleLabel = pedroUIFactory.createLabel(titleText);
        GridBagConstraints panelGC = pedroUIFactory.createGridBagConstraints();
        add(titleLabel, panelGC);
        panelGC.gridy++;
        panelGC.fill = GridBagConstraints.BOTH;
        panelGC.weightx = 100;
        panelGC.weighty = 100;
        navigationTree = new NavigationTree(pedroUIFactory);
        PedroDocumentContext pedroDocumentContext = pedroFormContext.getDocumentContext();
        pedroDocumentContext.setProperty(PedroDocumentContext.TABLET_WHEREAMI_NAVIGATION_TREE, navigationTree);
        RecordModelFactory recordModelFactory = (RecordModelFactory) pedroFormContext.getApplicationProperty(PedroApplicationContext.RECORD_MODEL_FACTORY);
        RecordModel topLevelRecordModel = recordModelFactory.createTopLevelRecordModel();
        navigationTree.setRoot(topLevelRecordModel);
        DataEntryPanel dataEntryPanel = dialog.getDataEntryPanel();
        navigationTree.setRecordView(dataEntryPanel.getRecordView());
        JScrollPane navigationTreePane = pedroUIFactory.createScrollPane(navigationTree);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, navigationTreePane, createCommentPanel());
        add(splitPane, panelGC);
        splitPane.setDividerLocation(0.5);
        panelGC.gridy++;
        panelGC.anchor = GridBagConstraints.SOUTHEAST;
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.weightx = 0;
        panelGC.weighty = 0;
        add(createButtonPanel(), panelGC);
        navigationTree.addChangeListener(this);
    }

    private JPanel createCommentPanel() {
        JPanel panel = pedroUIFactory.createPanel(new GridBagLayout());
        GridBagConstraints panelGC = new GridBagConstraints();
        panelGC.gridx = 0;
        panelGC.gridy = 0;
        panelGC.weightx = 0;
        panelGC.weighty = 0;
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.anchor = GridBagConstraints.NORTHWEST;
        String commentPanelTitleText = PedroResources.getMessage("tabletPedro.navigation.comments");
        JLabel commentPanelTitle = pedroUIFactory.createLabel(commentPanelTitleText);
        panel.add(commentPanelTitle, panelGC);
        panelGC.gridy++;
        panelGC.anchor = GridBagConstraints.NORTHWEST;
        panelGC.fill = GridBagConstraints.BOTH;
        panelGC.weightx = 100;
        panelGC.weighty = 100;
        comments = pedroUIFactory.createEditorPane();
        comments.setContentType("text/html");
        commentPane = pedroUIFactory.createScrollPane(comments);
        panel.add(commentPane, panelGC);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = pedroUIFactory.createPanel(new GridBagLayout());
        GridBagConstraints panelGC = new GridBagConstraints();
        panelGC.gridx = 0;
        panelGC.gridy = 0;
        panelGC.weightx = 0;
        panelGC.weighty = 0;
        panelGC.fill = GridBagConstraints.NONE;
        panelGC.anchor = GridBagConstraints.NORTHWEST;
        String goText = PedroResources.getMessage("buttons.go");
        go = pedroUIFactory.createButton(goText);
        go.addActionListener(this);
        panel.add(go, panelGC);
        panelGC.gridx++;
        String closeText = PedroResources.getMessage("buttons.close");
        close = pedroUIFactory.createButton(closeText);
        close.addActionListener(this);
        panel.add(close, panelGC);
        return panel;
    }

    public void clearComments() {
        navigationTree.resetDisplay();
        comments.setText(PedroResources.EMPTY_STRING);
    }

    public RecordModel getSelectedRecordModel() {
        NavigationTreeNode selectedNode = navigationTree.getActiveNode();
        if (selectedNode == null) {
            return null;
        }
        RecordModel selectedRecordModel = selectedNode.getRecordModel();
        return selectedRecordModel;
    }

    public void setFileSpace(FileSpace fileSpace) {
        this.fileSpace = fileSpace;
        RecordModel currentRecordModel = fileSpace.getCurrentRecordModel();
        RecordModelUtility recordModelUtility = new RecordModelUtility();
        RecordModel topLevelRecordModel = recordModelUtility.getRootModel(currentRecordModel);
        RecordModelViewCoordinator recordModelViewCoordinator = new RecordModelViewCoordinator();
        recordModelViewCoordinator.clearChangeListeners(topLevelRecordModel);
        RecordModelViewCoordinator treeAssembler = new RecordModelViewCoordinator();
        recordModelViewCoordinator.read(topLevelRecordModel, navigationTree);
        navigationTree.setRoot(recordModelViewCoordinator.getRoot());
        RecordModelCommentRegistry recordModelCommentRegistry = fileSpace.getRecordModelCommentRegistry();
        RecordNameProvider[] recordsWithComments = recordModelCommentRegistry.getRecordsWithComments();
        for (int i = 0; i < recordsWithComments.length; i++) {
            navigationTree.markNodeWithComments(recordsWithComments[i]);
        }
        navigationTree.setActiveNode(currentRecordModel);
    }

    public void openTemplate(RecordModel templateRootRecordModel) {
        String templateRecordModelClass = templateRootRecordModel.getRecordClassName();
        NavigationTreeNode currentNode = navigationTree.getActiveNode();
        RecordModel currentRecordModel = currentNode.getRecordModel();
        String currentRecordModelClass = currentRecordModel.getRecordClassName();
        if (currentRecordModelClass.equals(templateRecordModelClass) == false) {
            String errorMessage = PedroResources.getMessage("pedro.fileMenu.openTemplate.typeMismatch");
            SystemLog.addError(errorMessage);
            return;
        }
        navigationTree.pasteNode(currentRecordModel, templateRootRecordModel);
    }

    private void go() {
        NavigationTreeNode selectedNode = navigationTree.getActiveNode();
        fileSpace.setCurrentRecordModel(selectedNode.getRecordModel());
        restoreOldChangeListenerSettings();
        dialog.popCard(this);
        dialog.setFileSpace(fileSpace);
    }

    private void close() {
        restoreOldChangeListenerSettings();
        dialog.popCard(this);
        dialog.setFileSpace(fileSpace);
    }

    /**
	* I don't know what the scope of effect is if record model ends up 
	* having two change listeners so to be safe I get rid of the change
	* listeners when we go from the WhereAmIPanel to the DataEntryPanel
	*/
    private void restoreOldChangeListenerSettings() {
        RecordModel currentRecordModel = fileSpace.getCurrentRecordModel();
        RecordModelUtility recordModelUtility = new RecordModelUtility();
        RecordModel topLevelRecordModel = recordModelUtility.getRootModel(currentRecordModel);
        RecordModelViewCoordinator recordModelViewCoordinator = new RecordModelViewCoordinator();
        recordModelViewCoordinator.clearChangeListeners(topLevelRecordModel);
        DataEntryPanel dataEntryPanel = dialog.getDataEntryPanel();
        recordModelViewCoordinator.assignChangeListeners(topLevelRecordModel, dataEntryPanel.getRecordStack());
    }

    public void actionPerformed(ActionEvent event) {
        Object button = event.getSource();
        if (button == go) {
            go();
        } else if (button == close) {
            close();
        }
    }

    public void stateChanged(ChangeEvent event) {
        RecordNameProvider currentRecordNameProvider = (RecordNameProvider) event.getSource();
        RecordModelCommentRegistry recordModelCommentRegistry = fileSpace.getRecordModelCommentRegistry();
        String comment = recordModelCommentRegistry.getComment(currentRecordNameProvider);
        if (comment == null) {
            comments.setText(PedroResources.EMPTY_STRING);
        } else {
            comments.setText(comment);
        }
    }
}
