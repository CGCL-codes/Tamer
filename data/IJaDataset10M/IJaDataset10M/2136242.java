package com.hp.hpl.jena.vocabulary;

import com.hp.hpl.jena.rdf.model.*;

/** TestQuery vocabulary class for namespace http://jena.hpl.hp.com/2003/03/test-query#
 */
public class TestQuery {

    protected static final String uri = "http://jena.hpl.hp.com/2003/03/test-query#";

    /** returns the URI for this schema
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    public static final Resource QueryTest = ResourceFactory.createResource(uri + "QueryTest");

    public static final Property query = ResourceFactory.createProperty(uri + "query");

    public static final Property data = ResourceFactory.createProperty(uri + "data");
}
