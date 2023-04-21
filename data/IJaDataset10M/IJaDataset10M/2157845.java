package ircam.fts.client;

/**
 * The MessageHandler interface.
 */
public interface FtsMessageHandler {

    public void invoke(FtsObject obj, FtsArgs args);
}
