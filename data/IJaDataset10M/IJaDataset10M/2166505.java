package de.kapsi.net.daap.chunks.impl;

import de.kapsi.net.daap.chunks.UIntChunk;

/**
 * Used to tell the client its session-id
 *
 * @author  Roger Kapsi
 */
public class SessionId extends UIntChunk {

    public SessionId() {
        this(0);
    }

    public SessionId(long id) {
        super("mlid", "dmap.sessionid", id);
    }
}
