package at.langegger.xlwrap.common;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.helpers.CyclicBuffer;

/**
 * @author dorgon
 *
 * caches the last n lines and forwards messages to other buffers
 */
public class LogBufferDispatcher {

    private static final Logger log = LoggerFactory.getLogger(LogBufferDispatcher.class);

    public static final int DEFAULT_CACHED_EVENTS = 1000;

    protected final List<LogBuffer> registeredBuffers;

    private static LogBufferDispatcher instance;

    /** cache last lines */
    private final CyclicBufferAppender<LoggingEvent> cache;

    public static void init(int cachedLines) {
        if (instance != null) {
            log.warn("Re-initializing log buffer dispatcher with " + cachedLines + " cached log evenets. Active log buffers will be interrupted!");
            instance.stop();
        } else log.info("Initializing log buffer dispatcher with " + cachedLines + " cached log events.");
        instance = new LogBufferDispatcher(cachedLines);
    }

    /** init the dispatcher (will always cache last lines)
	 * 
	 * @param cachedLines
	 */
    private LogBufferDispatcher(int cachedLines) {
        if (cachedLines < 0) cachedLines = DEFAULT_CACHED_EVENTS;
        cache = new CyclicBufferAppender<LoggingEvent>();
        cache.setMaxSize(cachedLines);
        cache.start();
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger root = lc.getLogger(LoggerContext.ROOT_NAME);
        root.addAppender(this.cache);
        registeredBuffers = new ArrayList<LogBuffer>();
    }

    /** create a new LogBuffer (will register itself at the dispatcher in order
	 * to receive future events)
	 * 
	 * @return
	 */
    public static LogBuffer createLogBuffer() {
        LogBuffer buf = new LogBuffer(instance);
        for (int i = 0; i < instance.cache.getLength(); i++) buf.receiveEvent((LoggingEvent) instance.cache.get(i));
        return buf;
    }

    /**
	 * @param logBuffer
	 */
    protected void register(LogBuffer logBuffer) {
        registeredBuffers.add(logBuffer);
    }

    /**
	 * LogBuffers unregister themselves calling this method
	 * @param logBuffer
	 */
    protected void unregister(LogBuffer logBuffer) {
        registeredBuffers.remove(logBuffer);
    }

    protected void stop() {
        for (LogBuffer buf : registeredBuffers) buf.close();
        cache.stop();
    }

    /** modified version of ch.qos.logback.core.read.CyclicBufferAppender<E>
	 * 
	 * @author dorgon
	 *
	 * @param <E>
	 */
    public class CyclicBufferAppender<E> extends AppenderBase<E> {

        CyclicBuffer<E> cb;

        int maxSize = 512;

        public void start() {
            cb = new CyclicBuffer<E>(maxSize);
            super.start();
        }

        public void stop() {
            cb = null;
            super.stop();
        }

        @Override
        protected void append(E eventObject) {
            if (!isStarted()) return;
            cb.add(eventObject);
            for (LogBuffer buf : registeredBuffers) buf.receiveEvent((LoggingEvent) eventObject);
        }

        public int getLength() {
            if (isStarted()) {
                return cb.length();
            } else {
                return 0;
            }
        }

        public Object get(int i) {
            if (isStarted()) {
                return cb.get(i);
            } else {
                return null;
            }
        }

        /**
		 * Set the size of the cyclic buffer.
		 */
        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }
    }
}
