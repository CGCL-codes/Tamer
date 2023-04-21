package net.bervini.rasael.galacticfreedom.gui.windows;

import java.awt.Color;
import java.awt.Component;
import java.sql.*;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.*;
import net.bervini.rasael.galacticfreedom.MainFrame;
import net.bervini.rasael.galacticfreedom.game.missions.Mission;
import net.bervini.rasael.galacticfreedom.game.missions.MissionLog;
import net.bervini.rasael.galacticfreedom.ngn.GamePreferences;
import net.bervini.rasael.galacticfreedom.util.ListObject;
import net.bervini.rasael.util.swing.table.RasaelNonEditableCellEditor;
import net.bervini.rasael.util.swing.table.RasaelTableOperations;

/**
 *
 * @author  Rasael Bervini
 */
public class MailboxWindow extends DefaultWindow {

    final DefaultTableModel inboxTableModel = new DefaultTableModel() {

        public boolean isCellEditable(int i, int ii) {
            return false;
        }
    };

    final DefaultTableModel outboxTableModel = new DefaultTableModel() {

        public boolean isCellEditable(int i, int ii) {
            return false;
        }
    };

    final DefaultListModel draftsList = new DefaultListModel();

    /** Creates new form MissionWindow */
    public MailboxWindow(MainFrame main) {
        super(main);
        initComponents();
        doDefaultFrameOp();
        tableInbox.setModel(inboxTableModel);
        tableInbox.setColumnSelectionAllowed(false);
        tableInbox.setCellSelectionEnabled(false);
        tableInbox.setRowSelectionAllowed(true);
        tableInbox.setCellEditor(new RasaelNonEditableCellEditor());
        inboxTableModel.addColumn("Date");
        inboxTableModel.addColumn("From");
        inboxTableModel.addColumn("Subject");
        JTableHeader tempTableHeader = tableInbox.getTableHeader();
        tempTableHeader.setReorderingAllowed(false);
        tempTableHeader.resizeAndRepaint();
        tableInbox.getColumnModel().getColumn(1).setCellRenderer(new RenderRedGreen());
        tableOutbox.setModel(outboxTableModel);
        tableOutbox.setColumnSelectionAllowed(false);
        tableOutbox.setCellSelectionEnabled(false);
        tableOutbox.setRowSelectionAllowed(true);
        tableOutbox.setCellEditor(new RasaelNonEditableCellEditor());
        outboxTableModel.addColumn("Date");
        outboxTableModel.addColumn("To");
        outboxTableModel.addColumn("Subject");
        lblInboxMessageSubject.setText("-");
        lblInboxMessageFrom.setText("-");
        lblInboxMessageTo.setText("-");
        lblInboxMessageDate.setText("-");
        txtInboxMessage.setText("");
        loadInbox();
        loadOutbox();
        updateUnreadMessagesLabel();
        setVisible(true);
    }

    public void loadInbox() {
        RasaelTableOperations.clearTable(tableInbox);
        try {
            System.out.println("Loading inbox mail..");
            String inboxSql = "SELECT `playermessages_id`,`playermessages_read`,`playermessages_date`,`playermessages_from`,`playermessages_subject` FROM `playermessages` WHERE `playermessages_to` LIKE ? ORDER BY `playermessages_date` DESC";
            Connection c = getMain().getConnection();
            PreparedStatement pstmt = c.prepareStatement(inboxSql);
            pstmt.setString(1, " " + getMain().getPlayerAccount().getUsername() + " ");
            ResultSet result = pstmt.executeQuery();
            int numberOfMails = 0;
            while (result.next()) {
                final int messageId = result.getInt("playermessages_id");
                final String messageFrom = result.getString("playermessages_from");
                final String messageSubject = result.getString("playermessages_subject");
                final Timestamp messageDate = (Timestamp) result.getTimestamp("playermessages_date");
                final int messageRead = result.getInt("playermessages_read");
                inboxTableModel.addRow(new Object[] { new ListObject("" + messageDate, new Integer(messageId)), new ListObject(messageFrom, new Integer(messageRead)), messageSubject });
                numberOfMails++;
            }
            System.out.println("Finished, loaded " + numberOfMails + " mails");
            if (numberOfMails == 0) {
                inboxTableModel.addRow(new Object[] { "No mail", "", "" });
                System.out.println("No mail for the player");
            }
        } catch (SQLException e) {
            getMain().showErrorDialog("Cannot load the inbox! Please try again later!\n" + e.getMessage());
            return;
        }
        updateUnreadMessagesLabel();
    }

    public void loadOutbox() {
        RasaelTableOperations.clearTable(tableOutbox);
        try {
            System.out.println("Loading outbox mail..");
            String outboxSql = "SELECT `playermessages_id`,`playermessages_date`,`playermessages_to`,`playermessages_subject` FROM `playermessages` WHERE `playermessages_from` LIKE ? ORDER BY `playermessages_date` DESC";
            Connection c = getMain().getConnection();
            PreparedStatement pstmt = c.prepareStatement(outboxSql);
            pstmt.setString(1, getMain().getPlayerAccount().getUsername());
            ResultSet result = pstmt.executeQuery();
            int numberOfMails = 0;
            while (result.next()) {
                final int messageId = result.getInt("playermessages_id");
                final String messageTo = result.getString("playermessages_to");
                final String messageSubject = result.getString("playermessages_subject");
                final String messageDate = ((Timestamp) result.getTimestamp("playermessages_date")).toString();
                outboxTableModel.addRow(new Object[] { new ListObject(messageDate, new Integer(messageId)), messageTo, messageSubject });
                numberOfMails++;
            }
            System.out.println("Finished, loaded " + numberOfMails + " mails");
            if (numberOfMails == 0) {
                outboxTableModel.addRow(new Object[] { "No mail", "", "" });
                System.out.println("No mail from the player");
            }
        } catch (SQLException e) {
            getMain().showErrorDialog("Cannot load the outbox! Please try again later!\n" + e.getMessage());
            return;
        }
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panInbox = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableInbox = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblInboxMessageSubject = new javax.swing.JLabel();
        jLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtInboxMessage = new javax.swing.JEditorPane();
        lblInboxMessageFrom = new javax.swing.JLabel();
        lblInboxMessageTo = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblInboxMessageDate = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblUnreadMessages = new javax.swing.JLabel();
        panOutbox = new javax.swing.JPanel();
        jSplitPane7 = new javax.swing.JSplitPane();
        jScrollPane15 = new javax.swing.JScrollPane();
        tableOutbox = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        lblOutboxTo = new javax.swing.JLabel();
        lblOutboxSubject = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblOutboxDate = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane19 = new javax.swing.JScrollPane();
        txtOutboxMessage = new javax.swing.JTextArea();
        panCompose = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSendTo = new javax.swing.JTextField();
        txtSubject = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtMessage = new javax.swing.JTextArea();
        btnSendMessage = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        panOptions = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jList9 = new javax.swing.JList();
        jLabel9 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mailbox");
        setName("missionWindow");
        setResizable(false);
        jTabbedPane1.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPane1FocusGained(evt);
            }
        });
        jSplitPane1.setDividerLocation(60);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setLastDividerLocation(25);
        jSplitPane1.setOneTouchExpandable(true);
        tableInbox.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        tableInbox.setToolTipText("Double-click to display a message");
        tableInbox.setShowVerticalLines(false);
        tableInbox.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableInboxMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableInbox);
        jSplitPane1.setTopComponent(jScrollPane1);
        jLabel3.setText("Subject:");
        lblInboxMessageSubject.setText("-");
        jLabel.setText("From:");
        jLabel6.setText("To:");
        txtInboxMessage.setEditable(false);
        jScrollPane2.setViewportView(txtInboxMessage);
        lblInboxMessageFrom.setForeground(new java.awt.Color(0, 0, 204));
        lblInboxMessageFrom.setText("-");
        lblInboxMessageTo.setText("-");
        jLabel4.setText("Date:");
        lblInboxMessageDate.setText("-");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addComponent(jLabel).addComponent(jLabel6).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblInboxMessageFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE).addComponent(lblInboxMessageSubject, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE).addComponent(lblInboxMessageTo, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE).addComponent(lblInboxMessageDate, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(lblInboxMessageSubject)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel).addComponent(lblInboxMessageFrom)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(lblInboxMessageTo)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(lblInboxMessageDate)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE).addContainerGap()));
        jSplitPane1.setRightComponent(jPanel1);
        jLabel7.setText("Number of unread messages:");
        lblUnreadMessages.setForeground(new java.awt.Color(0, 0, 255));
        lblUnreadMessages.setText("-");
        javax.swing.GroupLayout panInboxLayout = new javax.swing.GroupLayout(panInbox);
        panInbox.setLayout(panInboxLayout);
        panInboxLayout.setHorizontalGroup(panInboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panInboxLayout.createSequentialGroup().addContainerGap().addGroup(panInboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE).addGroup(panInboxLayout.createSequentialGroup().addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblUnreadMessages))).addContainerGap()));
        panInboxLayout.setVerticalGroup(panInboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panInboxLayout.createSequentialGroup().addContainerGap().addGroup(panInboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(lblUnreadMessages)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jTabbedPane1.addTab("Inbox", panInbox);
        jSplitPane7.setDividerLocation(50);
        jSplitPane7.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane7.setOneTouchExpandable(true);
        tableOutbox.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        tableOutbox.setToolTipText("Double-click to display a message");
        tableOutbox.setShowVerticalLines(false);
        tableOutbox.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableOutboxMouseReleased(evt);
            }
        });
        jScrollPane15.setViewportView(tableOutbox);
        jSplitPane7.setLeftComponent(jScrollPane15);
        jLabel8.setText("To:");
        lblOutboxTo.setText("-");
        lblOutboxSubject.setText("-");
        jLabel5.setText("Subject:");
        lblOutboxDate.setText("-");
        jLabel10.setText("Date:");
        txtOutboxMessage.setColumns(20);
        txtOutboxMessage.setRows(5);
        jScrollPane19.setViewportView(txtOutboxMessage);
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5).addComponent(jLabel10).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblOutboxSubject, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE).addComponent(lblOutboxDate, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE).addGroup(jPanel3Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblOutboxTo, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))))).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(lblOutboxSubject)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel10).addComponent(lblOutboxDate)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(lblOutboxTo)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE).addContainerGap()));
        jSplitPane7.setRightComponent(jPanel3);
        javax.swing.GroupLayout panOutboxLayout = new javax.swing.GroupLayout(panOutbox);
        panOutbox.setLayout(panOutboxLayout);
        panOutboxLayout.setHorizontalGroup(panOutboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panOutboxLayout.createSequentialGroup().addContainerGap().addComponent(jSplitPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE).addContainerGap()));
        panOutboxLayout.setVerticalGroup(panOutboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panOutboxLayout.createSequentialGroup().addContainerGap().addComponent(jSplitPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE).addContainerGap()));
        jTabbedPane1.addTab("Outbox", panOutbox);
        jLabel1.setText("Send to:");
        txtMessage.setColumns(20);
        txtMessage.setRows(5);
        jScrollPane9.setViewportView(txtMessage);
        btnSendMessage.setText("Send");
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });
        jButton2.setText("Save As Draft");
        jLabel2.setText("Subject:");
        jButton3.setText("Choose");
        javax.swing.GroupLayout panComposeLayout = new javax.swing.GroupLayout(panCompose);
        panCompose.setLayout(panComposeLayout);
        panComposeLayout.setHorizontalGroup(panComposeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panComposeLayout.createSequentialGroup().addContainerGap().addGroup(panComposeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panComposeLayout.createSequentialGroup().addComponent(jButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnSendMessage)).addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE).addGroup(panComposeLayout.createSequentialGroup().addGroup(panComposeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panComposeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panComposeLayout.createSequentialGroup().addComponent(txtSendTo, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3)).addComponent(txtSubject, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)))).addContainerGap()));
        panComposeLayout.setVerticalGroup(panComposeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panComposeLayout.createSequentialGroup().addContainerGap().addGroup(panComposeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jButton3).addComponent(txtSendTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panComposeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(txtSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panComposeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnSendMessage).addComponent(jButton2)).addContainerGap()));
        jTabbedPane1.addTab("Compose", panCompose);
        jList9.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jList9.setEnabled(false);
        jScrollPane18.setViewportView(jList9);
        jLabel9.setText("Double-click on a saved message to load in the compose tab.");
        javax.swing.GroupLayout panOptionsLayout = new javax.swing.GroupLayout(panOptions);
        panOptions.setLayout(panOptionsLayout);
        panOptionsLayout.setHorizontalGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panOptionsLayout.createSequentialGroup().addContainerGap().addGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE).addComponent(jLabel9)).addContainerGap()));
        panOptionsLayout.setVerticalGroup(panOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panOptionsLayout.createSequentialGroup().addContainerGap().addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jTabbedPane1.addTab("Saved Messages", panOptions);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)));
        pack();
    }

    private void tableOutboxMouseReleased(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() > 1) {
            int index = tableOutbox.getSelectedRow();
            if (index == -1) {
                getMain().showErrorDialog("Please select a message");
            }
            Object o = tableOutbox.getValueAt(index, 0);
            if (o instanceof ListObject) {
                int objectIndex = ((Integer) ((ListObject) o).getObject()).intValue();
                System.out.println("Show message index " + objectIndex);
                try {
                    String sql = "SELECT * FROM `playermessages` WHERE `playermessages_id` = ?";
                    PreparedStatement pstmt = getMain().getConnection().prepareStatement(sql);
                    pstmt.setInt(1, objectIndex);
                    ResultSet results = pstmt.executeQuery();
                    if (results.next()) {
                        String subject = results.getString("playermessages_subject");
                        lblOutboxSubject.setText(subject);
                        String to = results.getString("playermessages_to");
                        lblOutboxTo.setText(to);
                        String mailbox = results.getString("playermessages_message");
                        txtOutboxMessage.setText(mailbox);
                        String date = ((Timestamp) results.getTimestamp("playermessages_date")).toString();
                        lblOutboxDate.setText(date);
                    } else {
                        getMain().showErrorDialog("Error loading the message, #id - " + objectIndex + "!\nThe message was probably deleted!");
                    }
                } catch (SQLException e) {
                    getMain().showErrorDialog("Error loading the message, #id - " + objectIndex + "!\n" + e.getMessage());
                }
            } else if (o instanceof String) {
                getMain().showErrorDialog("You don't have any messages in your inbox");
            }
        }
    }

    private void jTabbedPane1FocusGained(java.awt.event.FocusEvent evt) {
        loadInbox();
        loadOutbox();
        updateUnreadMessagesLabel();
        getMain().checkMail();
    }

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {
        String sendTo = txtSendTo.getText();
        String[] sendTos = sendTo.split(" ");
        int validusernames = 0;
        for (int i = 0; i < sendTos.length; i++) {
            final String username = sendTos[i].trim();
            if (username.length() > 0) {
                if (!NewAccountWindow.isValidUsername(username)) {
                    getMain().showErrorDialog("\"" + username + "\" is not a valid username");
                    return;
                }
                try {
                    if (!NewAccountWindow.checkIfNameExists(username)) {
                        getMain().showErrorDialog("Cannot find the username \"" + username + "\"");
                        return;
                    } else {
                        validusernames++;
                    }
                } catch (Exception e) {
                    getMain().showErrorDialog("An error occured while trying to validate the usernames!\n" + e.getMessage());
                    return;
                }
            }
        }
        if (validusernames == 0) {
            getMain().showErrorDialog("Please insert at least a valid username!");
            return;
        }
        try {
            sendMail(sendTos, getMain().getPlayerAccount().getUsername(), txtSubject.getText(), txtMessage.getText());
            getMain().showMessageDialog("Message sended succesfully! :)");
            txtMessage.setText("");
            txtSubject.setText("");
        } catch (SQLException e) {
            getMain().showErrorDialog("An error occured while trying to send the mail!\n" + e.getMessage());
        }
    }

    public static void sendMail(String to[], String from, String subject, String message) throws SQLException {
        String oneTo = "";
        for (int i = 0; i < to.length; i++) {
            if (to[i].trim().length() > 0) {
                oneTo += " " + to[i].trim() + " ";
            }
        }
        if (from == null) {
            from = GamePreferences.getGameName();
        }
        String sql = "INSERT INTO `playermessages` (" + "`playermessages_id` , `playermessages_date` ," + " `playermessages_from` , `playermessages_to` ," + " `playermessages_subject` , `playermessages_message`" + " ) VALUES (" + "NULL," + "NOW()," + "?,?,?,?" + ");";
        PreparedStatement pstmt = getMain().getConnection().prepareStatement(sql);
        pstmt.setString(1, from);
        pstmt.setString(2, oneTo);
        pstmt.setString(3, subject);
        pstmt.setString(4, message);
        int lastId = pstmt.executeUpdate();
    }

    private void tableInboxMouseReleased(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() > 1) {
            int index = tableInbox.getSelectedRow();
            if (index == -1) {
                getMain().showErrorDialog("Please select a message");
            }
            Object o = tableInbox.getValueAt(index, 0);
            if (o instanceof ListObject) {
                int objectIndex = ((Integer) ((ListObject) o).getObject()).intValue();
                System.out.println("Show message index " + objectIndex);
                try {
                    String sql = "SELECT * FROM `playermessages` WHERE `playermessages_id` = ?";
                    PreparedStatement pstmt = getMain().getConnection().prepareStatement(sql);
                    pstmt.setInt(1, objectIndex);
                    ResultSet results = pstmt.executeQuery();
                    if (results.next()) {
                        final int id = results.getInt("playermessages_id");
                        final String subject = results.getString("playermessages_subject");
                        lblInboxMessageSubject.setText(subject);
                        final String from = results.getString("playermessages_from");
                        lblInboxMessageFrom.setText(from);
                        final String to = results.getString("playermessages_to");
                        lblInboxMessageTo.setText(to);
                        final String mailbox = results.getString("playermessages_message");
                        txtInboxMessage.setText(mailbox);
                        final String date = ((Timestamp) results.getTimestamp("playermessages_date")).toString();
                        lblInboxMessageDate.setText(date);
                        setMessageAsRead(id);
                        tableInbox.setValueAt(new ListObject("" + tableInbox.getValueAt(index, 1), new Integer(1)), index, 1);
                        getMain().checkMail();
                    } else {
                        getMain().showErrorDialog("Error loading the message, #id - " + objectIndex + "!\nThe message was probably deleted!");
                    }
                } catch (SQLException e) {
                    getMain().showErrorDialog("Error loading the message, #id - " + objectIndex + "!\n" + e.getMessage());
                }
            } else if (o instanceof String) {
                getMain().showErrorDialog("You don't have any messages in your inbox");
            }
        }
    }

    public void updateUnreadMessagesLabel() {
        try {
            lblUnreadMessages.setText("" + getNumberOfUnreadMessages(getMain().getPlayerAccount().getUsername(), getMain()));
        } catch (SQLException e) {
            lblUnreadMessages.setText("N/A");
            System.out.println("Cannot set the unread messages label cause: " + e.getMessage());
        }
    }

    public static int getNumberOfUnreadMessages(String username, MainFrame main) throws SQLException {
        String sql = "SELECT `playermessages_read` FROM `playermessages` WHERE `playermessages_to` = ?;";
        final PreparedStatement pstmt = main.getConnection().prepareStatement(sql);
        pstmt.setString(1, " " + username + " ");
        final ResultSet result = pstmt.executeQuery();
        int count = 0;
        boolean cycled = false;
        while (result.next()) {
            cycled = true;
            if (result.getInt("playermessages_read") == 0) {
                count++;
            }
        }
        if (cycled == false) {
            return -1;
        }
        return count;
    }

    public void setMessageAsRead(int messageId) {
        try {
            String sql = "UPDATE `playermessages` SET `playermessages_read` = 1 WHERE `playermessages_id` = ?;";
            PreparedStatement pstmt = getMain().getConnection().prepareStatement(sql);
            pstmt.setInt(1, messageId);
            pstmt.execute();
            if (pstmt.getUpdateCount() != 1) {
                System.out.println("Exit #1: Cannot mark message as read, message id #" + messageId);
            }
        } catch (SQLException e) {
            System.out.println("Exit #2:Cannot mark message as read, message id #" + messageId + "\n" + e.getMessage());
        }
    }

    private javax.swing.JButton btnSendMessage;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JList jList9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane15;

    private javax.swing.JScrollPane jScrollPane18;

    private javax.swing.JScrollPane jScrollPane19;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane9;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JSplitPane jSplitPane7;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JLabel lblInboxMessageDate;

    private javax.swing.JLabel lblInboxMessageFrom;

    private javax.swing.JLabel lblInboxMessageSubject;

    private javax.swing.JLabel lblInboxMessageTo;

    private javax.swing.JLabel lblOutboxDate;

    private javax.swing.JLabel lblOutboxSubject;

    private javax.swing.JLabel lblOutboxTo;

    private javax.swing.JLabel lblUnreadMessages;

    private javax.swing.JPanel panCompose;

    private javax.swing.JPanel panInbox;

    private javax.swing.JPanel panOptions;

    private javax.swing.JPanel panOutbox;

    private javax.swing.JTable tableInbox;

    private javax.swing.JTable tableOutbox;

    private javax.swing.JEditorPane txtInboxMessage;

    private javax.swing.JTextArea txtMessage;

    private javax.swing.JTextArea txtOutboxMessage;

    private javax.swing.JTextField txtSendTo;

    private javax.swing.JTextField txtSubject;
}

final class RenderRedGreen extends DefaultTableCellRenderer {

    RenderRedGreen() {
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    public Component getTableCellRendererComponent(JTable aTable, Object aNumberValue, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
        if (aNumberValue == null) return this;
        Component renderer = super.getTableCellRendererComponent(aTable, aNumberValue, aIsSelected, aHasFocus, aRow, aColumn);
        final ListObject listObject = (ListObject) aNumberValue;
        final int isRead = ((Integer) listObject.getObject()).intValue();
        if (isRead == 0) {
            renderer.setForeground(Color.blue);
        } else {
            renderer.setForeground(null);
        }
        return this;
    }

    private Color fDarkGreen = Color.green.darker();
}
