package com.google.code.tree;

import com.google.code.model.ZipFragmentModel;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.treeStructure.actions.CollapseAllAction;
import com.intellij.ui.treeStructure.actions.ExpandAllAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Date: 12/18/10
 * Time: 8:08 PM
 */
public class BaseFolderSelectionTree extends AbstractFileSelectionTree<ZipFragmentModel> {

    public BaseFolderSelectionTree(final Project project, final ZipFragmentModel model) {
        super(project, model, "label.fragment.select-base-folder");
    }

    @Override
    protected CheckBoxSelectionStateEnum getNodeStatus(final DefaultMutableTreeNode node) {
        final String userObjStr = convertUserObjectToString(node.getUserObject());
        final CheckBoxSelectionStateEnum resultState;
        if (userObjStr.equals(myModel.getParentFolder())) {
            resultState = CheckBoxSelectionStateEnum.FULL;
        } else {
            resultState = CheckBoxSelectionStateEnum.CLEAR;
        }
        return resultState;
    }

    @Override
    protected DefaultTreeModel buildTreeModel() {
        final String autoParentFolder = myModel.autoComputeParentFolder();
        final TreeModel currentTreeModel = myTree.getModel();
        final DefaultMutableTreeNode currentRootNode = (DefaultMutableTreeNode) currentTreeModel.getRoot();
        final DefaultMutableTreeNode newRootNode = new DefaultMutableTreeNode(currentRootNode.getUserObject());
        if (autoParentFolder != null) {
            addChildrenToNodeFromVirtualFile(newRootNode, myProject.getBaseDir(), autoParentFolder);
        }
        return new DefaultTreeModel(newRootNode);
    }

    private void addChildrenToNodeFromVirtualFile(final DefaultMutableTreeNode parentNode, final VirtualFile virtualFile, final String autoParentFolder) {
        if (virtualFile.isValid()) {
            if (autoParentFolder.startsWith(virtualFile.getPath())) {
                final DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(virtualFile);
                parentNode.add(newNode);
                for (final VirtualFile loopChildVirtualFile : virtualFile.getChildren()) {
                    addChildrenToNodeFromVirtualFile(newNode, loopChildVirtualFile, autoParentFolder);
                }
            }
        }
    }

    @Override
    protected void handleUnselectedNodes(final DefaultMutableTreeNode pRoot) {
    }

    @Override
    protected Collection<String> getSelectedItemsInModel() {
        return Arrays.asList(myModel.getParentFolder());
    }

    protected void includeSelection() {
        handleChangeInSelection();
    }

    protected void excludeSelection() {
        handleChangeInSelection();
    }

    private void handleChangeInSelection() {
        final List<String> selectedItemsInTree = getSelectedItemsInTree();
        Collections.sort(selectedItemsInTree);
        if (!selectedItemsInTree.isEmpty()) {
            myModel.setParentFolder(selectedItemsInTree.get(0));
        }
        notifyInclusionListener();
        repaint();
    }

    protected void notifyInclusionListener() {
    }

    public ActionGroup getTreeActions() {
        final DefaultActionGroup group = new DefaultActionGroup();
        final ExpandAllAction expandAllAction = new ExpandAllAction(myTree);
        final CollapseAllAction collapseAllAction = new CollapseAllAction(myTree);
        group.add(expandAllAction);
        group.add(collapseAllAction);
        expandAllAction.registerCustomShortcutSet(new CustomShortcutSet(KeymapManager.getInstance().getActiveKeymap().getShortcuts(IdeActions.ACTION_EXPAND_ALL)), myTree);
        collapseAllAction.registerCustomShortcutSet(new CustomShortcutSet(KeymapManager.getInstance().getActiveKeymap().getShortcuts(IdeActions.ACTION_COLLAPSE_ALL)), myTree);
        return group;
    }
}
