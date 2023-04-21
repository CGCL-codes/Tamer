package com.manning.junitbook.ch12.htmlunit;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

/**
 * This error handler fails on any error, fatal error, or warning.
 * 
 * @author <a href="mailto:ggregory@apache.org">Gary Gregory</a>
 * @version $Id: AlwaysFailErrorHandler.java 390 2009-04-28 23:34:07Z garydgregory $
 */
public class AlwaysFailErrorHandler implements ErrorHandler {

    /**
     * Throws the given {@link CSSParseException}.
     */
    public void error(CSSParseException e) throws CSSException {
        throw e;
    }

    /**
     * Throws the given {@link CSSParseException}.
     */
    public void fatalError(CSSParseException e) throws CSSException {
        throw e;
    }

    /**
     * Throws the given {@link CSSParseException}.
     */
    public void warning(CSSParseException e) throws CSSException {
        throw e;
    }
}
