package com.peterhi.player;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import com.peterhi.player.shapes.Shape;

public class ElementsTreeModel extends DefaultTreeModel {

    private DefaultMutableTreeNode root;

    public ElementsTreeModel() {
        super(new DefaultMutableTreeNode());
        root = (DefaultMutableTreeNode) getRoot();
    }

    public void add(Shape shape) {
        DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(shape);
        root.add(leaf);
    }

    public void up(MutableTreeNode node) {
        int index = root.getIndex(node);
        if (index > 0) {
            root.insert(node, index - 1);
        }
    }

    public void down(MutableTreeNode node) {
        int index = root.getIndex(node);
        if (index >= 0 && index < root.getChildCount() - 1) {
            root.insert(node, index + 1);
        }
    }

    public void delete(MutableTreeNode node) {
        root.remove(node);
    }

    public void delete(Shape shape) {
        Enumeration e = root.children();
        DefaultMutableTreeNode node = null;
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) e.nextElement();
            if (item.getUserObject() == shape) {
                node = item;
                break;
            }
        }
        if (node != null) {
            delete(node);
        }
    }

    public void delete(String name) {
        Enumeration e = root.children();
        DefaultMutableTreeNode node = null;
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) e.nextElement();
            Shape shape = (Shape) item.getUserObject();
            System.out.println("shape name is: " + shape.name);
            if (shape.name.equals(name)) {
                node = item;
                break;
            }
        }
        if (node != null) {
            delete(node);
        }
    }

    public void removeAll() {
        root.removeAllChildren();
    }

    public void up(Shape shape) {
        Enumeration e = root.children();
        DefaultMutableTreeNode node = null;
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) e.nextElement();
            if (item.getUserObject() == shape) {
                node = item;
                break;
            }
        }
        if (node != null) {
            up(node);
        }
    }

    public void down(Shape shape) {
        Enumeration e = root.children();
        DefaultMutableTreeNode node = null;
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) e.nextElement();
            if (item.getUserObject() == shape) {
                node = item;
                break;
            }
        }
        if (node != null) {
            down(node);
        }
    }

    public void remove(Shape shape) {
        Enumeration e = root.children();
        DefaultMutableTreeNode node = null;
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) e.nextElement();
            if (item.getUserObject() == shape) {
                node = item;
                break;
            }
        }
        if (node != null) {
            delete(node);
        }
    }
}
