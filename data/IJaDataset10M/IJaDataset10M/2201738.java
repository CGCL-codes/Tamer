package net.sf.jimo.modules.bot.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import net.sf.jimo.modules.im.api.Buddy;
import net.sf.jimo.modules.im.api.Conference;
import net.sf.jimo.modules.im.api.IMListener;
import net.sf.jimo.modules.im.api.Message;
import net.sf.jimo.modules.im.api.MessageComponent;
import net.sf.jimo.modules.im.api.Protocol;
import net.sf.jimo.modules.im.api.Response;
import net.sf.jimo.modules.im.api.SmileyComponent;
import net.sf.jimo.modules.im.api.TextComponent;
import net.sf.jimo.modules.im.api.URLComponent;
import net.sf.jimo.modules.im.api.exception.IMIllegalArgumentException;
import net.sf.jimo.modules.im.api.exception.IMIllegalStateException;
import net.sf.jimo.modules.im.api.log.LogUtil;

public class IMBotListener implements IMListener {

    private boolean isConnected = false;

    private Buddy[] buddiesReceived = null;

    private Buddy[] buddiesIgnored = null;

    public Buddy[] getBuddyListReceived() {
        return this.buddiesReceived;
    }

    public Buddy[] getBuddyListIgnored() {
        return this.buddiesIgnored;
    }

    public void connecting(Protocol protocol) {
    }

    public void connected(Protocol protocol) {
        this.isConnected = true;
        LogUtil.console(protocol.getProtocolName() + " connected!");
    }

    public void connectFailed(Protocol protocol, String message) {
        LogUtil.console("Failed to connect!");
    }

    public void disconnected(Protocol protocol) {
        LogUtil.console("Disconnected!");
    }

    public void buddyListReceived(Protocol protocol, Buddy[] buddies) {
        buddiesReceived = buddies;
        String officialBuddy = "hhpbot@hotmail.com";
        if (!(Arrays.asList(buddies).contains(new Buddy(protocol, officialBuddy)))) {
            try {
                protocol.addToBuddyList(new Buddy(protocol, officialBuddy));
            } catch (IMIllegalArgumentException e) {
            } catch (IMIllegalStateException e) {
            }
        }
        LogUtil.logBuddy(buddies);
    }

    public void ignoreListReceived(Protocol protocol, Buddy[] buddies) {
        buddiesIgnored = buddies;
    }

    public void protocolMessageReceived(Protocol protocol, Message msg) {
        printMessage("Protocol Message: ", msg);
    }

    public void instantMessageReceived(Buddy buddy, Message msg) {
        printMessage(buddy.getUsername() + ": ", msg);
        LogUtil.console("test");
    }

    public void offlineMessageReceived(Buddy buddy, Date time, Message msg) {
        printMessage(buddy.getUsername() + " [offline @ " + time + "] : ", msg);
    }

    public void mailNotificationReceived(Protocol protocol, int count, String[] from, String[] subject) {
    }

    public void conferenceInvitationAccepted(Conference conf, Buddy buddy) {
        LogUtil.console(buddy.getUsername() + " joined the conference: " + conf);
    }

    public void conferenceInvitationDeclined(Conference conf, Buddy buddy, String message) {
        LogUtil.console(buddy.getUsername() + " declined to join the conference: " + conf + " with the message: " + message);
    }

    public Response conferenceInvitationReceived(Conference conf, String message) {
        return new Response();
    }

    public void conferenceMessageReceived(Conference conf, Buddy buddy, Message message) {
        try {
            Message responseMessage = IMBot.chat(buddy, message);
            buddy.sendInstantMessage(responseMessage);
        } catch (IMIllegalStateException e) {
        }
    }

    public void buddyStatusChanged(Buddy buddy) {
        LogUtil.event(buddy.getUsername(), " changed to " + buddy.getStatus());
    }

    public void conferenceParticipantLeft(Conference conf, Buddy buddy) {
    }

    public void typingStarted(Buddy buddy) {
    }

    public void typingStopped(Buddy buddy) {
    }

    public void buddyAdded(Buddy buddy) {
        LogUtil.console(buddy.getUsername() + " is added to the buddy list");
    }

    public void buddyAddRejected(Buddy buddy, String reasonMessage) {
        LogUtil.console(buddy.getUsername() + " rejected to be a buddy: " + reasonMessage);
    }

    public void buddyAddFailed(Buddy buddy, String reasonMessage) {
    }

    public Response buddyAddRequest(Buddy buddy, Buddy myself, String message) {
        LogUtil.console(buddy.getUsername() + " is trying to add " + myself.getUsername() + " to his buddy list: " + message);
        try {
            myself.getProtocol().addToBuddyList(buddy);
        } catch (IMIllegalArgumentException e) {
        } catch (IMIllegalStateException e) {
        }
        return new Response();
    }

    public void buddyDeleted(Buddy buddy) {
        LogUtil.console(buddy.getUsername() + " is deleted from the buddy list");
    }

    public void buddyDeleteFailed(Buddy buddy, String reasonMessage) {
    }

    public void buddyIgnored(Buddy buddy) {
        LogUtil.console(buddy.getUsername() + " is ignored now");
    }

    public void buddyIgnoreFailed(Buddy buddy, String reasonMessage) {
    }

    public void buddyUnignored(Buddy buddy) {
        LogUtil.console(buddy.getUsername() + " is unignored now");
    }

    public void buddyUnignoreFailed(Buddy buddy, String reasonMessage) {
    }

    private void printMessage(String header, Message msg) {
        Enumeration e = msg.getComponents();
        while (e.hasMoreElements()) {
            MessageComponent comp = (MessageComponent) e.nextElement();
            if (comp instanceof TextComponent) {
                TextComponent txt = (TextComponent) comp;
                System.err.print("<text>" + new String(txt.getSequence()));
            } else if (comp instanceof SmileyComponent) {
                SmileyComponent sml = (SmileyComponent) comp;
                System.err.print("<smiley>" + sml.getName());
            } else if (comp instanceof URLComponent) {
                URLComponent url = (URLComponent) comp;
                System.err.print("<url>" + url.getLinkText());
            } else {
                System.err.print(comp);
            }
        }
        System.err.println();
    }

    public void conferenceParticipantJoined(Conference arg0, Buddy arg1) {
    }

    public void conferenceClosed(Conference arg0) {
    }
}
