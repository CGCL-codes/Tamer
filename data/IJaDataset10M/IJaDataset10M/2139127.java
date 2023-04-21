package org.mobicents.slee.resource.xcapclient;

import java.net.URI;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.header.Header;

/**
 * Activity object for doing async requests on the XCAP Client RA.
 * 
 * @author emmartins
 * 
 */
public interface AsyncActivity {

    /**
	 * Retrieves the XML resource from the XCAP server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void get(URI uri, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Puts the specified content in the XCAP server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void put(URI uri, String mimetype, String content, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Puts the specified content in the XCAP server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void put(URI uri, String mimetype, byte[] content, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Puts the specified content in the XCAP Server, if the ETag matches with
	 * the one in the server
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void putIfMatch(URI uri, String eTag, String mimetype, String content, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Puts the specified content in the XCAP Server, if the ETag matches with
	 * the one in the server
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void putIfMatch(URI uri, String eTag, String mimetype, byte[] content, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Puts the specified content in the XCAP Server, if the ETag doesn't match
	 * the one in the server
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void putIfNoneMatch(URI uri, String eTag, String mimetype, String content, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Puts the specified content in the XCAP Server, if the ETag doesn't match
	 * the one in the server
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void putIfNoneMatch(URI uri, String eTag, String mimetype, byte[] content, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Deletes the resource in the XCAP Server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void delete(URI uri, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Deletes the resource in the XCAP Server, if the ETag matches the one in
	 * the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void deleteIfMatch(URI uri, String eTag, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Deletes the resource in the XCAP Server, if the ETag doesn't match the
	 * one in the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 */
    public void deleteIfNoneMatch(URI uri, String eTag, Header[] additionalRequestHeaders, Credentials credentials);

    /**
	 * Ends this activity;
	 */
    public void endActivity();
}
