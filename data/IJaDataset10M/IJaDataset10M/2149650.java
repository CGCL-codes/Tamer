package edu.tufts.vue.collab.im;

import net.kano.joscar.snaccmd.FullUserInfo;
import net.kano.joscar.snaccmd.chat.ChatMsg;

public interface ChatConnListener {

    void connFailed(ChatConn conn, Object reason);

    void connected(ChatConn conn);

    void joined(ChatConn conn, FullUserInfo[] members);

    void left(ChatConn conn, Object reason);

    void usersJoined(ChatConn conn, FullUserInfo[] members);

    void usersLeft(ChatConn conn, FullUserInfo[] members);

    void gotMsg(ChatConn conn, FullUserInfo sender, ChatMsg msg);
}
