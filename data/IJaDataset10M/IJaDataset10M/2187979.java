package org.grlea.log.rollover;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * <p>An interface for objects that wish to be able to decide when log files should be rolled over.
 * </p>
 *
 * @author Graham Lea
 * @version $Revision: 1.2 $
 */
public interface RolloverStrategy {

    /**
    * Reads from the given map any properties that affect the behaviour of this strategy.
    *
    * @param properties an unmodifiable map of properties, i.e. String keys with String values
    *
    * @throws IOException if an error occurs while configuring the strategy. 
    */
    public void configure(Map properties) throws IOException;

    /**
    * Decides whether or not the log file should be rolled over immediately.
    *
    * @param fileCreated the date and time at which the current log file was created
    * @param fileLength the current length of the log file
    *
    * @return <code>true</code> if the log file should be rolled over now, <code>false</code> if it
    * should not.
    */
    public boolean rolloverNow(Date fileCreated, long fileLength);
}
