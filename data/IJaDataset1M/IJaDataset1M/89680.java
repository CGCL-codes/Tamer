package net.sourceforge.domian.util.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A <a href="http://www.slf4j.org">SLF4J</a> logger instance is provided in this class.
 * It is lazily obtained through the {@code getLogger()} method.
 *
 * @author Eirik Torske
 * @see <a href="http://www.slf4j.org" target="_new">SLF4J (Simple Logging Facade for Java)</a>
 * @since 0.4.1
 */
public abstract class AbstractLoggingObject {

    private transient volatile Logger logger;

    private synchronized Logger createAndCacheLogger() {
        if (this.logger == null) {
            this.logger = LoggerFactory.getLogger(this.getClass());
            return this.logger;
        }
        throw new IllegalStateException("This method should not be invoked unless logger member is null");
    }

    /**
     * Lazy retrieval of the {@link org.slf4j.Logger} member.
     *
     * @return the logger member for this entity
     */
    protected Logger getLogger() {
        return this.logger != null ? this.logger : createAndCacheLogger();
    }
}
