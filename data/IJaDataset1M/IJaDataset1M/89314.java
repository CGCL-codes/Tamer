package org.archive.crawler.framework;

import java.io.Serializable;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.CoreAttributeConstants;

/**
 * Record of all interesting info about the most-recent
 * processing of a specific seed.
 * 
 * @author gojomo
 */
public class SeedRecord implements CoreAttributeConstants, Serializable {

    private static final long serialVersionUID = -8455358640509744478L;

    private final String uri;

    private int statusCode;

    private final String disposition;

    private String redirectUri;

    /**
     * Create a record from the given CrawlURI and disposition string
     * 
     * @param curi CrawlURI, already processed as reported to StatisticsTracker
     * @param disposition descriptive disposition string
     * 
     */
    public SeedRecord(CrawlURI curi, String disposition) {
        super();
        this.uri = curi.toString();
        this.statusCode = curi.getFetchStatus();
        this.disposition = disposition;
        if (statusCode == 301 || statusCode == 302) {
            for (CrawlURI cauri : curi.getOutCandidates()) {
                if ("location:".equalsIgnoreCase(cauri.getViaContext().toString())) {
                    redirectUri = cauri.toString();
                }
            }
        }
    }

    /**
     * Constructor for when a CrawlURI is unavailable; such
     * as when considering seeds not yet passed through as
     * CrawlURIs. 
     * 
     * @param uri
     * @param disposition
     */
    public SeedRecord(String uri, String disposition) {
        this(uri, disposition, -1, null);
    }

    /**
     * Create a record from the given URI, disposition, HTTP status code,
     * and redirect URI.
     * @param uri
     * @param disposition
     * @param statusCode
     * @param redirectUri
     */
    public SeedRecord(String uri, String disposition, int statusCode, String redirectUri) {
        super();
        this.uri = uri;
        this.statusCode = statusCode;
        this.disposition = disposition;
        this.redirectUri = redirectUri;
    }

    /**
     * @return Returns the disposition.
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * @return Returns the redirectUri.
     */
    public String getRedirectUri() {
        return redirectUri;
    }

    /**
     * @return Returns the statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return Returns the uri.
     */
    public String getUri() {
        return uri;
    }
}
