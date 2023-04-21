package com.rendion.ajl.gui;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class TreeFactory extends WidgetFactory {

    static Layout newTree(Layout layout, String id, String rootNodeContent) {
        NodeInfo top = new NodeInfo(rootNodeContent);
        Tree tree = new Tree(top);
        tree.setName(id.intern());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane listScroller = new JScrollPane(tree);
        attach(layout, id + "ScrollPane", listScroller);
        return navOnly(layout, id, tree);
    }

    static Layout newTree(Layout layout, String id) {
        NodeInfo top = new NodeInfo("A Tree");
        Tree tree = new Tree(top);
        tree.setName(id.intern());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane listScroller = new JScrollPane(tree);
        attach(layout, id + "ScrollPane", listScroller);
        return navOnly(layout, id, tree);
    }

    static Layout newTree(Layout layout, String id, Object[] data) {
        NodeInfo top = new NodeInfo("A Tree");
        Tree tree = new Tree(top);
        tree.setName(id.intern());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane listScroller = new JScrollPane(tree);
        attach(layout, id + "ScrollPane", listScroller);
        return navOnly(layout, id, tree);
    }

    static Layout newTree(Layout layout, String id, Object[] data, int width, int height) {
        NodeInfo top = new NodeInfo("A Tree");
        Tree tree = new Tree(top);
        tree.setName(id.intern());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane listScroller = new JScrollPane(tree);
        if (width == -1) {
        } else {
            listScroller.setPreferredSize(new Dimension(width, height));
        }
        attach(layout, id + "ScrollPane", listScroller);
        return navOnly(layout, id, tree);
    }
}
