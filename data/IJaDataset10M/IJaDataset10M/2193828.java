package net.sf.eBus.xml;

/**
 * Contains the top-level attributes for an XML document. These
 * include:
 * <ol>
 *   <li>
 *     XML version.
 *   </li>
 *   <li>
 *     encoding
 *   </li>
 *   <li>
 *     stand-alone flag
 *   </li>
 *   <li>
 *     root tag
 *   </li>
 * </ol>
 * This class is immutable.
 * <p>
 * This class is not designed to fulfill any XML API
 * specification.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
public final class XmlDocument {

    /**
     * Creates an XML document. Data members are set to the
     * provided values.
     * @param version The XML protocol version.
     * @param encoding The document's text encoding.
     * @param standAloneFlag {@code true} if this is a
     * stand-alone XML document.
     * @param rootTag The document's one-and-only root tag.
     */
    public XmlDocument(final String version, final String encoding, final boolean standAloneFlag, final XmlTag rootTag) {
        _version = version;
        _encoding = encoding;
        _standAloneFlag = standAloneFlag;
        _rootTag = rootTag;
    }

    /**
     * Returns the document's XML version.
     * @return the document's XML version.
     */
    public String version() {
        return (_version);
    }

    /**
     * Returns the document's encoding.
     * @return the document's encoding.
     */
    public String encoding() {
        return (_encoding);
    }

    /**
     * Returns {@code true} if this is a stand-alone XML
     * document and {@code false} otherwise.
     * @return {@code true} if this is a stand-alone XML
     * document and {@code false} otherwise.
     */
    public boolean isStandAlone() {
        return (_standAloneFlag);
    }

    /**
     * Returns the document's root tag.
     * @return the document's root tag.
     */
    public XmlTag rootTag() {
        return (_rootTag);
    }

    /**
     * Returns {@code true} if {@code o} is a non-null
     * XmlDocument instance with the same version, encoding
     * and root tag.
     * @param o Compare against this object.
     * @return {@code true} if {@code o} is a non-null
     * XmlDocument instance with the same version, encoding
     * and root tag.
     */
    @Override
    public boolean equals(final Object o) {
        boolean retcode = (this == o);
        if (retcode == false && o instanceof XmlDocument) {
            final XmlDocument doc = (XmlDocument) o;
            retcode = (_version.equals(doc._version) == true && (_encoding == null ? doc._encoding == null : _encoding.equals(doc._encoding) == true) && _standAloneFlag == doc._standAloneFlag && (_rootTag == null ? doc._rootTag == null : _rootTag.equals(doc._rootTag) == true));
        }
        return (retcode);
    }

    /**
     * Returns the document's hash code.
     * @return the document's hash code.
     */
    @Override
    public int hashCode() {
        return (_version.hashCode() ^ _encoding.hashCode() ^ (_standAloneFlag == true ? STAND_ALONE_HASH : DTD_HASH) ^ _rootTag.hashCode());
    }

    /**
     * Returns a textual version of this XML document.
     * The returned value is a valid XML document.
     * @return a textual version of this XML document.
     */
    @Override
    public String toString() {
        final StringBuilder retval = new StringBuilder();
        retval.append(String.format("<?xml version=\"%s\"", _version));
        if (_encoding != null) {
            retval.append(String.format(" encoding=\"%s\"", _encoding));
        }
        retval.append(String.format(" standalone=\"%s\"?>%n", (_standAloneFlag == true ? "yes" : "no")));
        if (_rootTag != null) {
            retval.append(_rootTag.toString());
        }
        return (retval.toString());
    }

    private final String _version;

    private final String _encoding;

    private final boolean _standAloneFlag;

    private final XmlTag _rootTag;

    /**
     * The default XML version is 1.0.
     */
    public static final String DEFAULT_VERSION = "1.0";

    /**
     * The default XML encoding is UTF-8.
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    private static final int STAND_ALONE_HASH = 1231;

    private static final int DTD_HASH = 1237;
}
