package com.mattgarner.jaddas.cli.net;

import java.io.UnsupportedEncodingException;
import com.mattgarner.jaddas.cli.ClientConfigurationManager;

public class Message {

    private ClientConfigurationManager clientConfig;

    private byte msgFlag;

    private int msgID;

    private int msgLength;

    private byte[] msgBytes;

    public Message(byte messageFlag, int messageID, int messageLength, byte[] messageBytes) {
        msgFlag = messageFlag;
        msgID = messageID;
        msgLength = messageLength;
        msgBytes = messageBytes;
        clientConfig = ClientConfigurationManager.getInstance();
    }

    public Message(byte messageFlag, byte[] messageBytes) {
        msgFlag = messageFlag;
        msgLength = messageBytes.length;
        msgBytes = messageBytes;
        clientConfig = ClientConfigurationManager.getInstance();
    }

    public Message(byte messageFlag, String messageString) {
        clientConfig = ClientConfigurationManager.getInstance();
        msgFlag = messageFlag;
        try {
            msgBytes = messageString.getBytes(clientConfig.getStringEncoding());
            msgLength = msgBytes.length;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Message Decoding Error: " + e.getMessage());
        }
    }

    public final void setMessageBytes(byte[] messageBytes) {
        msgBytes = messageBytes;
        msgLength = messageBytes.length;
    }

    public final void setMessageString(String messageString) {
        try {
            msgBytes = messageString.getBytes(clientConfig.getStringEncoding());
        } catch (UnsupportedEncodingException e) {
            System.out.println("Message Encoding Error: " + e.getMessage());
        }
        msgLength = msgBytes.length;
    }

    public final byte getMessageFlag() {
        return msgFlag;
    }

    public final int getMessageID() {
        return msgID;
    }

    public final int getMessageLength() {
        return msgLength;
    }

    public final byte[] getMessageBytes() {
        return msgBytes;
    }

    public final String getMessageString() {
        try {
            return new String(msgBytes, clientConfig.getStringEncoding());
        } catch (UnsupportedEncodingException e) {
            System.out.println("Message Decoding Error: " + e.getMessage());
        }
        return null;
    }
}
