package org.openrdf.query.parser.sparql;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Constants for Dublin Core primitives and for the Dublin Core namespace.
 */
public final class DC {

    /** http://purl.org/dc/elements/1.1/ */
    public static final String NAMESPACE = "http://purl.org/dc/elements/1.1/";

    /** http://purl.org/dc/elements/1.1/contributor */
    public static final URI CONTRIBUTOR;

    /** http://purl.org/dc/elements/1.1/coverage */
    public static final URI COVERAGE;

    /** http://purl.org/dc/elements/1.1/creator */
    public static final URI CREATOR;

    /** http://purl.org/dc/elements/1.1/date */
    public static final URI DATE;

    /** http://purl.org/dc/elements/1.1/description */
    public static final URI DESCRIPTION;

    /** http://purl.org/dc/elements/1.1/format */
    public static final URI FORMAT;

    /** http://purl.org/dc/elements/1.1/identifier */
    public static final URI IDENTIFIER;

    /** http://purl.org/dc/elements/1.1/language */
    public static final URI LANGUAGE;

    /** http://purl.org/dc/elements/1.1/publisher */
    public static final URI PUBLISHER;

    /** http://purl.org/dc/elements/1.1/relation */
    public static final URI RELATION;

    /** http://purl.org/dc/elements/1.1/rights */
    public static final URI RIGHTS;

    /** http://purl.org/dc/elements/1.1/source */
    public static final URI SOURCE;

    /** http://purl.org/dc/elements/1.1/subject */
    public static final URI SUBJECT;

    /** http://purl.org/dc/elements/1.1/title */
    public static final URI TITLE;

    /** http://purl.org/dc/elements/1.1/type */
    public static final URI TYPE;

    static {
        ValueFactory factory = ValueFactoryImpl.getInstance();
        CONTRIBUTOR = factory.createURI(NAMESPACE, "contributor");
        COVERAGE = factory.createURI(NAMESPACE, "coverage");
        CREATOR = factory.createURI(NAMESPACE, "creator");
        DATE = factory.createURI(NAMESPACE, "date");
        DESCRIPTION = factory.createURI(NAMESPACE, "description");
        FORMAT = factory.createURI(NAMESPACE, "format");
        IDENTIFIER = factory.createURI(NAMESPACE, "identifier");
        LANGUAGE = factory.createURI(NAMESPACE, "language");
        PUBLISHER = factory.createURI(NAMESPACE, "publisher");
        RELATION = factory.createURI(NAMESPACE, "relation");
        RIGHTS = factory.createURI(NAMESPACE, "rights");
        SOURCE = factory.createURI(NAMESPACE, "source");
        SUBJECT = factory.createURI(NAMESPACE, "subject");
        TITLE = factory.createURI(NAMESPACE, "title");
        TYPE = factory.createURI(NAMESPACE, "type");
    }
}
