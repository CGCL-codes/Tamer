package com.handcoded.fpml;

import com.handcoded.meta.Release;

public final class MessageType {

    public MessageType(final Release release, final String message) {
        this.release = release;
        this.message = message;
    }

    public Release getRelease() {
        return (release);
    }

    public String getMessage() {
        return (message);
    }

    public int hashCode() {
        return (release.hashCode() ^ message.hashCode());
    }

    public boolean equals(Object other) {
        return ((other instanceof MessageType) && equals((MessageType) other));
    }

    public boolean equals(MessageType other) {
        return (release.equals(other.release) && message.equals(other.message));
    }

    public String toString() {
        return (release.getVersion() + " : " + message);
    }

    private final Release release;

    private final String message;
}
