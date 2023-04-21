package org.bitdrive.network.failure.api;

public class FailureMessage {

    public static final short[] TYPE_ID = new short[] { 0, 5 };

    public short[] sourceMessageType;

    public int sourceMessageID;

    public short errorCode;

    public String message;
}
