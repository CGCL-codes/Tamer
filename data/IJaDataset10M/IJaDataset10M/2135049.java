package jrdesktop.server;

import java.net.InetAddress;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import jrdesktop.main;
import jrdesktop.server.rmi.Server;

/**
 * ActiveConnectionsGUI.java
 * @author benbac
 */
public class ActiveConnectionsGUI extends javax.swing.JFrame {

    DefaultListModel listModel = new DefaultListModel();

    /** Creates new form ActiveViewersGUI */
    public ActiveConnectionsGUI() {
        initComponents();
        jList1.setModel(listModel);
        updateList();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButtonClose = new javax.swing.JButton();
        jButtonDisconnect = new javax.swing.JButton();
        jButtonDisconnectAll = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Active Connections");
        setIconImage(new ImageIcon(main.IDLE_ICON).getImage());
        setResizable(false);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Active Connections"));
        jScrollPane1.setViewportView(jList1);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jButtonDisconnect.setText("Disconnect");
        jButtonDisconnect.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisconnectActionPerformed(evt);
            }
        });
        jButtonDisconnectAll.setText("Disconnect all");
        jButtonDisconnectAll.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisconnectAllActionPerformed(evt);
            }
        });
        jButtonRefresh.setText("Refresh");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jButtonDisconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButtonRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButtonDisconnectAll, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE).addComponent(jButtonClose, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))).addGap(26, 26, 26)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonDisconnect).addComponent(jButtonDisconnectAll)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonRefresh).addComponent(jButtonClose)).addContainerGap()));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 247) / 2, (screenSize.height - 380) / 2, 247, 380);
    }

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void jButtonDisconnectActionPerformed(java.awt.event.ActionEvent evt) {
        int index = jList1.getSelectedIndex();
        if (index == -1) return;
        InetAddress inetAddress = (InetAddress) jList1.getSelectedValue();
        if (JOptionPane.showConfirmDialog(this, "Disconnect " + inetAddress.toString() + " ? ", "Confirm Dialog", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION) return;
        Server.removeViewer(index);
        listModel.remove(index);
    }

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        listModel.clear();
        updateList();
    }

    private void jButtonDisconnectAllActionPerformed(java.awt.event.ActionEvent evt) {
        int index = jList1.getModel().getSize();
        if (index == 0) return;
        if (JOptionPane.showConfirmDialog(this, "Disconnect all viewers ? ", "Confirm Dialog", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) Server.disconnectAllViewers();
        listModel.clear();
        updateList();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ActiveConnectionsGUI().setVisible(true);
            }
        });
    }

    public void updateList() {
        if (Server.getViewersCount() == 0) return;
        Object[] objects = Server.getViewersAds().toArray().clone();
        for (int i = 0; i < objects.length; i++) listModel.addElement(objects[i]);
    }

    private javax.swing.JButton jButtonClose;

    private javax.swing.JButton jButtonDisconnect;

    private javax.swing.JButton jButtonDisconnectAll;

    private javax.swing.JButton jButtonRefresh;

    private javax.swing.JList jList1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;
}
