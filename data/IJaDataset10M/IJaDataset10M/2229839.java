package br.biofoco.p2p.rpc.messaging;

import java.io.IOException;

public class MessageException extends IOException {

    private static final long serialVersionUID = 8872848860937575071L;

    public MessageException(String message, Throwable t) {
        super(message, t);
    }
}
