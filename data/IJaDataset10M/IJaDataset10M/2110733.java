package greybird.gui;

import greybird.gui.form.About;
import greybird.gui.form.Preferences;
import greybird.gui.interfaces.NotifiableItem;
import greybird.gui.interfaces.DoubleClickableItem;
import greybird.gui.tree.ServiceTree;
import greybird.gui.tree.TreeNode;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * MainWindow is the main JFrame in Project Greybird
 */
public class MainWindow extends javax.swing.JFrame {

    private ServiceTree treeNodes;

    /** Creates new form MainWindow */
    public MainWindow() {
        this.treeNodes = new ServiceTree("Services", this);
        initComponents();
        this.taskTabbedPane.addMouseListener(new TabMenuListener(this.taskTabbedPane));
        super.setLocationRelativeTo(null);
    }

    /**
     * Gets the tree object.
     * @return tree
     */
    public JTree getTree() {
        return this.tree;
    }

    /**
     * Gets the tabbed pane object.
     * @return tabbed pane
     */
    public JTabbedPane getTabbedPane() {
        return this.taskTabbedPane;
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree(this.treeNodes);
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        jmxButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        sqlScriptButton = new javax.swing.JButton();
        createTableButton = new javax.swing.JButton();
        dropTableButton = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        homeButton = new javax.swing.JButton();
        taskTabbedPane = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenu6 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Project Greybird");
        tree.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tree);
        jTextArea1.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Messagelog for client. If clicked & selected service in tree supports JMX, redirect attention to JMXPanel, Messages, setPulsatingBorder for textArea");
        jTextArea1.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTextArea1);
        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);
        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/greybird/gui/icons/toolbar/standard/add.png")));
        addButton.setToolTipText("Add DBMS");
        addButton.setBorder(null);
        addButton.setFocusable(false);
        addButton.setOpaque(false);
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(addButton);
        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/greybird/gui/icons/toolbar/standard/remove.png")));
        removeButton.setToolTipText("Remove DBMS");
        removeButton.setBorder(null);
        removeButton.setFocusable(false);
        removeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeButton.setOpaque(false);
        removeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        removeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(removeButton);
        jmxButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/greybird/gui/icons/toolbar/standard/jmx.png")));
        jmxButton.setToolTipText("Properties");
        jmxButton.setBorder(null);
        jmxButton.setFocusable(false);
        jmxButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jmxButton.setOpaque(false);
        jmxButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jmxButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmxButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(jmxButton);
        jToolBar1.add(jSeparator2);
        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/greybird/gui/icons/toolbar/standard/start.png")));
        startButton.setToolTipText("Start");
        startButton.setBorder(null);
        startButton.setFocusable(false);
        startButton.setOpaque(false);
        jToolBar1.add(startButton);
        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/greybird/gui/icons/toolbar/standard/stop.png")));
        stopButton.setToolTipText("Stop");
        stopButton.setBorder(null);
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setOpaque(false);
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(stopButton);
        jToolBar1.add(jSeparator3);
        sqlScriptButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/greybird/gui/icons/toolbar/standard/sqlscript.png")));
        sqlScriptButton.setToolTipText("Run SQL-script");
        sqlScriptButton.setBorder(null);
        sqlScriptButton.setFocusable(false);
        sqlScriptButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sqlScriptButton.setOpaque(false);
        sqlScriptButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        sqlScriptButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sqlScriptButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(sqlScriptButton);
        createTableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/greybird/gui/icons/toolbar/standard/createtable.png")));
        createTableButton.setToolTipText("Create table");
        createTableButton.setBorder(null);
        createTableButton.setFocusable(false);
        createTableButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        createTableButton.setOpaque(false);
        createTableButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(createTableButton);
        dropTableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/greybird/gui/icons/toolbar/standard/droptable.png")));
        dropTableButton.setToolTipText("Drop table");
        dropTableButton.setBorder(null);
        dropTableButton.setFocusable(false);
        dropTableButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        dropTableButton.setOpaque(false);
        dropTableButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(dropTableButton);
        jToolBar1.add(jSeparator4);
        jToolBar1.add(jSeparator6);
        homeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/greybird/gui/icons/toolbar/standard/home.png")));
        homeButton.setToolTipText("Home of Apache Derby");
        homeButton.setBorder(null);
        homeButton.setFocusable(false);
        homeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        homeButton.setOpaque(false);
        homeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        homeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(homeButton);
        jMenu1.setText("File");
        jMenuItem5.setText("Exit");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);
        jMenuBar1.add(jMenu1);
        jMenu2.setText("Edit");
        jMenuItem2.setText("Preferences");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);
        jMenuBar1.add(jMenu2);
        jMenu3.setText("Test");
        jMenu5.setText("Pulsating Border");
        jMenuItem3.setText("setPulsatingBorder");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem3);
        jMenuItem4.setText("removePulsatingBorder");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem4);
        jMenu3.add(jMenu5);
        jMenu3.add(jSeparator1);
        jMenu6.setText("Other");
        jMenu3.add(jMenu6);
        jMenuBar1.add(jMenu3);
        jMenu4.setText("Help");
        jMenuItem1.setText("About");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);
        jMenuBar1.add(jMenu4);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(taskTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)).addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(taskTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private void treeMouseClicked(java.awt.event.MouseEvent evt) {
        int row = this.tree.getRowForLocation(evt.getX(), evt.getY());
        this.tree.setSelectionRow(row);
        DefaultMutableTreeNode node;
        try {
            node = (DefaultMutableTreeNode) this.tree.getSelectionPath().getLastPathComponent();
        } catch (NullPointerException e) {
            return;
        }
        switch(evt.getButton()) {
            case MouseEvent.BUTTON3:
                if (node instanceof TreeNode) {
                    TreeNode t = (TreeNode) node;
                    JPopupMenu p = t.getJPopupMenu();
                    if (p != null) {
                        p.show(this.tree, evt.getX(), evt.getY());
                    }
                }
                break;
            case MouseEvent.BUTTON1:
                if (evt.getClickCount() == 2) {
                    if (node instanceof DoubleClickableItem) {
                        ((DoubleClickableItem) node).doubleClicked();
                    }
                }
                break;
        }
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Show ServerForm with: connect to networkserver, create networkserver, create embedded");
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "if(remote) disconnect OR stop service, else stop service");
    }

    private void jmxButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "show JMXPanel if avaliable for selected service in tree");
    }

    private void sqlScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "open file dialog, get selected service in tree, read text to SQL-tool, execute as script?");
    }

    private void homeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://db.apache.org/derby/"));
            } else {
                JOptionPane.showMessageDialog(this, "Sorry, your desktop is noe supported.");
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        About a = new About();
        a.setLocationRelativeTo(this);
        a.setVisible(true);
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        Preferences p = new Preferences();
        p.setLocationRelativeTo(this);
        p.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    private class TabMenuListener extends MouseAdapter implements ActionListener {

        private JTabbedPane pane;

        private JPopupMenu popup;

        private JMenuItem close;

        private int index;

        public TabMenuListener(JTabbedPane pane) {
            this.pane = pane;
            this.popup = new JPopupMenu();
            this.popup.setInvoker(pane);
            this.close = this.popup.add("Close tab");
            this.close.addActionListener(this);
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            this.index = pane.indexAtLocation(evt.getX(), evt.getY());
            if (evt.getButton() == MouseEvent.BUTTON3 && this.index != -1) {
                this.popup.show(this.pane, evt.getX(), evt.getY());
            }
        }

        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource().equals(this.close)) {
                Component c = this.pane.getSelectedComponent();
                if (c instanceof NotifiableItem) {
                    ((NotifiableItem) c).notefyRemoved();
                } else {
                    this.pane.remove(this.index);
                }
            }
        }
    }

    private javax.swing.JButton addButton;

    private javax.swing.JButton createTableButton;

    private javax.swing.JButton dropTableButton;

    private javax.swing.JButton homeButton;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenu jMenu3;

    private javax.swing.JMenu jMenu4;

    private javax.swing.JMenu jMenu5;

    private javax.swing.JMenu jMenu6;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JMenuItem jMenuItem4;

    private javax.swing.JMenuItem jMenuItem5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JToolBar.Separator jSeparator2;

    private javax.swing.JToolBar.Separator jSeparator3;

    private javax.swing.JToolBar.Separator jSeparator4;

    private javax.swing.JToolBar.Separator jSeparator6;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JButton jmxButton;

    private javax.swing.JButton removeButton;

    private javax.swing.JButton sqlScriptButton;

    private javax.swing.JButton startButton;

    private javax.swing.JButton stopButton;

    private javax.swing.JTabbedPane taskTabbedPane;

    private javax.swing.JTree tree;
}
