package org.jcvi.log;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * The <code>StandardLoggingLayout</code> is a simple Log4J {@link PatternLayout} which 
 * prints log messages which contain:
 * <ul>
 *   <li>The time the message was reported.</li>
 *   <li>The name of the {@link Logger} reporting the message.</li>
 *   <li>The logging {@link Level} of the message.</lie>
 *   <li>The actual message.</li>
 * </ul>
 * An example of this is seen below, where <code>main</code> was the name of the {@link Logger}
 * and the <code>Level</code> was {@link Level#INFO}.
 * <pre>
 * 2009-09-24 11:23:16,769 [main] :: INFO - Beginning run of Vapor 1.1.2b1
 * </pre>
 *
 * @author jsitz@jcvi.org
 */
public class StandardLoggingLayout extends PatternLayout {

    private static Layout global;

    public static Layout global() {
        if (StandardLoggingLayout.global == null) {
            StandardLoggingLayout.global = new StandardLoggingLayout();
        }
        return StandardLoggingLayout.global;
    }

    /**
     * Creates a new <code>StandardLoggingLayout</code>.
     */
    public StandardLoggingLayout() {
        super("%d [%c] :: %p - %m%n");
    }
}
