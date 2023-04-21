package org.jaxen.saxpath;

/** Interface for readers which can parse textual
 *  XPath expressions, and produce callbacks to
 *  {@link org.jaxen.saxpath.XPathHandler} objects.
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public interface XPathReader extends SAXPathEventSource {

    /** Parse an XPath expression,
     *  and send event callbacks to an {@link org.jaxen.saxpath.XPathHandler}.
     *
     *  @param xpath the textual XPath expression to parse
     *
     *  @throws SAXPathException if the expression is syntactically incorrect
     */
    void parse(String xpath) throws SAXPathException;
}
