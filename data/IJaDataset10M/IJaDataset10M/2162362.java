package portal.presentation.cafe.wap;

import hambo.messaging.MessageSender;
import hambo.messaging.Messaging;
import hambo.messaging.SendFailedException;
import hambo.app.base.RedirectorBase;
import portal.presentation.cafe.web.cfStatic;
import hambo.app.util.Link;
import hambo.util.XMLUtil;
import hambo.community.FriendsManager;
import hambo.community.SMSSender;
import hambo.internationalization.TranslationServiceManager;

/**
 * Redirector used to send a Message to a Friend
 * WAP Version
 *
 */
public class cfwsendmsgredir extends RedirectorBase {

    /**
     * Default constructor. Does nothing except calling the constructor
     * of the superclass with the page id as an argument. The page id 
     * is set in the page_id variable that is inherited from PortalPage.
     */
    public void processPage() {
        String to = getParameter("to");
        String message = getParameter("message");
        String list = (String) getParameter("list");
        String idx = (String) getParameter("idx");
        FriendsManager fm = cfStatic.isUserRegistered(comms, user_id);
        String user_uid = getContext().getSessionAttributeAsString("user_uid");
        Link target = null;
        if (getParameter("fp") != null && getParameter("fp").equals("1")) target = new Link("cfwfriendpicker"); else if (idx != null) {
            target = new Link("cfwarchive");
            int msgno = 0;
            try {
                msgno = Integer.parseInt(idx);
                msgno = (int) (msgno / cfwarchive.NB_MAX_DISPLAYED) * cfwarchive.NB_MAX_DISPLAYED;
            } catch (NumberFormatException nfe) {
            }
            target.addParam("idxw", msgno);
        } else if (list != null) {
            target = new Link("cfwlistmsg");
        } else target = new Link("cfwfriends");
        if (message != null) {
            String startText = "";
            if (getContext().getSessionAttributeAsString("current_nick") != null) {
                startText = "CAFE33: " + TranslationServiceManager.translateTag("cfmessagefrom", language) + " " + getContext().getSessionAttributeAsString("current_nick") + ":";
            }
            if (startText != null) {
                startText = XMLUtil.decode(startText);
            }
            int so = fm.sendMsg(user_uid, to, message);
            if (so == 1 || so == 4 || so == 5 || so == 6) {
                if (so == 4 || so == 5 || so == 6) sendICQ(startText, message, to, user_uid, fm);
                if (so == 1 || so == 4) {
                    String friendMobileNumberWithPrefix = fm.getMobileNumber(to);
                    logDebug3("SEND SMS TO:" + friendMobileNumberWithPrefix);
                    if (friendMobileNumberWithPrefix == null || !friendMobileNumberWithPrefix.startsWith("+")) {
                        int i = fm.forceSendMsg(user_uid, to, message);
                        if (i == 1) {
                            fireUserEvent("cfSendMessage", new String[][] { { "Length", message.length() + "" }, { "SMS", "0" } });
                        }
                    } else {
                        try {
                            SMSSender server = new SMSSender("smscafe");
                            server.sendSimpleSMS(user_id, friendMobileNumberWithPrefix, startText + message);
                            fireUserEvent("cfSendMessage", new String[][] { { "Length", message.length() + "" }, { "SMS", "1" } });
                        } catch (Throwable e) {
                            int y = fm.forceSendMsg(user_uid, to, message);
                            if (y == 1) {
                                fireUserEvent("cfSendMessage", new String[][] { { "Length", message.length() + "" }, { "SMS", "0" } });
                            }
                        }
                    }
                }
            } else {
                fireUserEvent("cfSendMessage", new String[][] { { "Length", message.length() + "" }, { "SMS", "0" } });
            }
            target.addParam("err", "(@errcfokmsgsent@)");
        } else {
            target.addParam("err", "(@errcferrormsgnotsend@)");
        }
        throwRedirect(target);
    }

    private void sendICQ(String start, String theText, String fuid, String user_uid, FriendsManager fm) {
        String icqno = fm.getICQForward(fuid, user_uid);
        boolean sendViaIcq = false;
        if (icqno != null) {
            if (!icqno.equals("-1") && !icqno.equals("-2") && !icqno.equals("-4")) {
                if (!icqno.equals("-3")) {
                    MessageSender sender = Messaging.getDefaultSender();
                    String nick = getContext().getSessionAttributeAsString("current_nick");
                    try {
                        sender.sendICQ(icqno, "Hambo - forwarded message from " + nick, theText, nick, fm.getEmail(user_uid));
                        sendViaIcq = true;
                    } catch (SendFailedException sfe) {
                        logError("sendMsg", sfe);
                    } catch (Exception e) {
                        logError("sendMsg", e);
                    }
                } else {
                }
            }
        }
    }
}
