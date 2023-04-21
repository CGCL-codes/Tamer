package org.tripcom.query.client.RDF.types;

import org.tripcom.query.client.RDF.Literal;
import org.tripcom.query.client.RDF.Resource;
import org.tripcom.query.client.RDF.ns.PROFIUM;
import org.tripcom.query.client.RDF.ns.RDF;
import org.tripcom.query.client.RDF.ns.XSD;

/**
 * Factory used to build literal objects of different types.
 */
public class LiteralFactory {

    public static final ValidationMode LOOSE = new ValidationMode();

    public static final ValidationMode VALIDATE = new ValidationMode();

    public static final ValidationMode NORMALIZE = new ValidationMode();

    public static final class ValidationMode {

        private ValidationMode() {
        }
    }

    static {
    }

    /**
     * Static method to get one new untyped literal.
     * 
     * @param sValue
     *            Lexical representation of the literal
     */
    public static Literal getLiteral(String sValue) {
        return new StringLiteral(sValue, (String) null);
    }

    /**
     * Static method to get one new untyped literal with the given language
     * 
     * @param sValue
     *            Lexical representation of the literal
     * @param lang
     *            language of the literal
     * @return
     */
    public static Literal getLiteralWithLanguage(String sValue, String lang) {
        return new StringLiteral(sValue, lang);
    }

    /**
     * Static method to get one new literal whose data type is <b>sURI</b>
     * using the default validation mode.
     * 
     * @param sValue
     *            Lexical representation of the literal
     * @param sURI
     *            Data type URI encoded as String
     */
    public static Literal getLiteral(String sValue, String sURI) {
        return getLiteral(sValue, sURI, getValidationMode());
    }

    /**
     * @param date
     * @param uri
     * @return
     */
    public static Literal getLiteral(java.util.Date date, String uri) {
        if (uri == null) throw new NullPointerException("uri was null"); else return getLiteral("Not yet implemented");
    }

    /**
     * @param value
     * @param uri
     * @return
     */
    public static Literal getLiteral(Integer value, String uri) {
        return getLiteral(String.valueOf(value), value, uri, LOOSE);
    }

    /**
     * @param value
     * @param uri
     * @return
     */
    public static Literal getLiteral(Double value, String uri) {
        return getLiteral(String.valueOf(value), value, uri, LOOSE);
    }

    /**
     * @param value
     * @param uri
     * @return
     */
    public static Literal getLiteral(Float value, String uri) {
        return getLiteral(String.valueOf(value), value, uri, LOOSE);
    }

    /**
     * @param value
     * @param uri
     * @return
     */
    public static Literal getLiteral(Boolean value, String uri) {
        return getLiteral(String.valueOf(value), value, uri, LOOSE);
    }

    /**
     * @param value
     * @param uri
     * @return
     */
    public static Literal getLiteral(Long value, String uri) {
        return getLiteral(String.valueOf(value), value, uri, LOOSE);
    }

    /**
     * @param value
     * @param uri
     * @return
     */
    public static Literal getLiteral(Short value, String uri) {
        return getLiteral(String.valueOf(value), value, uri, LOOSE);
    }

    /**
     * Static method to get one new literal whose data type is <b>sURI</b>
     * using the given validation mode.
     * 
     * @param sValue
     *            Lexical representation of the literal
     * @param sURI
     *            Data type URI encoded as String
     */
    public static Literal getLiteral(String sValue, String sURI, ValidationMode mode) {
        return getLiteral(sValue, null, sURI, mode);
    }

    /**
     * @param sValue
     * @param valueObject
     * @param sURI
     * @param mode
     * @return
     */
    private static Literal getLiteral(String sValue, Object valueObject, String sURI, ValidationMode mode) {
        if (sURI == null) {
            if (sValue != null) {
                return new StringLiteral(sValue);
            } else {
                Exception e = new RuntimeException("Lexical representation of literal was null.");
                return null;
            }
        }
        Literal literal = null;
        literal = new Literal(sValue, new Resource(sURI));
        if (mode == LOOSE) {
            return literal;
        } else {
            if (!literal.isValid()) {
                Exception e = new RuntimeException("Literal with lexical representation " + sValue + " and datatype URI " + sURI + " was null.");
                return null;
            }
            if (mode == NORMALIZE) {
                literal.setLocalName(literal.getNormalizedLabel());
            }
            return literal;
        }
    }

    /**
     * @return
     */
    public static ValidationMode getValidationMode() {
        return null;
    }

    /**
     * @param mode
     */
    public static void setValidationMode(ValidationMode mode) {
    }
}
