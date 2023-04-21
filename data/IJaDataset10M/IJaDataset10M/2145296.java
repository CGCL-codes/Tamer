package com.daffodilwoods.mail.action;

import javax.swing.*;
import java.awt.event.*;
import com.daffodilwoods.mail.Main;
import com.daffodilwoods.mail.tools.*;
import com.daffodilwoods.mail.gui.*;
import javax.mail.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import com.daffodilwoods.mail.frame.*;

/**
 *
 * <p> </p>
 * <p> </p>
 * <p>Copyright: Copyright (c) 2004 Daffodil Software Ltd.</p>
 * <p>Company:  Daffodil Software Ltd.</p>
 * @author Alok Sarawat <alok.sarawat@daffodildb.com>
 * @version 1.0
 */
public class SendReceiveAction extends AbstractAction {

    private MailBO mailBO;

    public SendReceiveAction() {
        super("Send/Receive");
        ImageIcon icon = Main.getInstance().getImageGenerator().getImageIcon("send_receive.png");
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.SHORT_DESCRIPTION, "Send/Receive");
        try {
            mailBO = MailBO.getInstance();
        } catch (SQLException ex) {
            Utility.showMessage(Main.getInstance(), ex.getMessage(), "SQL Error", 0);
            ex.printStackTrace();
        }
        setEnabled(true);
    }

    public void actionPerformed(ActionEvent event) {
        UserObject userObject = (UserObject) ActionManager.getAction(ActionManager.GET_ACCOUNTS_ACTION).getValue("USEROBJECT");
        int i = userObject.getID();
        MyThread sender = new MyThread(mailBO, i, 's');
        sender.start();
        MyThread receiver = new MyThread(mailBO, i, 'r');
        receiver.start();
    }

    class MyThread extends Thread {

        private int id;

        private char ch;

        private MailBO mailBO;

        MyThread(MailBO mailBO, int id, char ch) {
            this.id = id;
            this.ch = ch;
            this.mailBO = mailBO;
        }

        public void run() {
            MainPanel mainPanel = MainPanel.getInstance();
            try {
                if (ch == 's') {
                    if (id == 0) {
                        mailBO.sendAll();
                    } else {
                        mailBO.send(id);
                    }
                } else {
                    ArrayList mail = null;
                    if (id == 0) {
                        mail = mailBO.receiveAll();
                    } else {
                        try {
                            mail = mailBO.receive(id);
                        } catch (MessagingException e) {
                            Utility.showMessage(mainPanel, e.getMessage(), "Error during mail receiving", 0);
                            e.printStackTrace();
                        }
                    }
                    MessageComponent mc = mainPanel.getMessageComponent();
                    if (mc != null && mail != null && mail.size() > 0 && mainPanel.getSelectedNodeUserObject().toString().equals(MainPanel.INBOX)) {
                        mc.createTable(mail);
                    }
                }
            } catch (SQLException ex2) {
                ex2.printStackTrace();
                Utility.showMessage(mainPanel, ex2.getMessage(), "SQL Error", 0);
            } catch (IOException ex) {
                ex.printStackTrace();
                Utility.showMessage(mainPanel, ex.getMessage(), "IO Error", 0);
            }
        }
    }
}
