package de.jlab.communication;

public interface BoardReceiver {

    public void decodeLabReply(String channelName, String labReply);
}
