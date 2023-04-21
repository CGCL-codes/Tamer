package org.columba.chat.ui.roaster;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.columba.chat.Connection;
import org.columba.chat.MainInterface;
import org.columba.chat.command.AddContactCommand;
import org.columba.chat.command.ChatCommandReference;
import org.columba.chat.command.SubscriptionCommand;
import org.columba.chat.conn.api.ConnectionChangedEvent;
import org.columba.chat.conn.api.IConnectionChangedListener;
import org.columba.chat.conn.api.IConnection.STATUS;
import org.columba.chat.model.BuddyList;
import org.columba.chat.model.BuddyStatus;
import org.columba.chat.model.api.IBuddyStatus;
import org.columba.chat.ui.frame.api.IChatFrameMediator;
import org.columba.chat.ui.roaster.api.IRoasterController;
import org.columba.core.command.CommandProcessor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * @author fdietz
 * 
 */
public class RoasterTree extends JTree implements IRoasterController, IConnectionChangedListener {

    private static final Logger LOG = Logger.getLogger("org.columba.chat.ui.roaster");

    private DefaultTreeModel model;

    private DefaultMutableTreeNode root;

    private IChatFrameMediator mediator;

    private SubscriptionListener subscriptionListener = new SubscriptionListener();

    private PresenceListener presenceListener = new PresenceListener();

    public RoasterTree(IChatFrameMediator mediator) {
        this.mediator = mediator;
        root = new DefaultMutableTreeNode("Roster");
        model = new DefaultTreeModel(root);
        setModel(model);
        setCellRenderer(new RoasterTreeRenderer());
        setRootVisible(false);
        setShowsRootHandles(true);
        MainInterface.connection.addConnectionChangedListener(this);
    }

    public IBuddyStatus getSelected() {
        TreePath path = getSelectionPath();
        if (path == null) return null;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node != null) {
            IBuddyStatus b = (IBuddyStatus) node.getUserObject();
            return b;
        }
        return null;
    }

    public void updateBuddyPresence(IBuddyStatus buddy) {
        DefaultMutableTreeNode node = findBuddy(root, buddy);
        if (node != null) {
            model.nodeChanged(node);
        }
        updateUI();
    }

    private DefaultMutableTreeNode findBuddy(DefaultMutableTreeNode parent, IBuddyStatus buddy) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
            Object o = child.getUserObject();
            if (o instanceof BuddyStatus) {
                if (buddy.getJabberId().equals(((IBuddyStatus) o).getJabberId())) return child;
            }
        }
        return null;
    }

    public void populate(DefaultMutableTreeNode rootNode) {
        this.root = rootNode;
        model.setRoot(root);
        model.nodeStructureChanged(root);
    }

    class SubscriptionListener implements PacketListener {

        public SubscriptionListener() {
            super();
        }

        /**
		 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
		 */
        public void processPacket(Packet p) {
            Presence presence = (Presence) p;
            if (presence.getType().equals(Presence.Type.SUBSCRIBE)) {
                String from = presence.getFrom();
                String normalizedFrom = from.replaceAll("\\/.*", "");
                int option = JOptionPane.showConfirmDialog(null, "The user " + from + " requests presence notification.\nDo you wish to allow them to see your " + "online presence?", "Subscription Request", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    CommandProcessor.getInstance().addOp(new SubscriptionCommand(mediator, new ChatCommandReference(normalizedFrom)));
                } else {
                    return;
                }
                option = JOptionPane.showConfirmDialog(null, "Do you wish to add " + from + " to your roaster?", "Add user", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    CommandProcessor.getInstance().addOp(new AddContactCommand(mediator, new ChatCommandReference(normalizedFrom)));
                }
            }
        }
    }

    class PresenceListener implements PacketListener {

        public PresenceListener() {
            super();
        }

        /**
		 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
		 */
        public void processPacket(Packet packet) {
            Presence presence = (Presence) packet;
            String from = presence.getFrom();
            if ((presence.getType() != Presence.Type.AVAILABLE) && (presence.getType() != Presence.Type.UNAVAILABLE)) return;
            LOG.info("From=" + from);
            LOG.info("Presence Mode=" + presence.getMode());
            String normalizedFrom = from.replaceAll("\\/.*", "");
            final IBuddyStatus status = BuddyList.getInstance().getBuddy(normalizedFrom);
            if (status == null) return;
            status.setPresenceMode(presence.getMode());
            if (presence.getType() == Presence.Type.AVAILABLE) {
                status.setSignedOn(true);
            } else if (presence.getType() == Presence.Type.UNAVAILABLE) {
                status.setSignedOn(false);
            }
            Runnable updateAComponent = new Runnable() {

                public void run() {
                    updateBuddyPresence(status);
                }
            };
            try {
                SwingUtilities.invokeAndWait(updateAComponent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * @see org.columba.chat.conn.api.IConnectionChangedListener#connectionChanged(org.columba.chat.conn.api.ConnectionChangedEvent)
	 */
    public void connectionChanged(ConnectionChangedEvent object) {
        STATUS status = object.getStatus();
        if (status == STATUS.ONLINE) {
            setEnabled(true);
            Connection.XMPPConnection.addPacketListener(subscriptionListener, new PacketTypeFilter(Presence.class));
            Connection.XMPPConnection.addPacketListener(presenceListener, new PacketTypeFilter(Presence.class));
        } else if (status == STATUS.OFFLINE) {
            setEnabled(false);
            Connection.XMPPConnection.removePacketListener(subscriptionListener);
            Connection.XMPPConnection.removePacketListener(presenceListener);
        }
    }
}
