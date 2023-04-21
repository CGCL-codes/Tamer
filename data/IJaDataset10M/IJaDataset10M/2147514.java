package com.jabberwookie;

import com.jabberwookie.ns.jabber.Chunk;

/**
 * This class provides the interface required by the {@link Stream}
 * class for anyone that wants to receive packets that aren't
 * of &lt;iq&gt;, &lt;presence&gt; or &lt;message&gt; types.
 * @author  smeiners
 */
public interface UnrecognizedChunkListener {

    void incomingChunk(Chunk chunk);
}
