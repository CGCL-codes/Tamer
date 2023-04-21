package org.mov.quote;

import org.mov.parser.EvaluationException;

/**
 * This interface describes the quote source for the quote functions
 * in {@link QuoteFunctions}. That class is used by both Gondola language 
 * expressions and charting functions, so it needs to accept quotes in multiple 
 * forms. This interface enables the quote function class to be unaware of 
 * this difference.
 *
 * @author Andrew Leppard
 * @see QuoteFunctions
 */
public interface QuoteFunctionSource {

    /**
     * Return the quote value on the given date. The quote function source
     * contains a set of quotes which can be accessed from offset 0, which
     * is the earliest date, to the number of quotes in the source minus one.
     * Typically a quote source is set up to contain a fixed number of quotes,
     * each of which is used by a quote function.
     *
     * @param  offset the offset of the date in the quote source.
     * @return the quote value or <code>NaN</code> if the quote is missing / not available
     * @exception    EvaluationException if {@link QuoteBundleFunctionSource} is not
     *               allowed access to a quote. See {@link org.mov.analyser.gp.GPQuoteBundle}.
     */
    public double getValue(int offset) throws EvaluationException;
}
