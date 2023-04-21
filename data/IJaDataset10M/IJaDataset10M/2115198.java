package net.sf.catchup.client.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.sf.catchup.client.ClientInfoStore;
import net.sf.catchup.common.ClientStatus;
import net.sf.catchup.common.InstantMessage;

public class ConferenceManager {

    private static final Map<String, ConversationDialog> conversationDialogMap = new HashMap<String, ConversationDialog>();

    private static final Object lock = new Object();

    /**
	 * Starts a conversation window with the specified {@link InstantMessage}
	 * object. If a conversation window exists for the buddy specified by the im
	 * object, the same will be reopened/reused. Else a new window will be
	 * created
	 * 
	 * @param im
	 * @param buddyOnline 
	 */
    public static void processMessage(final InstantMessage im) {
        final String chatPeer;
        if (im.getFromUser().equalsIgnoreCase(ClientInfoStore.getPeerSocketWrapper().getUsername())) {
            chatPeer = im.getTargetUsers()[0];
        } else {
            chatPeer = im.getFromUser();
        }
        synchronized (lock) {
            if (conversationDialogMap.containsKey(chatPeer)) {
                continueMessaging(chatPeer, im);
            } else {
                startMessaging(chatPeer, im);
            }
        }
    }

    private static void continueMessaging(String chatPeer, InstantMessage im) {
        final ConversationDialog conversationDialog = conversationDialogMap.get(chatPeer);
        conversationDialog.processIM(im);
        if (!conversationDialog.isShowing()) {
            conversationDialog.setVisible(true);
        }
    }

    private static void startMessaging(final String peerUsername, final InstantMessage im) {
        final ConversationDialog conversationDialog = new ConversationDialog(peerUsername);
        conversationDialog.setIconImage(BuddiesPanel.getInstance().getBuddyIcon(BuddiesPanel.getInstance().getBuddyStatus(peerUsername)).getImage());
        conversationDialogMap.put(peerUsername, conversationDialog);
        conversationDialog.createAndShowGUI();
        conversationDialog.processIM(im);
    }

    public static synchronized void resetChatIcon(final String chatPeer, final ClientStatus peerStatus) {
        final ConversationDialog conversationDialog = conversationDialogMap.get(chatPeer);
        if (conversationDialog != null) {
            conversationDialog.setIconImage(BuddiesPanel.getInstance().getBuddyIcon(peerStatus).getImage());
        }
    }

    public static void clearAll() {
        final Collection<ConversationDialog> conversationDialogs = conversationDialogMap.values();
        for (final ConversationDialog conversationDialog : conversationDialogs) {
            conversationDialog.dispose();
        }
        conversationDialogMap.clear();
    }
}
