package com.hvilela.common.messages;

import java.nio.ByteBuffer;
import java.util.UUID;
import com.hvilela.common.Message;

public class ChatMessage extends Message {

    private String message;

    public ChatMessage() {
        this(null, null);
    }

    public ChatMessage(UUID sender, String message) {
        super(Integer.SIZE + message.length(), Message.Type.CHAT, sender);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public ByteBuffer toBytes() {
        ByteBuffer byteBuffer = super.toBytes();
        return byteBuffer;
    }

    public ChatMessage fromBytes(ByteBuffer byteBuffer) {
        ChatMessage message = new ChatMessage();
        read(byteBuffer);
        return message;
    }

    public void read(ByteBuffer byteBuffer) {
        super.read(byteBuffer);
    }

    @Override
    public String toString() {
        return super.toString() + " " + message;
    }
}
