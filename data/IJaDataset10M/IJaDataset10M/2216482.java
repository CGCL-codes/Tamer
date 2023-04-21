package com.hifi.core.api.exceptions;

import java.io.IOException;

public class AudioPlayerException extends IOException {

    private static final long serialVersionUID = 1L;

    public AudioPlayerException(String msg) {
        super(msg);
    }

    public AudioPlayerException(String msg, Throwable t) {
        super(msg, t);
    }
}
