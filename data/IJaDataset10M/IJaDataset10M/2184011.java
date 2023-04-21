package org.mulgara.mrg.vocab.uri;

import java.net.URI;

/**
 * A class for holding the RDF vocabulary.
 */
public class RDF {

    /** The QName prefix for RDF */
    public static final String PREFIX = "rdf";

    /** The RDF namespace */
    public static final String BASE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    /** The class of unordered containers. */
    public static final URI BAG = URI.create(BASE + "Bag");

    /** The class of ordered containers. */
    public static final URI SEQ = URI.create(BASE + "Seq");

    /** The class of containers of alternatives. */
    public static final URI ALT = URI.create(BASE + "Alt");

    /** The class of RDF statements. */
    public static final URI STATEMENT = URI.create(BASE + "Statement");

    /** The class of RDF properties. */
    public static final URI PROPERTY = URI.create(BASE + "Property");

    /** The class of XML literal values. */
    public static final URI XML_LITERAL = URI.create(BASE + "XMLLiteral");

    /** The class of RDF Lists. */
    public static final URI LIST = URI.create(BASE + "List");

    /**
   * A special property element that is equivalent to rdf:_1, rdf:_2 in order.
   * Only used in RDF/XML as inserting members of containers using LI normally
   * will result in duplicate instances not being recorded.
   */
    public static final URI LI = URI.create(BASE + "li");

    /**
   * The empty list, with no items in it. If the rest of a list is nil then
   * the list has no more items in it.
   */
    public static final URI NIL = URI.create(BASE + "nil");

    /** The subject of the subject RDF statement. */
    public static final URI SUBJECT = URI.create(BASE + "subject");

    /** The predicate of the subject RDF statement. */
    public static final URI PREDICATE = URI.create(BASE + "predicate");

    /** The object of the subject RDF statement. */
    public static final URI OBJECT = URI.create(BASE + "object");

    /** The subject is an instance of a class. */
    public static final URI TYPE = URI.create(BASE + "type");

    /** Idiomatic property used for structured values. */
    public static final URI VALUE = URI.create(BASE + "value");

    /** The first item in the subject RDF list. */
    public static final URI FIRST = URI.create(BASE + "first");

    /** The rest of the subject RDF list after the first item. */
    public static final URI REST = URI.create(BASE + "rest");

    /** The abbreviation for the class of XML literal values. */
    public static final URI XML_LITERAL_QNAME = URI.create(PREFIX + ":XMLLiteral");
}
