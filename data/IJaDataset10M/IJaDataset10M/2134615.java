package org.alliance.ui.windows;

import com.stendahls.nif.ui.OptionDialog;
import com.stendahls.nif.ui.mdi.MDIManager;
import com.stendahls.nif.ui.mdi.MDIWindow;
import com.stendahls.util.TextUtils;
import org.alliance.core.node.Friend;
import org.alliance.core.node.MyNode;
import org.alliance.core.node.Node;
import org.alliance.ui.UISubsystem;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: maciek
 * Date: 2005-dec-30
 * Time: 16:22:07
 */
public class FriendListMDIWindow extends AllianceMDIWindow {

    private UISubsystem ui;

    private JList list;

    private ImageIcon iconFriendDimmed, iconFriendOld;

    private JLabel statusleft, statusright;

    private String[] LEVEL_NAMES = { "Rookie", "True Member", "Experienced", "King" };

    private String[] LEVEL_ICONS = { "friend_lame", "friend", "friend_cool", "friend_king" };

    private ImageIcon[] friendIcons;

    private ImageIcon[] friendIconsAway;

    private JPopupMenu popup;

    public FriendListMDIWindow() {
    }

    public FriendListMDIWindow(MDIManager manager, UISubsystem ui) throws Exception {
        super(manager, "friendlist", ui);
        this.ui = ui;
        friendIcons = new ImageIcon[LEVEL_ICONS.length];
        friendIconsAway = new ImageIcon[LEVEL_ICONS.length];
        for (int i = 0; i < LEVEL_ICONS.length; i++) {
            friendIcons[i] = new ImageIcon(ui.getRl().getResource("gfx/icons/" + LEVEL_ICONS[i] + ".png"));
            friendIconsAway[i] = new ImageIcon(ui.getRl().getResource("gfx/icons/" + LEVEL_ICONS[i] + "_away.png"));
        }
        iconFriendDimmed = new ImageIcon(ui.getRl().getResource("gfx/icons/friend_dimmed.png"));
        iconFriendOld = new ImageIcon(ui.getRl().getResource("gfx/icons/friend_old.png"));
        setWindowType(WINDOWTYPE_NAVIGATION);
        statusleft = (JLabel) xui.getComponent("statusleft");
        statusright = (JLabel) xui.getComponent("statusright");
        createUI();
        setTitle("My  Network");
    }

    public void update() {
        statusright.setText("Online: " + ui.getCore().getFriendManager().getNFriendsConnected() + "/" + ui.getCore().getFriendManager().getNFriends() + " (" + TextUtils.formatByteSize(ui.getCore().getFriendManager().getTotalBytesShared()) + ")");
    }

    static {
        final SystemFlavorMap sfm = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
        final String nat = "text/plain";
        final DataFlavor df = new DataFlavor("text/plain; charset=ASCII; class=java.io.InputStream", "Plain Text");
        sfm.addUnencodedNativeForFlavor(df, nat);
        sfm.addFlavorForUnencodedNative(nat, df);
    }

    private void createUI() throws Exception {
        list = new JList(ui.getFriendListModel());
        SystemFlavorMap.getDefaultFlavorMap();
        list.setCellRenderer(new FriendListRenderer());
        ((JScrollPane) xui.getComponent("scrollpanel")).setViewportView(list);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            private int selectedIndex = -1;

            public void valueChanged(ListSelectionEvent e) {
                try {
                    if (!e.getValueIsAdjusting() && selectedIndex != list.getSelectedIndex()) {
                        selectedIndex = list.getSelectedIndex();
                        EVENT_viewshare(null);
                    }
                } catch (Exception e1) {
                    ui.handleErrorInEventLoop(e1);
                }
            }
        });
        popup = (JPopupMenu) xui.getComponent("popup");
        list.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = list.locationToIndex(e.getPoint());
                    boolean b = false;
                    for (int r : list.getSelectedIndices()) {
                        if (r == row) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) list.getSelectionModel().setSelectionInterval(row, row);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        updateMyLevelInformation();
        postInit();
    }

    public void updateMyLevelInformation() throws IOException {
        ((JLabel) xui.getComponent("myname")).setText(ui.getCore().getFriendManager().getMe().getNickname());
        ((JLabel) xui.getComponent("mylevel")).setText(getLevelName(getMyLevel()));
        ((JLabel) xui.getComponent("myicon")).setIcon(new ImageIcon(ui.getRl().getResource(getLevelIcon(getMyLevel(), true))));
        String s = "";
        switch(getMyNumberOfInvites()) {
            case 0:
                s = "Invite 1 friend to become ";
                break;
            case 1:
                s = "Invite 2 friends to become ";
                break;
            case 2:
                s = "Invite 1 friend to become ";
                break;
            default:
                s = "Invite " + (ui.getCore().getFriendManager().getNumberOfInvitesNeededToBeKing() - getMyNumberOfInvites()) + " friend to become ";
                break;
        }
        if (getMyLevel() < LEVEL_NAMES.length - 1) {
            s += "'" + getLevelName(getMyLevel() + 1) + "' (";
            ((JLabel) xui.getComponent("nextLevelText")).setText(s);
            ((JLabel) xui.getComponent("nextLevelIcon")).setIcon(new ImageIcon(ui.getRl().getResource(getLevelIcon(getMyLevel() + 1, false))));
            ((JLabel) xui.getComponent("levelEnding")).setText(")");
        } else {
            ((JLabel) xui.getComponent("nextLevelText")).setText("");
            ((JLabel) xui.getComponent("nextLevelIcon")).setText("");
            ((JLabel) xui.getComponent("nextLevelIcon")).setIcon(null);
            ((JLabel) xui.getComponent("levelEnding")).setText("");
        }
    }

    private String getLevelIcon(int myLevel, boolean big) {
        if (myLevel < 0) myLevel = 0;
        if (myLevel >= LEVEL_ICONS.length) myLevel = LEVEL_ICONS.length - 1;
        return "gfx/icons/" + LEVEL_ICONS[myLevel] + (big ? "_big" : "") + ".png";
    }

    private String getLevelName(int myLevel) {
        if (myLevel < 0) myLevel = 0;
        if (myLevel >= LEVEL_NAMES.length) myLevel = LEVEL_NAMES.length - 1;
        return LEVEL_NAMES[myLevel];
    }

    private int getMyLevel() {
        return getLevel(getMyNumberOfInvites());
    }

    private int getLevel(int numberOfInvites) {
        if (numberOfInvites == 0) return 0;
        if (numberOfInvites == 1) return 1;
        if (numberOfInvites == 2) return 1;
        if (numberOfInvites == 3) return 2;
        if (numberOfInvites >= ui.getCore().getFriendManager().getNumberOfInvitesNeededToBeKing()) return 3;
        return 2;
    }

    private int getMyNumberOfInvites() {
        return ui.getCore().getSettings().getMy().getInvitations();
    }

    public void save() throws Exception {
    }

    public String getIdentifier() {
        return "friendlist";
    }

    public void revert() throws Exception {
        ui.getCore().invokeLater(new Runnable() {

            public void run() {
                try {
                    ui.getCore().refreshFriendInfo();
                } catch (IOException e) {
                    ui.handleErrorInEventLoop(e);
                }
            }
        });
    }

    public void serialize(ObjectOutputStream out) throws IOException {
    }

    public MDIWindow deserialize(ObjectInputStream in) throws IOException {
        return null;
    }

    private class FriendListRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Node n = (Node) value;
            if (n.isConnected()) {
                if (!n.isAway()) {
                    setIcon(friendIcons[getLevel(n.getNumberOfInvitedFriends())]);
                } else {
                    setIcon(friendIconsAway[getLevel(n.getNumberOfInvitedFriends())]);
                }
                if (isSelected) setForeground(Color.white); else setForeground(Color.black);
                String s = nickname(n.getGuid());
                if (n instanceof Friend) {
                } else {
                    s += " (myself)";
                }
                s += " (" + TextUtils.formatByteSize(n.getShareSize()) + ")";
                setText(s);
            } else if (n.hasNotBeenOnlineForLongTime()) {
                setIcon(iconFriendOld);
                setForeground(Color.lightGray);
                if (n.getLastSeenOnlineAt() != 0) {
                    setText(nickname(n.getGuid()) + " (offline for " + ((System.currentTimeMillis() - n.getLastSeenOnlineAt()) / 1000 / 60 / 60 / 24) + " days)");
                } else {
                    setText(nickname(n.getGuid()));
                }
            } else {
                setIcon(iconFriendDimmed);
                setForeground(Color.lightGray);
                setText(FriendListMDIWindow.this.ui.getCore().getFriendManager().nicknameWithContactPath(n.getGuid()));
            }
            String cp = FriendListMDIWindow.this.ui.getCore().getFriendManager().contactPath(n.getGuid());
            if (cp.trim().length() > 0) cp = "Found " + cp + "<br>";
            setToolTipText("<html>" + cp + "Share: " + TextUtils.formatByteSize(n.getShareSize()) + " in " + n.getNumberOfFilesShared() + " files<br>" + "Invited friends: " + n.getNumberOfInvitedFriends() + "<br>" + "Upload speed record: " + TextUtils.formatByteSize((long) n.getHighestOutgoingCPS()) + "/s<br>" + "Download speed record: " + TextUtils.formatByteSize((long) n.getHighestIncomingCPS()) + "/s<br>" + "Bytes uploaded: " + TextUtils.formatByteSize(n.getTotalBytesSent()) + "<br>" + "Bytes downloaded: " + TextUtils.formatByteSize(n.getTotalBytesReceived()) + "<br>" + "Ratio (ul:dl): " + n.calculateRatio() + "</html>");
            return this;
        }
    }

    private String nickname(int guid) {
        return ui.getCore().getFriendManager().nickname(guid);
    }

    public void EVENT_editname(ActionEvent e) {
        if (list.getSelectedValue() == null) return;
        if (list.getSelectedValue() instanceof MyNode) {
            OptionDialog.showInformationDialog(ui.getMainWindow(), "If you want to change your nickname you need to open the Options (View->Options)");
        } else {
            Friend f = (Friend) list.getSelectedValue();
            if (f != null) {
                String pi = JOptionPane.showInputDialog("Enter nickname for friend: " + nickname(f.getGuid()), nickname(f.getGuid()));
                if (pi != null) f.setNicknameToShowInUI(pi);
                ui.getFriendListModel().signalFriendChanged(f);
            }
        }
    }

    public void EVENT_chat(ActionEvent e) throws Exception {
        if (list.getSelectedValue() == null) return;
        if (list.getSelectedValue() instanceof MyNode) return;
        Friend f = (Friend) list.getSelectedValue();
        if (f != null) ui.getMainWindow().chatMessage(f.getGuid(), null, 0, false);
    }

    public void EVENT_reconnect(ActionEvent e) throws Exception {
        if (list.getSelectedValue() == null) return;
        if (list.getSelectedValue() instanceof MyNode) return;
        final Friend f = (Friend) list.getSelectedValue();
        if (f.isConnected()) f.reconnect();
    }

    public void EVENT_viewshare(ActionEvent e) throws Exception {
        if (list.getSelectedValue() == null) return;
        if (list.getSelectedValue() instanceof MyNode) {
            ui.getMainWindow().EVENT_myshare(null);
        } else {
            Friend f = (Friend) list.getSelectedValue();
            if (f != null) {
                if (!f.isConnected()) {
                } else {
                    ui.getMainWindow().viewShare(f);
                }
            }
        }
    }

    public void EVENT_addfriendwizard(ActionEvent e) throws Exception {
        ui.getMainWindow().EVENT_addfriendwizard(e);
    }

    public void EVENT_removefriend(ActionEvent e) throws Exception {
        if (list.getSelectedValue() == null) return;
        Object[] friends = list.getSelectedValues();
        if (friends != null && friends.length > 0) {
            Boolean delete = OptionDialog.showQuestionDialog(ui.getMainWindow(), "Are you sure you want to permanently delete these (" + friends.length + ") connections?");
            if (delete == null) return;
            if (delete) {
                for (Object friend : friends) {
                    Node f = (Node) friend;
                    if (f != null && f instanceof Friend) {
                        ui.getCore().getFriendManager().permanentlyRemove((Friend) f);
                    }
                }
                revert();
            }
        }
    }

    /**
     * Changes the hostname of a friend you have in your friendlist via the GUI. Can
     * be used to configure a hostname instead of the IP that's set via an invitation.
     * @author jpluebbert
     */
    public void EVENT_edithostname(ActionEvent e) {
        if (list.getSelectedValue() == null) return;
        if (!(list.getSelectedValue() instanceof Friend)) return;
        Friend friend = (Friend) list.getSelectedValue();
        if (friend != null) {
            String hostname = friend.getLastKnownHost();
            if (hostname == null) hostname = "";
            String input = JOptionPane.showInputDialog("Enter hostname for friend: " + nickname(friend.getGuid()), hostname);
            if (input != null && !hostname.equalsIgnoreCase(input)) {
                if (input.length() == 0) {
                    OptionDialog.showErrorDialog(ui.getMainWindow(), "Hostname can not be empty. Changes ignored.");
                } else {
                    friend.setLastKnownHost(input);
                    try {
                        if (friend.isConnected()) friend.reconnect(); else friend.connect();
                    } catch (IOException e1) {
                    }
                }
            }
        }
    }
}
