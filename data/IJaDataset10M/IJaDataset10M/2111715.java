package com.peterhi.player.view;

import static com.peterhi.player.ResourceLocator.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import com.peterhi.player.action.*;
import com.peterhi.player.*;
import com.peterhi.player.view.*;
import com.peterhi.player.component.*;

public class JBasicWhiteboardElementView extends JPanel implements View {

    private JToolBar toolBar = new JToolBar();

    private JButton cmdUp = new JButton();

    private JButton cmdDown = new JButton();

    private JButton cmdDelete = new JButton();

    private JScrollPane scrollPane = new JScrollPane();

    private JTree tree = new JTree();

    public JBasicWhiteboardElementView() {
        super();
        setLayout(new BorderLayout());
        tree.setModel(new Model());
        tree.setCellRenderer(new BasicWhiteboardElementTreeCellRenderer());
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                JPlayerWindow playerWindow = Application.getWindow();
                JBasicWhiteboardView basicWhiteboardView = playerWindow.getView(JBasicWhiteboardView.class);
                JBasicWhiteboard basicWhiteboard = basicWhiteboardView.getWhiteboard();
                boolean b = getSelectedItem() != getRoot();
                DoZOrderDownAction.getInstance().setEnabled(b);
                DoZOrderUpAction.getInstance().setEnabled(b);
                DoDeleteAction.getInstance().setEnabled(b);
                Object uo = ((DefaultMutableTreeNode) getSelectedItem()).getUserObject();
                if (uo instanceof Shape) {
                    JBasicWhiteboard.AbstractJShape shape = (JBasicWhiteboard.AbstractJShape) uo;
                    basicWhiteboard.setSelectedShape(shape);
                } else {
                    basicWhiteboard.setSelectedShape(null);
                }
            }
        });
        cmdUp.setAction(DoZOrderUpAction.getInstance());
        cmdDown.setAction(DoZOrderDownAction.getInstance());
        cmdDelete.setAction(DoDeleteAction.getInstance());
        toolBar.add(cmdUp);
        toolBar.add(cmdDown);
        toolBar.add(cmdDelete);
        toolBar.setPreferredSize(new Dimension(25, 25));
        scrollPane.setViewportView(tree);
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateTreeUI() {
        tree.updateUI();
    }

    public Model getModel() {
        return (Model) tree.getModel();
    }

    public Object getRoot() {
        return tree.getModel().getRoot();
    }

    public Object getSelectedItem() {
        return tree.getSelectionPath().getLastPathComponent();
    }

    public Enumeration elements() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
        return root.children();
    }

    public String getViewName() {
        return getString(this, "NAME");
    }

    public Icon getViewIcon() {
        return null;
    }

    public static class Model extends DefaultTreeModel {

        private DefaultMutableTreeNode root;

        public Model() {
            super(new DefaultMutableTreeNode());
            root = (DefaultMutableTreeNode) getRoot();
        }

        public void addShape(JBasicWhiteboard.AbstractJShape shape) {
            DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(shape);
            root.add(leaf);
        }

        public void addAllShape(JBasicWhiteboard.AbstractJShape[] array) {
            for (JBasicWhiteboard.AbstractJShape item : array) {
                addShape(item);
            }
        }

        public void addAllShapeColl(Collection<JBasicWhiteboard.AbstractJShape> coll) {
            for (JBasicWhiteboard.AbstractJShape item : coll) {
                addShape(item);
            }
        }

        public void nodeUp(MutableTreeNode node) {
            int index = root.getIndex(node);
            if (index > 0) {
                root.insert(node, index - 1);
            }
        }

        public void nodeDown(MutableTreeNode node) {
            int index = root.getIndex(node);
            if (index >= 0 && index < root.getChildCount() - 1) {
                root.insert(node, index + 1);
            }
        }

        public void deleteNode(MutableTreeNode node) {
            root.remove(node);
        }

        public void deleteShape(JBasicWhiteboard.AbstractJShape shape) {
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
                deleteNode(node);
            }
        }

        public void delete(String name) {
            Enumeration e = root.children();
            DefaultMutableTreeNode node = null;
            while (e.hasMoreElements()) {
                DefaultMutableTreeNode item = (DefaultMutableTreeNode) e.nextElement();
                JBasicWhiteboard.AbstractJShape shape = (JBasicWhiteboard.AbstractJShape) item.getUserObject();
                if (shape.getName().equals(name)) {
                    node = item;
                    break;
                }
            }
            if (node != null) {
                deleteNode(node);
            }
        }

        public void removeAll() {
            root.removeAllChildren();
        }

        public void shapeUp(JBasicWhiteboard.AbstractJShape shape) {
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
                nodeUp(node);
            }
        }

        public void shapeDown(JBasicWhiteboard.AbstractJShape shape) {
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
                nodeDown(node);
            }
        }

        public void removeShape(JBasicWhiteboard.AbstractJShape shape) {
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
                deleteNode(node);
            }
        }
    }

    class BasicWhiteboardElementTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
            if (value == tree.getModel().getRoot()) {
                setText(getString(JBasicWhiteboardElementView.class, "ROOT"));
            } else {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                JBasicWhiteboard.AbstractJShape shape = (JBasicWhiteboard.AbstractJShape) node.getUserObject();
                setText(shape.getName());
            }
            return this;
        }
    }
}
