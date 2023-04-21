package net.mjrz.fm.ui.utils;

import java.awt.Point;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.*;
import javax.swing.tree.TreePath;
import net.mjrz.fm.constants.AccountTypes;
import net.mjrz.fm.entity.FManEntityManager;
import net.mjrz.fm.entity.beans.Account;
import net.mjrz.fm.entity.beans.AccountCategory;
import net.mjrz.fm.ui.panels.AccountsTreePanel;

public class AccountsTreeDropTarget implements DropTargetListener {

    private DropTarget target;

    private JTree targetTree;

    private AccountsTreeModel model;

    FManEntityManager em;

    private AccountsTreePanel parentPanel;

    public AccountsTreeDropTarget(AccountsTreePanel panel) {
        parentPanel = panel;
        targetTree = panel.getAccountsTree();
        model = (AccountsTreeModel) targetTree.getModel();
        target = new DropTarget(targetTree, this);
        em = new FManEntityManager();
    }

    private AccountCategory getNodeForEvent(DropTargetDragEvent dtde) {
        Point p = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent();
        TreePath path = tree.getClosestPathForLocation(p.x, p.y);
        return (AccountCategory) path.getLastPathComponent();
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        AccountCategory node = getNodeForEvent(dtde);
        if (model.isAccount(node.getCategoryId())) {
            dtde.rejectDrag();
        } else {
            dtde.acceptDrag(dtde.getDropAction());
        }
    }

    public void dragOver(DropTargetDragEvent dtde) {
        AccountCategory node = getNodeForEvent(dtde);
        if (model.isAccount(node.getCategoryId())) {
            dtde.rejectDrag();
        } else {
            dtde.acceptDrag(dtde.getDropAction());
        }
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void drop(DropTargetDropEvent dtde) {
        Point pt = dtde.getLocation();
        DropTargetContext dtc = dtde.getDropTargetContext();
        JTree tree = (JTree) dtc.getComponent();
        TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
        AccountCategory parent = (AccountCategory) parentpath.getLastPathComponent();
        if (model.isAccount(parent.getCategoryId())) {
            dtde.rejectDrop();
            return;
        }
        try {
            Transferable tr = dtde.getTransferable();
            DataFlavor[] flavors = tr.getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) {
                if (tr.isDataFlavorSupported(flavors[i])) {
                    Long[] p = (Long[]) tr.getTransferData(flavors[i]);
                    Long l = p[p.length - 1];
                    Account node = model.getAccount(l);
                    if (node == null) continue;
                    long parentMajorCategoryId = model.getMajorAccountCategory(parent);
                    AccountCategory tmp = new AccountCategory(1, node.getAccountId(), node.getCategoryId());
                    long nodeMajorCategoryId = model.getMajorAccountCategory(tmp);
                    if (parentMajorCategoryId != nodeMajorCategoryId) {
                        dtde.rejectDrop();
                        parentPanel.showErrorDialog("Cannot change category type from: " + AccountTypes.getAccountType(Long.valueOf(nodeMajorCategoryId)) + " to " + AccountTypes.getAccountType(Long.valueOf(parentMajorCategoryId)));
                        return;
                    }
                    dtde.acceptDrop(dtde.getDropAction());
                    int r = em.updateAccountForCategory(node.getAccountId(), parent.getCategoryId());
                    if (r != 1) {
                        dtde.dropComplete(false);
                        return;
                    }
                    model.removeAccount(node);
                    node.setCategoryId(parent.getCategoryId());
                    model.addAccount(node);
                    dtde.dropComplete(true);
                    return;
                }
            }
            dtde.dropComplete(false);
        } catch (Exception e) {
            e.printStackTrace();
            dtde.rejectDrop();
        }
    }
}
