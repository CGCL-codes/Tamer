package com.quikj.server.framework;

public interface AceInputFilterInterface {

    public static final int CONTINUE_RECEIVING = 1;

    public static final int SEND_MESSAGE = 2;

    public static final int RESET_BUFFER = 3;

    public abstract int numberOfBytesToRead();

    public abstract int processMessage(byte[] buffer, int offset, int length);
}
