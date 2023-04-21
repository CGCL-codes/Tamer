package org.oclc.da.common.textformat;

import org.oclc.da.properties.XMLProps;

/**
 * This class defines all of the tags used to encode a <code>Ref</code> into
 * an XML string.
 * 
 * @author JCG
 */
public class RefTags {

    /** The Ref tag */
    public static final String REF_TAG = "ref";

    /** The Key tag */
    public static final String KEY_TAG = "key";

    /** The Type attribute */
    public static final String TYPE_ATTRIB = "type";

    /** The Value attribute */
    public static final String VALUE_ATTRIB = "value";

    /** Standard XML Start tag */
    public static final String XML_START_TAG = "<";

    /** Standard XML Closing tag */
    public static final String XML_CLOSE_TAG = "</";

    /** Standard XML End tag */
    public static final String XML_END_TAG = ">";

    /** Namespace prefix for Refs */
    public static final String REF_NAMESPACE_PREFIX = XMLProps.REF_NAMESPACE_ABBREV;

    /** Namespace attribute for Refs */
    public static final String REF_NAMESPACE_ATTRIB = "xmlns:" + REF_NAMESPACE_PREFIX;

    /** Namespace URI for Refs */
    public static final String REF_NAMESPACE_URI = XMLProps.REF_NAMESPACE_URI;
}
