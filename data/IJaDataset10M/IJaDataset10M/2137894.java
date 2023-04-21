package org.archive.io;

import java.util.logging.LogRecord;

/**
 * Interface indicating a logging Formatter can preformat a record (outside
 * the standard-implementation synchronized block) and cache it, returning it
 * for the next request for formatting from the same thread. 
 * @contributor gojomo
 */
public interface Preformatter {

    public void preformat(LogRecord record);

    public void clear();
}
