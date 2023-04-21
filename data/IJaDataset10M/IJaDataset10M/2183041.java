package topchat.server.protocol.xmpp.stream.parser;

import java.util.Iterator;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import topchat.server.protocol.xmpp.stream.element.Constants;
import topchat.server.protocol.xmpp.stream.element.Features;
import topchat.server.protocol.xmpp.stream.element.IQStanza;
import topchat.server.protocol.xmpp.stream.element.PresenceStanza;
import topchat.server.protocol.xmpp.stream.element.MessageStanza;
import topchat.server.protocol.xmpp.stream.element.Query;
import topchat.server.protocol.xmpp.stream.element.StreamElement;
import topchat.server.protocol.xmpp.stream.element.XElement;
import topchat.server.protocol.xmpp.stream.element.XMPPAuth;
import topchat.server.protocol.xmpp.stream.element.XMPPStream;

/**
 * Contains methods used for parsing messages sent by the client 
 * 
 * Uses StaX API included in JAVA SE 6 in javax.xml.stream
 * 
 */
public class Parser implements Constants {

    private static Logger logger = Logger.getLogger(Parser.class);

    /**
	 * Entry point for parsing functions
	 * 
	 * @param msg
	 *            the string to be parsed
	 * @return a Vector of StreamElement data parsed from the string
	 * @throws if parsing fails
	 */
    public static Vector<StreamElement> parse(String msg) throws Exception {
        Vector<StreamElement> result = null;
        logger.debug(msg);
        boolean endOfStream = false;
        if (endOfStream = containsEndOfStream(msg)) {
            msg = cleanupEndOfStream(msg);
        }
        logger.debug("after cleanupEndOfStream: " + msg);
        msg = cleanupStreamPrefix(msg);
        logger.debug("after cleanupStreamPrefix: " + msg);
        if (!msg.startsWith("<?xml")) msg = wrapRootTag(msg);
        if (msg.trim().length() > 0) {
            result = genericParse(msg);
        }
        if (endOfStream) logger.info("End of stream reached");
        return result;
    }

    /**
	 * Generic parsing function
	 * 
	 * @param msg
	 *            the string to be parsed
	 * @return a Vector of StreamElement data parsed from the string
	 * @throws if parsing fails
	 */
    private static Vector<StreamElement> genericParse(String msg) throws Exception {
        Vector<StreamElement> resultElements = new Vector<StreamElement>();
        StreamElement result = null;
        XMLEventReader reader = Utils.createReader(msg);
        logger.debug("parsing : " + msg);
        while (reader.hasNext()) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                result = null;
                StartElement startElement = ((StartElement) event);
                logger.debug("start element: " + startElement.toString());
                logger.debug("local name: " + startElement.getName().getLocalPart().toString());
                String local = startElement.getName().getLocalPart().toString();
                if ("message".equals(local)) {
                    result = parseMessage(startElement, reader);
                    resultElements.add(result);
                }
                if ("iq".equals(local)) {
                    result = parseIq(startElement, reader);
                    resultElements.add(result);
                }
                if ("presence".equals(local)) {
                    result = parsePresence(startElement, reader);
                    resultElements.add(result);
                }
                if ("error".equals(local)) parseError(startElement, reader);
                if ("features".equals(local)) {
                    result = parseFeatures(startElement, reader);
                    resultElements.add(result);
                }
                if ("starttls".equals(local)) {
                    result = parseStartTLS(startElement, reader);
                    resultElements.add(result);
                }
                if ("proceed".equals(local)) parseProceed(startElement, reader);
                if ("failure".equals(local)) parseFailure(startElement, reader);
                if ("auth".equals(local)) {
                    result = parseAuth(startElement, reader);
                    resultElements.add(result);
                }
                if ("stream".equals(local)) {
                    result = parseStreamStart(startElement, reader);
                    resultElements.add(result);
                    break;
                }
            } else {
                logger.debug(event.toString());
            }
        }
        return resultElements;
    }

    /**
	 * Method that parses a message stanza
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    @SuppressWarnings("unchecked")
    private static MessageStanza parseMessage(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        MessageStanza messageStanza = new MessageStanza();
        Iterator<Attribute> it = start.getAttributes();
        while (it.hasNext()) {
            Attribute attrib = it.next();
            messageStanza.addAttribute(attrib.getName().getLocalPart(), attrib.getValue());
        }
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
                if ("body".equals(startElement.getName().getLocalPart())) {
                    event = reader.nextEvent();
                    if (event.isCharacters()) {
                        logger.debug("chars " + event.toString());
                        messageStanza.addData("body", event.toString());
                    }
                }
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("message".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
        return messageStanza;
    }

    /**
	 * Method that parses an Iq stanza
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    @SuppressWarnings("unchecked")
    private static IQStanza parseIq(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        IQStanza iqStanza = new IQStanza();
        Iterator<Attribute> it = start.getAttributes();
        while (it.hasNext()) {
            Attribute attrib = it.next();
            iqStanza.addAttribute(attrib.getName().getLocalPart(), attrib.getValue());
        }
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
                if ("resource".equals(startElement.getName().getLocalPart())) {
                    event = reader.nextEvent();
                    if (event.isCharacters()) {
                        logger.debug("chars " + event.toString());
                        iqStanza.addData("resource", event.toString());
                    }
                }
                if ("query".equals(startElement.getName().getLocalPart())) {
                    Query query = parseQuery(startElement, reader);
                    iqStanza.addQuery(query);
                }
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("iq".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
        return iqStanza;
    }

    /**
	 * Method that parses a presence stanza
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    @SuppressWarnings("unchecked")
    private static PresenceStanza parsePresence(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        PresenceStanza presenceStanza = new PresenceStanza();
        Iterator<Attribute> it = start.getAttributes();
        while (it.hasNext()) {
            Attribute attrib = it.next();
            presenceStanza.addAttribute(attrib.getName().getLocalPart(), attrib.getValue());
        }
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
                if ("x".equals(startElement.getName().getLocalPart())) {
                    XElement xElement = parseXElement(startElement, reader);
                    presenceStanza.addXElement(xElement);
                }
                if ("status".equals(startElement.getName().getLocalPart())) {
                    event = reader.nextEvent();
                    if (event.isCharacters()) {
                        logger.debug("chars " + event.toString());
                        presenceStanza.addData("status", event.toString());
                    }
                }
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("presence".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
        return presenceStanza;
    }

    /**
	 * Method that parses a query
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    @SuppressWarnings("unchecked")
    private static Query parseQuery(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        Query query = new Query();
        Iterator<Namespace> itN = start.getNamespaces();
        while (itN.hasNext()) {
            Namespace namespace = itN.next();
            if ("".equals(namespace.getName().getLocalPart())) query.addNamespace(namespace.getValue()); else logger.debug("Namespace " + namespace.getName().getLocalPart() + " unsuppoted.");
        }
        Iterator<Attribute> it = start.getAttributes();
        while (it.hasNext()) {
            Attribute attrib = it.next();
            query.addAttribute(attrib.getName().getLocalPart(), attrib.getValue());
        }
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("query".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
        return query;
    }

    /**
	 * Method that parses an XElement
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    @SuppressWarnings("unchecked")
    private static XElement parseXElement(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        XElement xElement = new XElement();
        Iterator<Namespace> itN = start.getNamespaces();
        while (itN.hasNext()) {
            Namespace namespace = itN.next();
            if ("".equals(namespace.getName().getLocalPart())) xElement.addNamespace(namespace.getValue()); else logger.debug("Namespace " + namespace.getName().getLocalPart() + " unsuppoted.");
        }
        Iterator<Attribute> it = start.getAttributes();
        while (it.hasNext()) {
            Attribute attrib = it.next();
            xElement.addAttribute(attrib.getName().getLocalPart(), attrib.getValue());
        }
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("x".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
        return xElement;
    }

    /**
	 * Method that parses an error message
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    private static void parseError(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("error".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
    }

    /**
	 * Method that parses a features stanza
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    private static Features parseFeatures(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        boolean usesTLS = false;
        boolean usesSASL = false;
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
                if ("starttls".equals(startElement.getName().getLocalPart())) usesTLS = true;
                if ("mechanisms".equals(startElement.getName().getLocalPart())) usesSASL = true;
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("features".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
        return new Features(usesTLS, usesSASL);
    }

    /**
	 * Method that parses a starttls
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    private static StreamElement parseStartTLS(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("starttls".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
        return new StreamElement(STARTTLS_TYPE);
    }

    /**
	 * Method that parses proceed
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    private static void parseProceed(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("proceed".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
    }

    /**
	 * Method that parses failure
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    private static void parseFailure(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("failure".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else {
                logger.debug(event.toString());
            }
        }
    }

    /**
	 * Method that parses auth
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    private static XMPPAuth parseAuth(StartElement start, XMLEventReader reader) throws Exception {
        boolean end = false;
        String mechanism = start.getAttributeByName(new QName("mechanism")).getValue().toString();
        String initialChallenge = null;
        logger.debug("auth mechanism " + mechanism);
        while (reader.hasNext() && !end) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
            } catch (XMLStreamException e) {
                logger.fatal("Exception when reading next event", e);
                throw new Exception("Exception on parsing");
            }
            if (event.isStartElement()) {
                StartElement startElement = ((StartElement) event);
                logger.debug("start: " + startElement.toString());
            } else if (event.isEndElement()) {
                EndElement endElement = (EndElement) event;
                if ("failure".equals(endElement.getName().getLocalPart().toString())) end = true;
                logger.debug(event.toString());
            } else if (event.isCharacters()) {
                initialChallenge = event.toString();
                logger.debug("other: " + event.toString());
            }
        }
        return new XMPPAuth(mechanism, initialChallenge);
    }

    /**
	 * Parse the message sent by the client to initiate the communication
	 * 
	 * @param start
	 *            the StartElement of the StreamElement
	 * @param reader
	 *            the XMLEventReader used for reading the rest of the
	 *            StreamElement
	 * @throws if the parsing fails
	 */
    @SuppressWarnings("unchecked")
    private static XMPPStream parseStreamStart(StartElement start, XMLEventReader reader) throws Exception {
        String to = null;
        String version = null;
        String id = null;
        String lang = null;
        String from = null;
        for (Iterator<Attribute> it = start.getAttributes(); it.hasNext(); ) {
            Attribute attribute = it.next();
            logger.debug("attribute name: " + attribute.getName());
            logger.debug("attribute value: " + attribute.getValue());
            if ("to".equals(attribute.getName().toString())) {
                to = attribute.getValue();
                logger.debug("set to " + to);
            }
            if ("version".equals(attribute.getName().toString())) {
                version = attribute.getValue();
                logger.debug("set version " + version);
            }
            if ("id".equals(attribute.getName().toString())) {
                id = attribute.getValue();
                logger.debug("set id " + id);
            }
            if ("from".equals(attribute.getName().toString())) {
                from = attribute.getValue();
                logger.debug("set from " + from);
            }
        }
        return new XMPPStream(to, from, id, lang, version);
    }

    /**
	 * Method returning true if the message contains end of stream info
	 * 
	 * @param msg
	 *            the message string
	 * @return true if the message contains end of stream
	 */
    private static boolean containsEndOfStream(String msg) {
        return msg.contains("</stream:stream>");
    }

    /**
	 * Method that removes the stream: prefix
	 * 
	 * @param msg
	 *            the string to be processed
	 * @return the clean string
	 */
    private static String cleanupStreamPrefix(String msg) {
        if (msg.contains("xmlns:stream")) return msg;
        return msg.replace("stream:", "");
    }

    /**
	 * Method that removes the end of stream info
	 * 
	 * @param msg
	 *            the string to be processed
	 * @return the clean string
	 */
    private static String cleanupEndOfStream(String msg) {
        return msg.replace("</stream:stream>", "");
    }

    /**
	 * Method that wraps a message in a root tag to support multiple stanzas in
	 * one processing
	 * 
	 * @param msg
	 *            the string to be processed
	 * @return the clean string
	 */
    private static String wrapRootTag(String msg) {
        return "<root>".concat(msg).concat("</root>");
    }
}
