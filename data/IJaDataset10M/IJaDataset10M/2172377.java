package games.strategy.engine.chat;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple flood control, only allow so many events per window of time
 */
public class ChatFloodControl {

    private static final int ONE_MINUTE = 60 * 1000;

    static final int EVENTS_PER_WINDOW = 20;

    static final int WINDOW = ONE_MINUTE;

    private final Object lock = new Object();

    private long clearTime = System.currentTimeMillis();

    private final Map<String, Integer> messageCount = new HashMap<String, Integer>();

    public boolean allow(final String from, final long now) {
        synchronized (lock) {
            if (now > clearTime) {
                messageCount.clear();
                clearTime = now + WINDOW;
            }
            if (!messageCount.containsKey(from)) {
                messageCount.put(from, 0);
            }
            messageCount.put(from, messageCount.get(from) + 1);
            return messageCount.get(from) <= EVENTS_PER_WINDOW;
        }
    }
}
