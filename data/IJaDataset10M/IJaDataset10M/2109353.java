package edu.udo.cs.wvtool.generic.wordfilter;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * This interface represents a mechanism by which tokens (words) are filtered from a stream of tokens. This step usually implements the stop word list.
 * 
 * @author Michael Wurst
 * @version $Id: WVTWordFilter.java,v 1.3 2007/05/20 18:06:03 mjwurst Exp $
 * 
 */
public interface WVTWordFilter {

    /**
     * Filter tokens from a token stream.
     * 
     * @param source the token stream to filter
     * @param d the <code>WVTDocumentInfo</code> value about the document processed
     * @return the filtered stream
     * @exception Exception if an error occurs
     */
    public TokenEnumeration filter(TokenEnumeration source, WVTDocumentInfo d) throws WVToolException;
}
