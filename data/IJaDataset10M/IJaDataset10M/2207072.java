package com.aionengine.chatserver.model.message;

import com.aionengine.chatserver.model.ChatClient;
import com.aionengine.chatserver.model.channel.Channel;

/**
 * @author ATracer
 */
public class Message {

    private Channel channel;

    private byte[] text;

    private ChatClient sender;

    /**
	 * 
	 * @param channel
	 * @param text
	 */
    public Message(Channel channel, byte[] text, ChatClient sender) {
        this.channel = channel;
        this.text = text;
        this.sender = sender;
    }

    /**
	 * @return the channel
	 */
    public Channel getChannel() {
        return channel;
    }

    /**
	 * @return the text
	 */
    public byte[] getText() {
        return text;
    }

    public int size() {
        return text.length;
    }

    /**
	 * @return the sender
	 */
    public ChatClient getSender() {
        return sender;
    }
}
