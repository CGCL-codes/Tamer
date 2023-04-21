package org.openscience.jmol;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JScrollPane;
import java.util.Vector;

public class MeasurementList extends JDialog {

    /** List of listeners */
    protected EventListenerList listenerList = new EventListenerList();

    private DisplayPanel display;

    private static JmolResourceHandler jrh;

    protected DefaultMutableTreeNode top;

    protected ListNode distances, angles, dihedrals;

    protected DefaultTreeModel treeModel;

    protected JTree tree;

    protected JButton xButton = new JButton("Delete Measurement");

    private Vector distanceList = new Vector(10);

    private Vector angleList = new Vector(10);

    private Vector dihedralList = new Vector(10);

    private CDistanceAction cdistanceAction = new CDistanceAction();

    private CAngleAction cangleAction = new CAngleAction();

    private CDihedralAction cdihedralAction = new CDihedralAction();

    private CMeasureAction cmeasureAction = new CMeasureAction();

    private ViewMListAction viewmlistAction = new ViewMListAction();

    private Hashtable commands;

    static {
        jrh = new JmolResourceHandler("MeasurementList");
    }

    /**
     * Constructor 
     *
     * @param f the parent frame
     * @param dp the DisplayPanel in which the animation will take place
     */
    public MeasurementList(JFrame f, DisplayPanel dp) {
        super(f, "Measurement List", false);
        this.display = dp;
        commands = new Hashtable();
        Action[] actions = getActions();
        for (int i = 0; i < actions.length; i++) {
            Action a = actions[i];
            commands.put(a.getValue(Action.NAME), a);
        }
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JPanel mPanel = new JPanel();
        mPanel.setLayout(new BorderLayout());
        top = new DefaultMutableTreeNode(jrh.getString("mLabel"));
        treeModel = new DefaultTreeModel(top);
        distances = new ListNode(jrh.getString("distanceLabel"), distanceList);
        angles = new ListNode(jrh.getString("angleLabel"), angleList);
        dihedrals = new ListNode(jrh.getString("dihedralLabel"), dihedralList);
        treeModel.insertNodeInto(distances, top, top.getChildCount());
        treeModel.insertNodeInto(angles, top, top.getChildCount());
        treeModel.insertNodeInto(dihedrals, top, top.getChildCount());
        tree = new JTree(treeModel);
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node == null) return;
                Object nodeInfo = node.getUserObject();
                System.out.println(nodeInfo.toString());
                if (node.isLeaf()) {
                    if (nodeInfo.toString().equalsIgnoreCase("Empty")) xButton.setEnabled(false); else xButton.setEnabled(true);
                } else {
                    xButton.setEnabled(false);
                }
            }
        });
        tree.putClientProperty("JTree.lineStyle", "Angled");
        JScrollPane treeView = new JScrollPane(tree);
        mPanel.add(treeView, BorderLayout.CENTER);
        container.add(mPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        xButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DeletePressed();
            }
        });
        buttonPanel.add(xButton);
        xButton.setEnabled(false);
        JButton dismiss = new JButton("Dismiss");
        dismiss.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        buttonPanel.add(dismiss);
        getRootPane().setDefaultButton(xButton);
        container.add(buttonPanel);
        addWindowListener(new MeasurementListWindowListener());
        getContentPane().add(container);
        pack();
        centerDialog();
    }

    public void updateTree() {
        distances.update();
        angles.update();
        dihedrals.update();
        treeModel.reload(top);
        fireMlistChanged(new MeasurementListEvent(this));
    }

    public Vector getDistanceList() {
        return distanceList;
    }

    public Vector getAngleList() {
        return angleList;
    }

    public Vector getDihedralList() {
        return dihedralList;
    }

    public void addDistance(Distance d) {
        distanceList.addElement(d);
        distances.update();
        treeModel.reload(distances);
        fireMlistChanged(new MeasurementListEvent(this));
        display.repaint();
    }

    public void addAngle(Angle a) {
        angleList.addElement(a);
        angles.update();
        treeModel.reload(angles);
        fireMlistChanged(new MeasurementListEvent(this));
        display.repaint();
    }

    public void addDihedral(Dihedral d) {
        dihedralList.addElement(d);
        dihedrals.update();
        treeModel.reload(dihedrals);
        fireMlistChanged(new MeasurementListEvent(this));
        display.repaint();
    }

    public void clear() {
        distanceList.removeAllElements();
        angleList.removeAllElements();
        dihedralList.removeAllElements();
        distances.update();
        angles.update();
        dihedrals.update();
        treeModel.reload(top);
        fireMlistChanged(new MeasurementListEvent(this));
    }

    public void clearDistanceList() {
        distanceList.removeAllElements();
        distances.update();
        treeModel.reload(distances);
        fireMlistChanged(new MeasurementListEvent(this));
        display.repaint();
    }

    public void clearAngleList() {
        angleList.removeAllElements();
        angles.update();
        treeModel.reload(angles);
        fireMlistChanged(new MeasurementListEvent(this));
        display.repaint();
    }

    public void clearDihedralList() {
        dihedralList.removeAllElements();
        dihedrals.update();
        treeModel.reload(dihedrals);
        fireMlistChanged(new MeasurementListEvent(this));
        display.repaint();
    }

    public boolean deleteMatchingDistance(int i1, int i2) {
        for (Enumeration e = distanceList.elements(); e.hasMoreElements(); ) {
            Distance d = (Distance) e.nextElement();
            if (d.sameAs(i1, i2)) {
                distanceList.removeElement(d);
                distances.update();
                treeModel.reload(distances);
                fireMlistChanged(new MeasurementListEvent(this));
                display.repaint();
                return true;
            }
        }
        return false;
    }

    public boolean deleteMatchingAngle(int i1, int i2, int i3) {
        for (Enumeration e = angleList.elements(); e.hasMoreElements(); ) {
            Angle a = (Angle) e.nextElement();
            if (a.sameAs(i1, i2, i3)) {
                angleList.removeElement(a);
                angles.update();
                treeModel.reload(angles);
                fireMlistChanged(new MeasurementListEvent(this));
                display.repaint();
                return true;
            }
        }
        return false;
    }

    public boolean deleteMatchingDihedral(int i1, int i2, int i3, int i4) {
        for (Enumeration e = dihedralList.elements(); e.hasMoreElements(); ) {
            Dihedral dh = (Dihedral) e.nextElement();
            if (dh.sameAs(i1, i2, i3, i4)) {
                dihedralList.removeElement(dh);
                dihedrals.update();
                treeModel.reload(dihedrals);
                fireMlistChanged(new MeasurementListEvent(this));
                display.repaint();
                return true;
            }
        }
        return false;
    }

    protected void centerDialog() {
        Dimension screenSize = this.getToolkit().getScreenSize();
        Dimension size = this.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        this.setLocation(x, y);
    }

    public void close() {
        this.setVisible(false);
        enableActions();
    }

    public void DeletePressed() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) return;
        Object nodeInfo = node.getUserObject();
        String mType = nodeInfo.getClass().getName();
        if (mType.endsWith("Distance")) {
            Distance d = (Distance) nodeInfo;
            int[] at = d.getAtomList();
            boolean b = deleteMatchingDistance(at[0], at[1]);
        } else {
            if (mType.endsWith("Angle")) {
                Angle a = (Angle) nodeInfo;
                int[] at = a.getAtomList();
                boolean b = deleteMatchingAngle(at[0], at[1], at[2]);
            } else {
                if (mType.endsWith("Dihedral")) {
                    Dihedral dh = (Dihedral) nodeInfo;
                    int[] at = dh.getAtomList();
                    boolean b = deleteMatchingDihedral(at[0], at[1], at[2], at[3]);
                }
            }
        }
    }

    /**
     * Add a listener to the list that's notified each time a change
     * to the MeasurementList occurs.
     *
     * @param   l               the MeasurementListListener
     */
    public void addMeasurementListListener(MeasurementListListener l) {
        listenerList.add(MeasurementListListener.class, l);
    }

    /**
     * Remove a listener from the list that's notified each time a
     * change to the MeasurementList occurs.
     *
     * @param   l               the MeasurementListListener
     */
    public void removeMeasurementListListener(MeasurementListListener l) {
        listenerList.remove(MeasurementListListener.class, l);
    }

    /**
     * Forward the given notification event to all
     * MeasurementListListeners that registered themselves as
     * listeners for this MeasurementList
     * @see #addMeasurementListListener
     * @see MeasurementListEvent
     * @see EventListenerList */
    public void fireMlistChanged(MeasurementListEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MeasurementListListener.class) {
                ((MeasurementListListener) listeners[i + 1]).mlistChanged(e);
            }
        }
    }

    public void enableActions() {
        cdistanceAction.setEnabled(true);
        cangleAction.setEnabled(true);
        cdihedralAction.setEnabled(true);
        cmeasureAction.setEnabled(true);
        viewmlistAction.setEnabled(true);
    }

    public void disableActions() {
        cdistanceAction.setEnabled(false);
        cangleAction.setEnabled(false);
        cdihedralAction.setEnabled(false);
        cmeasureAction.setEnabled(false);
        viewmlistAction.setEnabled(false);
    }

    class CDistanceAction extends AbstractAction {

        public CDistanceAction() {
            super("cdistance");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            clearDistanceList();
        }
    }

    class CAngleAction extends AbstractAction {

        public CAngleAction() {
            super("cangle");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            clearAngleList();
        }
    }

    class CDihedralAction extends AbstractAction {

        public CDihedralAction() {
            super("cdihedral");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            clearDihedralList();
        }
    }

    class CMeasureAction extends AbstractAction {

        public CMeasureAction() {
            super("cmeasure");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            clearDistanceList();
            clearAngleList();
            clearDihedralList();
        }
    }

    class ViewMListAction extends AbstractAction {

        public ViewMListAction() {
            super("viewmlist");
            this.setEnabled(true);
        }

        public void actionPerformed(ActionEvent e) {
            updateTree();
            show();
        }
    }

    public Action[] getActions() {
        Action[] defaultActions = { cdistanceAction, cangleAction, cdihedralAction, cmeasureAction, viewmlistAction };
        return defaultActions;
    }

    protected Action getAction(String cmd) {
        return (Action) commands.get(cmd);
    }

    class MeasurementListWindowListener extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            close();
        }
    }
}
