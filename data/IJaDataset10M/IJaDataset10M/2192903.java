package nsdb.cytoscape.gui;

import cytoscape.util.OpenBrowser;
import java.net.URL;
import javax.swing.event.HyperlinkEvent;
import nsdb.cytoscape.CytoscapeSQL;
import nsdb.cytoscape.networksimdb.CyGraphTool;
import nsdb.cytoscape.util.CytoscapeWrapper;

/**
 *
 * @author mkoenig
 */
@SuppressWarnings("serial")
public class CytoscapeSQLPanel extends javax.swing.JPanel {

    @SuppressWarnings("unused")
    private CytoscapeSQL cytoscapeSQL;

    /** Creates new form CytoscapeSQLPanel */
    public CytoscapeSQLPanel(CytoscapeSQL cytoscapeSQL) {
        this.cytoscapeSQL = cytoscapeSQL;
        initComponents();
        infoTextPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                infoTextPaneHyperlinkUpdate(evt);
            }
        });
    }

    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        infoPane = new javax.swing.JPanel();
        updateButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoTextPane = new javax.swing.JEditorPane();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        commitGraphButton = new javax.swing.JToggleButton();
        loadGraphButton = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        idTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 191, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 497, Short.MAX_VALUE));
        setPreferredSize(new java.awt.Dimension(180, 700));
        updateButton.setText("MouseListener");
        updateButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });
        infoTextPane.setBackground(new java.awt.Color(254, 254, 254));
        infoTextPane.setContentType("text/html");
        infoTextPane.setEditable(false);
        infoTextPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                infoTextPaneHyperlinkUpdate(evt);
            }
        });
        jScrollPane1.setViewportView(infoTextPane);
        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        jLabel2.setText("HepatoNet1");
        javax.swing.GroupLayout infoPaneLayout = new javax.swing.GroupLayout(infoPane);
        infoPane.setLayout(infoPaneLayout);
        infoPaneLayout.setHorizontalGroup(infoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE).addGroup(infoPaneLayout.createSequentialGroup().addGroup(infoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(infoPaneLayout.createSequentialGroup().addContainerGap().addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, infoPaneLayout.createSequentialGroup().addContainerGap().addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(91, Short.MAX_VALUE)));
        infoPaneLayout.setVerticalGroup(infoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPaneLayout.createSequentialGroup().addContainerGap().addComponent(updateButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jTabbedPane1.addTab("info", infoPane);
        jLabel1.setText("CommitGraph");
        jLabel4.setText("LoadGraph");
        jList1.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane3.setViewportView(jList1);
        commitGraphButton.setText("commit");
        commitGraphButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitGraphButtonActionPerformed(evt);
            }
        });
        loadGraphButton.setText("load");
        loadGraphButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadGraphButtonActionPerformed(evt);
            }
        });
        jLabel6.setText("db");
        jLabel7.setText("user");
        jTextField2.setText("jdbc:sqlite:/home/mkoenig/Science/projects/NetworkSimDB/db/netsimdb.sqlite");
        jTextField3.setText("koenig");
        idTextField.setText("10");
        idTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTextFieldActionPerformed(evt);
            }
        });
        jLabel8.setText("id");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE).addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE).addComponent(commitGraphButton, javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7).addComponent(jLabel6)).addGap(50, 50, 50).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jTextField3).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(loadGraphButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE).addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(26, 26, 26)))));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(commitGraphButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(16, 16, 16).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(9, 9, 9).addComponent(loadGraphButton).addGap(36, 36, 36).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(jLabel6).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(jLabel7).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(272, Short.MAX_VALUE)));
        jTabbedPane1.addTab("networkSimDB", jPanel3);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE));
    }

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("updateInformation()");
        CytoscapeSQL.updateInformation();
    }

    private void commitGraphButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void loadGraphButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String strid = this.idTextField.getText();
        System.out.println("Load graph with id: " + strid);
        Long id = Long.parseLong(strid);
        CyGraphTool.loadGraphFromDB(id);
    }

    private void infoTextPaneHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
        URL url = evt.getURL();
        if (url != null) {
            if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                CytoscapeWrapper.setStatusBarMsg(url.toString());
            } else if (evt.getEventType() == HyperlinkEvent.EventType.EXITED) {
                CytoscapeWrapper.clearStatusBar();
            } else if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                OpenBrowser.openURL(url.toString());
            }
        }
    }

    private void idTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JToggleButton commitGraphButton;

    private javax.swing.JTextField idTextField;

    private javax.swing.JPanel infoPane;

    private javax.swing.JEditorPane infoTextPane;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JList jList1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JToggleButton loadGraphButton;

    private javax.swing.JButton updateButton;

    public javax.swing.JEditorPane getInfoTextPane() {
        return this.infoTextPane;
    }
}
