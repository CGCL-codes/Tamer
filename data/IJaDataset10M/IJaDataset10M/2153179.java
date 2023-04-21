package com.jes.classfinder.gui.tree.filesystemtree;

import java.util.Enumeration;
import java.util.List;
import com.jes.classfinder.gui.internationalization.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import com.jes.classfinder.helpers.findjar.JarNode;
import com.jes.classfinder.helpers.findjar.PathNode;

@SuppressWarnings("serial")
public class FileSystemTree extends JTree implements Internationalization {

    public static Logger logger = Logger.getLogger(FileSystemTree.class);

    public void setLanguage(String lang) {
        GUIDictionary.changeLanguage(DICTIONARY_RESOURCES, lang);
    }

    public FileSystemTree(List<PathNode> pathNodeList) {
        this(getTreeModel(pathNodeList));
        this.addMouseListener(new FileSystemMouseListener(this));
    }

    public FileSystemTree(TreeModel treeModel) {
        super(treeModel);
        this.setCellRenderer(new CheckBoxNodeRenderer());
        this.setCellEditor(new CheckBoxNodeEditor(this));
        this.setEditable(true);
    }

    private static TreeModel getTreeModel(List<PathNode> pathNodeList) {
        DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode();
        defaultMutableTreeNode.setUserObject("Computer");
        for (PathNode pathNode : pathNodeList) {
            populateTreeNode(pathNode, defaultMutableTreeNode);
        }
        TreeModel treeModel = new DefaultTreeModel(defaultMutableTreeNode);
        return treeModel;
    }

    private static void populateTreeNode(PathNode childNode, DefaultMutableTreeNode parentDefaultMutableTreeNode) {
        DefaultMutableTreeNode childDefaultMutableTreeNode = new DefaultMutableTreeNode();
        logger.debug("Debugging pathNodes" + childNode);
        childDefaultMutableTreeNode.setUserObject(childNode);
        parentDefaultMutableTreeNode.add(childDefaultMutableTreeNode);
        List<PathNode> childPathList = childNode.getChildNodes();
        for (PathNode childOfChildPathNode : childPathList) {
            populateTreeNode(childOfChildPathNode, childDefaultMutableTreeNode);
        }
    }

    private boolean isJar(DefaultMutableTreeNode childTreeNode) {
        Object userObject = childTreeNode.getUserObject();
        if (userObject instanceof JarNode) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasJars(DefaultMutableTreeNode treeNode) {
        Enumeration<DefaultMutableTreeNode> treeNodeEnum = treeNode.children();
        DefaultMutableTreeNode childTreeNode;
        boolean foundJar = false;
        while (treeNodeEnum.hasMoreElements()) {
            childTreeNode = (DefaultMutableTreeNode) treeNodeEnum.nextElement();
            if (isJar(childTreeNode)) {
                foundJar = true;
                break;
            }
        }
        return foundJar;
    }

    private void expandTreeRecursively(DefaultMutableTreeNode treeNode) {
        if (hasJars(treeNode)) {
            this.scrollPathToVisible(new TreePath(treeNode.getPath()));
        } else {
            Enumeration<DefaultMutableTreeNode> treeNodeEnum = treeNode.children();
            DefaultMutableTreeNode childTreeNode;
            while (treeNodeEnum.hasMoreElements()) {
                childTreeNode = (DefaultMutableTreeNode) treeNodeEnum.nextElement();
                expandTreeRecursively(childTreeNode);
            }
        }
    }

    public void expandTreeToOneLevelBeforeJars() {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) this.treeModel.getRoot();
        expandTreeRecursively(treeNode);
    }
}
