package net.sf.catchup.client.conversation;

import net.sf.catchup.client.ClientInfoStore;
import net.sf.catchup.common.InstantMessage;
import net.sf.catchup.common.InstantMessage.IMType;

public class InstantMessageProcessor {

    private static GroupConversationMgr grpConvMgr = GroupConversationMgr.getInstance();

    private static PersonalConversationMgr personalConvMgr = PersonalConversationMgr.getInstance();

    public static void processMessage(final InstantMessage im) {
        if (im.getType() == IMType.CONFERENCE_MSG) {
            grpConvMgr.processMessage(im.getSessionID(), im);
        } else {
            final String peerUsername;
            if (im.getFromUser().equalsIgnoreCase(ClientInfoStore.getPeerSocketWrapper().getUsername())) {
                peerUsername = im.getTargetUsers()[0];
            } else {
                peerUsername = im.getFromUser();
            }
            personalConvMgr.processMessage(peerUsername, im);
        }
    }
}
