package org.opentradingsolutions.log4fix.core;

import org.opentradingsolutions.log4fix.datadictionary.DataDictionaryLoader;
import quickfix.Log;
import quickfix.SessionID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * QuickFIX/J {@link Log} implementation that maintains all QuickFIX messages
 * in memory.
 *
 * @author Brian M. Coyner
 */
public abstract class AbstractMemoryLog implements Log {

    private DataDictionaryLoader dictionaryLoader;

    private AtomicInteger index;

    public AbstractMemoryLog(DataDictionaryLoader dictionaryLoader) {
        this.dictionaryLoader = dictionaryLoader;
        index = new AtomicInteger();
    }

    public void clear() {
        getMemoryLogModel().clear();
    }

    public void onIncoming(String message) {
        log(message, true);
    }

    public void onOutgoing(String message) {
        log(message, false);
    }

    public void onEvent(String text) {
        getMemoryLogModel().addLogEvent(new LogEvent(text));
    }

    protected abstract SessionID getSessionId();

    protected abstract MemoryLogModel getMemoryLogModel();

    private void log(final String rawMessage, final boolean incoming) {
        SessionID sessionId = getSessionId();
        int messageIndex = index.getAndIncrement();
        getMemoryLogModel().addLogMessage(new LogMessage(messageIndex, incoming, sessionId, rawMessage, dictionaryLoader.loadDictionary(sessionId)));
    }
}
