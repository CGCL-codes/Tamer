package uk.ac.imperial.ma.metric.gui;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import uk.ac.imperial.ma.metric.apps.metricGUI.MetricMain;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import uk.ac.imperial.ma.metric.gui.ExtendedGridBagLayout;
import javax.swing.ToolTipManager;
import uk.ac.imperial.ma.metric.nav.NavigationTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.Cursor;
import javax.swing.ProgressMonitor;
import uk.ac.imperial.ma.metric.err.ExceptionProcessor;
import javax.swing.text.Position.Bias;
import javax.swing.tree.TreePath;
import java.util.Vector;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;

public class NavigationPanel extends JPanel implements TreeSelectionListener, MouseListener {

    private static final String NO_CONTENTS_YET = "Building navigation tree, please wait.";

    public MetricMain metricMain;

    private JLabel jlblMessage;

    private JTree jtree;

    private JScrollPane jscrl;

    private ExtendedGridBagLayout egbl;

    public NavigationPanel(MetricMain metricMain) {
        this.metricMain = metricMain;
        jlblMessage = new JLabel(NO_CONTENTS_YET);
        jscrl = new JScrollPane(jlblMessage);
        egbl = new ExtendedGridBagLayout();
        setLayout(egbl);
        egbl.add(jscrl, this, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
    }

    /** 
     * Sets the navigation tree to be used by this instance of the GUI.
     * 
     * @param jtree the instance of <code>JTree</code> which contains the navigation tree.
     */
    public void setNavigationTree(JTree jtree) {
        this.jtree = jtree;
        this.jtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.jtree.addTreeSelectionListener(this);
        this.jtree.addMouseListener(this);
        ToolTipManager.sharedInstance().registerComponent(this.jtree);
        jtree.setCellRenderer(new MetricTreeCellRenderer1(metricMain.LOADER));
        jtree.revalidate();
        jscrl.setViewportView(jtree);
    }

    public void selectAndLoad(NavigationTreeNode ntn) {
        load(ntn);
    }

    private void load(NavigationTreeNode ntn) {
        metricMain.LOADER.setNavigationTreeNodeToLoad(ntn);
        Thread tLoader = new Thread(metricMain.LOADER);
        Thread tMonitor = new Thread(metricMain.TASK_MONITOR);
        metricMain.TASK_MONITOR.setTask(metricMain.LOADER);
        tLoader.start();
        tMonitor.start();
    }

    public void valueChanged(TreeSelectionEvent tse) {
        try {
            NavigationTreeNode ntn = (NavigationTreeNode) jtree.getLastSelectedPathComponent();
            switch(ntn.getType()) {
                case NavigationTreeNode.DOCUMENT:
                case NavigationTreeNode.EXERCISE:
                case NavigationTreeNode.EXPLORATION:
                    load(ntn);
                    break;
                default:
                    break;
            }
        } catch (ClassCastException cce) {
            metricMain.EXCEPTION_PROCESSOR.process(cce, false, ExceptionProcessor.WARNING_DISPLAY, "The valueChanged(TreeSelectionEvent tse) method for the navigation tree has obtained a value that in not a NavigationTreeNode. This is not a problem unless you are sure that it is!");
        } catch (NullPointerException npe) {
        }
    }

    public void mouseClicked(MouseEvent me) {
        switch(me.getButton()) {
            case MouseEvent.BUTTON1:
                break;
            case MouseEvent.BUTTON2:
                break;
            case MouseEvent.BUTTON3:
                ResourcePopupMenu rpm = new ResourcePopupMenu((NavigationTreeNode) jtree.getPathForLocation(me.getX(), me.getY()).getLastPathComponent(), this);
                rpm.show(jtree, me.getX(), me.getY());
                break;
            default:
        }
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {
    }

    public void setNavigationTreeSelectedNode(NavigationTreeNode ntn) {
        Vector vecReversedPath = new Vector();
        TreeNode tnPathNode = ntn;
        while (tnPathNode != null) {
            vecReversedPath.add(tnPathNode);
            tnPathNode = tnPathNode.getParent();
        }
        vecReversedPath.removeElementAt(vecReversedPath.size() - 1);
        Enumeration enumeration = vecReversedPath.elements();
        TreeNode tnPath[] = new TreeNode[vecReversedPath.size()];
        int i = 1;
        while (enumeration.hasMoreElements()) {
            tnPath[vecReversedPath.size() - i] = (TreeNode) enumeration.nextElement();
            i++;
        }
        TreePath treePath = new TreePath(tnPath);
        TreePath treePathParent = treePath.getParentPath();
        metricMain.metricFrame.navViewPanel.setTopic();
        jtree.expandPath(treePathParent);
        jtree.setSelectionPath(treePath);
    }
}
