package com.sesca.audio;

public interface AudioReceiverListener {

    public void onIncomingReceivedFrame(byte[] frame, int payloadType);
}
