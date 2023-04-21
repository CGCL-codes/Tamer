package model.retrieval;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;

;

/**
 * An interface for handling proxy plugins for various organisations.
 *
 * @author jbl
 */
public interface ProxyPluginInterface extends ProxyInterface {

    /**
	 * Method to pass a URL and have it formatted to satisfy
	 * the proxy requirements
	 * 
	 * @param url URL of the page
	 * @param client HttpClient used to retrieve the page
	 * @return String of the re-written URL
	 */
    public String retrieveURL(String url, HttpClient client) throws SourceConnectionException, HttpStatusException;

    /**
	 * Method to pass a URL and have it formatted to satisfy
	 * the proxy requirements
	 * 
	 * @param url URL of the page
	 * @param client HttpClient used to retrieve the page
	 * @param rawURL whether we want the proxy to rewrite the URL or leave as-is
	 * @return String of the re-written URL
	 */
    public String retrieveURL(String url, HttpClient client, boolean rawURL) throws SourceConnectionException, HttpStatusException;

    /**
	 * Method to pass a URL and have it formatted to satisfy
	 * the proxy requirements
	 * 
	 * @param url URL of the page
	 * @param client HttpClient used to retrieve the page
	 * @param submitMethod URLGrabber.Method.GET/POST form submission type
	 * @param postVars NameValuePair[] of post variables
	 * @return String of the re-written URL
	 */
    public String retrieveURL(String url, HttpClient client, boolean rawURL, URLGrabber.Method submitMethod, NameValuePair[] postVars) throws SourceConnectionException, HttpStatusException;
}
