package com.agentfactory.debugger.communicator;

import com.agentfactory.platform.core.Agent;
import com.agentfactory.platform.mts.Message;
import com.agentfactory.platform.mts.StringMessage;
import com.agentfactory.platform.util.Logger;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author  remcollier
 */
public class CommunicatorTool extends javax.swing.JInternalFrame implements Observer {

    public static final int CLASS_LOG_LEVEL = Logger.DEFAULT_LEVEL;

    private CommunicatorAgent communicator;

    private Conversation conversation;

    /** Creates new form CommunicatorTool2 */
    public CommunicatorTool(CommunicatorAgent communicator, Conversation conversation) {
        this.communicator = communicator;
        this.conversation = conversation;
        conversation.addObserver(this);
        initComponents();
        setTitle("Conversation: " + conversation.getId());
        DefaultComboBoxModel model = (DefaultComboBoxModel) receiver.getModel();
        model.removeAllElements();
        model.setSelectedItem(null);
        Iterator it = communicator.getReceivers().iterator();
        while (it.hasNext()) {
            model.addElement(((Agent) it.next()).getAgentID());
        }
    }

    public void update(Observable source, Object event) {
        if (event instanceof Message) {
            DefaultListModel model = (DefaultListModel) messages.getModel();
            model.addElement(event);
        }
    }

    private void initComponents() {
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        performative = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        receivers = new javax.swing.JList();
        jPanel6 = new javax.swing.JPanel();
        receiver = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        content = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        messages = new javax.swing.JList();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Communicator");
        setPreferredSize(new java.awt.Dimension(300, 150));
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));
        jPanel8.setLayout(new java.awt.BorderLayout());
        jLabel1.setText("Performative:");
        jPanel8.add(jLabel1, java.awt.BorderLayout.WEST);
        jPanel4.add(jPanel8);
        performative.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "inform", "request", "cfp", "confirm", "cancel", "accept-proposal", "reject-proposal" }));
        jPanel4.add(performative);
        jPanel9.setLayout(new java.awt.BorderLayout());
        jLabel2.setText("Receivers:");
        jPanel9.add(jLabel2, java.awt.BorderLayout.WEST);
        jPanel4.add(jPanel9);
        jPanel5.setLayout(new java.awt.BorderLayout());
        receivers.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(receivers);
        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        jPanel6.setLayout(new java.awt.BorderLayout());
        receiver.setModel(new DefaultComboBoxModel());
        jPanel6.add(receiver, java.awt.BorderLayout.CENTER);
        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton2);
        jButton3.setText("Remove");
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton3);
        jPanel6.add(jPanel7, java.awt.BorderLayout.EAST);
        jPanel5.add(jPanel6, java.awt.BorderLayout.SOUTH);
        jPanel4.add(jPanel5);
        jPanel3.setLayout(new java.awt.BorderLayout());
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Content:");
        jPanel3.add(jLabel3, java.awt.BorderLayout.WEST);
        jPanel4.add(jPanel3);
        jPanel4.add(content);
        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jPanel1.add(jPanel2, java.awt.BorderLayout.SOUTH);
        jSplitPane1.setRightComponent(jPanel1);
        messages.setModel(new DefaultListModel());
        jScrollPane3.setViewportView(messages);
        jSplitPane1.setLeftComponent(jScrollPane3);
        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        StringMessage message = StringMessage.newInstance();
        message.setPerformative((String) performative.getSelectedItem());
        message.setSender(communicator.getAgentID());
        DefaultListModel model = (DefaultListModel) receivers.getModel();
        for (int i = 0; i < model.size(); i++) {
            message.getReceivers().add(model.get(i));
        }
        String data = content.getText();
        message.setContent(data);
        message.setContentLength(data.length());
        message.setLanguage("AFAPL");
        message.setConversationId(conversation.getId());
        System.out.println("sending: " + message.toFIPAString());
        communicator.getMessageQueue().sendMessage(message);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        DefaultListModel model = (DefaultListModel) receivers.getModel();
        if (model.contains(receiver.getSelectedItem())) {
            JOptionPane.showMessageDialog(receiver, "Receiver Already Added");
        } else {
            model.addElement(receiver.getSelectedItem());
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        DefaultListModel model = (DefaultListModel) receivers.getModel();
        if (model.contains(receiver.getSelectedItem())) {
            model.removeElement(receiver.getSelectedItem());
        } else {
            JOptionPane.showMessageDialog(receiver, "Receiver Not In List");
        }
    }

    private javax.swing.JTextField content;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JList messages;

    private javax.swing.JComboBox performative;

    private javax.swing.JComboBox receiver;

    private javax.swing.JList receivers;
}
