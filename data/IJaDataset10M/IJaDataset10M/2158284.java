package gov.nasa.luv;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.util.HashMap;
import static gov.nasa.luv.Constants.*;

/** 
 * The DispatchHandler class dispatches XML messages to the correct handler from 
 * the set of provided handlers in the handler map.
 */
public class DispatchHandler extends DefaultHandler {

    /** table of messages handlers */
    static HashMap<String, AbstractDispatchableHandler> handlerMap = new HashMap<String, AbstractDispatchableHandler>();

    /** The currently selected handler */
    AbstractDispatchableHandler currentHandler;

    /** 
       * Constructs a dispatch handler which operates on the provided
       * model.
       */
    public DispatchHandler() {
        registerHandler(PLAN_INFO, new PlanInfoHandler());
        registerHandler(NODE_STATE_UPDATE, new NodeStateUpdateHandler());
        registerHandler(ASSIGNMENT, new AssignmentHandler());
        AbstractDispatchableHandler planHandler = new PlexilPlanHandler();
        registerHandler(PLEXIL_PLAN, planHandler);
        registerHandler(PLEXIL_LIBRARY, planHandler);
    }

    /** 
       * Adds a new handler to the set of available handlers to dispatch.
       *
       * @param key the tag for which this handler is invoked
       * @param handler handler to add to the set available handers to dispatch.
       */
    public static void registerHandler(String key, AbstractDispatchableHandler handler) {
        handlerMap.put(key, handler);
    }

    /** Handles the start of a document event. */
    public void startDocument() {
        currentHandler = null;
    }

    /** 
       * Establishes the current handler then dispatch events to that handler. 
       */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (currentHandler == null) {
            currentHandler = handlerMap.get(localName);
            if (currentHandler == null) {
                Luv.getLuv().getStatusMessageHandler().displayErrorMessage(null, "ERROR: unhandled XML tag: <" + localName + ">");
                throw (new Error("ERROR: unhandled XML tag: <" + localName + ">."));
            }
            currentHandler.startDocument();
        }
        currentHandler.startElement(uri, localName, qName, attributes);
    }

    /** Dispatches the characters action. */
    @SuppressWarnings("static-access")
    public void characters(char[] ch, int start, int length) throws SAXException {
        String test = "";
        if (ch.length > 1) {
            if (start + length < ch.length) {
                test = test.valueOf(ch, start + length, 1);
                if (test.equals("&")) {
                    test = test.valueOf(ch, start + length, 6);
                    if (test.equals("&#x1d;") || test.equals("&#x1D;") || test.equals("&#x04;")) {
                        ch[start + length] = '&';
                        ch[start + length + 1] = 'a';
                        ch[start + length + 2] = 'm';
                        ch[start + length + 3] = 'p';
                        ch[start + length + 4] = ';';
                        ch[start + length + 5] = ch[start + length - 1];
                    } else {
                        test = test.valueOf(ch, start + length, 5);
                        if (test.equals("&#x4;") || test.equals("&#x0;")) {
                            ch[start + length] = '&';
                            ch[start + length + 1] = 'a';
                            ch[start + length + 2] = 'm';
                            ch[start + length + 3] = 'p';
                            ch[start + length + 4] = ';';
                        }
                    }
                }
            }
        } else if (ch[0] == '&') ch[0] = ' ';
        currentHandler.characters(ch, start, length);
    }

    /** Dispatches the endElement action. */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentHandler.endElement(uri, localName, qName);
    }

    /** Dispatches end of document event. */
    public void endDocument() throws SAXException {
        currentHandler.endDocument();
    }

    /** Gets the selected handler.
       *
       * @return the handler selected to parse last message
       */
    public AbstractDispatchableHandler getHandler() {
        return currentHandler;
    }
}
