package com.thesett.aima.logic.fol;

import com.thesett.common.parsing.SourceCodeException;

/**
 * LinkageException is an error condition that typically relates to a location with a source text file, and represents a
 * failure to link a compiled form of the source code into a machine that processes it, usually due to unresolved
 * symbols.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Represent failure to link, due to unresolved symbols.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class LinkageException extends SourceCodeException {

    /**
     * Builds an exception with a message and a user message and a message key. To create an exception with no key and
     * just a user message or with no user message at all pass in null arguments for the key or user message.
     *
     * @param message     The exception message.
     * @param cause       The wrapped exception underlying this one.
     * @param key         A key to look up user readable messages with.
     * @param userMessage The user readable message or data string.
     */
    public LinkageException(String message, Throwable cause, String key, String userMessage) {
        super(message, cause, key, userMessage, null);
    }
}
