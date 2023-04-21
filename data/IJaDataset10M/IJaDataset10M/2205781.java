package chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.Timer;

public class IHM extends JFrame implements ActionListener, TreeSelectionListener {

    IHM_rootComponents component;

    JMenuBar JMB_menuBar;

    JMenu JM_action;

    JMenuItem JMI_createTopic;

    JPanel container;

    JSplitPane JSPl_chatUsers;

    JPanel JP_chatUsers;

    JTextArea JTA_chat;

    JScrollPane JSP_chat;

    DefaultMutableTreeNode DMT_UsersChatroom;

    JTree JT_UsersChatroom;

    JScrollPane JSP_UsersChatroom;

    JPanel JP_sendMess;

    JButton JB_sendMess;

    JTextArea JTA_senMess;

    JScrollPane JSP_senMess;

    Timer timer;

    public void initJMenu() {
        JMB_menuBar = new JMenuBar();
        JM_action = new JMenu("Action");
        JMI_createTopic = new JMenuItem("Create topic");
        JMI_createTopic.addActionListener(this);
        JMB_menuBar.add(JM_action);
        JM_action.add(JMI_createTopic);
        this.setJMenuBar(JMB_menuBar);
    }

    public void initcomponent() throws RemoteException {
        JP_chatUsers = new JPanel();
        JTA_chat = new JTextArea();
        JP_chatUsers.setLayout(new BorderLayout());
        JTA_chat.setPreferredSize(new Dimension(700, 400));
        JTA_chat.setEditable(true);
        JTA_chat.setText("hello world");
        JSP_chat = new JScrollPane(JTA_chat);
        DMT_UsersChatroom = new DefaultMutableTreeNode("Chatroom");
        chargementUsersChatroom(DMT_UsersChatroom);
        JT_UsersChatroom = new JTree(DMT_UsersChatroom);
        JT_UsersChatroom.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JT_UsersChatroom.addTreeSelectionListener(this);
        JSP_UsersChatroom = new JScrollPane(JT_UsersChatroom);
        JSP_chat.setPreferredSize(new Dimension(800, 570));
        JSPl_chatUsers = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, JSP_chat, JSP_UsersChatroom);
        JP_chatUsers.add(JSPl_chatUsers, "North");
        JP_sendMess = new JPanel();
        JP_sendMess.setLayout(new BorderLayout());
        JB_sendMess = new JButton("SEND");
        JB_sendMess.setPreferredSize(new Dimension(160, 80));
        JB_sendMess.addActionListener(this);
        JTA_senMess = new JTextArea();
        JTA_senMess.setEditable(true);
        JSP_senMess = new JScrollPane(JTA_senMess);
        JSP_senMess.setPreferredSize(new Dimension(800, 100));
        JP_sendMess.add(JSP_senMess, BorderLayout.WEST);
        JP_sendMess.add(JB_sendMess, BorderLayout.EAST);
        container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(JP_chatUsers, BorderLayout.NORTH);
        container.add(JP_sendMess, BorderLayout.SOUTH);
        this.add(container);
        this.setTitle("Chatroom");
        this.setSize(975, 730);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setContentPane(container);
        this.setVisible(true);
        timer = new Timer(1000, this);
        timer.start();
    }

    public IHM(IHM_rootComponents c) throws RemoteException {
        component = c;
        component.mainFen = this;
        this.initJMenu();
        this.initcomponent();
    }

    public void chargementUsersChatroom(DefaultMutableTreeNode top) throws RemoteException {
        Iterator i = component._topicsManag.getListTopic().keySet().iterator();
        while (i.hasNext()) {
            String nameChatroom = i.next().toString();
            DefaultMutableTreeNode chatroom = new DefaultMutableTreeNode(nameChatroom);
            top.add(chatroom);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) JT_UsersChatroom.getLastSelectedPathComponent();
        String name = JT_UsersChatroom.getLastSelectedPathComponent().toString();
        System.out.print(name);
        Iterator it;
        int index = 0;
        try {
            it = component._topicsManag.getListTopic().keySet().iterator();
            while (it.hasNext()) {
                String topicName = (String) it.next();
                if (topicName.equals(name)) {
                    JTA_chat.setText(JTA_chat.getText() + "\n" + ">> Connection to : " + name);
                    component._topicsManag.joinTopic(name);
                    component.setRootChatroom(name);
                    try {
                        component.refreshChatMess();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                index++;
            }
        } catch (RemoteException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            try {
                component.refreshChatMess();
            } catch (RemoteException exp) {
                exp.printStackTrace();
            }
        } else if (e.getSource() == JMI_createTopic) {
            IHM_TOPIC logFen = new IHM_TOPIC(component, this);
        }
        if (e.getSource() == JB_sendMess) {
            try {
                System.out.print("\nChatroom :" + component._topicsManag.getListTopic().get(component.getRootChatroom()).getTopic() + "\n");
                component._topicsManag.post(component._rootChatroom, JTA_senMess.getText(), component.getRootChatter());
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
            component.mainFen = this;
            try {
                component.refreshChatMess();
            } catch (RemoteException exp) {
                exp.printStackTrace();
            }
            JTA_senMess.setText("");
        }
    }
}
