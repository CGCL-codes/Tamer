package proteintool;

import javax.swing.tree.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class Help extends javax.swing.JFrame {

    public static final String[] string = { "Legal notices", "Getting Started", "Protein Tool Basics", "Protein Analysis Background" };

    public static final String[] string0 = { "License" };

    public static final String[] string2 = { "Creating a new project", "Aligning Sequences" };

    public static final String[] string3 = { "Introduction", "Developers", "Motivation" };

    public static final String[] string4 = { "Multi-Sequence Alignments" };

    public static final String HELP_TITLE = "Protein Tool Help";

    private DefaultMutableTreeNode baseNode = null;

    private DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    public Help() {
        super(Help.HELP_TITLE);
        initComponents();
        this.setLocationRelativeTo(null);
        for (int i = 0; i < string.length; i++) {
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(string[i]);
            root.add(treeNode);
            switch(i) {
                case 0:
                    for (int j = 0; j < string0.length; j++) {
                        treeNode.add(new DefaultMutableTreeNode(string0[j]));
                    }
                    break;
                case 1:
                    for (int j = 0; j < string2.length; j++) {
                        treeNode.add(new DefaultMutableTreeNode(string2[j]));
                    }
                    break;
                case 2:
                    for (int j = 0; j < string3.length; j++) {
                        treeNode.add(new DefaultMutableTreeNode(string3[j]));
                    }
                    break;
                case 3:
                    for (int j = 0; j < string4.length; j++) {
                        treeNode.add(new DefaultMutableTreeNode(string4[j]));
                    }
                    break;
                default:
                    break;
            }
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        jTree1.setModel(treeModel);
        this.helpEditorPane.addHyperlinkListener(new EditorPaneHyperLinkListener(this.helpEditorPane));
        this.setStartPage();
        this.helpEditorPane.setEditable(false);
    }

    private void setStartPage() {
        String url = "file:///" + ProteinTool.PROGRAM_LOC + File.separator + "html" + File.separator + "startPage.html";
        try {
            this.helpEditorPane.setPage(url);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void initComponents() {
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        helpEditorPane = new javax.swing.JEditorPane();
        jSplitPane1.setDividerLocation(200);
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE));
        jTabbedPane1.addTab("Contents", jPanel1);
        jLabel1.setText("Find:");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(484, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Search", jPanel2);
        jSplitPane1.setLeftComponent(jTabbedPane1);
        jScrollPane2.setViewportView(helpEditorPane);
        jSplitPane1.setRightComponent(jScrollPane2);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE));
        this.jSplitPane1.setOneTouchExpandable(true);
        pack();
    }

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) evt.getPath().getLastPathComponent();
        String nodeString = node.toString();
        updateTree(nodeString);
    }

    private void updateTree(String nodeSelected) {
        String url = "file:///" + ProteinTool.PROGRAM_LOC + File.separator + "html" + File.separator;
        if (nodeSelected == null) return;
        if (nodeSelected.equalsIgnoreCase("license")) {
            url += "license.htm";
        } else if (nodeSelected.equalsIgnoreCase("Creating a new project")) {
            url += "howto.htm";
        } else if (nodeSelected.equalsIgnoreCase("Aligning sequences")) {
            url += "howto.htm";
        } else if (nodeSelected.equalsIgnoreCase("Introduction")) {
            url += "howto.htm";
        } else if (nodeSelected.equalsIgnoreCase("Developers")) {
            url += "howto.htm";
        } else if (nodeSelected.equalsIgnoreCase("Motivation")) {
            url += "howto.htm";
        } else if (nodeSelected.equalsIgnoreCase("Multi-sequence Alignment")) {
            url += "howto.htm";
        } else if (nodeSelected.equals("")) {
            url += "startPage.html";
        }
        try {
            this.helpEditorPane.setPage(url);
        } catch (Exception e) {
            e.printStackTrace();
            this.helpEditorPane.setText("An error occured while trying to set the page");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                Help help = new Help();
                help.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                help.setVisible(true);
            }
        });
    }

    private javax.swing.JEditorPane helpEditorPane;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTree jTree1;
}
