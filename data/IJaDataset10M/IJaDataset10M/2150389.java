package com.rlsoftwares.virtualdeck.network.messages;

import com.rlsoftwares.network.Message;
import org.w3c.dom.Text;

/**
 *
 * @author Rodrigo
 */
public class MessageChatText extends Message {

    private String nick;

    private String text;

    public MessageChatText() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
