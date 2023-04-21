package net.grinder.engine.messages;

import net.grinder.communication.Message;

/**
 * Message that instructs the agent to clear its file cache.
 *
 * @author Philip Aston
 * @version $Revision: 2664 $
 */
public final class ClearCacheMessage implements Message {

    private static final long serialVersionUID = 6451850661282525463L;

    /**
   * Constructor.
   */
    public ClearCacheMessage() {
    }
}
