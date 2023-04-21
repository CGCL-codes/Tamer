package com.mattgarner.jaddas.node.net;

import java.io.UnsupportedEncodingException;
import com.mattgarner.jaddas.node.ErrorLogManager;
import com.mattgarner.jaddas.node.NodeConfigurationManager;

public class Message {

    private NodeConfigurationManager nodeConfig;

    private ErrorLogManager logManager;

    private byte msgFlag;

    private int msgID;

    private int msgLength;

    private byte[] msgBytes;

    public Message(byte messageFlag, int messageID, int messageLength, byte[] messageBytes) {
        msgFlag = messageFlag;
        msgID = messageID;
        msgLength = messageLength;
        msgBytes = messageBytes;
        nodeConfig = NodeConfigurationManager.getInstance();
        logManager = ErrorLogManager.getInstance();
    }

    public Message(byte messageFlag, byte[] messageBytes) {
        msgFlag = messageFlag;
        msgLength = messageBytes.length;
        msgBytes = messageBytes;
        nodeConfig = NodeConfigurationManager.getInstance();
        logManager = ErrorLogManager.getInstance();
    }

    public final void setMessageBytes(byte[] messageBytes) {
        msgBytes = messageBytes;
        msgLength = messageBytes.length;
    }

    public final void setMessageString(String messageString) {
        try {
            msgBytes = messageString.getBytes(nodeConfig.getStringEncoding());
        } catch (UnsupportedEncodingException e) {
            logManager.writeToLog(1, "NET", "Message Encoding Error: " + e.getMessage());
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
            return new String(msgBytes, nodeConfig.getStringEncoding());
        } catch (UnsupportedEncodingException e) {
            logManager.writeToLog(1, "NET", "Message Decoding Error: " + e.getMessage());
        }
        return null;
    }
}
