package net.sourceforge.vigilog.parse;

import net.sourceforge.vigilog.model.LocationInfo;
import net.sourceforge.vigilog.model.LogEntry;
import net.sourceforge.vigilog.model.LogLevel;
import net.sourceforge.vigilog.utils.xml.XMLUtil;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for java utils logging
 * Info: http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html
 */
public class JavaLoggingXMLFileLogFileParser extends AbstractLogFileParser {

    private static Logger log = Logger.getLogger(JavaLoggingXMLFileLogFileParser.class);

    private static final String JAVA_LOGGING_LEVEL_CONFIG = "CONFIG";

    private static final String JAVA_LOGGING_LEVEL_WARNING = "WARNING";

    private static final String JAVA_LOGGING_LEVEL_SEVERE = "SEVERE";

    private DocumentBuilder documentBuilder;

    public JavaLoggingXMLFileLogFileParser() throws LogFileParserException {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new LoggerDTDEntityResolver());
        } catch (ParserConfigurationException e) {
            throw new LogFileParserException(e);
        }
    }

    public List<LogEntry> parse(File logFile) throws LogFileParserException {
        try {
            List<LogEntry> result = new ArrayList<LogEntry>();
            Document document = convertToXMLDocument(logFile);
            NodeList eventsNodeList = XMLUtil.getNodeList("log/record", document);
            int nrOfEvents = eventsNodeList.getLength();
            String fileName = logFile.getName();
            if (nrOfEvents == 0) {
                throw new LogFileParserException("There are not logging events in the file: " + fileName);
            }
            log.debug("nr of log entries: " + nrOfEvents);
            for (int i = 0; i < nrOfEvents; i++) {
                int progress = (int) ((double) i / (double) nrOfEvents * 100.0);
                setProgress(progress);
                Node eventNode = eventsNodeList.item(i);
                String id = XMLUtil.readXMLProperty("sequence", eventNode);
                String message = XMLUtil.readXMLProperty("message", eventNode);
                String throwable = XMLUtil.readXMLProperty("exception", eventNode);
                String logger = XMLUtil.readXMLProperty("logger", eventNode);
                String timestamp = XMLUtil.readXMLProperty("millis", eventNode);
                String level = XMLUtil.readXMLProperty("level", eventNode);
                String thread = XMLUtil.readXMLProperty("thread", eventNode);
                String className = XMLUtil.readXMLProperty("class", eventNode);
                String methodName = XMLUtil.readXMLProperty("method", eventNode);
                LogEntry entry = new LogEntry();
                entry.setId(parseId(id));
                entry.setMessage(message);
                entry.setLogger(logger);
                entry.setTimestamp(Long.parseLong(timestamp));
                entry.setLevel(getLogLevel(level));
                entry.setThread(thread);
                entry.setThrowable(throwable);
                entry.setFileName(fileName);
                entry.setLocationInfo(new LocationInfo(className, methodName));
                result.add(entry);
            }
            return result;
        } catch (IOException e) {
            throw new LogFileParserException(e);
        } catch (SAXException e) {
            throw new LogFileParserException(e);
        } catch (XPathExpressionException e) {
            throw new LogFileParserException(e);
        }
    }

    private int parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Document convertToXMLDocument(File logFile) throws IOException, SAXException {
        return documentBuilder.parse(logFile);
    }

    /**
	 * Maps the levels from java logging to the ones from log4j
	 * CONF is mapped to INFO
	 * FINE, FINER and FINEST are mapped to debug
	 * <p/>
	 * This should be improved later by making the actual levels available (and also adjust the level slider
	 * and the color filter to take that into account)
	 *
	 * @param level
	 * @return the LogLevel
	 */
    private LogLevel getLogLevel(String level) {
        LogLevel result;
        try {
            if (JAVA_LOGGING_LEVEL_CONFIG.equals(level)) {
                result = LogLevel.DEBUG;
            } else if (level.startsWith("FINE")) {
                result = LogLevel.TRACE;
            } else if (JAVA_LOGGING_LEVEL_WARNING.equals(level)) {
                result = LogLevel.WARN;
            } else if (JAVA_LOGGING_LEVEL_SEVERE.equals(level)) {
                result = LogLevel.ERROR;
            } else {
                result = LogLevel.valueOf(level);
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return LogLevel.DEBUG;
        }
        return result;
    }

    private static class LoggerDTDEntityResolver implements EntityResolver {

        public InputSource resolveEntity(String publicId, String systemId) {
            InputSource result = null;
            if (systemId.endsWith("logger.dtd")) {
                result = new InputSource(getClass().getResourceAsStream("logger.dtd"));
            }
            return result;
        }
    }
}
