package uk.ac.cam.caret.tagphage.parser;

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.*;

/** Container for the current state of parsing. Used for retrieving the
 * {@link ObjectStack}, and retrieving parsers, during document parsing. Within actions
 * is also useful for managing XML "unparsed external entities" (if you wish to), getting
 * the current prefix mapping context, etc.
 * 
 * @author Dan Sheppard <dan@caret.cam.ac.uk>
 *
 */
public class ParseState {

    private ParserFactory pf;

    private ParseHandler ph;

    private OuterHandler oh;

    private ElementStack elements;

    private ObjectStack objects = new ObjectStack();

    private OuterHandler root_oh;

    private Rules rules;

    ParseState(ParserFactory pf, Rules r) throws BadConfigException {
        this.pf = pf;
        oh = new OuterHandler(this);
        ph = new ParseHandler(this);
        elements = new ElementStack(oh, ph);
        root_oh = oh;
        rules = r;
    }

    void setRootParseState(ParseState root) {
        root_oh = root.root_oh;
        objects = root.objects;
    }

    Rules getRules() {
        return rules;
    }

    void pushElement(String namespace, String name) {
        elements.push(new ElementStackMember(namespace, name));
    }

    void addNamespace(String prefix, String url) {
        elements.addNamespace(prefix, url);
    }

    void prePop() throws SAXException {
        elements.prePop();
    }

    void popElement() throws SAXException {
        elements.pop();
    }

    void addFilter(Filter f, String namespace, String name, Attributes attrs) throws SAXException {
        elements.addFilter(f, namespace, name, attrs);
    }

    ElementStack getElementStack() {
        return elements;
    }

    /** <b>ADVANCED</b> Retrieve the namespace map currently in force. Used within 
	 * actions to resolve prefixes, if necessary.
	 * 
	 * @return the current namespace map.
	 */
    public NamespaceResolver getNamespaceMap() {
        return elements;
    }

    /** Retrieve an XMLReader ready to parse your data.
	 * 
	 * @return The XMLReader to parse your data.
	 * 
	 * @throws BadConfigException exception thrown whilst creating reader.
	 * @throws SAXException exception thrown whilst creating reader.
	 */
    public XMLReader getReader() throws BadConfigException, SAXException {
        try {
            ContentHandler ch = oh;
            byte[][] xslts = pf.getXSLTs();
            SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            for (byte[] xslt : xslts) {
                TransformerHandler th = tf.newTransformerHandler(new StreamSource(new ByteArrayInputStream(xslt)));
                th.setResult(new SAXResult(ch));
                ch = th;
            }
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(ch);
            reader.setDTDHandler(root_oh);
            reader.setEntityResolver(root_oh);
            reader.setErrorHandler(root_oh);
            return reader;
        } catch (TransformerConfigurationException x) {
            throw new BadConfigException("Error adding stylesheet", x);
        }
    }

    public NamespaceExpander getNamespaceExpander() {
        return rules.getNamespaceExpander();
    }

    /** Retrieve the parsing object stack.
	 * 
	 * @return the object stack.
	 */
    public ObjectStack getObjectStack() {
        return objects;
    }

    void imposeDefaultNamespace(String in) {
        ph.imposeDefaultNamespace(in);
    }

    /** <b>ADVANCED</b> return the public ID of the given notation (NDATA), if any.
	 * 
	 * @param name name of notation
	 * @return public ID, or null if missing.
	 */
    public String notationGetPublicID(String name) {
        return oh.notationGetPublicID(name);
    }

    /** <b>ADVANCED</b> return the system ID of the given notation (NDATA), if any.
	 * 
	 * @param name name of notation
	 * @return system ID, or null if missing.
	 */
    public String notationGetSystemID(String name) {
        return oh.notationGetSystemID(name);
    }

    /** <b>ADVANCED</b> a list of names for all declared notations (NDATAs).
	 * 
	 * @return list of all notation names, in no particular order.
	 */
    public String[] getAllNotations() {
        return oh.getNotations();
    }

    /** <b>ADVANCED</b> get public ID of given unparsed external entity (if any).
	 * 
	 * @param name Name of unparsed external entity.
	 * @return public ID, or null if missing. 
	 */
    public String upeGetPublicID(String name) {
        return oh.upeGetPublicID(name);
    }

    /** <b>ADVANCED</b> get system ID of given unparsed external entity (if any).
	 * 
	 * @param name Name of unparsed external entity.
	 * @return system ID, or null if missing. 
	 */
    public String upeGetSystemID(String name) {
        return oh.upeGetSystemID(name);
    }

    /** <b>ADVANCED</b> get notation (NDATA) of given unparsed external entity (if any).
	 * 
	 * @param name Name of unparsed external entity.
	 * @return notation, or null if missing. 
	 */
    public String upeGetNotation(String name) {
        return oh.upeGetNotation(name);
    }

    /** <b>ADVANCED</b> get names of all declared unparsed external entities.
	 * 
	 * @return list of names of all declared uparsed external entities.
	 */
    public String[] getAllUpes() {
        return oh.getUpes();
    }

    /** Declare a SAX {@link org.xml.sax.EntityResolver} to use during parsing.
	 * 
	 * @param in The entity resolver to use.
	 */
    public void setEntityResolver(EntityResolver in) {
        root_oh.setEntityResolver(in);
    }

    /** Declare a SAX {@link org.xml.sax.ErrorHandler} to use during parsing.
	 * 
	 * @param in The error handler to use.
	 */
    public void setErrorHandler(ErrorHandler in) {
        root_oh.setErrorHandler(in);
    }

    /** Get the SAX {@link org.xml.sax.ContentHandler} which would be used during
	 * parsing. Useful if you wish to control parsing yourself, rather than call
	 * {@link #getReader()}. 
	 * 
	 * @return The contentHandler to use for parsing.
	 */
    public ContentHandler getHandler() {
        return oh;
    }

    /** Get the corresponding {@link ParserFactory} to this ParseState.
	 * 
	 * @return The corresponding {@link ParserFactory}.
	 */
    public ParserFactory getParserFactory() {
        return pf;
    }
}
