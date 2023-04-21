package org.fbmc.server;

public final class EmbeddedServerException extends Exception {

    public EmbeddedServerException(String serverName, String explication) {
        super("Impossible to start this server : " + serverName + ". " + explication);
    }
}
