package net.kano.joustsim.oscar.oscar.service.icbm;

import net.kano.joscar.flapcmd.SnacCommand;
import net.kano.joscar.snac.SnacRequestAdapter;
import net.kano.joscar.snac.SnacRequestSentEvent;
import net.kano.joscar.snac.SnacResponseEvent;
import net.kano.joscar.snaccmd.error.SnacError;
import net.kano.joscar.snaccmd.icbm.MessageAck;
import net.kano.joscar.snaccmd.icbm.SendImIcbm;
import net.kano.joustsim.Screenname;
import net.kano.joustsim.oscar.AimConnection;
import net.kano.joustsim.oscar.BuddyInfoTrackerListener;
import java.util.Date;

public class ImConversation extends Conversation implements TypingNotificationConversation {

    private final AimConnection conn;

    private final BuddyInfoTrackerListener tracker = new BuddyInfoTrackerListener() {
    };

    ImConversation(AimConnection conn, Screenname buddy) {
        super(buddy);
        this.conn = conn;
        setAlwaysOpen();
    }

    protected void opened() {
        conn.getBuddyInfoTracker().addTracker(getBuddy(), tracker);
    }

    protected void closed() {
        conn.getBuddyInfoTracker().removeTracker(getBuddy(), tracker);
    }

    public void sendMessage(final Message msg) throws ConversationException {
        IcbmService service = conn.getIcbmService();
        if (service == null) {
            throw new ConversationException("no ICBM service to send to", this);
        }
        ((MutableIcbmService) service).sendIM(getBuddy(), msg.getMessageBody(), msg.isAutoResponse(), new SnacRequestAdapter() {

            private boolean waitingForAck = false;

            public void handleSent(SnacRequestSentEvent e) {
                SnacCommand outCmd = e.getRequest().getCommand();
                if (outCmd instanceof SendImIcbm) {
                    SendImIcbm imIcbm = (SendImIcbm) outCmd;
                    if (imIcbm.isAckRequested()) {
                        waitingForAck = true;
                        return;
                    }
                }
                fireMessageSentEvent(msg, conn.getScreenname());
            }

            public void handleResponse(SnacResponseEvent e) {
                SnacCommand snac = e.getSnacCommand();
                if (snac instanceof MessageAck) {
                    if (waitingForAck) {
                        fireMessageSentEvent(msg, conn.getScreenname());
                        waitingForAck = false;
                    }
                } else if (snac instanceof SnacError) {
                    SnacError error = (SnacError) snac;
                    Screenname mysn = conn.getScreenname();
                    SendFailedEvent event = new ImSendFailedEvent(mysn, getBuddy(), error, msg);
                    for (ConversationListener l : getListeners()) {
                        l.gotOtherEvent(ImConversation.this, event);
                    }
                }
            }
        });
        fireOutgoingEvent(ImMessageInfo.getInstance(conn.getScreenname(), getBuddy(), msg, new Date()));
    }

    protected void handleIncomingEvent(ConversationEventInfo event) {
        assert !Thread.holdsLock(this);
        super.handleIncomingEvent(event);
    }

    public void setTypingState(TypingState typingState) {
        ((MutableIcbmService) conn.getIcbmService()).sendTypingStatus(getBuddy(), typingState);
        fireOutgoingEvent(new TypingInfo(conn.getScreenname(), getBuddy(), new Date(), typingState));
    }

    public void handleMissedMsg(MissedImInfo info) {
        for (ConversationListener listener : getListeners()) {
            if (listener instanceof ImConversationListener) {
                ImConversationListener imlistener = (ImConversationListener) listener;
                imlistener.missedMessages(this, info);
            }
        }
    }
}
