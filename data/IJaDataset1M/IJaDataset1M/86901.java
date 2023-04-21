package org.jabber.jabberbeans.sax;

import org.xml.sax.*;
import org.jabber.jabberbeans.Packet;
import org.jabber.jabberbeans.ConnectionBean;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Main (top level) handler given to a SAX-enabled parser.
 * 
 * @author  David Waite <a href="mailto:dwaite@jabber.com">
 *                      <i>&lt;dwaite@jabber.com&gt;</i></a>
 * @author  $Author: mass $
 * @version $Revision: 1.8 $ 
 */
public class XMLStreamDocumentHandler extends SubHandler {

    /** instantiating handler, used for sending messages back up
     * the pipe. */
    protected ConnectionBean.InputStreamInterface isi;

    /**
     * Creates a new <code>XMLStreamDocumentHandler</code> instance.
     */
    public XMLStreamDocumentHandler() {
        super();
        try {
            setHandlerFactory(new HandlerFactory());
        } catch (IOException e) {
            throw new RuntimeException("error with extension factory");
        }
    }

    /**
     * set the InputStreamInterface, used to inform client code of new 
     * received data.
     *
     * @param isi ConnectionBean.InputStreamInterface
     */
    public final void setPacketHandler(ConnectionBean.InputStreamInterface isi) {
        this.isi = isi;
    }

    /**
     * handle the start of an element, including finding an appropriate
     * handler for the element or namespace type
     *
     * @param name element name
     * @param attributes element attributes
     * @throws SAXException unknown root element, or XML parsing error
     */
    public void handleStartElement(String name, AttributeList attributes) throws SAXException {
        SubHandler subhandler = getHandlerFactory().getHandlerInstance(name, attributes);
        if (subhandler == null) throw new SAXException("Unknown/invalid root element: " + name);
        setChildSubHandler(subhandler, name, attributes);
    }

    /**
     * <code>handleEndElement</code> method catches </stream:stream> and calls
     * disconnect.
     *
     * @param name a <code>String</code> value
     * @exception SAXException if an error occurs
     */
    public final void handleEndElement(String name) throws SAXException {
    }

    /**
     * <code>receiveChildData</code> receives packets constructed by the
     * subordinate handlers, and shuttles it to the protocol handler
     *
     * @param subHandler a <code>SubHandler</code> value of the handler the
     * data came from, unused
     * @param p an <code>Object</code> value, a Packet
     * @throws SAXException if the data is invalid (i.e. null)
     */
    public void receiveChildData(SubHandler subHandler, Object p) throws SAXException {
        if (p == null) throw new SAXException("null data returned from child");
        isi.received((Packet) p);
    }

    /**
     * <code>stopHandler</code> returns null since there is no 'data'
     * returned which was not already sent via the 'receive' method.
     *
     * @return an <code>Object</code> value, unused.
     * @exception SAXException if an error occurs
     */
    public final Object stopHandler(String name) throws SAXException {
        return null;
    }
}
