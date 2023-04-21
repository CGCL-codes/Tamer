package com.volantis.mcs.runtime;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Class to manage URLRewriter interfaces
 *
 */
public class URLRewriterManager implements URLRewriter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(URLRewriterManager.class);

    private URLRewriter urlRewriter;

    /**
   * Creates a new URLRewriterManager instance. Uses the
   * rewriterClass string to detirmine the class of the 
   * URLWriter that is to be loaded. If this string is null
   * then a default implementation of URLWriter is used.
   * This default implementation performs no rewriting.
   *
   * @param rewriterClass the URLRewriter interface to use
   * @exception ClassNotFoundException if an error occurs
   * @exception InstantiationException if an error occurs
   * @exception IllegalAccessException if an error occurs
   */
    public URLRewriterManager(String rewriterClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (null != rewriterClass) {
            urlRewriter = (URLRewriter) Class.forName(rewriterClass).newInstance();
        } else {
            urlRewriter = new DefaultURLRewriter();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Using the URLRewriter " + urlRewriter);
        }
    }

    /**
   * Map a MarinerURL object using some external mapping to another
   * MarinerURL. Delegate this operation to the URLRewriter that was 
   * loaded at initialisation.
   * @param context a MarinerRequestContext object
   * @param url the MarinerURL object to map from
   * @return a MarinerURL object
   */
    public MarinerURL mapToExternalURL(MarinerRequestContext context, MarinerURL url) {
        return urlRewriter.mapToExternalURL(context, url);
    }

    /**
   * Map a MarinerURL object from some external mapping to a mariner
   * MarinerURL. Delegate this operation to the URLRewriter that was 
   * loaded at initilialisation.
   * @param context a MarinerRequestContext object
   * @param url the MarinerURL object to map from
   * @return a MarinerURL object
   */
    public MarinerURL mapToMarinerURL(MarinerRequestContext context, MarinerURL url) {
        return urlRewriter.mapToMarinerURL(context, url);
    }

    /**
   * Default implementation of URLRewriter interface.
   * Does nothing
   *
   */
    public static class DefaultURLRewriter implements URLRewriter {

        public MarinerURL mapToExternalURL(MarinerRequestContext context, MarinerURL url) {
            return url;
        }

        public MarinerURL mapToMarinerURL(MarinerRequestContext context, MarinerURL url) {
            return url;
        }
    }
}
