package org.moyoman.client.reference.util;

import org.moyoman.comm.client.CommandExecutor;
import org.moyoman.comm.client.DirectCommandExecutor;
import org.moyoman.util.InternalErrorException;

/**
 * Provides support for obtaining internationalized messages. This class is a
 * wrapper around <code>org.moyoman.comm.client.CommandExecutor</code>.
 *
 * @author Jeffrey M. Thompson
 * @version v0.01
 *
 * @since v0.01
 */
public class MessageSupport implements IGUIConstants {

    /** Command executor. */
    private static CommandExecutor _commandExecutor;

    static {
        try {
            _commandExecutor = DirectCommandExecutor.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Private constructor to prevent instance creation.
     *
     * @since v0.01
     */
    private MessageSupport() {
    }

    /**
     * Return the string for the given key.
     *
     * @param key Key to the message.
     *
     * @since v0.01
     */
    public static String getMessage(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        String answer = key;
        try {
            answer = _commandExecutor.getMessage(key);
        } catch (InternalErrorException e) {
            if (DEBUG) {
                System.err.println("missing string resource " + key);
            }
            e.printStackTrace();
        }
        return answer;
    }
}
