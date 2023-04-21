package org.musicbrainz.webservice;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.musicbrainz.wsxml.MbXMLException;
import org.musicbrainz.wsxml.MbXmlParser;
import org.musicbrainz.wsxml.element.Metadata;
import org.musicbrainz.wsxml.impl.JDOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A default abstract web service implementation that provides common
 * properties of a web service client that can be extended.
 * 
 * @author Patrick Ruhkopf
 */
public abstract class DefaultWebService implements WebService {

    private Logger log = LoggerFactory.getLogger(DefaultWebService.class);

    /**
	 * Encoding used to encode url parameters
	 */
    public static final String URL_ENCODING = "UTF-8";

    /**
	 * A string containing the web service version to use
	 */
    private String version = "1";

    /**
	 * A string that is used in the web service url 
	 * @see DefaultWebService#makeUrl(String, String, Map, Map, String, String)
	 */
    private String pathPrefix = "/ws";

    /**
	 * The host, defaults to musicbrainz.org
	 */
    private String host = "musicbrainz.org";

    /**
	 * The protocol, defaults to http
	 */
    private String protocol = "http";

    /**
	 * The port, defaults to null
	 */
    private Integer port = null;

    /**
	 * The web service type, defaults to xml
	 */
    private String type = "xml";

    /**
	 * XML parser used to consume the response stream
	 */
    private MbXmlParser parser;

    /**
	 * Default Constructor that uses {@link JDOMParser}
	 */
    public DefaultWebService() {
        this.parser = new JDOMParser();
    }

    /**
	 * Constructor to inject a custom parser
	 * @param parser XML parser used to consume the response stream
	 */
    public DefaultWebService(final MbXmlParser parser) {
        this.parser = parser;
    }

    /**
	 * Sends a GET request to the specified url
	 * 
	 * @param url The URL
	 * @return A populated {@link Metadata} object
	 * @throws WebServiceException
	 */
    protected abstract Metadata doGet(String url) throws WebServiceException, MbXMLException;

    /**
	 * Sends a POST request to the specified url.
	 * 
	 * @param url
	 * @param data Input stream of the data to post
	 * @throws WebServiceException
	 */
    protected abstract void doPost(String url, InputStream data) throws WebServiceException;

    public Metadata get(final String entity, final String id, final List<String> includeParams, final Map<String, String> filterParams) throws WebServiceException, MbXMLException {
        String url = this.makeURL(entity, id, includeParams, filterParams);
        this.log.debug("GET " + url);
        return this.doGet(url);
    }

    public void post(final String entity, final String id, final InputStream data) throws WebServiceException {
        String url = this.makeURL(entity, id, null, null);
        this.log.debug("POST " + url);
        this.doPost(url, data);
    }

    /**
	 * Constructs a URL that can be used to query the web service. The url is made
	 * up of the protocol, host, port, version, type, path and parameters.
	 * 
	 * @param entity The entity (i.e. type, e.g. 'artist') the request is targeting
	 * @param id The id of the entity 
	 * @param includeParams A list containing values for the 'inc' parameter (can be null)
	 * @param filterParams Additional parameters depending on the entity (can be null)
	 * 
	 * @return An URL as String
	 */
    protected String makeURL(final String entity, final String id, final List<String> includeParams, final Map<String, String> filterParams) {
        StringBuffer url = new StringBuffer();
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("type", this.type);
        if (filterParams != null) {
            urlParams.putAll(filterParams);
        }
        url.append(this.protocol).append("://").append(this.host);
        if (this.port != null) {
            url.append(":").append(this.port);
        }
        url.append(this.pathPrefix).append("/").append(this.version).append("/").append(entity).append("/").append(id);
        if (includeParams != null) {
            urlParams.put("inc", StringUtils.join(includeParams, " "));
        }
        url.append("?");
        Iterator<Entry<String, String>> it = urlParams.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> e = it.next();
            try {
                url.append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue(), DefaultWebService.URL_ENCODING)).append("&");
            } catch (UnsupportedEncodingException ex) {
                this.log.error("Internal Error: Could not encode url parameter " + e.getKey(), ex);
            }
        }
        return url.substring(0, url.length() - 1);
    }

    /**
	 * @return the parser
	 */
    public MbXmlParser getParser() {
        return this.parser;
    }

    /**
	 * @param parser the parser to set
	 */
    public void setParser(final MbXmlParser parser) {
        this.parser = parser;
    }
}
