package org.offseam.conversation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;

public class ConversationContext {

    private int id = 0;

    private final Object lock = new Object();

    private static final Logger logger = org.offseam.util.Util.getLogger("ConversationContext");

    private ConcurrentHashMap<String, Object> conversations = new ConcurrentHashMap<String, Object>();

    private ConversationIdCacheAdapter cachedIdAdapter = new ConversationIdCacheAdapter();

    /**
	 * clear conversation-scoped or injection-scoped beans matching given prefix 
	 * @param prefix : beans whose key starting with this prefix will be removed
	 */
    public void clearValuesMatchingPrefix(String prefix) {
        synchronized (conversations) {
            for (String key : conversations.keySet()) {
                if (key.indexOf(prefix) == 0) {
                    logger.info("Removing conversation bean! - " + key);
                    conversations.remove(key);
                }
            }
        }
    }

    public Map<String, Object> getConversationMap() {
        synchronized (conversations) {
            return conversations;
        }
    }

    public void put(String key, Object value) {
        synchronized (conversations) {
            conversations.put(key, value);
        }
    }

    public Object get(String key) {
        synchronized (conversations) {
            return conversations.get(key);
        }
    }

    public boolean remove(String key) {
        synchronized (conversations) {
            return conversations.remove(key) != null;
        }
    }

    public void destroy() {
        synchronized (conversations) {
            conversations.clear();
            lock.notifyAll();
        }
    }

    /**
	 * new conversation id
	 * @return
	 */
    public int generateConversationId() {
        return ++id;
    }

    public int getCurrentConversationId() {
        return id;
    }

    public ConversationIdCacheAdapter getCachedIdAdapter() {
        return cachedIdAdapter;
    }

    public void setCachedIdAdapter(ConversationIdCacheAdapter cachedIdAdapter) {
        this.cachedIdAdapter = cachedIdAdapter;
    }
}
