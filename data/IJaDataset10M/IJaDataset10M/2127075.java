package com.vzaar.transport.httpclient3;

import org.apache.commons.httpclient.HttpMethod;
import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.SignatureMethod;

/**
 * Support for OAuth and commons HTTP 3.1 client using the signpost 1.1
 * open source library.
 * 
 * @author Marc G. Smith
 */
@SuppressWarnings("serial")
class CommonsHttpOAuthConsumer extends AbstractOAuthConsumer {

    /**
     * Create the OAuth consumer.
     * 
     * @param consumerKey the OAuth consumer key
     * @param consumerSecret the OAuth consumer secret
     * @param signatureMethod the signature method
     */
    public CommonsHttpOAuthConsumer(String consumerKey, String consumerSecret, SignatureMethod signatureMethod) {
        super(consumerKey, consumerSecret, signatureMethod);
    }

    /**
	 * Wrap the request method object.
	 */
    @Override
    protected HttpRequest wrap(Object request) {
        if (!(request instanceof HttpMethod)) {
            throw new IllegalArgumentException("This consumer expects requests of type " + HttpMethod.class.getCanonicalName());
        }
        return new HttpRequestAdapter((HttpMethod) request);
    }
}
