package de.searchworkorange.indexcrawler.remoteCommandServ;

import de.searchworkorange.indexcrawler.Command;
import de.searchworkorange.indexcrawler.Queue;
import java.net.MalformedURLException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import jcifs.smb.SmbFile;
import org.apache.log4j.Level;
import org.xml.sax.SAXException;
import de.searchworkorange.indexcrawler.configuration.ConfigurationCollection;
import de.searchworkorange.lib.configuration.ConfigFileParameterException;
import de.searchworkorange.lib.logger.LoggerCollection;
import de.searchworkorange.indexcrawler.remoteCommandServ.exception.NoRawMsgQueueException;
import de.searchworkorange.indexcrawler.remoteCommandServ.exception.NoXSDFileFoundException;
import de.searchworkorange.indexcrawler.remoteCommandServ.exception.SambaMsgServerUrlException;
import de.searchworkorange.indexcrawler.remoteCommandServ.exception.UnknownTransportProtocolException;
import de.searchworkorange.lib.LocalFileTransportProtocol;
import de.searchworkorange.lib.MyTransportProtocol;
import de.searchworkorange.lib.SmbTransportProtocol;
import de.searchworkorange.lib.acl.AclDirectoryStorage;
import de.searchworkorange.lib.netbios.NetbiosNameConverter;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class ReceivedMsgQueue {

    private static final boolean CLASSDEBUG = false;

    private ConfigurationCollection config = null;

    private LoggerCollection loggerCol = null;

    private SQLFileDeleter sqlFileDeleter = null;

    private StringBuffer rawMsgQueue = null;

    private String transactionNumber = null;

    private String receivedTransactioNumber = null;

    private String lastXMLStartElement = null;

    private Queue commandQueue = null;

    private MyTransportProtocol protocol = null;

    private NetbiosNameConverter nbtConverter = null;

    private AclDirectoryStorage aclDirectoryStorage = null;

    /**
     * 
     * @param loggerCol
     * @param config
     * @throws ConfigFileParameterException
     */
    public ReceivedMsgQueue(LoggerCollection loggerCol, ConfigurationCollection config, NetbiosNameConverter nbtConverter, AclDirectoryStorage aclDirectoryStorage, SQLFileDeleter sqlFileDeleter) throws ConfigFileParameterException {
        if (config == null || loggerCol == null || nbtConverter == null || aclDirectoryStorage == null || sqlFileDeleter == null) {
            throw new IllegalArgumentException();
        } else {
            this.config = config;
            this.loggerCol = loggerCol;
            this.nbtConverter = nbtConverter;
            this.aclDirectoryStorage = aclDirectoryStorage;
            this.sqlFileDeleter = sqlFileDeleter;
            commandQueue = new Queue(loggerCol);
        }
    }

    public void resetTransactionNumber() {
        transactionNumber = null;
    }

    /**
     * 
     * @param rawMsgQueue
     */
    public void setRawMsgQueue(StringBuffer rawMsgQueue) {
        if (rawMsgQueue == null) {
            throw new IllegalArgumentException();
        } else {
            this.rawMsgQueue = rawMsgQueue;
        }
    }

    /**
     *
     * @return StringBuffer
     */
    public StringBuffer getRawMsgQueue() {
        return rawMsgQueue;
    }

    /**
     *
     * @return boolean
     * @throws NoXSDFileFoundException
     */
    public boolean checkMsqQueue() throws NoXSDFileFoundException {
        boolean result = false;
        modifyQueue();
        return validateXML(rawMsgQueue.toString());
    }

    /**
     *
     * @return String
     */
    public String getLastXMLStartElement() {
        return lastXMLStartElement;
    }

    /**
     *
     * @return String
     */
    public String getReceivedTransactioNumber() {
        return receivedTransactioNumber;
    }

    /**
     *
     * @return String
     * @throws NoRawMsgQueueException
     */
    public String getTransactionNumber() throws NoRawMsgQueueException {
        if (transactionNumber == null) {
            try {
                if (rawMsgQueue != null) {
                    parseXMLQueue(true, rawMsgQueue.toString());
                } else {
                    throw new NoRawMsgQueueException();
                }
            } catch (UnknownTransportProtocolException ex) {
                loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
            } catch (SambaMsgServerUrlException ex) {
                loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
            }
        }
        return transactionNumber;
    }

    /**
     * 
     * @param getOnlyTransactionNumber
     * @param xmlToParse
     * @throws SambaMsgServerUrlException
     */
    public void parseXMLQueue(boolean getOnlyTransactionNumber, String xmlToParse) throws SambaMsgServerUrlException, UnknownTransportProtocolException {
        loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "xmlToParse:" + xmlToParse);
        StringReader xmlInput = new StringReader(xmlToParse);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader parser;
        try {
            parser = factory.createXMLEventReader(xmlInput);
            while (parser.hasNext()) {
                XMLEvent event = parser.nextEvent();
                switch(event.getEventType()) {
                    case XMLStreamConstants.START_DOCUMENT:
                        loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "START_DOCUMENT:");
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "END_DOCUMENT:");
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement element = event.asStartElement();
                        lastXMLStartElement = element.getName().toString();
                        if (element.getName().toString().equals("QUEUE")) {
                            Iterator attributes = element.getAttributes();
                            while (attributes.hasNext()) {
                                Attribute attr = (Attribute) attributes.next();
                                if (attr.getName().toString().equals("ID")) {
                                    if (attr.getValue() != null) {
                                        if (getOnlyTransactionNumber) {
                                            transactionNumber = attr.getValue();
                                            receivedTransactioNumber = null;
                                        }
                                    }
                                }
                                if (attr.getName().toString().equals("PROT")) {
                                    if (attr.getValue() != null) {
                                        if (attr.getValue().equalsIgnoreCase("smb")) {
                                            protocol = new SmbTransportProtocol(nbtConverter, aclDirectoryStorage);
                                        } else if (attr.getValue().equalsIgnoreCase("LF")) {
                                            protocol = new LocalFileTransportProtocol();
                                        } else {
                                            throw new UnknownTransportProtocolException(attr.getName().toString());
                                        }
                                    }
                                }
                            }
                        } else if (element.getName().toString().equals("LOCALQUEUEDELETED")) {
                            if (parser.hasNext()) {
                                XMLEvent nextEvent = parser.nextEvent();
                                if (nextEvent.getEventType() == XMLStreamConstants.CHARACTERS) {
                                    Characters characters = nextEvent.asCharacters();
                                    if (!characters.isWhiteSpace()) {
                                        receivedTransactioNumber = characters.getData();
                                    }
                                }
                            }
                        } else if ((element.getName().toString().equals("RM")) || (element.getName().toString().equals("RMD")) || (element.getName().toString().equals("WF")) || (element.getName().toString().equals("ALL"))) {
                            if (parser.hasNext()) {
                                addTagToCommandQueue(parser.nextEvent(), element.getName().toString());
                            }
                        }
                        loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "START_ELEMENT:" + element.getName());
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "END_ELEMENT:");
                        break;
                }
            }
        } catch (XMLStreamException ex) {
            loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
        }
    }

    public int getCommandQueueSize() {
        if (commandQueue == null) {
            return 0;
        } else {
            return commandQueue.getQsize();
        }
    }

    /**
     *
     * @return Command
     */
    public Command getCommandFromQueue() {
        Object result = commandQueue.receive();
        if (result instanceof Command) {
            return (Command) result;
        } else {
            return null;
        }
    }

    /**
     * 
     * @param xmlEvent
     * @param xmlCommand
     * @throws SambaMsgServerUrlException
     */
    public void addTagToCommandQueue(XMLEvent xmlEvent, String xmlCommand) throws SambaMsgServerUrlException {
        Command command = null;
        if (xmlEvent == null || xmlCommand == null) {
            throw new IllegalArgumentException();
        } else {
            Characters characters = xmlEvent.asCharacters();
            String line = characters.getData();
            if (xmlEvent.getEventType() == XMLStreamConstants.CHARACTERS) {
                if ((line.startsWith("smb")) || (line.startsWith("SMB"))) {
                    try {
                        SmbFile smbFile = new SmbFile(line);
                        int pathLength = smbFile.getPath().length();
                        int fileLength = smbFile.getName().length();
                        String filePath = smbFile.getPath().substring(0, pathLength - fileLength);
                        String fileName = smbFile.getName();
                        command = new Command(config, sqlFileDeleter, xmlCommand, filePath, fileName, protocol, null);
                    } catch (MalformedURLException ex) {
                        loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                    }
                }
            } else {
                if ((protocol.equals("smb")) || (protocol.equals("SMB"))) {
                    throw new SambaMsgServerUrlException();
                } else {
                    File file = new File(line);
                    int pathLength = file.getPath().length();
                    int fileLength = file.getName().length();
                    String filePath = file.getPath().substring(0, pathLength - fileLength);
                    String fileName = file.getName();
                    command = new Command(config, sqlFileDeleter, xmlCommand, filePath, fileName, protocol, null);
                }
            }
            if (command != null) {
                commandQueue.send(command);
            }
        }
    }

    private void modifyQueue() {
        rawMsgQueue.insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    }

    public boolean validateXML(String xmlToValidate) throws NoXSDFileFoundException {
        final boolean METHODDEBUG = true;
        if (METHODDEBUG) {
            loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "xmlToValidate:" + xmlToValidate);
        }
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        String xsdFileName = config.getSambaIndexerEventXSD();
        if (METHODDEBUG) {
            loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "validating against:" + xsdFileName);
        }
        if (xsdFileName != null) {
            File schemaLocation = new File(xsdFileName);
            if (schemaLocation.exists()) {
                try {
                    Schema schema = factory.newSchema(schemaLocation);
                    Validator validator = (Validator) schema.newValidator();
                    Source source = new StreamSource(new StringReader(xmlToValidate));
                    validator.validate(source);
                    loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "XML is valid");
                    return true;
                } catch (SAXParseException ex) {
                    if (METHODDEBUG) {
                        loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, "XML:" + xmlToValidate);
                        loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                    }
                    loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "XML is NOT valid");
                    return false;
                } catch (SAXException ex) {
                    if (METHODDEBUG) {
                        loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, "XML:" + xmlToValidate);
                        loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                    }
                    loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "XML is NOT valid");
                    return false;
                } catch (IOException ex) {
                    return false;
                }
            } else {
                throw new NoXSDFileFoundException("FileName:" + xsdFileName);
            }
        } else {
            throw new NoXSDFileFoundException("please check the config File:" + config.getConfigFile().getAbsolutePath());
        }
    }
}
