package regnumhelper.gui;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import regnumhelper.Main;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.StyledDocument;

/**
 *
 * @author  Niels
 */
public class ChatPanel extends javax.swing.JPanel {

    Main main = null;

    StyledDocument doc = null;

    boolean generalChat = false;

    MyListModel namesModel = new MyListModel();

    /** Creates new form ChatPanel */
    public ChatPanel(boolean generalChat) {
        this.generalChat = generalChat;
        initComponents();
        doc = txtChat.getStyledDocument();
        lstNames.setModel(namesModel);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setGroupNames(String group, List<String> names) {
        namesModel.setGroupData(group, names);
    }

    public void setPlayerNames(List<String> names) {
        namesModel.setPlayerNamaData(names);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        lblEnterChat = new javax.swing.JLabel();
        txtChatEntry = new javax.swing.JTextField();
        butSendChat = new javax.swing.JButton();
        butClear = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        scrNames = new javax.swing.JScrollPane();
        lstNames = new javax.swing.JList();
        scrChat = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextPane();
        setLayout(new java.awt.BorderLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new java.awt.GridBagLayout());
        lblEnterChat.setText(" Enter chat: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(lblEnterChat, gridBagConstraints);
        txtChatEntry.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChatEntryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(txtChatEntry, gridBagConstraints);
        butSendChat.setText("Send");
        butSendChat.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butSendChatActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(butSendChat, gridBagConstraints);
        butClear.setText("clear");
        butClear.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butClearActionPerformed(evt);
            }
        });
        jPanel1.add(butClear, new java.awt.GridBagConstraints());
        add(jPanel1, java.awt.BorderLayout.SOUTH);
        jSplitPane1.setDividerLocation(100);
        lstNames.setBackground(new java.awt.Color(255, 255, 204));
        lstNames.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        scrNames.setViewportView(lstNames);
        jSplitPane1.setLeftComponent(scrNames);
        txtChat.setBackground(new java.awt.Color(255, 204, 204));
        txtChat.setContentType("text/html");
        txtChat.setEditable(false);
        scrChat.setViewportView(txtChat);
        jSplitPane1.setRightComponent(scrChat);
        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }

    public void sendChatMessage() {
        if (txtChatEntry.getText().length() > 0) {
            String txt = txtChatEntry.getText().trim();
            if (main != null) main.networkClient.setChat(txt + "\n", generalChat);
        }
        txtChatEntry.setText("");
    }

    private void butSendChatActionPerformed(java.awt.event.ActionEvent evt) {
        sendChatMessage();
    }

    private void txtChatEntryActionPerformed(java.awt.event.ActionEvent evt) {
        sendChatMessage();
    }

    private void butClearActionPerformed(java.awt.event.ActionEvent evt) {
        txtChat.setText("");
        docBuffer = new StringBuffer();
    }

    StringBuffer docBuffer = new StringBuffer();

    public void setChatMessage(String playername, String text, long time) {
        SimpleDateFormat form = new SimpleDateFormat("HH:mm:ss");
        String date = form.format(new Date(time));
        String data = "<B>[" + date + "] " + playername + ":</b> " + text + "<br>";
        docBuffer.append(data);
        txtChat.setText(docBuffer.toString());
        txtChat.setCaretPosition(txtChat.getDocument().getLength());
    }

    private class MyListModel implements ListModel {

        Vector<ListDataListener> listeners = new Vector();

        HashMap<String, List<String>> groupNameAssignments = new HashMap();

        Vector<String> names = new Vector();

        public void setPlayerNamaData(List<String> newNames) {
            names.clear();
            names.addAll(newNames);
            informDataChanged();
        }

        public void setGroupData(String group, List<String> newnames) {
            if (groupNameAssignments.containsKey(group)) {
                groupNameAssignments.remove(group);
            }
            groupNameAssignments.put(group, newnames);
            this.names.clear();
            HashSet<String> tmp = new HashSet();
            Iterator iter = groupNameAssignments.keySet().iterator();
            while (iter.hasNext()) {
                String grpName = (iter.next()).toString();
                List<String> grpNames = (List<String>) groupNameAssignments.get(grpName);
                tmp.addAll(grpNames);
            }
            names.addAll(tmp);
            Collections.sort(names);
            informDataChanged();
        }

        public void clearAll() {
            names.clear();
            informDataChanged();
        }

        public int getSize() {
            return names.size();
        }

        public Object getElementAt(int index) {
            return names.get(index);
        }

        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        public void informDataChanged() {
            Iterator<ListDataListener> iter = listeners.iterator();
            while (iter.hasNext()) {
                iter.next().contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
            }
        }
    }

    private javax.swing.JButton butClear;

    private javax.swing.JButton butSendChat;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JLabel lblEnterChat;

    private javax.swing.JList lstNames;

    private javax.swing.JScrollPane scrChat;

    private javax.swing.JScrollPane scrNames;

    private javax.swing.JTextPane txtChat;

    private javax.swing.JTextField txtChatEntry;
}
