package cx.ath.contribs.internal.xerces.util;

import cx.ath.contribs.internal.xerces.xni.XMLResourceIdentifier;

/**
 * The XMLResourceIdentifierImpl class is an implementation of the 
 * XMLResourceIdentifier interface which defines the location identity
 * of a resource.
 *
 * @author Andy Clark 
 *
 * @version $Id: XMLResourceIdentifierImpl.java,v 1.1 2007/06/02 09:58:58 paul Exp $
 */
public class XMLResourceIdentifierImpl implements XMLResourceIdentifier {

    /** The public identifier. */
    protected String fPublicId;

    /** The literal system identifier. */
    protected String fLiteralSystemId;

    /** The base system identifier. */
    protected String fBaseSystemId;

    /** The expanded system identifier. */
    protected String fExpandedSystemId;

    /** The namespace of the resource. */
    protected String fNamespace;

    /** Constructs an empty resource identifier. */
    public XMLResourceIdentifierImpl() {
    }

    /**
     * Constructs a resource identifier.
     *
     * @param publicId The public identifier.
     * @param literalSystemId The literal system identifier.
     * @param baseSystemId The base system identifier.
     * @param expandedSystemId The expanded system identifier.
     */
    public XMLResourceIdentifierImpl(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId) {
        setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, null);
    }

    /**
     * Constructs a resource identifier.
     *
     * @param publicId The public identifier.
     * @param literalSystemId The literal system identifier.
     * @param baseSystemId The base system identifier.
     * @param expandedSystemId The expanded system identifier.
     * @param namespace The namespace.
     */
    public XMLResourceIdentifierImpl(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace) {
        setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, namespace);
    }

    /** Sets the values of the resource identifier. */
    public void setValues(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId) {
        setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, null);
    }

    /** Sets the values of the resource identifier. */
    public void setValues(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace) {
        fPublicId = publicId;
        fLiteralSystemId = literalSystemId;
        fBaseSystemId = baseSystemId;
        fExpandedSystemId = expandedSystemId;
        fNamespace = namespace;
    }

    /** Clears the values. */
    public void clear() {
        fPublicId = null;
        fLiteralSystemId = null;
        fBaseSystemId = null;
        fExpandedSystemId = null;
        fNamespace = null;
    }

    /** Sets the public identifier. */
    public void setPublicId(String publicId) {
        fPublicId = publicId;
    }

    /** Sets the literal system identifier. */
    public void setLiteralSystemId(String literalSystemId) {
        fLiteralSystemId = literalSystemId;
    }

    /** Sets the base system identifier. */
    public void setBaseSystemId(String baseSystemId) {
        fBaseSystemId = baseSystemId;
    }

    /** Sets the expanded system identifier. */
    public void setExpandedSystemId(String expandedSystemId) {
        fExpandedSystemId = expandedSystemId;
    }

    /** Sets the namespace of the resource. */
    public void setNamespace(String namespace) {
        fNamespace = namespace;
    }

    /** Returns the public identifier. */
    public String getPublicId() {
        return fPublicId;
    }

    /** Returns the literal system identifier. */
    public String getLiteralSystemId() {
        return fLiteralSystemId;
    }

    /** 
     * Returns the base URI against which the literal SystemId is to be resolved.
     */
    public String getBaseSystemId() {
        return fBaseSystemId;
    }

    /** Returns the expanded system identifier. */
    public String getExpandedSystemId() {
        return fExpandedSystemId;
    }

    /** Returns the namespace of the resource. */
    public String getNamespace() {
        return fNamespace;
    }

    /** Returns a hash code for this object. */
    public int hashCode() {
        int code = 0;
        if (fPublicId != null) {
            code += fPublicId.hashCode();
        }
        if (fLiteralSystemId != null) {
            code += fLiteralSystemId.hashCode();
        }
        if (fBaseSystemId != null) {
            code += fBaseSystemId.hashCode();
        }
        if (fExpandedSystemId != null) {
            code += fExpandedSystemId.hashCode();
        }
        if (fNamespace != null) {
            code += fNamespace.hashCode();
        }
        return code;
    }

    /** Returns a string representation of this object. */
    public String toString() {
        StringBuffer str = new StringBuffer();
        if (fPublicId != null) {
            str.append(fPublicId);
        }
        str.append(':');
        if (fLiteralSystemId != null) {
            str.append(fLiteralSystemId);
        }
        str.append(':');
        if (fBaseSystemId != null) {
            str.append(fBaseSystemId);
        }
        str.append(':');
        if (fExpandedSystemId != null) {
            str.append(fExpandedSystemId);
        }
        str.append(':');
        if (fNamespace != null) {
            str.append(fNamespace);
        }
        return str.toString();
    }
}
