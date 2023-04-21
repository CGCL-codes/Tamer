package com.rise.rois.ui.trees;

import java.rmi.RemoteException;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import com.rise.rois.server.SessionManagerWrapper;
import com.rise.rois.server.SessionManagerStub.LightweightSession;
import com.rise.rois.server.SessionManagerStub.SessionDAO;
import com.rise.rois.ui.main.SessionWrapper;
import com.rise.rois.ui.util.ImageManager;
import com.rise.rois.ui.util.MessageUtil;
import com.rise.rois.ui.util.ServerConfig;
import com.rise.rois.ui.util.SessionUtil;

public class ActiveSessionTree extends SessionTree {

    private static final long serialVersionUID = 1L;

    public ActiveSessionTree(DefaultTreeModel model, TreeNode root) {
        super(model);
        TreeManager.setSessionTree(this);
        ImageIcon leafIcon = ImageManager.getDefault().getSessionIcon(false);
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setLeafIcon(leafIcon);
            this.setCellRenderer(renderer);
        }
        if (root instanceof DefaultMutableTreeNode) {
            this.root = (DefaultMutableTreeNode) root;
        }
    }

    public void populate() {
        MessageUtil.addMessage("Loading Session Data");
        try {
            if (SessionManagerWrapper.size() != 0) {
                createTree();
            } else {
                MessageUtil.addMessage("No Session data found");
            }
        } catch (RemoteException e) {
            MessageUtil.addMessage("Error populating Session tree - " + e.getMessage());
        }
        expandPath(getPathForRow(0));
        MessageUtil.addMessage("Session Data Loaded");
    }

    private void createTree() {
        if (ServerConfig.isInitialised()) {
            root.removeAllChildren();
            LightweightSession[] lightweightSessions = SessionManagerWrapper.getUISessionOfType(SessionUtil.INDIVIDUAL_ID, true);
            if (lightweightSessions != null && lightweightSessions.length > 0) {
                for (LightweightSession lightweightSession : lightweightSessions) {
                    SessionWrapper sessionWrapper = new SessionWrapper(lightweightSession);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(sessionWrapper);
                    root.add(node);
                }
            }
            model.reload(root);
            this.setSelectionRow(0);
        }
    }

    public void addSession(int session_id) {
        SessionDAO sessionDAO = SessionManagerWrapper.getSessionDao(session_id);
        SessionWrapper sessionWrapper = new SessionWrapper(sessionDAO);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(sessionWrapper);
        model.insertNodeInto(node, root, root.getChildCount());
        TreePath pathToNewNode = new TreePath(node.getPath());
        scrollPathToVisible(pathToNewNode);
        setSelectionPath(pathToNewNode);
        sessionPanel.updateDisplayData();
    }

    public void endSession(int session_id) {
        Enumeration<?> nodeEnumeration = root.children();
        while (nodeEnumeration.hasMoreElements()) {
            Object object = nodeEnumeration.nextElement();
            if (object instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode treeNodeToMove = (DefaultMutableTreeNode) object;
                Object treeObject = treeNodeToMove.getUserObject();
                if (treeObject instanceof SessionWrapper) {
                    SessionWrapper sessionWrapper = (SessionWrapper) treeObject;
                    if (sessionWrapper.getSessionUuid() == session_id) {
                        model.removeNodeFromParent(treeNodeToMove);
                    }
                }
            }
        }
    }

    public void update(DefaultMutableTreeNode node, int id) {
        if (node == null) {
            node = root;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            TreeNode treeNode = node.getChildAt(i);
            if (treeNode instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treeNode;
                Object obj = defaultMutableTreeNode.getUserObject();
                if (obj instanceof SessionWrapper) {
                    SessionWrapper sessionWrapper = (SessionWrapper) obj;
                    if (sessionWrapper.getSessionUuid() == id) {
                        model.nodeChanged(defaultMutableTreeNode);
                        TreePath pathToNewNode = new TreePath(defaultMutableTreeNode.getPath());
                        scrollPathToVisible(pathToNewNode);
                        setSelectionPath(pathToNewNode);
                        if (sessionPanel != null) {
                            sessionPanel.updateDisplayData();
                        }
                        return;
                    }
                }
                if (defaultMutableTreeNode.getChildCount() > 0) {
                    update(defaultMutableTreeNode, id);
                }
            }
        }
    }

    public boolean find(DefaultMutableTreeNode node, String textToFind) {
        if (node == null) {
            node = root;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            TreeNode treeNode = node.getChildAt(i);
            if (treeNode instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treeNode;
                Object obj = defaultMutableTreeNode.getUserObject();
                if (obj instanceof SessionWrapper) {
                    SessionWrapper sessionWrapper = (SessionWrapper) obj;
                    String str1 = sessionWrapper.toString().toLowerCase();
                    String str2 = textToFind.toLowerCase();
                    if (str1.contains(str2)) {
                        TreePath pathToNewNode = new TreePath(defaultMutableTreeNode.getPath());
                        scrollPathToVisible(pathToNewNode);
                        setSelectionPath(pathToNewNode);
                        return true;
                    }
                }
                if (defaultMutableTreeNode.getChildCount() > 0) {
                    return find(defaultMutableTreeNode, textToFind);
                }
            }
        }
        return false;
    }
}
