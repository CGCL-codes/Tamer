package com.volantis.mcs.wbdom;

import com.volantis.mcs.dom.output.SerialisationURLListener;
import org.apache.log4j.Category;

/**
 * A dubious test implementation of {@link com.volantis.mcs.dom.output.SerialisationURLListener}.
 * <p>
 * All it does currently is nicely violate the first law of testcases, (that
 * tests must not require an "expert" user to observe their results), by
 * logging URL attribute events. Yuck!
 * <p>
 * We will need to extend this to do something automated as well in order for
 * it to be generally useful.
 * 
 * @todo make detecting failures assert based rather than relying on logging! 
 */
public class TestURLListener implements SerialisationURLListener {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The log4j object to log to.
     */
    private static Category logger = Category.getInstance("com.volantis.mcs.wbdom.TestURLAttributeListener");

    public void foundURL(String url) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received URL Attribute event for '" + url + "'");
        }
    }
}
