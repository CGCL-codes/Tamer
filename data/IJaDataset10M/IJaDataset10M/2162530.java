package org.openymsg.contact;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openymsg.YahooContact;
import org.openymsg.YahooContactGroup;
import org.openymsg.YahooProtocol;
import org.openymsg.contact.group.ContactGroupImpl;
import org.openymsg.contact.group.SessionGroupImpl;
import org.openymsg.contact.roster.SessionRosterImpl;
import org.openymsg.contact.status.SessionStatusImpl;
import org.openymsg.execute.read.MultiplePacketResponse;
import org.openymsg.network.YMSG9Packet;

public class ListOfContactsResponse implements MultiplePacketResponse {

    private static final Log log = LogFactory.getLog(ListOfContactsResponse.class);

    private SessionRosterImpl sessionContact;

    private SessionGroupImpl sessionGroup;

    private SessionStatusImpl sessionStatus;

    public ListOfContactsResponse(SessionRosterImpl sessionContact, SessionGroupImpl sessionGroup, SessionStatusImpl sessionStatus) {
        this.sessionContact = sessionContact;
        this.sessionGroup = sessionGroup;
        this.sessionStatus = sessionStatus;
    }

    @Override
    public void execute(List<YMSG9Packet> packets) {
        String username = null;
        YahooProtocol protocol = YahooProtocol.YAHOO;
        ContactGroupImpl currentListGroup = null;
        Map<String, ContactGroupImpl> receivedGroups = new HashMap<String, ContactGroupImpl>();
        Set<YahooContact> usersOnFriendsList = new HashSet<YahooContact>();
        Set<YahooContact> usersOnIgnoreList = new HashSet<YahooContact>();
        Set<YahooContact> usersOnPendingList = new HashSet<YahooContact>();
        boolean isPending = false;
        for (YMSG9Packet qPkt : packets) {
            Iterator<String[]> iter = qPkt.entries().iterator();
            while (iter.hasNext()) {
                String[] s = iter.next();
                int key = Integer.valueOf(s[0]);
                String value = s[1];
                switch(key) {
                    case 302:
                        if (value != null && value.equals("320")) {
                            currentListGroup = null;
                        }
                        break;
                    case 301:
                        if (username != null) {
                            YahooContact yu = null;
                            if (currentListGroup != null) {
                                for (YahooContact friend : usersOnFriendsList) {
                                    if (friend.getName().equals(username)) {
                                        yu = friend;
                                        currentListGroup.add(yu);
                                        if (!yu.getProtocol().equals(protocol) && yu.getProtocol().equals(YahooProtocol.YAHOO)) {
                                            log.error("Switching protocols because user is in list more that once: " + yu.getName() + " from: " + yu.getProtocol() + " to: " + protocol);
                                        }
                                    }
                                }
                                if (yu == null) {
                                    yu = new YahooContact(username, protocol);
                                    currentListGroup.add(yu);
                                    usersOnFriendsList.add(yu);
                                }
                            } else {
                                yu = new YahooContact(username, protocol);
                                usersOnIgnoreList.add(yu);
                            }
                            if (isPending) {
                                usersOnPendingList.add(yu);
                            }
                            username = null;
                            isPending = false;
                            protocol = YahooProtocol.YAHOO;
                        }
                        break;
                    case 223:
                        isPending = true;
                        break;
                    case 300:
                        break;
                    case 65:
                        currentListGroup = receivedGroups.get(value);
                        if (currentListGroup == null) {
                            currentListGroup = new ContactGroupImpl(value);
                            receivedGroups.put(value, currentListGroup);
                        }
                        break;
                    case 7:
                        username = value;
                        break;
                    case 241:
                        protocol = YahooProtocol.getProtocolOrDefault(value, username);
                        break;
                    case 59:
                        break;
                    case 317:
                        break;
                }
            }
            if (username != null) {
                YahooContact yu = null;
                if (currentListGroup != null) {
                    for (YahooContact friend : usersOnFriendsList) {
                        if (friend.getName().equals(username)) {
                            yu = friend;
                            currentListGroup.add(yu);
                        }
                    }
                    if (yu == null) {
                        yu = new YahooContact(username, protocol);
                        currentListGroup.add(yu);
                        usersOnFriendsList.add(yu);
                    }
                } else {
                    yu = new YahooContact(username, protocol);
                    usersOnIgnoreList.add(yu);
                }
                if (isPending) {
                    usersOnPendingList.add(yu);
                }
                username = null;
                isPending = false;
                protocol = YahooProtocol.YAHOO;
            }
        }
        for (YahooContact contact : usersOnFriendsList) {
            sessionContact.loadedContact(contact);
        }
        if (!usersOnIgnoreList.isEmpty()) {
            sessionStatus.addedIgnored(usersOnIgnoreList);
        }
        if (!usersOnPendingList.isEmpty()) {
            sessionStatus.addedPending(usersOnPendingList);
        }
        if (!receivedGroups.values().isEmpty()) {
            sessionGroup.addedGroups(new HashSet<YahooContactGroup>(receivedGroups.values()));
        }
        sessionContact.rosterLoaded();
    }

    @Override
    public int getProceedStatus() {
        return 0;
    }
}
