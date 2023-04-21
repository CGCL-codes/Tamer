package org.amityregion5.projectx.common.communication;

/**
 * An interface that can handle byte array messages.
 *
 * @author Joe Stein
 * @author Daniel Centore
 */
public interface RawListener {

    /**
     * Handles a String that we received from the server
     * @param prefix The prefix from {@link Constants}
     * @param str String to process
     */
    public void handle(char prefix, String str);
}
