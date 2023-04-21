package com.phamola.examples.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.phamola.examples.client.InvitationChatService;

/**
 * Service that manages the invitations.
 */
public class InvitationChatServiceImpl extends RemoteServiceServlet implements InvitationChatService {

    public void addAFriend(String friendEmail) {
        System.out.println("Server sends invitation to: " + friendEmail);
        try {
            MailUtil.sendFriendAddEmail(friendEmail);
        } catch (GPhoneRuntimeException e) {
            e.printStackTrace();
        }
    }

    public void inviteAFriend(String friendEmail) {
    }
}
