package xmpp.listeners;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import xmpp.utils.presence.PresenceCache;
import xmpp.utils.presence.PresenceProcessor;

/**
 * Base class for all chat listeners
 * 
 * @author tillias
 * 
 */
public abstract class AbstractChatListener implements PacketListener {

    /**
     * Sole constructor for invocation by subclasses
     * 
     * @param cache
     *            Presence cache which will be used by this listener
     * @param chat
     *            Multi-user chat room which will be used by this listener
     * @throws NullPointerException
     *             Thrown if any argument passed to constructor is null
     */
    public AbstractChatListener(PresenceCache cache, MultiUserChat chat) throws NullPointerException {
        if (cache == null) throw new NullPointerException("Presence cache can't be null");
        if (chat == null) throw new NullPointerException("Chat can't be null");
        this.cache = cache;
        this.chat = chat;
        this.presenceProcessor = new PresenceProcessor();
    }

    /**
     * Gets {@link PresenceCache} of this listener
     * 
     * @return Presence cache. Can't be null
     */
    protected PresenceCache getCache() {
        return cache;
    }

    /**
     * Gets {@link MultiUserChat} of this listener
     * 
     * @return Multi-user chat. Can't be null
     */
    protected MultiUserChat getChat() {
        return chat;
    }

    PresenceCache cache;

    PresenceProcessor presenceProcessor;

    MultiUserChat chat;
}
