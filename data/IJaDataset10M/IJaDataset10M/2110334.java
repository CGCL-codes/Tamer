package com.radroid.mediaplayer.exceptions;

public abstract class AudioPlayerException extends RuntimeException {

    public AudioPlayerException() {
        super();
    }

    public AudioPlayerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AudioPlayerException(String detailMessage) {
        super(detailMessage);
    }

    public AudioPlayerException(Throwable throwable) {
        super(throwable);
    }
}
