package sk.yw.azetclient.gui;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sk.yw.azetclient.Main;
import sk.yw.azetclient.gui.actions.OpenConversationAction;
import sk.yw.azetclient.model.Buddy;
import sk.yw.azetclient.model.MessageThread;
import sk.yw.azetclient.model.MessageThreadEvent;
import sk.yw.azetclient.model.MessageThreadListener;

/**
 *
 * @author  error216
 */
public class ConversationFrame extends javax.swing.JFrame implements ChangeListener, MessageThreadListener {

    private static final long serialVersionUID = 236409244l;

    private ConversationManager manager;

    private Buddy user;

    private Buddy buddy;

    private Map<MessageThread, JMenuItem> threads = new HashMap<MessageThread, JMenuItem>();

    private MessageThread selectedThread;

    protected class MoveToThreadAction extends AbstractAction {

        private static final long serialVersionUID = 494039534l;

        private MessageThread thread;

        public MoveToThreadAction(MessageThread thread) {
            super(thread.getName());
            this.thread = thread;
        }

        public MessageThread getThread() {
            return thread;
        }

        public void actionPerformed(ActionEvent e) {
            synchronized (thread) {
                synchronized (selectedThread) {
                    thread.forceAddMessage(selectedThread.getLastMessage().createMutable());
                    selectedThread.removeLastMessage();
                }
            }
        }
    }

    /** Creates new form MessageFrame */
    public ConversationFrame(ConversationManager manager, Buddy user, Buddy buddy, MessageThread thread) {
        if (manager == null) throw new IllegalArgumentException("Null pointer in manager");
        if (user == null) throw new IllegalArgumentException("Null pointer in user");
        if (buddy == null) throw new IllegalArgumentException("Null pointer in buddy");
        if (thread == null) throw new IllegalArgumentException("Null pointer in thread");
        this.manager = manager;
        this.user = user;
        this.buddy = buddy;
        initComponents();
        setLocationByPlatform(true);
        jTabbedPane1.addChangeListener(this);
        createConversationPanel(thread);
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        conversationMenu = new javax.swing.JMenu();
        newConversationItem = new javax.swing.JMenuItem();
        openConversationItem = new javax.swing.JMenuItem();
        changeConversationNameItem = new javax.swing.JMenuItem();
        closeConversationItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        moveMessageToMenu = new javax.swing.JMenu();
        jSeparator2 = new javax.swing.JSeparator();
        moveMessageToNewConversationItem = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(createTitle());
        setIconImage(Main.getMainFrame().getIconImage());
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {

            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                ConversationFrame.this.windowGainedFocus(evt);
            }

            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                ConversationFrame.this.windowClosing(evt);
            }
        });
        conversationMenu.setText(Main.getText("menu.conversation"));
        newConversationItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        newConversationItem.setText(Main.getText("menu.conversation.newConversation"));
        newConversationItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newConversationItemActionPerformed(evt);
            }
        });
        conversationMenu.add(newConversationItem);
        openConversationItem.setAction(new OpenConversationAction(user, buddy, this));
        conversationMenu.add(openConversationItem);
        changeConversationNameItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        changeConversationNameItem.setText(Main.getText("menu.conversation.changeConversationName"));
        changeConversationNameItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeConversationNameItemActionPerformed(evt);
            }
        });
        conversationMenu.add(changeConversationNameItem);
        closeConversationItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        closeConversationItem.setText(Main.getText("menu.conversation.closeConversation"));
        closeConversationItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeConversationItemActionPerformed(evt);
            }
        });
        conversationMenu.add(closeConversationItem);
        conversationMenu.add(jSeparator1);
        moveMessageToMenu.setText(Main.getText("menu.conversation.moveMessageTo"));
        moveMessageToMenu.add(jSeparator2);
        moveMessageToNewConversationItem.setText(Main.getText("menu.conversation.moveMessageTo.newConversation"));
        moveMessageToNewConversationItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveMessageToNewConversationActionPerformed(evt);
            }
        });
        moveMessageToMenu.add(moveMessageToNewConversationItem);
        conversationMenu.add(moveMessageToMenu);
        jMenuBar1.add(conversationMenu);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE));
        pack();
    }

    private void changeConversationNameItemActionPerformed(java.awt.event.ActionEvent evt) {
        String newName = JOptionPane.showInputDialog(this, Main.getText("changeConversationNameDialog.text"), Main.getText("changeConversationNameDialog.title"), JOptionPane.PLAIN_MESSAGE);
        if (newName != null) setSelectedConversationName(newName);
    }

    private void windowClosing(java.awt.event.WindowEvent evt) {
        manager.removeConversationFrame(buddy);
        for (java.awt.Component component : jTabbedPane1.getComponents()) {
            if (component instanceof ConversationPanel) {
                ConversationPanel panel = (ConversationPanel) component;
                panel.dispose();
            }
        }
        dispose();
    }

    private void windowGainedFocus(java.awt.event.WindowEvent evt) {
        ((ConversationPanel) jTabbedPane1.getSelectedComponent()).requestFocusInWindowOnInputTextPane();
    }

    private void newConversationItemActionPerformed(java.awt.event.ActionEvent evt) {
        manager.createConversationPanel(buddy, Main.getMainFrame().createNewThread(), true);
    }

    private void closeConversationItemActionPerformed(java.awt.event.ActionEvent evt) {
        ConversationPanel panel = (ConversationPanel) jTabbedPane1.getSelectedComponent();
        closeConversationPanel(panel);
    }

    private void moveMessageToNewConversationActionPerformed(java.awt.event.ActionEvent evt) {
        synchronized (selectedThread) {
            MessageThread newThread = Main.getMainFrame().createNewThread();
            newThread.addMessage(selectedThread.getLastMessage().createMutable());
            selectedThread.removeLastMessage();
        }
    }

    private javax.swing.JMenuItem changeConversationNameItem;

    private javax.swing.JMenuItem closeConversationItem;

    private javax.swing.JMenu conversationMenu;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JMenu moveMessageToMenu;

    private javax.swing.JMenuItem moveMessageToNewConversationItem;

    private javax.swing.JMenuItem newConversationItem;

    private javax.swing.JMenuItem openConversationItem;

    private String createTitle() {
        return Main.getText("conversationFrame.title").replace("%USERNAME%", buddy.getVisualName());
    }

    public void createConversationPanel(MessageThread thread) {
        JMenuItem threadItem = new JMenuItem(new MoveToThreadAction(thread));
        threads.put(thread, threadItem);
        addThreadToMenu(thread, false);
        synchronized (thread) {
            jTabbedPane1.addTab(thread.getName(), new ConversationPanel(this, user, buddy, thread));
            thread.addMessageThreadListener(this);
        }
    }

    public void closeConversationPanel(ConversationPanel panel) {
        panel.dispose();
        selectedThread = null;
        synchronized (panel.getThread()) {
            threads.remove(panel.getThread());
            jTabbedPane1.remove(panel);
            Main.getMainFrame().closeThread(panel.getThread());
            if (jTabbedPane1.getTabCount() == 0) {
                manager.removeConversationFrame(buddy);
                dispose();
            }
            panel.getThread().removeMessageThreadListener(this);
        }
    }

    public Set<MessageThread> getThreads() {
        return Collections.unmodifiableSet(threads.keySet());
    }

    public void setSelectedConversationName(String name) {
        int selectedIndex = jTabbedPane1.getSelectedIndex();
        ConversationPanel selectedPanel = (ConversationPanel) jTabbedPane1.getSelectedComponent();
        selectedPanel.getThread().setName(name);
        jTabbedPane1.setTitleAt(selectedIndex, name);
    }

    public void setSelectedConversationPanel(ConversationPanel panel) {
        jTabbedPane1.setSelectedComponent(panel);
    }

    public void requestFocusOnLastInputTextPane() {
        int lastTabIndex = jTabbedPane1.getTabCount() - 1;
        jTabbedPane1.setSelectedIndex(lastTabIndex);
        ((ConversationPanel) jTabbedPane1.getSelectedComponent()).requestFocusOnInputTextPane();
    }

    protected void addThreadToMenu(MessageThread thread, boolean top) {
        if (top) {
            moveMessageToMenu.add(threads.get(thread), 0);
        } else {
            moveMessageToMenu.add(threads.get(thread), moveMessageToMenu.getItemCount() - 2);
        }
    }

    protected void removeThreadFromMenu(MessageThread thread) {
        moveMessageToMenu.remove(threads.get(thread));
    }

    protected void enableThreadMenu() {
        moveMessageToMenu.setEnabled(selectedThread.getMessages().size() > 0 && user.equals(selectedThread.getLastMessage().getReceiver()));
    }

    public void stateChanged(ChangeEvent e) {
        if (selectedThread != null) {
            addThreadToMenu(selectedThread, true);
        }
        ConversationPanel selectedPanel = (ConversationPanel) jTabbedPane1.getSelectedComponent();
        if (selectedPanel == null) return;
        selectedThread = selectedPanel.getThread();
        removeThreadFromMenu(selectedThread);
        enableThreadMenu();
    }

    public void messageAdded(MessageThreadEvent e) {
        final MessageThread thread = e.getMessage().getThread();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                removeThreadFromMenu(thread);
                addThreadToMenu(thread, false);
                enableThreadMenu();
            }
        });
    }

    public void messageChanged(MessageThreadEvent e) {
    }

    public void messageRemoved(MessageThreadEvent e) {
        MessageThread thread = e.getMessage().getThread();
        if (thread.isEmpty()) {
            closeConversationPanel((ConversationPanel) jTabbedPane1.getSelectedComponent());
        }
    }
}
