package net.sourceforge.jlogutil.jsr47.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author klein
 */
public class LoggerContentHandler extends DefaultHandler {

    private static final Logger LOGGER = Logger.getLogger(LoggerContentHandler.class.getName());

    public static final String LOGGER_DTD = "/java/util/logging/logger.dtd";

    private String elementName;

    private ArrayList<LogRecord> recordList = new ArrayList<LogRecord>();

    private boolean recordParsing = false;

    private boolean exceptionParsing = false;

    private boolean frameParsing = false;

    private ArrayList<StackTraceElement> frames = new ArrayList<StackTraceElement>();

    private ArrayList<Object> params = new ArrayList<Object>();

    private LogRecord record;

    private String thrownMessage = null;

    private String traceClass = null;

    private String traceMethod = null;

    private int traceLine = -1;

    public LoggerContentHandler() {
    }

    public List<LogRecord> getRecordList() {
        return recordList;
    }

    /**
     * <p>This reports the occurance of an actual element.  It includes the
     * element's attributes, such as <code>smlns:[namespace prefix]</code>
     * and <code>xsi:schemaLocation</code>.</p>
     * 
     * @param uri <code>String</code> namespace URI this element is associated
     *            with, or an empty <code>String</code>.
     * @param localName <code>String</code> name of element (with no namespace
     *                  prefix, if one is present).
     * @param qName <code>String</code> XML 1.0 version of element name:
     *              [namespace prefix]:[localName]
     * @param atts <code>Attributes</code> list for this element.
     * @throws <code>org.xml.sax.SAXException</code> when things go wrong.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if ((localName == null) || (localName.isEmpty())) {
            elementName = qName;
        } else {
            elementName = localName;
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("startElement:  ");
            sb.append(elementName);
            if (!uri.equals("")) {
                sb.append("    in namespace ");
                sb.append(uri);
                sb.append("(");
                sb.append(qName);
                sb.append(")");
                sb.append('\n');
            } else {
                sb.append("    has no associate namespace uri:");
                sb.append(uri);
                sb.append(" qName:");
                sb.append(qName);
                sb.append('\n');
            }
            for (int i = 0; i < atts.getLength(); i++) {
                sb.append("\tAttribute:  ");
                sb.append(atts.getLocalName(i));
                sb.append(" = ");
                sb.append(atts.getValue(i));
                sb.append('\n');
            }
            LOGGER.fine(sb.toString());
        }
        if (elementName.equalsIgnoreCase("record")) {
            record = new LogRecord(Level.ALL, "Default");
            recordParsing = true;
            exceptionParsing = false;
            frameParsing = false;
        } else if (elementName.equalsIgnoreCase("exception")) {
            recordParsing = false;
            exceptionParsing = true;
            frameParsing = false;
        } else if (elementName.equalsIgnoreCase("frame")) {
            recordParsing = false;
            exceptionParsing = false;
            frameParsing = true;
        }
    }

    /**
     * <p>Indicates the end of an element (<code>&lt;/[element name]&gt;</code>)
     * is reached.  Not that the parser does not distinguish between empty
     * elements and non-empty elements, so this occurs uniformly.</p>
     * 
     * @param uri <code>String</code> URI of namespace this element is 
     *            associated with.
     * @param localName <code>String</code> name of element without prefix.
     * @param qName <code>String</code> name of element in XML 1.0 form.
     * @throws <code>org.xml.sax.SAXException</code> when things go wrong.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ((localName == null) || (localName.isEmpty())) {
            elementName = qName;
        } else {
            elementName = localName;
        }
        if (elementName.equalsIgnoreCase("record")) {
            record.setParameters(params.toArray());
            recordList.add(record);
            record = null;
            recordParsing = false;
            exceptionParsing = false;
            frameParsing = false;
        } else if (elementName.equalsIgnoreCase("exception")) {
            Throwable thrown = new Throwable(thrownMessage);
            StackTraceElement[] elementArray = new StackTraceElement[0];
            thrown.setStackTrace(frames.toArray(elementArray));
            record.setThrown(thrown);
            frames.clear();
            thrownMessage = null;
            recordParsing = true;
            exceptionParsing = false;
            frameParsing = false;
        } else if (elementName.equalsIgnoreCase("frame")) {
            StackTraceElement traceElement = null;
            if (traceLine >= 0) {
                traceElement = new StackTraceElement(traceClass, traceMethod, traceClass, traceLine);
            } else {
                traceElement = new StackTraceElement(traceClass, traceMethod, null, traceLine);
            }
            frames.add(traceElement);
            recordParsing = false;
            exceptionParsing = true;
            frameParsing = false;
            traceClass = null;
            traceMethod = null;
            traceLine = -1;
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("endElement:  ");
            sb.append(elementName);
            LOGGER.fine(sb.toString());
        }
    }

    /**
     * <p>This reports character data (within an element).</p>
     * 
     * @param ch <code>char[]</code> character array with character data.
     * @param start <code>int</code> index in array where data starts.
     * @param length <code>int</code> index in array where data ends.
     * @throws <code>org.xml.sax.SAXException</code> when things go wrong.
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if (recordParsing) {
            handleRecord(s);
        } else if (exceptionParsing) {
            handleException(s);
        } else if (frameParsing) {
            handleFrame(s);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("characters:  ");
            sb.append(s);
            LOGGER.fine(sb.toString());
        }
    }

    private void handleRecord(String s) throws SAXException {
        if (this.elementName.equalsIgnoreCase("millis")) {
            long millis = 0;
            if (s != null) {
                millis = Long.valueOf(s);
            }
            record.setMillis(millis);
        } else if (elementName.equalsIgnoreCase("sequence")) {
            long sequence = 0;
            if (s != null) {
                sequence = Long.valueOf(s);
            }
            record.setSequenceNumber(sequence);
        } else if (elementName.equalsIgnoreCase("logger")) {
            record.setLoggerName(s);
        } else if (elementName.equalsIgnoreCase("level")) {
            record.setLevel(Level.parse(s));
        } else if (elementName.equalsIgnoreCase("class")) {
            record.setSourceClassName(s);
        } else if (elementName.equalsIgnoreCase("method")) {
            record.setSourceMethodName(s);
        } else if (elementName.equalsIgnoreCase("thread")) {
            int threadId = 0;
            if (s != null) {
                threadId = Integer.valueOf(s);
            }
            record.setThreadID(threadId);
        } else if (elementName.equalsIgnoreCase("message")) {
            record.setMessage(s);
        } else if (elementName.equalsIgnoreCase("key")) {
        } else if (elementName.equalsIgnoreCase("catalog")) {
            record.setResourceBundleName(s);
        } else if (elementName.equalsIgnoreCase("param")) {
            params.add(s);
        }
    }

    private void handleException(String s) throws SAXException {
        if (elementName.equalsIgnoreCase("message")) {
            thrownMessage = s;
        }
    }

    private void handleFrame(String s) throws SAXException {
        if (elementName.equalsIgnoreCase("class")) {
            traceClass = s;
        } else if (elementName.equalsIgnoreCase("method")) {
            traceMethod = s;
        } else if (elementName.equalsIgnoreCase("line")) {
            traceLine = -1;
            if (s != null) {
                traceLine = Integer.valueOf(s);
            }
        }
    }

    @Override
    public InputSource resolveEntity(String publicID, String systemID) throws SAXException, IOException {
        InputSource source = null;
        if (lastIndexOf(systemID, "logger.dtd") != -1) {
            InputStream is = LoggerContentHandler.class.getResourceAsStream(LOGGER_DTD);
            source = new InputSource(is);
            if (LOGGER.isLoggable(Level.FINE)) {
                StringBuilder sb = new StringBuilder();
                sb.append("resolveEntity  publicID: ");
                sb.append(publicID);
                sb.append(" systemID: ");
                sb.append(systemID);
                LOGGER.fine(sb.toString());
            }
        } else {
            source = super.resolveEntity(publicID, systemID);
        }
        return source;
    }

    private int lastIndexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr);
    }

    /**
     * <p>This indicates the start of a Document parse &mdash; this precedes
     * all callbacks in all SAX Handlers with the sole exception of 
     * <code>{@link #setDocumentLocator}</code>.</p>
     * 
     * @throws <code>org.xml.sax.SAXException</code> when things go wrong.
     */
    @Override
    public void startDocument() throws SAXException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Parsing begins...");
        }
    }

    /**
     * <p>This indicates the end of a Document parse &mdash; this occurs after
     * all callbacks in all SAX Handlers.</p>
     * 
     * @throws <code>org.xml.sax.SAXException</code> when things go wrong.
     */
    @Override
    public void endDocument() throws SAXException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("...Parsing ends.");
        }
    }
}
