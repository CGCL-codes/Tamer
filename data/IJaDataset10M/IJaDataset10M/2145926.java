package org.base.apps.xml.elem;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.StringUtils;
import org.base.apps.xml.elem.impl.BaseNamespace;
import org.base.apps.xml.elem.util.NodeError;
import org.base.apps.xml.elem.util.XmlAssert;
import org.base.apps.xml.elem.vis.NodeVisitor;

/**
 * Represents an XML node tag, with local name, and optional namespace URI and 
 * prefix (like a mutable {@link QName}.
 * 
 * @author Kevan Simpson
 */
public class Tag extends BaseNamespace {

    private String mLocalName;

    /**
     * Void constructor, with a local name of &quot;xml&quot;.
     */
    public Tag() {
        this("xml");
    }

    public Tag(CharSequence lname) {
        this(lname, XMLConstants.NULL_NS_URI);
    }

    public Tag(CharSequence lname, String ns) {
        this(lname, ns, XMLConstants.DEFAULT_NS_PREFIX);
    }

    public Tag(CharSequence lname, String ns, String prefix) {
        super(prefix, ns);
        setLocalName(lname);
    }

    public Tag(QName qname) {
        this(qname.getLocalPart(), qname.getNamespaceURI(), qname.getPrefix());
    }

    /**
     * Guaranteed to return a unique hashCode for each distinct instance.
     *  
     * @see java.lang.Object#hashCode() 
     */
    @Override
    public final int hashCode() {
        return superHashCode();
    }

    /**
     * Compares object identity to determine equality.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(Object obj) {
        return superEquals(obj);
    }

    /**
     * Follows the same format as prescribed by 
     * @see java.lang.Object#toString() */
    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("{");
        if (StringUtils.isNotBlank(getPrefix())) {
            buff.append(getPrefix()).append(":");
        }
        return buff.append(getLocalName()).append("}").toString();
    }

    /**
     * Returns the unqualified node name.
     * @return the unqualified node name.
     */
    public String getLocalName() {
        return mLocalName;
    }

    /**
     * Sets the unqualified node name.
     * @param lname The unqualified node name
     * @return this <code>Tag</code>
     * @throws NodeError if the given node name is blank
     */
    public Tag setLocalName(CharSequence lname) {
        XmlAssert.validNodeName(lname);
        mLocalName = String.valueOf(lname);
        return this;
    }

    /** @see org.base.apps.xml.elem.impl.BaseNamespace#setPrefix(java.lang.CharSequence) */
    @Override
    public void setPrefix(CharSequence prefix) {
        super.setPrefix(prefix);
    }

    /** @see org.base.apps.xml.elem.impl.BaseNamespace#setNamespaceURI(java.lang.CharSequence) */
    @Override
    public void setNamespaceURI(CharSequence namespaceURI) {
        super.setNamespaceURI(namespaceURI);
    }

    /** @see org.base.apps.xml.elem.vis.VisitableNode#accept(org.base.apps.xml.elem.vis.NodeVisitor, java.lang.Object) */
    @Override
    public <R, P> R accept(NodeVisitor<R, P> vis, P param) {
        return (vis == null) ? null : vis.visit(this, param);
    }

    /**
     * Copies the prefix, local name, and namespace URI from one <code>Tag</code>
     * to another.
     * 
     * @param src The source <code>Tag</code>.
     * @param dest The destination <code>Tag</code>.
     */
    public static void copy(Tag src, Tag dest) {
        if (src != null && dest != null) {
            dest.setLocalName(src.getLocalName());
            dest.setNamespaceURI(src.getNamespaceURI());
            dest.setPrefix(src.getPrefix());
        }
    }

    /**
     * <p><code>Tag</code> derived from parsing the formatted <code>String</code>.</p>
     * <p>This method follows the same pattern as {@link QName#valueOf(String)},
     * as most of the following documentation and implementation are identical
     * (swapping <code>Tag</code> for {@link QName} references).
     * 
     * <p>If the <code>String</code> is <code>null</code> or does not conform to
     * the same format as prescribed by {@link #toString() Tag.toString()}, an
     * <code>IllegalArgumentException</code> is thrown.</p>
     *
     * <p><em>The <code>String</code> <strong>MUST</strong> be in the
     * form returned by {@link #toString() Tag.toString()}.</em></p>
     *
     * <p>The commonly accepted way of representing a <code>QName</code>
     * (and, as of this release, <code>Tag</code> as well)
     * as a <code>String</code> was
     * <a href="http://jclark.com/xml/xmlns.htm">defined</a>
     * by James Clark.  Although this is not a <em>standard</em>
     * specification, it is in common use, e.g. {@link
     * javax.xml.transform.Transformer#setParameter(String name, Object value)}.
     * This implementation parses a <code>String</code> formatted
     * as: "{" + Namespace URI + "}" + local part.  If the Namespace
     * URI <code>.equals(XMLConstants.NULL_NS_URI)</code>, only the
     * local part should be provided.</p>
     *
     * <p>The prefix value <strong><em>CANNOT</em></strong> be
     * represented in the <code>String</code> and will be set to
     * {@link javax.xml.XMLConstants#DEFAULT_NS_PREFIX
     * XMLConstants.DEFAULT_NS_PREFIX}.</p>
     *
     * <p>This method does not do full validation of the resulting <code>Tag</code>.
     * <p>The Namespace URI is not validated as a
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">URI reference</a>.
     * The local part is not validated as a
     * <a href="http://www.w3.org/TR/REC-xml-names/#NT-NCName">NCName</a>
     * as specified in
     * <a href="http://www.w3.org/TR/REC-xml-names/">Namespaces in XML</a>.</p>
     *
     * @param tagAsString <code>String</code> representation of the <code>Tag</code>
     * @throws IllegalArgumentException When <code>tagAsString</code> is
     *                                  <code>null</code> or malformed
     * @return <code>Tag</code> corresponding to the given <code>String</code>
     * @see #toString() Tag.toString()
     * @see QName#toString() QName.toString()
     */
    public static Tag valueOf(String tagAsString) {
        if (tagAsString == null) {
            throw new IllegalArgumentException("cannot create Tag from \"null\" or \"\" String");
        }
        if (tagAsString.length() == 0) {
            return new Tag(new QName(XMLConstants.NULL_NS_URI, tagAsString, XMLConstants.DEFAULT_NS_PREFIX));
        }
        if (tagAsString.charAt(0) != '{') {
            return new Tag(new QName(XMLConstants.NULL_NS_URI, tagAsString, XMLConstants.DEFAULT_NS_PREFIX));
        }
        if (tagAsString.startsWith("{" + XMLConstants.NULL_NS_URI + "}")) {
            throw new IllegalArgumentException("Namespace URI .equals(XMLConstants.NULL_NS_URI), " + ".equals(\"" + XMLConstants.NULL_NS_URI + "\"), " + "only the local part, " + "\"" + tagAsString.substring(2 + XMLConstants.NULL_NS_URI.length()) + "\", " + "should be provided.");
        }
        int endOfNamespaceURI = tagAsString.indexOf('}');
        if (endOfNamespaceURI == -1) {
            throw new IllegalArgumentException("cannot create Tag from \"" + tagAsString + "\", missing closing \"}\"");
        }
        return new Tag(new QName(tagAsString.substring(1, endOfNamespaceURI), tagAsString.substring(endOfNamespaceURI + 1), XMLConstants.DEFAULT_NS_PREFIX));
    }

    /**
     * Returns <code>true</code> if both <code>Tag</code>s are 
     * {@link #equivalent(Tag, Tag) equivalent} and have the same 
     * {@link #getPrefix() prefices}.
     * 
     * @param tag1 The first tag.
     * @param tag2 The second tag.
     * @return <code>true</code> if the two <code>Tag</code>s are identical.
     */
    public static boolean identical(Tag tag1, Tag tag2) {
        return (similar(tag1, tag2) && equivalent(tag1, tag2) && StringUtils.equals(tag1.getPrefix(), tag2.getPrefix()));
    }

    /**
     * Returns <code>true</code> if both <code>Tag</code>s are 
     * {@link #similar(Tag, Tag) similar} and have the same 
     * {@link #getNamespaceURI() namespace URIs}.
     * 
     * @param tag1 The first tag.
     * @param tag2 The second tag.
     * @return <code>true</code> if the two <code>Tag</code>s are equivalent.
     */
    public static boolean equivalent(Tag tag1, Tag tag2) {
        return (similar(tag1, tag2) && StringUtils.equals(tag1.getNamespaceURI(), tag2.getNamespaceURI()));
    }

    /**
     * Returns <code>true</code> if both <code>Tag</code>s are non-<code>null</code>
     * and have the same {@link #getLocalName() local names}.
     * 
     * @param tag1 The first tag.
     * @param tag2 The second tag.
     * @return <code>true</code> if the two <code>Tag</code>s are similar.
     */
    public static boolean similar(Tag tag1, Tag tag2) {
        if (tag1 == null) {
            return (tag2 == null);
        } else {
            return (tag2 != null && StringUtils.equals(tag1.getLocalName(), tag2.getLocalName()));
        }
    }
}
