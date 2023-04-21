package com.volantis.mcs.integration;

/**
 * Encapsulates details about the URL that is about to be rewritten.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @see PageURLRewriter
 *
 * @mock.generate
 */
public interface PageURLDetails {

    /**
     * The type of URL.
     * 
     * @return An instance of {@link PageURLType}, may not be null.
     */
    public PageURLType getPageURLType();
}
