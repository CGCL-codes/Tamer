package com.skype.connector.test;

public final class PlayerMessage {

    public enum Type {

        RECEIVED, SENT
    }

    private final Type type;

    private final long time;

    private final String message;

    public PlayerMessage(final Type type, final long time, final String message) {
        this.type = type;
        this.time = time;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }
}
