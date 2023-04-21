package org.apache.ws.jaxme.impl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.bind.UnmarshallerHandler;

/** The {@link javax.xml.parsers.SAXParser} is controlling
 * an internal stack of {@link JMSAXGroupParser} instances, one
 * for any nested sequence, choice, or all group being parsed.<br>
 * Note, that complex types with complex content are 
 */
public abstract class JMParser {

    /** Initializes the element parser by setting the required data.
	 */
    public abstract void init(UnmarshallerHandler pHandler, Object pObject, String pNamespaceURI, String pLocalName, int pLevel);

    /** Equivalent to
     * {@link org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}.
     */
    public abstract boolean startElement(String pNamespaceURI, String pLocalName, String pQName, Attributes pAttrs) throws SAXException;

    /** Roughly equivalent to
     * {@link org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}.
     * @param pResult The object that has been parsed.
     */
    public abstract void endElement(String pNamespaceURI, String pLocalName, String pQName, Object pResult) throws SAXException;

    /** Returns, whether the group contents are valid.
     */
    public abstract boolean isFinished();

    /** Returns, whether the group supports mixed content.
	 */
    public abstract boolean isMixed();

    /** Used for adding textual context. Valid only, if
	 * {@link #isMixed()} returns true.
	 * @param pChars Character buffer, as specified by
	 * {@link org.xml.sax.ContentHandler#characters(char[], int, int)}.
	 * @param pOffset Offset into buffer, as specified by
	 * {@link org.xml.sax.ContentHandler#characters(char[], int, int)}.
	 * @param pLen Length of relevant buffer part, as specified by
	 * {@link org.xml.sax.ContentHandler#characters(char[], int, int)}.
	 */
    public abstract void addText(char[] pChars, int pOffset, int pLen) throws SAXException;

    /** Returns the end elements level (number of nested
     * elements enclosing this element).
     */
    public abstract int getEndLevel();

    /** Returns, whether the element has atomic content.
	 */
    public abstract boolean isAtomic();

    /** Invokes {@link #addAttribute(String, String, String)} for
	 * all the attributes in the list <code>pAttrs</code>.
	 */
    public abstract void setAttributes(Attributes pAttrs) throws SAXException;
}
