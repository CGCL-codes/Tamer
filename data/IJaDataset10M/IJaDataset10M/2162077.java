package org.tockit.plugin;

public class PluginLoadFailedException extends Exception {

    public PluginLoadFailedException(String message) {
        super(message);
    }

    public PluginLoadFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
