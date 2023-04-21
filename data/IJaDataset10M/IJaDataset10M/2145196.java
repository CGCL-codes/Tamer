package org.jquantlib;

import org.slf4j.Logger;

/**
 * This class allows global definitions
 *
 * @author Richard Gomes
 */
public class JQuantLib {

    static Logger logger;

    /**
     * This method injects a org.slf4j.Logger into JQuantLib.
     * <p>
     * JQuantLib is dependent on SLF4J interface but not dependent on any logging implementation, which means that JQuantLib
     * delegates to the calling application code the responsability of choosing whatever implementation of SLF4J it is more
     * convenient, if any. If no Logger is injected, all messages are simply sent to System.err.
     *
     * @note that JQuantLib <i>never</i> synchronizes any access to the Logger.
     */
    public static final void setLogger(final Logger logger) {
        JQuantLib.logger = logger;
    }
}
