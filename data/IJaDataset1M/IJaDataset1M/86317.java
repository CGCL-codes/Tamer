package org.jivesoftware.spark.ui.conferences;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jivesoftware.resource.Res;
import org.jivesoftware.resource.SparkRes;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.bookmark.BookmarkedConference;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.spark.ChatManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.component.JiveTreeNode;
import org.jivesoftware.spark.component.RolloverButton;
import org.jivesoftware.spark.component.Table;
import org.jivesoftware.spark.component.TitlePanel;
import org.jivesoftware.spark.component.Tree;
import org.jivesoftware.spark.ui.ChatRoomNotFoundException;
import org.jivesoftware.spark.ui.rooms.GroupChatRoom;
import org.jivesoftware.spark.util.ResourceUtils;
import org.jivesoftware.spark.util.SwingWorker;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

/**
 * A UI that handles all Group Rooms contained in an XMPP Messenger server.  This handles
 * creation and joining of rooms for group chat discussions as well as the listing
 * of the creation times, number of occupants in a room, and the room name itself.
 */
public class ConferenceRoomBrowser extends JPanel implements ActionListener, ComponentListener {

    private static final long serialVersionUID = -4483998189117467048L;

    private final RoomList roomsTable;

    private final RolloverButton createButton = new RolloverButton("", SparkRes.getImageIcon(SparkRes.SMALL_USER1_NEW));

    private final RolloverButton joinRoomButton = new RolloverButton("", SparkRes.getImageIcon(SparkRes.DOOR_IMAGE));

    private final RolloverButton refreshButton = new RolloverButton("", SparkRes.getImageIcon(SparkRes.REFRESH_IMAGE));

    private final RolloverButton addRoomButton = new RolloverButton("", SparkRes.getImageIcon(SparkRes.ADD_BOOKMARK_ICON));

    private final RolloverButton showHiddenButtons = new RolloverButton(SparkRes.getImageIcon(SparkRes.PANE_UP_ARROW_IMAGE));

    private JMenuItem joinRoomItem;

    private JMenuItem addRoomItem;

    private JMenuItem createItem;

    private JMenuItem refreshItem;

    private ChatManager chatManager;

    private JDialog dlg;

    private BookmarksUI conferences;

    private String serviceName;

    private int allButtonWidth;

    private int threeButtonWidth;

    private int twoButtonWidth;

    private int oneButtonWidth;

    private boolean partialDiscovery = false;

    private JPopupMenu popup;

    /**
     * Creates a new instance of ConferenceRooms.
     *
     * @param conferences the conference ui.
     * @param serviceName the name of the conference service.
     *                    //TODO This needs to be refactored.
     */
    public ConferenceRoomBrowser(BookmarksUI conferences, final String serviceName) {
        this.setLayout(new BorderLayout());
        this.conferences = conferences;
        this.serviceName = serviceName;
        popup = new JPopupMenu();
        joinRoomItem = new JMenuItem(Res.getString("menuitem.join.room"));
        addRoomItem = new JMenuItem(Res.getString("menuitem.bookmark.room"));
        createItem = new JMenuItem(Res.getString("menuitem.create.room"));
        refreshItem = new JMenuItem(Res.getString("menuitem.refresh"));
        joinRoomItem.setIcon(SparkRes.getImageIcon(SparkRes.DOOR_IMAGE));
        addRoomItem.setIcon(SparkRes.getImageIcon(SparkRes.ADD_BOOKMARK_ICON));
        createItem.setIcon(SparkRes.getImageIcon(SparkRes.SMALL_USER1_NEW));
        refreshItem.setIcon(SparkRes.getImageIcon(SparkRes.REFRESH_IMAGE));
        popup.add(joinRoomItem);
        popup.add(addRoomItem);
        popup.add(createItem);
        popup.add(refreshItem);
        final JPanel Hauptpanel = new JPanel(new BorderLayout());
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pane_hiddenButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbar.add(joinRoomButton);
        toolbar.add(addRoomButton);
        toolbar.add(createButton);
        toolbar.add(refreshButton);
        pane_hiddenButtons.add(showHiddenButtons);
        Hauptpanel.add(toolbar, BorderLayout.WEST);
        Hauptpanel.add(pane_hiddenButtons, BorderLayout.EAST);
        this.add(Hauptpanel, BorderLayout.NORTH);
        createButton.addActionListener(this);
        createItem.addActionListener(this);
        joinRoomButton.addActionListener(this);
        refreshButton.addActionListener(this);
        ResourceUtils.resButton(createButton, Res.getString("button.create.room"));
        ResourceUtils.resButton(joinRoomButton, Res.getString("button.join.room"));
        ResourceUtils.resButton(refreshButton, Res.getString("button.refresh"));
        ResourceUtils.resButton(addRoomButton, Res.getString("button.bookmark.room"));
        refreshButton.setToolTipText(Res.getString("message.update.room.list"));
        joinRoomButton.setToolTipText(Res.getString("message.join.conference.room"));
        createButton.setToolTipText(Res.getString("message.create.or.join.room"));
        roomsTable = new RoomList();
        final JScrollPane pane = new JScrollPane(roomsTable);
        pane.setBackground(Color.white);
        pane.setForeground(Color.white);
        this.setBackground(Color.white);
        this.setForeground(Color.white);
        pane.getViewport().setBackground(Color.white);
        this.add(pane, BorderLayout.CENTER);
        chatManager = SparkManager.getChatManager();
        joinRoomButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                joinSelectedRoom();
            }
        });
        addRoomButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                bookmarkRoom(serviceName);
            }
        });
        refreshButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                refreshRoomList(serviceName);
            }
        });
        joinRoomItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                joinSelectedRoom();
            }
        });
        addRoomItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                bookmarkRoom(serviceName);
            }
        });
        refreshItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                refreshRoomList(serviceName);
            }
        });
        showHiddenButtons.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                popup.show(showHiddenButtons, 0, showHiddenButtons.getHeight());
            }
        });
        joinRoomButton.setEnabled(false);
        addRoomButton.setEnabled(false);
        joinRoomItem.setEnabled(false);
        addRoomItem.setEnabled(false);
        addTableListener();
    }

    private void refreshRoomList(final String serviceName) {
        roomsTable.clearTable();
        SwingWorker worker = new SwingWorker() {

            Collection result;

            public Object construct() {
                result = getRoomsAndInfo(serviceName);
                return result;
            }

            public void finished() {
                try {
                    for (Object aResult : result) {
                        RoomObject obj = (RoomObject) aResult;
                        addRoomToTable(obj.getRoomJID(), obj.getRoomName(), obj.getNumberOfOccupants());
                    }
                } catch (Exception e) {
                    Log.error("Unable to retrieve room list and info.", e);
                }
            }
        };
        worker.start();
    }

    private Collection getRoomsAndInfo(final String serviceName) {
        List<RoomObject> roomList = new ArrayList<RoomObject>();
        boolean stillSearchForOccupants = true;
        try {
            Collection result = getRoomList(serviceName);
            try {
                for (Object aResult : result) {
                    HostedRoom hostedRoom = (HostedRoom) aResult;
                    String roomName = hostedRoom.getName();
                    String roomJID = hostedRoom.getJid();
                    int numberOfOccupants = -1;
                    if (stillSearchForOccupants) {
                        RoomInfo roomInfo = null;
                        try {
                            roomInfo = MultiUserChat.getRoomInfo(SparkManager.getConnection(), roomJID);
                        } catch (Exception e) {
                        }
                        if (roomInfo != null) {
                            numberOfOccupants = roomInfo.getOccupantsCount();
                            if (numberOfOccupants == -1) {
                                stillSearchForOccupants = false;
                            }
                        } else {
                            stillSearchForOccupants = false;
                        }
                    }
                    RoomObject obj = new RoomObject();
                    obj.setRoomJID(roomJID);
                    obj.setRoomName(roomName);
                    obj.setNumberOfOccupants(numberOfOccupants);
                    roomList.add(obj);
                }
            } catch (Exception e) {
                Log.error("Error setting up GroupChatTable", e);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return roomList;
    }

    private void bookmarkRoom(String serviceName) {
        int selectedRow = roomsTable.getSelectedRow();
        if (-1 == selectedRow) {
            JOptionPane.showMessageDialog(dlg, Res.getString("message.select.add.room.to.add"), Res.getString("title.group.chat"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        final String roomJID = roomsTable.getValueAt(selectedRow, 2) + "@" + serviceName;
        final String roomName = roomsTable.getValueAt(selectedRow, 1).toString();
        try {
            final RoomInfo roomInfo = MultiUserChat.getRoomInfo(SparkManager.getConnection(), roomJID);
            if (!roomInfo.isPersistent()) {
                JOptionPane.showMessageDialog(dlg, Res.getString("message.bookmark.temporary.room.error"), Res.getString("title.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
        }
        Tree serviceTree = conferences.getTree();
        JiveTreeNode rootNode = (JiveTreeNode) serviceTree.getModel().getRoot();
        TreePath rootPath = serviceTree.findByName(serviceTree, new String[] { rootNode.toString(), serviceName });
        boolean isBookmarked = isBookmarked(roomJID);
        if (!isBookmarked) {
            JiveTreeNode node = (JiveTreeNode) serviceTree.getLastSelectedPathComponent();
            if (node == null) {
                TreePath path = serviceTree.findByName(serviceTree, new String[] { rootNode.toString(), ConferenceServices.getDefaultServiceName() });
                node = (JiveTreeNode) path.getLastPathComponent();
            }
            JiveTreeNode roomNode = new JiveTreeNode(roomName, false, SparkRes.getImageIcon(SparkRes.BOOKMARK_ICON));
            roomNode.setAssociatedObject(roomJID);
            node.add(roomNode);
            final DefaultTreeModel model = (DefaultTreeModel) serviceTree.getModel();
            model.nodeStructureChanged(node);
            serviceTree.expandPath(rootPath);
            roomsTable.getTableModel().setValueAt(new JLabel(SparkRes.getImageIcon(SparkRes.BOOKMARK_ICON)), selectedRow, 0);
            addBookmarkUI(false);
            conferences.addBookmark(roomName, roomJID, false);
        } else {
            TreePath path = serviceTree.findByName(serviceTree, new String[] { rootNode.toString(), serviceName, roomName });
            JiveTreeNode node = (JiveTreeNode) path.getLastPathComponent();
            final DefaultTreeModel model = (DefaultTreeModel) serviceTree.getModel();
            model.removeNodeFromParent(node);
            roomsTable.getTableModel().setValueAt(new JLabel(SparkRes.getImageIcon(SparkRes.BLANK_IMAGE)), selectedRow, 0);
            addBookmarkUI(true);
            String jid = (String) node.getAssociatedObject();
            conferences.removeBookmark(jid);
        }
    }

    private void joinSelectedRoom() {
        int selectedRow = roomsTable.getSelectedRow();
        if (-1 == selectedRow) {
            JOptionPane.showMessageDialog(dlg, Res.getString("message.select.room.to.join"), Res.getString("title.group.chat"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        enterRoom();
    }

    private void addTableListener() {
        roomsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                int selectedRow = roomsTable.getSelectedRow();
                if (selectedRow != -1) {
                    joinRoomButton.setEnabled(true);
                    joinRoomItem.setEnabled(true);
                    String roomJID = roomsTable.getValueAt(selectedRow, 2) + "@" + serviceName;
                    addRoomButton.setEnabled(true);
                    addRoomItem.setEnabled(true);
                    if (isBookmarked(roomJID)) {
                        addBookmarkUI(false);
                    } else {
                        addBookmarkUI(true);
                    }
                } else {
                    joinRoomButton.setEnabled(false);
                    addRoomButton.setEnabled(false);
                    joinRoomItem.setEnabled(false);
                    addRoomItem.setEnabled(false);
                    addBookmarkUI(true);
                }
            }
        });
    }

    /**
     * Displays the ConferenceRoomBrowser.
     */
    public void invoke() {
        SwingWorker worker = new SwingWorker() {

            Collection rooms;

            public Object construct() {
                try {
                    rooms = getRoomList(serviceName);
                } catch (Exception e) {
                    Log.error("Unable to retrieve list of rooms.", e);
                }
                return "OK";
            }

            public void finished() {
                if (rooms == null) {
                    JOptionPane.showMessageDialog(conferences, Res.getString("message.conference.info.error"), Res.getString("title.error"), JOptionPane.ERROR_MESSAGE);
                    if (dlg != null) {
                        dlg.dispose();
                    }
                }
                try {
                    for (Object room : rooms) {
                        HostedRoom hostedRoom = (HostedRoom) room;
                        String roomName = hostedRoom.getName();
                        String roomJID = hostedRoom.getJid();
                        int numberOfOccupants = -1;
                        if (!partialDiscovery) {
                            RoomInfo roomInfo = null;
                            try {
                                roomInfo = MultiUserChat.getRoomInfo(SparkManager.getConnection(), roomJID);
                            } catch (Exception e) {
                            }
                            if (roomInfo != null) {
                                numberOfOccupants = roomInfo.getOccupantsCount();
                            }
                            if (roomInfo == null || numberOfOccupants == -1) {
                                partialDiscovery = true;
                            }
                        }
                        addRoomToTable(roomJID, roomName, numberOfOccupants);
                    }
                } catch (Exception e) {
                    Log.error("Error setting up GroupChatTable", e);
                }
            }
        };
        worker.start();
        final JOptionPane pane;
        TitlePanel titlePanel;
        titlePanel = new TitlePanel(Res.getString("title.create.or.bookmark.room"), Res.getString("message.add.favorite.room"), SparkRes.getImageIcon(SparkRes.BLANK_IMAGE), true);
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        Object[] options = { Res.getString("close") };
        pane = new JOptionPane(this, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
        mainPanel.add(pane, BorderLayout.CENTER);
        final JOptionPane p = new JOptionPane();
        dlg = p.createDialog(SparkManager.getMainWindow(), Res.getString("title.browse.room.service", serviceName));
        dlg.setModal(false);
        dlg.pack();
        dlg.addComponentListener(this);
        if (Res.getBundle().getLocale().toString().equals("de")) dlg.setSize(700, 400); else dlg.setSize(650, 400);
        dlg.setResizable(true);
        dlg.setContentPane(mainPanel);
        dlg.setLocationRelativeTo(SparkManager.getMainWindow());
        PropertyChangeListener changeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                String value = (String) pane.getValue();
                if (Res.getString("close").equals(value)) {
                    pane.removePropertyChangeListener(this);
                    dlg.dispose();
                } else if (Res.getString("close").equals(value)) {
                    pane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                }
            }
        };
        pane.addPropertyChangeListener(changeListener);
        dlg.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    dlg.dispose();
                }
            }
        });
        setButtonsWidth();
        showHiddenButtons.setVisible(false);
        dlg.setVisible(true);
        dlg.toFront();
        dlg.requestFocus();
    }

    private final class RoomList extends Table {

        public RoomList() {
            super(new String[] { " ", Res.getString("title.name"), Res.getString("title.address"), Res.getString("title.occupants") });
            getColumnModel().setColumnMargin(0);
            getColumnModel().getColumn(0).setMaxWidth(30);
            getColumnModel().getColumn(3).setMaxWidth(80);
            setSelectionBackground(Table.SELECTION_COLOR);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            setRowSelectionAllowed(true);
            addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        enterRoom();
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    checkPopup(e);
                }

                public void mousePressed(MouseEvent e) {
                    checkPopup(e);
                }
            });
        }

        public TableCellRenderer getCellRenderer(int row, int column) {
            Object o = getValueAt(row, column);
            if (o != null) {
                if (o instanceof JLabel) {
                    return new JLabelRenderer(false);
                }
            }
            if (column == 3) {
                return new CenterRenderer();
            }
            return super.getCellRenderer(row, column);
        }

        private void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                final JPopupMenu popupMenu = new JPopupMenu();
                Action roomInfoAction = new AbstractAction() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        int selectedRow = roomsTable.getSelectedRow();
                        if (selectedRow != -1) {
                            String roomJID = roomsTable.getValueAt(selectedRow, 2) + "@" + serviceName;
                            RoomBrowser roomBrowser = new RoomBrowser();
                            roomBrowser.displayRoomInformation(roomJID);
                        }
                    }
                };
                roomInfoAction.putValue(Action.NAME, Res.getString("menuitem.view.room.info"));
                roomInfoAction.putValue(Action.SMALL_ICON, SparkRes.getImageIcon(SparkRes.SMALL_DATA_FIND_IMAGE));
                popupMenu.add(roomInfoAction);
                popupMenu.show(roomsTable, e.getX(), e.getY());
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton || e.getSource() == createItem) {
            createRoom();
        }
    }

    private void enterRoom() {
        int selectedRow = roomsTable.getSelectedRow();
        if (-1 == selectedRow) {
            JOptionPane.showMessageDialog(dlg, Res.getString("message.select.room.to.enter"), Res.getString("title.group.chat"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        final String roomJID = roomsTable.getValueAt(selectedRow, 2) + "@" + serviceName;
        final String roomDescription = (String) roomsTable.getValueAt(selectedRow, 1);
        try {
            chatManager.getChatContainer().getChatRoom(roomJID);
        } catch (ChatRoomNotFoundException e1) {
            ConferenceUtils.joinConferenceOnSeperateThread(roomDescription, roomJID, null);
        }
    }

    /**
     * Returns a Collection of all rooms in the specified Conference Service.
     *
     * @param serviceName the name of the conference service.
     * @return a Collection of all rooms in the Conference service.
     * @throws Exception if a problem occurs while getting the room list
     */
    private static Collection getRoomList(String serviceName) throws Exception {
        return MultiUserChat.getHostedRooms(SparkManager.getConnection(), serviceName);
    }

    /**
     * Create a new room based on room table selection.
     */
    private void createRoom() {
        RoomCreationDialog mucRoomDialog = new RoomCreationDialog();
        final MultiUserChat groupChat = mucRoomDialog.createGroupChat(SparkManager.getMainWindow(), serviceName);
        LocalPreferences pref = SettingsManager.getLocalPreferences();
        if (null != groupChat) {
            try {
                GroupChatRoom room = new GroupChatRoom(groupChat);
                groupChat.create(pref.getNickname());
                chatManager.getChatContainer().addChatRoom(room);
                chatManager.getChatContainer().activateChatRoom(room);
                Form form = groupChat.getConfigurationForm().createAnswerForm();
                if (mucRoomDialog.isPasswordProtected()) {
                    String password = mucRoomDialog.getPassword();
                    form.setAnswer("muc#roomconfig_passwordprotectedroom", true);
                    form.setAnswer("muc#roomconfig_roomsecret", password);
                }
                form.setAnswer("muc#roomconfig_roomname", mucRoomDialog.getRoomName());
                if (mucRoomDialog.isPermanent()) {
                    form.setAnswer("muc#roomconfig_persistentroom", true);
                }
                List<String> owners = new ArrayList<String>();
                owners.add(SparkManager.getSessionManager().getBareAddress());
                form.setAnswer("muc#roomconfig_roomowners", owners);
                groupChat.sendConfigurationForm(form);
                addRoomToTable(groupChat.getRoom(), StringUtils.parseName(groupChat.getRoom()), 1);
            } catch (XMPPException e1) {
                Log.error("Error creating new room.", e1);
                JOptionPane.showMessageDialog(this, Res.getString("message.room.creation.error"), Res.getString("title.error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Adds a room to the room table.
     *
     * @param jid               the jid of the conference room.
     * @param roomName          the name of the conference room.
     * @param numberOfOccupants the number of occupants in the conference room. If -1 is specified,
     *                          the the occupant count will show as n/a.
     */
    private void addRoomToTable(String jid, String roomName, int numberOfOccupants) {
        JLabel bookmarkedLabel = new JLabel();
        if (isBookmarked(jid)) {
            bookmarkedLabel.setIcon(SparkRes.getImageIcon(SparkRes.BOOKMARK_ICON));
        }
        String occupants = Integer.toString(numberOfOccupants);
        if (numberOfOccupants == -1) {
            occupants = "n/a";
        }
        final Object[] insertRoom = new Object[] { bookmarkedLabel, roomName, StringUtils.parseName(jid), occupants };
        roomsTable.getTableModel().addRow(insertRoom);
    }

    /**
     * Returns true if the room specified is bookmarked.
     *
     * @param roomJID the jid of the room to check.
     * @return true if the room is bookmarked.
     */
    private boolean isBookmarked(String roomJID) {
        for (Object o : conferences.getBookmarks()) {
            BookmarkedConference bk = (BookmarkedConference) o;
            String jid = bk.getJid();
            if (jid != null && roomJID.equals(jid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Toggles the bookmark room button depending on it's state.
     *
     * @param addBookmark true if the button should display itself as bookmarkable :)
     */
    private void addBookmarkUI(boolean addBookmark) {
        if (!addBookmark) {
            addRoomButton.setText(Res.getString("button.remove.bookmark"));
            addRoomButton.setIcon(SparkRes.getImageIcon(SparkRes.DELETE_BOOKMARK_ICON));
        } else {
            ResourceUtils.resButton(addRoomButton, Res.getString("button.bookmark.room"));
            addRoomButton.setIcon(SparkRes.getImageIcon(SparkRes.ADD_BOOKMARK_ICON));
        }
    }

    static class CenterRenderer extends DefaultTableCellRenderer {

        public CenterRenderer() {
            setHorizontalAlignment(CENTER);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            return this;
        }
    }

    private class RoomObject {

        private String roomName;

        private String roomJID;

        int numberOfOccupants;

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getRoomJID() {
            return roomJID;
        }

        public void setRoomJID(String roomJID) {
            this.roomJID = roomJID;
        }

        public int getNumberOfOccupants() {
            return numberOfOccupants;
        }

        public void setNumberOfOccupants(int numberOfOccupants) {
            this.numberOfOccupants = numberOfOccupants;
        }
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        if (this.getWidth() <= (oneButtonWidth + 19)) {
            joinRoomButton.setVisible(false);
            addRoomButton.setVisible(false);
            createButton.setVisible(false);
            refreshButton.setVisible(false);
            joinRoomItem.setVisible(true);
            addRoomItem.setVisible(true);
            createItem.setVisible(true);
            refreshItem.setVisible(true);
            showHiddenButtons.setVisible(true);
        } else if (this.getWidth() <= (twoButtonWidth + 19)) {
            joinRoomButton.setVisible(true);
            addRoomButton.setVisible(false);
            createButton.setVisible(false);
            refreshButton.setVisible(false);
            joinRoomItem.setVisible(false);
            addRoomItem.setVisible(true);
            createItem.setVisible(true);
            refreshItem.setVisible(true);
            showHiddenButtons.setVisible(true);
        } else if (this.getWidth() <= (threeButtonWidth + 19)) {
            joinRoomButton.setVisible(true);
            addRoomButton.setVisible(true);
            createButton.setVisible(false);
            refreshButton.setVisible(false);
            joinRoomItem.setVisible(false);
            addRoomItem.setVisible(false);
            createItem.setVisible(true);
            refreshItem.setVisible(true);
            showHiddenButtons.setVisible(true);
        } else if (this.getWidth() <= (allButtonWidth + 19)) {
            joinRoomButton.setVisible(true);
            addRoomButton.setVisible(true);
            createButton.setVisible(true);
            refreshButton.setVisible(false);
            joinRoomItem.setVisible(false);
            addRoomItem.setVisible(false);
            createItem.setVisible(false);
            refreshItem.setVisible(true);
            showHiddenButtons.setVisible(true);
        } else if (this.getWidth() > (allButtonWidth + 19)) {
            joinRoomButton.setVisible(true);
            addRoomButton.setVisible(true);
            createButton.setVisible(true);
            refreshButton.setVisible(true);
            joinRoomItem.setVisible(false);
            addRoomItem.setVisible(false);
            createItem.setVisible(false);
            refreshItem.setVisible(false);
            showHiddenButtons.setVisible(false);
        }
    }

    public void componentShown(ComponentEvent e) {
    }

    private void setButtonsWidth() {
        allButtonWidth = createButton.getWidth() + refreshButton.getWidth() + addRoomButton.getWidth() + joinRoomButton.getWidth();
        threeButtonWidth = createButton.getWidth() + addRoomButton.getWidth() + joinRoomButton.getWidth() + showHiddenButtons.getWidth();
        twoButtonWidth = addRoomButton.getWidth() + joinRoomButton.getWidth() + showHiddenButtons.getWidth();
        oneButtonWidth = joinRoomButton.getWidth() + showHiddenButtons.getWidth();
    }
}
