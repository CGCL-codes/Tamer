package com.sun.ebxml.registry.security.authorization;

import java.net.URI;
import org.w3c.dom.Node;
import com.sun.xacml.attr.AttributeValue;

/**
 * Representation of an java.lang.Object value. 
 * All objects of this class are immutable and
 * all methods of the class are thread-safe.
 *
 * @author Farrukh Najmi
 */
public class ObjectAttribute extends AttributeValue {

    /**
     * Official name of this type
     */
    public static final String identifier = "http://www.w3.org/2001/XMLSchema#object";

    /**
     * URI version of name for this type
     * <p>
     * This field is initialized by a static initializer so that
     * we can catch any exceptions thrown by URI(String) and
     * transform them into a RuntimeException, since this should
     * never happen but should be reported properly if it ever does.
     */
    private static URI identifierURI;

    /**
     * RuntimeException that wraps an Exception thrown during the
     * creation of identifierURI, null if none.
     */
    private static RuntimeException earlyException;

    /**
     * Static initializer that initializes the identifierURI
     * class field so that we can catch any exceptions thrown
     * by URI(String) and transform them into a RuntimeException.
     * Such exceptions should never happen but should be reported
     * properly if they ever do.
     */
    static {
        try {
            identifierURI = new URI(identifier);
        } catch (Exception e) {
            earlyException = new IllegalArgumentException();
            earlyException.initCause(e);
        }
    }

    ;

    /**
     * The actual Object value that this object represents.
     */
    private Object value;

    /**
     * Creates a new <code>ObjectAttribute</code> that represents
     * the Object value supplied.
     *
     * @param value the <code>Object</code> value to be represented
     */
    public ObjectAttribute(Object value) {
        super(identifierURI);
        if (earlyException != null) throw earlyException;
        this.value = value;
    }

    /**
     * Returns a new <code>ObjectAttribute</code> that represents
     * a java.lang.Object.
     *
     * @param root the <code>Node</code> that contains the desired value
     * @return a new <code>ObjectAttribute</code> representing the
     *         appropriate value (null if there is a parsing error)
     */
    public static ObjectAttribute getInstance(Node root) {
        return getInstance(root.getFirstChild().getNodeValue());
    }

    /**
     * Returns a new <code>ObjectAttribute</code> that represents
     * the Object value indicated by the <code>Object</code> provided.
     *
     * @param value a Object representing the desired value
     * @return a new <code>ObjectAttribute</code> representing the
     *         appropriate value
     */
    public static ObjectAttribute getInstance(Object value) {
        return new ObjectAttribute(value);
    }

    /**
     * Returns the <code>Object</code> value represented by this object.
     *
     * @return the <code>Object</code> value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns true if the input is an instance of this class and if its
     * value equals the value contained in this class.
     *
     * @param o the object to compare
     *
     * @return true if this object and the input represent the same value
     */
    public boolean equals(Object o) {
        if (!(o instanceof ObjectAttribute)) return false;
        ObjectAttribute other = (ObjectAttribute) o;
        return value.equals(other.value);
    }

    /**
     * Returns the hashcode value used to index and compare this object with
     * others of the same type. Typically this is the hashcode of the backing
     * data object.
     *
     * @return the object's hashcode value
     */
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Converts to a String representation.
     *
     * @return the String representation
     */
    public String toString() {
        return "ObjectAttribute: \"" + value + "\"";
    }

    /**
     *
     */
    public String encode() {
        return value.toString();
    }
}
