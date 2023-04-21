package jshomeorg.simplytrain.xml;

import org.xml.sax.*;

/**
 * 
 * The class reads XML documents according to specified DTD and 
 * translates all related events into GamestoreHandler events.
 * <p>Usage sample:
 * <pre>
 *    GamestoreParser parser = new GamestoreParser(...);
 *    parser.parse(new InputSource("..."));
 * </pre>
 * <p><b>Warning:</b> the class is machine generated. DO NOT MODIFY</p>
 */
public class GamestoreParser implements ContentHandler {

    private java.lang.StringBuffer buffer;

    private GamestoreHandler handler;

    private java.util.Stack context;

    private EntityResolver resolver;

    /**
	 *
	 * Creates a parser instance.
	 * @param handler handler interface implementation (never <code>null</code>
	 * @param resolver SAX entity resolver implementation or <code>null</code>.
	 * It is recommended that it could be able to resolve at least the DTD.
	 */
    public GamestoreParser(final GamestoreHandler handler, final EntityResolver resolver) {
        this.handler = handler;
        this.resolver = resolver;
        buffer = new StringBuffer(111);
        context = new java.util.Stack();
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void setDocumentLocator(Locator locator) {
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void startDocument() throws SAXException {
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void endDocument() throws SAXException {
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void startElement(java.lang.String ns, java.lang.String name, java.lang.String qname, Attributes attrs) throws SAXException {
        dispatch(true);
        context.push(new Object[] { qname, new org.xml.sax.helpers.AttributesImpl(attrs) });
        if ("connect".equals(qname)) {
            handler.handle_connect(attrs);
        } else if ("data".equals(qname)) {
            handler.handle_data(attrs);
        } else if ("pathelement".equals(qname)) {
            handler.handle_pathelement(attrs);
        } else if ("epart".equals(qname)) {
            handler.handle_epart(attrs);
        } else if ("tpart".equals(qname)) {
            handler.start_tpart(attrs);
        } else if ("time".equals(qname)) {
            handler.handle_time(attrs);
        } else if ("trackobject".equals(qname)) {
            handler.start_trackobject(attrs);
        } else if ("pathrequest".equals(qname)) {
            handler.start_pathrequest(attrs);
        } else if ("path".equals(qname)) {
            handler.start_path(attrs);
        } else if ("command".equals(qname)) {
            handler.start_command(attrs);
        } else if ("train".equals(qname)) {
            handler.start_train(attrs);
        } else if ("frequence".equals(qname)) {
            handler.handle_frequence(attrs);
        } else if ("trains".equals(qname)) {
            handler.start_trains(attrs);
        } else if ("savegame".equals(qname)) {
            handler.start_savegame(attrs);
        } else if ("emit".equals(qname)) {
            handler.start_emit(attrs);
        } else if ("track".equals(qname)) {
            handler.start_track(attrs);
        } else if ("tracks".equals(qname)) {
            handler.start_tracks(attrs);
        } else if ("pathroute".equals(qname)) {
            handler.start_pathroute(attrs);
        } else if ("signaldata".equals(qname)) {
            handler.start_signaldata(attrs);
        } else if ("pathqueue".equals(qname)) {
            handler.start_pathqueue(attrs);
        } else if ("mutex".equals(qname)) {
            handler.handle_mutex(attrs);
        } else if ("point".equals(qname)) {
            handler.handle_point(attrs);
        } else if ("startqueue".equals(qname)) {
            handler.start_startqueue(attrs);
        } else if ("routes".equals(qname)) {
            handler.start_routes(attrs);
        } else if ("route".equals(qname)) {
            handler.start_route(attrs);
        } else if ("requestedpath".equals(qname)) {
            handler.handle_requestedpath(attrs);
        } else if ("trainemitter".equals(qname)) {
            handler.start_trainemitter(attrs);
        }
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void endElement(java.lang.String ns, java.lang.String name, java.lang.String qname) throws SAXException {
        dispatch(false);
        context.pop();
        if ("tpart".equals(qname)) {
            handler.end_tpart();
        } else if ("trackobject".equals(qname)) {
            handler.end_trackobject();
        } else if ("pathrequest".equals(qname)) {
            handler.end_pathrequest();
        } else if ("path".equals(qname)) {
            handler.end_path();
        } else if ("command".equals(qname)) {
            handler.end_command();
        } else if ("train".equals(qname)) {
            handler.end_train();
        } else if ("trains".equals(qname)) {
            handler.end_trains();
        } else if ("savegame".equals(qname)) {
            handler.end_savegame();
        } else if ("emit".equals(qname)) {
            handler.end_emit();
        } else if ("track".equals(qname)) {
            handler.end_track();
        } else if ("tracks".equals(qname)) {
            handler.end_tracks();
        } else if ("pathroute".equals(qname)) {
            handler.end_pathroute();
        } else if ("signaldata".equals(qname)) {
            handler.end_signaldata();
        } else if ("pathqueue".equals(qname)) {
            handler.end_pathqueue();
        } else if ("startqueue".equals(qname)) {
            handler.end_startqueue();
        } else if ("routes".equals(qname)) {
            handler.end_routes();
        } else if ("route".equals(qname)) {
            handler.end_route();
        } else if ("trainemitter".equals(qname)) {
            handler.end_trainemitter();
        }
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void characters(char[] chars, int start, int len) throws SAXException {
        buffer.append(chars, start, len);
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void ignorableWhitespace(char[] chars, int start, int len) throws SAXException {
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void processingInstruction(java.lang.String target, java.lang.String data) throws SAXException {
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void startPrefixMapping(final java.lang.String prefix, final java.lang.String uri) throws SAXException {
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void endPrefixMapping(final java.lang.String prefix) throws SAXException {
    }

    /**
	 *
	 * This SAX interface method is implemented by the parser.
	 */
    public final void skippedEntity(java.lang.String name) throws SAXException {
    }

    private void dispatch(final boolean fireOnlyIfMixed) throws SAXException {
        if (fireOnlyIfMixed && buffer.length() == 0) {
            return;
        }
        Object[] ctx = (Object[]) context.peek();
        String here = (String) ctx[0];
        Attributes attrs = (Attributes) ctx[1];
        if ("tdata".equals(here)) {
            if (fireOnlyIfMixed) {
                throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
            }
            handler.handle_tdata(buffer.length() == 0 ? null : buffer.toString(), attrs);
        } else {
        }
        buffer.delete(0, buffer.length());
    }

    /**
	 *
	 * The recognizer entry method taking an InputSource.
	 * @param input InputSource to be parsed.
	 * @throws java.io.IOException on I/O error.
	 * @throws SAXException propagated exception thrown by a DocumentHandler.
	 * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
	 * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
	 */
    public void parse(final InputSource input) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, this);
    }

    /**
	 *
	 * The recognizer entry method taking a URL.
	 * @param url URL source to be parsed.
	 * @throws java.io.IOException on I/O error.
	 * @throws SAXException propagated exception thrown by a DocumentHandler.
	 * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
	 * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
	 */
    public void parse(final java.net.URL url) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), this);
    }

    /**
	 *
	 * The recognizer entry method taking an Inputsource.
	 * @param input InputSource to be parsed.
	 * @throws java.io.IOException on I/O error.
	 * @throws SAXException propagated exception thrown by a DocumentHandler.
	 * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
	 * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
	 */
    public static void parse(final InputSource input, final GamestoreHandler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, new GamestoreParser(handler, null));
    }

    /**
	 *
	 * The recognizer entry method taking a URL.
	 * @param url URL source to be parsed.
	 * @throws java.io.IOException on I/O error.
	 * @throws SAXException propagated exception thrown by a DocumentHandler.
	 * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
	 * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
	 */
    public static void parse(final java.net.URL url, final GamestoreHandler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), handler);
    }

    private static void parse(final InputSource input, final GamestoreParser recognizer) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        XMLReader parser = factory.newSAXParser().getXMLReader();
        parser.setContentHandler(recognizer);
        parser.setErrorHandler(recognizer.getDefaultErrorHandler());
        if (recognizer.resolver != null) {
            parser.setEntityResolver(recognizer.resolver);
        }
        parser.parse(input);
    }

    /**
	 *
	 * Creates default error handler used by this parser.
	 * @return org.xml.sax.ErrorHandler implementation
	 */
    protected ErrorHandler getDefaultErrorHandler() {
        return new ErrorHandler() {

            public void error(SAXParseException ex) throws SAXException {
                if (context.isEmpty()) {
                    System.err.println("Missing DOCTYPE.");
                }
                throw ex;
            }

            public void fatalError(SAXParseException ex) throws SAXException {
                throw ex;
            }

            public void warning(SAXParseException ex) throws SAXException {
            }
        };
    }
}
