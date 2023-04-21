package com.lowagie.tools.plugins.treeview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeViewInternalFrame extends JInternalFrame implements PropertyChangeListener, IUpdatenodeview {

    static final String PROPERTYFILENAME = "inputfilename";

    AnalyzePDF bla;

    public TreeViewInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
        super(title, resizable, closable, maximizable);
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        jPanel1.setLayout(borderLayout2);
        jPanel2.setLayout(cardLayout2);
        jSplitPane1.setMinimumSize(new Dimension(150, 100));
        jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jTree1.addTreeSelectionListener(new Untitled1_jTree1_treeSelectionAdapter(this));
        jTextPane1.setText("jTextPane1");
        jPanel4.setLayout(borderLayout3);
        jPanel5.setLayout(borderLayout5);
        jLabel1.setText("jLabel1");
        jTree1.setModel(defaultree);
        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);
        this.getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);
        jPanel2.add(jPanel4, "empty");
        jPanel2.add(jPanel5, "values");
        jScrollPane3.setViewportView(jLabel1);
        jScrollPane2.setViewportView(jTextPane1);
        jScrollPane1.setViewportView(jTree1);
        jSplitPane1.add(jPanel2, JSplitPane.RIGHT);
        jSplitPane1.add(jScrollPane1, JSplitPane.LEFT);
        jPanel2.add(jScrollPane2, "text");
        jPanel5.add(jScrollPane3, java.awt.BorderLayout.CENTER);
        jSplitPane1.setDividerLocation(170);
    }

    public void setTreemodel(TreeModel treemodel) {
        jTree1.setModel(treemodel);
    }

    JPanel jPanel1 = new JPanel();

    BorderLayout borderLayout1 = new BorderLayout();

    JSplitPane jSplitPane1 = new JSplitPane();

    JScrollPane jScrollPane1 = new JScrollPane();

    JTree jTree1 = new JTree();

    JPanel jPanel2 = new JPanel();

    BorderLayout borderLayout2 = new BorderLayout();

    BorderLayout borderLayout4 = new BorderLayout();

    CardLayout cardLayout2 = new CardLayout();

    JPanel jPanel4 = new JPanel();

    JTextPane jTextPane1 = new JTextPane();

    JPanel jPanel5 = new JPanel();

    BorderLayout borderLayout3 = new BorderLayout();

    BorderLayout borderLayout5 = new BorderLayout();

    JLabel jLabel1 = new JLabel();

    JScrollPane jScrollPane2 = new JScrollPane();

    JScrollPane jScrollPane3 = new JScrollPane();

    DefaultTreeModel defaultree = new DefaultTreeModel(new DefaultMutableTreeNode());

    /**
   * This method gets called when a bound property is changed.
   *
   * @param evt A PropertyChangeEvent object describing the event source and the property that has
   *   changed.
   * @todo Implement this java.beans.PropertyChangeListener method
   */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyname = evt.getPropertyName();
        if (propertyname == null) {
            return;
        } else if (propertyname.equals(PROPERTYFILENAME)) {
            String filename = (String) evt.getNewValue();
            ProgressDialog blubb;
            blubb = new ProgressDialog(null, "PDF Analysefortschritt", false);
            blubb.show();
            bla = new AnalyzePDF(filename, blubb);
            Timer activitymonitor = new Timer(250, new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    bla.updatecount();
                }
            });
            bla.start();
            activitymonitor.start();
            setTreemodel(bla);
        }
    }

    public void jTree1_valueChanged(TreeSelectionEvent e) {
        String event = e.getClass().toString();
        if (event.equalsIgnoreCase("class javax.swing.event.TreeSelectionEvent")) {
            UpdateableTreeNode selectednode = (UpdateableTreeNode) jTree1.getLastSelectedPathComponent();
            System.out.println("Selected node: " + selectednode);
            if (selectednode != null) {
                selectednode.updateview(this);
            }
        }
    }

    public void showtext(String text) {
        jTextPane1.setText(text);
        cardLayout2.show(jPanel2, "text");
        jPanel2.repaint();
        jTextPane1.repaint();
    }

    public void showempty() {
        cardLayout2.show(jPanel2, "empty");
        jPanel2.repaint();
    }

    public void showvalues(String text) {
        jLabel1.setText(text);
        cardLayout2.show(jPanel2, "values");
    }
}

class Untitled1_jTree1_treeSelectionAdapter implements TreeSelectionListener {

    private TreeViewInternalFrame adaptee;

    Untitled1_jTree1_treeSelectionAdapter(TreeViewInternalFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void valueChanged(TreeSelectionEvent e) {
        adaptee.jTree1_valueChanged(e);
    }
}
