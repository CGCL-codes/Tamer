package unbbayes.datamining.gui.metaphor;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import unbbayes.prs.bn.*;
import unbbayes.util.*;

/**
 * @author M�rio Henrique Paes Vieira
 * @version 1.0
 */
public class MetaphorTree extends JTree {

    private class StateObject {

        private ProbabilisticNode node;

        private int stateIndex = -1;

        private int check = CHECK_EMPTY;

        public StateObject(ProbabilisticNode node, int stateIndex, int check) {
            this.node = node;
            this.stateIndex = stateIndex;
            this.check = check;
        }

        public int getStateIndex() {
            return stateIndex;
        }

        public void setStateIndex(int stateIndex) {
            this.stateIndex = stateIndex;
        }

        public int getCheck() {
            return check;
        }

        public void setCheck(int check) {
            this.check = check;
        }

        public ProbabilisticNode getProbabilisticNode() {
            return node;
        }
    }

    private class MetaphorTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {

        ImageIcon yesIcon = new ImageIcon(getClass().getResource("/icons/yes-state.gif"));

        ImageIcon noIcon = new ImageIcon(getClass().getResource("/icons/no-state.gif"));

        ImageIcon emptyIcon = new ImageIcon(getClass().getResource("/icons/empty-state.gif"));

        ImageIcon evidenciasIcon = new ImageIcon(getClass().getResource("/icons/more.gif"));

        ImageIcon folderSmallIcon = new ImageIcon(getClass().getResource("/icons/folder-small.gif"));

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
                if (obj instanceof Node) {
                    setIcon(evidenciasIcon);
                }
                this.setOpenIcon(folderSmallIcon);
                this.setClosedIcon(folderSmallIcon);
            }
            return this;
        }
    }

    public static final int CHECK_YES = 1;

    public static final int CHECK_NO = -1;

    public static final int CHECK_EMPTY = 0;

    private ProbabilisticNetwork net = null;

    private boolean showProbability = false;

    private ArrayMap objectsMap = new ArrayMap();

    private NumberFormat nf;

    protected MetaphorTree() {
        setShowsRootHandles(true);
        setSelectionModel(null);
        setRootVisible(false);
        this.setAutoscrolls(true);
        setCellRenderer(new MetaphorTreeCellRenderer());
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                methaphorTreeMouseClicked(evt);
            }
        });
        nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(4);
    }

    public MetaphorTree(ProbabilisticNetwork net) {
        this(net, false);
    }

    public MetaphorTree(ProbabilisticNetwork net, boolean showProbability) {
        this();
        this.showProbability = showProbability;
        setProbabilisticNetwork(net);
    }

    public void setProbabilisticNetwork(ProbabilisticNetwork net) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
        if (net != null) {
            if (!net.equals(this.net)) {
                this.net = net;
                root.removeAllChildren();
                objectsMap.clear();
                DefaultTreeModel model = new DefaultTreeModel((DefaultMutableTreeNode) net.getHierarchicTree().getModel().getRoot());
                this.setModel(model);
                root = (DefaultMutableTreeNode) getModel().getRoot();
                NodeList nos = net.getDescriptionNodes();
                int size = nos.size();
                for (int i = 0; i < size; i++) {
                    Node node = (Node) nos.get(i);
                    DefaultMutableTreeNode treeNode = findUserObject(node.getDescription(), root);
                    if (treeNode != null) {
                        objectsMap.put(treeNode, node);
                        int statesSize = node.getStatesSize();
                        for (int j = 0; j < statesSize; j++) {
                            DefaultMutableTreeNode stateNode = new DefaultMutableTreeNode(node.getStateAt(j) + (showProbability ? " " + nf.format(((TreeVariable) node).getMarginalAt(j) * 100.0) + "%" : ""));
                            treeNode.add(stateNode);
                            objectsMap.put(stateNode, new StateObject((ProbabilisticNode) node, j, CHECK_EMPTY));
                        }
                    } else {
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(node.getDescription());
                        objectsMap.put(newNode, node);
                        root.add(newNode);
                    }
                }
            }
        } else {
            this.net = null;
            root.removeAllChildren();
            objectsMap.clear();
        }
    }

    private DefaultMutableTreeNode findUserObject(String treeNode, DefaultMutableTreeNode root) {
        Enumeration e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.getUserObject().toString().equals(treeNode)) return node;
        }
        return null;
    }

    public ProbabilisticNetwork getProbabilisticNetwork() {
        return net;
    }

    public void setShowProbability(boolean showProbability) {
        if (showProbability != this.showProbability) {
            this.showProbability = showProbability;
            ProbabilisticNetwork temp = net;
            setProbabilisticNetwork(null);
            setProbabilisticNetwork(temp);
        }
    }

    public boolean getShowProbability() {
        return showProbability;
    }

    public void propagate() {
        try {
            net.initialize();
            int count = getRowCount();
            for (int i = 0; i < count; i++) {
                TreePath path = getPathForRow(i);
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                Object obj = objectsMap.get(treeNode);
                if (obj instanceof StateObject) {
                    StateObject stateObject = (StateObject) obj;
                    if (stateObject.getCheck() == CHECK_YES) {
                        ProbabilisticNode node = stateObject.getProbabilisticNode();
                        node.addFinding(stateObject.getStateIndex());
                    }
                }
            }
            net.updateEvidences();
        } catch (Exception e) {
        }
    }

    private void methaphorTreeMouseClicked(java.awt.event.MouseEvent evt) {
        TreePath clickedPath = getPathForLocation(evt.getX(), evt.getY());
        if (clickedPath != null) {
            DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) (clickedPath.getLastPathComponent());
            if (clickedNode != null && clickedNode.isLeaf()) {
                Object obj = objectsMap.get(clickedNode);
                if (obj instanceof StateObject) {
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) clickedNode.getParent();
                    Enumeration childrenEnum = parentNode.children();
                    StateObject yesChecked = null;
                    ArrayList noCheckeds = new ArrayList(), emptyCheckeds = new ArrayList();
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
                }
            }
        }
    }

    /**
     *  Expande todos os n�s da �rvore.
     *
     * @see            JTree
     */
    public void expandTree() {
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }

    /**
     *  Retrai todos os n�s da �rvore.
     *
     * @see            JTree
     */
    public void collapseTree() {
        for (int i = 0; i < getRowCount(); i++) {
            collapseRow(i);
        }
    }

    /**
     * Modifica o formato de n�meros
     *
     * @param local localidade do formato de n�meros.
     */
    public void setNumberFormat(Locale local) {
        nf = NumberFormat.getInstance(local);
    }
}
