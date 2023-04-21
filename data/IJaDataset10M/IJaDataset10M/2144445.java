package org.commonlibrary.lcms.support.atom.service;

/**
 * Atom feed service
 *
 * @author jcastillo
 *         Jul 27, 2009
 *         10:02:46 AM
 */
public interface AtomFeedService {

    /**
     * Gets the XML Atom feed for an entity (curriculum or folder)
     * @param entityId the entity id wich feed is going to be genereted
     * @param uri the uri request path
     * @return the entity XML Atom feed
     */
    String getFeed(String entityId, String uri);

    /**
     * Creates an URL Atom feed to access an entity
     * @param entityId the entity id that will be accessed via URL feed
     * @param entityName the entity name
     * @param clv2BaseUri the lcms base url. An url like "http://localhost:8080/lcms" is expected
     * @return the URL Atom feed
     */
    String createURLFeed(String clv2BaseUri, String entityId, String entityName);
}
