package unbbayes.datamining.gui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import unbbayes.controller.IconController;
import unbbayes.datamining.datamanipulation.Attribute;
import unbbayes.datamining.datamanipulation.Instance;
import unbbayes.util.ArrayMap;

/**
 *	Class that implements a tree used to insert a new instance for classification.
 *
 *	@author Rafael Moraes Noivo
 *	@version $1.0 $ (02/16/2003)
 */
public class AttributesTree extends JTree {

    /** Serialization runtime version number */
    private static final long serialVersionUID = 0;

    public static final int CHECK_YES = 1;

    public static final int CHECK_NO = -1;

    public static final int CHECK_EMPTY = 0;

    private ArrayMap<Object, Object> objectsMap = new ArrayMap<Object, Object>();

    private Attribute[] attributeVector;

    private IInferencePanel inferencePanel;

    protected IconController iconController = IconController.getInstance();

    private AttributesTree attributesTree;

    private ResourceBundle resource = ResourceBundle.getBundle("unbbayes" + ".datamining.gui.resources.GuiResource");

    public AttributesTree() {
        attributesTree = this;
        setShowsRootHandles(true);
        setSelectionModel(null);
        setRootVisible(true);
        this.setAutoscrolls(true);
        setCellRenderer(new CnmTreeCellRenderer());
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                cnmTreeMouseClicked(evt);
            }
        });
        setEditable(true);
    }

    /**
	 * Builds a new inference tree.
	 *
	 * @param attributeVector an array with all the attributes of the trainning set.
	 * @param classIndex the index of the class attribute.
	 */
    public AttributesTree(Attribute[] attributeVector, int classIndex) {
        this();
        setAttributes(attributeVector, classIndex);
    }

    /**
	 * Blocks any intermediary node (no leaf node) of being edited.
	 * 
	 * @param path The current node to be edited.
	 */
    public boolean isPathEditable(TreePath path) {
        if (isEditable()) {
            return getModel().isLeaf(path.getLastPathComponent());
        }
        return false;
    }

    /**
	 * Used to set the controller of this class.
	 *
	 * @param inferencePanel the controller.
	 */
    public void setController(IInferencePanel inferencePanel) {
        this.inferencePanel = inferencePanel;
    }

    /**
	 * Used to set the attributes of the trainning set.
	 *
	 * @param attributeVector an array with all the attributes of the trainning set.
	 * @param classIndex the index of the class attribute.
	 */
    public void setAttributes(Attribute[] attributeVector, int classIndex) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
        if (attributeVector == null) {
            this.attributeVector = null;
            root.removeAllChildren();
            objectsMap.clear();
            return;
        }
        if (attributeVector.equals(this.attributeVector)) {
            return;
        }
        this.attributeVector = attributeVector;
        root.removeAllChildren();
        objectsMap.clear();
        DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode());
        model.addTreeModelListener(new MyTreeModelListener());
        this.setModel(model);
        root = (DefaultMutableTreeNode) getModel().getRoot();
        int size = attributeVector.length;
        for (int att = 0; att < size; att++) {
            if (att == classIndex) {
                continue;
            }
            Attribute attribute = attributeVector[att];
            DefaultMutableTreeNode treeNode;
            treeNode = new DefaultMutableTreeNode(attribute.getAttributeName());
            objectsMap.put(treeNode, attribute);
            root.add(treeNode);
            if (attribute.isNominal()) {
                int numStates = attribute.numValues();
                numStates = attribute.numValues();
                for (byte j = 0; j < numStates; j++) {
                    DefaultMutableTreeNode stateNode;
                    stateNode = new DefaultMutableTreeNode(attribute.value(j));
                    treeNode.add(stateNode);
                    objectsMap.put(stateNode, new StateObject(attribute, j, CHECK_EMPTY));
                }
            } else {
                String text = resource.getString("clickToChange");
                DefaultMutableTreeNode realNode;
                realNode = new DefaultMutableTreeNode(text);
                treeNode.add(realNode);
                objectsMap.put(realNode, new TextInputObject(attribute));
            }
        }
    }

    /**
	 * Returns the instance inserted on the tree by the user.
	 *
	 * @return the instance inserted on the tree by the user.
	 */
    public Instance getInstance() {
        ArrayList keys = objectsMap.getKeys();
        int keysSize = keys.size();
        Instance instance = new Instance(attributeVector.length);
        for (int i = 0; i < attributeVector.length; i++) {
            instance.setMissing(i);
        }
        for (int i = 0; i < keysSize; i++) {
            Object obj = objectsMap.get(keys.get(i));
            if (obj instanceof StateObject) {
                StateObject state = (StateObject) obj;
                int check = state.getCheck();
                if (check == CHECK_YES) {
                    instance.setValue(state.getAttribute().getIndex(), state.getAttributeValue());
                }
            } else if (obj instanceof TextInputObject) {
                TextInputObject inputText = (TextInputObject) obj;
                instance.setValue(inputText.getAttribute().getIndex(), inputText.getAttributeValue());
            }
        }
        return instance;
    }

    private void cnmTreeMouseClicked(MouseEvent evt) {
        TreePath clickedPath = getPathForLocation(evt.getX(), evt.getY());
        if (clickedPath == null) {
            return;
        }
        DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) (clickedPath.getLastPathComponent());
        if (clickedNode == null || !clickedNode.isLeaf()) {
            return;
        }
        Object obj = objectsMap.get(clickedNode);
        if ((obj instanceof StateObject)) {
            stateNodeClicked(evt, clickedNode, obj);
        } else {
            startEditingAtPath(clickedPath);
        }
    }

    private void stateNodeClicked(MouseEvent evt, DefaultMutableTreeNode clickedNode, Object obj) {
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) clickedNode.getParent();
        Enumeration childrenEnum = parentNode.children();
        StateObject yesChecked = null;
        ArrayList<Object> noCheckeds = new ArrayList<Object>(), emptyCheckeds = new ArrayList<Object>();
        while (childrenEnum.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) childrenEnum.nextElement();
            if (!child.equals(clickedNode)) {
                if (((StateObject) objectsMap.get(child)).getCheck() == CHECK_YES) {
                    yesChecked = (StateObject) objectsMap.get(child);
                } else if (((StateObject) objectsMap.get(child)).getCheck() == CHECK_NO) {
                    noCheckeds.add(objectsMap.get(child));
                } else {
                    emptyCheckeds.add(objectsMap.get(child));
                }
            }
        }
        if (SwingUtilities.isLeftMouseButton(evt)) {
            if (((StateObject) obj).getCheck() == CHECK_YES) {
                ((StateObject) obj).setCheck(CHECK_EMPTY);
                for (int i = 0; i < noCheckeds.size(); i++) {
                    ((StateObject) noCheckeds.get(i)).setCheck(CHECK_EMPTY);
                }
            } else {
                ((StateObject) obj).setCheck(CHECK_YES);
                if (yesChecked != null) {
                    yesChecked.setCheck(CHECK_NO);
                }
                for (int i = 0; i < emptyCheckeds.size(); i++) {
                    ((StateObject) emptyCheckeds.get(i)).setCheck(CHECK_NO);
                }
            }
        }
        if (SwingUtilities.isRightMouseButton(evt)) {
            if (((StateObject) obj).getCheck() == CHECK_NO) {
                ((StateObject) obj).setCheck(CHECK_EMPTY);
                if (yesChecked != null) {
                    yesChecked.setCheck(CHECK_EMPTY);
                }
            } else if (noCheckeds.size() < (parentNode.getChildCount() - 1)) {
                ((StateObject) obj).setCheck(CHECK_NO);
                if (noCheckeds.size() == (parentNode.getChildCount() - 2)) {
                    ((StateObject) emptyCheckeds.get(0)).setCheck(CHECK_YES);
                }
            }
        }
        repaint();
        inferencePanel.printSelectedAttributes(this.getInstance());
    }

    /**
	 * Expand the tree nodes.
	 *
	 * @see JTree
	 */
    public void expandTree() {
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }

    /**
	 * Collapse the tree nodes.
	 *
	 * @see JTree
	 */
    public void collapseTree() {
        for (int i = 0; i < getRowCount(); i++) {
            collapseRow(i);
        }
    }

    private class StateObject {

        private Attribute attribute;

        private byte attributeValue = -1;

        private int check = CHECK_EMPTY;

        public StateObject(Attribute attribute, byte attributeValue, int check) {
            this.attribute = attribute;
            this.attributeValue = attributeValue;
            this.check = check;
        }

        public void setAttributeValue(byte attributeValue) {
            this.attributeValue = attributeValue;
        }

        public void setCheck(int check) {
            this.check = check;
        }

        public byte getAttributeValue() {
            return attributeValue;
        }

        public int getCheck() {
            return check;
        }

        public Attribute getAttribute() {
            return attribute;
        }
    }

    private class TextInputObject {

        private Attribute attribute;

        float attributeValue;

        public TextInputObject(Attribute attribute) {
            this.attribute = attribute;
        }

        public void setAttributeValue(float attributeValue) {
            this.attributeValue = attributeValue;
        }

        public float getAttributeValue() {
            return attributeValue;
        }

        public Attribute getAttribute() {
            return attribute;
        }
    }

    private class CnmTreeCellRenderer extends DefaultTreeCellRenderer {

        /** Serialization runtime version number */
        private static final long serialVersionUID = 0;

        ImageIcon yesIcon = iconController.getYesStateIcon();

        ImageIcon noIcon = iconController.getNoStateIcon();

        ImageIcon emptyIcon = iconController.getEmptyStateIcon();

        ImageIcon evidenciasIcon = iconController.getMoreIcon();

        ImageIcon folderSmallIcon = iconController.getFolderSmallIcon();

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
            if (leaf) {
                Object obj = objectsMap.get(treeNode);
                if (obj instanceof StateObject) {
                    StateObject stateObject = (StateObject) obj;
                    int check = stateObject.getCheck();
                    setIcon((check == CHECK_YES) ? yesIcon : ((check == CHECK_NO) ? noIcon : emptyIcon));
                }
            } else {
                Object obj = objectsMap.get(treeNode);
                if (obj instanceof Attribute) {
                    setIcon(evidenciasIcon);
                }
                this.setOpenIcon(folderSmallIcon);
                this.setClosedIcon(folderSmallIcon);
            }
            return this;
        }
    }

    private class MyTreeModelListener implements TreeModelListener {

        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());
            node = (DefaultMutableTreeNode) node.getChildAt(0);
            Object obj = objectsMap.get(node);
            if (obj instanceof TextInputObject) {
                TextInputObject inputText = (TextInputObject) obj;
                float value = Float.parseFloat(node.toString());
                inputText.setAttributeValue(value);
                inferencePanel.printSelectedAttributes(attributesTree.getInstance());
            }
        }

        public void treeNodesInserted(TreeModelEvent e) {
        }

        public void treeNodesRemoved(TreeModelEvent e) {
        }

        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
}
