package org.jfree.xml;

import java.util.HashMap;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * The Parser handles the SAXEvents and forwards the event call to the currently
 * active ElementDefinitionHandler. Contains methods to manage and
 * configure the parsing process.
 * <p>
 * An initial report definition handler must be set before the parser can be used.
 *
 * @author Thomas Morgner
 */
public abstract class Parser extends FrontendDefaultHandler {

    /** A key for the content base. */
    public static final String CONTENTBASE_KEY = "content-base";

    /** A stack for the active factories. */
    private Stack activeFactories;

    /** The initial factory. */
    private ElementDefinitionHandler initialFactory;

    /** Storage for temporary objects and factories used during the parsing process. */
    private HashMap parserHelperObjects;

    /**
     * Creates a new parser.
     */
    public Parser() {
        this.activeFactories = new Stack();
        this.parserHelperObjects = new HashMap();
    }

    /**
     * Returns the currently collected comments.
     * @return the comments.
     */
    public String[] getComments() {
        return getCommentHandler().getComments();
    }

    /**
     * Pushes a handler onto the stack.
     *
     * @param factory  the handler.
     */
    public void pushFactory(final ElementDefinitionHandler factory) {
        this.activeFactories.push(factory);
    }

    /**
     * Reads a handler off the stack without removing it.
     *
     * @return The handler.
     */
    public ElementDefinitionHandler peekFactory() {
        return (ElementDefinitionHandler) this.activeFactories.peek();
    }

    /**
     * Pops a handler from the stack.
     *
     * @return The handler.
     */
    public ElementDefinitionHandler popFactory() {
        this.activeFactories.pop();
        return peekFactory();
    }

    /**
     * Receive notification of the end of the document.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the end
     * of a document (such as finalising a tree or closing an output
     * file).</p>
     *
     * @exception SAXException Any SAX exception, possibly wrapping another exception.
     *
     * @see org.xml.sax.ContentHandler#endDocument
     */
    public void endDocument() throws SAXException {
    }

    /**
     * Receive notification of the beginning of the document.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the beginning
     * of a document (such as allocating the root node of a tree or
     * creating an output file).</p>
     *
     * @exception SAXException Any SAX exception, possibly wrapping another exception.
     * @see org.xml.sax.ContentHandler#startDocument
     */
    public void startDocument() throws SAXException {
        this.activeFactories.clear();
        pushFactory(getInitialFactory());
    }

    /**
     * Receive notification of character data inside an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method to take specific actions for each chunk of character data
     * (such as adding the data to a node or buffer, or printing it to
     * a file).</p>
     *
     * @param ch  the characters.
     * @param start  the start position in the character array.
     * @param length  the number of characters to use from the character array.
     *
     * @exception SAXException Any SAX exception, possibly wrapping another exception.
     * @see org.xml.sax.ContentHandler#characters
     */
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        try {
            peekFactory().characters(ch, start, length);
        } catch (ParseException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ParseException(e, getLocator());
        }
    }

    /**
     * Receive notification of the end of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the end of
     * each element (such as finalising a tree node or writing
     * output to a file).</p>
     *
     * @param uri  the URI.
     * @param localName  the element type name.
     * @param qName  the name.
     *
     * @exception SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        try {
            peekFactory().endElement(qName);
        } catch (ParseException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ParseException(e, getLocator());
        } finally {
            getCommentHandler().clearComments();
        }
    }

    /**
     * Receive notification of the start of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the start of
     * each element (such as allocating a new tree node or writing
     * output to a file).</p>
     *
     * @param uri  the URI.
     * @param localName  the element type name.
     * @param qName  the name.
     * @param attributes  the specified or defaulted attributes.
     *
     * @exception SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        try {
            peekFactory().startElement(qName, attributes);
        } catch (ParseException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ParseException(e, getLocator());
        } finally {
            getCommentHandler().clearComments();
        }
    }

    /**
     * Sets the initial handler.
     *
     * @param factory  the initial handler.
     */
    public void setInitialFactory(final ElementDefinitionHandler factory) {
        this.initialFactory = factory;
    }

    /**
     * Returns the initial handler.
     *
     * @return The initial handler.
     */
    public ElementDefinitionHandler getInitialFactory() {
        return this.initialFactory;
    }

    /**
     * Sets a helper object.
     *
     * @param key  the key.
     * @param value  the value.
     */
    public void setHelperObject(final String key, final Object value) {
        if (value == null) {
            this.parserHelperObjects.remove(key);
        } else {
            this.parserHelperObjects.put(key, value);
        }
    }

    /**
     * Returns a helper object.
     *
     * @param key  the key.
     *
     * @return The object.
     */
    public Object getHelperObject(final String key) {
        return this.parserHelperObjects.get(key);
    }

    /**
     * Returns a new instance of the parser.
     *
     * @return a new instance of the parser.
     */
    public abstract Parser getInstance();

    /**
     * Returns a new instance of {@link FrontendDefaultHandler}.
     *
     * @return A new instance.
     */
    public final FrontendDefaultHandler newInstance() {
        return getInstance();
    }

    /**
     * Returns the parsed result object after the parsing is complete. Calling
     * this function during the parsing is undefined and may result in an
     * IllegalStateException.
     *
     * @return the parsed result.
     */
    public abstract Object getResult();
}
