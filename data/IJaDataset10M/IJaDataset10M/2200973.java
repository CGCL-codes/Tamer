package eu.somatik.botleecher.gui;

import eu.somatik.botleecher.service.JarImageLoader;
import eu.somatik.botleecher.gui.BotMediator;
import eu.somatik.botleecher.*;
import eu.somatik.botleecher.gui.BotPanel;
import eu.somatik.botleecher.service.ImageLoader;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jibble.pircbot.User;

/**
 *
 * @author  francisdb
 */
public class LeecherFrame extends javax.swing.JFrame {

    private final BotMediator mediator;

    private final ImageLoader imageLoader;

    /**
     * Creates new form LeecherFrame
     * @param mediator
     */
    public LeecherFrame(final BotMediator mediator, final ImageLoader imageLoader) {
        initComponents();
        this.imageLoader = imageLoader;
        this.mediator = mediator;
        logTextArea.setFont(new Font("monospaced", Font.PLAIN, 10));
    }

    private void initComponents() {
        tabbedPane = new javax.swing.JTabbedPane();
        logScrollPane = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        serverPanel = new javax.swing.JPanel();
        connectButton = new javax.swing.JButton();
        serverLabel = new javax.swing.JLabel();
        serverComboBox = new javax.swing.JComboBox();
        roomLabel = new javax.swing.JLabel();
        roomComboBox = new javax.swing.JComboBox();
        botListPanel = new javax.swing.JPanel();
        botListScrollPane = new javax.swing.JScrollPane();
        botList = new javax.swing.JList();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        optionsItem = new javax.swing.JMenuItem();
        exitItem = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Botleecher");
        logTextArea.setColumns(20);
        logTextArea.setRows(5);
        logScrollPane.setViewportView(logTextArea);
        tabbedPane.addTab("Log", logScrollPane);
        serverPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Server"));
        connectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/connect.png")));
        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });
        serverLabel.setText("Server:");
        serverComboBox.setEditable(true);
        serverComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "irc.efnet.net", "irc.efnet.fr", "efnet.xs4all.nl", "irc.efnet.nl", "irc.du.se", "irc.efnet.no", "efnet.demon.co.uk" }));
        roomLabel.setText("Room:");
        roomComboBox.setEditable(true);
        roomComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "#dnbmp3", "#dnbarena" }));
        javax.swing.GroupLayout serverPanelLayout = new javax.swing.GroupLayout(serverPanel);
        serverPanel.setLayout(serverPanelLayout);
        serverPanelLayout.setHorizontalGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(serverPanelLayout.createSequentialGroup().addContainerGap().addGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(serverPanelLayout.createSequentialGroup().addGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(serverLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(roomLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(roomComboBox, 0, 385, Short.MAX_VALUE).addComponent(serverComboBox, 0, 385, Short.MAX_VALUE))).addComponent(connectButton, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
        serverPanelLayout.setVerticalGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(serverPanelLayout.createSequentialGroup().addGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(serverLabel).addComponent(serverComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(roomLabel).addComponent(roomComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(connectButton).addContainerGap(19, Short.MAX_VALUE)));
        botListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bots"));
        botList.setToolTipText("Doubleclick to open a bot connection");
        botList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botListMouseClicked(evt);
            }
        });
        botListScrollPane.setViewportView(botList);
        javax.swing.GroupLayout botListPanelLayout = new javax.swing.GroupLayout(botListPanel);
        botListPanel.setLayout(botListPanelLayout);
        botListPanelLayout.setHorizontalGroup(botListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(botListPanelLayout.createSequentialGroup().addContainerGap().addComponent(botListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE).addContainerGap()));
        botListPanelLayout.setVerticalGroup(botListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(botListPanelLayout.createSequentialGroup().addComponent(botListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE).addContainerGap()));
        fileMenu.setText("File");
        optionsItem.setText("Options");
        fileMenu.add(optionsItem);
        exitItem.setText("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(serverPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(botListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(serverPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(botListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private void botListMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            User user = (User) botList.getSelectedValue();
            if (user != null) {
                BotLeecher botLeecher = mediator.getIcrConnection().makeLeecher(user);
                BotPanel botPanel = new BotPanel(botLeecher, user);
                final String tabName = user.getNick();
                tabbedPane.addTab(tabName, botPanel);
                JPanel tabHeader = new JPanel();
                tabHeader.setOpaque(false);
                tabHeader.setBorder(new EmptyBorder(0, 0, 0, 0));
                JLabel nameLabel = new JLabel(tabName);
                nameLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
                tabHeader.add(nameLabel);
                JButton closeButton = new JButton(imageLoader.loadImageIcon("/icons/cancel.png", "cancel icon"));
                closeButton.setBorderPainted(false);
                closeButton.setBorder(new EmptyBorder(0, 0, 0, 0));
                closeButton.setOpaque(false);
                closeButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                            if (tabName.equals(tabbedPane.getTitleAt(i))) {
                                tabbedPane.removeTabAt(i);
                            }
                        }
                    }
                });
                tabHeader.add(closeButton);
                tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabHeader);
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
        }
    }

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        mediator.connect();
    }

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt) {
        mediator.getIcrConnection().shutdown();
        setVisible(false);
        dispose();
    }

    /**
 *
 * @param message
 */
    public void writeToLog(String message) {
        logTextArea.append(message);
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
    }

    /**
 *
 * @param state
 */
    protected void setContolsActivated(boolean state) {
        connectButton.setEnabled(state);
        serverComboBox.setEnabled(state);
        roomComboBox.setEnabled(state);
    }

    /**
 *
 * @return
 */
    protected String getServer() {
        return String.valueOf(serverComboBox.getSelectedItem());
    }

    /**
 *
 * @return
 */
    protected String getRoom() {
        return String.valueOf(roomComboBox.getSelectedItem());
    }

    /**
 *
 * @param message
 */
    protected void showException(String message) {
        JOptionPane.showMessageDialog(this, message, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    /**
 * Sets the user list, call this on the edt!
 * @param users 
 */
    public void setUsers(User[] users) {
        botList.setListData(users);
    }

    private javax.swing.JList botList;

    private javax.swing.JPanel botListPanel;

    private javax.swing.JScrollPane botListScrollPane;

    private javax.swing.JButton connectButton;

    private javax.swing.JMenuItem exitItem;

    private javax.swing.JMenu fileMenu;

    private javax.swing.JScrollPane logScrollPane;

    private javax.swing.JTextArea logTextArea;

    private javax.swing.JMenuBar menuBar;

    private javax.swing.JMenuItem optionsItem;

    private javax.swing.JComboBox roomComboBox;

    private javax.swing.JLabel roomLabel;

    private javax.swing.JComboBox serverComboBox;

    private javax.swing.JLabel serverLabel;

    private javax.swing.JPanel serverPanel;

    private javax.swing.JTabbedPane tabbedPane;
}
