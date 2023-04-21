package net.iharder.xmlizable;

/**
 *
 *
 * <p>
 * I am placing this code in the Public Domain. Do with it as you will.
 * This software comes with no guarantees or warranties but with
 * plenty of well-wishing instead!
 * Please visit <a href="http://iharder.net/xmlizable">http://iharder.net/xmlizable</a>
 * periodically to check for updates or to contribute improvements.
 * </p>
 *
 * @author Robert Harder
 * @author rharder@usa.net
 * @version 1.2
 */
public class ObjectArrayContentsHandler extends ObjectHandler implements XmlConstants {

    private Object[] objArr;

    int nextIndex = 0;

    public ObjectArrayContentsHandler() {
        this(new Object[0]);
    }

    /**
     * Creates a {@link CollectionHandler} that will add data from the
     * SAX2 events into the passed {@link java.util.Collection}. With this
     * construct you do not need to call {@link #getCollection getCollection()} at the
     * end of the parsing.
     *
     * @param map The {@link java.util.Collection} to build
     * @since 1.2
     */
    public ObjectArrayContentsHandler(Object[] obj) {
        setObjectArray(obj);
    }

    /**
     *
     */
    public void setObjectArray(Object[] objArr) {
        setObject(this.objArr = objArr);
    }

    public Object[] getObjectArray() {
        return objArr;
    }

    public void startElement(final String namespaceURI, final String localName, final String qName, final org.xml.sax.Attributes atts) throws org.xml.sax.SAXException {
        if (getAltHandler() != null) {
            getAltHandlerElementStack().push(localName);
            getAltHandler().startElement(namespaceURI, localName, qName, atts);
        } else if (NAMESPACE.equals(namespaceURI)) {
            setAltHandler(new ObjectHandler());
            resetAltHandlerElementStack().push(localName);
            getAltHandler().startElement(namespaceURI, localName, qName, atts);
        } else {
            setAltHandler(new org.xml.sax.helpers.DefaultHandler());
            resetAltHandlerElementStack().push(localName);
            getAltHandler().startElement(namespaceURI, localName, qName, atts);
        }
    }

    public void endElement(final String namespaceURI, final String localName, final String qName) throws org.xml.sax.SAXException {
        if (getAltHandler() != null) {
            getAltHandlerElementStack().pop();
            getAltHandler().endElement(namespaceURI, localName, qName);
            if (getAltHandlerElementStack().isEmpty()) {
                if (getAltHandler() instanceof ObjectHandler) {
                    Object obj = ((ObjectHandler) getAltHandler()).getObject();
                    getObjectArray()[nextIndex++] = obj;
                }
                clearAltHandlerElementStack();
                clearAltHandler();
            }
        }
    }
}
